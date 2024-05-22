package com.mpjosemanuel86.conectavet.adapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.mpjosemanuel86.conectavet.model.Cliente;
import com.mpjosemanuel86.conectavet.ui.fragment.CrearClienteFragment;

import java.util.List;
public class ClienteAdapter extends FirestoreRecyclerAdapter<Cliente, ClienteAdapter.ViewHolder> {
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    Activity activity;
    FragmentManager fm;


    public ClienteAdapter(@NonNull FirestoreRecyclerOptions<Cliente> options, Activity activity, FragmentManager fm) {
        super(options);
        this.activity = activity;
        this.fm = fm;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Cliente cliente) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(viewHolder.getAdapterPosition());
        final String id = documentSnapshot.getId();

        viewHolder.direccionCliente.setText(cliente.getDireccionCliente());
        viewHolder.nombreCliente.setText(cliente.getNombreCliente());
        viewHolder.telefonoCliente.setText(cliente.getTelefonoCliente());

        viewHolder.btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(activity, CrearClienteFragment.class);
                i.putExtra("id_cliente",id);
              //  activity.startActivity(i);

                CrearClienteFragment crearClienteFragment = new CrearClienteFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id_cliente", id);
                crearClienteFragment.setArguments(bundle);
                crearClienteFragment.show(fm, "abrir fragment");
            }
        });
        viewHolder.btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                borrarCliente(id);
            }
        });
    }

    private void borrarCliente(String id) {
        mFirestore.collection("cliente").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(activity, "Cliente eliminado correctamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Error al eliminar cliente", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_cliente, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView direccionCliente, nombreCliente, telefonoCliente;
        ImageButton btn_eliminar, btn_editar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            direccionCliente = itemView.findViewById(R.id.tvDireccion);
            nombreCliente = itemView.findViewById(R.id.tvNombre);
            telefonoCliente = itemView.findViewById(R.id.tvTelefono);
            btn_eliminar = itemView.findViewById(R.id.btn_eliminar);
            btn_editar = itemView.findViewById(R.id.btn_editar);
        }
    }
}


