package com.raysmond.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.MalformedURLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.raysmond.player.BasicController;
import com.raysmond.player.BasicPlayer;
import com.raysmond.player.BasicPlayerException;
import com.raysmond.song.Song;
import com.raysmond.util.Util;
public class PlayPanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 6937676353674237733L;
	
	private BasicController player = new BasicPlayer();
	private enum PlayState{PLAYING,PAUSE,STOP};
	private PlayState state = PlayState.STOP;
	private Song song = null;										//��ǰ���Ÿ���
	private File file = null;
	public static int WIDTH = 300;						    //���������
	public static int HEIGHT = 200;							//�������߶�
	
	//��������������
	PlayListPanel panelPlayList = null;
	LyricPanel panelLyric = null;
	PlayingSongPanel panelSong = new PlayingSongPanel();
	
	private JSlider slider = null;								//���Ž�����
	private JSlider volumeSlider = null;
	private float volume = 1.0F;
	private boolean noVolume = false;
	private SliderListener sliderListener;						//������������
	private JLabel labelCurrentTime = new JLabel("00:00");		//��ǰ����ʱ��
    private JLabel labelTotalTime = new JLabel("00:00");		//��ǰ������ʱ��
    private boolean ajusted = false;
	private int ajustedPos = 0;
	private Color timeColor = Util.getThemeDefaultColor();		//����ʱ�����ֵ���ɫ
	
	private JButton butPlayOrPause = new JButton(Util.getImageIcon("play.png"));
	private JButton butPlayPrev = new JButton(Util.getImageIcon("prev.png"));
	private JButton butPlayNext = new JButton(Util.getImageIcon("next.png"));
	private JButton butLike = new JButton(Util.getImageIcon("like.png"));
	private JButton butVolume = new JButton(Util.getImageIcon("volume.png"));
	
	
	public PlayPanel(){
		setOpaque(false);   
		init();
		setSize(WIDTH,HEIGHT);
		setLayout(null);
	}
	
	public void init(){
		initButtonStyle();
		initSlider();
		setLocations();
		addComponents();
		addListener();
		initStyle();
	}
	
	public void initStyle(){
		labelCurrentTime.setForeground(timeColor);
		labelTotalTime.setForeground(timeColor);
	}
	public void addListener(){
		butPlayOrPause.addActionListener(this);
		butPlayPrev.addActionListener(this);
		butPlayNext.addActionListener(this);
		butLike.addActionListener(this);
		butVolume.addActionListener(this);
	}
	public void setLocations(){
		
		int width = Util.defaultImageWidth;
		int height = Util.defaultImageHeight;
		
		panelSong.setBounds(0,0,300,105);
		slider.setBounds(10,105,280,15);
		labelCurrentTime.setBounds(10,125,40,20);
		labelTotalTime.setBounds(300-40,125,40,20);
		butPlayPrev.setBounds(70, 130, width, height);
		butPlayOrPause.setBounds(110, 130, width, height);
		butPlayNext.setBounds(150, 130, width, height);
		butLike.setBounds(190,130, width, height);
		butVolume.setBounds(230,147,16,16);
		volumeSlider.setBounds(243, 150, 50, 12);
	}
	
	public void addComponents(){
		add(panelSong);
		add(butPlayOrPause);
		add(butPlayPrev);
		add(butPlayNext);
		add(butVolume);
		add(butLike);
		add(slider);
		add(labelCurrentTime);
		add(labelTotalTime);
		add(volumeSlider);
	}
	public void initSlider(){
		slider = new JSlider(SwingConstants.HORIZONTAL, 0, 1000, 0);
		slider.setFocusable(false);
		slider.setOpaque(false);
		slider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		volumeSlider = new JSlider(SwingConstants.HORIZONTAL,0,100,0);
		volumeSlider.setFocusable(false);
		volumeSlider.setOpaque(false);
		volumeSlider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		volumeSlider.setValue((int)(Util.config.getVolume()*100.0));
		volumeSlider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent arg0) {
				float v = (float) (volumeSlider.getValue()/100.0);
				try {
					player.setGain(v);
				} catch (BasicPlayerException e) {
					System.err.println("Set volume error.");
					e.printStackTrace();
				}
				Util.config.setVolume(v);
				Util.config.writeConfig();
			}
			
		});
	}
	
	public void setPlayListPanel(PlayListPanel panelPlayList){
		this.panelPlayList = panelPlayList;
	}
	public void setPanelLyric(LyricPanel lyricPanel){
		this.panelLyric = lyricPanel;
	}
	
	/**
	 * ���ð�ť���Ϊ͸��
	 * @param button
	 */
	public void setButtonStyle(JButton button){
		button.setBackground(null);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setContentAreaFilled(false);
		button.setFocusable(true);
	}
	
	public void initButtonStyle(){
		setButtonStyle(butPlayPrev);
		setButtonStyle(butPlayNext);
		setButtonStyle(butPlayOrPause);
		setButtonStyle(butLike);
		setButtonStyle(butVolume);
		
		butVolume.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent arg0) {
				butVolume.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				if(noVolume) butVolume.setIcon(Util.getImageIcon("volume_noHover.png"));
				else butVolume.setIcon(Util.getImageIcon("volumeHover.png"));
			}
			public void mouseExited(MouseEvent arg0) {
				butVolume.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				if(noVolume) butVolume.setIcon(Util.getImageIcon("volume_no.png"));
				else butVolume.setIcon(Util.getImageIcon("volume.png"));
			}
			});
		butPlayOrPause.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent arg0) {
				butPlayOrPause.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				if(state==PlayState.PLAYING){
					butPlayOrPause.setIcon(Util.getImageIcon("pauseHover.png"));
				}
				else butPlayOrPause.setIcon(Util.getImageIcon("playHover.png"));
			}
			public void mouseExited(MouseEvent arg0) {
				butPlayOrPause.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				if(state==PlayState.PLAYING){
					butPlayOrPause.setIcon(Util.getImageIcon("pause.png"));
				}
				else butPlayOrPause.setIcon(Util.getImageIcon("play.png"));
			}
			});
		butPlayPrev.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent arg0) {
				butPlayPrev.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				butPlayPrev.setIcon(Util.getImageIcon("prevHover.png"));
			}
			public void mouseExited(MouseEvent arg0) {
				butPlayPrev.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				butPlayPrev.setIcon(Util.getImageIcon("prev.png"));
			}
			});
		butPlayNext.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent arg0) {
				butPlayNext.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				butPlayNext.setIcon(Util.getImageIcon("nextHover.png"));
			}
			public void mouseExited(MouseEvent arg0) {
				butPlayNext.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				butPlayNext.setIcon(Util.getImageIcon("next.png"));
			}
			});
		butLike.addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent arg0) {
				butLike.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				butLike.setIcon(Util.getImageIcon("likeHover.png"));
			}
			public void mouseExited(MouseEvent arg0) {
				butLike.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				butLike.setIcon(Util.getImageIcon("like.png"));
			}
			});
	}
	
	/**
	 * �ṩ����粥��һ�׸�������ں���,���ٵ���beginPlay���в���
	 * @param song
	 */
	public void play(Song song) throws MalformedURLException, Exception{
		System.out.println("play song \"" + Song.getFileNameNoEx(song.getFileName()) + "\" begin.");
		this.song = song;
		play();
	}
	
	public Song getSong(){
		return song;
	}
	
	public boolean isStoped(){
		return state==PlayState.STOP;
	}
	public void beforePlay(){
		if(state==PlayState.PLAYING||state==PlayState.PAUSE){
			try {
				player.stop();
			} catch (BasicPlayerException e) {
				//e.printStackTrace();
			}
		}
		state = PlayState.STOP;
		
		labelCurrentTime.setText("00:00");
		labelTotalTime.setText("00:00");
		toggleProgressListen();
		ajustedPos = 0;
		slider.setValue(0);
	}
	
	/**
	 * ��ʼ����һ��ѡ��ĸ���
	 */
	public void play() {
		if(song==null) return;
		beforePlay();
		file = new File(song.getLocation());
		if(!file.exists()){
			System.err.println("The song file: " +song.getLocation() + " not exists.");
			return;
		}
		
		try {
			player.open(file);
			player.play();
			player.setGain(volumeSlider.getValue()/100.0);
		} catch (BasicPlayerException e) {
			//e.printStackTrace();
		}
		state = PlayState.PLAYING;
		adjustPlayButton();
		
		toggleProgressListen();
		showLyric();
		panelSong.showInfo(this.song);
		
	}
	
	public void pause(){
		if(state==PlayState.STOP) return;
		try {
			player.pause();
		} catch (BasicPlayerException e) {
			//e.printStackTrace();
		}
		state = PlayState.PAUSE;
		toggleProgressListen();
	}
	public void resume(){
		if(state==PlayState.PAUSE){
			try {
				player.resume();
			} catch (BasicPlayerException e) {
				//e.printStackTrace();
			}
			state = PlayState.PLAYING;
			toggleProgressListen();
		}
	}
	public void adjustPlayButton(){
		if (state==PlayState.PLAYING) {
			butPlayOrPause.setIcon(Util.getImageIcon("pause.png"));
		} else {
			butPlayOrPause.setIcon(Util.getImageIcon("play.png"));
		}
	}
	/**
	 * �ѽ������ļ������Ƴ�
	 */
	private void removeSliderChangeListeners(){
		ChangeListener[] listeners = slider.getChangeListeners();
		for(ChangeListener aListener: listeners){
			slider.removeChangeListener(aListener);
		}
	}
	/**
	 * �����������벻��������
	 */
	private synchronized void toggleProgressListen(){
		if(state==PlayState.STOP||state==PlayState.PAUSE){
			if(sliderListener!=null){
				sliderListener.stopListen();
				sliderListener = null;
				removeSliderChangeListeners();
			}
		}
		else{
			removeSliderChangeListeners();
			slider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent event) {
					JSlider source = (JSlider) event.getSource();
					if(source.getValueIsAdjusting()){
						System.out.println("Adjusting progress..");
						ajusted = true;
						return;
					}
					double progress = source.getValue() / 1000.0;
					if(ajusted)
					try {
						ajustedPos = source.getValue();
						player.seek((long)(progress*song.getSize()));
					} catch (BasicPlayerException e) {
						//e.printStackTrace();
					}
					ajusted = false;
				}
			});
			if(sliderListener!=null){
				sliderListener.stopListen();
				sliderListener = null;
			}
			sliderListener = new SliderListener();
			sliderListener.startListen();
		}
	}
	
	
	public void showLyric(){
		if(song==null) return;
		if(Util.isLyricPanelClosed){
			System.out.println("��ǰ�ѹرո����ʾ��");
			return;
		} 
		this.panelLyric.showSong(song);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource()==butPlayOrPause){
			if(state==PlayState.PLAYING){
				pause();
			}
			else if(state==PlayState.PAUSE){
				resume();
			}
			else panelPlayList.playSelectedSong();
		}
		else if(arg0.getSource()==butPlayPrev){
			panelPlayList.playPrev();
		}
		else if(arg0.getSource()==butPlayNext){
			panelPlayList.playNext();
		}
		else if(arg0.getSource()==butLike){
			panelPlayList.handleLike(song);
		}
		else if(arg0.getSource()==butVolume){
			if(noVolume){
				noVolume = false;
				butVolume.setIcon(Util.getImageIcon("volumeHover.png"));
				try {
					player.setGain(volumeSlider.getValue()/100.0);
				} catch (BasicPlayerException e) {
					e.printStackTrace();
				}
			}
			else{
				noVolume = true;
				butVolume.setIcon(Util.getImageIcon("volume_noHover.png"));
				try {
					player.setGain(0);
				} catch (BasicPlayerException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * �������ļ������࣬����Ϊ�ڲ�����Ҫ�Ǳ��Ѽ�������slider֮��Ļ�����
	 * @author Jiankun Lei
	 *
	 */
	private class SliderListener implements Runnable {
		private  Thread listenerThread = null;		
		
		private void startListen() {
			stopListen();
			listenerThread = new Thread(this, "listenerThread");
			listenerThread.start();
		}

		private void stopListen() {
			//�ն˼����̣߳�ȷ���䰲ȫ�˳�
			if(listenerThread!=null){
				listenerThread.interrupt(); 
			}
			listenerThread = null;
		}
		public synchronized void completeListen() {
			adjustPlayButton();
			slider.setValue(0);
			stopListen();
		}
		
		@Override
		public synchronized void run() {
			long totalMicrosecond = song.getTrackLength() * 1000000;
			long currentMicrosecond = 0;
			boolean totalFlag = false;
			while (true) {
				try {
					Thread.sleep(100);
					currentMicrosecond =  (long) (player.getMicrosecondPosition() + ajustedPos*totalMicrosecond/1000.0);
					int z = (int) (1000.0 * currentMicrosecond / totalMicrosecond);
					if (player.isCompleted()) {
						System.out.println("ֹͣ����: " + song.getTitle());
						state = PlayState.STOP;;
						completeListen();
						panelPlayList.controlThreadAction();  
						return;
					}
					if(ajusted) continue;
					slider.setValue(z);
					labelCurrentTime.setText(Util.formatMicroseconds(currentMicrosecond));
					if(!totalFlag){
						labelTotalTime.setText(Util.formatMicroseconds(totalMicrosecond));
						totalFlag = true;
					}
					if(!Util.isLyricPanelClosed()) 
					panelLyric.tryToShowNextLine(currentMicrosecond/1000);	
						
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}
	
	/**
	 * �ı�����ʱ����
	 */
	public void changeTheme(){
		if(labelCurrentTime!=null) labelCurrentTime.setForeground(Util.getThemeDefaultColor());
		if(labelTotalTime!=null) labelTotalTime.setForeground(Util.getThemeDefaultColor());
	}
}
