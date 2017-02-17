package pers.crawler.zhihu;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class d {

    private String getHTML(String url) throws URISyntaxException, ClientProtocolException, IOException {
        //采用用户自定义cookie策略,不显示cookie rejected的报错
        CookieSpecProvider cookieSpecProvider = new CookieSpecProvider(){
            public CookieSpec create(HttpContext context){
                return new BrowserCompatSpec(){
                    @Override
                    public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {

                    }
                };
            }
        };
        Registry<CookieSpecProvider> r = RegistryBuilder
                .<CookieSpecProvider> create()
                .register(CookieSpecs.BEST_MATCH, new BestMatchSpecFactory())
                .register(CookieSpecs.BROWSER_COMPATIBILITY, new BrowserCompatSpecFactory())
                .register("cookie", cookieSpecProvider)
                .build();
        RequestConfig requestConfig = RequestConfig.custom()
                .setCookieSpec("cookie")
                .setSocketTimeout(5000) //设置socket超时时间
                .setConnectTimeout(5000)  //设置connect超时时间
                .build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCookieSpecRegistry(r)
                .setDefaultRequestConfig(requestConfig)
                .build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        String html = "html获取失败"; //用于验证是否取到正常的html
        try{
            CloseableHttpResponse response = httpClient.execute(httpGet);
            html = EntityUtils.toString(response.getEntity());
            //System.out.println(html); //打印返回的html         
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html;
    }

    private boolean isExistHTML(String html) throws InterruptedException {
        boolean isExist = false;
        Pattern pNoResult = Pattern.compile("\\\\u6ca1\\\\u6709\\\\u627e\\\\u5230\\\\u76f8"
                + "\\\\u5173\\\\u7684\\\\u5fae\\\\u535a\\\\u5462\\\\uff0c\\\\u6362\\\\u4e2a"
                + "\\\\u5173\\\\u952e\\\\u8bcd\\\\u8bd5\\\\u5427\\\\uff01"); //没有找到相关的微博呢，换个关键词试试吧！（html页面上的信息）
        Matcher mNoResult = pNoResult.matcher(html);
        if(!mNoResult.find()) {
            isExist = true;
        }
        return isExist;
    }

    private void writeWeibo2txt(String html, String savePath) throws IOException {
        File htmltxt = new File(savePath); //新建一个txt文件用于存放爬取的结果信息
        FileWriter fw = new FileWriter(htmltxt);
        BufferedWriter bw = new BufferedWriter(fw);
        //regex-----"id":\s"\d{19}",(\n*?)|(\s*?)"content":\s".*?",(\n*?)|(\s*?)"prettyTime":\s".*?"
        Pattern p = Pattern.compile("\"id\":\\s\"\\d{19}\",(\\n*?)|(\\s*?)\"content\":\\s\".*?\",(\\n*?)|(\\s*?)\"prettyTime\":\\s\".*?\"");
        Matcher m = p.matcher(html);
        while(m.find()) {
            System.out.println(m.group());
            bw.write(m.group());
        }
        bw.close();
    }

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        d crawler = new d();
        String searchword = "iPad"; //搜索关键字为"iPad"的微博html页面
        String s = "http://s.weibo.com/weibo/苹果手机&nodup=1&page=1";
        String html = crawler.getHTML(s);
        String savePath = "e:/weibo/d.txt"; //输出到txt文件的存放路径
        if(html != "html获取失败") {
            if(crawler.isExistHTML(html)) {
                System.out.println(html);
                crawler.writeWeibo2txt(html, savePath);
            }
        }
        //Pattern p = Pattern.compile("<script id=\"data_searchTweet\" type=\"application/json\">.+?<\script>"); //<script id="data_searchTweet" type="application/json">.*?<\script>
        //Matcher m = p.matcher(html);
        //html = crawler.getHTML("http://s.weibo.com/weibo/"+searchword+"&nodup=1&page="+1);

        //System.out.println(html);
    }

}