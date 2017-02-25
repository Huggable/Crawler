package pers.crawler.weibo;

import pers.crawler.basic.BasicFunctions;
import pers.crawler.basic.JDBC;
import pers.crawler.basic.UrlFilter;
import pers.crawler.basic.WeiboConfig;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by LY on 2/17/2017.
 */
public class WeiboParser {
    private final String html;
    public final String userUrl;
    private static final int MINCOMMENT = 300;
    private static final int MAXCOMMENT = 5000000;
    private static final int MINREPOST = 1500;
    private static final int MAXREPOST = 15000000;
    private static  ArrayList<String> regex ;
    public boolean isCelebrity(String html)
    {
        String intro = null;
        Matcher m = Pattern.compile("pf_intro.*?title=\\\\\"(.*?)\\\\\">").matcher(html);
        if(m.find())intro = m.group(1);
        for(int i = 0; i<regex.size();++i) {
            if (Pattern.compile(regex.get(i)).matcher(intro).find())return true;
        }
        return false;
    }
    public WeiboParser(String userUrl)
    {
        html = BasicFunctions.getContentWeibo(userUrl);
        this.userUrl = userUrl;

        if(regex == null)
        {
            regex = new ArrayList<String>();
            regex.add("演");
            regex.add("歌");
            regex.add("艺人");
            regex.add("偶像");


        }
        //System.out.print(userUrl+"\n");
    }
    private int findFollowerNum()
    {
        Pattern followers = Pattern.compile("strong.+?>(.+?)<\\\\/strong");
        Matcher followersMatcher = followers.matcher(html);
        followersMatcher.find();
        int followerNum = 0;
        if(followersMatcher.find())//Followers number
        {
            followerNum = Integer.parseInt(followersMatcher.group(1));

        }
        return followerNum;
    }

    private String getRidOfSpace(String num)
    {
        return num.replaceAll(" ","");
    }
    private boolean isForwardWeibo(String weibo)
    {
        boolean isforward = Pattern.compile("isforward=",Pattern.CASE_INSENSITIVE).matcher(weibo).find();
        return isforward;
    }

    private boolean findCommentandReposts(String weibo,Weibo w) {

        Matcher repostNumMatcher,commentNumMatcher;
        repostNumMatcher=Pattern.compile("ficon_forward.+?<em>(.+?)<", Pattern.DOTALL).matcher(weibo);
        commentNumMatcher = Pattern.compile("ficon_repeat.+?<em>(.+?)<", Pattern.DOTALL).matcher(weibo);

        if(repostNumMatcher.find()&&commentNumMatcher.find())
        {
            int repostNum = 0;
            int commentNum = 0;
            try {
                repostNum = Integer.parseInt(getRidOfSpace(repostNumMatcher.group(1)));
            }catch (Exception e) {
            }
            try {
                commentNum = Integer.parseInt(getRidOfSpace(commentNumMatcher.group(1)));
            }catch (Exception e) {
            }
            w.commentNum = commentNum;
            w.repostNum = repostNum;
            //System.out.println(repostNum+"   "+commentNum);
            if(commentNum > MINCOMMENT)
            {
                if(commentNum < MAXCOMMENT) return true;
            }
            else if (repostNum >MINREPOST)
            {
                if(repostNum < MAXREPOST)return true;
            }
            else return false;
        }
        return false;

    }
    public String findNickName()
    {
        Matcher m = Pattern.compile("pf_username.+?username.*?>(.+?)<",Pattern.CASE_INSENSITIVE).matcher(html);
        if(m.find())return m.group(1);
        else return null;
    }

    public void  parse(UrlFilter visited, UrlFilter unvisit, UrlFilter nickNames) {
        String nickName = findNickName();
        if(nickNames.contains(nickName)||visited.contains(userUrl))return ;
        nickNames.add(nickName);
        visited.add(userUrl);

        ArrayList<Weibo> weibos = new ArrayList<Weibo>();

        int followerNum = findFollowerNum();
        boolean isFamous = followerNum > WeiboConfig.MINFOLLOWERNUM ;
        boolean isCele = isCelebrity(html);
        System.out.printf("Visiting: %s\n",nickName);
        if(!isCele && isFamous) {
            Matcher weiboMatcher = Pattern.compile("tbinfo.+?feed_list_repeat", Pattern.DOTALL).matcher(html);
            while (weiboMatcher.find()) {

                Weibo w = new Weibo();
                String weibo = weiboMatcher.group(0);
                boolean isForward = isForwardWeibo(weibo);
                if(isForward)continue;
                boolean isHot = findCommentandReposts(weibo,w);
                if(isHot)
                {
                    //System.out.print("HOT!\n");
                    Matcher weiboTextMatcher = Pattern.compile("<div class=\\\\\"WB_text.+?>\\\\n(.+?)<\\\\/div",Pattern.DOTALL).matcher(weibo);
                    if(weiboTextMatcher.find()) {
                        w.text = weiboTextMatcher.group(1);
                    }
                    Matcher weiboIdMatcher = Pattern.compile("mid=\\\\\"(.+?)\\\\").matcher(weibo);
                    if(weiboIdMatcher.find())
                    {
                        w.id = weiboIdMatcher.group(1);
                    }
                    w.authorNickname = nickName;
                    w.authorLink = userUrl;
                    weibos.add(w);
                }
                //System.out.println(isHot);
            }
        }
        if(!weibos.isEmpty())
        {
            try {
                JDBC.insertWeibos(weibos);
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        Matcher atMatcher = Pattern.compile("atname.*?href=\\\\\"(.+?)\\\\\".*?>@(.+?)<",Pattern.DOTALL).matcher(html);
        while(atMatcher.find())
        {
            String url = atMatcher.group(1).replaceAll("\\\\","");
            String atNameNickName = atMatcher.group(2).replaceAll(" ","");
            if(unvisit.contains(url)||nickNames.contains(atNameNickName))continue;
            unvisit.add(url);
        }

    }


}
