package com.kuansing.rndmjck.pelelangan.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kuansing.rndmjck.pelelangan.model.JadwalLelang;

import java.util.List;

/**
 * Created by rndmjck on 12/08/18.
 */

public class DataJadwalLelangResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<JadwalLelang> data = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<JadwalLelang> getData() {
        return data;
    }

    public void setData(List<JadwalLelang> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
