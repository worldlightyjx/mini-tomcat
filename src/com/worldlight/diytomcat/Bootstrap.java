package com.worldlight.diytomcat;

import cn.hutool.core.util.NetUtil;
import com.worldlight.diytomcat.http.Request;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @Author yangjiaxin
 * @Date 2023/5/16
 **/
public class Bootstrap {
    public static void main(String[] args) {
        try {
            int port = 18080;

            if(!NetUtil.isUsableLocalPort(port)){
                System.out.println("port is already in use");
                return;
            }

            ServerSocket ss = new ServerSocket(port);
            while (true){
                Socket s = ss.accept();
                Request request = new Request(s);
  /*              InputStream is = s.getInputStream();
                byte[] buffer = new byte[1024];
                is.read(buffer);
                String requestString = new String(buffer, StandardCharsets.UTF_8);*/
                System.out.println("Browser input info：\r\n"+request.getRequestString());
                System.out.println("uri："+request.getUri());

                OutputStream os = s.getOutputStream();
                String response_head = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n\r\n";
                String responseString = "Hello DIY Tomcat from worldlight.com";
                responseString = response_head + responseString;
                os.write(responseString.getBytes());
                os.flush();
                s.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
