package com.dirscanner.service;

import com.dirscanner.model.FileModel;

import static java.nio.file.FileVisitResult.*;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by Belyaev Alexei (lebllex) on 14.11.18.
 */
public class FilesExploreService extends SimpleFileVisitor<Path> implements ICmdService<List<String>,TreeSet<FileModel>>{

    private final PathMatcher matcher;
    private final DateFormat  formatter;

    private List<String>      incLst;
    private List<String>      excLst;
    private String            strPath;

    // data container
    private TreeSet<FileModel> treeContainer;


    public FilesExploreService(){
        formatter     = new SimpleDateFormat("yyyy.MM.dd");
        matcher       = FileSystems.getDefault().getPathMatcher("glob:"+"*");
        treeContainer = new TreeSet<>();
    }

    @Override
    public void setCmdList(List<String> cmds) {
        incLst = cmds;
        strPath = incLst.get(0); // костыль, надо переделать
    }

    @Override
    public void setFilterList(List<String> cmds) {
        excLst = cmds;
    }


    @Override
    public TreeSet<FileModel> getResults(){
        try {
            Files.walkFileTree(Paths.get(strPath), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return treeContainer;
    }



    void getFileInfo(Path fpath, FileTime ftime, long size){
        Path path = fpath.getFileName();
        if(path != null && matcher.matches(path)){
                treeContainer.add(new FileModel(fpath.toString(),formatter.format(ftime.toMillis()),new Long(size).toString()));
        }
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        getFileInfo(file, attrs.creationTime(), attrs.size());
        return  CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        System.err.println(exc);
        return CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        for(String strExl: excLst)
            if(dir.toString().compareTo(strExl)==0)return SKIP_SUBTREE;
        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return CONTINUE;
    }

}
