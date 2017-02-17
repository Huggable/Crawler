package pers.crawler.zhihu;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	public static void main(String[] args) throws Exception {
        String url = "https://www.zhihu.com/explore/recommendations";
       /* String res = ZhihuSpider.get(url);
        ArrayList<Zhihu> r = ZhihuSpider.getZhihu(res);
        Iterator<Zhihu> it = r.iterator();
        while(it.hasNext())
    	{
        	ZhihuSpider.getQueAndDesc(it.next());
    	}
        
        //System.out.println(r);
        //System.out.println(r.size());
         //与
        //ZhihuJDBC.insert(r);
        
        //ArrayList<Answer> results = ZhihuSpider.getAnswers(r.get(1),1000);

        System.out.println(r);*/
        
        
        ZhihuSpider.StartCrawl(20);
        /*ArrayList<Answer> answers = new ArrayList<Answer>();
        Answer a = new Answer("w","sdfa",3002,"1231231");
        answers.add(a);
        Zhihu z = new Zhihu("https://www.zhihu.com/question/55445739");
        z.question="why";
        z.questionDescription="d";
        ZhihuJDBC.insertAnswers(answers, z);*/
        
        
        //ZhihuSpider.getAnswers(z,300,authorLinks);
    	
    	//System.out.println(authorLinks.size());
        

    }
}
