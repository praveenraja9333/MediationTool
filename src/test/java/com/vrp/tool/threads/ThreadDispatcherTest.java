package com.vrp.tool.threads;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class ThreadDispatcherTest {

    private static ThreadDispatcher threadDispatcher;
    @BeforeAll
    private static void setUp(){
        System.setProperty("max_SFTPThread","10");
        threadDispatcher=new ThreadDispatcher();
    }
    @Test
    void produce() {
    }

    @Test
    void consume() {
        SFTPThread sftpThread=threadDispatcher.consume();
        assertTrue(sftpThread instanceof SFTPThread);
    }

    @Test
    void consume_withSize() {
        List<SFTPThread> threads=threadDispatcher.consume(3);
        List<SFTPThread> threads1=threadDispatcher.consume(3);
        List<SFTPThread> threads2=threadDispatcher.consume(5);
        assertEquals(3,threads.size());
        assertEquals(3,threads1.size());
        assertEquals(4,threads2.size());
    }

    @Test
    void consume_withThreeThread() throws ExecutionException, InterruptedException {
        Callable<List<SFTPThread>> r1=()->threadDispatcher.consume(3);
        Callable<List<SFTPThread>> r2=()->threadDispatcher.consume(3);
        Callable<List<SFTPThread>> r3=()->threadDispatcher.consume(4);
        ExecutorService executor= Executors.newFixedThreadPool(3);
        Future<List<SFTPThread>> f1 =executor.submit(r1);
        Future<List<SFTPThread>> f2 =executor.submit(r2);
        Future<List<SFTPThread>> f3 =executor.submit(r3);
        assertEquals(3,f1.get().size());
        assertEquals(3,f2.get().size());
        assertEquals(4,f3.get().size());
    }

    @Test
    void consume_withThreeThread_NotFair() throws ExecutionException, InterruptedException {
        Callable<List<SFTPThread>> r1=()->threadDispatcher.consume(3);
        Callable<List<SFTPThread>> r2=()->threadDispatcher.consume(10);
        Callable<List<SFTPThread>> r3=()->threadDispatcher.consume(7);
        ExecutorService executor= Executors.newFixedThreadPool(3);
        Future<List<SFTPThread>> f1 =executor.submit(r1);
        Future<List<SFTPThread>> f2 =executor.submit(r2);
        Future<List<SFTPThread>> f3 =executor.submit(r3);
        assertTrue(10==f2.get().size()||7==f3.get().size()||7==f2.get().size());
    }

    @Test
    void getAvailableThreads() {
        assertEquals(5,threadDispatcher.getAvailableThreads());
    }
}