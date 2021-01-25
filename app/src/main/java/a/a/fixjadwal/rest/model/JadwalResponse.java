package a.a.fixjadwal.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JadwalResponse {

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("data")
    @Expose
    private Data data;

    public Integer getSuccess() {
        return success;
    }

    public Data getData() {
        return data;
    }

}