package com.mpjosemanuel86.conectavet.model;

public class Mascota {

    private String especieMascota;
    private String nombreMascota;
    private String razaMascota;
    private String generoMascota;
    private String fechaNacimientoMascota;
    private String colorMascota;

    private String nombreCliente;
    private Cliente cliente;

    public Mascota() {
    }

    public Mascota(String nombreMascota) {
        this.nombreMascota = nombreMascota;
    }

    public Mascota(String especieMascota, String nombreMascota, String razaMascota, String generoMascota, String fechaNacimientoMascota, String colorMascota, Cliente cliente) {
        this.especieMascota = especieMascota;
        this.nombreMascota = nombreMascota;
        this.razaMascota = razaMascota;
        this.generoMascota = generoMascota;
        this.fechaNacimientoMascota = fechaNacimientoMascota;
        this.colorMascota = colorMascota;
        this.cliente = cliente;
    }

    public String getEspecieMascota() {
        return especieMascota;
    }

    public void setEspecieMascota(String especieMascota) {
        this.especieMascota = especieMascota;
    }

    public String getNombreMascota() {
        return nombreMascota;
    }

    public void setNombreMascota(String nombreMascota) {
        this.nombreMascota = nombreMascota;
    }

    public String getRazaMascota() {
        return razaMascota;
    }

    public void setRazaMascota(String razaMascota) {
        this.razaMascota = razaMascota;
    }

    public String getGeneroMascota() {
        return generoMascota;
    }

    public void setGeneroMascota(String generoMascota) {
        this.generoMascota = generoMascota;
    }

    public String getFechaNacimientoMascota() {
        return fechaNacimientoMascota;
    }

    public void setFechaNacimientoMascota(String fechaNacimientoMascota) {
        this.fechaNacimientoMascota = fechaNacimientoMascota;
    }

    public String getColorMascota() {
        return colorMascota;
    }

    public void setColorMascota(String colorMascota) {
        this.colorMascota = colorMascota;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public void setCliente(Cliente cliente){
        this.cliente = cliente;
    }

    public Cliente getCliente(){
        return this.cliente;
    }
}
