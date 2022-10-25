package com.tranred.fcpd060_720;

import com.tranred.fcpd060_720.bean.Transacciones;
import com.tranred.utils.Archivo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Proceso {

    private String[][] registrosLista;
    private List<String[][]> reversosLista;
    private Long monto;
    private Archivo a;
    private String filter = null;
    private String detalle = null;

    public Proceso() {
    }

//    public Proceso(String[][] r, Archivo a, Integer lote) {
//        this.registrosLista = r;
//        this.a = a;
//        this.lote = lote;
//    }

    public Proceso(String[][] r, Archivo a, String Detalle) {
        this.registrosLista = r;
        this.a = a;
        this.filter = Detalle;
    }

    public String[][] getRegistrosLista() {
        return registrosLista;
    }

    public void setRegistrosLista(String[][] registrosLista) {
        this.registrosLista = registrosLista;
    }

    public Long getMonto() {
        return monto;
    }

    public Archivo getA() {
        return a;
    }

    public void setA(Archivo a) {
        this.a = a;
    }

    public void iniciar() throws IOException {

        String linea;
        String[][] registros;
        List<Transacciones> lote = new ArrayList<>();
        registros = registrosLista;

        for (Integer i = 0; i < registros.length; i++) {
            String firstColumn = Objects.equals(registros[i][0], null) ? "" : registros[i][0];
            String secondColumn = Objects.equals(registros[i][1], null) ? "" : registros[i][1];
            String thirdColumn = Objects.equals(registros[i][2], null) ? "" : registros[i][2];

            if(thirdColumn != null &&  thirdColumn.equals(this.filter)) {
                lote.add(new Transacciones(firstColumn, secondColumn, thirdColumn));
            }
        }


        a.openFileWriter(Boolean.FALSE);
        a.setOutputStreamCharset("UTF8");

//        a[0].openOutputStream(Boolean.FALSE);
//        a[0].setOutputStreamCharset("IBM1047");
//        a[1].openFileWriter(Boolean.FALSE);
//        a[2].setOutputStreamCharset("UTF8");
//        a[2].openFileWriter(Boolean.FALSE);

//        linea = "101" + StringUtils.leftPad("720", 10, "0")
//                + StringUtils.repeat("0", 10)
//                + new Fecha().getString("yyMMdd")
//                + new Fecha().getString("HHmm")
//                + "1" + "094101"
//                + StringUtils.leftPad(StringUtils.left("BCO. VENEZOLANO DE CREDITO VZLA", 23), 23)
//                + StringUtils.repeat(" ", 23)
//                + StringUtils.repeat(" ", 9);
//        a[0].escribirEBCDIC(linea);
//        a[1].escribir(linea);

        for (Transacciones l : lote) {
            linea = "";
            linea += l.getLinea();
            a.escribir(linea);
        }

        a.closeFileWriter();
//        a[0].closeOutputStream();
//        a[1].closeFileWriter();
//        a[2].closeFileWriter();


//        if (trans3 != null) {
//
//            totalRegistros3 = 0l;
//            for (Transacciones t : trans3) {
//                totalRegistros3++;
//
//                linea = "6";
//                if (StringUtils.containsAny(t.getMsgType(), "0200", "0220")
//                        && StringUtils.containsAny(t.getFromAccount(), "20", "00", "30")) {
//                    linea += "27";
//                    totalDebitos += Double.parseDouble(t.getSourceNodeAmountApproved());
//                } else if (StringUtils.containsAny(t.getMsgType(), "0200", "0220")
//                        && StringUtils.containsAny(t.getFromAccount(), "10")) {
//                    linea += "37";
//                    totalDebitos += Double.parseDouble(t.getSourceNodeAmountApproved());
//                } else if (StringUtils.containsAny(t.getMsgType(), "0420")
//                        && StringUtils.containsAny(t.getFromAccount(), "20", "00", "30")) {
//                    linea += "22";
//                    totalCreditos += Double.parseDouble(t.getSourceNodeAmountApproved());
//                } else if (StringUtils.containsAny(t.getMsgType(), "0420")
//                        && StringUtils.containsAny(t.getFromAccount(), "10")) {
//                    linea += "32";
//                    totalCreditos += Double.parseDouble(t.getSourceNodeAmountApproved());
//                }
//                linea += t.getCurrencyCode().equals("928") ? "61000000" : t.getSinkNodeConversionRate() ;
//                linea += StringUtils.leftPad(t.getSinkNodeFee(), 17, "0")
//                        + StringUtils.leftPad(t.getSinkNodeAmountApproved(), 12, "0")
//                        + StringUtils.leftPad(t.getPan(), 21, "0")
//                        + t.getCurrencyCode()
//                        + StringUtils.leftPad(new BigDecimal(t.getSourceNodeAmountApproved()).setScale(0, BigDecimal.ROUND_HALF_UP).toString(), 12, "0");
//                if (StringUtils.containsAny(t.getMsgType(), "0200", "0220")
//                        && t.getTranType().equals("00")) {
//                    linea += "40";
//                } else if (t.getMsgType().equals("0200")
//                        && t.getTranType().equals("01")) {
//                    linea += "30";
//                } else if (t.getMsgType().equals("0420")
//                        && t.getTranType().equals("00")) {
//                    linea += "41";
//                } else if (t.getMsgType().equals("0420")
//                        && t.getTranType().equals("01")) {
//                    linea += "31";
//                }
//                linea += "1" + StringUtils.leftPad(totalRegistros.toString(), 15, "0");
//                a[0].escribirEBCDIC(linea);
//                a[1].escribir(linea);
//
//                totalRegistros++;
//
//                linea = "705"
//                        + StringUtils.leftPad(t.getMerchantType(), 7, "0")
//                        + "I  "
//                        + t.getCardAcceptorTermId() + "    "
//                        + Fecha.parseDate(t.getInReq(), "yyyy-MM-dd HH:mm:ss.SSS", "MMdd")
//                        + StringUtils.leftPad(t.getAuthIdRsp(), 6, "0")
//                        + StringUtils.left(t.getCardAcceptorNameLoc(), 28)
//                        + StringUtils.right(t.getCardAcceptorIdCode(), 9) + "     " + ".VE"
//                        + Fecha.parseDate(t.getInReq(), "yyyy-MM-dd HH:mm:ss.SSS", "yyyyMMdd")
//                        + StringUtils.leftPad(t.getSinkNodeReqSysTrace(), 7, "0");
//                a[0].escribirEBCDIC(linea);
//                a[1].escribir(linea);
//            }
//        }
        
//        linea = "8200"
//                + StringUtils.leftPad(Long.toString(totalRegistros), 8, "0")
//                + StringUtils.repeat("0", 10)
//                + StringUtils.leftPad(Long.toString(totalDebitos.longValue()), 15, "0")
//                + StringUtils.leftPad(Long.toString(totalCreditos.longValue()), 15, "0")
//                + "1000000000"
//                + StringUtils.repeat(" ", 18)
//                + StringUtils.repeat("0", 8)
//                + StringUtils.leftPad(loteNum.toString(), 7, "0");
//        a[0].escribirEBCDIC(linea);
//        a[1].escribir(linea);
//
//        linea = "9"
//                + StringUtils.leftPad(loteNum.toString(), 6, "0")
//                + StringUtils.leftPad(Integer.toString(new Double(totalRegistros / 10).intValue() + 1), 6, "0")
//                + StringUtils.leftPad(Long.toString(totalRegistros), 8, "0")
//                + StringUtils.repeat("0", 10)
//                + StringUtils.leftPad(Long.toString(totalDebitos.longValue()), 12, "0")
//                + StringUtils.leftPad(Long.toString(totalCreditos.longValue()), 12, "0")
//                + StringUtils.repeat(" ", 40);
//        a[0].escribirEBCDIC(linea);
//        a[1].escribir(linea);
//
//        if (!reversosLista.isEmpty()) {
//            reversos = reversosLista.get(0);
//            for (Integer i = 0; i < reversos.length; i++) {
//                a[2].escribir(reversos[i][0]);
//            }
//        }
//

//
//        monto = (long) (totalDebitos - totalCreditos);

    }
}
