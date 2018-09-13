package com.holley.mvc.web.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;

public class MyTest {

    @Test
    public void testSpringLife() {
        ClassPathXmlApplicationContext a = new ClassPathXmlApplicationContext("testApplicationContext.xml");
        TestBean testBean = (TestBean) a.getBean("testBean");
        System.out.println(JSON.toJSONString(testBean));
        a.registerShutdownHook();
    }

    public static void main(String[] args) {
        List<String> l = new ArrayList<String>();
        List<String> l2 = new ArrayList<String>();
        for (int x = 0; x < 10; x++) {
            l.add(x + "");
        }

        for (Iterator<String> it = l.iterator(); it.hasNext();) {
            int tem = Integer.valueOf(it.next());
            if (tem < 5) {
                l2.add(tem + "");
                it.remove();
            }
        }
        System.out.println(JSON.toJSONString(l));
        System.out.println(JSON.toJSONString(l2));
    }
}
