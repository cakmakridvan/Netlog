package com.example.mobiltestcase.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.mobiltestcase.utils.Image;
import com.example.mobiltestcase.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class FragmentSend extends Fragment implements View.OnClickListener {

    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private static final int REQUEST_CODE_CAMERA = 102;
    private Geocoder geocoder;
    private List<Address> addresses;
    private TextView cityName, addressName;
    private ImageView imgGumruk;
    private static final int CAMERA_CAPTURE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_send, container, false);
        cityName = view.findViewById(R.id.txt_city);
        addressName = view.findViewById(R.id.txt_address);
        ImageView img = view.findViewById(R.id.imageView);
        img.setOnClickListener(this);
        PhotoView imgFullScreen = view.findViewById(R.id.img_fullscreen);
        imgFullScreen.setOnClickListener(this);
        ImageButton takePicture = view.findViewById(R.id.btn_take_picture);
        takePicture.setOnClickListener(this);
        imgGumruk = view.findViewById(R.id.imgView_gumruk);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        fetchLocation();
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        return view;
    }

    public void fetchLocation() {
        if ((ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;

                try {
                    addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String address = addresses.get(0).getAddressLine(0);
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();

                cityName.setText(state + "/" + country);
                addressName.setText(address);

                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
                if (mapFragment != null) {
                    mapFragment.getMapAsync(mMap -> {
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                        mMap.clear(); //clear old markers

                        CameraPosition googlePlex = CameraPosition.builder()
                                .target(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                                .zoom(10)
                                .bearing(0)
                                .tilt(45)
                                .build();

                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);

                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                                .title("Spider Man"));
                    });
                }

            }
        });
    }

    private void fetcCamera() {
        if ((ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
            return;
        }

        Intent camIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camIntent, CAMERA_CAPTURE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;

            case REQUEST_CODE_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetcCamera();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView:

            case R.id.img_fullscreen:
                Intent i = new Intent(getContext(), Image.class);
                startActivity(i);
                break;

            case R.id.btn_take_picture:
                fetcCamera();
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_CAPTURE) {
                // get the Uri for the captured image
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                if (bmp != null) {
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                }
                byte[] byteArray = stream.toByteArray();

                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                        byteArray.length);

                imgGumruk.setImageBitmap(bitmap);

            }
            // user is returning from cropping the image
            else if (requestCode == UCrop.REQUEST_CROP) {
                Uri imageUriResultCrop = UCrop.getOutput(data);
                if (imageUriResultCrop != null) {
                    imgGumruk.setImageURI(imageUriResultCrop);
                }

            }
        }
    }
}
