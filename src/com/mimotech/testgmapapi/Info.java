package com.mimotech.testgmapapi;

import java.util.ArrayList;

public class Info {
	// public static HashMap<String, News> newsList = new HashMap<String,
	// News>();
	public static ArrayList<News> newsList = new ArrayList<News>();

	public static int unReadNumber() {
		int count = newsList.size();
		for (int i = 0; i < newsList.size(); i++) {
			if (newsList.get(i).isRead == true) {
				--count;
			}
		}
		return count;
	}

	public static void uniqueAdd(News news) {
		
		for (int i = 0; i < newsList.size(); i++) {
			if (news.id.equalsIgnoreCase(newsList.get(i).id)) {
				return;
			}
		}
		newsList.add(news);
	}

	public static News getNews(String newsId) {
		for (int i = 0; i < newsList.size(); i++) {
			if (newsList.get(i).id.equalsIgnoreCase(newsId)) {
				return newsList.get(i);
			}
		}
		return null;
	}

	public static void bubbleSort2(int[] array) {
		int tmp = 0;
		for (int i = 0; i < array.length; i++) {
			for (int j = 1; j < (array.length - i); j++) {
				if (array[j - 1] < array[j]) {
					tmp = array[j];
					array[j] = array[j - 1];
					array[j - 1] = tmp;
				}
			}
		}
	}
}
