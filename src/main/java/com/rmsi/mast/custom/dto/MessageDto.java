package com.rmsi.mast.custom.dto;

import java.io.Serializable;

public class MessageDto implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String msg;
    private String date;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
