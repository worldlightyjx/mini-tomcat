package com.worldlight.diytomcat.catalina;

import com.worldlight.diytomcat.util.ServerXMLUtil;

public class Service {
    private String name;
    private Engine engine;
    private Server server;


    public Service(Server server) {
        this.server = server;
        this.name = ServerXMLUtil.getServiceName();
        this.engine = new Engine(this);
    }

    public Engine getEngine() {
        return engine;
    }

    public Server getServer() {
        return server;
    }
}
