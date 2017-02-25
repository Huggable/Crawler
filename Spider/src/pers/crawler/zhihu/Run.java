package pers.crawler.zhihu;

import pers.crawler.basic.JDBC;
import pers.crawler.basic.ZhihuConfig;

import java.util.ArrayList;
import java.util.Iterator;

public class Run {
	public static void main(String[] args) throws Exception {
        JDBC.connect();
	    ZhihuConfig.MINVOTES = Integer.parseInt(args[0]);
        long start = System.currentTimeMillis();

        ArrayList<Thread> threadList = new ArrayList<Thread>();
        for(int i = 0; i < ZhihuConfig.THREAD_NUM; i++) {
            ZhihuCrawlerThread crawler = new ZhihuCrawlerThread(i);
            Thread t = new Thread(crawler);
            t.start();
            threadList.add(t);

        }
        boolean b = true;
        while(b)
        {
            Iterator<Thread> it = threadList.iterator();
            boolean c = false;
            while(it.hasNext())
            {
            	boolean d = it.next().isAlive();
            	c = c | d;
                System.out.print(d+"  ");

            }
            b = c;
            System.out.print("\n");
            Thread.sleep(60000*5);
        }

        //String url = "http://weibo.com/n/%E5%88%98%E8%8A%B8?from=feed&loc=at";

        //System.out.print(url+"\n");

        //long end = System.currentTimeMillis();
        //System.out.print((end-start)/ ZhihuConfig.PAGELIMIT);
        

    }
}
