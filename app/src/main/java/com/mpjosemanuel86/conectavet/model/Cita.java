package com.mpjosemanuel86.conectavet.model;

public class Cita {
    private String uid;
    private String fechaCita;
    private String horaCita;
    private String nombreCliente;

    public Cita() {}

    public Cita(String uid, String fechaCita, String horaCita, String nombreCliente) {
        this.uid = uid;
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
        this.nombreCliente = nombreCliente;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(String fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getHoraCita() {
        return horaCita;
    }

    public void setHoraCita(String horaCita) {
        this.horaCita = horaCita;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

}
