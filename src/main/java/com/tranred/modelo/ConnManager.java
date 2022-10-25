package com.tranred.modelo;

import java.sql.*;

public class ConnManager {
    private String driver;
    private String url;
    private String nombreIPServidorBD;
    private Integer puertoServidorBD;
    private String nombreBD;
    private String propiedades;
    private String usuarioBD;
    private String passwordUsuarioBD;
    private String connectionString;
    private Connection conexion;
    private PreparedStatement sentencia;
    private ResultSet filasConsulta;

    public ConnManager() {
    }

    public ConnManager(String driver, String url, String servidor, Integer puerto, String nombreBD, String propiedades, String usuarioBD, String passwordUsuarioBD) throws Exception {
        this.driver = driver;
        this.url = url;
        this.nombreIPServidorBD = servidor;
        this.puertoServidorBD = puerto;
        this.nombreBD = nombreBD;
        this.propiedades = propiedades;
        this.usuarioBD = usuarioBD;
        this.passwordUsuarioBD = passwordUsuarioBD;
    }

    public ConnManager conectar() throws Exception {
        try {
            Class.forName(this.driver);
        } catch (ClassNotFoundException var2) {
            throw new SQLException("Error con el driver " + var2.getMessage());
        }

        this.connectionString = this.url + this.nombreIPServidorBD + ":" + this.puertoServidorBD + "/" + this.nombreBD + this.propiedades;
        this.conexion = DriverManager.getConnection(this.connectionString, this.usuarioBD, this.passwordUsuarioBD);
        this.conexion.setAutoCommit(Boolean.FALSE);
        return this;
    }

    public Integer actualizar(PreparedStatement sentencia) throws Exception {
        try {
            return sentencia.executeUpdate();
        } catch (SQLException var3) {
            throw new SQLException("Error en modificacion de BD \nCodigo:" + var3.getErrorCode() + " Explicacion:" + var3.getMessage());
        }
    }

    public ResultSet consultar(PreparedStatement sentencia) throws Exception {
        try {
            return sentencia.executeQuery();
        } catch (SQLException var3) {
            throw new SQLException("Error en consulta de BD \nCodigo:" + var3.getErrorCode() + " Explicacion:" + var3.getMessage());
        }
    }

    public void desconectar() {
        try {
            this.conexion.close();
        } catch (SQLException var2) {
            this.conexion = null;
        }

    }

    public PreparedStatement crearSentencia(String sql) throws Exception {
        try {
            return this.conexion.prepareStatement(sql);
        } catch (SQLException var3) {
            throw new SQLException("Error en creacion de sentecia DB \nCodigo:" + var3.getErrorCode() + " Explicacion:" + var3.getMessage());
        }
    }

    public Connection getConexion() {
        return this.conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }

    public String getDriver() {
        return this.driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public ResultSet getFilasConsulta() {
        return this.filasConsulta;
    }

    public void setFilasConsulta(ResultSet filasConsulta) {
        this.filasConsulta = filasConsulta;
    }

    public String getNombreBD() {
        return this.nombreBD;
    }

    public void setNombreBD(String nombreBD) {
        this.nombreBD = nombreBD;
    }

    public String getNombreIPServidorBD() {
        return this.nombreIPServidorBD;
    }

    public void setNombreIPServidorBD(String nombreIPServidorBD) {
        this.nombreIPServidorBD = nombreIPServidorBD;
    }

    public String getPasswordUsuarioBD() {
        return this.passwordUsuarioBD;
    }

    public void setPasswordUsuarioBD(String passwordUsuarioBD) {
        this.passwordUsuarioBD = passwordUsuarioBD;
    }

    public int getPuertoServidorBD() {
        return this.puertoServidorBD;
    }

    public void setPuertoServidorBD(Integer puertoServidorBD) {
        this.puertoServidorBD = puertoServidorBD;
    }

    public PreparedStatement getSentencia() {
        return this.sentencia;
    }

    public void setSentencia(PreparedStatement sentencia) {
        this.sentencia = sentencia;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsuarioBD() {
        return this.usuarioBD;
    }

    public void setUsuarioBD(String usuarioBD) {
        this.usuarioBD = usuarioBD;
    }

    public String getPropiedades() {
        return this.propiedades;
    }

    public void setPropiedades(String propiedades) {
        this.propiedades = propiedades;
    }

    public String getConnectionString() {
        return this.connectionString = this.url + this.nombreIPServidorBD + ":" + this.puertoServidorBD + "/" + this.nombreBD + this.propiedades;
    }
}
