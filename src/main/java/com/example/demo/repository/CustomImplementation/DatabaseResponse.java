package com.example.demo.repository.CustomImplementation;

import java.util.LinkedList;
import java.util.Map;

public class DatabaseResponse {
    private LinkedList<Map<String, Object>> resultList;
    private ResponseCode responseCode = ResponseCode.SUCCES;
    private String message;

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public void setResultList(LinkedList<Map<String, Object>> resultList) {
        this.resultList = resultList;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public LinkedList<Map<String, Object>> getResultList() {
        return resultList;
    }    

    public String getMessage() {
        return message;
    }

    public boolean error() {
        return responseCode == ResponseCode.ERROR;
    }
}
