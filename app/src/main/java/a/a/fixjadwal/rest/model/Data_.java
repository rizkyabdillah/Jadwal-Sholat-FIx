package a.a.fixjadwal.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data_ {

    @SerializedName("date")
    @Expose
    private Date date;
    @SerializedName("data")
    @Expose
    private Data__ data;

    public Date getDate() {
        return date;
    }

    public Data__ getData() {
        return data;
    }

}