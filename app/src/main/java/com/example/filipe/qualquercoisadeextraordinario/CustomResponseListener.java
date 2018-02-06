package com.example.filipe.qualquercoisadeextraordinario;

import android.util.Log;

import com.ibm.mobilefirstplatform.clientsdk.android.core.api.Response;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.ResponseListener;

import org.json.JSONObject;

/**
 * Created by Filipe on 05/09/2017.
 */

public class CustomResponseListener implements com.ibm.mobilefirstplatform.clientsdk.android.core.api.ResponseListener{
    @Override
    public void onSuccess(Response response) {
        if(response != null)
        {
            Log.i("CleanSecurePool","Response status: "+ response.getStatus());
            Log.i("CleanSecurePool","Response headers: "+ response.getHeaders());
            Log.i("CleanSecurePool","Response body: "+ response.getResponseText());
        }
    }

    @Override
    public void onFailure(Response response, Throwable t, JSONObject extendedInfo) {
        if(response != null)
        {
            Log.i("CleanSecurePool","Response status: "+ response.getStatus());
            Log.i("CleanSecurePool","Response body: "+ response.getResponseText());
        }
        if (t != null && t.getMessage() != null)
        {
            Log.i("CleanSecurePool","Error: "+ t.getMessage());
        }
    }
}
