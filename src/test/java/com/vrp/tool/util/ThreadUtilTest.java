package com.vrp.tool.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThreadUtilTest {

    @Test
    void getThreadCount(){
        assertEquals(2,ThreadUtil.getThreadCount(10));
    }

    @Test
    void getThreadCount_one(){
        assertEquals(1,ThreadUtil.getThreadCount(1));
    }

    @Test
    void getThreadCount_withsetproperty(){
        System.setProperty("threadperfiles","15");
        assertEquals(1,ThreadUtil.getThreadCount(15));
    }

}