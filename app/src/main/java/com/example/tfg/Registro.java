package com.example.tfg;

import java.io.Serializable;

public class Registro implements Serializable {
    private Long id;
    private String nombre;
    private String dni;
    private String nExpediente;
    private double euros;
    private String email;
    private int telefono;

    public Registro(Long id, String nombre, String dni, String nExpediente, double euros, String email, int telefono) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.nExpediente = nExpediente;
        this.euros = euros;
        this.email = email;
        this.telefono = telefono;
    }

    public Registro() {
    }

    public Registro(String nombre, String dni, String nExpediente, double euros) {
        this.nombre = nombre;
        this.dni = dni;
        this.nExpediente = nExpediente;
        this.euros = euros;
        this.email = "";
        this.telefono = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + " / DNI: " + dni + " / Expediente: " + nExpediente +
                " / Euros: " + euros + "/ Email: " + email + "/ Telefono: " + telefono;
    }
}
