package com.raysmond.lyric;

/**
 * �����
 * ������������ʹ��
 * @author Jiankun Lei
 *
 */
public class LRCSearchResult {
	private String songTitle = null;	//����
	private String songArtist = null;	//����
	private String downloadUrl = null;	//��������
	private String lrcText = null;		//�������
	
	public LRCSearchResult(){
		
	}
	
	public String getSongTitle() {
		return songTitle;
	}


	public void setSongTitle(String songTitle) {
		this.songTitle = songTitle;
	}


	public String getSongArtist() {
		return songArtist;
	}


	public void setSongArtist(String songArtist) {
		this.songArtist = songArtist;
	}


	public String getDownloadUrl() {
		return downloadUrl;
	}


	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}


	public String getLrcText() {
		return lrcText;
	}


	public void setLrcText(String lrcText) {
		this.lrcText = lrcText;
	}


	public static void main(String[] args) {

	}

}