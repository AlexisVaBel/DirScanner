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

    // по сути неважно в каком порядке обрабатывать список
    // главное чтобы он был вообще был обработан
    private List<String> incLst;
    private List<String> excLst;
    private static List<String> lstCmds;

    // всю остальную работу на себя возьмет данное хранилище
    // сортировка, а также отсутствие привязки к размеру множества файлов
    // минусы в затратах времени на балансировку дерева
    // про сортировку знает реализация IDIrFileModel
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
        if(!lstCmds.isEmpty()){
            return makeIncsExcls();
        }else
        return false;
    };

    private boolean makeIncsExcls(){
        boolean bFlagExcl = false;

        CmdArgsOptions argOptions = new CmdArgsOptions();
        String strCmdExcl = argOptions.getExclCmd();
        String strPrev = "";


        for(String str: lstCmds){
            if(strPrev.contains("-") && strPrev.length()>1)argOptions.setKeyMap(strPrev,str);
            strPrev = str;
            if(str.equals(strCmdExcl)){
                bFlagExcl=true;
                continue;
            };

            if(bFlagExcl){
                if(!incLst.remove(str))excLst.add(str); // если нет данного элемента в incl, вероятно excl элемент может быть сабдиром
            }
            else if(!incLst.contains(str)) incLst.add(str); // избавимся от повторов, могут случайно быть набраны
        }
        // реализации по умолчанию, в парамертах предполагается указние иного (вывод в терминал, другой форматировщик)
        this.resultSvc = ResultFactory.getResultService(argOptions.getSvc(),new String[]{argOptions.getOutFname(),"UTF-8"});

        this.resultFormatter = OutFormatterFactory.getFormatter(argOptions.getOutFormatter());

        return (!incLst.isEmpty() && resultSvc !=null && resultFormatter != null); // без excl работать можно
    };

    @Override
    public void run() {
        if(canProcess()){
            int iThrCnt = Runtime.getRuntime().availableProcessors();
            //определим пул процессов
            ExecutorService threadPool = Executors.newFixedThreadPool(iThrCnt);


            // список объектов, что вернут нам свои деревья файлов
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

            // хотя спорный момент что не нужно именно завершение в определенном порядке
            // ведь дереву требуется время для балансировки
            List<Future<TreeSet<IDirFileModel>>> futureProcessed = new ArrayList<>(futureFModels.size());

            // не предусмотрен защитный механизм выхода
            while(true) {

                // как только поток выполнился, забираем его данные
                // и больше его не трогаем
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
                // списки идентичны, значит все обработано
                if(futureFModels.equals(futureProcessed))break;
            };
                // выводим данные в зависимости от resultSvc в виде определенном resultFormatter
            for(IDirFileModel fmodel : treeFiles)
                resultSvc.appendToResult(String.format(resultFormatter.getFormatter(), fmodel.getName(), fmodel.getDate(), fmodel.getSize()));
            resultSvc.closeResult();
            threadPool.shutdown();
        };
    }
}
