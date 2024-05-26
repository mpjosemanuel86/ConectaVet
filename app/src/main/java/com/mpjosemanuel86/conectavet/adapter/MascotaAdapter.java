package com.mpjosemanuel86.conectavet.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.mpjosemanuel86.conectavet.model.Mascota;
import com.mpjosemanuel86.conectavet.ui.fragment.CrearMascotaFragment;

public class MascotaAdapter extends FirestoreRecyclerAdapter<Mascota, MascotaAdapter.ViewHolder> {
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    Activity activity;
    FragmentManager fm;

    public MascotaAdapter(@NonNull FirestoreRecyclerOptions<Mascota> options, Activity activity, FragmentManager fm) {
        super(options);
        this.activity = activity;
        this.fm = fm;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Mascota mascota) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(viewHolder.getAdapterPosition());
        final String id = documentSnapshot.getId();

        viewHolder.nombreMascota.setText(mascota.getNombreMascota());
        viewHolder.especieMascota.setText(mascota.getEspecieMascota());
        viewHolder.razaMascota.setText(mascota.getRazaMascota());

        // Establecer la imagen según la especie de la mascota
        if (mascota.getEspecieMascota().equalsIgnoreCase("perro")) {
            viewHolder.photo.setImageResource(R.drawable.perro_generico);
        } else if (mascota.getEspecieMascota().equalsIgnoreCase("gato")) {
            viewHolder.photo.setImageResource(R.drawable.gato_generico);
        } else {
            // Si la especie no coincide con ninguna de las anteriores, puedes mostrar una imagen genérica predeterminada o dejarla vacía
            viewHolder.photo.setImageResource(R.drawable.imagen_generica_predeterminada);
        }

        viewHolder.btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrearMascotaFragment crearMascotaFragment = new CrearMascotaFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id_mascota", id);
                crearMascotaFragment.setArguments(bundle);
                crearMascotaFragment.show(fm, "abrir fragment");
            }
        });

        viewHolder.btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarMascota(id);
            }
        });
    }

    private void borrarMascota(String id) {
        mFirestore.collection("pet").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(activity, "Mascota eliminada correctamente", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity, "Error al eliminar mascota", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_mascota, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreMascota, especieMascota, razaMascota;
        ImageButton btn_eliminar, btn_editar;
        ImageView photo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreMascota = itemView.findViewById(R.id.tvNombreMascota);
            especieMascota = itemView.findViewById(R.id.tvEspecie);
            razaMascota = itemView.findViewById(R.id.tvRaza);
            btn_eliminar = itemView.findViewById(R.id.btn_eliminar);
            btn_editar = itemView.findViewById(R.id.btn_editar);
            photo = itemView.findViewById(R.id.photo);
        }
    }
}
