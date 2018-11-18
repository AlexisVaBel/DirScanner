package com.dirscanner.controller;

import java.util.HashMap;
import java.util.Map;

public class CmdArgsOptions {
    private static final int BASE_OPTIONS_SIZE = 8;
    // недоделка, использовать вместо группы булевых и строковых операторов
    // для обработки командной строки
    private static Map<String,String> options_map;
    public CmdArgsOptions(){
        options_map = new HashMap<>(BASE_OPTIONS_SIZE);
        options_map.put("-","");
        options_map.put("-FN","");
        options_map.put("-SVC","");
        options_map.put("-FMRT","");
    };

    public void setKeyMap(String strKey, String strVal){
        options_map.put(strKey,strVal);
    }

    public String getSvc(){return options_map.get("-SVC");}
    public String getOutFname(){return options_map.get("-FN");}
    public String getOutFormatter(){return options_map.get("-FMRT");}

}
