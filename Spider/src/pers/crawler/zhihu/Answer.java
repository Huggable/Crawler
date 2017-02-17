package pers.crawler.zhihu;

public class Answer {

	public String text;
	public String author;
	public int votes;
	public String link;
	public Answer(String a, String t, int v, String l)
	{
		//有
		author = a;
		text = t;
		votes = v;
		link = l;
	}
}
