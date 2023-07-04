package com.worldlight.diytomcat.http;

import cn.hutool.core.util.StrUtil;
import com.worldlight.diytomcat.Bootstrap;
import com.worldlight.diytomcat.catalina.Context;
import com.worldlight.diytomcat.catalina.Engine;
import com.worldlight.diytomcat.catalina.Host;
import com.worldlight.diytomcat.catalina.Service;
import com.worldlight.diytomcat.util.MiniBrowser;
import org.apache.tools.ant.taskdefs.Input;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @Author yangjiaxin
 * @Date 2023/5/22
 **/
public class Request {
    private String requestString;
    private String uri;

    private Socket socket;

    private Context context;



    private Service service;

    public Request(Socket socket,Service service) throws IOException {
        this.socket = socket;
        this.service = service;
        parseRequest();
        if (StrUtil.isEmpty(requestString)) {
            return;
        }
        parseUri();
        parseContext();
        if (!"/".equals(context.getPath())) {
            uri = StrUtil.removePrefix(uri, context.getPath());
        }
    }

    private void parseUri() {
        String temp;
        temp = StrUtil.subBetween(requestString, " ", " ");
        if (!StrUtil.contains(temp, '?')) {
            uri = temp;
            return;
        }
        temp = StrUtil.subBefore(temp, '?', false);
        uri = temp;
    }

    private void parseRequest() throws IOException {
        InputStream is = this.socket.getInputStream();
        byte[] bytes = MiniBrowser.readBytes(is);
        requestString = new String(bytes, StandardCharsets.UTF_8);
    }

    private void parseContext() {
        String path = StrUtil.subBetween(uri, "/", "/");
        if (null == path) {
            path = "/";

        } else {
            path = "/" + path;
        }
        Engine engine = service.getEngine();
        context = engine.getDefaultHost().getContext(path);
        if (null == context) {
            context = engine.getDefaultHost().getContext("/");
        }
    }

    public String getRequestString() {
        return requestString;
    }

    public String getUri() {
        return uri;
    }

    public Context getContext() {
        return context;
    }
}
