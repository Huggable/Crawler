package pers.crawler.zhihu;

import pers.crawler.basic.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by LY on 2/20/2017.
 */
public class ZhihuCrawler implements Crawler{
    public static UrlFilter unvisit,visited,nickName;
    public ZhihuCrawler()
    {
        if(unvisit == null)unvisit = new UrlFilter();
        if(visited == null)visited = new UrlFilter();
        if(nickName == null)nickName = new UrlFilter();

    }
    public static void getZhihu(String content)
    {
        Pattern p1 = Pattern.compile("question_link.+?href=.+?/question/(.*?)\"",Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
        Matcher m1 = p1.matcher(content);


        //OutputStream o = new FileOutputStream("e:/weibo/d.txt");
        //System.out.println("U "+isFind);
        while(m1.find())
        {
            Zhihu zhihu = new Zhihu("https://www.zhihu.com/question/" + (m1.group(1).indexOf("answer")==-1 ? m1.group(1):m1.group(1).substring(0,m1.group(1).indexOf("answer"))));
            //o.write(zhihu.zhihuUrl.getBytes());
            unvisit.add(zhihu.zhihuUrl);

            //System.out.println("U "+unvisit.size());
        }
        //o.close();
    }
    public synchronized void initCrawler()
    {
        if (!unvisit.isEmpty())return;
        System.out.println("Starting at the recommendation page");
        try{
            getZhihu(BasicFunctions.getContentZhihu(ZhihuConfig.URL));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void crawling(int threadId)
    {
        synchronized(ZhihuCrawler.class) {
            while (unvisit.isEmpty()) initCrawler();
        }
        while(visited.size() < ZhihuConfig.PAGELIMIT)
        {
            System.out.print("Now is Thread :"+threadId+"  "+"unvisit size: "+unvisit.size()+"  "+"visited size: "+visited.size()+"  \n");
            String pageUrl = null;
            if((pageUrl = (String)unvisit.getNext()) != null)
            {
                //System.out.println(pageUrl);

                try {
                    ZhihuParser parser = new ZhihuParser(pageUrl);
                    parser.parse(visited, unvisit);
                }catch (Exception e)
                {
                    System.out.println("ERROR!!!!!");
                    e.printStackTrace();
                }
            }
            else
            {
                try {
                    Thread.sleep(5000);
                    System.out.println("END1:-------------------------------------"+threadId);
                    break;
                }catch (Exception e)
                {
                    System.out.println("END:-------------------------------------"+threadId);
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

}
