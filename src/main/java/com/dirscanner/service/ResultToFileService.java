package com.dirscanner.service;


import java.io.*;


/**
 * Created by Belyaev Alexei (lebllex) on 15.11.18.
 */
public class ResultToFileService implements IResultService {
    private static Writer writer;

    public ResultToFileService(String fname,String charset) throws FileNotFoundException, UnsupportedEncodingException {
        // упущение, кодировку лучше бы укзать в конструкторе
        String name;
        if(fname != null && !fname.isEmpty()) name = fname;
        else name = "defaultfile";
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(name),charset));
    }

    @Override
    public void appendToResult(String str) {
        try {
            writer.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeResult() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
