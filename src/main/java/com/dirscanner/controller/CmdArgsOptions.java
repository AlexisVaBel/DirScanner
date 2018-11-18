package com.dirscanner.controller;

import java.util.HashMap;
import java.util.Map;

public class CmdArgsOptions {
    // параметры команд

    private static final String CMD_EXCL = "-";
    private static final String CMD_FOUTNAME = "-FN";
    private static final String CMD_SVCNAME = "-SVC";
    private static final String CMD_FRMTNAME = "-FMRT";

    //~ параметры команд

    private static final int BASE_OPTIONS_SIZE = 8;
    // недоделка, использовать вместо группы булевых и строковых операторов
    // для обработки командной строки
    private static Map<String,String> options_map;
    public CmdArgsOptions(){
        options_map = new HashMap<>(BASE_OPTIONS_SIZE);
        options_map.put(CMD_EXCL,"");
        options_map.put(CMD_FOUTNAME,"");
        options_map.put(CMD_SVCNAME,"");
        options_map.put(CMD_FRMTNAME,"");
    };

    public void setKeyMap(String strKey, String strVal){
        options_map.put(strKey,strVal);
    }

    public String getExclCmd(){return CMD_EXCL;}

    public String getSvc(){return options_map.get(CMD_SVCNAME);}
    public String getOutFname(){return options_map.get(CMD_FOUTNAME);}
    public String getOutFormatter(){return options_map.get(CMD_FRMTNAME);}

}
