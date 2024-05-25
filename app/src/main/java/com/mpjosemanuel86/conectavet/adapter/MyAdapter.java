package com.mpjosemanuel86.conectavet.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.mpjosemanuel86.conectavet.R;
import com.mpjosemanuel86.conectavet.model.Mascota;
import com.mpjosemanuel86.conectavet.ui.fragment.CrearClienteFragment;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreMascota, especieMascota, razaMascota, sexoMascota, nom_cliente;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreMascota = itemView.findViewById(R.id.tvNombreMascota);
            especieMascota = itemView.findViewById(R.id.tvEspecie);
            razaMascota = itemView.findViewById(R.id.tvRaza);
            sexoMascota = itemView.findViewById(R.id.tvGenero);
            nom_cliente = itemView.findViewById(R.id.tvCliente);
        }
    }
    ArrayList<Mascota> mascotas;
    LayoutInflater inflater = null;

    public MyAdapter(ArrayList<Mascota> mascotas, Context c){
        this.mascotas = mascotas;
        this.inflater = LayoutInflater.from(c);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mascotas.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_mascota, parent, false);
        return new MyAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nombreMascota.setText(mascotas.get(position).getNombreMascota());
        holder.especieMascota.setText(mascotas.get(position).getEspecieMascota());
        holder.razaMascota.setText(mascotas.get(position).getRazaMascota());
        holder.sexoMascota.setText(mascotas.get(position).getSexoMascota());
        holder.nom_cliente.setText(mascotas.get(position).getCliente().getNombreCliente());
    }

    @Override
    public int getItemViewType(int position)
    {
        Log.v(TAG, "in getItemViewType() for position " + position);
        return 0;
    }
    // override other abstract methods here

}

