package com.worldlight.diytomcat.test;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.StrUtil;
import com.worldlight.diytomcat.util.MiniBrowser;
import org.jsoup.internal.StringUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Year;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
        String html = getContentString("/");
        Assert.assertEquals(html,"Hello DIY Tomcat from worldlight.com");
    }

    @Test
    public void testaHtml() {
        String html = getContentString("/a.html");
        Assert.assertEquals(html,"Hello DIY Tomcat from worldlight.com");
    }

    @Test
    public void testaIndex(){
        String html = getContentString("/a/index.html");
        Assert.assertEquals(html,"Hello DIY Tomcat from index.html@a");
    }

    @Test
    public void testbIndex(){
        String html = getContentString("/b/index.html");
        Assert.assertEquals(html,"Hello DIY Tomcat from index.html@b");
    }

    @Test
    public void testTimeConsumeHtml() throws InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(20,20,
                60, TimeUnit.SECONDS,new LinkedBlockingDeque<>(10));
        TimeInterval timeInterval = DateUtil.timer();
        for (int i = 0; i < 3; i++) {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    getContentString("/timeConsume.html");
                }
            });
        }
        threadPoolExecutor.shutdown();
        threadPoolExecutor.awaitTermination(1,TimeUnit.HOURS);
        long duration = timeInterval.intervalMs();
        System.out.println(duration);
        Assert.assertTrue(duration<3000);

    }

    @Test
    public void test404(){
        String response = getHttpString("/not_exist.html");
        containAssert(response,"HTTP/1.1 404 Not Found");
    }

    @Test
    public void test500() {
        String response  = getHttpString("/500.html");
        containAssert(response, "HTTP/1.1 500 Internal Server Error");
    }

    private String getContentString(String uri){
        String url = StrUtil.format("http://{}:{}{}",ip,port, uri);
        String content = MiniBrowser.getContentString(url);
        return content;
    }

    private String getHttpString(String uri){
        String url = StrUtil.format("http://{}:{}{}",ip,port,uri);
        String http = MiniBrowser.getHttpString(url);
        return http;
    }

    private void containAssert(String html, String match){
        boolean isMatch = StrUtil.containsAny(html,match);
        Assert.assertTrue(isMatch);
    }



}
