package com.example.tfg;

import java.io.Serializable;

public class ApelacionGuardia implements Serializable {
    private Long id;
    private Long guardiaId;
    private String nExpediente;
    private boolean admitido;
    private boolean presentado;
    private boolean sentencia;

    public ApelacionGuardia(Long id, Long guardiaId, String nExpediente, boolean admitido, boolean presentado, boolean sentencia) {
        this.id = id;
        this.guardiaId = guardiaId;
        this.nExpediente = nExpediente;
        this.admitido = admitido;
        this.presentado = presentado;
        this.sentencia = sentencia;
    }

    public ApelacionGuardia() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGuardiaId() {
        return guardiaId;
    }

    public void setGuardiaId(Long guardiaId) {
        this.guardiaId = guardiaId;
    }

    public String getnExpediente() {
        return nExpediente;
    }

    public void setnExpediente(String nExpediente) {
        this.nExpediente = nExpediente;
    }

    public boolean isAdmitido() {
        return admitido;
    }

    public void setAdmitido(boolean admitido) {
        this.admitido = admitido;
    }

    public boolean isPresentado() {
        return presentado;
    }

    public void setPresentado(boolean presentado) {
        this.presentado = presentado;
    }

    public boolean isSentencia() {
        return sentencia;
    }

    public void setSentencia(boolean sentencia) {
        this.sentencia = sentencia;
    }
}
