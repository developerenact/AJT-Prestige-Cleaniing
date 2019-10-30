
package com.android.ajtprestigecleaning.model.JobsPojo;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Task implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("imageCount")
    @Expose
    private String imageCount;

    @SerializedName("textCount")
    @Expose
    private String textCount;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageCount() {
        return imageCount;
    }

    public void setImageCount(String imageCount) {
        this.imageCount = imageCount;
    }

    public String getTextCount() {
        return textCount;
    }

    public void setTextCount(String textCount) {
        this.textCount = textCount;
    }

    @SerializedName("logs")
    @Expose
    private List<Log> logs = null;
    private final static long serialVersionUID = 1970160359975546055L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }

}
