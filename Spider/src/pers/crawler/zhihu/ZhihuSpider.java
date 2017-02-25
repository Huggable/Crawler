package pers.crawler.zhihu;
import pers.crawler.basic.BasicFunctions;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class ZhihuSpider {

	private final static String starUrl = "https://www.zhihu.com/explore/recommendations";
    public static String get1(String url){  
    	StringBuffer sb=new StringBuffer();
    	//System.out.println(url);
        try {  
            URL urls=new URL(url); 
            URLConnection urlconnection=urls.openConnection(); 
            urlconnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)"); // 有些浏览器会查看浏览器信息，防止爬取。  
            //urlconnection.connect();
            InputStream ins=urlconnection.getInputStream(); // 以流的方式读出内容  
            BufferedReader buff = new BufferedReader(new InputStreamReader(ins, "utf-8")); // 以字符流读入  
            String lines="";  
            
            while((lines=buff.readLine())!=null){  
                sb.append(lines+"\n");  
            }  
            
        } catch (MalformedURLException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }     
          return sb.toString();
    }  
    public static String match(String targetStr,String patternStr)
    {
    	Pattern pattern = Pattern.compile(patternStr);
    	Matcher matcher = pattern.matcher(targetStr);
    	if(matcher.find())
    	{
    		OutputStream f;
			try {
				f = new FileOutputStream("e:/weibo/d.txt");
				f.write(matcher.group(1).getBytes());
	            f.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
    		return matcher.group(1);
    	}
    	else return "No";
    }
    public static Queue<Zhihu> getZhihu(String content) throws Exception
    {
    	 Queue<Zhihu> results = new LinkedList<Zhihu>();
    	 
    	 Pattern p1 = Pattern.compile("question_link.+?href=.+?/question/(.*?)/");
    	 Matcher m1 = p1.matcher(content);
    	 
    	 Boolean isFind = m1.find();
    	 //OutputStream o = new FileOutputStream("e:/weibo/d.txt");
    	 while(isFind)
    	 {
    		 Zhihu zhihu = new Zhihu("https://www.zhihu.com/question/" + m1.group(1));
    		 //o.write(zhihu.zhihuUrl.getBytes());
    		 results.add(zhihu);
    		 isFind = m1.find();
    	 }
    	 //o.close();
    	 return results;
    }
    public static void getQueAndDesc(Zhihu z)
    {
    	String s = BasicFunctions.getContentZhihu(z.zhihuUrl);
    	//System.out.println(s);
    	//System.out.println(z.zhihuUrl);
    	Pattern p1 = Pattern.compile("zm-item-title\">\n\n.+?content\">(.+?)<");
    	Matcher m1 = p1.matcher(s);
    	
    	Pattern p2 = Pattern.compile("zh-question-detail\".+?\n\n.+?content\">(.*?)</div>");
    	Matcher m2 = p2.matcher(s);
    	
    	Boolean isFind = m1.find()&&m2.find();
    	if(isFind)
    	{
    		z.question = m1.group(1);
    		z.questionDescription = m2.group(1);
    	}
    }
    public static ArrayList<Answer> getAnswers(Zhihu z, int min ,Queue<String> authorLinks)
    {
    	ArrayList<Answer> result = new ArrayList<Answer>();
    	String s = BasicFunctions.getContentZhihu(z.zhihuUrl);
    	
    	Pattern link = Pattern.compile("data-atoken=\"(.+?)\"");
    	Matcher m0 = link.matcher(s);
    	
    	Pattern votes = Pattern.compile("data-votecount=\"(.+?)\"");
    	Matcher m1 = votes.matcher(s);
    	
    	Pattern author =Pattern.compile("data-author-name=\"(.+?)\"");
    	Matcher m2 = author.matcher(s);
    	
    	Pattern text = Pattern.compile("zm-editable-content clearfix\">\n(.+?)\n</div>");
    	Matcher m3 = text.matcher(s);
    	
    	Pattern authorLink = Pattern.compile("\"author-link\"\n.+?\n.+?href=\"(.+?)\"");
    	Matcher m4 = authorLink.matcher(s);

    	Boolean isFind = m0.find()&&m1.find()&&m2.find()&&m3.find()&&m4.find();
    	//System.out.println(isFind);
    	//Boolean a0 = m0.find(),a1=m1.find(),a2=m2.find(),a3=m3.find(),a4=m4.find();
    	//Boolean isFind = a0&&a1&&a2&&a3&&a4;
    	while(isFind)
    	{
    		//System.out.println(m2.group(1));
    		int v = Integer.parseInt(m1.group(1));
    		//System.out.println(m4.group(1));
    		if(v>=min)
    		{
    			Answer a= new Answer(m2.group(1),m3.group(1),v,m0.group(1),"");
    			result.add(a);
    		}
    		authorLinks.add("https://www.zhihu.com"+m4.group(1)+"/answers");
    		isFind = m0.find()&&m1.find()&&m2.find()&&m3.find()&&m4.find();
    	}
    	
    	return result;
    }
    
    public static void getZhihufromAuthorLinks(Queue<Zhihu> todo,Queue<String> authorLinks,HashSet<String> visited,int maxQueue)
    {
    	while(!authorLinks.isEmpty()&&todo.size()<maxQueue)
    	{
    		String html = BasicFunctions.getContentZhihu(authorLinks.poll());
	    	Pattern question = Pattern.compile("ContentItem-title\">.+?href=\"(.+?)\"");
	    	Matcher m1 = question.matcher(html);
	    	while(m1.find())
	    	{
	    		Zhihu z = new Zhihu("https://www.zhihu.com"+m1.group(1).substring(0,m1.group(1).indexOf("answer")));
	    		if(!visited.contains(z.zhihuUrl))todo.add(z);
	    	}
    	}
    }
   
    public static void StartCrawl(int min)
    {
    	Queue<Zhihu> todo = null;
    	
		try {
			todo = getZhihu(BasicFunctions.getContentZhihu(starUrl));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashSet<String> visited = new HashSet<String>();
    	while(!todo.isEmpty())
    	{
    		System.out.printf("todo size %d\n",todo.size());
    		System.out.printf("visited size %d\n",visited.size());
    		Zhihu ques = todo.poll();

    		if(!visited.contains(ques.zhihuUrl))
    		{
    			Queue<String> authorLinks = new LinkedList<>();
    			visited.add(ques.zhihuUrl);
    			//System.out.println(authorLinks.size());
    			ArrayList<Answer> answers = getAnswers(ques,min,authorLinks);
    			
    			//System.out.println(authorLinks.size());
    			if(answers.size() != 0)//top answer found
    			{
    				getQueAndDesc(ques);
    				Iterator<Answer> it = answers.iterator(); 
    				try {
						ZhihuJDBC.insertAnswers(answers, ques);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    			getZhihufromAuthorLinks(todo,authorLinks,visited,1000);
    			
    		}
    	}
    }

}