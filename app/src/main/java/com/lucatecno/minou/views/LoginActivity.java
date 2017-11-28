package com.lucatecno.minou.views;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lucatecno.minou.R;
import com.lucatecno.minou.WelcomeActivity;
import com.lucatecno.minou.presenters.LoginPresenter;
import com.lucatecno.minou.utils.MyUtils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ILoginView, GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "LoginActivity";
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleApiClient googleApiClient;

    private static final int SIGN_IN_GOOGLE_CODE = 1;

    private Button btnCreateAccount;
    private Button btnSignIn;

    private EditText edtEmail;
    private EditText edtPassword;


    private Button btnAnonymousAuth;



    private SignInButton btnSignInGoogle;
    private Button mCreateRoom;
    private Button mEnterExistingRoom;
    private TextView mInfo;
    private LoginPresenter mLoginPresenter;
    private Dialog mChatRoomDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnCreateAccount    = (Button) findViewById(R.id.btnCreateAccount);
        btnSignIn           = (Button) findViewById(R.id.btnSignIn);
        edtEmail            = (EditText) findViewById(R.id.edtEmail);
        edtPassword         = (EditText) findViewById(R.id.edtPassword);

        btnAnonymousAuth    = (Button) findViewById(R.id.btnAnonymousAuth);
        btnSignInGoogle     = (SignInButton) findViewById(R.id.btnSignInGoogle);
        mCreateRoom         = (Button) findViewById(R.id.button_create_room);
        mEnterExistingRoom  = (Button) findViewById(R.id.button_existing_room);
        mInfo               = (TextView) findViewById(R.id.text_info);
        initialize();

        btnCreateAccount.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        btnAnonymousAuth.setOnClickListener(this);
        btnSignInGoogle.setOnClickListener(this);
        mCreateRoom.setOnClickListener(this);
        mEnterExistingRoom.setOnClickListener(this);

        mLoginPresenter=new LoginPresenter(this);
    }

    public void initialize() {
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Log.w(TAG, "onAuthStateChanged - signed_in" + firebaseUser.getUid());
                    Log.w(TAG, "onAuthStateChanged - signed_in" + firebaseUser.getEmail());
                } else {
                    Log.w(TAG, "onAuthStateChanged - signed_out");
                }
            }
        };

        //Inicialización de Google Account
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnCreateAccount:
                mLoginPresenter.createAccount(edtEmail.getText().toString(), edtPassword.getText().toString(), firebaseAuth);
                break;
            case R.id.btnSignIn:
                mLoginPresenter.signIn(edtEmail.getText().toString(), edtPassword.getText().toString(), firebaseAuth);
                break;
            case R.id.btnAnonymousAuth:
                mLoginPresenter.firebaseAnonymousAuth();
                break;
            case R.id.btnSignInGoogle:
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SIGN_IN_GOOGLE_CODE);
                //mLoginPresenter.firebaseGoogleAuth();
                break;
            case R.id.button_create_room:
                mLoginPresenter.showRoomDialogInActivity();
                break;
            case R.id.button_existing_room:
                mLoginPresenter.showRoomDialogInActivity();
                break;
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void authSuccessful() {
        /*btnAnonymousAuth.setVisibility(View.GONE);
        mCreateRoom.setVisibility(View.VISIBLE);
        mEnterExistingRoom.setVisibility(View.VISIBLE);
        mInfo.setVisibility(View.VISIBLE);
        Intent i = new Intent(this, LocationActivity.class);*/
        Intent i = new Intent(this, WelcomeActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void showRoomDialog() {
        mChatRoomDialog=new Dialog(this);
        mChatRoomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_room,null);
        Button submitRoomName= (Button) view.findViewById(R.id.button_room_submit);
        final EditText editTextRoomName=(EditText) view.findViewById(R.id.edittext_room_name);
        submitRoomName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginPresenter.invalidateRoomName(editTextRoomName.getText().toString());
            }
        });

        mChatRoomDialog.setContentView(view);
        mChatRoomDialog.show();

    }

    @Override
    public void changeButtonText() {
        btnAnonymousAuth.setText(getString(R.string.text_waiting_for_auth));
    }

    @Override
    public void startChatActivity(String roomName) {
        mChatRoomDialog.dismiss();
        mChatRoomDialog=null;

        Intent intent=new Intent(this,ChatActivity.class);
        intent.putExtra(MyUtils.EXTRA_ROOM_NAME,roomName);
        startActivity(intent);
    }

    @Override
    public void onStart(){
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_GOOGLE_CODE){
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            mLoginPresenter.signInGoogleFirebase(googleSignInResult, firebaseAuth);
        }/*else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }*/


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
