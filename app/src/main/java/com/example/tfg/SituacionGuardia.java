package com.example.tfg;

public class SituacionGuardia {

    private Long id;
    private Guardia guardia;
    private String comentarios;
    private String nTalon;
    private String euros;
    private Boolean presentado;
    private Boolean validado;
    private Boolean pagado;

    public SituacionGuardia(Long id, Guardia guardia, String comentarios, String nTalon, String euros, Boolean presentado, Boolean validado, Boolean pagado) {
        this.id = id;
        this.guardia = guardia;
        this.comentarios = comentarios;
        this.nTalon = nTalon;
        this.euros = euros;
        this.presentado = presentado;
        this.validado = validado;
        this.pagado = pagado;
    }

    public SituacionGuardia() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Guardia getGuardia() {
        return guardia;
    }

    public void setGuardia(Guardia guardia) {
        this.guardia = guardia;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getNTalon() {
        return nTalon;
    }

    public void setNTalon(String nTalon) {
        this.nTalon = nTalon;
    }

    public String getEuros() {
        return euros;
    }

    public void setEuros(String euros) {
        this.euros = euros;
    }

    public Boolean getPresentado() {
        return presentado != null ? presentado : false;
    }

    public void setPresentado(Boolean presentado) {
        this.presentado = presentado;
    }

    public Boolean getValidado() {
        return validado != null ? validado : false;
    }

    public void setValidado(Boolean validado) {
        this.validado = validado;
    }

    public Boolean getPagado() {
        return pagado != null ? pagado : false;
    }

    public void setPagado(Boolean pagado) {
        this.pagado = pagado;
    }
}

