package com.vrp.tool.schedular;

import com.vrp.tool.models.File;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Import(Configuration1.class)
class MainSchedularTest {

    @Autowired
    MainSchedular mainSchedular;
    @Test
    public void test_TreeSet(){
            Comparator<Object> comparator=(f, ff)->Long.compare(((File) f).getId(),((File) ff).getId());
            TreeSet<Object> t=mainSchedular.collectedFiles.getOrDefault("1",new TreeSet<>(
                    )
            );
            HashSet<Object> set=new HashSet<>();
            t.add("name1".equals());
            assertTrue(set.contains(new File("name1",1)));

    }

}