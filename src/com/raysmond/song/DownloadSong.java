package com.raysmond.song;

/**
 * �����࣬�������������
 * @author Jiankun Lei
 *
 */
public class DownloadSong {
	private String downloadUrl = null;		//mp3��������
	private String fileName = null;		//�ļ���
	private double fileSize = 0;			//�ļ���С 
	private String title = null;			//����
	private String artist = null;			//����
	private String albumTitle = null;		//ר����
	
	/**
	 * ����һ������ʵ��
	 */
	public DownloadSong(String title,String artist,String fileName,double fileSize,String downloadUrl,String albumTitle){
		this.downloadUrl = downloadUrl;
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.title = title;
		this.artist = artist;
		this.albumTitle = albumTitle;
	}
	
	//getters and setters
	
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public double getFileSize() {
		return fileSize;
	}
	public void setFileSize(double fileSize) {
		this.fileSize = fileSize;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbumTitle() {
		return albumTitle;
	}

	public void setAlbumTitle(String albumTitle) {
		this.albumTitle = albumTitle;
	}
}
