package com.raysmond.internet;
import java.net.*;
import java.io.*;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.raysmond.song.DownloadSong;
import com.raysmond.view.SearchSongDialog;

/**
 * �ļ������߳���
 * 
 * @author Jiankun Lei
 *
 */
public class DownloadThread extends Thread
{
    private SearchSongDialog owner;   //����
    private String inUrl;			  //��������
    private String outPath;			  //���汾�ص�Ŀ¼λ��
    private String outName;		      //���浽���ص��ļ�����������׺��
    private DownloadSong song;
    
    //̫ǿ��,��Ȼ����Զ�������
    //iPad��ţ��
    
    public DownloadThread(SearchSongDialog owner,DownloadSong song,String path){
    	 setOwner(owner);
    	 this.song = song;
         inUrl = song.getDownloadUrl();
         outPath = path;
         outName = song.getFileName();
    }
    
    /**
     * ���������̣߳���ʼ����
     */
    public void run()
    {
        boolean error = false;
        try
        {
            URL url = new URL(inUrl);
            //String filename = url.getFile();
            try
            {
                DataInputStream in = new DataInputStream(url.openConnection().getInputStream()); 
                FileOutputStream fOut = new FileOutputStream(outPath+"/"+outName);
                DataOutputStream out = new DataOutputStream(fOut);
                
                int chc;
                double downedsize = 0;
                while ((chc = in.read()) != -1)
                {
                    out.write(chc);
                    ++downedsize;
                    //System.out.println("downloaded: " + downedsize + " bytes." + " ------ " + (downedsize/1024) + " KB");
                }
                in.close();
                fOut.flush(); 
                fOut.close();
            }
            catch (IOException e)
            {
            	System.out.println("Unable to download");
                error = true;
            }
        }
        catch (MalformedURLException e)
        {
            System.out.println("Unable to download");
            error = true;
        }
        if (!error){
        	System.out.println("Download finished:" + this.inUrl);
        	owner.completeDownload(song);
        } 
        return;
    }

    
    //getters and setters
    
	public SearchSongDialog getOwner() {
		return owner;
	}

	public void setOwner(SearchSongDialog owner) {
		this.owner = owner;
	}
}
