package com.dirscanner.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestsFileModel {
    @Test
    @DisplayName("/path2/file1")
    void testFilePath(){
        FileModelByDateyyMMdd fmodel = new FileModelByDateyyMMdd("/path2/file1", LocalDate.of(2018,11,17).toEpochDay(), "115");
        assertEquals(fmodel.getName(),"/path2/file1","should be /path2/file1");
    }

    @Test
    @DisplayName("115(Bytes)")
    void testFileSize(){
        FileModelByDateyyMMdd fmodel = new FileModelByDateyyMMdd("/path2/file1", LocalDate.of(2018,11,17).toEpochDay(), "115");
        assertEquals(fmodel.getSize(),"115","should be 115");
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
