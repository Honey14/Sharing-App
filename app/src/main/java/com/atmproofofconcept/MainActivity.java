package com.atmproofofconcept;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 11;
    @BindView(R.id.enter_text)
    EditText enter_text;
    @BindView(R.id.image_getLocation)
    ImageView image_getLocation;
    @BindView(R.id.image_getPictures)
    ImageView image_getPictures;
    @BindView(R.id.image_delete)
    ImageView image_delete;
    @BindView(R.id.text_latLong)
    TextView text_latLong;
    @BindView(R.id.head_latLong)
    TextView head_latLong;
    @BindView(R.id.RecyclerView_Images)
    RecyclerView recyclerView_Images;
    @BindView(R.id.button_submit)
    Button button_submit;
    String text_entered;
    ArrayList<Bitmap> bitmaps = new ArrayList<>();
    double longitude, latitude;
    String latlong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        String[] PERMISSIONS = {
                android.Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
        };
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }
        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/atmpoc/");
        if (!directory.exists())
            directory.mkdirs();

        text_entered = enter_text.getText().toString();
        image_getLocation.setOnClickListener(this);
        image_getPictures.setOnClickListener(this);
        image_delete.setOnClickListener(this);
        button_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.image_getLocation:
                getLocation();
                break;
            case R.id.image_getPictures:
                clickImages();
                break;
            case R.id.image_delete:
                deleteLattitudeLongitude();
                break;
            case R.id.button_submit:
                Intent intent = new Intent(MainActivity.this,ListActivity.class);
                intent.putExtra("latitudeLongitude",latlong);
                startActivity(intent);
                break;
        }
    }

    private void deleteLattitudeLongitude() {
        image_getLocation.setAlpha((float) 1);
        image_getLocation.setClickable(true);
        text_latLong.setVisibility(View.GONE);
        head_latLong.setVisibility(View.GONE);
        image_delete.setVisibility(View.GONE);
        text_latLong.setText("");
    }

    private void clickImages() {
        String[] PERMISSIONS = {
                android.Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Opening Camera", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Image Captured Successfully.", Toast.LENGTH_SHORT).show();
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                bitmaps.add(imageBitmap);
                recyclerView_Images.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                AdapterImages adapterImages = new AdapterImages(bitmaps);
                recyclerView_Images.setAdapter(adapterImages);
            }
        }
    }

    private void getLocation() {

//        if (gpsTracker.getIsGPSTrackingEnabled()) {

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
//            gpsTracker.showSettingsAlert();

        } else {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                latlong = latitude + " , " + longitude;
                text_latLong.setText(latlong);
            } else {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
            }
            image_getLocation.setAlpha((float) 0.5);
            image_getLocation.setClickable(false);
            text_latLong.setVisibility(View.VISIBLE);
            head_latLong.setVisibility(View.VISIBLE);
            image_delete.setVisibility(View.VISIBLE);
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            latlong = latitude + " , " + longitude;
            text_latLong.setText(latlong);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}
