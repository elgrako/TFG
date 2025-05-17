package com.example.tfg;

public class Guardia {
    private String nombreAsistido;
    private String diaActuacion;
    private boolean porJuzgado;
    private boolean cobrado;

    public Guardia(String nombreAsistido, String diaActuacion, boolean porJuzgado, boolean cobrado) {
        this.nombreAsistido = nombreAsistido;
        this.diaActuacion = diaActuacion;
        this.porJuzgado = porJuzgado;
        this.cobrado = cobrado;
    }

    public String getNombreAsistido() {
        return nombreAsistido;
    }

    public String getDiaActuacion() {
        return diaActuacion;
    }

    public boolean isPorJuzgado() {
        return porJuzgado;
    }

    public boolean isCobrado() {
        return cobrado;
    }

    @Override
    public String toString() {
        return nombreAsistido + " (" + diaActuacion + ")" +
                "\nPor juzgado: " + (porJuzgado ? "Sí" : "No") +
                " | Cobrado: " + (cobrado ? "Sí" : "No");
    }
}
