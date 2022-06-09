package com.apibotdeveloper.passpin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import static android.hardware.biometrics.BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE;
import static android.hardware.biometrics.BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED;
import static android.hardware.biometrics.BiometricManager.BIOMETRIC_SUCCESS;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "PassPin-main";
    private static final int INTENT_AUTHENTICATE = 19245;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ImageFormat
    }

    public void checkDeviceSecure(View view) {
        String way = view.getTag().toString();
        Log.d(TAG, "Checking device secure " + way);
        Boolean secure = false;
        if (way.equals("km")) {
            KeyguardManager keyguardManager = (KeyguardManager) this.getSystemService(KEYGUARD_SERVICE);
            secure = (keyguardManager.isDeviceSecure() && keyguardManager.isKeyguardSecure());
        } else if (way.equals("bm")) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                BiometricManager biometricManager = (BiometricManager) this.getSystemService(BIOMETRIC_SERVICE);
                secure = (BIOMETRIC_SUCCESS == biometricManager.canAuthenticate());
                Log.d(TAG, "start");
                Log.d(TAG, BIOMETRIC_SERVICE);
                Log.d(TAG, String.valueOf(BIOMETRIC_SUCCESS));
                Log.d(TAG, String.valueOf(BIOMETRIC_ERROR_NONE_ENROLLED));
                Log.d(TAG, String.valueOf(biometricManager.canAuthenticate()));
                Log.d(TAG, String.valueOf(BIOMETRIC_ERROR_HW_UNAVAILABLE));
                Log.d(TAG, String.valueOf(biometricManager.canAuthenticate(BiometricManager.Authenticators.DEVICE_CREDENTIAL)));
                Log.d(TAG, "stop");

            } else {
                Toast.makeText(getApplicationContext(), "android version not supported", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (secure) {
            Toast.makeText(getApplicationContext(), "Device Secure", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Device Not Secure", Toast.LENGTH_SHORT).show();
        }

    }


    public void enterPasswordAndEnterHeaven(View view) {
        Log.d(TAG, "Process started for entering hell");
        KeyguardManager keyguardManager = (KeyguardManager) this.getSystemService(KEYGUARD_SERVICE);
        String checkSecure = view.getTag().toString();
        if (checkSecure.equals("yes")) {
            if (!(keyguardManager.isDeviceSecure() && keyguardManager.isKeyguardSecure())) {
                Log.d(TAG, "Pathway not secure, please apply a setting with GOD (james)");
                Toast.makeText(getApplicationContext(), "Pin not set", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Log.d(TAG, "Trying to access even if it not secure");
        }
            Log.d(TAG, "Pathway is secure, proceeding");

            if (keyguardManager.inKeyguardRestrictedInputMode()) {
                Log.d(TAG, "In restricted mode");
            } else {
                Log.d(TAG, "Keyboard not restricted");
            }

            if (android.os.Build.VERSION.SDK_INT < 29){
                Log.d(TAG, "Brain in less than 29, using keyguard's intent");
                Intent authIntent = keyguardManager.createConfirmDeviceCredentialIntent("Tell me a prayer", "Slipknot is the board of Lords");
                startActivityForResult(authIntent, INTENT_AUTHENTICATE);
            } else{
                BiometricPrompt.Builder promptBuilder = new BiometricPrompt.Builder(this);
                promptBuilder.setTitle("Tell me a prayer");
                promptBuilder.setDescription("Slipknot is the board of the lords");
                if (android.os.Build.VERSION.SDK_INT == 29){
                    Log.d(TAG, "Brain == 29, using biometric prompt intent");
                    promptBuilder.setDeviceCredentialAllowed(true);
                } else if (android.os.Build.VERSION.SDK_INT > 29) {
                    Log.d(TAG, "Brain greater than 29, using biometric prompt");
                    promptBuilder.setAllowedAuthenticators(BiometricManager.Authenticators.DEVICE_CREDENTIAL);
                }

                BiometricPrompt biometricPrompt = promptBuilder.build();
                biometricPrompt.authenticate(new CancellationSignal(), this.getMainExecutor(), new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        Log.d(TAG, "You fingerprint will be the sacrifice");
                        startActivity(new Intent(MainActivity.this, HellActivity.class));
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Log.d(TAG, "Authentication failed");
                        Toast.makeText(getApplicationContext(), "Auth failed", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, FailedActivity.class));
                    }
                });
            }

//        } else {
//            Log.d(TAG, "Pathway not secure, please apply a setting with GOD (james)");
//            Toast.makeText(getApplicationContext(), "Pin not set", Toast.LENGTH_SHORT).show();
//
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "Verifying your prayers - only non-barter prayers will be heard - its' not a wish granting system " + requestCode + " " + resultCode);

        if (requestCode == INTENT_AUTHENTICATE && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "Congratulation human! you are officially a member of the Hell organization");
            startActivity(new Intent(MainActivity.this, HellActivity.class));
        } else {
            Log.d(TAG, "Buzz off poser");
            Log.d(TAG, "Authentication failed");
            Toast.makeText(getApplicationContext(), "Auth failed", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, FailedActivity.class));
        }
    }
}