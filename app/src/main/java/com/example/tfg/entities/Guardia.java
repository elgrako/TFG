package com.example.tfg.entities;

public class Guardia {
    private Long id;
    private String nombreAsistido;
    private String diaActuacion;
    private boolean porJuzgado;
    private boolean cobrado;

    public Guardia(Long id, String nombreAsistido, String diaActuacion, boolean porJuzgado, boolean cobrado) {
        this.id = id;
        this.nombreAsistido = nombreAsistido;
        this.diaActuacion = diaActuacion;
        this.porJuzgado = porJuzgado;
        this.cobrado = cobrado;
    }

    public Guardia() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreAsistido() {
        return nombreAsistido;
    }

    public void setNombreAsistido(String nombreAsistido) {
        this.nombreAsistido = nombreAsistido;
    }

    public String getDiaActuacion() {
        return diaActuacion;
    }

    public void setDiaActuacion(String diaActuacion) {
        this.diaActuacion = diaActuacion;
    }

    public boolean isPorJuzgado() {
        return porJuzgado;
    }

    public void setPorJuzgado(boolean porJuzgado) {
        this.porJuzgado = porJuzgado;
    }

    public boolean isCobrado() {
        return cobrado;
    }

    public void setCobrado(boolean cobrado) {
        this.cobrado = cobrado;
    }

    @Override
    public String toString() {
        return nombreAsistido + " (" + diaActuacion + ")" +
                "\nPor juzgado: " + (porJuzgado ? "Sí" : "No") +
                " | Cobrado: " + (cobrado ? "Sí" : "No");
    }
}
