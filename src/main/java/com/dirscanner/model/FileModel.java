package com.dirscanner.model;

/**
 * Created by Belyaev Alexei (lebllex) on 15.11.18.
 */
public class FileModel implements IDirFileModel {
    private final String fileName;
    private final String fileDate;
    private final String fileSize;

    public FileModel(String fName,String fDate,String fSize){
        this.fileName = fName;
        this.fileDate = fDate;
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
        if(that == null) return 1;
        if(this == that) return 0;
        // need to check how to sort them
        // in one directory by name and size?
        // or???
        return (this.getName().compareTo(that.getName()));
    }
}
