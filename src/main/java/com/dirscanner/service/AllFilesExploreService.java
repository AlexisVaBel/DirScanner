package com.dirscanner.service;

import com.dirscanner.model.FileModelByDateyyMMdd;
import com.dirscanner.model.IDirFileModel;

import static java.nio.file.FileVisitResult.*;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Belyaev Alexei (lebllex) on 14.11.18.
 */
public class AllFilesExploreService extends SimpleFileVisitor<Path> implements ICmdService<List<String>,TreeSet<IDirFileModel>>{

    private static PathMatcher matcher;

    // список директорий для обработки
    private List<String>      incLst;
    // общий список исключений
    private List<String>      excLst;
    private String            strPath;

    // data container
    private TreeSet<IDirFileModel> treeContainer;


    public AllFilesExploreService(){
        // реализация под все файлы, в том числе символьные ссылки
        matcher       = FileSystems.getDefault().getPathMatcher("glob:"+"*");
        treeContainer = new TreeSet<>();
    }

    @Override
    public void setCmdList(List<String> cmds) {
        incLst = cmds;
        // интерфейс позволяет работать со списком, но
        // данная реализация работает только с одним элементом inclList
        strPath = incLst.get(0);
    }

    @Override
    public void setFilterList(List<String> cmds) {
        excLst = cmds;
    }


    @Override
    public TreeSet<IDirFileModel> getResults(){
        try {
            // по обходу всего дерева файлов
            // получим заполненное дерево treeContainer
            Files.walkFileTree(Paths.get(strPath), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return treeContainer;
    }


    // забираем только оперделенные данные из файла
    // путь, дату и время создания, размер
    void getFileInfo(Path fpath, FileTime ftime, long size){
        Path path = fpath.getFileName();
        if(path != null && matcher.matches(path)){
            // заложился на фильтр ненужных файлов
            // все в одном exl листе
            for(String strExl: excLst)
                if(path.toString().compareTo(strExl)==0)return;
            treeContainer.add(new FileModelByDateyyMMdd(fpath.toString(),ftime.toMillis(),new Long(size).toString()));
        }
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        getFileInfo(file, attrs.creationTime(), attrs.size());
        return  CONTINUE;
    }

    // к примеру нет прав доступ к папке
    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        System.err.println(exc);
        return CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        // применяем оставшийся фильтр
        // если имееется, то к поддиректориям
        for(String strExl: excLst)
            if(dir.toString().compareTo(strExl)==0)return SKIP_SUBTREE;
        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return CONTINUE;
    }

}
