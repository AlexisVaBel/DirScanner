package com.dirscanner.service;

import com.dirscanner.model.FileModel;

import java.io.*;
import java.util.List;

/**
 * Created by Belyaev Alexei (lebllex) on 15.11.18.
 */
public class ResultToFileService implements IResultService {
    private static Writer writer;
    private List<FileModel> lstResult;

    public ResultToFileService(String fname) throws FileNotFoundException, UnsupportedEncodingException {
        String name;
        if(fname != null && !fname.isEmpty()) name = fname;
        else name = "defaultfile";
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(name),"UTF-8"));
    }

    @Override
    public void setResultProducer(ICmdService cmdService) {
        lstResult = (List<FileModel>)cmdService.getResults();
    }

    @Override
    public void processResult() {
        try {
            for (FileModel fmodel : lstResult) {
                writer.write(String.format("[\nfile = %s\ndate = %s\nsize = %s]", fmodel.getName(), fmodel.getDate(), fmodel.getSize()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addByResult(String str) {
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
