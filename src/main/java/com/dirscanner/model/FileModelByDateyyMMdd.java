package com.dirscanner.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Belyaev Alexei (lebllex) on 15.11.18.
 */
public class FileModelByDateyyMMdd implements IDirFileModel {
    private static DateFormat formatter;
    private final String fileName;
    private final String fileDate;
    private final String fileSize;


    public FileModelByDateyyMMdd(String fName, long dt , String fSize){
        // конкретна модель отличается способом представления данных о файле
        // в том числе форматированием
        // поэтому добавляем его в статик
        this.formatter= new SimpleDateFormat("yyyy.MM.dd");
        this.fileName = fName;
        this.fileDate = formatter.format(dt);
        this.fileSize = fSize;
    }

    @Override
    public String getName() {
        return fileName;
    }

    @Override
    public String getDate() {
        return fileDate;
    }

    @Override
    public String getSize() {
        return fileSize;
    }

    @Override
    public int compareTo(IDirFileModel that) {
        // сортируем все по одному принципу
        if(that == null) return 1;
        if(this == that) return 0;
        return (this.getName().compareTo(that.getName()));
    }
}
