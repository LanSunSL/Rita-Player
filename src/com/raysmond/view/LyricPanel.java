package com.raysmond.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.raysmond.lyric.LRCSearchResult;
import com.raysmond.lyric.LRCUtil;
import com.raysmond.lyric.LyricFileParser;
import com.raysmond.lyric.LyricFileParserImpl;
import com.raysmond.lyric.LyricLine;
import com.raysmond.song.Song;
import com.raysmond.util.Util;

/**
 * �����ʾ��� ��Ҫ��������ʾ��ʡ������ͬ����ʾ�ġ� ���ӹ��ܣ���ʱ���ƥ�䡢�������ƥ�䣨�ٶ����֣�
 * 
 * @author Jiankun Lei
 * 
 */
public class LyricPanel extends JPanel implements Runnable, ActionListener {

	private static final long serialVersionUID = 7991482538335656273L;

	// ��ǰ���ŵĸ���
	private Song song = null;
	private int currentLine = 0;
	
	public final Pattern pattern = Pattern.compile("(?<=//[).*?(?=//])");
	static RandomAccessFile in = null;

	SimpleAttributeSet bSet = null;

	private ArrayList<JLabel> lyrics = new ArrayList<JLabel>();
	private ArrayList<String> times = new ArrayList<String>();

	private Color hoverColor = new Color(201, 13, 13);
	private Color defaultColor = Color.BLACK;
	private static final Font defaultFont = new Font("Microsoft YaHei",
			Font.PLAIN, 14);
	private static final Font hoverFont = new Font("Microsoft YaHei",
			Font.PLAIN, 16);

	private JPopupMenu popMenu = new JPopupMenu();
	private JMenuItem itemSearchLRC = new JMenuItem("Search lyric");

	Thread moveThread = null;

	// ����Ƿ��Ѿ�����ı�ʶ��û�����룬��ֹ��ʵ���ʾ���ƶ��������У��������ƶ�
	private boolean lyricLoaded = false;
	// ����Ƿ���ʾ�ڽ����ϵı�ʶ��û����ʾ���ǰҪ��ֹ�е��ƶ�������������
	private boolean lyricShowFinished = false;

	JLabel labelTitle = new JLabel("����");
	JLabel labelArtist = new JLabel("����");
	JTextField fieldTitle = new JTextField(30);
	JTextField fieldArtist = new JTextField(30);
	JTable resultTable = new JTable();
	JDialog dialog = null;
	JButton butSearch = new JButton("����");
	ArrayList<LRCSearchResult> results = new ArrayList<LRCSearchResult>();

	public LyricPanel() {
		setLayout(null);
		setSize(600, 530 - 10);
		setBackground(null);
		setOpaque(false);
		init();
	}

	public void init() {
		popMenu.add(itemSearchLRC);
		itemSearchLRC.addActionListener(this);
		this.addMouseListener(new MouseHandler(this));
	}

	private class MouseHandler extends MouseAdapter {
		private JPanel parentPanel;

		public MouseHandler(JPanel parentPanel) {
			this.parentPanel = parentPanel;
		}

		public void mouseClicked(MouseEvent e) {
			if (e.getModifiersEx() == 256) { // ����һ�
				popMenu.show(parentPanel, e.getX(), e.getY());
			}
			if (e.getSource() == resultTable && e.getClickCount() == 2) { // ˫��
				int row = resultTable.getSelectedRow();
				LRCSearchResult result = results.get(row);
				System.out.println(result.getLrcText());
				String fileName = Song.getFileNameNoEx(song.getFileName()) + ".lrc";
				LRCUtil.saveLRC(Util.lyricDirPath,fileName, result.getLrcText());
				loadLocalLyric();
				if (lyrics.size() == 0) {
					loadInfoWhenNoLyricFound();
				} else {
					lyricLoaded = true;
					displayLyrics();
				}
			}
		}
	}

	public void showSong(Song song) {
		lyricLoaded = false;
		lyricShowFinished = false;
		setSong(song);
		loadLocalLyric();
		if (lyrics.size() == 0) {
			loadInfoWhenNoLyricFound();
			matchingLyricOnLine();
		} else {
			lyricLoaded = true;
			displayLyrics();
			lyricShowFinished = true;
		}
	}

	/**
	 * Load lyric file from the local file system
	 */
	public void loadLocalLyric() {
		String songNameNoEx = Song.getFileNameNoEx(song.getFileName());
		String fileName = Util.lyricDirPath + "/" + songNameNoEx + ".lrc";
		resetLyricPanel();
		LyricFileParser parser = new LyricFileParserImpl();
		List<LyricLine> lines = parser.parseLyric(fileName);
		if(null!=lines){
			for(LyricLine line: lines){
				JLabel label = new JLabel(line.getContent(),(int) JLabel.LEFT_ALIGNMENT);
				label.setFont(defaultFont);
				label.setPreferredSize(new Dimension(this.getWidth(), 30));
				label.setBackground(null);
				lyrics.add(label);
				times.add(String.valueOf(line.getTime()));
			}
		}
	}

