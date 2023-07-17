package com.vrp.tool.schedular;

import com.vrp.tool.models.File;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;



@Import(Configuration.class)
class MainSchedularTest {

    @Autowired
    MainSchedular mainSchedular;
    @Test
    public void test_TreeSet(){

            TreeSet<File> t=mainSchedular.collectedFiles.getOrDefault("1",new TreeSet<>(
                    (  f, ff)->Long.compare(f.getId(), ff.getId()))
            );
            t.add(new File("name1",1));
            assertTrue(t.contains("name1"));

    }

}