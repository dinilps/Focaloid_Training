package com.focaloid.focaloid_training;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements MyAdapter.OnItemInteractionListener{

        String TITLES[] = {"Home","Camera","Event","Map","Logout"};
        int ICONS[] = {R.drawable.home_white,R.drawable.ic_camera_white,R.drawable.event_white,R.drawable.map_white,R.drawable.logout_white};



        String NAME = "";
        String EMAIL = "";

        private AnimatedCircleLoadingView animatedCircleLoadingView;

        private Toolbar toolbar;
        private static int RESULT_LOAD_IMAGE = 1;

        private static final int CAMERA_REQUEST = 1888;
        String userChoosenTask;


        private static final String PREFS_NAME = "fullname";
        private static final String PREF_EMAIL = "Username";
        private static final String PREF_IMAGE ="image_loc";



        private final String DefaultUnameValue = "Jacob mathew";


        private final String DefaultEmailValue = "";

        private final String DefaultImageValue = "R.drawable.profile_pic";

        String Profileimage;


        RecyclerView mRecyclerView;
        RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager mLayoutManager;
        DrawerLayout Drawer;

        ActionBarDrawerToggle mDrawerToggle;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);


        SharedPreferences settings = getSharedPreferences("MyFB_PREFS",Activity.MODE_PRIVATE);

        // Get value
        NAME = settings.getString(PREFS_NAME, DefaultUnameValue);

        Profileimage=settings.getString(PREF_IMAGE, DefaultImageValue);


        animatedCircleLoadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);
        startLoading();
        startPercentMockThread();


    /* Assinging the toolbar object ot the view
    and setting the the Action bar to our toolbar
     */
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,Profileimage,this);

        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);


        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);



        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.openDrawer,R.string.closeDrawer){

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





        Home();


        }

        private void startLoading() {
                animatedCircleLoadingView.startDeterminate();
        }

        private void startPercentMockThread() {
                Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                                try {
                                        Thread.sleep(1500);
                                        for (int i = 0; i <= 100; i++) {
                                                Thread.sleep(65);
                                                changePercent(i);
                                        }
                                } catch (InterruptedException e) {
                                        e.printStackTrace();
                                }
                        }
                };
                new Thread(runnable).start();
        }

        private void changePercent(final int percent) {
                runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                animatedCircleLoadingView.setPercent(percent);
                        }
                });
        }

        public void resetLoading() {
                runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                animatedCircleLoadingView.resetLoading();
                        }
                });
        }


        private  void Home()
        {
                setActionBarTitle("Home");
                //LinearLayout layout = (LinearLayout)findViewById(R.id.linearlayoutfragment);
               // layout.removeAllViewsInLayout();
                HomeFragment homeFragment=new HomeFragment();
                //android.app.FragmentManager fm= getFragmentManager();
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment,homeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


        }

        private void SetEvent()
        {
                //LinearLayout layout = (LinearLayout)findViewById(R.id.linearlayoutfragment);
                //layout.removeAllViewsInLayout();
                EventFragment eventFragment=new EventFragment();
                //android.app.FragmentManager fm= getFragmentManager();
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment,eventFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


        }

        private void GetMap()
        {
                setActionBarTitle("Map");


                Intent intent=new Intent(MainActivity.this,GmapActivity.class);
                startActivity(intent);



        }

        private void Logout()
        {
                LoginManager.getInstance().logOut();
                clear();

                //LoginActivity logAct = (LoginActivity )getBaseContext();
                //((LoginActivity)logAct).signOut();
                //signOut();
                Intent logoutIntent= new Intent(MainActivity.this,LoginActivity.class);
                startActivity(logoutIntent);
                MainActivity.this.finish();
        }
        public void clear()
        {
                SharedPreferences prefs=getSharedPreferences("MyFB_PREFS",Activity.MODE_PRIVATE); // here you get your prefrences by either of two methods
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.commit();
        }

        /*public void signOutFromGplus() {
                MyApplication myApp = (MyApplication )getApplicationContext();

                if (myApp.mGoogleApiClient.isConnected()) {
                        Plus.AccountApi.clearDefaultAccount(myApp.mGoogleApiClient);
                        myApp.mGoogleApiClient.disconnect();
                        myApp.mGoogleApiClient.connect();
                }
        }*/

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

      /*  private void updateUI(boolean signedIn) {
                if (signedIn) {
                        findViewById(R.id.sign_in_button).setVisibility(View.GONE);
                        findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
                } else {
                        //mStatusTextView.setText(R.string.signed_out);

                        findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
                        findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
                }
        }*/

protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode ==RESULT_OK && data !=null) {
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

private void onGallaryImageResult(Intent data)
        {
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



        public void setActionBarTitle(String title) {
                getSupportActionBar().setTitle(title);
        }





@Override
public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
        }

@Override
public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement



                switch(id) {
                        case R.id.action_shareapp:
                                Intent shareapps=new Intent(MainActivity.this,ShareappActivity.class);
                                startActivity(shareapps);

                                break;
                       // case R.id.menu_compass:
                             //   btnName = "Compass";
                              //  break;
                        //case R.id.menu_help:
                              //  btnName = "Help";
                              //  break;

                }

               // Snackbar.make(layout, "Button " + btnName, Snackbar.LENGTH_SHORT).show();

        //return true;

        return super.onOptionsItemSelected(item);
        }

private void selectImage() {
final CharSequence[] items = { "Take Photo", "Choose from Library",
        "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
@Override
public void onClick(DialogInterface dialog, int item) {
        boolean result=Utility.checkPermission(MainActivity.this);

        if (items[item].equals("Take Photo")) {
        userChoosenTask="Take Photo";
        if(result)
        cameraIntent();
        } else if (items[item].equals("Choose from Library")) {
        userChoosenTask="Choose from Library";
        if(result)
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

private void galleryIntent()
        {

        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,RESULT_LOAD_IMAGE);
        }


@Override
public void onCamButtonClicked() {

        selectImage();
        }

@Override
public void onItemClicked(int position) {
        switch (position)
        {
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

        //Toast.makeText(this,"Clicked "+position,Toast.LENGTH_SHORT).show();
        }

        }
