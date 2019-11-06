package com.kuansing.rndmjck.pelelangan.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kuansing.rndmjck.pelelangan.model.CountBarang;

/**
 * Created by rndmjck on 12/08/18.
 */

public class CountBarangResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private CountBarang data;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public CountBarang getData() {
        return data;
    }

    public void setData(CountBarang data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
