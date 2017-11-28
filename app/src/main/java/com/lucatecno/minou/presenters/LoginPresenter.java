package com.lucatecno.minou.presenters;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.lucatecno.minou.views.ILoginView;


/**
 * Created by saksham on 20/6/17.
 */

public class LoginPresenter {

    ILoginView mILoginView;

    public LoginPresenter(ILoginView iLoginCallbacks){
        this.mILoginView =iLoginCallbacks;
    }

    public void  createAccount(String email, String password, FirebaseAuth firebaseAuth){
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        mILoginView.showToast("Create Account Success");
                        mILoginView.authSuccessful();
                    }else {
                        mILoginView.showToast("Create Account Unsuccess");
                    }
                }
        });
    }

    public void  signIn(String email, String password, FirebaseAuth firebaseAuth){
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        mILoginView.showToast("Email Authentication Success");
                        mILoginView.authSuccessful();
                    }else {
                        mILoginView.showToast("Email Authentication Unsuccess");
                    }
                }
        });
    }

    public void firebaseAnonymousAuth() {
        mILoginView.changeButtonText();
        FirebaseAuth.getInstance().signInAnonymously()
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete( Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            mILoginView.showToast("Firebase authentication failed, please check your internet connection");
                        } else {
                            mILoginView.showToast("Anonymous Authentication Success");
                            mILoginView.authSuccessful();
                        }
                    }
                });
    }

    public void signInGoogleFirebase(GoogleSignInResult googleSignInResult, FirebaseAuth firebaseAuth) {
        if (googleSignInResult.isSuccess()){
            AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInResult.getSignInAccount().getIdToken(),null);
            firebaseAuth.signInWithCredential(authCredential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            mILoginView.showToast("Google SignIn Success");
                            mILoginView.authSuccessful();
                        }else {
                            mILoginView.showToast("Google SignIn Unsuccess");
                        }
                    }
            });
        }else {
            mILoginView.showToast("Google SignIn Unsuccess");
        }

    }

    public void invalidateRoomName(String roomName) {

        if (roomName.trim().isEmpty()){
            mILoginView.showToast("Enter a valid Name");
        } else {
            mILoginView.startChatActivity(roomName);
        }
    }

    public void showRoomDialogInActivity(){
        mILoginView.showRoomDialog();
    }
}
