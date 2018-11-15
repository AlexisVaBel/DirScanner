package com.dirscanner.model;

/**
 * Created by Belyaev Alexei (lebllex) on 15.11.18.
 */
public interface IDirFileModel extends Comparable<IDirFileModel> {
    public String getName();
    public String getDate();
    public String getSize();

    @Override
    int compareTo(IDirFileModel that);
}
