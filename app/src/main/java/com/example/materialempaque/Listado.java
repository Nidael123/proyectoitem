package com.example.materialempaque;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.materialempaque.adaptadoresrecycler.adaptadorListado;
import com.example.materialempaque.clases.listadoobjetos;
import com.example.materialempaque.clases.productosGuardar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Listado extends AppCompatActivity {

    private Button anadir;
    private SharedPreferences preferences;
    private TextView bienvenida;

    ArrayList<listadoobjetos> data ,apoyo;
    RecyclerView recicler;

    RequestQueue n_requeriminto;
    JSONObject jsonObject;
    String urlpedidos,fechadia;
    adaptadorListado adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);
        preferences = getSharedPreferences("datosapp",MODE_PRIVATE);
        bienvenida = (TextView) findViewById(R.id.bienvenida);
        anadir = findViewById(R.id.anadir);
        recicler = (RecyclerView) findViewById(R.id.reciclerestadopeticion);
        data = new ArrayList<listadoobjetos>();
        apoyo =new ArrayList<listadoobjetos>();
        recicler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        urlpedidos = getString(R.string.api_pedidos);

        bienvenida.setText(preferences.getString("usuario","estamal"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());//seteo la fecha actual
        Date date = new Date();
        fechadia = dateFormat.format(date);
        anadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Listado.this,agregarproductos.class));
                finish();
            }
        });
        buscarpedidos();
    }

    public void buscarpedidos()
    {
        JsonObjectRequest requerimintos = new JsonObjectRequest(Request.Method.GET, urlpedidos, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("data");
                    for (int i=0; i<=array.length()-1;i++)
                    {
                        listadoobjetos help = new listadoobjetos();
                        jsonObject = new JSONObject(array.getJSONObject(i).toString());
                        help.setFecha(jsonObject.getString("fecha"));
                        help.setSolicitante(jsonObject.getString("solicitante"));
                        help.setAreasolicitante(jsonObject.getString("areasolicitante"));
                        apoyo.add(i,help);
                        //apoyo.add(help);
                        Log.d("buscarpedidos"+i ,apoyo.get(i).getFecha());
                        //data = apoyo;
                    }
                    adapter = new adaptadorListado(apoyo);
                    recicler.setAdapter(adapter);
                }catch (JSONException error)
                {
                    Toast.makeText(Listado.this,"No hay pedidos",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Listado.this,"No hay datos para este dia, añada un pedido",Toast.LENGTH_LONG).show();
            }
        });

        n_requeriminto = Volley.newRequestQueue(this);
        n_requeriminto.add(requerimintos);
    }
}