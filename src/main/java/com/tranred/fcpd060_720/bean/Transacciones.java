package com.tranred.fcpd060_720.bean;

import java.io.Serializable;

public class Transacciones implements Serializable {

    private String linea;
    private String orden;
    private String lote;

    public String getLinea() {
        return linea;
    }

    public String getOrden() {
        return orden;
    }

    public String getLote() {
        return lote;
    }

    public Transacciones() {
    }


    public Transacciones(String linea, String orden, String lote) {
        this.linea = linea;
        this.orden = orden;
        this.lote = lote;
    }
}
