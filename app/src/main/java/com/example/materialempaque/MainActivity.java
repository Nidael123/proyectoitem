package com.example.materialempaque;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button empezar;
    private EditText usuario;
    private EditText contrasena;
    String urlusurio;
    RequestQueue n_requeriminto;
    JSONObject jsonObject;

    private SharedPreferences preferences;
    private SharedPreferences.Editor preferenceseditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        empezar = (Button) findViewById(R.id.button);
        usuario = (EditText) findViewById(R.id.usuario);
        contrasena = (EditText) findViewById(R.id.contrasena);
        preferences = getSharedPreferences("datosapp",MODE_PRIVATE);
        preferenceseditor = preferences.edit();
        urlusurio = getString(R.string.api_usurios);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        empezar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(usuario.getText().toString() != "" && contrasena.getText().toString() != "" )
                    buscarusuario(urlusurio + "?usuario=" + usuario.getText().toString()+"&contrasena="+contrasena.getText().toString());
                else
                    Toast.makeText(MainActivity.this,"No se admiten campos en blanco",Toast.LENGTH_LONG);

            }
        });
    }


    public  void buscarusuario(String url)
    {
        JsonObjectRequest requeriminto = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("datos");
                    jsonObject = new JSONObject(jsonArray.get(0).toString());

                    if(jsonObject.getInt("id") == 0)
                    {
                        Toast.makeText(MainActivity.this,"Error en usuario o contraseña",Toast.LENGTH_LONG).show();
                    }
                    else {

                        Toast.makeText(MainActivity.this, "Ingreso con Exito", Toast.LENGTH_LONG);
                    }
                }catch (JSONException e) {
                    Toast.makeText(MainActivity.this,"error de sistema contactar con sistemas",Toast.LENGTH_LONG).show();

                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Usuario o Contraseña incorrecto",Toast.LENGTH_LONG).show();
            }
        });

        n_requeriminto = Volley.newRequestQueue(this);
        n_requeriminto.add(requeriminto);
    }
}