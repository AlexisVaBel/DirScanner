package com.dirscanner.formatter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestOutFormatterFactory {
    @Test
    public void testDirScannerFormatterReturned(){
        assertEquals("DirScannerFormatter",new DirScannerFormatter().getClass(),(OutFormatterFactory.getFormatter("")).getClass());
    }
}
