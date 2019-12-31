package com.shop.stock.response;

import lombok.Data;

@Data
public class Response {

    public final static String SUCCESS = "success";
    public final static String FAIL = "fail";

    private int code;

    private String msg;

    private Object data;

    public Response(){
        this.code = 200;
        this.msg = this.SUCCESS;
    }

    public Response(String msg){
        this.code = 200;
        this.msg = msg;
    }

    public Response(String msg,Object data){
        this.code = 200;
        this.msg = msg;
        this.data = data;
    }

}
