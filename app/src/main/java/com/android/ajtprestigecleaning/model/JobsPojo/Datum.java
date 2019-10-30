
package com.android.ajtprestigecleaning.model.JobsPojo;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;



    private boolean isHeader;



    private String headerName;

    public boolean getHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("startDateTime")
    @Expose
    private String startDateTime;
    @SerializedName("recurring")
    @Expose
    private String recurring;
    @SerializedName("endDateTime")
    @Expose
    private String endDateTime;
    @SerializedName("checkList")
    @Expose
    private List<CheckList> checkList = null;
    @SerializedName("hoursDaily")
    @Expose
    private String hoursDaily;
    @SerializedName("hours")
    @Expose
    private List<Hour> hours = null;
    @SerializedName("notes")
    @Expose
    private String notes;

    @SerializedName("jobStatus")
    @Expose
    private String jobStatus;

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("jobType")
    @Expose
    private String jobType;

    @SerializedName("carpets")
    @Expose
    private String carpets;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getCarpets() {
        return carpets;
    }

    public void setCarpets(String carpets) {
        this.carpets = carpets;
    }

    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    private final static long serialVersionUID = 707689893806533950L;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getRecurring() {
        return recurring;
    }

    public void setRecurring(String recurring) {
        this.recurring = recurring;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public List<CheckList> getCheckList() {
        return checkList;
    }

    public void setCheckList(List<CheckList> checkList) {
        this.checkList = checkList;
    }

    public String getHoursDaily() {
        return hoursDaily;
    }

    public void setHoursDaily(String hoursDaily) {
        this.hoursDaily = hoursDaily;
    }

    public List<Hour> getHours() {
        return hours;
    }

    public void setHours(List<Hour> hours) {
        this.hours = hours;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
