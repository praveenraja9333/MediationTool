package com.vrp.tool.schedular;

import com.vrp.tool.models.File;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;



//@Import(Configuration1.class)
class MainSchedularTest {
    static MainSchedular mainSchedular;
    @BeforeAll
    public static void setUp(){
          mainSchedular=new MainSchedular();
    }



    @Test
    public void test_Set() {
        Comparator<File> comparator = (f, ff) -> Long.compare(f.getId(), ff.getId());
        Set<File> t = mainSchedular.collectedFiles.getOrDefault("1", new HashSet<>());
        t.add(new File("name3", 3));
        t.add(new File("name10", 10));
        t.add(new File("name2", 2));
        t.add(new File("name1", 1));
        File f1 = new File("dummy", 11);
        assertFalse(t.contains(f1));
        f1.setName("name1");
        assertTrue(t.contains(f1));
    }

}