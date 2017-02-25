package pers.crawler.zhihu;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

public class ZhihuJDBC {

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://l.cgxyavzjr66j.ap-northeast-1.rds.amazonaws.com:3306/sys?characterEncoding=utf8";
	//static final String DB_URL = "jdbc:mysql://localhost:3306/sys?characterEncoding=utf8";
    static final String USER = "ly";
    static final String PASS = "l7419298115";
	//static final String PASS = "1234567";
    //static final String TABLENAME = "ZhihuTopAnswers";
	static final String TABLENAME = "topanswers";

    public static Connection connect() throws Exception
    {
    	Class.forName("com.mysql.jdbc.Driver");
		System.out.println("连接数据库...");
		Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);

		System.out.println("成功连接到数据库！");
		return conn;
    }
	public static synchronized  void insert(ArrayList<Zhihu> z) throws Exception
    {
		Connection conn = connect();

        Statement stmt = conn.createStatement();
       
		Iterator<Zhihu> it = z.iterator();
		while(it.hasNext())
		{
			Zhihu temp = it.next();
			StringBuffer sql = new StringBuffer("Insert Into zhihu Values ('");
			sql.append(temp.question);
			sql.append("','");
			sql.append(temp.zhihuUrl);
			sql.append("','");
			sql.append(temp.questionDescription);
			sql.append("')");
			System.out.println(sql.toString());
			try {
				stmt.executeUpdate(sql.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("chucuo");
			}
		}
		if(conn !=null)
		{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
	public static synchronized void insertAnswers(ArrayList<Answer> answers,Zhihu z) throws Exception
	{
		Connection conn = connect();
        Statement stmt = conn.createStatement();

        
        Iterator<Answer> it = answers.iterator();
        while(it.hasNext())
        {
        	Answer temp = it.next();
        	StringBuffer sql = new StringBuffer("Insert Into "+TABLENAME+" Values ('");
        	sql.append(temp.id);
        	sql.append("','");
        	sql.append(z.question);
        	sql.append("','");
        	sql.append(z.questionDescription);
        	sql.append("','");
        	sql.append(z.zhihuUrl+(z.zhihuUrl.endsWith("/")?"answer/":"/answer/")+temp.id);
        	sql.append("','");
        	sql.append(temp.author);
        	sql.append("','");
        	sql.append(temp.votes);
			sql.append("','");
			sql.append(temp.authorLink);
			sql.append("','");
			sql.append(temp.text);
        	sql.append("')");
        	System.out.println(sql.toString());
			try {
				stmt.executeUpdate(sql.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
        }
        if(conn !=null)
		{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        
	}
	public static synchronized  void get()throws Exception
	{
		Connection conn = connect();
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery("SELECT * FROM SoicalMedia.topanswers;");
		while(rs.next())
		{
			System.out.println(rs.getInt("ID")+"  "+rs.getString("Question"));
		}
	}
}
