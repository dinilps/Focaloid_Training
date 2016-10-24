package com.focaloid.focaloid_training;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;


/**
 * Created by focaloid on 27/09/16.
 */
public class GmapActivity extends FragmentActivity implements MyAdapter.OnItemInteractionListener, OnMapReadyCallback, LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        View.OnClickListener {


    String TITLES[] = {"Home", "Camera", "Event", "Map", "Logout"};
    int ICONS[] = {R.drawable.home_white, R.drawable.ic_camera_white, R.drawable.event_white, R.drawable.map_white, R.drawable.logout_white};


    String NAME = "Jacob mathew";
    String EMAIL = "jacob@focaloid.com";
    int PROFILE = R.drawable.profile_pic;

    private Toolbar toolbar;
    private static int RESULT_LOAD_IMAGE = 1;

    private static final int CAMERA_REQUEST = 1888;
    String userChoosenTask;


    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout Drawer;

    ActionBarDrawerToggle mDrawerToggle;


    private static final String TAG = "error";
    //Our Map
    private GoogleMap mMap;

    //To store longitude and latitude from map
    private double longitude;
    private double latitude;

    private static final int PERMISSION_REQUEST_CODE = 101;

   /* //Buttons
    private ImageButton buttonSave;
    private ImageButton buttonCurrent;
    private ImageButton buttonView;*/

    //Google ApiClient
    private GoogleApiClient mGoogleApiClient;

    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;


    Marker mCurrLocationMarker;
    private Location mLastLocation;
    private Location location;

    View parentLayout;

    private final String DefaultUnameValue = "Jacob mathew";


    private final String DefaultEmailValue = "";

    private final String DefaultImageValue = "";

    private static final String PREFS_NAME = "fullname";
    private static final String PREF_EMAIL = "Username";
    private static final String PREF_IMAGE ="image_loc";

    String Profileimage;


