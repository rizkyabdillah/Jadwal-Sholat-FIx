package a.a.fixjadwal.rest;

import a.a.fixjadwal.rest.Service.JadwalApiService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApi {

    private static final String URL_JADWAL = "https://kalenderindonesia.com/";
    public static JadwalApiService jadwalService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(URL_JADWAL)
            .addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(JadwalApiService.class);
    }

}
