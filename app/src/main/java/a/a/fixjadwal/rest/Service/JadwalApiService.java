package a.a.fixjadwal.rest.Service;

import a.a.fixjadwal.rest.model.JadwalResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JadwalApiService {

//    @GET("api/APIH5tENuctWV/shalat/masehi/2021/1/25/-7.809948224116153/113.37262328137616/7")
    @GET("api/APIH5tENuctWV/shalat/masehi/{tahun}/{bulan}/{tanggal}/{latitude}/{longitude}/{gmt}")
    Call<JadwalResponse> getJadwalSholat(
        @Path("tahun") int tahun,
        @Path("bulan") int bulan,
        @Path("tanggal") int tanggal,
        @Path("latitude") double latitude,
        @Path("longitude") double longitude,
        @Path("gmt") int gmt
    );

}