    private static final long ONE_MIN = 1000 * 60;
    private static final long TWO_MIN = ONE_MIN * 2;
    private static final long FIVE_MIN = ONE_MIN * 5;
    private static final long POLLING_FREQ = 1000 * 30;
    private static final long FASTEST_UPDATE_FREQ = 1000 * 15;
    private static final float MIN_ACCURACY = 25.0f;
    private static final float MIN_LAST_READ_ACCURACY = 500.0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.map_layout);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(POLLING_FREQ);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_FREQ);

        SharedPreferences settings = getSharedPreferences("MyFB_PREFS",Activity.MODE_PRIVATE);

        // Get value
        NAME = settings.getString(PREFS_NAME, DefaultUnameValue);
        EMAIL = settings.getString(PREF_EMAIL, DefaultEmailValue);
        Profileimage=settings.getString(PREF_IMAGE, DefaultImageValue);
        //LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        ///locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,this);

        parentLayout = findViewById(R.id.root_view);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


        /*if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();
        }*/

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        buildGoogleApiClient();

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MyAdapter(TITLES, ICONS, NAME, EMAIL, Profileimage, this);

        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);


        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);


        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }

        };
        Drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        // mMap = mapFragment.getMap();
        //mMap.setMyLocationEnabled(true);

        // mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //LocationManager locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        /*List<String> providersList = locManager.getAllProviders();
        LocationProvider provider =locManager.getProvider(providersList.get(0));
        int precision = provider.getAccuracy();*/
        //Criteria req = new Criteria();
        //req.setAccuracy(Criteria.ACCURACY_FINE);





       /* if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }*/


        //getCurrentLocation();

        //mMap.getUiSettings().setAllGesturesEnabled(true);
        //mMap.getUiSettings().setMyLocationButtonEnabled(true);
        // mMap.setMyLocationEnabled(true);
        //mMap.getUiSettings().setZoomControlsEnabled(true);

        //MapsInitializer.initialize(this);
    }


    @Override
    public void onConnected(Bundle bundle) {
        //getCurrentLocation();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);


                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

                // startLocationUpdates();
                location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (location != null) {

                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                    //moveMap();
                }


            } else {

                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                //requestPermission();



                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

                Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                if (location != null) {
                    //Getting longitude and latitude
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                    //moveMap();
                }


                Toast.makeText(this, "error_permission_map", Toast.LENGTH_LONG).show();
            }

            //if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            //int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);


       /* if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


            mMap.setMyLocationEnabled(true);
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {

                longitude = mLastLocation.getLongitude();
                latitude = mLastLocation.getLatitude();
                //mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
                //mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
            }
        } else {

            // Show rationale and request permission.
        }*/
        } else {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);


            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (location != null) {
                //Getting longitude and latitude
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                //moveMap();
            }

        }


    }

    //api client

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }


    @Override
    public void onConnectionSuspended(int i) {

        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

       // mMap.getUiSettings().setAllGesturesEnabled(true);
        //mMap.getUiSettings().setMyLocationButtonEnabled(true);
        // mMap.setMyLocationEnabled(true);
        //mMap.getUiSettings().setZoomControlsEnabled(true);


        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }



        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {


            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


                location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                mMap.setMyLocationEnabled(true);

                if (location != null) {
                    //Getting longitude and latitude
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();

                    //moving the map to location
                    moveMap();
                }
                return;
            } else {
                requestPermission();

                //buildGoogleApiClient();

                mMap.setMyLocationEnabled(true);

                location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


                if (location != null) {
                    //Getting longitude and latitude
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                    moveMap();
                }

            }

        } else {


            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mMap.setMyLocationEnabled(true);

            if (location != null) {
                //Getting longitude and latitude
                longitude = location.getLongitude();
                latitude = location.getLatitude();

                //moving the map to location
                moveMap();
            }


        }




    }

    /*public static boolean isMarshMallow() {

        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);

    }*/

   /* //Getting current location
    private void getCurrentLocation() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {


            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


                Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                if (location != null) {
                    //Getting longitude and latitude
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();

                    //moving the map to location
                    moveMap();
                }
                return;
            } else {
                requestPermission();


                Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                if (location != null) {
                    //Getting longitude and latitude
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                    moveMap();
                }

            }
        } else {

            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (location != null) {
                //Getting longitude and latitude
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                moveMap();
            }

        }


    }*/

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            Toast.makeText(GmapActivity.this, "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {

            //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {


       /* switch (requestCode) {
            case FINE_LOCATION_RESULT:
                if (hasPermission(ACCESS_FINE_LOCATION)) {
                    permissionSuccess.setVisibility(View.VISIBLE);
                } else {
                    permissionsRejected.add(ACCESS_FINE_LOCATION);
                    makePostRequestSnack();
                }
                break;
            case COARSE_LOCATION_RESULT:
                if (hasPermission(ACCESS_COARSE_LOCATION)) {
                    permissionSuccess.setVisibility(View.VISIBLE);
                } else {
                    permissionsRejected.add(ACCESS_COARSE_LOCATION);
                    makePostRequestSnack();
                }
                break;
        }*/




        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Snackbar.make(parentLayout, "Permission Granted, Now you can access location data.", Snackbar.LENGTH_LONG).show();
                    if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    Snackbar.make(parentLayout, "Permission Denied, You cannot access location data.", Snackbar.LENGTH_LONG).show();


                }
                break;
        }
    }

    //Function to move the map
    private void moveMap() {

        if (location != null) {

            latitude = location.getLatitude();
            longitude = location.getLongitude();


            //String to display current latitude and longitude
            String msg = latitude + ", " + longitude;

            //Creating a LatLng Object to store Coordinates
            LatLng latLng = new LatLng(latitude, longitude);


            if (mCurrLocationMarker != null) {
                mMap.clear();
                //mCurrLocationMarker.remove();
            }

            //Adding marker to map
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            mCurrLocationMarker = mMap.addMarker(markerOptions);


           /* mMap.addMarker(new MarkerOptions()
                    .position(latLng) //setting position
                    //.draggable(true) //Making the marker draggable
                    .title("Current Location")); //Adding a title*/

            //Moving the camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            //Animating the camera
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17));

            //Displaying current coordinates in toast
            //Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } else {
            Log.d(TAG, "location is null ...............");
        }


    }


    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onBackPressed() {

        /*Intent intent=new Intent(GmapActivity.this,MainActivity.class);
        startActivity(intent);
        finish();*/
        super.onBackPressed();
    }

    @Override
    protected void onStart() {

        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub

        super.onResume();

        if (mGoogleApiClient.isConnected()) {
            //startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
            //startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        /*if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }*/
        //stopLocationUpdates();
    }

    @Override
    protected void onStop() {

        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }

    /*protected void startLocationUpdates() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


                PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                Log.d(TAG, "Location update started ..............: ");
            } else {

                requestPermission();
                PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                Log.d(TAG, "Location update started ..............: ");
            }
        } else {
            PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            Log.d(TAG, "Location update started ..............: ");
        }
    }*/

    @Override
    public void onLocationChanged(Location mlocation) {

        location = mlocation;
        if (location != null) {

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            //String to display current latitude and longitude
            //String msg = latitude + ", " + longitude;

            //Creating a LatLng Object to store Coordinates
            LatLng latLng = new LatLng(latitude, longitude);

            if (mCurrLocationMarker != null) {
                //mMap.clear();
                //mCurrLocationMarker.remove();
            }

            //Adding marker to map
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            mCurrLocationMarker = mMap.addMarker(markerOptions);


           /* mMap.addMarker(new MarkerOptions()
                    .position(latLng) //setting position
                    //.draggable(true) //Making the marker draggable
                    .title("Current Location")); //Adding a title*/

            //Moving the camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            //Animating the camera
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17));

            //Displaying current coordinates in toast
            //Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } else {
            Log.d(TAG, "location is null ...............");
        }
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    private void Home() {

        Intent intent=new Intent(GmapActivity.this,MainActivity.class);
        startActivity(intent);


        //LinearLayout layout = (LinearLayout)findViewById(R.id.linearlayoutfragment);
        // layout.removeAllViewsInLayout();
        /*HomeFragment homeFragment = new HomeFragment();
        //android.app.FragmentManager fm= getFragmentManager();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment, homeFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/


    }

    private void SetEvent() {
        //LinearLayout layout = (LinearLayout)findViewById(R.id.linearlayoutfragment);
        //layout.removeAllViewsInLayout();
        EventFragment eventFragment = new EventFragment();
        //android.app.FragmentManager fm= getFragmentManager();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment, eventFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }

    private void GetMap() {

        //LinearLayout layout = (LinearLayout)findViewById(R.id.linearlayoutfragment);
        //layout.removeAllViewsInLayout();


        Intent intent = new Intent(GmapActivity.this, GmapActivity.class);
        startActivity(intent);
        //finish();


    }

    private void Logout() {
        LoginManager.getInstance().logOut();
        clear();
        Intent logoutIntent = new Intent(GmapActivity.this, LoginActivity.class);
        startActivity(logoutIntent);
        GmapActivity.this.finish();
        Toast.makeText(GmapActivity.this, "Successfully logout", Toast.LENGTH_LONG).show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == CAMERA_REQUEST) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ImageView profileimageView = (ImageView) findViewById(R.id.circleView);
                profileimageView.setImageBitmap(photo);
            }

            if (requestCode == RESULT_LOAD_IMAGE) {
                onGallaryImageResult(data);

            }
        }
    }

    public void clear()
    {
        SharedPreferences prefs=getSharedPreferences("MyFB_PREFS",Activity.MODE_PRIVATE); // here you get your prefrences by either of two methods
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }
    private void signOut() {

        LoginActivity logAct = (LoginActivity )getApplicationContext();
        Auth.GoogleSignInApi.signOut(logAct.mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        //updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }

    private void onGallaryImageResult(Intent data) {
        Uri selectedImage = data.getData();
        InputStream imageStream = null;
        try {
            imageStream = getContentResolver().openInputStream(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ImageView profileImageView = (ImageView) findViewById(R.id.circleView);
        Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
        profileImageView.setImageBitmap(yourSelectedImage);

    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(GmapActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(GmapActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {


        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void galleryIntent() {

        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }


    @Override
    public void onCamButtonClicked() {

        selectImage();

    }

    @Override
    public void onItemClicked(int position) {

        switch (position) {
            case 1:
                Home();
                break;
            case 2:
                selectImage();
                break;
            case 3:
                SetEvent();
                break;
            case 4:
                GetMap();
                break;
            case 5:
                Logout();
                break;
            default:
                break;
        }
        Drawer.closeDrawer(GravityCompat.START);

    }
}
