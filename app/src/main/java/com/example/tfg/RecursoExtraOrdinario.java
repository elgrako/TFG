package com.example.tfg;

import java.io.Serializable;

public class RecursoExtraOrdinario implements Serializable {
    private Long id;
    private Long guardiaId;
    private Integer nExpediente;
    private Boolean admitido;

    public RecursoExtraOrdinario() {
    }

    public RecursoExtraOrdinario(Long id, Long guardiaId, Integer nExpediente, Boolean admitido) {
        this.id = id;
        this.guardiaId = guardiaId;
        this.nExpediente = nExpediente;
        this.admitido = admitido;
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

    public Integer getnExpediente() {
        return nExpediente;
    }

    public void setnExpediente(Integer nExpediente) {
        this.nExpediente = nExpediente;
    }

    public Boolean getAdmitido() {
        return admitido;
    }

    public void setAdmitido(Boolean admitido) {
        this.admitido = admitido;
    }
}
