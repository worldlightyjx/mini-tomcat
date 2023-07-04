package com.worldlight.diytomcat.catalina;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.LogFactory;
import cn.hutool.system.SystemUtil;
import com.worldlight.diytomcat.http.Request;
import com.worldlight.diytomcat.http.Response;
import com.worldlight.diytomcat.util.Constant;
import com.worldlight.diytomcat.util.ThreadPoolUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Server {
    private Service service;

    public Server() {
        this.service = new Service(this);
    }

    public void start() {
        logJVM();
        init();
    }

    private void init() {
        try {
            int port = 18080;

//            if (!NetUtil.isUsableLocalPort(port)) {
//                System.out.println("port is already in use");
//                return;
//            }

            ServerSocket ss = new ServerSocket(port);
            while (true) {
                Socket s = ss.accept();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {

                            Request request = new Request(s, service);
                            System.out.println("Browser input info：\r\n" + request.getRequestString());
                            System.out.println("uri：" + request.getUri());
                            String uri = request.getUri();
                            if (null == uri)
                                return;
                            System.out.println(uri);
                            Context context = request.getContext();
                            Response response = new Response();
                            if("/500.html".equals(uri)){
                                throw new Exception("this is a deliberately created exception");
                            }
                            if ("/".equals(uri)) {
                                String html = "Hello DIY Tomcat from worldlight.com";
                                response.getWriter().println(html);
                            } else {
                                String fileName = StrUtil.removePrefix(uri, "/");
                                File file = FileUtil.file(context.getDocBase(), fileName);
                                if (file.exists()) {
                                    String fileContent = FileUtil.readUtf8String(file);
                                    response.getWriter().println(fileContent);
                                    if (fileName.equals("timeConsume.html")) {
                                        ThreadUtil.sleep(1000);
                                    }
                                } else {
                                    handle400(s, uri);
                                    return;
                                }

                            }

                            handle200(s, response);
                        } catch (Exception e) {
                            LogFactory.get().error(e);
                            handle500(s,e);
                        } finally {
                            try {
                                if (!s.isClosed()) {
                                    s.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                };
                ThreadPoolUtil.run(runnable);
            }
        } catch (Exception e) {
            LogFactory.get().error(e);
            e.printStackTrace();
        }
    }

    private void logJVM() {
        Map<String, String> infos = new LinkedHashMap<>();
        infos.put("Server version", "How2J DiyTomcat/1.0.1");
        infos.put("Server built", "2020-04-08 10:20:22");
        infos.put("Server number", "1.0.1");
        infos.put("OS Name\t", SystemUtil.get("os.name"));
        infos.put("OS Version", SystemUtil.get("os.version"));
        infos.put("Architecture", SystemUtil.get("os.arch"));
        infos.put("Java Home", SystemUtil.get("java.home"));
        infos.put("JVM Version", SystemUtil.get("java.runtime.version"));
        infos.put("JVM Vendor", SystemUtil.get("java.vm.specification.vendor"));

        Set<String> keys = infos.keySet();
        for (String key : keys) {
            LogFactory.get().info(key + ":\t\t" + infos.get(key));
        }
    }

    private void handle200(Socket s, Response response) throws IOException {
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

    }

    private void handle400(Socket s, String uri) throws IOException {
        OutputStream os = s.getOutputStream();
        String responseText = StrUtil.format(Constant.textFormat_404, uri, uri);
        responseText = Constant.response_head_404 + responseText;
        byte[] responseBytes = responseText.getBytes(StandardCharsets.UTF_8);
        os.write(responseBytes);

    }

    protected void handle500(Socket s, Exception e) {
        try {
            StackTraceElement[] stackTraceElements = e.getStackTrace();
            StringBuilder sb = new StringBuilder();
            sb.append(e.toString());
            sb.append("\r\n");
            for (StackTraceElement stackTraceElement : stackTraceElements) {
                sb.append("\t");
                sb.append(stackTraceElement.toString());
                sb.append("\r\n");
            }
            String msg = e.getMessage();
            if (null != msg && msg.length() > 20) {
                msg = msg.substring(0, 19);
            }

            String text = StrUtil.format(Constant.textFormat_500, msg, e.toString(), sb.toString());
            text = Constant.response_head_500 + text;
            OutputStream os = s.getOutputStream();
            os.write(text.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e1) {
            e1.printStackTrace();
        }


    }

}
