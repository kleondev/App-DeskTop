package com.tranred.fcpd060_720;

import com.tranred.domain.Banco;
import com.tranred.fcpd060_720.bean.Configuracion;
import com.tranred.modelo.DAOTransacciones;
import com.tranred.utils.Archivo;
import com.tranred.utils.DateLabelFormatter;
import com.tranred.utils.Fecha;
import com.tranred.utils.Numero;
import org.apache.logging.log4j.LogManager;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


public class Form extends JFrame {

    private final JPanel contentPane;
    private final JDatePickerImpl datePicker, datePicker2, datePicker3;
    static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(Form.class.getName());
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private MouseListener ml;
    private final JLabel lblMensaje;
    private final JTextField textField;
    private final JPasswordField passwordField;
    private JCheckBox bancoPanel;
    private JCheckBox bancoPanel1;
    private final List<Banco> bancos = new ArrayList<Banco>();
    private List<JCheckBox> checkBoxesDia = new ArrayList<JCheckBox>();
    private List<JCheckBox> checkBoxesRango = new ArrayList<JCheckBox>();
    private final Integer LOGIN_FAILED = 18456;

    public static void main(String[] args) {
        String sSistemaOperativo = System.getProperty("os.name");
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    if (sSistemaOperativo.contains("Windows")) {
                        javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    } else {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    }
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            throw new RuntimeException(ex);
        }


