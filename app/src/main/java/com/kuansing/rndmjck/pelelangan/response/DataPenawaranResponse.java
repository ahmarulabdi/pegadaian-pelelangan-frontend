package com.kuansing.rndmjck.pelelangan.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kuansing.rndmjck.pelelangan.model.Penawaran;

import java.util.List;

/**
 * Created by rndmjck on 07/08/18.
 */

public class DataPenawaranResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<Penawaran> data = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Penawaran> getData() {
        return data;
    }

    public void setData(List<Penawaran> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
