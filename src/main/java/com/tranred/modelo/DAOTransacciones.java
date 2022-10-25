package com.tranred.modelo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOTransacciones extends ConnManager {
    static final Logger LOGGER = LogManager.getLogger(DAOTransacciones.class.getName());

    public DAOTransacciones() {
    }

    public List<String[][]> getTransacciones(CallableStatement sentencia) throws Exception {
        List<String[][]> lista = new ArrayList();
        String[][] registros = (String[][])null;
        LOGGER.info("CONSULTA INICIADA");
        List<List<String>> filas = this.getResults(this.consultar(sentencia));
        LOGGER.info("CONSULTA FINALIZADA");
        Integer i;
        List row;
        if (filas.isEmpty()) {
            LOGGER.info("Sin informacion para procesar");
            lista.add(registros);
        } else {
            LOGGER.info("row: " + filas.size() + " | columnas: " + ((List)filas.get(0)).size());
            registros = new String[filas.size()][];

            for(i = 0; i < filas.size(); i = i + 1) {
                row = (List)filas.get(i);
                registros[i] = (String[])row.toArray(new String[row.size()]);
                ((List)filas.get(i)).clear();
            }

            lista.add(registros);
            filas.clear();
            registros = (String[][])null;
        }

        while(true) {
            while(sentencia.getMoreResults()) {
                LOGGER.info("CONSULTA INICIADA - OTRAS");
                filas = this.getResults(sentencia.getResultSet());
                LOGGER.info("CONSULTA FINALIZADA - OTRAS");
                if (filas.isEmpty()) {
                    LOGGER.info("Sin informacion para procesar");
                    lista.add(registros);
                } else {
                    LOGGER.info("filas: " + filas.size() + " | columnas: " + ((List)filas.get(0)).size());
                    registros = new String[filas.size()][];

                    for(i = 0; i < filas.size(); i = i + 1) {
                        row = (List)filas.get(i);
                        registros[i] = (String[])row.toArray(new String[row.size()]);
                        ((List)filas.get(i)).clear();
                    }

                    lista.add(registros);
                    filas.clear();
                    registros = (String[][])null;
                }
            }

            return lista;
        }
    }

    public String[][] getTransacciones(PreparedStatement sentencia) throws Exception {
        String[][] registros = (String[][])null;
        LOGGER.info("CONSULTA INICIADA");
        List<List<String>> filas = this.getResults(this.consultar(sentencia));
        LOGGER.info("CONSULTA FINALIZADA");
        if (filas.isEmpty()) {
            LOGGER.info("Sin informacion para procesar");
        } else {
            LOGGER.info("filas: " + filas.size() + " | columnas: " + ((List)filas.get(0)).size());
            registros = new String[filas.size()][];

            for(Integer i = 0; i < filas.size(); i = i + 1) {
                List<String> row = (List)filas.get(i);
                registros[i] = (String[])row.toArray(new String[row.size()]);
                ((List)filas.get(i)).clear();
            }

            filas.clear();
        }

        return registros;
    }

    public List<List<String>> getResults(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        Integer columnsNumber = rsmd.getColumnCount();
        ArrayList filas = new ArrayList();

        while(rs.next()) {
            List<String> columnas = new ArrayList();

            for(Integer i = 1; i <= columnsNumber; i = i + 1) {
                columnas.add(rs.getString(i));
            }

            filas.add(columnas);
        }

        rs.close();
        return filas;
    }
}
