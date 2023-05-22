package com.worldlight.diytomcat.test;

import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.StrUtil;
import com.worldlight.diytomcat.util.MiniBrowser;
import org.jsoup.internal.StringUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Year;

/**
 * @Author yangjiaxin
 * @Date 2023/5/18
 **/
public class TestTomcat {
    private static String ip = "127.0.0.1";
    private static int port = 18080;

    @BeforeClass
    public static void beforeClass(){
        if(NetUtil.isUsableLocalPort(port)){
            System.out.println("Please strat tomcat at port: "+port+" first");
            System.exit(1);
        }else{
            System.out.println("Ready to start test…………");

        }
    }

    @Test
    public void testHelloTomcat(){
        String html = getContent("/");
        Assert.assertEquals(html,"Hello DIY Tomcat from worldlight.com");
    }

    private String getContent(String uri){
        String url = StrUtil.format("http://{}:{}{}",ip,port, uri);
        String content = MiniBrowser.getContentString(url);
        return content;
    }
}
