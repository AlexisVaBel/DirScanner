package com.dirscanner.service;

import com.dirscanner.model.FileModel;

import static java.nio.file.FileVisitResult.*;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Belyaev Alexei (lebllex) on 14.11.18.
 */
public class FilesExploreService extends SimpleFileVisitor<Path> implements ICmdService<List<String>,ArrayList<? super FileModel>>, Iterable<String>{

    private final PathMatcher matcher;
    private final DateFormat  formatter;

    private List<String>      lstCmds;
    private boolean           bSkipSubtree;

    // data container
    ArrayList<FileModel> lstContainer;


    public FilesExploreService(){
        formatter    = new SimpleDateFormat("yyyy.MM.dd");
        matcher      = FileSystems.getDefault().getPathMatcher("glob:"+"*");
        bSkipSubtree = false;
        lstContainer = new ArrayList<>();
    }

    @Override
    public void setCmdList(List<String> cmds) {
        // need first prepare what to skip or not
        lstCmds=cmds;
    }

    @Override
    public ArrayList<? super FileModel> getResults() {
        return lstContainer;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void run() {
        for(String cmd:lstCmds)
            try {
                Files.walkFileTree(Paths.get(cmd), this);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @Override
    public Iterator<String> iterator() {
        return null;
    }

//    @Benchmark - not used now, maybe next after
    void getFileInfo(Path fpath, FileTime ftime, long size){
        Path path = fpath.getFileName();
        if(path != null && matcher.matches(path)){
            synchronized (lstContainer){
                lstContainer.add(new FileModel(fpath.toString(),formatter.format(ftime.toMillis()),new Long(size).toString()));
            }
        }
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Thread thr = new Thread(new Runnable() {
            @Override
            public void run() {
                getFileInfo(file, attrs.creationTime(), attrs.size());
            }
        });
        thr.start();
        return  CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        System.err.println(exc);
        return CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
//        if(bSkipSubtree)
//            return SKIP_SUBTREE; // this place to dicide if continue, than will get subdirs fileszzzz
//        bSkipSubtree = true;
        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return CONTINUE;
    }

}
