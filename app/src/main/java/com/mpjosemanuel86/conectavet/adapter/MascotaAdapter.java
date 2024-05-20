package com.mpjosemanuel86.conectavet.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mpjosemanuel86.conectavet.R;
import com.mpjosemanuel86.conectavet.model.Mascota;

import java.util.List;

public class MascotaAdapter extends RecyclerView.Adapter<MascotaAdapter.MascotaViewHolder> {

    private List<Mascota> mascotas;

    public MascotaAdapter(List<Mascota> mascotas) {
        this.mascotas = mascotas;
    }

    @NonNull
    @Override
    public MascotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_mascota, parent, false);
        return new MascotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MascotaViewHolder holder, int position) {
        Mascota mascota = mascotas.get(position);
        holder.mascotaNombre.setText(mascota.getNombreMascota());
        // Configurar otros campos de la mascota según sea necesario
    }

    @Override
    public int getItemCount() {
        return mascotas.size();
    }

    public void startListening() {
        // Implementar si es necesario
    }

    public void stopListening() {
        // Implementar si es necesario
    }

    public static class MascotaViewHolder extends RecyclerView.ViewHolder {
        TextView mascotaNombre;
        // Otros campos de la mascota

        public MascotaViewHolder(@NonNull View itemView) {
            super(itemView);
            mascotaNombre = itemView.findViewById(R.id.tvNombreMascota);
            // Inicializar otros campos de la mascota
        }
    }
}
