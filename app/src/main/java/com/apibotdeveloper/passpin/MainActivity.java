package com.apibotdeveloper.passpin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.KeyguardManager;
import android.content.Intent;
import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "PassPin-main";
    private static final int INTENT_AUTHENTICATE = 19245;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void enterPasswordAndEnterHeaven(View view) {
        Log.d(TAG, "Process started for entering hell");
        KeyguardManager keyguardManager = (KeyguardManager) this.getSystemService(KEYGUARD_SERVICE);
        if (keyguardManager.isDeviceSecure() && keyguardManager.isKeyguardSecure()) {
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
                });
            }

        } else {
            Log.d(TAG, "Pathway not secure, please apply a setting with GOD (james)");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "Verifying your prayers - only non-barter prayers will be heard - its' not a wish granting system " + requestCode + " " + resultCode);

        if (requestCode == INTENT_AUTHENTICATE) {
            Log.d(TAG, "Congratulation human! you are officially a member of the Hell organization");
            startActivity(new Intent(MainActivity.this, HellActivity.class));
        } else {
            Log.d(TAG, "Buzz off poser");
        }
    }
}