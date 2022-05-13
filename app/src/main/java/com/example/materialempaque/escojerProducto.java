package com.example.materialempaque;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class escojerProducto extends AppCompatActivity {
    JSONObject jsonObject;
    EditText buscador;
    ListView listaproductos;
    String urlproducto;
    RequestQueue n_requeriminto;
    ArrayList<String> productosLista,productosId;

    ArrayAdapter<String> adapter;
    SharedPreferences preferences;
    SharedPreferences.Editor editorPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escojer_producto);

        buscador = (EditText) findViewById(R.id.buscador);
        listaproductos = (ListView) findViewById(R.id.listaProductos);
        urlproducto = getString(R.string.api_productos);
        productosLista = new ArrayList<String>();
        productosId = new ArrayList<String>();

        buscar_productos();
        adapter = new ArrayAdapter<String>(escojerProducto.this, android.R.layout.simple_list_item_1,productosLista);

        buscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        preferences = getSharedPreferences("datosapp",MODE_PRIVATE);
        editorPreferences = preferences.edit();
        adapter.notifyDataSetChanged();
        listaproductos.setAdapter(adapter);

        listaproductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                editorPreferences.putString("producto",productosLista.get(i));
                editorPreferences.putString("Idproducto",productosId.get(i));
                editorPreferences.commit();
                finish();
            }
        });
    }




    public void buscar_productos()
    {
        Log.d("escojerproducto","ingreso a buscar");
        JsonObjectRequest requerimiento = new JsonObjectRequest(Request.Method.GET, urlproducto, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    //ArrayList array = new ArrayList<String>();
                    Log.d("escojerproductodearray",""+jsonArray.length());
                    for (int i =0;i<jsonArray.length();i++)
                    {
                        jsonObject= new JSONObject(jsonArray.get(i).toString());

                        productosLista.add(jsonObject.getString("nombre"));
                        productosId.add(jsonObject.getString("id"));
                        Log.d("escojerproductodearray",productosLista.get(i)+productosId.get(i));
                    }
                }catch (JSONException e)
                {
                    Toast.makeText(escojerProducto.this,"Error con la base consulte a sistemas",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(escojerProducto.this,"Error con las conexiones",Toast.LENGTH_LONG).show();
            }
        });
        n_requeriminto = Volley.newRequestQueue(this);
        n_requeriminto.add(requerimiento);

    }
}