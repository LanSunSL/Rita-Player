package com.raysmond.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class MyScrollBarUI extends BasicScrollBarUI {
	private Color frameColor = new Color(145, 105, 55);

	public Dimension getPreferredSize(JComponent c) {
		return new Dimension(8, 8);
	}

	// ����
	public void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
		super.paintThumb(g, c, thumbBounds);
		int tw = thumbBounds.width;
		int th = thumbBounds.height;
		// �ض�ͼ�������ĵ�ԭ�㣬���һ��Ҫд����Ȼ������϶�����ʱ���鲻��������
		g.translate(thumbBounds.x, thumbBounds.y);

		Graphics2D g2 = (Graphics2D) g;
		GradientPaint gp = null;
		if (this.scrollbar.getOrientation() == JScrollBar.VERTICAL) {
			gp = new GradientPaint(0, 0, new Color(200, 200, 200), tw, 0,
					new Color(255, 255, 255));
		}
		if (this.scrollbar.getOrientation() == JScrollBar.HORIZONTAL) {
			gp = new GradientPaint(0, 0, new Color(242, 222, 198), 0, th,
					new Color(207, 190, 164));
		}
		g2.setPaint(gp);
		g2.fillRoundRect(0, 0, tw - 1, th - 1, 5, 5);
		g2.setColor(frameColor);
		g2.drawRoundRect(0, 0, tw - 1, th - 1, 5, 5);

	}

	// ����
	public void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
		super.paintTrack(g, c, trackBounds);
	}

	// �ػ浱��������������ϻ�����ť֮�������
	protected void paintDecreaseHighlight(Graphics g) {
	}

	// �ػ浱��������������»����Ұ�ť֮�������

	protected void paintIncreaseHighlight(Graphics g) {
	}

	protected JButton createIncreaseButton(int orientation) {
		return new BasicArrowButton(orientation) {
			// �ػ水ť�����Ǳ��
			public void paintTriangle(Graphics g, int x, int y, int size,
					int direction, boolean isEnabled) {
				Graphics2D g2 = (Graphics2D) g;
				GradientPaint gp = null;
				Image arrowImg = null;
				switch (this.getDirection()) {
				case BasicArrowButton.SOUTH:
					gp = new GradientPaint(0, 0, new Color(242, 222, 198),
							getWidth(), 0, new Color(207, 190, 164));
					arrowImg = new ImageIcon("south.png").getImage();
					break;
				case BasicArrowButton.EAST:
					gp = new GradientPaint(0, 0, new Color(242, 222, 198), 0,
							getHeight(), new Color(207, 190, 164));
					arrowImg = new ImageIcon("east.png").getImage();
					break;
				}
				g2.setPaint(gp);
				g2.fillRect(0, 0, getWidth(), getHeight());
				g2.setColor(frameColor);
				g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
				g2.drawImage(arrowImg, (getWidth() - 2) / 2 - 5,
						(getHeight() - 2) / 2 - 5, 13, 13, null);
			}
		};
	}

	protected JButton createDecreaseButton(int orientation) {
		return new BasicArrowButton(orientation) {
			public void paintTriangle(Graphics g, int x, int y, int size,
					int direction, boolean isEnabled) {
				Graphics2D g2 = (Graphics2D) g;
				GradientPaint gp = null;
				Image arrowImg = null;
				switch (this.getDirection()) {
				case BasicArrowButton.NORTH:
					gp = new GradientPaint(0, 0, new Color(242, 222, 198),
							getWidth(), 0, new Color(207, 190, 164));
					arrowImg = new ImageIcon("south.png").getImage();
					break;
				case BasicArrowButton.WEST:
					gp = new GradientPaint(0, 0, new Color(242, 222, 198), 0,
							getHeight(), new Color(207, 190, 164));
					arrowImg = new ImageIcon("east.png").getImage();
					break;
				}
				g2.setPaint(gp);
				g2.fillRect(0, 0, getWidth(), getHeight());
				g2.setColor(frameColor);
				g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
				g2.drawImage(arrowImg, (getWidth() - 2) / 2 - 5,
						(getHeight() - 2) / 2 - 5, 13, 13, null);
			}
		};
	}
}
