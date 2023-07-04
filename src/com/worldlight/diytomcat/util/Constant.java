package com.worldlight.diytomcat.util;

import cn.hutool.system.SystemUtil;

import java.io.File;

/**
 * @Author yangjiaxin
 * @Date 2023/5/22
 **/
public class Constant {
    public final static String RESPONSE_HEAD_202 = "HTTP/1.1 200 OK\r\n" +
            "Content-Type: {}\r\n\r\n";

    public final static File webappsFolder = new File(SystemUtil.get(SystemUtil.USER_DIR),"webapps");
    public final static File rootFolder = new File(webappsFolder,"ROOT");

    public final static File confFolder = new File(SystemUtil.get(SystemUtil.USER_DIR),"conf");
    public final static File serverXmlFile = new File(confFolder,"server.xml");
}
