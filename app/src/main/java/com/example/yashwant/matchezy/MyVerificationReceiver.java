package com.example.yashwant.matchezy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.twilio.verification.TwilioVerification;
import com.twilio.verification.external.VerificationStatus;

public class MyVerificationReceiver extends BroadcastReceiver {

    static VerificationStatus.State state;

    @Override
    public void onReceive(Context context, Intent intent) {
        VerificationStatus verificationStatus = TwilioVerification.getVerificationStatus(intent);

        // NOT_STARTED, STARTED, AWAITING_VERIFICATION, SUCCESS, ERROR
        state = verificationStatus.getState();

        switch (state) {
            case STARTED: Log.e("verification state", "STARTED"); break;
            case AWAITING_VERIFICATION: Log.e("verification state", "AWAITING_VERIFICATION"); break;
            case SUCCESS: Log.e("verification state", "SUCCESS");
            Toast.makeText(context, "Registration Success", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, Login.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            break;
            case ERROR: Log.e("verification state", "ERROR");
            Toast.makeText(context, "Verification Failed, retry!", Toast.LENGTH_SHORT).show();
            break;
        }

    }

}
