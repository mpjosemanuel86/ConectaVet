package com.mpjosemanuel86.conectavet.model;

public class Cliente {
    private String uid;
    private String nombreCliente;
    private String direccionCliente;
    private String telefonoCliente;

    public Cliente() {}

    public Cliente(String uid, String nombreCliente, String direccionCliente, String telefonoCliente) {
        this.uid = uid;
        this.nombreCliente = nombreCliente;
        this.direccionCliente = direccionCliente;
        this.telefonoCliente = telefonoCliente;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    @Override
    public String toString() {
        // Mostrar el nombre del cliente y los primeros 5 caracteres del UID
        return nombreCliente + " (" + uid.substring(0, 5) + ")";
    }
}
