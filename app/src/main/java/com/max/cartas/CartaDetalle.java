package com.max.cartas;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class CartaDetalle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carta_detalle);

        String CartaId = getIntent().getStringExtra("cartaId");

        if(CartaId.length()>0) {
            new Async().execute("https://api.magicthegathering.io/v1/cards/" + CartaId);
        } else {
            Toast.makeText(this,"No se recibió el id de la carta", Toast.LENGTH_LONG).show();
        }

    }

    public class Async extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(CartaDetalle.this,"Cargando...","Descargando...", true);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            InputStream inputStream = null;
            String result = " ";
            try {
                inputStream = new URL(urls[0]).openStream();
                if (inputStream != null){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = " ";
                    while ((line = bufferedReader.readLine()) !=null);
                    result += line;
                    inputStream.close();
                }else {

                }
            } catch (Exception e) {
                Log.d("InputStream",e.getLocalizedMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String texto) {
            dialog.cancel();
            try{
                JSONObject json= new JSONObject(texto);



            } catch (Exception e) {
                Toast.makeText(CartaDetalle.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
}
