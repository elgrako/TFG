package com.example.tfg;

public class Datos {
    private String nombre;
    private String dni;
    private String nExpediente;
    private double euros;

    public Datos(String nombre, String dni, String nExpediente, double euros) {
        this.nombre = nombre;
        this.dni = dni;
        this.nExpediente = nExpediente;
        this.euros = euros;
    }

    public Datos() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getnExpediente() {
        return nExpediente;
    }

    public void setnExpediente(String nExpediente) {
        this.nExpediente = nExpediente;
    }

    public double getEuros() {
        return euros;
    }

    public void setEuros(double euros) {
        this.euros = euros;
    }
}
