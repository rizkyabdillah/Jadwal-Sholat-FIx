package a.a.fixjadwal.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.intentfilter.androidpermissions.PermissionManager;
import com.intentfilter.androidpermissions.models.DeniedPermissions;

import java.util.Arrays;
import java.util.List;

import a.a.fixjadwal.R;
import a.a.fixjadwal.rest.RestApi;
import a.a.fixjadwal.rest.model.Data__;
import a.a.fixjadwal.rest.model.JadwalResponse;
import a.a.fixjadwal.rest.model.Text;
import a.a.fixjadwal.view.utility.Utility;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView hijriah, masehi, kota, imsak, subuh, terbit, dhuha, dzuhur, ashar, maghrib, isya;
    private PermissionManager permissionManager;
    private ProgressDialog dialog;
    private AlertDialog.Builder alert;

    private boolean isLoaded = false;

    // Check gps provider is enabled
    private boolean isProviderEnable() {
        return SmartLocation.with(this).location().state().isAnyProviderAvailable();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hijriah = (TextView) findViewById(R.id.hijriah);
        masehi = (TextView) findViewById(R.id.masehi);
        kota = (TextView) findViewById(R.id.kota);
        imsak = (TextView) findViewById(R.id.imsak);
        subuh = (TextView) findViewById(R.id.subuh);
        terbit = (TextView) findViewById(R.id.terbit);
        dhuha = (TextView) findViewById(R.id.dhuha);
        dzuhur = (TextView) findViewById(R.id.dzuhur);
        ashar = (TextView) findViewById(R.id.ashar);
        maghrib = (TextView) findViewById(R.id.maghrib);
        isya = (TextView) findViewById(R.id.isya);

        Button change_date = (Button) findViewById(R.id.ubah_tanggal);

        // Where button change date is on clicking
        change_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if gps provider is enabled
                if(isProviderEnable()) {
                    // Where gps provider is enabled than show date picker dialog
                    new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                showProgressDialog();
                                // Show jadwal sholat using date obtained from date picker
                                // and using latitude, longitude from Utility temporary for to
                                // avoid location repeatedly
                                showJadwalSholat(year, month, dayOfMonth, Utility.getLatitude(), Utility.getLongitude(), Utility.getGMT());
                            }
                    }, Utility.getYear(), Utility.getMonth(), Utility.getDay()).show();
                } else {
                    // Where gps provider is not enabled, than show toast information
                    Toast.makeText(getApplicationContext(), "Gps anda belum aktif", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if gps provider is not enabled
        if(!isLoaded) {
            if (!isProviderEnable()) {
                // If is not enabled showing alert dialog
                alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Diperlukan akses lokasi!");
                alert.setMessage("GPS anda belum aktif, Aktifkan?");
                alert.setCancelable(false);
                alert.setPositiveButton("Aktifkan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Goto setting page for gps activated
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
                alert.show();
            } else {
                cekPermission();
            }
        }
    }

    // Request permission function
    private void cekPermission() {
        // Show permission dialog using library androidPermission
        permissionManager = PermissionManager.getInstance(MainActivity.this);
        permissionManager.checkPermissions(Arrays.asList(Utility.PERMISSION),
                new PermissionManager.PermissionRequestListener() {
            @Override
            public void onPermissionGranted() {
                // Write command here where permission is granted
                showLocation();
            }

            @Override
            public void onPermissionDenied(DeniedPermissions deniedPermissions) {
                // Write command here where permission is denied
                showDialogSetting();
            }
        });
    }

    private void showLocation() {
        // Start ProgressDialog
        showProgressDialog();

        // Get location using library SmartLocation
        SmartLocation.with(MainActivity.this).location().config(LocationParams.BEST_EFFORT).oneFix().start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {

                // Get location using reverse geocode
                SmartLocation.with(MainActivity.this).geocoding().reverse(location, new OnReverseGeocodingListener() {
                    @Override
                    public void onAddressResolved(Location location, List<Address> list) {
                        String text_kota = null;
                        if (list.size() > 0) {
                            String kab = list.get(0).getSubAdminArea();
                            String kecamatan = list.get(0).getLocality();
                            String negara = list.get(0).getCountryName();

                            // Setting text kota with kecamatan, kab - negara
                            text_kota = kecamatan.concat(", ").concat(kab).concat(" - ").concat(negara);
                        } else {
                            text_kota = "Location Not Found!";
                        }
                        kota.setText(text_kota);

                    }
                });

                // Save latitude and longitude into Utility as temporary
                Utility.setLatitude(location.getLatitude());
                Utility.setLongitude(location.getLongitude());

                // Show jadwal sholat using response API
                showJadwalSholat(
                        Utility.getYear(), Utility.getMonth(), Utility.getDay(),
                        location.getLatitude(),
                        location.getLongitude(), Utility.getGMT()
                );
            }
        });
    }

    private void showJadwalSholat(int year, int month, int day, double latitude, double longitude, int timezone) {
        // Rest API
        Call<JadwalResponse> responses = RestApi.jadwalService().getJadwalSholat(year, month + 1,day, latitude, longitude, timezone);
        responses.enqueue(new Callback<JadwalResponse>() {
            @Override
            public void onResponse(Call<JadwalResponse> call, Response<JadwalResponse> response) {
                if(response.isSuccessful()) {
                    Data__ jadwal = response.body().getData().getData().getData();
                    imsak.setText(jadwal.getShortImsak());
                    subuh.setText(jadwal.getShortShubuh());
                    terbit.setText(jadwal.getShortSyuruq());
                    dhuha.setText(jadwal.getShortDhuha());
                    dzuhur.setText(jadwal.getShortDhuhur());
                    ashar.setText(jadwal.getShortAshar());
                    maghrib.setText(jadwal.getShortMaghrib());
                    isya.setText(jadwal.getShortIsya());

                    Text dates = response.body().getData().getData().getDate().getText();
                    hijriah.setText(dates.getH());
                    masehi.setText(dates.getM());

                    isLoaded = true;
                }
                dialog.cancel();
            }

            @Override
            public void onFailure(Call<JadwalResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Data jadwal tidak ditemukan", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
    }

    private void showDialogSetting() {
        // Showing alert dialog where permission is denied
        alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Diperlukan akses lokasi!");
        alert.setMessage("Aplikasi ini membutuhkan akses lokasi, Apakah anda setuju?");
        alert.setCancelable(false);
        alert.setPositiveButton("Pengaturan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Showing setting page where setting button is pressed
                openSetting();
                finish();
            }
        });
        alert.setNegativeButton("Kembali", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Closing activity where back button is pressed
                dialog.dismiss();
                finish();
            }
        });
        alert.show();
    }

    private void showProgressDialog() {
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Mengambil data!");
        dialog.setCancelable(true);
        dialog.show();
    }

    private void openSetting() {
        // Open setting application page detail for allow permission
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, permissionManager.getResultCode());
    }
}