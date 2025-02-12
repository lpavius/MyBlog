package org.wildcodeschool.myblog.exception;

public class MaxLengthExceededException extends RuntimeException{
    public MaxLengthExceededException(String message) {
        super(message);
    }
}
