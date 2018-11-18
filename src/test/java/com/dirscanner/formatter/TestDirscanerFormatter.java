package com.dirscanner.formatter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestDirscanerFormatter {
    @Test
    public void testSome3Params(){
        DirScannerFormatter formatter = new DirScannerFormatter();
        assertEquals (" ","[\nfile = hello\ndate = wor\nsize = ld]",String.format(formatter.getFormatter(),"hello","wor","ld"));
    }
}
