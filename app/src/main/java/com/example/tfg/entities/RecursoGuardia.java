package com.example.tfg.entities;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class RecursoGuardia implements Serializable {
    private Long id;
    private Long guardiaId;
    @SerializedName("nExpediente")
    private String nExpediente;
    private Boolean resuelto;

    public RecursoGuardia() {
    }

    public RecursoGuardia(Long id, Long guardiaId, String nExpediente, Boolean resuelto) {
        this.id = id;
        this.guardiaId = guardiaId;
        this.nExpediente = nExpediente;
        this.resuelto = resuelto;
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

    public Boolean getResuelto() {
        return resuelto;
    }

    public void setResuelto(Boolean resuelto) {
        this.resuelto = resuelto;
    }
}
