package com.raysmond.song;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.text.DecimalFormat;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.raysmond.tag.JAudioTagger;
import com.raysmond.tag.TagInfo;

/**
 * ������
 * ���������ļ�����ϸ��Ϣ
 * @author Jiankun Lei
 *
 */
public class Song implements TagInfo,Comparable {
	
	protected String fileName = null;	    //������Ӧ���ص��ļ���
	protected String title = null;			//����
	protected String artist = null;		//����
	protected String album = null;			//ר����
	protected String year = null;			//�������
	protected String comment = null;   	//��ע
	protected String type = null;      	//�ļ�����
	protected String location = null;  	//��������·��
	protected String track = null;     	//track
	protected long trackLength = -1;		//track���ȣ�ʱ����
	protected String duration = "00:00";  	//����ʱ�����ַ�����
	protected long size = -1;          	//������С
	protected int bitRate = -1; 			//������
	protected String channel = null;		//
	protected String format = null;		//�������ͣ���ͬ�����Ͷ�Ӧ��ÿ֡��������һ��
	protected int sampleRate = -1;			//ÿ֡������
	protected boolean like = false;		//�Ƿ��עΪϲ��
	
	private static final long serialVersionUID = -1579945086126849773L;
	
	public Song(){}
	
	@Override
	public void load(InputStream input) {
		
	}

	/**
	 * ���ļ���ȡ������Ϣ����ʱֻ����Ϊmp3�ļ�
	 */
	@Override
	public void load(File input) throws IOException, UnsupportedAudioFileException  {
        if (null == input || !input.exists()||!input.getName().endsWith(".mp3"))
        {
            return;
        }
        //�����ļ������Ϣ
        size = input.length();
        location = input.getPath();
        fileName = input.getName();
        
        //����TAG��Ϣ
        loadTag(input);
	}
	
	/**
	 * ���ļ���ȡTAG��Ϣ
	 * @param input
	 */
	public void loadTag(File input){
		JAudioTagger tag = null;
		if(input.getName().endsWith(".mp3")){
			tag = new JAudioTagger();
			tag.loadMP3File(input);
			title = tag.getTitle();
			artist = tag.getArtist();
			album = tag.getAlbum();
			year = tag.getYear();
			comment = tag.getComment();
			track = tag.getTrack();
			bitRate = tag.getBitRate();
			channel = tag.getChannels();
			trackLength = tag.getTrackLength();
			duration = tag.getPlayTime();
			sampleRate = tag.getSampleRate();
			format = tag.getFormat();
		}
		
	}
	
	
	/**
	 * ��һ������TAG��Ϣ��ֱ�ӴӸ������ж�ȡ�ֽ�
	 * @param input
	 * @throws IOException
	 */
	public void loadInfo(File input) throws IOException{
		byte[] data = new byte[128];
		RandomAccessFile ran = new RandomAccessFile(input, "r");
        ran.seek(ran.length() - 128);
        ran.read(data);
		if (data.length != 128) {
			throw new RuntimeException("���ݳ��Ȳ��Ϸ�:" + data.length);
			}
		String tag = new String(data, 0, 3);
		if (tag.equalsIgnoreCase("TAG")) {
			title = new String(data, 3, 30).trim();
			artist = new String(data, 33, 30).trim();
			album = new String(data, 63, 30).trim();
			year = new String(data, 93, 4).trim();
			comment = new String(data, 97, 28).trim();
			//r1 = data[125];
			//r2 = data[126];
			//r3 = data[127];
			} else {}
	}
	
	/**
	 * Java�ļ����� ��ȡ�ļ���չ��
	 */
    public static String getExtensionName(String filename) { 
        if ((filename != null) && (filename.length() > 0)) { 
            int dot = filename.lastIndexOf('.'); 
            if ((dot >-1) && (dot < (filename.length() - 1))) { 
                return filename.substring(dot + 1); 
            } 
        } 
        return filename; 
    } 
    /**
     * Java�ļ����� ��ȡ������չ�����ļ���
     */
    public static String getFileNameNoEx(String filename) { 
        if ((filename != null) && (filename.length() > 0)) { 
            int dot = filename.lastIndexOf('.'); 
            if ((dot >-1) && (dot < (filename.length()))) { 
                return filename.substring(0, dot); 
            } 
        } 
        return filename; 
    } 
    
	public String toString(){
		StringBuffer song = new StringBuffer();
		song.append("title: " + getTitle() + "\n");
		song.append("artist: " + getArtist() + "\n");
		song.append("album:" + getAlbum() + "\n");
		song.append("year:" + getYear() + "\n");
		song.append("comment:" + getComment() + "\n");
		song.append("duration: " + getPlayTime() + "\n");
		song.append("size: " + getSize() + "\n");
		song.append("track length: " + getTrackLength() + "\n");
		song.append("sample rate: " + getSampleRate() + "\n");
		song.append("format: " + getFormat() + "\n");
		return song.toString();
	}

	public long getSize() {
		return size;
	}
	public double getSizeByMb(){
		DecimalFormat df = new DecimalFormat("##.00");   
		return Double.parseDouble(df.format((double)this.getSize()/(1024.0*1024.0)));
	}
	@Override
	public void load(URL input) {
		
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getPlayTime() {
		return duration;
	}
	
	public long getTrackLength(){
		return trackLength;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getArtist() {
		return artist;
	}

	@Override
	public String getAlbum() {
		return album;
	}

	@Override
	public String getTrack() {
		return track;
	}

	@Override
	public String getYear() {
		return year;
	}

	@Override
	public String getComment() {
		return comment;
	}

	public String getChannels() {
		return channel;
	}

	@Override
	public int getBitRate() {
		return bitRate;
	}
	
	@Override
	public String getLocation(){
		return location;
	}
	
	@Override
	public String getFileName(){
		return fileName;
	}
    
	@Override
	public int getSampleRate() {
		return sampleRate;
	}

	@Override
	public String getFormat() {
		return format;
	}
	
	public boolean isLike() {
		return like;
	}

	public void setLike(boolean like) {
		this.like = like;
	}
	
	public boolean equals(Song song){
		if(this.fileName.equals(song.fileName)){
			return this.getLocation().equals(song.getLocation());
		}
		return false;
	}
	
	@Override
	public int compareTo(Object arg0) {
		if(arg0 instanceof Song){
			if(equals(arg0)) return 0;
		}
		return -1;
	}
}

