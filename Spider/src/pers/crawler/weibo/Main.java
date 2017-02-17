package pers.crawler.weibo;

import pers.crawler.basic.WeiboConfig;

import java.util.ArrayList;

/**
 * Created by LY on 2/15/2017.
 */
public class Main
{
    public static void main(String[] args)
    {
        int pages = 10;
        long start = System.currentTimeMillis();
        //WeiboCrawler w = new WeiboCrawler();
        //w.crawling(0,20);
        //BasicFunctions.getContent("http://weibo.com/xinwenhuabao?from=feed&loc=at&nick=%E6%96%B0%E6%96%87%E5%8C%96%E6%8A%A5");
        ArrayList<Thread> threadList = new ArrayList<Thread>(WeiboConfig.THREAD_NUM);
        /*for(int i = 0 ; i < WeiboConfig.THREAD_NUM; i++) {
            WeiboCrawlerThread crawler = new WeiboCrawlerThread(i);
            Thread t = new Thread(crawler);
            t.start();
            threadList.add(t);

        }
        while(threadList.size() > 0) {
            Thread child = (Thread) threadList.remove(0);
            try {
                child.join();
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }*/
        WeiboCrawler w = new WeiboCrawler();
        w.crawling(0,WeiboConfig.PAGELIMIT);
        //String url = "http://weibo.com/n/%E5%88%98%E8%8A%B8?from=feed&loc=at";

        //System.out.print(url+"\n");

        long end = System.currentTimeMillis();
        System.out.print((end-start)/pages);
    }

}