package com.focaloid.focaloid_training;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by dinil on 20-08-2016.
 */
public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{


    Button submit;
    EditText editTextUserName,editTextPassword;
    TextView register,guestuser;
    CallbackManager callbackManager;
    LoginButton loginButton;
    ProfileTracker profileTracker;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;

    DbAdapter dbAdapter;

     public GoogleApiClient mGoogleApiClient;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private static final int FB_LOG_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.login_main);

        dbAdapter=new DbAdapter(this);
        dbAdapter=dbAdapter.open();



        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        editTextUserName=(EditText)findViewById(R.id.userlog);
        editTextPassword=(EditText)findViewById(R.id.passlog);

        submit=(Button)findViewById(R.id.btnlogin);
        register=(TextView) findViewById(R.id.txtviewRegister);
        guestuser=(TextView)findViewById(R.id.txtViewGuest);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]


        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());

        findViewById(R.id.sign_in_button).setOnClickListener(this);


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                profileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {


                        if(Profile.getCurrentProfile()!=null) {

                            //String uname = currentProfile.getFirstName();
                            //String uId = currentProfile.getId();


                            //Glide.with(LoginActivity.this).load("https://graph.facebook.com/" + currentProfile.getId() + "/picture?type=large").into(thumbnail);

                            SharedPreferences settings = getSharedPreferences("MyFB_PREFS", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("fb_id", currentProfile.getId());
                            editor.putString("fullname", currentProfile.getFirstName() + " " + currentProfile.getLastName());
                            editor.putString("firstname", currentProfile.getFirstName());
                            editor.putString("lastname", currentProfile.getLastName());
                            editor.putString("image_loc", "https://graph.facebook.com/" + currentProfile.getId() + "/picture?type=large");

                            editor.commit();

                            Intent hmIntent=new Intent(LoginActivity.this,MainActivity.class);
                            Toast.makeText(LoginActivity.this, "Congrats: Login Successfull", Toast.LENGTH_LONG).show();
                            LoginActivity.this.finish();
                            startActivity(hmIntent);

                        }
                        else{

                            LoginManager.getInstance().logOut();


                        }

                    }
                };

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });





        editTextPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass=editTextPassword.getText().toString();
                if(pass.length()<8) {
                    editTextPassword.setError("Password must 8 charaters");
                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName=editTextUserName.getText().toString();
                String password=editTextPassword.getText().toString();

                String storedPassword=dbAdapter.getsingleUser(userName);

                // check if the Stored password matches with  Password entered by user
                if(password.equals(storedPassword))
                {
                    Intent hmIntent=new Intent(LoginActivity.this,MainActivity.class);
                    Toast.makeText(LoginActivity.this, "Congrats: Login Successfull", Toast.LENGTH_LONG).show();
                    LoginActivity.this.finish();
                    startActivity(hmIntent);
                }
                else
                {

                    editTextUserName.setError("User name is incorrect");
                    editTextPassword.setError("Password is incorrect.");


                    // Toast.makeText(MainActivity.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
                }

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent=new Intent(LoginActivity.this,Register.class);
                // Toast.makeText(MainActivity.this, "Congrats: Login Successfull", Toast.LENGTH_LONG).show();
                LoginActivity.this.finish();
                startActivity(regIntent);
            }
        });

        guestuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homIntent=new Intent(LoginActivity.this,MainActivity.class);

                LoginActivity.this.finish();
                startActivity(homIntent);
            }
        });



    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            //showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    //hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String name=acct.getDisplayName();
            SharedPreferences settings = getSharedPreferences("MyFB_PREFS", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("fb_id", acct.getId());
            editor.putString("fullname", acct.getDisplayName());
            editor.putString("email", acct.getEmail());
            //editor.putString("lastname", currentProfile.getLastName());
            editor.putString("image_loc", String.valueOf(acct.getPhotoUrl()));

            editor.commit();

            Intent hmIntent=new Intent(LoginActivity.this,MainActivity.class);
            Toast.makeText(LoginActivity.this, "Congrats: Login Successfull " +name, Toast.LENGTH_LONG).show();
            LoginActivity.this.finish();
            startActivity(hmIntent);


            Toast.makeText(this,"Welcome "+name,Toast.LENGTH_LONG).show();
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this,"Signed Out",Toast.LENGTH_LONG).show();
            //mStatusTextView.setText(R.string.signed_out);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(profileTracker!=null)
        profileTracker.stopTracking();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            // ...
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.

        Log.d(TAG, "onConnectionFailed:" + connectionResult);

    }
}
