package com.dirscanner.controller;

import com.dirscanner.exception.EmptyCommandListException;
import com.dirscanner.formatter.IOutFormatter;
import com.dirscanner.formatter.OutFormatterFactory;
import com.dirscanner.model.IDirFileModel;
import com.dirscanner.service.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.*;

/**
 * Created by Belyaev Alexei (lebllex) on 14.11.18.
 */
public class BaseAppController extends Thread{
    // параметры команд
    private static final String CMD_EXCL = "-";
    private static final String CMD_FOUTNAME = "-FN";
    //~ параметры команд

    private List<String> incLst;
    private List<String> excLst;
    private static List<String> lstCmds;
    private TreeSet<IDirFileModel> treeFiles;

    //
    private IResultService  resultSvc;
    private IOutFormatter   resultFormatter;


    public BaseAppController(List<String> lstAct) throws EmptyCommandListException {
        if(lstAct       == null)          throw  new EmptyCommandListException("For work need some command");


        this.lstCmds         = lstAct;

        treeFiles       = new TreeSet<>();
        incLst          = new ArrayList<>(lstCmds.size());
        excLst          = new ArrayList<>(lstCmds.size());
    }

    public boolean canProcess(){
        if(!lstCmds.isEmpty()){ // в конструкторе озаботился не null командой
            return makeIncsExcls();
        }else
        return false;
    };

    private boolean makeIncsExcls(){
        boolean bFlagExcl = false;
        boolean bFileName = false;
        String fileName = "";
        for(String str: lstCmds){
            if(bFileName){
                fileName  = str;
                bFileName = false;
                continue;
            }
            if(str.equals(CMD_EXCL)){
                bFlagExcl=true;
                continue;
            };
            if(str.equals(CMD_FOUTNAME)){
                bFileName = true;
                continue;
            }
            if(bFlagExcl){
                if(!incLst.remove(str))excLst.add(str); // если нет данного элемента в incl, вероятно excl элемент может быть сабдиром
            }
            else if(!incLst.contains(str)) incLst.add(str); // избавимся от повторов
        }
        // реализации по умолчанию, в парамертах предполагается указние иного (вывод в терминал, другой форматировщик)
        this.resultSvc = ResultFactory.getResultService("",new String[]{fileName,"UTF-8"});

        this.resultFormatter = OutFormatterFactory.getFormatter("");

        System.out.println("Total include list "+incLst);
        System.out.println("Total exclude exclude "+excLst);
        return (!incLst.isEmpty() && resultSvc !=null); // без excl работать можно
    };

    @Override
    public void run() {
        if(canProcess()){
            System.out.println("Starting cmds thread process ");
            int iThrCnt = Runtime.getRuntime().availableProcessors();
            //определим пул процессов
            ExecutorService threadPool = Executors.newFixedThreadPool(iThrCnt);

            // хотя спорный момент что не нужно именно завершение в определенном порядке
            // ведь дереву требуется время для балансировки

            List<Future<TreeSet<IDirFileModel>>> futureFModels  = new ArrayList<>();
            for(String str: incLst) {
                ICmdService  cmd = CmdFactory.getCmdService("");
                cmd.setCmdList(Arrays.asList(str));
                cmd.setFilterList(excLst);
                futureFModels.add(
                        CompletableFuture.supplyAsync(
                                () -> ((TreeSet<IDirFileModel>) cmd.getResults()),
                                threadPool)
                );
            };

            List<Future<TreeSet<IDirFileModel>>> futureProcessed = new ArrayList<>(futureFModels.size());

            // не предусмотрен защитный механизм выхода
            while(true) {

                // как только поток выполнился, забираем его данные
                for (Future<TreeSet<IDirFileModel>> fut : futureFModels) {
                    if(futureProcessed.contains(fut)) continue;
                    try {
                        if(fut.isDone()) {
                            treeFiles.addAll(fut.get());
                            futureProcessed.add(fut);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                };
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(futureFModels.equals(futureProcessed))break;
            };
                // выводим данные в зависимости от resultSvc в виде определенном resultFormatter
            for(IDirFileModel fmodel:treeFiles)
                resultSvc.appendToResult(String.format(resultFormatter.getFormatter(), fmodel.getName(), fmodel.getDate(), fmodel.getSize()));
            resultSvc.closeResult();
            threadPool.shutdown();
            System.out.println("result closed");

        };
    }
}
