package pers.crawler.zhihu;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

public class ZhihuJDBC {

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost:3306/sys";
 
    static final String USER = "ly";
    static final String PASS = "1234567";
    public static Connection connect() throws Exception
    {
    	Class.forName("com.mysql.jdbc.Driver");
		System.out.println("连接数据库...");
		Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
		System.out.println("成功连接到数据库！");
		return conn;
    }
	public static void insert(ArrayList<Zhihu> z) throws Exception 
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
	public static void insertAnswers(ArrayList<Answer> answers,Zhihu z) throws Exception
	{
		Connection conn = connect();
        Statement stmt = conn.createStatement();
        
        Iterator<Answer> it = answers.iterator();
        while(it.hasNext())
        {
        	Answer temp = it.next();
        	StringBuffer sql = new StringBuffer("Insert Into topanswers Values ('");
        	sql.append(temp.link);
        	sql.append("','");
        	sql.append(z.question);
        	sql.append("','");
        	sql.append(z.questionDescription);
        	sql.append("','");
        	sql.append(z.zhihuUrl+"/answer/"+temp.link);
        	sql.append("','");
        	sql.append(temp.author);
        	sql.append("','");
        	sql.append(temp.votes);
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
}
