package a.a.fixjadwal.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Text {

    @SerializedName("M")
    @Expose
    private String m;
    @SerializedName("H")
    @Expose
    private String h;

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getH() {
        return h;
    }

    public void setH(String h) {
        this.h = h;
    }

}