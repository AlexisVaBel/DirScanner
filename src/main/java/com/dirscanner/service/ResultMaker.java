package com.dirscanner.service;

import com.dirscanner.model.FileModel;

import java.io.*;
import java.util.List;

/**
 * Created by Belyaev Alexei (lebllex) on 15.11.18.
 */
public class ResultMaker {
    private static Writer writer;
    public ResultMaker(String fname) throws FileNotFoundException, UnsupportedEncodingException {
        String name;
        if(fname != null && !fname.isEmpty()) name = fname;
        else name = "defaultfile";
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(name),"UTF-8"));
    }

    public void  setWriteData(List<FileModel> lst) throws IOException {
        try {
            for (FileModel fmodel : lst) {
                writer.write(String.format("[\nfile = %s\ndate = %s\nsize = %s]", fmodel.getName(), fmodel.getDate(), fmodel.getSize()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            writer.close();
        }

    }
}
