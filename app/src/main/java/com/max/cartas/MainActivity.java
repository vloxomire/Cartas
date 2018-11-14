package com.max.cartas;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends ListActivity {
    private ArrayList<Cartas> cartasArrayList;
    private ArrayAdapter<Cartas> cartasArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //new Async().execute("https://api.magicthegathering.io/v1/cards/7e70b405a3d9393de09084182ccf6d4dbb06610f");

        new Async().execute("http://api.openweathermap.org/data/2.5/weather?q=London&appid=aa43128c1614074c31228079baa6869a");

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        Cartas selecionada = cartasArrayList.get(position);

        Intent i = new Intent(this, CartaDetalle.class);

        i.putExtra("cartaId", selecionada.getCartaId());

        startActivity(i);

        super.onListItemClick(l,v,position,id);
    }

    public class Async extends AsyncTask<String, Void, String>{
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(MainActivity.this,"Cargando...","Descargando...", true);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            InputStream inputStream = null;
            String result = "";
            try {
                inputStream = new URL(urls[0]).openStream();
                if (inputStream != null){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
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
        protected void onPostExecute(String t) {
            dialog.cancel();
            try{
                JSONObject json= new JSONObject(t);
                JSONArray cardsJson = json.getJSONArray("cards");

                cartasArrayList = new ArrayList<Cartas>();

                for(int i=0;i<=cardsJson.length();i++) {

                    JSONObject jsonCard = cardsJson.getJSONObject(i);

                    Cartas cartas = new Cartas();

                    cartas.setName(jsonCard.getString("name"));
                    cartas.setCartaId(jsonCard.getString("id"));

                    cartasArrayList.add(cartas);

                    cartas = null;

                }

                cartasArrayAdapter = new ArrayAdapter<Cartas>(MainActivity.this,android.R.layout.simple_list_item_1, cartasArrayList);
                setListAdapter(cartasArrayAdapter);
            }

                catch (Exception e) {
                Toast.makeText(MainActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
}
