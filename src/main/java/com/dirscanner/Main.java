package com.dirscanner;

import com.dirscanner.controller.BaseAppController;
import com.dirscanner.exception.EmptyCommandListException;
import com.dirscanner.service.*;


import java.util.Arrays;
import java.util.Timer;

/**
 * Created by Belyaev Alexei (lebllex) on 14.11.18.
 */

public class Main {
    public static void main(String[] args) throws InterruptedException {
        if(args.length < 1){
            // не тратим ничье время - без параметров не работаем.
            System.out.println("not enough actual parameters");
            System.out.println("use  /path1/toDir to include");
            System.out.println("use -/path2/toDir to exclude");
        }else {
            long curTime = System.currentTimeMillis();
            System.out.println("starting process");
            // таймер по расписанию будет вызывать метод, объекта, что приложение живо
            TermNotBoringService termNotBoring = new TermNotBoringService();
            Timer timer = new Timer(true);

            try {
                //контроллер пойдет своим потоком
                BaseAppController cmdController = new BaseAppController(Arrays.asList(args));
                Thread thr = new Thread(cmdController);
                thr.start();
                timer.scheduleAtFixedRate(termNotBoring,0,5*1000);
                // пока контроллер не закончит работу ждем
                thr.join();
            } catch (EmptyCommandListException e) {
                e.printStackTrace();
            } finally {
                timer.cancel();
            }
            timer.cancel();
            long lastTime = System.currentTimeMillis();
            System.out.printf("total time procs: %d millis\n",lastTime-curTime);
            System.out.println("process finished");
        }
    }
}
