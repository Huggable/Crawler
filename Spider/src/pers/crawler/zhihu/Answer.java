package pers.crawler.zhihu;

public class Answer {

	public String text;
	public String author;
	public int votes;
	public String id;
	public String authorLink;
	public Answer(String a, String t, int v, String i,String al)
	{
		//有
		author = a;
		text = t;
		votes = v;
		id = i;
		authorLink = al;
	}
}
