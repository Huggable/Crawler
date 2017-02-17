package pers.crawler.weibo;

import pers.crawler.basic.WeiboConfig;

/**
 * Created by LY on 2/17/2017.
 */
public class WeiboCrawlerThread extends WeiboCrawler implements Runnable{
    private int threadId;


    public WeiboCrawlerThread(int id) {
        this.threadId = id;
    }
    public void run() {
        try {
            crawling(threadId, WeiboConfig.PAGELIMIT);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
