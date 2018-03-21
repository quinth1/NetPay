package com.example.quinten.netpay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.quinten.requests.WachtwoordVRequest;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.quinten.netpay.MainActivity.USER_INFO;

public class WachtwoordVeranderen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wachtwoord_veranderen);
        setTitle("Wachtwoord veranderen");

        //Vars
        final EditText txtOudWachtwoord = (EditText) findViewById(R.id.txtOudWachtwoord);
        final EditText txtNieuwWachtwoord = (EditText) findViewById(R.id.txtNieuwWachtwoord);
        final EditText txtNieuwWachtwoord2 = (EditText) findViewById(R.id.txtNieuwWachtwoord2);
        final Button btnWachtwoordV = (Button) findViewById(R.id.btnWachtwoordV);

        //Gegevens ophalen
        SharedPreferences settings = getSharedPreferences(USER_INFO, 0);
        final String strGebruikersnaamResp = settings.getString("gebruikersnaam", "");


        btnWachtwoordV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Vars
                String strOudWachtwoord = txtOudWachtwoord.getText().toString();
                String strNieuwWachtwoord = txtNieuwWachtwoord.getText().toString();
                String strNieuwWachtwoord2 = txtNieuwWachtwoord2.getText().toString();

                if(txtOudWachtwoord.getText().toString().equals("") || txtNieuwWachtwoord.getText().toString().equals("")
                    || txtNieuwWachtwoord2.toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Vul alle velden in!", Toast.LENGTH_LONG).show();
                }
                else if (!(strNieuwWachtwoord.equals(strNieuwWachtwoord2))) {
                    Toast.makeText(getApplicationContext(), "Wachtwoorden komen niet overeen!", Toast.LENGTH_LONG).show();

                }else{


                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonReponse = new JSONObject(response);
                                boolean success = jsonReponse.getBoolean("success");

                                if (success) {
                                    Toast.makeText(getApplicationContext(), "Wachtwoord is aangepast!", Toast.LENGTH_LONG).show();
                                    Intent i = getBaseContext().getPackageManager()
                                            .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);


                                }else if(jsonReponse.getString("error").equals("error-06")) {
                                    Toast.makeText(getApplicationContext(), "Oud en nieuw wachtwoord zijn hetzelfde!", Toast.LENGTH_LONG).show();

                                }else if(jsonReponse.getString("error").equals("error-07")) {
                                    Toast.makeText(getApplicationContext(), "Wachtwoord incorrect!", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Oeps! Er is iets fout gelopen! We werken eraan!", Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "ERROR!" + " " +  e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                        }
                    };

                    WachtwoordVRequest wachtwoordVRequest = new WachtwoordVRequest(strOudWachtwoord, strNieuwWachtwoord2, strGebruikersnaamResp, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(WachtwoordVeranderen.this);
                    queue.add(wachtwoordVRequest);


                }






            }
        });


    }
}
