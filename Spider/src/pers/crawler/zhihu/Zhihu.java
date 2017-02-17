package pers.crawler.zhihu;
import java.util.ArrayList;

public class Zhihu {
	public String question;
    public String zhihuUrl;
    public String questionDescription;
    public ArrayList<String> answers;
  

    public Zhihu(String url) {  
        question = "";  
        zhihuUrl = url;  
        questionDescription = "";
        answers = new ArrayList<String>();
    }  
  
    @Override  
    public String toString() {  
        return "问题：" + question + "\n链接：" + zhihuUrl + "\n描述：" + questionDescription + "\n回答：" + answers + "\n";  
    }  
}
