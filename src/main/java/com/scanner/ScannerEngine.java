package com.scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;




public class ScannerEngine {

    private final int threadCount;
    private final int timeoutMs;
    private final ResultCollector collector;

    public ScannerEngine(int threadCount,int timeoutMs){
        this.threadCount = threadCount;
        this.timeoutMs = timeoutMs;
        this.collector = new ResultCollector();

    }

    public ResultCollector run(List<ScanTask> tasks) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        AtomicInteger completed = new AtomicInteger(0);
        int total = tasks.size();


        List<Future<ScanResult>> futures  = new ArrayList<>();

        for(ScanTask task: tasks){
            Future<ScanResult> future = executor.submit(new PortScanner(task,timeoutMs));
            futures.add(future);

        }

        for(Future<ScanResult> future:futures){
            try{
                ScanResult result = future.get();
                if(result.isOpen()){
                    collector.add(result);
                    System.out.printf(" [OPEN] %-20s port %d %s%n",result.getHost(),result.getPort(),result.getBanner() != null ? result.getBanner():"");

                    

                }

                int done = completed.incrementAndGet();
                printProgress(done,total);


            }catch(ExecutionException e){
                completed.incrementAndGet();


            }
        }

        System.out.println();
        

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.MINUTES);

        return collector;

    }

    private void printProgress(int done ,int total){
        int percent = (int) ((done/(double) total)*100);
        int filled = percent /5;

        String bar = "█".repeat(filled) + "░".repeat(20 - filled);

        System.out.printf("\r  [%s] %d%% (%d/%d)", bar, percent, done, total);

    }
    
}
