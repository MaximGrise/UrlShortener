package com.maxim.us.mongo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.maxim.us.configuration.AppConfiguration;
import com.maxim.us.converter.BaseConverterImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { MongoDbUrlClient.class, BaseConverterImpl.class, AppConfiguration.class })
public class MongoDbUrlClientTest {

    @Autowired
    MongoDbUrlClient client;

    @Test
    public void testInsert() throws Exception {
    }

    @Test
    public void testGet() throws Exception {
    }
}
