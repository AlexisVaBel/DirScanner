package com.dirscanner.model;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;


public class TestsFileModel {
    @Test

    public void testFilePath(){
        FileModelByDateyyMMdd fmodel = new FileModelByDateyyMMdd("/path2/file1", LocalDate.of(2018,11,17).toEpochDay(), "115");
        assertEquals("should be /path2/file1","/path2/file1",fmodel.getName());
    }

    @Test

    public void testFileSize(){
        FileModelByDateyyMMdd fmodel = new FileModelByDateyyMMdd("/path2/file1", LocalDate.of(2018,11,17).toEpochDay(), "115");
        assertEquals("should be 115","115",fmodel.getSize());
    }

//    @Test
//    @DisplayName("17.11.2018")
//    void testFileDate(){
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(new Date(2018,11,17));
//        FileModelByDateyyMMdd fmodel = new FileModelByDateyyMMdd("/path2/file1", LocalDate.of(2018,11,17).toEpochDay(), "115");
//        assertEquals(fmodel.getDate(),"17.11.2018","should be 17.11.2018");
//    }
}
