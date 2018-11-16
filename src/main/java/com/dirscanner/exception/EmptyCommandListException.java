package com.dirscanner.exception;

/**
 * Created by Belyaev Alexei (lebllex) on 16.11.18.
 */
public class EmptyCommandListException extends Throwable {
    public EmptyCommandListException(String s) {
        super(s);
    }
}
