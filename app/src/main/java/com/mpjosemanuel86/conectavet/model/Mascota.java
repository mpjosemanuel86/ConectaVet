package com.mpjosemanuel86.conectavet.model;

public class Mascota {

    String especieMascota, nombreMascota, razaMascota, generoMascota;

    public Mascota() {
    }

    public Mascota(String especieMascota,  String nombreMascota, String razaMascota, String generoMascota) {

        this.especieMascota = especieMascota;
        this.nombreMascota = nombreMascota;
        this.razaMascota = razaMascota;
        this.generoMascota = generoMascota;


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

    public void setGeneroMascota(String tamanioMascota) {
        this.generoMascota = tamanioMascota;
    }
}

