package com.example.filipe.qualquercoisadeextraordinario;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by Filipe on 02/09/2017.
 */

public class FirebaseInstanceIdService extends com.google.firebase.iid.FirebaseInstanceIdService {

    private static final String REG_TOKEN = "REG_TOKEN";

    @Override
    public void onTokenRefresh() {

        System.out.println("Aqui vem a token");
        String recent_token = FirebaseInstanceId.getInstance().getToken();
        Log.d(REG_TOKEN,recent_token);

    }
}
