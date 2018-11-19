package com.dirscanner.model;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;


public class TestsFileModel {
    @Test

    public void testFilePath(){
        FileModelByDateyyMMdd fmodel = new FileModelByDateyyMMdd("/path2/file1", 0L, "115");
        assertEquals("should be /path2/file1","/path2/file1",fmodel.getName());
    }

    @Test

    public void testFileSize(){
        FileModelByDateyyMMdd fmodel = new FileModelByDateyyMMdd("/path2/file1", 0L, "115");
        assertEquals("should be 115","115",fmodel.getSize());
    }

    public void testFileDate(){
        SimpleDateFormat dtFrmt = new SimpleDateFormat("yyyy.MM.dd");
        Date date = null;
        try {
            String strSrcDate = "2018.11.17";
            date = dtFrmt.parse(strSrcDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            FileModelByDateyyMMdd fmodel = new FileModelByDateyyMMdd("/path2/file1", calendar.getTimeInMillis(), "115");
            assertEquals("2018.11.17",strSrcDate,fmodel.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

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
