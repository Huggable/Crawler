package pers.crawler.weibo;

import pers.crawler.basic.BasicFunctions;
import pers.crawler.basic.WeiboConfig;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by LY on 2/15/2017.
 */
public class WeiboCrawler {
    public static UrlFilter unvisit ,visited,nickName;
    public static String startHtml;
    public WeiboCrawler()
    {
        if(unvisit == null)unvisit = new UrlFilter();
        if(visited == null)visited = new UrlFilter();
        if(nickName == null)nickName = new UrlFilter();
        if(startHtml == null)startHtml = BasicFunctions.getContent(WeiboConfig.URL);
    }
    public synchronized void initCrawler(int threadId) {
        System.out.printf("Initializing thread %d\n", threadId);
        Pattern p = Pattern.compile("star_name.+?href=\\\\\"(.+?)\\\\\"");
        Matcher m = p.matcher(startHtml);
        for (int i = 0; i < WeiboConfig.THREAD_NUM && m.find(); ++i) {
            //System.out.println("Visiting: " + WeiboConfig.HEAD + convert(m.group(1)));
            if (i != threadId) continue;
            String temp = BasicFunctions.getContent(WeiboConfig.HEAD + convert(m.group(1)));
            Pattern userLinkPattern = Pattern.compile("W_texta W_fb.+?nick-name.+?href=\\\\\"(.+?)\\\\\"");
            Matcher userMatcher = userLinkPattern.matcher(temp);
            int limit = 1;
            while (userMatcher.find() && limit > 0) {
                //System.out.println(convert(userMatcher.group(1)));
                unvisit.add(convert(userMatcher.group(1)));
                limit--;
            }
        }

    }
    public String convert(String url)
    {
        return url.replaceAll("\\\\","");
    }
    public void crawling(int id,int pageLimit)
    {
        initCrawler(id);
        while(visited.size() < pageLimit)
        {
            System.out.print("Now is Thread :"+id+"  "+"unvisit size: "+unvisit.size()+"  "+"visited size: "+visited.size()+"  \n");
            String userUrl;
            //System.out.println(unvisit.size());
            if((userUrl = (String)unvisit.getNext()) != null) {
                WeiboParser parser = new WeiboParser(userUrl);
                //System.out.println(parser.userUrl);
                parser.parse(visited,unvisit,nickName);
            }else {
                break;
            }

        }

    }
}
