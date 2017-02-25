package pers.crawler.zhihu;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.MarionetteDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by LY on 2/22/2017.
 */
public class test {
    String s1 = "webdriver.chrome.driver";
    String s2 = "phantomjs.binary.path";
    String s3 = "C:\\Users\\LY\\Desktop\\chromedriver.exe";
    String s4 = "C:\\Users\\LY\\Desktop\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe";
    String cookie = "_zap=2606fa60-887f-4301-81bb-a06a8379b834; d_c0=\"ACBAJUyZvAqPTrSvmSqPw_3nT9TE3dMlXOs=|1477235344\"; _zap=2a73752c-7efd-4f60-b4c3-17efcf46c432; aliyungf_tc=AQAAAPAMyAtTGwkAFT5RQ7096WEZadSO; q_c1=a791cc21fdcf432c9759e8c61a6421b9|1487597074000|1487597074000; nweb_qa=heifetz; cap_id=\"YmE3OTczOTc1NWQ2NDYzYjljZjgzMDUxZTI2YjhiM2M=|1487597074|4877bc6c936489aeefe774074b5dcb9c9cbabab8\"; l_cap_id=\"MzZkN2Y2YTFlMDUwNGEwM2FlOTY3ZWRlYzZlZjljZWE=|1487597074|0a579dd625f18100b6a9d99235a62df1382ca30d\"; n_c=1";
    String WeiboCookie="SINAGLOBAL=1161948734771.414.1477235508150; __gads=ID=06753b5718cb2570:T=1486092893:S=ALNI_MbBlg1dGJnBiH3wUaBz-z1FkHvpRg; TC-Page-G0=42b289d444da48cb9b2b9033b1f878d9; _s_tentry=-; Apache=4022032213107.8467.1487265949724; ULV=1487265949747:1:1:1:4022032213107.8467.1487265949724:; TC-Ugrow-G0=5e22903358df63c5e3fd2c757419b456; SCF=Ag-z5a16ocDMbixwXf6tpXN8GBr7-htjlK2At5JfJOcHYpoYIhtCC0sloBGJiabM3ytTL2h7WU1yXL3He7tdjEs.; SUHB=0jBYVf5pyEw_-f; un=l7771304257@gmail.com; TC-V5-G0=c427b4f7dad4c026ba2b0431d93d839e; wb_g_upvideo_6135772102=1; WBtopGlobal_register_version=c689c52160d0ea3b; UOR=,,www.google.com; SUB=_2AkMv-h3UdcNhrAVQm_0Sy2njbI9H-jycL3QiAn7uJhMyAxgv7nIOqSVd0aZoWwc_cZVj3Enh33CMRnHW3Q..; SUBP=0033WrSXqPxfM72wWs9jqgMF55529P9D9WWhxDP70z2Pssqkfc7kai_k5JpV8g7NS0Mpe05Xeo-NBsU_qgLjTJSadBtt";
    String cookie2 = "\"q_c1=cbc3d23d80074748a14a5776df91ca03|1487597704000|1456008489000; __utma=51854390.628310160.1456008495.1487597727.1487776312.5; __utmv=51854390.000--|3=entry_date=20160220=1; _za=c4d486a2-ef9f-446c-aba5-869c0683b686; _zap=6f429c56-48e6-4904-8525-619b24ed43e9; __utmz=51854390.1487597727.4.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); l_cap_id=\"YzkyNjgyOWMyNzVmNGFhN2E4Y2QzNWExZjIwY2NiYjM=|1487776272|16c6e2b1347fb27e5354098c1350064158e41184\"; cap_id=\"MjZiMTFkYTNkNTZmNGY2ODg1ODA4NzNkYjUzNTUxNTc=|1487776272|4cd6f015beed633947a4bf1dad620f983bd1ff81\"; d_c0=\"AFCCi5ACVwuPTm8ugdsDXZWZAg1Af-oWSgo=|1487597704\"; aliyungf_tc=AQAAAN/41W8ExgIAFT5RQ7GgGgwUiIHv; _xsrf=a013df990d6824bbaad49dffad7daab3; nweb_qa=heifetz; n_c=1; __utmb=51854390.2.10.1487776312; __utmc=51854390; __utmt=1\"";
    private static  Set<Cookie> Cookies;
    public test()
    {
        Cookies = new HashSet<>();
    }
    public void GetContentByPhantomJS(String s)
    {
        System.setProperty(s2, s4);

        WebDriver webDriver = new PhantomJSDriver();
        ArrayList<Cookie> cookies = spiltCookie(cookie2);
        Iterator<Cookie> it = cookies.iterator();
        while(it.hasNext())
        {
            Cookie cc = it.next();
            //System.out.println(cc.getName()+" "+cc.getValue()+" ||"+cc.getDomain()+" "+cc.getPath());
            Cookie c = new Cookie(cc.getName(), cc.getValue(), ".zhihu.com", "/", null);
            webDriver.manage().addCookie(c);
            //
        }
        /*if(!Cookies.isEmpty())
        {
            Iterator<Cookie> it = Cookies.iterator();
            while(it.hasNext())
            {
                Cookie s = it.next();
                System.out.println(s.getName()+"  "+s.getValue()+"  "+s.getPath()+"   "+s.getDomain()+"   "+s.getExpiry());
                Cookie c = new Cookie(s.getName(), s.getValue(), ".zhihu.com", "/", null);
                webDriver.manage().addCookie(c);
            }
        }*/
        //webDriver.manage().timeouts().pageLoadTimeout(30,TimeUnit.SECONDS);
        webDriver.manage().timeouts().pageLoadTimeout(200,TimeUnit.SECONDS);
        webDriver.get(s);

        //webDriver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);

        /*Cookies = webDriver.manage().getCookies();
        Iterator<Cookie> it2 = Cookies.iterator();
        while(it2.hasNext())
        {
            Cookie c = it2.next();
            System.out.println(c.getName()+"  "+c.getValue()+"  "+c.getPath()+"   "+c.getDomain()+"   "+c.getExpiry());
        }*/
        WebElement webElement = webDriver.findElement(By.xpath("/html"));
        //System.out.println(webElement.getAttribute("outerHTML"));
        try {
            OutputStream o = new FileOutputStream("e:/weibo/d.html");
            o.write(webElement.getAttribute("outerHTML").getBytes());
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        webDriver.close();
    }
    public void GetContentByChrome(String s)
    {

        System.setProperty(s1, s3);

        WebDriver webDriver = new ChromeDriver();
        //Cookie c = new Cookie("_zap","2606fa60-887f-4301-81bb-a06a8379b834");
        //webDriver.manage().addCookie(c);
        ArrayList<Cookie> cookies = spiltCookie(cookie2);
        Iterator<Cookie> it = cookies.iterator();
        while(it.hasNext())
        {
            Cookie cc = it.next();
            //System.out.println(cc.getName()+" "+cc.getValue()+" ||"+cc.getDomain()+" "+cc.getPath());
            Cookie c = new Cookie(cc.getName(), cc.getValue(), ".zhihu.com", "/", null);
            webDriver.manage().addCookie(c);
            //
        }
        //webDriver.get("https://www.zhihu.com/question/49308935");
        webDriver.manage().timeouts().pageLoadTimeout(30,TimeUnit.SECONDS);
        webDriver.get(s);

        /*Cookies = webDriver.manage().getCookies();
        if(Cookies.size() != 0)
        {
            Iterator<Cookie> it = Cookies.iterator();
            while(it.hasNext())
            {
                Cookie d = it.next();
                System.out.println(d.getName()+"   "+d.getValue()+"   "+d.getDomain()+"   "+d.getPath()+"   "+d.getExpiry()+"   "+d.isSecure());
                webDriver.manage().addCookie(d);
            }
        }*/
        /*cookies= webDriver.manage().getCookies();

        Iterator<Cookie> it = cookies.iterator();
        while(it.hasNext())
        {
            Cookie cc = it.next();
            //System.out.println(cc.getName()+" "+cc.getValue()+" ||"+cc.getDomain()+" "+cc.getPath());
        }*/
        WebElement webElement = webDriver.findElement(By.xpath("/html"));

        System.out.println(webElement.getAttribute("outerHTML"));
        try {
            OutputStream o = new FileOutputStream("e:/weibo/d.html");
            o.write(webElement.getAttribute("outerHTML").getBytes());
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        webDriver.close();
    }
    public void GetContentByFirefox(String s)
    {
        System.setProperty("webdriver.gecko.driver", "C:\\Users\\LY\\Desktop\\geckodriver.exe");
        WebDriver webDriver = new MarionetteDriver();
        ArrayList<Cookie> cookies = spiltCookie(cookie2);
        Iterator<Cookie> it = cookies.iterator();
        while(it.hasNext())
        {
            Cookie cc = it.next();
            //System.out.println(cc.getName()+" "+cc.getValue()+" ||"+cc.getDomain()+" "+cc.getPath());
            Cookie c = new Cookie(cc.getName(), cc.getValue(), ".zhihu.com", "/", null);
            //webDriver.manage().addCookie(c);

        }
        webDriver.manage().timeouts().pageLoadTimeout(30,TimeUnit.SECONDS);
        webDriver.get(s);
        WebElement webElement = webDriver.findElement(By.xpath("/html"));

        System.out.println(webElement.getAttribute("outerHTML"));
        try {
            OutputStream o = new FileOutputStream("e:/weibo/d.html");
            o.write(webElement.getAttribute("outerHTML").getBytes());
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        webDriver.close();
    }


    public ArrayList<Cookie> spiltCookie(String s)
    {
        String[] cookies = s.split(";");
        ArrayList<Cookie> c = new ArrayList<>();
        for(int i = 0; i <cookies.length; ++i)
        {
            int index = cookies[i].indexOf("=");
            c.add(new Cookie(cookies[i].substring(0,index).replaceAll(" ",""),cookies[i].substring(index+1).replaceAll(" ","")));
            //System.out.println(cookies[i].substring(0,index)+"   "+cookies[i].substring(index+1).replaceAll(" ",""));

        }
        return c;

    }
}
