package com.android.ajtprestigecleaning.model;

import java.io.Serializable;

public class JobModel implements Serializable {

    String header  = "";
    String id  = "";
    String address  = "";
    String work  = "";

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }
}
