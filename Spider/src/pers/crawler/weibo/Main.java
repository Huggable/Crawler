package pers.crawler.weibo;

import pers.crawler.basic.JDBC;
import pers.crawler.basic.WeiboConfig;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by LY on 2/15/2017.
 */
public class Main
{
    public static void main(String[] args)
    {
        JDBC.connect();
        long start = System.currentTimeMillis();

        ArrayList<Thread> threadList = new ArrayList<Thread>(WeiboConfig.THREAD_NUM);
        for(int i = 0 ; i < WeiboConfig.THREAD_NUM; i++) {
            Runnable crawler = new WeiboCrawlerThread(i);

            Thread t = new Thread(crawler);
            t.start();
            threadList.add(t);

        }
        boolean b = true;
        while(b)
        {
            Iterator<Thread> it = threadList.iterator();
            boolean c = false;
            System.out.print("Threads status :  ");
            while(it.hasNext())
            {
                boolean d = it.next().isAlive();
                c = c | d;
                System.out.print(d+"  ");

            }
            b = c;
            System.out.print("\n");
            try {
                Thread.sleep(5000);
            }catch (Exception e){}
        }

        //String url = "http://weibo.com/n/%E5%88%98%E8%8A%B8?from=feed&loc=at";

        //System.out.print(url+"\n");

        long end = System.currentTimeMillis();
        System.out.print((end-start)/WeiboConfig.PAGELIMIT);
        /*WeiboParser p = new WeiboParser("http://weibo.com/u/5931118095?refer_flag=1001030102_");
        p.parse(new UrlFilter(),new UrlFilter(),new UrlFilter());*/
    }

}