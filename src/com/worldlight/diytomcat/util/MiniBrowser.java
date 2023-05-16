package com.worldlight.diytomcat.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author yangjiaxin
 * @Date 2023/5/16
 **/
public class MiniBrowser {
    public static void main(String[] args) {
        String url = "http://static.how2j.cn/diytomcat.html";
        String contentString = getContentString(url, false);
        System.out.println(contentString);
        String httpString= getHttpString(url,false);
        System.out.println(httpString);
    }

    public static byte[] getContentBytes(String url) {
        return getContentBytes(url, false);
    }

    public static String getContentString(String url) {
        return getContentString(url, false);
    }


    public static String getContentString(String url, boolean gzip) {
        byte[] result = getContentBytes(url, gzip);
        if (null == result)
            return null;
        return new String(result, StandardCharsets.UTF_8).trim();
    }

    public static byte[] getContentBytes(String url, boolean gzip) {
        byte[] response = getHttpBytes(url, gzip);
        byte[] doubleReturn = "\r\n\r\n".getBytes();

        int pos = -1;
        for (int i = 0; i < response.length - doubleReturn.length; i++) {
            byte[] temp = Arrays.copyOfRange(response, i, i + doubleReturn.length);

            if (Arrays.equals(temp, doubleReturn)) {
                pos = i;
                break;
            }
        }
        if (-1 == pos)
            return null;

        pos += doubleReturn.length;

        byte[] result = Arrays.copyOfRange(response, pos, response.length);
        return result;
    }

    public static String getHttpString(String url, boolean gzip) {
        byte[] bytes = getHttpBytes(url, gzip);
        return new String(bytes).trim();
    }

    public static String getHttpString(String url) {
        return getHttpString(url, false);
    }

    public static byte[] getHttpBytes(String url, boolean gzip) {
        byte[] result = null;
        try {
            URL u = new URL(url);
            Socket client = new Socket();
            int port = u.getPort();
            if (-1 == port) {
                port = 80;
            }
            InetSocketAddress inetSocketAddress = new InetSocketAddress(u.getHost(), port);
            client.connect(inetSocketAddress, 1000);
            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("Host", u.getHost() + ":" + port);
            requestHeaders.put("Accept", "text/html");
            requestHeaders.put("Connection", "close");
            requestHeaders.put("User-Agent", "worldlight mini browser / java1.8");

            if (gzip) {
                requestHeaders.put("Accept-Encoding", "gzip");
            }
            String path = u.getPath();
            if (path.length() == 0) {
                path = "/";
            }

            String firstLine = "GET " + path + " HTTP/1.1\r\n";

            StringBuffer httpRequestString = new StringBuffer();
            httpRequestString.append(firstLine);

            Set<String> httpRequestHeader = requestHeaders.keySet();
            for (String header : httpRequestHeader) {
                String headerLine = header + ":" + requestHeaders.get(header) + "\r\n";
                httpRequestString.append(headerLine);
            }
            PrintWriter printWriter = new PrintWriter(client.getOutputStream(), true);
            printWriter.println(httpRequestString);
            InputStream is = client.getInputStream();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];

            while (true) {
                int length = is.read(buffer);
                if (-1 == length) {
                    break;
                }
                byteArrayOutputStream.write(buffer, 0, length);
                if (length != 1024) {
                    break;
                }
            }

            result = byteArrayOutputStream.toByteArray();
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }
}
