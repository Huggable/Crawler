package pers.crawler.basic;
import org.apache.http.*;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;

import java.io.*;

public class BasicFunctions {

    private static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
    private static String USER_AGENT2 = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
    private static String DEFAULT_CHARSET = "GB2312,utf-8;q=0.7,*;q=0.7";
    private static String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
    private static String ACCEPT2 = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8";
    private static String COOKIES = "SINAGLOBAL=1161948734771.414.1477235508150; __gads=ID=06753b5718cb2570:T=1486092893:S=ALNI_MbBlg1dGJnBiH3wUaBz-z1FkHvpRg; TC-Page-G0=42b289d444da48cb9b2b9033b1f878d9; _s_tentry=-; Apache=4022032213107.8467.1487265949724; ULV=1487265949747:1:1:1:4022032213107.8467.1487265949724:; TC-Ugrow-G0=5e22903358df63c5e3fd2c757419b456; SCF=Ag-z5a16ocDMbixwXf6tpXN8GBr7-htjlK2At5JfJOcHYpoYIhtCC0sloBGJiabM3ytTL2h7WU1yXL3He7tdjEs.; SUHB=0jBYVf5pyEw_-f; un=l7771304257@gmail.com; TC-V5-G0=c427b4f7dad4c026ba2b0431d93d839e; wb_g_upvideo_6135772102=1; WBtopGlobal_register_version=c689c52160d0ea3b; UOR=,,www.google.com; SUB=_2AkMv-h3UdcNhrAVQm_0Sy2njbI9H-jycL3QiAn7uJhMyAxgv7nIOqSVd0aZoWwc_cZVj3Enh33CMRnHW3Q..; SUBP=0033WrSXqPxfM72wWs9jqgMF55529P9D9WWhxDP70z2Pssqkfc7kai_k5JpV8g7NS0Mpe05Xeo-NBsU_qgLjTJSadBtt";
	private static String COOKIES2 ="_zap=2606fa60-887f-4301-81bb-a06a8379b834; d_c0=\"ACBAJUyZvAqPTrSvmSqPw_3nT9TE3dMlXOs=|1477235344\"; _zap=2a73752c-7efd-4f60-b4c3-17efcf46c432; aliyungf_tc=AQAAAPAMyAtTGwkAFT5RQ7096WEZadSO; q_c1=a791cc21fdcf432c9759e8c61a6421b9|1487597074000|1487597074000; nweb_qa=heifetz; cap_id=\"YmE3OTczOTc1NWQ2NDYzYjljZjgzMDUxZTI2YjhiM2M=|1487597074|4877bc6c936489aeefe774074b5dcb9c9cbabab8\"; l_cap_id=\"MzZkN2Y2YTFlMDUwNGEwM2FlOTY3ZWRlYzZlZjljZWE=|1487597074|0a579dd625f18100b6a9d99235a62df1382ca30d\"; n_c=1";
    public static String getContentWeibo(String url)
    {
    	RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(globalConfig).setRedirectStrategy(new RedirectStrategy()
        {
            @Override
            public boolean isRedirected(HttpRequest arg0, HttpResponse arg1, HttpContext arg2) {
                return false;
            }

            @Override
            public HttpUriRequest getRedirect(HttpRequest arg0, HttpResponse arg1, HttpContext arg2) {
                return null;
            }
        }).build();

        HttpGet getHttp = new HttpGet(url);

        getHttp.addHeader("Accept-Charset", DEFAULT_CHARSET);
        getHttp.addHeader("Cookie",COOKIES);
        getHttp.addHeader("Accept", ACCEPT);
        getHttp.addHeader("User-Agent", USER_AGENT);
        StringBuilder sb = new StringBuilder();
        try {
        	CloseableHttpResponse response = client.execute(getHttp);
        	int statusCode = response.getStatusLine().getStatusCode();

        	if(statusCode == HttpStatus.SC_OK) {
                InputStream in = response.getEntity().getContent();

                BufferedReader buff = new BufferedReader(new InputStreamReader(in, "utf-8")); // 以字符流读入
                //OutputStream o = new FileOutputStream("e:/weibo/d.html");
                String lines = "";
                while ((lines = buff.readLine()) != null) {
                    sb.append(lines + "\n");
                }
                //o.write(sb.toString().getBytes());
            }
            else if(statusCode == HttpStatus.SC_MOVED_TEMPORARILY || statusCode == HttpStatus.SC_MOVED_PERMANENTLY)
            {
                Header[] headers = response.getHeaders("Location");
                if(headers != null && headers.length>0)
                {
                    String redirectUrl = headers[0].getValue();
                    return getContentWeibo(weiboRedirect(url,redirectUrl));
                }


            }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return sb.toString();
    }

    private static String weiboRedirect(String url,String redirectUrl)
    {
        return redirectUrl.substring(0,redirectUrl.indexOf("nick")+5)+url.substring(19,url.indexOf("?from"));

    }

    public static String getContentZhihu(String url)
    {
        RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();

        HttpGet getHttp = new HttpGet(url);
        getHttp.addHeader("Accept-Charset", DEFAULT_CHARSET);
        //getHttp.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36");
        //getHttp.addHeader("Cookie","_gat=1; nsfw-click-load=off; gif-click-load=on; _ga=GA1.2.1861846600.1423061484");
        //getHttp.addHeader("Cookie",COOKIES2);
        getHttp.addHeader("Accept", ACCEPT);
        getHttp.addHeader("User-Agent", USER_AGENT);
        StringBuilder sb = new StringBuilder();
        try {
            CloseableHttpResponse response = client.execute(getHttp);
            InputStream in = response.getEntity().getContent();
            //System.out.println("status: "+response.getStatusLine().getStatusCode());
            //Pattern charSetPattern = Pattern.compile("charset=(.+?)\"/>\n");

            BufferedReader buff = new BufferedReader(new InputStreamReader(in, "utf-8")); // 以字符流读入
            //OutputStream o = new FileOutputStream("e:/weibo/d.html");

            String lines = "";

            while ((lines = buff.readLine()) != null) {
                sb.append(lines + "\n");
            }
            //o.write(sb.toString().getBytes());
            response.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }
}