	public void resetLyricPanel() {
		lyrics.clear();
		times.clear();
		currentLine = 0;
		this.removeAll();
		this.validate();
		this.setBackground(null);
	}

	public void displayLyrics() {
		if (!lyricLoaded)
			return;
		lyricShowFinished = false; // ��ֹ�ƶ��У�ֱ��ת��������ƶ�
		int currentY = 230;
		for (int i = 0; i < lyrics.size(); i++) {
			add(lyrics.get(i));
			lyrics.get(i).setBounds(0, currentY, this.getWidth(), 30);
			lyrics.get(i).setBackground(null);
			currentY += 30;
		}
		lyricShowFinished = true;
	}

	public void displayLyrics(int lineBegin) {
		if (!lyricLoaded)
			return;
		lyricShowFinished = false; // ��ֹ�ƶ��У�ֱ��ת��������ƶ�
		int currentY = 230;
		lyrics.get(lineBegin).setForeground(hoverColor);
		lyrics.get(lineBegin).setFont(hoverFont);
		lyrics.get(lineBegin).setBounds(0, currentY, this.getWidth(), 30);
		currentY += 30;
		for (int i = lineBegin + 1; i < lyrics.size(); i++) {
			lyrics.get(i).setForeground(defaultColor);
			lyrics.get(i).setBackground(null);
			lyrics.get(i).setBounds(0, currentY, this.getWidth(), 30);
			currentY += 30;
		}
		currentY = 230 - 30;
		for (int i = lineBegin - 1; i >= 0; i--) {
			lyrics.get(i).setForeground(defaultColor);
			lyrics.get(i).setBackground(null);
			lyrics.get(i).setBounds(0, currentY, this.getWidth(), 30);
			currentY -= 30;
		}
		lyricShowFinished = true;
	}

	/**
	 * �Զ�����ƥ���ʣ�������һ����ѵĸ��
	 */
	public void matchingLyricOnLine() {
		if (!isSetSong())
			return;
		JLabel labelSearching = new JLabel("��������ƥ���ʣ����Ե�...",
				(int) JLabel.LEFT_ALIGNMENT);
		labelSearching.setBounds(0, getHeight() / 2 - 30, getWidth(),50);
		labelSearching.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
		labelSearching.setForeground(new Color(121, 49, 0));

		this.removeAll();
		add(labelSearching);
		this.validate();
		this.setBackground(null);

		// ����һ���µ��߳�ȥ���������ظ��,�������̶߳���
		new Thread(new Runnable() {
			@Override
			public synchronized void run() {
				String title = song.getTitle();
				// �����TAG��Ϣ�л�ȡ�ı���Ϊ�գ���Ѹ����ļ������޺�׺����Ϊ���ҵĹؼ���
				if (title == null || title.isEmpty())
					title = Song.getFileNameNoEx(song.getFileName());
				String artist = song.getArtist();

				ArrayList<LRCSearchResult> results = LRCUtil.searchLRCFromBaidu(title, artist, 1);
				
				if (results != null && results.size() == 1) {
					String fileName = Song.getFileNameNoEx(song.getFileName()) + ".lrc";
					LRCUtil.saveLRC(Util.lyricDirPath, fileName, results.get(0).getLrcText());
					loadLocalLyric();
					if (lyrics.size() != 0) {
						lyricLoaded = true;
						displayLyrics();
						return;
					} 
				} 
				loadInfoWhenNoLyricFound();
			}
		}).start();
	}

	/**
	 * ���ݵ�ǰ���ŵ��ĺ������͸�������㵱ǰ��Ӧ�����ľ���
	 * 
	 * @param seekTime
	 * @return
	 */
	public int getLineSeekingTo(long seekTime) {
		int i = 0;
		for (; i < lyrics.size(); i++) {
			if (Integer.valueOf(times.get(i)) >= seekTime) {
				break;
			}
		}
		return i == 0 ? 0 : i - 1;
	}

	/**
	 * �������ڲ��ŵ�ʱ��λ�õ��������ʾλ��
	 * 
	 * @param seekNextTime
	 *            ��ǰ���ŵĺ�����
	 */
	public void tryToShowNextLine(long seekNextTime) {
		if (!lyricLoaded)
			return; // û�и�ʣ�����Ҫ����
		if (!lyricShowFinished)
			return; // ����Ѿ����룬����û����Ⱦ�ڽ����ϡ������ʱ�ƶ����ᵼ�¸�ʼ����һ��
		int lineToSeek = getLineSeekingTo(seekNextTime); // ��ȡ��Ҫת��������
		gotoLine(lineToSeek); // ת������
	}

	boolean isNeedToMove = true;

