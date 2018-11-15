package com.dirscanner;

import com.dirscanner.model.FileModel;
import com.dirscanner.service.FilesExploreService;
import com.dirscanner.service.ICmdService;
import com.dirscanner.service.ResultMaker;
import com.dirscanner.service.TerminalNotBoring;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

/**
 * Created by Belyaev Alexei (lebllex) on 14.11.18.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        if(args.length < 1){
            System.out.println("not enough actual parameters");
        }else {


            TerminalNotBoring termNotBoring = new TerminalNotBoring();

            ICmdService cmd =  new FilesExploreService();
            Thread thr = new Thread(cmd);
            cmd.setCmdList(Arrays.asList(args));
            thr.start();
            Timer timer = new Timer(true);
            timer.scheduleAtFixedRate(termNotBoring,0,5*1000);

            thr.join();
            timer.cancel();

            List<FileModel> lst = (List<FileModel>) cmd.getResults();
            System.out.println("process finished");
            try {
                ResultMaker resultmaker = new ResultMaker("");
                resultmaker.setWriteData(lst);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
