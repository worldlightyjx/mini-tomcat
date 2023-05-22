package com.worldlight.diytomcat.http;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/**
 * @Author yangjiaxin
 * @Date 2023/5/22
 **/
public class Response {
    private StringWriter stringWriter;
    private PrintWriter writer;

    private String contentType;

    public Response() {
        this.stringWriter = new StringWriter();
        this.writer = new PrintWriter(stringWriter);
        this.contentType = "text/html";
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getBody(){
        String content = stringWriter.toString();
        byte[] body = content.getBytes(StandardCharsets.UTF_8);
        return body;
    }
}
