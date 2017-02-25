package pers.crawler.basic;

/**
 * Created by LY on 2/15/2017.
 */
public class WeiboConfig {
    public static final String URL = "http://s.weibo.com/top/summary?cate=realtimehot";
    public static final String HEAD = "http://s.weibo.com";
    public static final int THREAD_NUM = 5;
    public static final int MINFOLLOWERNUM = 500000;
    public static final int PAGELIMIT = Integer.MAX_VALUE;
}
