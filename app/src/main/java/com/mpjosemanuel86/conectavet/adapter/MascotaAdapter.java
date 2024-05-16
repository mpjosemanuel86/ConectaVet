package com.mpjosemanuel86.conectavet.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mpjosemanuel86.conectavet.R;
import com.mpjosemanuel86.conectavet.model.Mascota;

public class MascotaAdapter extends FirestoreRecyclerAdapter<Mascota, MascotaAdapter.ViewHolder> {
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    Activity activity;

    public MascotaAdapter(@NonNull FirestoreRecyclerOptions<Mascota> options, Activity activity) {

        super(options);
        this.activity = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull MascotaAdapter.ViewHolder viewHolder, int i, @NonNull Mascota mascota) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(viewHolder.getAdapterPosition());
        final String id = documentSnapshot.getId();


        viewHolder.especieMascota.setText(mascota.getEspecieMascota());
        viewHolder.nombreMascota.setText(mascota.getNombreMascota());
        viewHolder.razaMascota.setText(mascota.getRazaMascota());
        viewHolder.generoMascota.setText(mascota.getGeneroMascota());

        viewHolder.btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarMascota(id);

            }


        });
    }

    private void borrarMascota(String id) {
        String idString = String.valueOf(id);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("¿Estás seguro de eliminar esta mascota de forma permanente?")
                .setTitle("Confirmar Eliminación")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        mFirestore.collection("pet").document(idString).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(activity, "Mascota eliminado correctamente", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(activity, "Error al eliminar cliente", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }

                });
        AlertDialog dialog= builder.create();
        dialog.show();
    }

    @NonNull
    @Override
    public MascotaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_mascota, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView especieMascota, nombreMascota, razaMascota,generoMascota;
        ImageButton btn_eliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            especieMascota = itemView.findViewById(R.id.tvEspecie);
            nombreMascota = itemView.findViewById(R.id.tvNombreMascota);
            razaMascota = itemView.findViewById(R.id.tvRaza);
            generoMascota = itemView.findViewById(R.id.tvGenero);
            btn_eliminar = itemView.findViewById(R.id.btn_eliminar);
        }
    }
}

