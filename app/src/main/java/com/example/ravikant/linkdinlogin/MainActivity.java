package com.example.ravikant.linkdinlogin;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.linkedin.platform.AccessToken;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String PACKAGE = "com.example.ravikant.linkdinlogin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnLogin).setOnClickListener(this);
        // LISessionManager.getInstance(getApplicationContext()).init(AccessToken, null);

    }

    private void login(){
        LISessionManager.getInstance(getApplicationContext()).init(this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                // Authentication was successful.  You can now do
                // other calls with the SDK.
            }

            @Override
            public void onAuthError(LIAuthError error) {
                // Handle authentication errors
            }
        }, true);
    }

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Add this line to your existing onActivityResult() method
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
        startActivity(new Intent(MainActivity.this, HomeActivity.class));
    }

    public void generateHashkey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    PACKAGE,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                Log.e("HASH KEY==", Base64.encodeToString(md.digest(),
                        Base64.NO_WRAP));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Name not found", e.getMessage(), e);

        } catch (NoSuchAlgorithmException e) {
            Log.d("Error", e.getMessage(), e);
        }
    }

    @Override
    public void onClick(View view) {
        login();
    }
}
