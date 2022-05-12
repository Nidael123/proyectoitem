package com.example.materialempaque.adaptadoresrecycler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.materialempaque.R;
import com.example.materialempaque.clases.listadoobjetos;
import com.example.materialempaque.clases.productosGuardar;

import java.util.ArrayList;

public class adaptadorListado extends RecyclerView.Adapter<adaptadorListado.ViewHolder> {

    ArrayList <listadoobjetos> datas = new ArrayList<listadoobjetos>();

    public adaptadorListado(ArrayList<listadoobjetos> data) {
        this.datas = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//enlaza el archivo con el item list
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_lista,null,false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {//enlace para de el adaptador y la clase al final
        Log.d("Adaptador",datas.get(position).getFecha());
        holder.asignaciondata(datas.get(position));
    }

    @Override
    public int getItemCount() { //tamaño de laista
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {//aui referncio a los objetos de item_lista

        TextView fecha, solicitante,area;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fecha = (TextView) itemView.findViewById(R.id.fecha);
            solicitante = (TextView) itemView.findViewById(R.id.solicitante);
            area = (TextView) itemView.findViewById(R.id.area);
            }

        public void asignaciondata(listadoobjetos listadoobjetos) {
            Log.d("Adaptador",listadoobjetos.getFecha());
            fecha.setText(listadoobjetos.getFecha());
            solicitante.setText(listadoobjetos.getSolicitante());
            area.setText(listadoobjetos.getAreasolicitante());
        }
    }
}
