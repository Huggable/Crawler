package pers.crawler.weibo;

import pers.crawler.basic.BasicFunctions;
import pers.crawler.basic.WeiboConfig;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by LY on 2/17/2017.
 */
public class WeiboParser {
    private final String html;
    public final String userUrl;
    private static final int MINCOMMENT = 500;
    private static final int MINREPOST = 1500;
    private static String s = "http://weibo.com/n/%E8%83%A1%E6%AD%8C?from=feed&loc=at";
    public WeiboParser(String userUrl)
    {
        html = BasicFunctions.getContent(userUrl);
        this.userUrl = userUrl;
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

    private boolean findCommentandReposts(String weibo,boolean isForward) {
        Matcher repostNumMatcher,commentNumMatcher;
        if(!isForward) {
            repostNumMatcher=Pattern.compile("ficon_forward.+?<em>(.+?)<", Pattern.DOTALL).matcher(weibo);
            commentNumMatcher = Pattern.compile("ficon_repeat.+?<em>(.+?)<", Pattern.DOTALL).matcher(weibo);
        }
        else
        {
            repostNumMatcher=Pattern.compile("ficon_forward.+?ficon_forward.+?<em>(.+?)<", Pattern.DOTALL).matcher(weibo);
            commentNumMatcher = Pattern.compile("ficon_repeat.+?ficon_repeat.+?<em>(.+?)<", Pattern.DOTALL).matcher(weibo);
        }
        if(repostNumMatcher.find()&&commentNumMatcher.find())
        {
            int repostNum = Integer.parseInt(getRidOfSpace(repostNumMatcher.group(1)));
            int commentNum = Integer.parseInt(getRidOfSpace(commentNumMatcher.group(1)));
            System.out.println(isForward + "   " + repostNum + "    " + commentNum);
            if(commentNum > MINCOMMENT)return true;
            else if (repostNum >MINREPOST)return true;
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
    public void  parse(UrlFilter visited,UrlFilter unvisit,UrlFilter nickNames) {
        String nickName = findNickName();
        if(nickNames.contains(nickName))return ;
        nickNames.add(nickName);
        visited.add(userUrl);

        int followerNum = findFollowerNum();
        boolean isFamous = followerNum > WeiboConfig.MINFOLLOWERNUM ;
        boolean notaCelebrity = (followerNum > WeiboConfig.MINFOLLOWERNUM) && (followerNum < 5000000);
        System.out.printf("Visiting: %s\n",nickName);
        /*if(!isFamous) {
            Matcher weiboMatcher = Pattern.compile("tbinfo.+?feed_list_repeat", Pattern.DOTALL).matcher(html);
            while (weiboMatcher.find()) {

                String weibo = weiboMatcher.group(0);
                boolean isForward = isForwardWeibo(weibo);
                boolean isHot = findCommentandReposts(weibo, isForward);
                //System.out.println(isHot);
            }
        }*/

        Matcher atMatcher = Pattern.compile("atname.*?href=\\\\\"(.+?)\\\\\".*?>@(.+?)<",Pattern.DOTALL).matcher(html);
        while(atMatcher.find())
        {
            String url = atMatcher.group(1).replaceAll("\\\\","");
            String atNameNickName = atMatcher.group(2).replaceAll(" ","");

            //System.out.println(atNameNickName);
            if(unvisit.contains(url)||nickNames.contains(atNameNickName))continue;
            unvisit.add(url);

        }

    }


}
