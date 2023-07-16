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
        List<SFTPThread> threads=threadDispatcher.consume(6);
        assertEquals(5,threads.size());
    }

    @Test
    void getAvailableThreads() {
        assertEquals(5,threadDispatcher.getAvailableThreads());
    }
}