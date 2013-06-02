package com.raysmond.tag;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * ���ڻ�ȡ����TAG��Ϣ�Ľӿ�
 * @author Raysmond
 *
 */
public interface TagInfo extends Serializable {
	
	public void load(InputStream input);
	public void load(File input) throws UnsupportedAudioFileException,IOException;
	public void load(URL input);
	
	public String getType();
	
	public String getPlayTime();
	
	public String getTitle();
	
	public String getArtist();
	
	public String getAlbum();
	
	public String getTrack();
	
	public String getYear();
	
	public String getComment();
	
	public String getLocation();
	
	public String getFileName();
	
	public String getChannels();
	
	public int getBitRate();
	
	public int getSampleRate();
	
	public String getFormat();
}
