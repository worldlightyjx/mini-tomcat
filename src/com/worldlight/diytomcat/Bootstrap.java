package com.worldlight.diytomcat;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.StrUtil;
import com.worldlight.diytomcat.http.Request;
import com.worldlight.diytomcat.http.Response;
import com.worldlight.diytomcat.util.Constant;

import java.io.File;
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
                System.out.println("Browser input info：\r\n" + request.getRequestString());
                System.out.println("uri：" + request.getUri());
                String uri = request.getUri();
                if(null==uri)
                    continue;
                System.out.println(uri);
                Response response = new Response();
                if("/".equals(uri)){
                    String html = "Hello DIY Tomcat from worldlight.com";
                    response.getWriter().println(html);
                }else{
                    String fileName = StrUtil.removePrefix(uri,"/");
                    File file = FileUtil.file(Constant.rootFolder,fileName);
                    if(file.exists()){
                        String fileContent = FileUtil.readUtf8String(file);
                        response.getWriter().println(fileContent);
                    }else {
                        response.getWriter().println("File not found");
                    }

                }

                handle200(s, response);
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
