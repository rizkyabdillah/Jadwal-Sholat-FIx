package a.a.fixjadwal.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data__ {

    @SerializedName("short-imsak")
    @Expose
    private String shortImsak;
    @SerializedName("short-shubuh")
    @Expose
    private String shortShubuh;
    @SerializedName("short-syuruq")
    @Expose
    private String shortSyuruq;
    @SerializedName("short-dhuha")
    @Expose
    private String shortDhuha;
    @SerializedName("short-dhuhur")
    @Expose
    private String shortDhuhur;
    @SerializedName("short-ashar")
    @Expose
    private String shortAshar;
    @SerializedName("short-maghrib")
    @Expose
    private String shortMaghrib;
    @SerializedName("short-isya")
    @Expose
    private String shortIsya;

    public String getShortImsak() {
        return shortImsak;
    }

    public String getShortShubuh() {
        return shortShubuh;
    }

    public String getShortSyuruq() {
        return shortSyuruq;
    }

    public String getShortDhuha() {
        return shortDhuha;
    }

    public String getShortDhuhur() {
        return shortDhuhur;
    }

    public String getShortAshar() {
        return shortAshar;
    }

    public String getShortMaghrib() {
        return shortMaghrib;
    }

    public String getShortIsya() {
        return shortIsya;
    }

}