package a.a.fixjadwal.view.utility;

import android.Manifest;
import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Utility {
    
    private static double latitude, longitude;
    private static final Calendar c = Calendar.getInstance();

    public static final String[] PERMISSION = {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public static int getYear() {
        return c.get(Calendar.YEAR);
    }

    public static int getMonth() {
        return c.get(Calendar.MONTH);
    }

    public static int getDay() {
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static int getGMT() {
        TimeZone timeZone = TimeZone.getDefault();
        return timeZone.getOffset(new Date().getTime()) / 3600000;
    }

    public static double getLatitude() {
        return latitude;
    }

    public static void setLatitude(double latitude) {
        Utility.latitude = latitude;
    }

    public static double getLongitude() {
        return longitude;
    }

    public static void setLongitude(double longitude) {
        Utility.longitude = longitude;
    }
}
