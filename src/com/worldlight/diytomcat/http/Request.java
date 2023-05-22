package com.worldlight.diytomcat.http;

import cn.hutool.core.util.StrUtil;
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

    public Request(Socket socket) throws IOException {
        this.socket = socket;
        parseRequest();
        if(StrUtil.isEmpty(requestString)){
            return;
        }
        parseUri();
    }

    private void parseUri() {
        String temp;
        temp = StrUtil.subBetween(requestString," "," ");
        if(!StrUtil.contains(temp,'?')){
            uri = temp;
            return;
        }
        temp = StrUtil.subBefore(temp,'?',false);
        uri = temp;
    }

    private void parseRequest() throws IOException {
        InputStream is = this.socket.getInputStream();
        byte[] bytes = MiniBrowser.readBytes(is);
        requestString = new String(bytes, StandardCharsets.UTF_8);
    }

    public String getRequestString() {
        return requestString;
    }

    public String getUri() {
        return uri;
    }
}
