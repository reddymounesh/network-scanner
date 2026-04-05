package com.scanner;


import java.util.List;

public class Main{

    private static final int DEFAULT_THREADS = 200;
    private static final int DEFAULT_TIMEOUT = 200;
    private static final int DEFAULT_START_PORT = 1;
    private static final int DEFAULT_END_PORT = 1024;


    public static void main(String[] args) throws Exception {
        
        String startIp = args.length > 0 ? args[0]: "127.0.0.1";
        String endIp = args.length > 1 ? args[1]: startIp;
        int startPort = args.length > 2 ? Integer.parseInt(args[2]):DEFAULT_START_PORT;
        int endPort = args.length > 3 ? Integer.parseInt(args[3]): DEFAULT_END_PORT;
        int threads = args.length > 4 ? Integer.parseInt(args[4]): DEFAULT_THREADS;
        int timeoutsMs = args.length > 5 ? Integer.parseInt(args[5]): DEFAULT_TIMEOUT;

        printBanner(startIp,endIp,startPort,endPort,threads,timeoutsMs);

        long startTime = System.currentTimeMillis();

        IPRangeGenerator generator = new IPRangeGenerator();
        List<ScanTask> tasks = generator.generate(startIp, endIp, startPort, endPort);

        System.out.println("[*] Tasks generated: "+tasks.size());

        ScannerEngine engine = new ScannerEngine(threads, timeoutsMs);
        ResultCollector collector = engine.run(tasks);

        collector.printSummary();
        collector.exportJson("scan_results.json");
        

        

        long elapsed = System.currentTimeMillis() - startTime;
        System.out.printf("%n[*] Scan completed in %2f seconds%n" , elapsed/1000.0);





    } 

    private static void printBanner(String startIp,String endIp,int startPort,int endPort,int threads,int timeoutMs){

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║     Java Multithreaded Scanner       ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.printf("Target : %s -> %s%n",startIp,endIp);
        System.out.printf("Ports  : %d - %d%n",startPort,endPort);
        System.out.printf("Threads : %d%n",threads);
        System.out.printf("Timeout : %d ms%n%n",timeoutMs);

    }
    
    
}