package pers.crawler.zhihu;

import pers.crawler.basic.BasicFunctions;
import pers.crawler.basic.JDBC;
import pers.crawler.basic.UrlFilter;
import pers.crawler.basic.ZhihuConfig;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by LY on 2/20/2017.
 */
public class ZhihuParser {
    private final String html;
    public final String pageUrl;
    public String Question;
    public String QuestionDescription;
    public ZhihuParser(String url)
    {
        Question = null;
        QuestionDescription = null;
        pageUrl = url;
        html = BasicFunctions.getContentZhihu(pageUrl);
    }
    private int getVotestAndAuthorLink(String answer,UrlFilter visited, UrlFilter unvisit)
    {
        Matcher m = Pattern.compile("\"author-link\".+?href=\"(.+?)\"",Pattern.DOTALL).matcher(answer);
        int votes = 0;
        if(m.find())
        {
            String link = m.group(1);
            String url = ZhihuConfig.HEAD+(link.startsWith("/")?link.substring(1):link)+(link.endsWith("/")?"answers":"/answers");
            //System.out.println("GET2 : "+url);
            String authorPage = BasicFunctions.getContentZhihu(url);
            Matcher m1 = Pattern.compile("ContentItem-title\">.+?href=\"(.+?)\"").matcher(authorPage);
            while(m1.find())
            {
                String quesId = m1.group(1).substring(0,m1.group(1).indexOf("answer"));
                String quesUrl = ZhihuConfig.HEAD+(quesId.startsWith("/")?quesId.substring(1):quesId);

                if(!visited.contains(quesUrl))unvisit.add(quesUrl);
            }
        }
        Matcher votesMatcher = Pattern.compile("data-votecount=\"(.+?)\"").matcher(answer);
        if(votesMatcher.find())
        {
            try {
                votes = Integer.parseInt(votesMatcher.group(1).replaceAll(" ", ""));
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return votes;
    }
    public  void getQueAndDesc()
    {


        Pattern p1 = Pattern.compile("zm-item-title\">.+?content\">(.+?)<",Pattern.DOTALL);
        Matcher m1 = p1.matcher(html);

        Pattern p2 = Pattern.compile("zh-question-detail\".+?content\">(.*?)</div>",Pattern.DOTALL);
        Matcher m2 = p2.matcher(html);

        if(m1.find())
        {
            Question = m1.group(1);
        }
        if(m2.find())
        {
            QuestionDescription = m2.group(1);
        }
        if(QuestionDescription.length() == 0)
        {
            Matcher m3  = Pattern.compile("zh-question-detail\".+?hidden\">(.*?)</textarea>",Pattern.DOTALL).matcher(html);
            if(m3.find())QuestionDescription = m3.group(1);
        }
    }
    public static Answer getAnswers(String answer)
    {
        String s = answer;
        String id = null,author = null,text = null,authorLink = null;

        Matcher m0 = Pattern.compile("data-atoken=\"(.+?)\"").matcher(s);
        if(m0.find())
        {
            id = m0.group(1);
        }
        Matcher m2 = Pattern.compile("data-author-name=\"(.+?)\"").matcher(s);
        if(m2.find())
        {
            author = m2.group(1);
        }
        Matcher m3 = Pattern.compile("zm-editable-content clearfix\">\n(.+?)\n</div>").matcher(s);
        if(m3.find())
        {
            text = m3.group(1);
        }
        Matcher m4 = Pattern.compile("\"author-link\"\n.+?\n.+?href=\"(.+?)\"").matcher(s);
        if(m4.find())
        {
            String link = m4.group(1);
            authorLink = ZhihuConfig.HEAD+(link.startsWith("/")?link.substring(1):link)+(link.endsWith("/")?"answers":"/answers");
        }

        Answer result = new Answer(author,text,0,id,(authorLink==null?"":authorLink));
        return result;
    }
    public void parse(UrlFilter visited, UrlFilter unvisit) throws Exception
    {
        if(visited.contains(pageUrl))return;
        visited.add(pageUrl);
        Matcher answerMatcher = Pattern.compile("data-atoken.*?zm-item-meta answer-actions clearfix js-contentActions",Pattern.CASE_INSENSITIVE|Pattern.DOTALL).matcher(html);
        int i = 0;
        ArrayList<Answer> answers = new ArrayList<Answer>();
        while(answerMatcher.find())
        {
            String answer = answerMatcher.group(0);
            int votes = 0;
            if((votes = getVotestAndAuthorLink(answer,visited,unvisit)) > ZhihuConfig.MINVOTES){
                if(Question == null) {
                    getQueAndDesc();
                }
                Answer a = getAnswers(answer);
                a.votes = votes;
                answers.add(a);
            }
        }
        if(!answers.isEmpty())
        {
            Zhihu z = new Zhihu(pageUrl);
            z.question = Question;
            z.questionDescription = QuestionDescription;
            try {
                JDBC.insertAnswers(answers, z);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
