package com.mpjosemanuel86.conectavet.adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mpjosemanuel86.conectavet.R;
import com.mpjosemanuel86.conectavet.model.Cliente;

import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder> {


    private List<Cliente> mData;
    private LayoutInflater mInflater;

    public ClienteAdapter(Context context, List<Cliente> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.view_cliente, parent, false);
        return new ClienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        Cliente cliente = mData.get(position);
        holder.bind(cliente);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ClienteViewHolder extends RecyclerView.ViewHolder {
        TextView nombreCliente;
        TextView telefonoCliente;
        TextView direccionCliente;



        public ClienteViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreCliente = itemView.findViewById(R.id.tvNombre);
            telefonoCliente = itemView.findViewById(R.id.tvTelefono);
            direccionCliente = itemView.findViewById(R.id.tvDireccion);
        }

        public void bind(Cliente cliente) {
            nombreCliente.setText(cliente.getNombreCliente());
            telefonoCliente.setText(cliente.getTelefonoCliente());
            direccionCliente.setText(cliente.getDireccionCliente());
        }
    }
}
