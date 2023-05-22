package com.worldlight.diytomcat;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.StrUtil;
import com.worldlight.diytomcat.http.Request;
import com.worldlight.diytomcat.http.Response;
import com.worldlight.diytomcat.util.Constant;

import java.io.IOException;
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

            if (!NetUtil.isUsableLocalPort(port)) {
                System.out.println("port is already in use");
                return;
            }

            ServerSocket ss = new ServerSocket(port);
            while (true) {
                Socket s = ss.accept();
                Request request = new Request(s);
  /*              InputStream is = s.getInputStream();
                byte[] buffer = new byte[1024];
                is.read(buffer);
                String requestString = new String(buffer, StandardCharsets.UTF_8);
   */
                System.out.println("Browser input info：\r\n" + request.getRequestString());
                System.out.println("uri：" + request.getUri());

                Response response = new Response();
                String html = "Hello DIY Tomcat from worldlight.com";
                response.getWriter().println(html);

                handle200(s, response);
                /*OutputStream os = s.getOutputStream();
                String response_head = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n\r\n";
                String responseString = "Hello DIY Tomcat from worldlight.com";
                responseString = response_head + responseString;
                os.write(responseString.getBytes());
                os.flush();
                s.close();
                */
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handle200(Socket s, Response response) throws IOException {
        String contentTyepe = response.getContentType();
        String headText = Constant.RESPONSE_HEAD_202;
        headText = StrUtil.format(headText, contentTyepe);

        byte[] head = headText.getBytes();
        byte[] body = response.getBody();

        byte[] responseBytes = new byte[head.length + body.length];
        ArrayUtil.copy(head, 0, responseBytes, 0, head.length);
        ArrayUtil.copy(body, 0, responseBytes, head.length, body.length);

        OutputStream os = s.getOutputStream();
        os.write(responseBytes);
        s.close();
    }
}
