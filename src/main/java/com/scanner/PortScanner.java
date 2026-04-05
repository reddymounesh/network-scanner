package com.scanner;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.Callable;




public class PortScanner implements Callable<ScanResult> {

    private final ScanTask task;
    private final int timeoutMs;

    public PortScanner(ScanTask task, int timeoutMs){
        this.task = task;
        this.timeoutMs=timeoutMs;

    }

    @Override
    public ScanResult call(){
        String host = task.getHost();
        int port = task.getPort();

        try (Socket socket = new Socket()){

            socket.connect(
                new InetSocketAddress(host,port),
                timeoutMs
            );

            String banner = grabBanner(socket);
            return new ScanResult(host, port, ScanResult.State.OPEN, banner);
        }
        catch(ConnectException e){
            return new ScanResult(host, port, ScanResult.State.CLOSED, null);

        }
        catch(SocketTimeoutException e){
            return new ScanResult(host,port,ScanResult.State.FILTERED,null);

        }
        catch(IOException e){
            return new ScanResult(host, port,ScanResult.State.UNKNOWN,null);

        }
    }

    private String grabBanner(Socket socket){
        try {
            socket.setSoTimeout(500);
            byte[] buffer = new byte[1024];
            int bytesRead = socket.getInputStream().read(buffer);
            if(bytesRead > 0){
                return new String(buffer,0,bytesRead).trim().replaceAll("[^\\x20-\\x7E]", "");

            }
        } 
        catch (IOException ignored) {

        }
        return null;
        

    }
    
}
