package com.worldlight.diytomcat.catalina;

import com.worldlight.diytomcat.util.Constant;
import com.worldlight.diytomcat.util.ServerXMLUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Host {
    private String name;
    private Engine engine;
    private Map<String, Context> contextMap;

    public Host(String name, Engine engine) {
        this.contextMap = new HashMap<>();
        this.name = name;
        this.engine = engine;

        scanServerContextInXML();
        scanContextsOnWbAppsFolder();

    }

    private void scanServerContextInXML() {
        List<Context> contextList = ServerXMLUtil.getContexts();
        for (Context context : contextList) {
            contextMap.put(context.getPath(), context);
        }
    }

    private void scanContextsOnWbAppsFolder() {
        File[] folders = Constant.webappsFolder.listFiles();
        for (File folder : folders) {
            if (!folder.isDirectory()) {
                continue;
            }
            loadContext(folder);
        }
    }

    private void loadContext(File folder) {
        String path = folder.getName();
        if ("ROOT".equals(path)) {
            path = "/";
        } else {
            path = "/" + path;
        }
        String docBase = folder.getAbsolutePath();
        Context context = new Context(path, docBase);
        contextMap.put(context.getPath(), context);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Context> getContextMap() {
        return contextMap;
    }

    public void setContextMap(Map<String, Context> contextMap) {
        this.contextMap = contextMap;
    }

    public Context getContext(String path) {
        return contextMap.get(path);
    }
}
