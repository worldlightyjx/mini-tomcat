package com.worldlight.diytomcat;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.LogFactory;
import cn.hutool.system.SystemUtil;
import com.worldlight.diytomcat.catalina.*;
import com.worldlight.diytomcat.http.Request;
import com.worldlight.diytomcat.http.Response;
import com.worldlight.diytomcat.util.Constant;
import com.worldlight.diytomcat.util.ServerXMLUtil;
import com.worldlight.diytomcat.util.ThreadPoolUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @Author yangjiaxin
 * @Date 2023/5/16
 **/
public class Bootstrap {

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }






}
