package com.tranred.domain;

public class Banco {

    private String codBanco;
    private String nameBanco;
    private Boolean activo;
    private Boolean checked;
    private Integer boundX;
    private Integer boundY;
    private Integer boundWidth;
    private Integer boundHeight;



    public Boolean getChecked() { return checked; }

    public void setChecked(Boolean checked) { this.checked = checked; }

    public String getCodBanco() { return codBanco; }

    public String getNameBanco() { return nameBanco; }

    public Boolean getActivo() { return activo; }

    public Integer getBoundX() { return boundX; }

    public Integer getBoundY() { return boundY; }

    public Integer getBoundWidth() { return boundWidth; }

    public Integer getBoundHeight() { return boundHeight; }

    public Banco(String codBanco, String nameBanco, Boolean activo, Integer boundX, Integer boundY, Integer boundWidth, Integer boundHeight) {
        this.codBanco = codBanco;
        this.nameBanco = nameBanco;
        this.activo = activo;
        this.boundX = boundX;
        this.boundY = boundY;
        this.boundWidth = boundWidth;
        this.boundHeight = boundHeight;
    }


}
