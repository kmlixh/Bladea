package com.janyee.bladea.Http;

/**
 * Created by kmlixh on 14/11/1.
 */
public class NTHttpException extends RuntimeException {
    public NTHttpException(Throwable cause) {
        super(cause);
    }

    public NTHttpException(String msg) {
        super(msg);
    }
}
