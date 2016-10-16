package org.fossasia.openevent.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import org.fossasia.openevent.OpenEventApp;
import org.fossasia.openevent.R;
import org.json.JSONObject;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;

/**
 * Created by MananWason on 10-06-2015.
 */
public class SplashActivity extends Activity {
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OpenEventApp.getEventBus().register(this);
        initBranch();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }


    private void initBranch() {
        Branch branch = Branch.getInstance(getApplicationContext());
        branch.initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    Branch.getInstance(getApplicationContext()).userCompletedAction("init finished");
                } else {
                    Branch.getInstance(getApplicationContext()).userCompletedAction("init failed");
                }
            }
        }, this.getIntent().getData(), this);
    }


    @Override
    public void onNewIntent(Intent intent) {
        //This is associated with branch IO Integration
        this.setIntent(intent);
    }

}
