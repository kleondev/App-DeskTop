package com.tranred.fcpd060_720.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "configuracion")
@XmlAccessorType (XmlAccessType.FIELD)
public class Configuracion implements Serializable {
    @XmlElement
    private String rutaX;
    @XmlElement
    private String rutaAD;
    @XmlElement
    private Boolean btrans;
    @XmlElement
    private String ip;
    @XmlElement
    private Integer port;
    @XmlElement
    private String dbname;

    public String getRutaX() {
        return rutaX;
    }

    public String getRutaAD() { return rutaAD; }

    public Boolean getBtrans() { return btrans; }

    public String getIp() { return  ip; }

    public Integer getPort() { return  port; }

    public String getDbname() { return dbname; }

}
