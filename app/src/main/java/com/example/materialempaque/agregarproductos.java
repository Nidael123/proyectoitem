package com.example.materialempaque;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.materialempaque.adaptadoresrecycler.adaptermasproductos;
import com.example.materialempaque.clases.productosGuardar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class agregarproductos extends AppCompatActivity {
    ArrayAdapter adapter;
    String urlproducto, urlareas, urlguardarproduc,fechadia,idproducto;
    JSONObject jsonobject;
    RequestQueue n_requeriminto;

    TextView fecha, solicitante, fechaentrega,areas;
    //Spinner productos;
    ImageButton mas;
    SharedPreferences preferences;
    SharedPreferences.Editor editorPrefernces;
    int estado;
    ArrayList cantidades;
    adaptermasproductos masproductos;
    ArrayList<String> listadoproductos,listadoproductosId;
    ArrayList<productosGuardar> productosaguardar,baseproductos;
    productosGuardar helpproductos;
    ArrayList<productosGuardar> nuevosproductos;
    productosGuardar help;
    EditText cantidadpro,observaciones;
    TextView nuevo_producto;
    ListView listado;
    Button guardar,btnescojer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregarproductos);
        estado = 0 ;//0 no guarda la cabecera 1 guardo la cabecera
        preferences = getSharedPreferences("datosapp",MODE_PRIVATE);
        editorPrefernces = preferences.edit();
        fecha = findViewById(R.id.fechaingreso);
        solicitante = findViewById(R.id.solicitante);
        fechaentrega = findViewById(R.id.fechahoraentrega);
        areas = findViewById(R.id.textarea);
        //productos = findViewById(R.id.cargarproductos);
        mas = (ImageButton) findViewById(R.id.mas);
        cantidadpro = (EditText) findViewById(R.id.cantidad);
        listado =  (ListView) findViewById(R.id.listviewproductos);
        guardar = (Button) findViewById(R.id.guardarpedido);
        observaciones = (EditText) findViewById(R.id.observacion);
        nuevo_producto = (TextView) findViewById(R.id.textNuevoProducto);
        productosaguardar = new ArrayList<productosGuardar>();
        helpproductos = new productosGuardar();
        solicitante.setText(preferences.getString("usuario","Error"));
        areas.setText(preferences.getString("nombrearea","Error"));
        btnescojer = (Button) findViewById(R.id.btnEscojer);
        listadoproductosId = new ArrayList<String>();
        cantidades = new ArrayList<Integer>();


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());//seteo la fecha actual
        Date date = new Date();
        estado = 0;
        fechadia = dateFormat.format(date);

        //list view
        listadoproductos = new ArrayList<String>();
        //listadoproductos.add("daniel prueba");
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line,listadoproductos);
        //list view
        fecha.setText(fechadia);
        fechaentrega.setText(fechadia);

        urlguardarproduc = getString(R.string.api_pedidos);
        urlareas = getString(R.string.api_areas);
        urlproducto = getString(R.string.api_productos);
        //recicler
        nuevosproductos = new ArrayList<productosGuardar>();
        baseproductos = new ArrayList<productosGuardar>();
        help = new productosGuardar();

        buscarareas();

        listado.setAdapter(adapter);
        btnescojer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(agregarproductos.this,escojerProducto.class));
            }
        });
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listadoproductos.size() <= 0)
                    Toast.makeText(agregarproductos.this,"Minimo un producto por pedido",Toast.LENGTH_LONG).show();
                else
                {
                    guardarcabecera();
                }


                //realizar la insercion repitiendo por la cantidad de item
                //sacar los datos de un array list
            }
        });
        listado.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int posicion=i;
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(agregarproductos.this);
                dialogo1.setTitle("Importante"); dialogo1.setMessage("¿ Elimina este Poducto ?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener()
                { public void onClick(DialogInterface dialogo1, int id)
                { listadoproductos.remove(posicion);
                    listadoproductosId.remove(posicion);
                    adapter.notifyDataSetChanged(); }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
                { public void onClick(DialogInterface dialogo1, int id) { } });
                dialogo1.show();
                return false;
            }
        });
        mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//guardo los datos

                if(!cantidadpro.getText().toString().isEmpty()) {
                    if(Integer.valueOf(cantidadpro.getText().toString()) > 0)
                    {
                        help.setProducto(preferences.getString("producto", null));
                        help.setId_producto(preferences.getString("Idproducto", null));
                        help.setCantidad(Integer.valueOf(cantidadpro.getText().toString()));
                        Log.d("agragar producto", help.getId_producto() + "," + help.getCantidad());
                        masproductos = new adaptermasproductos(help);
                        masproductos.notifyDataSetChanged();
                        listadoproductos.add(nuevo_producto.getText().toString() + "  CANTIDAD:" + Integer.valueOf(cantidadpro.getText().toString()));
                        listadoproductosId.add(idproducto);
                        cantidades.add(Integer.valueOf(cantidadpro.getText().toString()));

                        adapter.notifyDataSetChanged();
                        cantidadpro.setText("");
                        nuevosproductos.add(help);
                    }
                    else
                    {
                        Toast.makeText(agregarproductos.this,"No se admiten valores en cero" ,Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(agregarproductos.this,"La cantidad no puede estar en blanco",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        nuevo_producto.setText(preferences.getString("producto",null));
        idproducto = preferences.getString("Idproducto",null);

        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            startActivity(new Intent(agregarproductos.this,Listado.class));
            return false;
        }
        return super.onKeyDown(keyCode, event);

    }

    public void buscarareas()
    {
        Log.d("agregarproductos","si entroarea");
        JsonObjectRequest requerimiento = new JsonObjectRequest(Request.Method.GET, urlareas, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");

                    ArrayList array = new ArrayList<String>();
                    for(int i=0;i < jsonArray.length();i++)
                    {
                        jsonobject = new JSONObject(jsonArray.getJSONObject(i).toString());
                        array.add(jsonobject.getString("nombre"));
                    }


                    Log.d("agregarproductos",jsonobject.getString("nombre"));
                }catch (JSONException e)
                {
                    Toast.makeText(agregarproductos.this,"error de sistema contactar con sistemas",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(agregarproductos.this,"error con la obtencion de datos",Toast.LENGTH_LONG).show();
            }
        });

        n_requeriminto = Volley.newRequestQueue(this);
        n_requeriminto.add(requerimiento);
    }
    //$v_fechacabecera,$v_id_solicitante,$v_id_area,$v_fechaingreso,$v_observaciones,$v_id_producto,$v_cantidad,$v_fecgaingresovalidar
    public void guardarproductos(String id_producto,int cantidad)
    {
        StringRequest requerimiento = new StringRequest(Request.Method.POST, urlguardarproduc, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(agregarproductos.this,"error guardado",Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("v_fechaingreso", fechadia);
                parametros.put("v_id_producto", id_producto);
                parametros.put("v_cantidad", Integer.toString(cantidad));
                parametros.put("v_fecgaingresovalidar", fechadia);
                parametros.put("v_estado", Integer.toString(estado));

                return parametros;
            }
        };
        n_requeriminto = Volley.newRequestQueue(this);
        n_requeriminto.add(requerimiento);
    }
    public void guardarcabecera()
    {
        StringRequest requerimiento = new StringRequest(Request.Method.POST, urlguardarproduc, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                estado = 1;
                Log.d("tamaño de lista",""+nuevosproductos.size());
                for (int i = 0; i<listadoproductos.size();i++)
                {
                    guardarproductos(listadoproductosId.get(i),(Integer) cantidades.get(i));
                    Log.d("agragar producto",listadoproductosId.get(i)+","+(Integer) cantidades.get(i));
                }
                Toast.makeText(agregarproductos.this,"guardado",Toast.LENGTH_LONG).show();
                startActivity(new Intent(agregarproductos.this,Listado.class));
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(agregarproductos.this,"error guardado",Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("v_fechacabecera", fechadia);
                parametros.put("v_id_solicitante", preferences.getString("id_usuario","1"));
                parametros.put("v_id_area",preferences.getString("id_area","Error"));//cambiar junto con los datos del usuario
                parametros.put("v_fechaingreso", fechadia);
                parametros.put("v_observaciones", observaciones.getText().toString());
                parametros.put("v_fecgaingresovalidar", fechadia);
                parametros.put("v_estado", Integer.toString(estado));
                return parametros;
            }
        };
        n_requeriminto = Volley.newRequestQueue(this);
        n_requeriminto.add(requerimiento);
    }
}