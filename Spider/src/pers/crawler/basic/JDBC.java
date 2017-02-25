package pers.crawler.basic;

import pers.crawler.weibo.Weibo;
import pers.crawler.zhihu.Answer;
import pers.crawler.zhihu.Zhihu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by LY on 2/25/2017.
 */
public class JDBC {
    public static Connection conn = null;
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://l.cgxyavzjr66j.ap-northeast-1.rds.amazonaws.com:3306/sys?characterEncoding=utf8";
    //static final String DB_URL = "jdbc:mysql://localhost:3306/sys?characterEncoding=utf8";
    static final String USER = "ly";
    static final String PASS = "l7419298115";
    //static final String PASS = "1234567";
    //static final String TABLENAME = "ZhihuTopAnswers";
    static final String ZHIHUTABLENAME = "topanswers";
    static final String WEIBOTABLENAME = "topweibos";
    public static void connect()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("连接数据库...");
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("成功连接到数据库！");
            conn = connection;
        }catch (Exception e){e.printStackTrace();}
    }
    public static void insertAnswers(ArrayList<Answer> answers, Zhihu z) throws Exception
    {
        Statement stmt = conn.createStatement();

        Iterator<Answer> it = answers.iterator();
        while(it.hasNext())
        {
            Answer temp = it.next();
            StringBuffer sql = new StringBuffer("Insert Into "+ZHIHUTABLENAME+" Values ('");
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
    }
    public static void insertWeibos(ArrayList<Weibo> weibos)throws Exception
    {
        Statement stmt = conn.createStatement();
        Iterator<Weibo> it = weibos.iterator();
        while(it.hasNext())
        {
            Weibo temp = it.next();
            StringBuffer sql = new StringBuffer("Insert Into "+WEIBOTABLENAME+" Values ('");
            sql.append(temp.id);
            sql.append("','");
            sql.append(temp.authorNickname);
            sql.append("','");
            sql.append(temp.authorLink);
            sql.append("','");
            sql.append(temp.text);
            sql.append("','");
            sql.append(temp.commentNum);
            sql.append("','");
            sql.append(temp.repostNum);
            sql.append("')");
            System.out.println(sql.toString());
            try {
                stmt.executeUpdate(sql.toString());
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                System.out.println(e.getMessage());
            }
        }
    }
}