	/**
	 * �����кŵ��������ʾλ��
	 * 
	 * @param line
	 */
	public void gotoLine(int line) {
		if (lyrics == null || lyrics.size() == 0)
			return;
		int lineDistance = line - currentLine;
		if (line == 0 || lineDistance == 0) {
			// �����ǰ�ڵ�һ�������õ�һ����ɫ
			// ���ת����͵�ǰ����ͬ������ת��
			isNeedToMove = false;
			if (line == 0) {
				lyrics.get(0).setForeground(hoverColor);
				lyrics.get(0).setFont(LyricPanel.hoverFont);
			}
			return;
		}

		if (lineDistance == 1) { // �����ƶ�һ��
			isNeedToMove = true;
			currentLine = line;
			moveToNextLine();
		} else { // ֱ��ת������
			isNeedToMove = false;
			if (moveThread != null)
				moveThread.stop();
			moveThread = null;
			currentLine = line;
			displayLyrics(line);
		}
	}

	public void moveToNextLine() {
		lyrics.get(currentLine - 1).setForeground(defaultColor);
		lyrics.get(currentLine - 1).setFont(defaultFont);
		lyrics.get(currentLine).setForeground(hoverColor);
		lyrics.get(currentLine).setFont(hoverFont);
		if (moveThread == null || !moveThread.isAlive()) {
			moveThread = new Thread(this);
		}
		if (moveThread.isAlive()) {
			System.out.println("�һ��ţ�����ɱ���ң�");
		} else
			moveThread.start();
	}

	public void loadInfoWhenNoLyricFound() {
		if (!isSetSong())
			return;
		this.removeAll();

		JLabel title = new JLabel(Song.getFileNameNoEx(song.getFileName()),
				(int) JLabel.LEFT_ALIGNMENT);
		title.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
		title.setForeground(new Color(121, 49, 0));

		JLabel artist = new JLabel(song.getArtist(),
				(int) JLabel.LEFT_ALIGNMENT);
		artist.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
		artist.setForeground(new Color(121, 49, 0));

		JPanel titlePane = new JPanel();
		FlowLayout flow = new FlowLayout();
		flow.setAlignment(FlowLayout.LEFT);
		titlePane.setLayout(flow);
		titlePane.setOpaque(false);

		System.out.println(song.getArtist());
		titlePane.setBounds(0, this.getHeight() / 2 - 60, this.getWidth(), 100);

		titlePane.add(title);
		titlePane.add(artist);

		title.setPreferredSize(new Dimension(this.getWidth() - 10, 40));
		artist.setPreferredSize(new Dimension(this.getWidth() - 10, 40));
		add(titlePane);

		this.validate();
		this.setBackground(null);
	}

	@Override
	public synchronized void run() {
		if (Thread.currentThread() == moveThread) {
			int moveGap = 5;
			int countGap = 0;
			while (true/* &&isNeedToMove */) {
				for (int i = 0; i < lyrics.size(); i++) {
					lyrics.get(i).setLocation(lyrics.get(i).getX(),
							lyrics.get(i).getY() - moveGap);
				}
				countGap += moveGap;
				if (countGap >= 30)
					break;
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean isSetSong() {
		if (song != null)
			return true;
		else
			return false;
	}

	public Song getSong() {
		return song;
	}

	public void setSong(Song song) {
		this.song = song;
	}

	public void showSearchLRCDialog() {
		String title = song.getTitle();
		// �����TAG��Ϣ�л�ȡ�ı���Ϊ�գ���Ѹ����ļ������޺�׺����Ϊ���ҵĹؼ���
		if (title == null || title.isEmpty())
			title = Song.getFileNameNoEx(song.getFileName());

		String artist = song.getArtist();

		if (dialog == null) {
			dialog = new JDialog();
			dialog.setSize(405, 300);
			dialog.setTitle("�������");
			dialog.setLayout(null);
			dialog.setLocationRelativeTo(this);
			JScrollPane pane = new JScrollPane(resultTable);

			labelTitle.setBounds(5, 5, 30, 24);
			fieldTitle.setBounds(40, 5, 120, 24);
			labelArtist.setBounds(165, 5, 30, 24);
			fieldArtist.setBounds(200, 5, 110, 24);
			butSearch.setBounds(315, 5, 70, 24);
			pane.setBounds(5, 35, 380, 220);

			dialog.add(labelTitle);
			dialog.add(fieldTitle);
			dialog.add(labelArtist);
			dialog.add(fieldArtist);
			dialog.add(pane);
			dialog.add(butSearch);

			butSearch.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					results.clear();
					results = LRCUtil.searchLRCFromBaidu(fieldTitle.getText()
							.trim(), fieldArtist.getText().trim(), 10);
					resultTable.setModel(LRCUtil.getResultTableModel(results));
					resultTable.validate();
					resultTable.addMouseListener(new MouseHandler(null));
				}
			});
		}
		fieldTitle.setText(title);
		fieldArtist.setText(artist);
		dialog.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == itemSearchLRC) {
			showSearchLRCDialog();
		}
	}
}
