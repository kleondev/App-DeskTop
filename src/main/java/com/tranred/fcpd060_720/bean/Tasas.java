package com.tranred.fcpd060_720.bean;

import java.io.Serializable;

public class Tasas implements Serializable{

    private String id;
    private String fecha;
    private String valor;    

    public Tasas() {
    }

    public Tasas(String id, String fecha, String valor) {
        super();
        this.id = id;
        this.fecha = fecha;
        this.valor = valor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

}
