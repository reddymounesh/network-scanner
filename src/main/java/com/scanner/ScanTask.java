package com.scanner;

// ScanTask.java
public class ScanTask {
    private final String host;    // e.g. "192.168.1.1"
    private final int port;       // e.g. 80

    public ScanTask(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() { return host; }
    public int getPort()    { return port; }

    @Override
    public String toString() {
        return host + ":" + port;
    }
}

