package com.example.filipe.qualquercoisadeextraordinario;

import android.app.AlertDialog;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.design.*;
import android.support.design.BuildConfig;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPush;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushException;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushNotificationListener;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.BMSClient;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPush;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushNotificationListener;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushResponseListener;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPSimplePushNotification;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;



public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private MFPPush push;
    private MFPPushNotificationListener notificationListener;


    TextView TemperatureTextView;
    TextView ChlorineTextView;
    TextView pHTextView;
    TextView PressureTextView;
    TextView WaterLevelTextView;
    TextView ChlorineLevelTextView;
    TextView AcidLevelTextView;
    Button Request_Button;
    Button TemperatureControlDownButton;
    Button TemperatureControlUpButton;
    Button securityButton;
    Button TemperatureSetButton;

    Switch PumpSwitch;

    String water_level_aux = "-1"; //Notifications
    String chlorine_level_aux = "-1"; //Notifications
    String acid_level_aux = "-1"; //Notifications

    int notificationID=1;

    boolean SecurityPressed = false;
    boolean PumpPressed = false;
    float T;
    Toast Pump_toast;
    Toast Security_toast;
    Toast Temperature_toast;

    String ipAddress = "192.168.0.6"; //RICS_LAB
    //String ipAddress = "192.168.1.103"; //VODAFONE D4F7CF
    //String ipAddress = "192.168.1.209"; //HOTSPOT
    //String ipAddress = "192.168.0.158"; //HOTSPOT2

    String port = "80";


    DatabaseHelper helper = new DatabaseHelper(this);

    String OrganizationID = "rfh2dd";
    String DeviceType = "android";
    String DeviceID = "android";
    String AuthMethod = "use-auth-token";
    String Token = "tese12345";


    int ctrl = 0;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here11

        }

        BMSClient.getInstance().initialize(this, BMSClient.REGION_UK);

        push = MFPPush.getInstance();

        push.initialize(this, "3c79cbac-489e-4b4c-8821-d0ce4a388743", "731d84ba-ffa1-4db0-8c38-1c7b03d9d547");

        // Create notification listener and enable pop up notification when a message is received

        notificationListener = new MFPPushNotificationListener() {
            @Override
            public void onReceive(final MFPSimplePushNotification message) {
                Log.i(TAG, "Received a Push Notification: " + message.toString());
                runOnUiThread(new Runnable() {
                    public void run() {
                        new android.app.AlertDialog.Builder(MainActivity.this)
                                .setTitle("Received a Push Notification")
                                .setMessage(message.getAlert())
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                })
                                .show();
                    }
                });
            }
        };

        TemperatureTextView = (TextView) findViewById(R.id.temperatureView);

        TemperatureControlUpButton = (Button) findViewById(R.id.temperatureUpBtn);
        TemperatureControlUpButton.setOnClickListener(this);

        Request_Button = (Button) findViewById(R.id.request_button);
        Request_Button.setOnClickListener(this);

        TemperatureControlDownButton = (Button) findViewById(R.id.temperatureDownBtn);
        TemperatureControlDownButton.setOnClickListener(this);

        securityButton = (Button) findViewById(R.id.securityButton);
        securityButton.setOnClickListener(this);

        TemperatureSetButton = (Button) findViewById(R.id.set_temperature_button);
        TemperatureSetButton.setOnClickListener(this);

        PumpSwitch = (Switch) findViewById(R.id.pump_switch);
        PumpSwitch.setOnClickListener(this);

        TemperatureTextView.setBackgroundColor(Color.GREEN);
        TemperatureTextView.setTextColor(Color.BLACK);

        pHTextView = (TextView)findViewById(R.id.phView);
        ChlorineTextView = (TextView)findViewById(R.id.chlorineView);
        PressureTextView = (TextView)findViewById(R.id.PressureView);

        WaterLevelTextView = (TextView)findViewById(R.id.WaterLevelView);
        ChlorineLevelTextView = (TextView)findViewById(R.id.ChlorineLevelView);
        AcidLevelTextView = (TextView)findViewById(R.id.AcidLevelView);

        System.out.println("ESTA A DAR ERRO AQUI!");

        registerDevice();

    }



    public void registerDevice() {

        // Checks for null in case registration has failed previously
        if (push == null) {
            push = MFPPush.getInstance();
        }
        Log.i(TAG, "Registering for notifications");

        MFPPushResponseListener registrationResponselistener = new MFPPushResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                // Split response and convert to JSON object to display User ID confirmation from the backend
                String[] resp = response.split("Text: ");
                try {
                    JSONObject responseJSON = new JSONObject(resp[1]);
                    //setStatus("Device Registered Successfully with USER ID " + responseJSON.getString("userId"), true);
                    System.out.println("Device Registered Successfully with USER ID " + responseJSON.getString("userId"));
                } catch (JSONException e) {
                    e.printStackTrace();

                }

                Log.i(TAG, "Successfully registered for push notifications, " + response);
                // Start listening to notification listener now that registration has succeeded
                push.listen(notificationListener);
            }
            @Override
            public void onFailure(MFPPushException exception) {
                String errLog = "Error registering for push notifications: ";
                String errMessage = exception.getErrorMessage();
                int statusCode = exception.getStatusCode();

                // Set error log based on response code and error message
                if(statusCode == 401){
                    errLog += "Cannot authenticate successfully with Bluemix Push instance, ensure your CLIENT SECRET was set correctly.";
                } else if(statusCode == 404 && errMessage.contains("Push GCM Configuration")){
                    errLog += "Push GCM Configuration does not exist, ensure you have configured GCM Push credentials on your Bluemix Push dashboard correctly.";
                } else if(statusCode == 404 && errMessage.contains("PushApplication")){
                    errLog += "Cannot find Bluemix Push instance, ensure your APPLICATION ID was set correctly and your phone can successfully connect to the internet.";
                } else if(statusCode >= 500){
                    errLog += "Bluemix and/or your Push instance seem to be having problems, please try again later.";
                }

                //setStatus(errLog, false);
                Log.e(TAG,errLog);
                // make push null since registration failed
                push = null;
            }
        };

        push.registerDeviceWithUserId("Sample UserID",registrationResponselistener);
        ctrl = 1;
    }



    @Override
    protected void onPause() {
        super.onPause();

        if (push != null) {
            push.hold();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (push != null) {
            push.listen(notificationListener);
        }
    }



        @Override
    public void onClick(View V)
    {
        switch(V.getId()){
            case R.id.temperatureUpBtn:
                T = Float.parseFloat(TemperatureTextView.getText().toString());
                if(T==30)
                {
                    T=30;
                }
                else
                {
                    T = T + 1;
                }
                if(T>= 27)
                {
                    TemperatureTextView.setBackgroundColor(Color.RED);
                    TemperatureTextView.setTextColor(Color.WHITE);
                }
                else
                {
                    if(T<= 23)
                    {
                        TemperatureTextView.setBackgroundColor(Color.BLUE);
                        TemperatureTextView.setTextColor(Color.WHITE);
                    }
                    else
                    {
                        TemperatureTextView.setBackgroundColor(Color.GREEN);
                        TemperatureTextView.setTextColor(Color.BLACK);
                    }

                }
                TemperatureTextView.setText(Float.toString(T));
                break;
            case R.id.temperatureDownBtn:
                T = Float.parseFloat(TemperatureTextView.getText().toString());
                if(T==20)
                {
                    T=20;
                }
                else
                {
                    T = T - 1;
                }
                if(T>= 27)
                {
                    TemperatureTextView.setBackgroundColor(Color.RED);
                    TemperatureTextView.setTextColor(Color.WHITE);
                }
                else
                {
                    if(T<= 23)
                    {
                        TemperatureTextView.setBackgroundColor(Color.BLUE);
                        TemperatureTextView.setTextColor(Color.WHITE);
                    }
                    else
                    {
                        TemperatureTextView.setBackgroundColor(Color.GREEN);
                        TemperatureTextView.setTextColor(Color.BLACK);
                    }
                }

                TemperatureTextView.setText(Float.toString(T));
                break;
            case R.id.set_temperature_button:
                T = Float.parseFloat(TemperatureTextView.getText().toString());
                new HttpRequestAsyncTask(V.getContext(), ipAddress, port, 0, TemperatureTextView.getText().toString()).execute(); // Enviar o valor da temperatura em String para o ESP8266
                Temperature_toast = Toast.makeText(this, "Temperature set to " + T + "ºC", Toast.LENGTH_LONG);
                Temperature_toast.show();
                break;
            case R.id.request_button:
                //if(ipAddress.length()>0 && port.length()>0)
                //{
                new HttpRequestAsyncTask(V.getContext(), ipAddress, port, 1, "").execute();// Receber todos os dados em forma de String para serem tratados

                try {
                    getJSONObjectFromURL("http://"+ipAddress+":"+port+"/");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(this, "Resquest sent", Toast.LENGTH_SHORT).show();
                //}
                break;
            case R.id.securityButton:
                if(SecurityPressed == false)
                {
                    securityButton.setBackgroundColor(Color.GREEN);
                    SecurityPressed = true;
                    new HttpRequestAsyncTask(V.getContext(), ipAddress, port , 2, "SecurityOn").execute(); // ligar a segurança
                    Security_toast = Toast.makeText(this, "Security Activated", Toast.LENGTH_LONG);
                    Security_toast.show();
                }
                else
                {
                    securityButton.setBackgroundColor(Color.GRAY);
                    SecurityPressed = false;
                    new HttpRequestAsyncTask(V.getContext(), ipAddress, port, 2, "SecurityOff").execute(); //desligar a segurança
                    Security_toast = Toast.makeText(this, "Security Deactivated", Toast.LENGTH_LONG);
                    Security_toast.show();
                }
                break;
            case R.id.pump_switch:
                if(PumpPressed == false)
                {
                    new HttpRequestAsyncTask(V.getContext(), ipAddress, port, 3, "PumpOff").execute(); // Mandar um TRUE para desligar a bomba
                    Pump_toast = Toast.makeText(this, "Pump turned off! Maintenance suspended!", Toast.LENGTH_LONG);
                    Pump_toast.show();
                    PumpPressed = true;
                }
                else
                {
                    new HttpRequestAsyncTask(V.getContext(), ipAddress, port, 3, "PumpOn").execute(); // Mandar um FALSE para ligar a bomba
                    Pump_toast = Toast.makeText(this, "Pump turned on! Maintenance activated!", Toast.LENGTH_LONG);
                    Pump_toast.show();
                    PumpPressed = false;
                }
                break;
            default: break;
        }
    }




    // ATTENTION: This was auto-generated to implement the App Indexing API.
    // See https://g.co/AppIndexing/AndroidStudio for more information.
    //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
/*
    @Override
    public void backgroundTask() throws IOException, JSONException {
        while(true)
        {

            // Variables to control the notifications!!

            int pressure_aux = 0;
            int security_aux = 0;
            JSONObject jsonObject = getJSONObjectFromURL("https://1be7afeb-3e6f-4792-9060-103e3a67fb39-bluemix.cloudant.com/tese/_all_docs");
            ArrayList<String> arrayList = JSONParser(jsonObject);
            GetLastObject
        }
    }

    @Override
    public void GetLastObject(ArrayList<String> arrayListId) throws IOException, JSONException {

        String pH = "";
        String ORP = "";
        String Temperature = "";
        String Pressure = "";
        String water_level = "";
        String chlorine_level = "";
        String acid_level = "";
        String sec = "";

        int lastId =arrayListId.size()-1;
        JSONObject jsonObject = getJSONObjectFromURL("https://1be7afeb-3e6f-4792-9060-103e3a67fb39-bluemix.cloudant.com/tese/"+ arrayListId.get(lastId)).getJSONObject("d");
        //pH = jsonObject.getString("pH");
        //ORP = jsonObject.getString("ORP");
        //Temperature = jsonObject.getString("temp");
        Pressure = jsonObject.getString("pressure");
        water_level = jsonObject.getString("w_l");
        chlorine_level = jsonObject.getString("c_l");
        acid_level = jsonObject.getString("a_l");
        sec = jsonObject.getString("sec");


        //
        if(sec == "1")
        {
            createPushNotification("Security","POSSIBLE DROWNING!!");
        }

        float pressureValue =Float.parseFloat(Pressure);
        if (pressureValue>1.5)
        {
            createPushNotification("Pressure","High Pressure! Clean the sand filter");
        }
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id)
        {
            case R.id.check_data: // Fazer um request para a cloud e mandar os dados para um ecrã à parte

                try {
                    JSONObject jsonObject = getJSONObjectFromURL("https://1be7afeb-3e6f-4792-9060-103e3a67fb39-bluemix.cloudant.com/tese/_all_docs");
                    ArrayList<String> arrayList = JSONParser(jsonObject);
                    ArrayList<String> ResultToShow = GetList(arrayList);
                    GoToShowResults(ResultToShow);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }




                break;

            case R.id.log_out: // Como pôr um utilizador offline???????
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;

            case R.id.delete_account:

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("WARNING!");
                builder.setMessage("Are you sure you want to delete your account? You won't be able to access you swimming pool state!");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle bundle = getIntent().getExtras();
                        String Username2Str = bundle.getString("username");
                        System.out.println(Username2Str);
                        helper.deleteUser(Username2Str);
                        Toast.makeText(getApplicationContext() , "User successfully deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));

                        finish();
                    }
                });
                builder.setNegativeButton("CANCEL", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
        }
        /*
        if (id == R.id.action_settings) {
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }


    public static ArrayList<String> JSONParser(JSONObject JSON) throws JSONException {

        ArrayList<String> arraylist = new ArrayList<String>(); //Lista de ids

        // //Lista do parametro
        try {
            JSONArray json_array = JSON.getJSONArray("rows"); //json_array = Coluna "rows"
            for (int i = 0; i < json_array.length(); i++) {
                arraylist.add(i, json_array.getJSONObject(i).getString("id"));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        System.out.println("JSONParser");
        System.out.println(arraylist);
        return arraylist;
    }

    public ArrayList<String> GetList(ArrayList<String> arrayList_id) throws JSONException, IOException {

        ArrayList<String> arrayList = new ArrayList<String>();

        String pH = "";
        String ORP = "";
        String Temperature = "";
        String Pressure = "";

        String water_level_aux = "";
        String chlorine_level_aux = "";
        String acid_level_aux = "";

        String water_level = "";
        String chlorine_level = "";
        String acid_level = "";

        String sec = "";
        String sec_aux = "";

        for (int i = 0; i < arrayList_id.size(); i++) {
            JSONObject jsonObject = getJSONObjectFromURL("https://1be7afeb-3e6f-4792-9060-103e3a67fb39-bluemix.cloudant.com/tese/" + arrayList_id.get(i)).getJSONObject("d");
            ;//buscar cada json
            //JSONObject jsonObject1 = jsonObject.getJSONObject("d");

            pH = jsonObject.getString("pH");
            ORP = jsonObject.getString("ORP");
            Temperature = jsonObject.getString("temp");
            Pressure = jsonObject.getString("pressure");

            water_level_aux = jsonObject.getString("w_l");
            switch(water_level_aux)
            {
                case "0": water_level = "Empty";
                    break;
                case "1": water_level = "Almost Empty";
                    break;
                case "2": water_level = "Almost Full";
                    break;
                case "3": water_level = "Full";
                    break;
            }

            chlorine_level_aux = jsonObject.getString("c_l");

            switch(chlorine_level_aux)
            {
                case "0": chlorine_level = "Empty";
                    break;
                case "1": chlorine_level = "Almost Empty";
                    break;
                case "2": chlorine_level = "Normal";
                    break;
            }
            acid_level_aux = jsonObject.getString("a_l");
            switch(acid_level_aux)
            {
                case "0": acid_level = "Empty";
                    break;
                case "1": acid_level = "Almost Empty";
                    break;
                case "2": acid_level = "Normal";
                    break;
            }
            System.out.println("String que estou a enviar para a proxima actividade -> pH :" + pH +"; ORP: "+ ORP +"; Temperature: "+ Temperature +"; Pressure :"+ Pressure + " Water Level :"+ water_level +"; Chlorine Level :"+ chlorine_level +"; Acid Level :"+ acid_level);
            arrayList.add(i, "pH: "+ pH +";\nORP: "+ ORP +" mV;\nTemperature: "+ Temperature +" ºC;\nPressure :"+ Pressure + " bar" +";\nWater Level :"+ water_level +";\nChlorine Level :"+ chlorine_level +";\nAcid Level :"+ acid_level);
        }

        System.out.println(arrayList);

        return arrayList;
    }


    public void GoToShowResults(ArrayList<String> arrayList) {
        Intent intent = new Intent(this, ShowDataActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("result", arrayList);

        intent.putExtras(bundle);

        System.out.println(bundle);

        startActivity(intent);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    /*
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    public void postData(String ipAddress, String portNumber, String id, String Data, String Type)
    {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://"+ipAddress+":"+portNumber+"/");

        try
        {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("id", id));
            nameValuePairs.add(new BasicNameValuePair(Data,Type));

            HttpResponse response = httpclient.execute(httppost);
        }
        catch (ClientProtocolException e)
        {

        }
        catch (IOException e)
        {

        }
    }
*/
    public static JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException
    {

        HttpURLConnection urlConnection = null;

        URL url = new URL(urlString);

        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);

        urlConnection.setDoOutput(true);

        urlConnection.connect();

        BufferedReader br=new BufferedReader(new InputStreamReader(url.openStream()));

        char[] buffer = new char[1024];

        String jsonString = new String();

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();

        jsonString = sb.toString();

        System.out.println("JSON: " + jsonString);

        return new JSONObject(jsonString);
    }


    /**
     * Description: Send an HTTP Post request to a specified ip address and port.
     * Also send a parameter "parameterName" with the value of "parameterValue".
     * @param ipAddress the ip address to send the request to
     * @param portNumber the port number of the ip address

     * @return The ip address' reply text, or an ERROR message is it fails to receive one
     */
    public String sendRequest( String ipAddress, String portNumber, String Data, int ID) {
        String serverResponse = "ERROR";



        if(ID == 1)
        {
            try {
                System.out.println("ID: "+ID);
                HttpClient httpclient = new DefaultHttpClient(); // create an HTTP client
                // define the URL e.g. http://myIpaddress:myport/?pin=13 (to toggle pin 13 for example)
                URI uri = null;
                uri = new URI("http://"+ipAddress+":"+portNumber+"/" + ID);
                System.out.println(uri);
                //GET DATA FROM ESP8266
                HttpGet getRequest = new HttpGet(); // create an HTTP GET object
                getRequest.setURI(uri);// set the URL of the GET request

                System.out.println(getRequest.toString());

                HttpResponse response = httpclient.execute(getRequest);

                InputStream content = null;
                content = response.getEntity().getContent();
                BufferedReader in = new BufferedReader(new InputStreamReader(content));
                serverResponse = in.readLine();
                System.err.println(serverResponse);
                // Close the connection
                content.close();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {

            try {
                HttpClient httpclient = new DefaultHttpClient(); // create an HTTP client
                // define the URL e.g. http://myIpaddress:myport/?pin=13 (to toggle pin 13 for example)
                URI uri = null;
                uri = new URI("http://"+ipAddress+":"+portNumber+"/"+ID);

                System.out.println(uri);
                //SEND DATA TO ESP8266
                HttpPost postRequest = new HttpPost(); // create an HTTP GET object
                postRequest.setURI(uri);// set the URL of the GET request

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("id", Integer.toString(ID)));
                nameValuePairs.add(new BasicNameValuePair(Data, "\r"));
                postRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                System.out.println(postRequest.getEntity().toString());

                HttpResponse response = httpclient.execute(postRequest);

                InputStream content = null;
                content = response.getEntity().getContent();
                BufferedReader in = new BufferedReader(new InputStreamReader(content));
                serverResponse = in.readLine();
                //System.err.println(serverResponse);
                // Close the connection
                content.close();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // execute the request
        // get the ip address server's reply

        // return the server's reply/response text
        return serverResponse;
    }


    private void createPushNotification(String title, String content)
    {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setSmallIcon(R.drawable.ic_stat_announcement);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(content);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(notificationID, mBuilder.build());

        notificationID++;
    }



    /**
     * An AsyncTask is needed to execute HTTP requests in the background so that they do not
     * block the user interface.
     */
    private class HttpRequestAsyncTask extends AsyncTask<Void, Void, Void> {

        // declare variables needed
        private String requestReply,ipAddress, portNumber;
        private Context context;
        private AlertDialog alertDialog;
        private String dataToSend;
        private int Type;



        /**
         * Description: The asyncTask class constructor. Assigns the values used in its other methods.
         * @param context the application context, needed to create the dialog
         * @param ipAddress the ip address to send the request to
         * @param portNumber the port number of the ip address
         */

        //Data: Toda a informação a ser enviada para o ESP8266
        //Type: TRUE -> Enviar dados (Temperatura ou Ordem de segurança ou Ordem para ligar/desligar a bomba);
        //      FALSE -> Receber dados (Request data)
        public HttpRequestAsyncTask(Context context, String ipAddress, String portNumber, int Type, String dataToSend)
        {
            this.context = context;
            alertDialog = new AlertDialog.Builder(this.context)
                    .setTitle("HTTP Response From IP Address:")
                    .setCancelable(true)
                    .create();

            this.ipAddress = ipAddress;
            this.portNumber = portNumber;
            this.dataToSend = dataToSend;
            this.Type = Type;
        }

        /**
         * Name: doInBackground
         * Description: Sends the request to the ip address
         * @param voids
         * @return
         */
        @Override
        protected Void doInBackground(Void... voids) {
            alertDialog.setMessage("Data sent, waiting for reply from server...");
            if(!alertDialog.isShowing())
            {
                alertDialog.show();
            }
            System.out.println(dataToSend);
            System.out.println(Integer.toString(Type));

            requestReply = sendRequest(ipAddress,portNumber,dataToSend,Type);
            return null;
        }

        /**
         * Name: onPostExecute
         * Description: This function is executed after the HTTP request returns from the ip address.
         * The function sets the dialog's message with the reply text from the server and display the dialog
         * if it's not displayed already (in case it was closed by accident);
         * @param aVoid void parameter
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            alertDialog.setMessage(requestReply);
            if(!alertDialog.isShowing())
            {
                alertDialog.show();
            }
            System.out.println("CONA PARA TODOS");
            System.out.println(requestReply);
            if(Type==1) {

                StringTokenizer st = new StringTokenizer(requestReply,",");
                while(st.hasMoreTokens())
                {
                    StringTokenizer st2 = new StringTokenizer(st.nextToken(),": ");

                    String parameter = st2.nextToken(); //nome da variavel correspondente ao valor
                    System.out.println(parameter);
                    String value = st2.nextToken(); //valor em si
                    System.out.println(value);
                    if(parameter.equals("Temperature"))
                    {

                        float value2 = Float.parseFloat(value);
                        int value3 = Math.round(value2);
                        String value4 = Integer.toString(value3);
                        TemperatureTextView.setText(value4);
                    }
                    if(parameter.equals("pH"))
                    {
                        pHTextView.setText(value);
                    }
                    if(parameter.equals("ORP"))
                    {
                        ChlorineTextView.setText(value);
                    }
                    if(parameter.equals("Pressure"))
                    {
                        float value2 = Float.parseFloat((value));
                        if(value2 > 1.5)
                        {
                            createPushNotification("Pressure","High");
                        }

                        PressureTextView.setText(value);
                    }
                    if(parameter.equals("Water_level"))
                    {
                        if(value.equals("0"))
                        {
                            WaterLevelTextView.setText("Empty");
                            createPushNotification("Water level","Empty");
                        }
                        if(value.equals("1"))
                        {
                            createPushNotification("Water level","Almost Empty");
                            WaterLevelTextView.setText("Almost Empty");
                        }
                        if(value.equals("2"))
                        {
                            createPushNotification("Water level","Normal");
                            WaterLevelTextView.setText("Normal");
                        }
                        if(value.equals("3"))
                        {
                            createPushNotification("Water level","Full");
                            WaterLevelTextView.setText("Full");
                        }
                    }
                    if(parameter.equals("Chlorine_level"))
                    {
                        if(value.equals("0"))
                        {
                            createPushNotification("Chlorine","Refill chlorine");
                            ChlorineLevelTextView.setText("Empty");
                        }
                        if(value.equals("1"))
                        {
                            createPushNotification("Chlorine","Refill chlorine");
                            ChlorineLevelTextView.setText("Almost Empty");
                        }
                        if(value.equals("2"))
                        {
                            createPushNotification("Chlorine","Normal");
                            ChlorineLevelTextView.setText("Normal");
                        }

                    }
                    if(parameter.equals("Acid_level"))
                    {
                        if(value.equals("0"))
                        {
                            createPushNotification("Acid","Refill acid");
                            AcidLevelTextView.setText("Empty");
                        }
                        if(value.equals("1"))
                        {
                            createPushNotification("Acid","Refill acid");
                            AcidLevelTextView.setText("Almost Empty");
                        }
                        if(value.equals("2"))
                        {
                            createPushNotification("Acid","Normal");
                            AcidLevelTextView.setText("Normal");
                        }
                    }
                }


            }
        }

        /**
         * Name: onPreExecute
         * Description: This function is executed before the HTTP request is sent to ip address.
         * The function will set the dialog's message and display the dialog.
         */
        @Override
        protected void onPreExecute() {
            alertDialog.setMessage("Sending data to server, please wait...");
            if(!alertDialog.isShowing())
            {
                alertDialog.show();
            }
        }

    }

}



