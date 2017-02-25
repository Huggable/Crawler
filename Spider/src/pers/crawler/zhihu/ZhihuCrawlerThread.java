package pers.crawler.zhihu;

/**
 * Created by LY on 2/22/2017.
 */
public class ZhihuCrawlerThread extends ZhihuCrawler implements Runnable {
    private int threadId;
    public ZhihuCrawlerThread(int id){threadId = id;}
    public void run()
    {
        try {
            crawling(threadId);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
