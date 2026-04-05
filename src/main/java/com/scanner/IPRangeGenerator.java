package com.scanner;

// IPRangeGenerator.java
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class IPRangeGenerator {

    // Converts "192.168.1.1" → integer 3232235777
    private int ipToInt(String ip) throws UnknownHostException {
        byte[] bytes = InetAddress.getByName(ip).getAddress();
        int result = 0;
        for (byte b : bytes) {
            result = (result << 8) | (b & 0xFF);  // shift left, OR in next byte
        }
        return result;
    }

    // Converts integer 3232235777 → "192.168.1.1"
    private String intToIp(int ip) {
        return ((ip >> 24) & 0xFF) + "."
             + ((ip >> 16) & 0xFF) + "."
             + ((ip >> 8)  & 0xFF) + "."
             + ( ip        & 0xFF);
    }

    // Generates all ScanTasks for IP range × port range
    public List<ScanTask> generate(String startIp, String endIp,
                                    int startPort, int endPort)
                                    throws UnknownHostException {

        List<ScanTask> tasks = new ArrayList<>();

        int start = ipToInt(startIp);
        int end   = ipToInt(endIp);

        for (int ip = start; ip <= end; ip++) {
            String host = intToIp(ip);
            for (int port = startPort; port <= endPort; port++) {
                tasks.add(new ScanTask(host, port));
            }
        }

        return tasks;
    }
}