package com.maxim.us.converter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.maxim.us.configuration.AppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { BaseConverterImpl.class, AppConfiguration.class })
public class BaseConverterTest {

    @Autowired
    BaseConverter converter;

    @Test
    public void testEncodeSimple() {
        String encoded = converter.encode(312351L);
        assertEquals(312351L, converter.decode(encoded).longValue());
    }

    @Test(expected = NullPointerException.class)
    public void testEncodeNull() {
        converter.encode(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodeZero() {
        converter.encode(0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodeNegative() {
        converter.encode(-1351351L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDecodeEmpty() {
        converter.decode("        ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDecodeInvalid() {
        converter.decode("*&^*$@#&^@HDjksadnkasjd");
    }

    // TODO test different conf (check MAX_LONG also)
}
