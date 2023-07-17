package com.vrp.tool.threads;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class ThreadDispatcher {

    private final List<SFTPThread> sftpThreads = new LinkedList<>();
    private final Set<SFTPThread> availableThreads = new HashSet<>();

    private volatile int availableSize = 0;

    private volatile int sftpthreadssize = 0;
    private final ReentrantLock lock = new ReentrantLock();

    private final CacheTable cacheTable=new CacheTable();

    private final int MIN_THREADS;
    private final int MAX_THREADS;

    public class CacheTable{
        public void register(SFTPThread thread){
            try {
                lock.lockInterruptibly();
                availableThreads.add(thread);
                availableSize++;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ThreadDispatcher() {
        String min = System.getProperty("min_SFTPThread");
        String max = System.getProperty("max_SFTPThread");
        MIN_THREADS = Integer.parseInt(min == null ? "5" : min);
        MAX_THREADS = Integer.parseInt(max == null ? "5" : max);
        produce();
    }


    private void produce() {
        if (sftpThreads.size() < 1) {
            IntStream.range(0, 5).forEach(i -> sftpThreads.add(new SFTPThread("SFTPThread" + i,cacheTable)));
            sftpThreads.stream().forEach(availableThreads::add);
            availableSize = 5;
            sftpthreadssize =5;
        }
    }

    public  void produce(int count) {
        try {
            lock.lockInterruptibly();
            IntStream.range(sftpthreadssize, sftpthreadssize + count).forEach(i -> {
                SFTPThread s=new SFTPThread("SFTPThread" + i,cacheTable);
                sftpThreads.add(s);
                availableThreads.add(s);
            });
            availableSize += count;
            sftpthreadssize +=count;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            lock.unlock();
        }

    }

    public SFTPThread consume() {
        try {
            lock.lockInterruptibly();
            if (availableSize < 1) {
                if(sftpthreadssize<MAX_THREADS){
                    produce(1);
                }else return null;
            }
            SFTPThread thread = availableThreads.stream().findFirst().orElse(null);
            availableThreads.remove(thread);
            availableSize--;
            return thread;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            lock.unlock();
        }
    }

    public List<SFTPThread> consume(int wantedCount) {

        try {
            lock.lockInterruptibly();
            int limit=0;
            if(wantedCount>availableSize){
                int diff=Math.abs(wantedCount-availableSize);
                while(diff>0) {
                    if (sftpthreadssize + diff <= MAX_THREADS) {
                        produce(diff);
                        break;
                    }
                    diff--;
                }
                limit=Math.min(availableSize,wantedCount);
            }else{
                limit=wantedCount;
            }
            List<SFTPThread> returnThreads = availableThreads.stream().limit(limit).collect(Collectors.toList());
            availableThreads.removeAll(returnThreads);
            availableSize -= limit;
            return returnThreads;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            lock.unlock();
        }
    }

    public synchronized int getAvailableThreads() {
        return availableSize;
    }


}
