package com.scanner;

// ScanResult.java
public class ScanResult {
    public enum State {OPEN ,CLOSED,FILTERED,UNKNOWN}

    private final String host;
    private final int port;
    private final String banner;  
    private final State state;

    public ScanResult(String host, int port, State state, String banner) {
        this.host   = host;
        this.port   = port;
        this.banner = banner;
        this.state = state;

    }

    public boolean isOpen() {return state == state.OPEN;}
    public boolean isFiltered() {return state == State.FILTERED;}
    public String getHost()    { return host; }
    public int getPort()       { return port; }
    public String getBanner()  { return banner; }
    public State getState() {return state;}


    @Override
    public String toString() {
        return String.format("%-20s port %-6d %s  %s",
            host, port,state,
            banner != null?banner:"");

    }
}