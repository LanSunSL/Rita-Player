package com.raysmond.internet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.raysmond.lyric.LRCSearchResult;
import com.raysmond.song.DownloadSong;

/**
 * ʹ��Jsoup�����Դ����ҳHTML�������԰ٶ����ֵ�����������н��� ���� �Ӱٶ���������һ���׸�ĸ�ʣ��������������
 * �Ӱٶ���������һ�׸裬������������������������⡢���֡�ר������mp3�������ӣ�
 * 
 * @author Jiankun Lei
 * 
 */
public class JsoupTest {

	public static void main(String[] args) {
		getSongSearchResultFromBaidu("����", "����Ѹ", 5);
	}

	/**
	 * �Ӱٶ���������һ�׸裬��������ҳ��HTML���õ����
	 * 
	 * @param title ����
	 * @param artist ����
	 * @param number ָ����Ҫ�����Ľ����
	 * @return
	 */
	public static ArrayList<DownloadSong> getSongSearchResultFromBaidu(
			String title, String artist, int number) {
		ArrayList<DownloadSong> results = new ArrayList<DownloadSong>();
		try {
			System.out.println("searching: " + title);
			Document doc = Jsoup.connect(
					"http://music.baidu.com/search/song?key="
							+ URLEncoder.encode(title, "UTF-8")).get();
			Element s = doc.getElementById("result_container").getElementsByClass("song-list").first();
			Elements songList1 = s.getElementsByTag("ul").first().getElementsByTag("li");
			int counter = 0;
			for (Element e : songList1) {
				Element songInfo = e.getElementsByClass("song-item").first();
				Element titleElement = e.getElementsByClass("song-title")
						.first().getElementsByTag("a").first();
				if (titleElement == null) continue;
				
				// song title
				String titleStr = titleElement.attr("title");
				
				// song id
				String songId = titleElement.attr("href").substring(6);
		
				if (number == 1) { // �õ���һ����ȷһ��ĸ��
					if (!titleStr.equalsIgnoreCase(title))
						continue;
				}
				
				Element artistElement = e.getElementsByClass("singer").first().getElementsByTag("a").first();
				Element albumElement = e.getElementsByClass("album-title").first().getElementsByTag("a").first();

				String artistStr = null;
				String albumStr = null;
				
				if (artistElement != null) artistStr = artistElement.attr("title");
				if (albumElement != null) albumStr = albumElement.attr("title");
				
				System.out.println("title:" + titleStr);
				System.out.println("artist:" + artistStr);
				System.out.println("album:" + albumStr);
				System.out.println("id:" + songId);
				
				String downloadUrlPage = "http://music.baidu.com/song/" + songId + "/download";
				System.out.println(downloadUrlPage);
				String downData = null;
				try{
					Document downloadPage = Jsoup.connect(downloadUrlPage).get();
					Element downloadBut = downloadPage.getElementById("download");
					if (downloadBut == null) continue;
					downData = downloadBut.attr("href");
					downData = "http://music.baidu.com" + downData;
				}catch(Exception e1){
					e1.printStackTrace();
					continue;
				}
				System.out.println(downData);
				
				/*
				 * Elements downloads =
				 * downloadPage.getElementById("form").getElementsByTag
				 * ("ul").first().getElementsByTag("li");
				 * 
				 * if(downloads.size()>=2){ downData =
				 * downloads.get(1).attr("data-data");
				 * System.out.println("downData:" +
				 * downloads.get(1).toString()); } else if(downloads.size()==1){
				 * downData = downloads.first().attr("data-data"); }
				 */
				
				String rate = null;
				String link = null;
				if (downData != null) {
					 rate = downData.substring(downData.indexOf("\"rate\":") + 7, downData.indexOf(","));
					 link = downData.substring(downData.indexOf("\"link\":\"") + 8, downData.indexOf("\"}"));
				} else
					continue;

				System.out.println("download:" + downloadUrlPage);
				System.out.println("link:" + link);
				System.out.println("rate:" + rate);

				DownloadSong song = new DownloadSong(titleStr, artistStr,
						(artistStr + " - " + titleStr + ".mp3"), 0, downData,
						albumStr);
				results.add(song);
				counter++;
				if (counter >= number)
					return results;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return results;
	}

}
