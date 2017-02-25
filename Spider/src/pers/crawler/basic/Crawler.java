package pers.crawler.basic;

/**
 * Created by LY on 2/20/2017.
 */
public interface Crawler {
    public void initCrawler();
    public void crawling(int threadId);
}
