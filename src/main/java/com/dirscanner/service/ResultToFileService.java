package com.dirscanner.service;


import java.io.*;


/**
 * Created by Belyaev Alexei (lebllex) on 15.11.18.
 */
// реализация сервиса вывода обработки директорий
// пишем либо в defaultfile, либо в переданное имя
public class ResultToFileService implements IResultService {
    private static Writer writer;

    public ResultToFileService(String fname,String charset) throws FileNotFoundException, UnsupportedEncodingException {

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
