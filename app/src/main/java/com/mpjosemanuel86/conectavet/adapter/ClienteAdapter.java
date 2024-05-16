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
import com.mpjosemanuel86.conectavet.model.Cliente;

import java.util.List;

public class ClienteAdapter extends FirestoreRecyclerAdapter<Cliente, ClienteAdapter.ViewHolder> {
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    Activity activity;

    public ClienteAdapter(@NonNull FirestoreRecyclerOptions<Cliente> options, Activity activity) {

        super(options);
        this.activity = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Cliente cliente) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(viewHolder.getAdapterPosition());
        final String id = documentSnapshot.getId();

        viewHolder.direccionCliente.setText(cliente.getDireccionCliente());
        viewHolder.nombreCliente.setText(cliente.getNombreCliente());
        viewHolder.telefonoCliente.setText(cliente.getTelefonoCliente());
        viewHolder.btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarCliente(id);

                }
        });
    }

    private void borrarCliente(String id) {
        String idString = String.valueOf(id);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("¿Estás seguro de eliminar este cliente de forma permanente?")
                        .setTitle("Confirmar Eliminación")
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        mFirestore.collection("cliente").document(idString).delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(activity, "Cliente eliminado correctamente", Toast.LENGTH_SHORT).show();
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_cliente, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView direccionCliente, nombreCliente, telefonoCliente;
        ImageButton btn_eliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            direccionCliente = itemView.findViewById(R.id.tvDireccion);
            nombreCliente = itemView.findViewById(R.id.tvNombre);
            telefonoCliente = itemView.findViewById(R.id.tvTelefono);
            btn_eliminar = itemView.findViewById(R.id.btn_eliminar);
        }
    }
}


