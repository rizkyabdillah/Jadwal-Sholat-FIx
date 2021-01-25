package a.a.fixjadwal.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Date {

    @SerializedName("text")
    @Expose
    private Text text;

    public Text getText() {
        return text;
    }

}