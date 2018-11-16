package com.dirscanner.controller;

import com.dirscanner.exception.EmptyCommandListException;
import com.dirscanner.exception.NoCommandSvcExcpetion;
import com.dirscanner.model.FileModel;
import com.dirscanner.service.FilesExploreService;
import com.dirscanner.service.ICmdService;
import com.dirscanner.service.IResultService;
import com.dirscanner.service.ResultToFileService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.*;

/**
 * Created by Belyaev Alexei (lebllex) on 14.11.18.
 */
public class InputCmdsController extends Thread{
    // параметры команд
    private static final String CMD_EXCL = "-";
    private static final String CMD_FOUTNAME = "-FN";
    //~ параметры команд

    private List<String> incLst;
    private List<String> excLst;
    private static List<String> lstCmds;
    private TreeSet<FileModel> treeFiles;

    public InputCmdsController(List<String> lstAct) throws EmptyCommandListException, NoCommandSvcExcpetion {
        if(lstAct == null) throw new EmptyCommandListException("For work need some command");
        lstCmds     = lstAct;
        treeFiles = new TreeSet<>();
        incLst  = new ArrayList<>(lstCmds.size());
        excLst  = new ArrayList<>(lstCmds.size());
    }

    public boolean canProcess(){
        if(!lstCmds.isEmpty()){ // в конструкторе озаботился не null командой
            return makeIncsExcls();
        }else
        return false;
    };

    private boolean makeIncsExcls(){
        boolean bFlagExcl = false;
        for(String str: lstCmds){
            if(str.equals(CMD_EXCL)){
                bFlagExcl=true;
                continue;
            }
            if(bFlagExcl){
                if(!incLst.remove(str))excLst.add(str); // если нет данного элемента в incl, вероятно excl элемент может быть сабдиром
            }
            else if(!incLst.contains(str)) incLst.add(str); // избавимся от повторов
        }
        System.out.println("Total include "+incLst);
        System.out.println("Total exclude "+excLst);
        return !incLst.isEmpty(); // без excl работать можно
    };

    @Override
    public void run() {
        if(canProcess()){
            System.out.println("Shurely process ");
            int iThrCnt = Runtime.getRuntime().availableProcessors(); // приложение не должно грузить всех и вся
            // поэтому занимать все яйца, мягко говоря не красиво. не забыть уменьшить
            ExecutorService threadPool = Executors.newFixedThreadPool(iThrCnt);
            List<Future<TreeSet<FileModel>>> futureFiles = new ArrayList<>();
            for(String str: incLst) {
                ICmdService cmd = new FilesExploreService();
                List<String> lst = new ArrayList<String>();
                lst.add(str);
                cmd.setCmdList(lst);
                cmd.setFilterList(excLst);
                futureFiles.add(
                        CompletableFuture.supplyAsync(
                                () -> ((TreeSet<FileModel>) cmd.getResults()),
                                threadPool)
                );
            };
            IResultService result = null; // можно имя файла задать, где то в ключах обработать
            try {
                result = new ResultToFileService("");
                for(Future<TreeSet<FileModel>> fut: futureFiles){
                    try {
                        treeFiles.addAll(fut.get());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    };
                }
                for(FileModel fmodel:treeFiles)
                    // некрасиво, конкретный выводильщик должен сам знать как форматировать
                    // контроллеру нефиг об этом знать
                    result.addByResult(String.format("[\nfile = %s\ndate = %s\nsize = %s]", fmodel.getName(), fmodel.getDate(), fmodel.getSize()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }finally {
                result.closeResult();
                threadPool.shutdown();
                System.out.println("result closed");
            }
        };
    }
}
