package com.example.materialempaque.adaptadoresrecycler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.materialempaque.R;
import com.example.materialempaque.clases.productosGuardar;

public class adaptermasproductos extends RecyclerView.Adapter<adaptermasproductos.ViewHolder> {

    productosGuardar producto;
    int posicion;

    public adaptermasproductos(productosGuardar producto) {
        this.producto = producto;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_anadir_producto,null,false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.asignardata(producto);
    }


    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView producto, caontidad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            producto = (TextView) itemView.findViewById(R.id.textproducto);
            caontidad = (TextView) itemView.findViewById(R.id.textcantidad);
        }

        public void asignardata(productosGuardar f) {
            producto.setText(f.getProducto());
            caontidad.setText(f.getCantidad());
        }
    }
}
