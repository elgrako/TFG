package com.example.tfg.entities;

import java.io.Serializable;

public class Registro implements Serializable {
    private Long id;
    private String nombre;
    private String dni;
    private String nExpediente;
    private double euros;
    private String email;
    private int telefono;
    private Boolean presentado;
    private Boolean validado;
    private Boolean pagado;
    private Integer nTalon;
    private String comentarios;


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

    public Registro(Long id, String nombre, String dni, String nExpediente, double euros,
                    String email, int telefono, Boolean presentado, Boolean validado,
                    Boolean pagado, Integer nTalon, String comentarios) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.nExpediente = nExpediente;
        this.euros = euros;
        this.email = email;
        this.telefono = telefono;
        this.presentado = presentado;
        this.validado = validado;
        this.pagado = pagado;
        this.nTalon = nTalon;
        this.comentarios = comentarios;
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

    public Boolean getPresentado() {
        return presentado;
    }

    public void setPresentado(Boolean presentado) {
        this.presentado = presentado;
    }

    public Boolean getValidado() {
        return validado;
    }

    public void setValidado(Boolean validado) {
        this.validado = validado;
    }

    public Boolean getPagado() {
        return pagado;
    }

    public void setPagado(Boolean pagado) {
        this.pagado = pagado;
    }

    public Integer getnTalon() {
        return nTalon;
    }

    public void setnTalon(Integer nTalon) {
        this.nTalon = nTalon;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + " / DNI: " + dni + " / Expediente: " + nExpediente +
                " / Euros: " + euros + "/ Email: " + email + "/ Telefono: " + telefono;
    }
}