        EventQueue.invokeLater(() -> {
            try {
                new Form().setVisible(true);
            } catch (Exception e) {
                LOGGER.error(e);
            }
        });
    }

    public static List<Banco> getBancos() throws IOException, SAXException, ParserConfigurationException {

        List<Banco> bancos = new ArrayList<Banco>();

        File cfgFile = new File(System.getProperty("user.dir") + File.separator + "config.xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(cfgFile);
        doc.getDocumentElement();
        NodeList bancoNodes = doc.getElementsByTagName("banco");
        for(int i=0; i<bancoNodes.getLength(); i++)
        {
            Node bancoNode = bancoNodes.item(i);
            if(bancoNode.getNodeType() == Node.ELEMENT_NODE)
            {
                Element bancoNodeElement = (Element) bancoNode;
                bancos.add(
                        new Banco(
                            bancoNodeElement.getElementsByTagName("codBanco").item(0).getTextContent(),
                            bancoNodeElement.getElementsByTagName("nameBanco").item(0).getTextContent(),
                            Boolean.valueOf(bancoNodeElement.getElementsByTagName("activo").item(0).getTextContent()),
                            Integer.valueOf(bancoNodeElement.getElementsByTagName("BoundX").item(0).getTextContent()),
                            Integer.valueOf(bancoNodeElement.getElementsByTagName("BoundY").item(0).getTextContent()),
                            Integer.valueOf(bancoNodeElement.getElementsByTagName("BoundWidth").item(0).getTextContent()),
                            Integer.valueOf(bancoNodeElement.getElementsByTagName("BoundHeight").item(0).getTextContent())
                        )
                );
            }
        }

        return bancos;
    }

    public Form() {

        try {
            this.bancos.addAll(getBancos());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        setTitle("POSTREMAE");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 460);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getSize().width) / 2, (Toolkit.getDefaultToolkit().getScreenSize().height - getSize().height) / 2);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(5, 5, 444, 155);
        tabbedPane.setFocusable(false);
        tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 14));
        contentPane.add(tabbedPane);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        tabbedPane.addTab("Dia", null, panel, null);

        UtilDateModel model = new UtilDateModel();
        model.setValue(new Fecha().restarDias(1).getFecha());
        model.setSelected(true);
        Properties p = new Properties();
        p.put("text.today", "Hoy");
        p.put("text.month", "Mes");
        p.put("text.year", "Año");

        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);

        // TAB DIA
        boolean selected = false;

        if(bancos != null) {
            JLabel lblBanco = new JLabel("Banco:");
            lblBanco.setBounds(190, 20, 100, 23);
            lblBanco.setFont(new Font("Tahoma", Font.BOLD, 14));
            panel.setLayout(null);
            panel.add(lblBanco);

            for(Banco banco: bancos) {
                selected = (banco.getActivo()== true)? true : false;
                bancoPanel = new JCheckBox(banco.getCodBanco().concat(" ").concat(banco.getNameBanco()), selected);
                bancoPanel.setEnabled(banco.getActivo());
                bancoPanel.setFocusable(false);
                bancoPanel.setBounds(banco.getBoundX(),banco.getBoundY(),banco.getBoundWidth(),banco.getBoundHeight());
                panel.add(bancoPanel);
                this.checkBoxesDia.add(bancoPanel);
            }


        }

        JLabel lblFecha = new JLabel("Fecha:");
        lblFecha.setBounds(190, 130, 45, 23);
        lblFecha.setFont(new Font("Tahoma", Font.BOLD, 14));
        panel.setLayout(null);
        panel.add(lblFecha);

        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker.setBackground(Color.WHITE);
        datePicker.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        datePicker.setBounds(147, 170, 130, 25);
        datePicker.getJFormattedTextField().setBackground(Color.WHITE);
        panel.add(datePicker);

        // TAB RANGO
        JPanel panel_1 = new JPanel();
        panel_1.setBackground(Color.WHITE);
        tabbedPane.addTab("Rango", null, panel_1, null);
        tabbedPane.setBounds(5, 0, 440, 250);
        panel_1.setLayout(null);

        if(bancos != null) {
            JLabel lblBanco2 = new JLabel("Banco:");
            lblBanco2.setBounds(190, 20, 100, 23);
            lblBanco2.setFont(new Font("Tahoma", Font.BOLD, 14));
            panel_1.setLayout(null);
            panel_1.add(lblBanco2);

            for(Banco banco: bancos) {
                selected = (banco.getActivo()== true)? true : false;
                bancoPanel1 = new JCheckBox(banco.getCodBanco().concat(" ").concat(banco.getNameBanco()), selected);
                bancoPanel1.setFocusable(false);
                bancoPanel1.setEnabled(banco.getActivo());
                bancoPanel1.setBounds(banco.getBoundX(),banco.getBoundY(),banco.getBoundWidth(),banco.getBoundHeight());
                panel_1.add(bancoPanel1);
                this.checkBoxesRango.add(bancoPanel1);
            }
        }

        JLabel label = new JLabel("Fecha:");
        label.setFont(new Font("Tahoma", Font.BOLD, 14));
        label.setBounds(190, 130, 45, 23);
        panel_1.add(label);
        lblMensaje = new JLabel("");
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        lblMensaje.setBounds(5, 330, 429, 23);
        contentPane.add(lblMensaje);
        lblMensaje.setFont(new Font("Tahoma", Font.PLAIN, 14));

        JLabel lblDesde = new JLabel("Desde");
        lblDesde.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblDesde.setBounds(95, 150, 46, 14);
        panel_1.add(lblDesde);

        JLabel lblHsta = new JLabel("Hasta");
        lblHsta.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblHsta.setBounds(291, 150, 46, 14);
        panel_1.add(lblHsta);

        UtilDateModel model2 = new UtilDateModel();
        JDatePanelImpl datePanel2 = new JDatePanelImpl(model2, p);
        datePicker2 = new JDatePickerImpl(datePanel2, new DateLabelFormatter());
        datePicker2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        datePicker2.getJFormattedTextField().setBackground(Color.WHITE);
        datePicker2.setBackground(Color.WHITE);
        datePicker2.setLocation(247, 170);
        datePicker2.setSize(130, 50);
        panel_1.add(datePicker2);

        UtilDateModel model3 = new UtilDateModel();
        JDatePanelImpl datePanel3 = new JDatePanelImpl(model3, p);
        datePicker3 = new JDatePickerImpl(datePanel3, new DateLabelFormatter());
        datePicker3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        datePicker3.setBackground(Color.WHITE);
        datePicker3.getJFormattedTextField().setBackground(Color.WHITE);
        datePicker3.setLocation(51, 170);
        datePicker3.setSize(130, 50);
        panel_1.add(datePicker3);

        // ACTIONS USERNAME && PASSWORD
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setForeground(Color.WHITE);
        layeredPane.setBackground(Color.BLACK);
        layeredPane.setBounds(5, 180, 450, 160);
        contentPane.add(layeredPane);

        textField = new JTextField();
        textField.setColumns(10);
        textField.setBounds(74, 120, 120, 23);
        layeredPane.add(textField);

        passwordField = new JPasswordField();
        passwordField.setBounds(234, 120, 120, 23);
        layeredPane.add(passwordField);

        JLabel label_1 = new JLabel("Usuario");
        label_1.setBounds(74, 100, 100, 14);
        layeredPane.add(label_1);

        JLabel label_2 = new JLabel("Contrase\u00F1a");
        label_2.setBounds(234, 100, 100, 14);
        layeredPane.add(label_2);

        JButton btnNewButton = new JButton("Procesar");
        btnNewButton.setBounds(157, 360, 130, 30);

        ml = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                btnNewButton.setEnabled(false);
                btnNewButton.removeMouseListener(ml);
                executor.submit(() -> {
                    Integer tab = 0;
                    if (tabbedPane.getSelectedIndex() == 0) {
                        tab++;
                    }
                    tabbedPane.setEnabledAt(tab, false);
                    try {
                        if (tabbedPane.getSelectedIndex() == 0) {
                            proceso(false);
                        } else {
                            proceso(true);
                        }
                    } catch (SQLException se) {
                        LOGGER.error("Algun problema con la BD", se);
                        LOGGER.info("PROCESO FINALIZADO INSATISFACTORIO -----------------------------");
                        JOptionPane.showMessageDialog(null, "Ocurrio un problema de conexion, por favor revisa el log de la aplicacion", "Mensaje", JOptionPane.WARNING_MESSAGE);
                    } catch (Exception ex) {
                        LOGGER.error("Algun problema no direccionado", ex);
                        LOGGER.info("PROCESO FINALIZADO INSATISFACTORIO -----------------------------");
                        JOptionPane.showMessageDialog(null, "Ocurrio un problema", "Mensaje", JOptionPane.ERROR_MESSAGE);
                    } finally {
                        lblMensaje.setText("");
                    }
                    btnNewButton.addMouseListener(ml);
                    btnNewButton.setEnabled(true);
                    tabbedPane.setEnabledAt(tab, true);
                });
            }
        };
        btnNewButton.addMouseListener(ml);

        contentPane.add(btnNewButton);
        btnNewButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void proceso(boolean choose) throws Exception {

        Fecha fechaInicial, fechaFinal;
        List<Archivo> archivos = new ArrayList<>();
        Proceso p;
        String[][] registrosLiquidacion, registrosDetalles;
        PreparedStatement sentencia;

        List<String[][]> reversosLista = new ArrayList<>();
        Numero numero = new Numero();
        DAOTransacciones tran = new DAOTransacciones();
        JAXBContext jcontext;
        Unmarshaller unmarshaller;
        Configuracion cfg;

        File cfgFile = new File(System.getProperty("user.dir") + File.separator + "config.xml");
        /**
         * obtener lista de organismos
         */
        jcontext = JAXBContext.newInstance(Configuracion.class);
        unmarshaller = jcontext.createUnmarshaller();
        cfg = (Configuracion) unmarshaller.unmarshal(cfgFile);

        List<Banco> bancoChecked = new ArrayList<Banco>();
        if (choose == false) {
            List<JCheckBox> checkBoxesActivated = this.checkBoxesDia.stream().filter(e -> e.isSelected() ).collect(Collectors.toList());

            for(Banco banco: this.bancos) {
                banco.setChecked(false);
                if(checkBoxesActivated.stream().anyMatch(e -> e.getText().equalsIgnoreCase(banco.getCodBanco().concat(" ").concat(banco.getNameBanco())))) {
                    banco.setChecked(true);
                    bancoChecked.add(banco);
                }
            }

            if(bancoChecked.size() == 0) {
                JOptionPane.showMessageDialog(this, "Seleccione algún banco", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            fechaInicial = new Fecha((Date) datePicker.getModel().getValue());
            fechaFinal = new Fecha((Date) datePicker.getModel().getValue());

            if (fechaInicial.getDayOfWeek() == 1) {
                fechaInicial.restarDias(2);
            }
        }
        else {

            List<JCheckBox> checkBoxesActivated = this.checkBoxesRango.stream().filter(e -> e.isSelected() ).collect(Collectors.toList());

            for(Banco banco: this.bancos) {
                banco.setChecked(false);
                if(checkBoxesActivated.stream().anyMatch(e -> e.getText().equalsIgnoreCase(banco.getCodBanco().concat(" ").concat(banco.getNameBanco())))) {
                    banco.setChecked(true);
                    bancoChecked.add(banco);
                }
            }

            if(bancoChecked.size() == 0) {
                JOptionPane.showMessageDialog(this, "Seleccione algún banco", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if ((datePicker2.getModel().getValue() == null && datePicker3.getModel().getValue() == null) || (datePicker2.getModel().getValue() == null || datePicker3.getModel().getValue() == null)) {
                JOptionPane.showMessageDialog(this, "Seleccione las fechas", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                return;
            } else if (((Date) datePicker2.getModel().getValue()).before((Date) datePicker3.getModel().getValue())) {
                JOptionPane.showMessageDialog(this, "La fecha 'Desde' no debe ser mayor a la fecha 'Hasta'", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            fechaInicial = new Fecha((Date) datePicker3.getModel().getValue());
            fechaFinal = new Fecha((Date) datePicker2.getModel().getValue());
        }


        if (textField.getText().isEmpty() || (new String(passwordField.getPassword())).isEmpty()) {
            JOptionPane.showMessageDialog(this, "Esciba sus credenciales de acceso a la BD", "Mensaje", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            tran.setDriver("net.sourceforge.jtds.jdbc.Driver");
            tran.setUrl("jdbc:jtds:sqlserver://");
            tran.setNombreIPServidorBD(cfg.getIp());
            tran.setPuertoServidorBD(cfg.getPort());
            tran.setNombreBD(cfg.getDbname());
            tran.setPropiedades(";charset=ISO-8859-1;appName=fcpd060-720");

            tran.setUsuarioBD(textField.getText());
            tran.setPasswordUsuarioBD(new String(passwordField.getPassword()));

            LOGGER.info("PROCESO INICIADO -----------------------------------------------");
            lblMensaje.setText("PROCESO INICIADO");

            for(Banco banco : bancoChecked) {

                // Ejecutar Consulta
                tran.conectar();
                sentencia = tran.crearSentencia("EXEC sp_liq_postremae @cod_banco = ?, @fecha_inicio = ?, @fecha_fin = ?, @lote = ?");
                sentencia.setString(1, banco.getCodBanco());
                sentencia.setString(2, fechaInicial.getString("yyyy-MM-dd") + " 00:00:00.001");
                sentencia.setString(3, fechaFinal.getString("yyyy-MM-dd") + " 23:59:59.999");
                sentencia.setString(4, null);
                registrosLiquidacion = tran.getTransacciones(sentencia);
                tran.getConexion().commit();
                sentencia.close();
                tran.desconectar();


                if (registrosLiquidacion == null) {
                    JOptionPane.showMessageDialog(this, banco.getNameBanco() + " : " + "Sin informacion para procesar", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
//                return;
                }else{

                    tran.conectar();
                    sentencia = tran.crearSentencia("EXEC SP_REPORTES_POSTREMAE @cod_banco = ?, @fecha_inicio = ?, @fecha_fin = ?");
                    sentencia.setString(1, banco.getCodBanco());
                    sentencia.setString(2, fechaInicial.getString("yyyy-MM-dd") + " 00:00:00.001");
                    sentencia.setString(3, fechaFinal.getString("yyyy-MM-dd") + " 23:59:59.999");
                    registrosDetalles = tran.getTransacciones(sentencia);
                    tran.getConexion().commit();
                    sentencia.close();
                    tran.desconectar();


                    Date today = new Date();
                    SimpleDateFormat todayFormat = new SimpleDateFormat("YYYYMMdd");
                    SimpleDateFormat todayFormatFolder = new SimpleDateFormat("MMM.dd");
                    String DatePOSTREMAE = todayFormat.format(today);
                    String DatePOSTREMAEFolder = todayFormatFolder.format(today);

                    List<String> lotes = new ArrayList<>();
                    Integer maxWidth = 5;
                    for (Integer i = 0; i < registrosLiquidacion.length; i++) {
                        String lote = registrosLiquidacion[i][2];
                        if(!lotes.stream().anyMatch(l -> l.equals(lote))) {
                            lotes.add(lote);
                            String formattedLote = String.format("%0" + maxWidth + "d", Integer.valueOf(lote));
                            Archivo archivo = new Archivo(new File(System.getProperty("user.dir") + File.separator + "POSTREMAE" + DatePOSTREMAE + formattedLote + ".txt"));
                            p = new Proceso(registrosLiquidacion, archivo, lote);
                            p.iniciar();

                            archivo.copyFile(cfg.getRutaX() + File.separator + banco.getCodBanco() + File.separator + DatePOSTREMAEFolder);
                            archivo.getArchivo().delete();
                        }
                    }

                    Archivo archivo = new Archivo(new File(System.getProperty("user.dir") + File.separator + "REP_POSTREMAE_RES_" + DatePOSTREMAE + ".txt"));
                    p = new Proceso(registrosDetalles, archivo, "RESUMEN");
                    p.iniciar();
                    archivo.copyFile(cfg.getRutaX() + File.separator + banco.getCodBanco() + File.separator + DatePOSTREMAEFolder);
                    archivo.getArchivo().delete();

                    archivo = new Archivo(new File(System.getProperty("user.dir") + File.separator + "REP_POSTREMAE_DET_" + DatePOSTREMAE + ".txt"));
                    p = new Proceso(registrosDetalles, archivo, "DETALLE");
                    p.iniciar();
                    lblMensaje.setText("Copiando archivos a sus destinos");
                    archivo.copyFile(cfg.getRutaX() + File.separator + banco.getCodBanco() + File.separator + DatePOSTREMAEFolder);
                    archivo.getArchivo().delete();

                }

            }

            LOGGER.info("PROCESO FINALIZADO ---------------------------------------------");
            lblMensaje.setText("LISTO!!!");

            JOptionPane.showMessageDialog(this, "Se generaron los archivos correctamente", "Mensaje", JOptionPane.INFORMATION_MESSAGE);

            lblMensaje.setText("");
        }catch (SQLException e) {
            if(e.getErrorCode() == LOGIN_FAILED) {
                JOptionPane.showMessageDialog(this, "El usuario y/o contraseña es inválido", "Mensaje", JOptionPane.ERROR_MESSAGE);
            }
        }



    }

}
