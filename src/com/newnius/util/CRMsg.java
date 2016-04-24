package com.newnius.util;

/**
 * @author Newnius
 * @version 0.1.0(General)
 * Dependencies
 *  com.newnius.util.CRObject
 *
 */
public class CRMsg extends CRObject{
    private int errno = 0;
    private String message = "";

    public CRMsg(int errno){
        super();
        this.errno = errno;
    }

    public CRMsg(int errno, String message){
        super();
        this.errno = errno;
        this.message = message;
    }

    public int getErrno() {
        return errno;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
