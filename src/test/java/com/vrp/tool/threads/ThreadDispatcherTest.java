package com.vrp.tool.threads;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
    void getAvailableThreads() {
        assertEquals(5,threadDispatcher.getAvailableThreads());
    }
}