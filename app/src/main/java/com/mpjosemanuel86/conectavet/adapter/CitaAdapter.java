package com.mpjosemanuel86.conectavet.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mpjosemanuel86.conectavet.R;
import com.mpjosemanuel86.conectavet.model.Cita;
import com.mpjosemanuel86.conectavet.ui.fragment.CrearClienteFragment;

public class CitaAdapter extends FirestoreRecyclerAdapter<Cita, CitaAdapter.ViewHolder> {
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private Activity activity;


    public CitaAdapter(@NonNull FirestoreRecyclerOptions<Cita> options, Activity activity, FragmentManager fm) {
        super(options);
        this.activity = activity;

    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Cita cita) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(viewHolder.getAdapterPosition());
        final String id = documentSnapshot.getId();

        viewHolder.fechaCita.setText(cita.getFechaCita());
        viewHolder.horaCita.setText(cita.getHoraCita());
        viewHolder.nombreCliente.setText(cita.getNombreCliente());

        viewHolder.btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí deberías definir la lógica para editar una cita si es necesario
            }
        });
        viewHolder.btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarCita(id);
            }
        });
    }

    private void borrarCita(String id) {
        mFirestore.collection("citas").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(activity, "Cita eliminada correctamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Error al eliminar cita", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cita, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fechaCita, horaCita, nombreCliente;
        ImageButton btn_eliminar, btn_editar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fechaCita = itemView.findViewById(R.id.tvFechaCita);
            horaCita = itemView.findViewById(R.id.tvHoraCita);
            nombreCliente = itemView.findViewById(R.id.tvNombreCliente);
            btn_eliminar = itemView.findViewById(R.id.btn_eliminar);
            btn_editar = itemView.findViewById(R.id.btn_editar);
        }
    }
}
