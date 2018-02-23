/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidormensajeria;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author andres
 */
public class GUI extends JFrame implements ActionListener {

    private ServerProject sp;
    private JTextField inPort, outPortOutServer1,
            outPortOutServer2, outHostOutServer1, outHostOutServer2;
    private JButton start, stop, stopOutServer1,
            stopOutServer2, startOutServer1, startOutServer2;
    private JLabel tituloConfig, lEste, lExterno1, lExterno2, lPort, lHost, estadoEste, estadoServer1, estadoServer2;

    private JTextPane registro;
    private StyledDocument doc;
    private DefaultTableModel tOnline;
    private SimpleAttributeSet me;
    private DateFormat dateFormat;

    private ImageIcon iconConectado, iconDesconectado;
    private Estadisticas estadisticas;

    public GUI(ServerProject sp, Estadisticas est) {

        this.sp = sp;
        this.estadisticas = est;
        init();
    }

    private void init() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.add(getTabs(), BorderLayout.CENTER);
        this.setSize(1500, 600);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        this.setResizable(false);
        this.setVisible(true);
    }

    //Genera las pestañas de navegación.
    private JTabbedPane getTabs() {

        JLabel lEstado = new JLabel("Estado");
        lEstado.setPreferredSize(new Dimension(200, 50));
        lEstado.setHorizontalTextPosition(SwingConstants.RIGHT);
        lEstado.setFont(lEstado.getFont().deriveFont(25f));

        JLabel lEstadisticas = new JLabel("Estadísticas");
        lEstadisticas.setPreferredSize(new Dimension(200, 50));
        lEstadisticas.setHorizontalTextPosition(SwingConstants.RIGHT);
        lEstadisticas.setFont(lEstado.getFont().deriveFont(25f));

        JLabel lRegistro = new JLabel("Registro");
        lRegistro.setPreferredSize(new Dimension(200, 50));
        lRegistro.setHorizontalTextPosition(SwingConstants.RIGHT);
        lRegistro.setFont(lEstado.getFont().deriveFont(25f));

        UIManager.put("TabbedPane.selected", Color.decode("#E0F2F1"));
        UIManager.put("TabbedPane.borderHightlightColor", java.awt.Color.CYAN);
        UIManager.put("TabbedPane.darkShadow", java.awt.Color.CYAN);
        UIManager.put("TabbedPane.focus", Color.decode("#E0F2F1"));

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);

        tabbedPane.addTab(null, getEstadoPanel());
        tabbedPane.addTab(null, getPanelEstadisticas());
        tabbedPane.addTab(null, initRegistro());

        tabbedPane.setTabComponentAt(0, lEstado);
        tabbedPane.setBackgroundAt(0, Color.decode("#80CBC4"));
        tabbedPane.setTabComponentAt(1, lEstadisticas);
        tabbedPane.setBackgroundAt(1, Color.decode("#80CBC4"));
        tabbedPane.setTabComponentAt(2, lRegistro);
        tabbedPane.setBackgroundAt(2, Color.decode("#80CBC4"));

        return tabbedPane;
    }

    private JPanel getEstadoPanel() {
        JPanel estado = new JPanel();
        estado.setBackground(Color.decode("#009688"));
        estado.setLayout(new GridBagLayout());

        Font fuenteTexto = new Font("HELVETICA", Font.BOLD, 20);

        initEstadoComponents(fuenteTexto);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1; //Esto es como ser redimensiona 
        c.weightx = 1;

        //Titulo
        c.weighty = 0.05;
        c.insets = new Insets(10, 0, 10, 0);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 6;
        c.gridheight = 1;
        estado.add(tituloConfig, c);

        //LABELS 
        c.weighty = 0.01;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        estado.add(lEste, c);
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        c.gridheight = 1;
        estado.add(lExterno1, c);
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        estado.add(lExterno2, c);
        c.insets = new Insets(10, 5, 10, 5);
        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        estado.add(lPort, c);
        c.insets = new Insets(10, 5, 10, 5);
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        estado.add(lHost, c);

        //TextFields HOST
        c.insets = new Insets(10, 5, 10, 5);
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 1;
        c.gridheight = 1;
        estado.add(this.outHostOutServer1, c);
        c.gridx = 1;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        estado.add(this.outHostOutServer2, c);

        //TextFields PORT
        c.gridx = 2;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        estado.add(this.inPort, c);
        c.gridx = 2;
        c.gridy = 3;
        c.gridwidth = 1;
        c.gridheight = 1;
        estado.add(this.outPortOutServer1, c);
        c.gridx = 2;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        estado.add(this.outPortOutServer2, c);

        //Estado
        c.insets = new Insets(10, 30, 10, 0);
        c.gridx = 3;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        estado.add(this.estadoEste, c
        );
        c.insets = new Insets(10, 30, 10, 0);
        c.gridx = 3;
        c.gridy = 3;
        c.gridwidth = 1;
        c.gridheight = 1;
        estado.add(this.estadoServer1, c);

        c.insets = new Insets(10, 30, 10, 0);
        c.gridx = 3;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        estado.add(this.estadoServer2, c);

        //Botoenes
        c.insets = new Insets(10, 30, 10, 0);
        c.gridx = 4;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        estado.add(this.start, c);
        c.insets = new Insets(10, 30, 10, 0);
        c.gridx = 4;
        c.gridy = 3;
        c.gridwidth = 1;
        c.gridheight = 1;
        estado.add(this.startOutServer1, c);
        c.insets = new Insets(10, 30, 10, 0);
        c.gridx = 4;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        estado.add(this.startOutServer2, c);

        c.insets = new Insets(10, 10, 10, 15);
        c.gridx = 5;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        estado.add(this.stop, c);
        c.insets = new Insets(10, 10, 10, 15);
        c.gridx = 5;
        c.gridy = 3;
        c.gridwidth = 1;
        c.gridheight = 1;
        estado.add(this.stopOutServer1, c);
        c.insets = new Insets(10, 10, 10, 15);
        c.gridx = 5;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        estado.add(this.stopOutServer2, c);

        return estado;

    }

    private void initEstadoComponents(Font fontLabel) {
        try {

            Font fuenteTitulo = new Font("HELVETICA", Font.BOLD, 25);

            this.inPort = new JTextField();
            this.inPort.setFont(fontLabel);
            this.inPort.setToolTipText("Puerto local");

            this.tituloConfig = new JLabel("Estado del servidor");
            this.tituloConfig.setFont(fuenteTitulo);
            this.tituloConfig.setHorizontalAlignment(0);

            this.lEste = new JLabel("Este servidor");
            this.lEste.setFont(fontLabel);
            this.lEste.setHorizontalAlignment(0);

            this.lExterno1 = new JLabel("Servidor externo 1");
            this.lExterno1.setFont(fontLabel);
            this.lExterno1.setHorizontalAlignment(0);

            this.lExterno2 = new JLabel("Servidor externo 2");
            this.lExterno2.setFont(fontLabel);
            this.lExterno2.setHorizontalAlignment(0);

            this.lPort = new JLabel("Puerto");
            this.lPort.setFont(fontLabel);
            this.lPort.setHorizontalAlignment(0);

            this.lHost = new JLabel("Host");
            this.lHost.setFont(fontLabel);
            this.lHost.setHorizontalAlignment(0);

            BufferedImage imageConectado = ImageIO.read(new File("img/conectado.png"));
            this.iconConectado = new ImageIcon((Image) imageConectado);

            BufferedImage imageDesconectado = ImageIO.read(new File("img/desconectado.png"));
            this.iconDesconectado = new ImageIcon((Image) imageDesconectado);

            this.estadoEste = new JLabel();
            this.estadoEste.setIcon(this.iconDesconectado);

            this.estadoServer1 = new JLabel();
            this.estadoServer1.setIcon(this.iconDesconectado);

            this.estadoServer2 = new JLabel();
            this.estadoServer2.setIcon(this.iconDesconectado);

            this.outPortOutServer1 = new JTextField();
            this.outPortOutServer1.setFont(fontLabel);
            this.outPortOutServer1.setToolTipText("Puerto del servidor 1");
            this.outPortOutServer1.setEditable(false);

            this.outPortOutServer2 = new JTextField();
            this.outPortOutServer2.setFont(fontLabel);
            this.outPortOutServer2.setToolTipText("Puerto del servidor 2");
            this.outPortOutServer2.setEditable(false);

            this.outHostOutServer1 = new JTextField();
            this.outHostOutServer1.setFont(fontLabel);
            this.outHostOutServer1.setToolTipText("Host del servidor 1");
            this.outHostOutServer1.setEditable(false);

            this.outHostOutServer2 = new JTextField();
            this.outHostOutServer2.setFont(fontLabel);
            this.outHostOutServer2.setToolTipText("Host del servidor 2");
            this.outHostOutServer2.setEditable(false);

            this.start = new JButton("Start");
            this.start.setFont(fontLabel);
            this.start.addActionListener(this);
            this.start.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            this.stop = new JButton("Stop");
            this.stop.setFont(fontLabel);
            this.stop.addActionListener(this);
            this.stop.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            this.stop.setEnabled(false);

            this.startOutServer1 = new JButton("Start");
            this.startOutServer1.setFont(fontLabel);
            this.startOutServer1.addActionListener(this);
            this.startOutServer1.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            this.startOutServer1.setEnabled(false);

            this.stopOutServer1 = new JButton("Stop");
            this.stopOutServer1.setFont(fontLabel);
            this.stopOutServer1.addActionListener(this);
            this.stopOutServer1.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            this.stopOutServer1.setEnabled(false);

            this.startOutServer2 = new JButton("Start");
            this.startOutServer2.setFont(fontLabel);
            this.startOutServer2.addActionListener(this);
            this.startOutServer2.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            this.startOutServer2.setEnabled(false);

            this.stopOutServer2 = new JButton("Stop");
            this.stopOutServer2.setFont(fontLabel);
            this.stopOutServer2.addActionListener(this);
            this.stopOutServer2.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            this.stopOutServer2.setEnabled(false);

        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private JPanel initRegistro() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JPanel panelRegistro = new JPanel(new GridBagLayout());
        JPanel panelOnline = new JPanel(new GridBagLayout());

        this.me = new SimpleAttributeSet();
        StyleConstants.setForeground(this.me, Color.BLUE);

        this.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        this.registro = new JTextPane();
        Font fontChat = new Font("HELVETICA", Font.BOLD, 20);
        this.registro.setFont(fontChat);
        DefaultCaret caret = (DefaultCaret) this.registro.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        this.registro.setEditable(false);

        this.doc = this.registro.getStyledDocument();

        JScrollPane pane = new JScrollPane(this.registro,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setPreferredSize(new Dimension(950, 500));

        this.tOnline = new DefaultTableModel();
        this.tOnline.addColumn("Nickname", this.sp.getNicknames().toArray());

        JTable tableOnline = new JTable(this.tOnline);

        tableOnline.setEnabled(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        TableColumn column;
        for (int i = 0; i < this.tOnline.getColumnCount(); i++) {
            column = tableOnline.getColumnModel().getColumn(i);
            column.setPreferredWidth(200);
            column.setCellRenderer(centerRenderer);
        }
        tableOnline.setRowHeight(40);

        JScrollPane paneOnline = new JScrollPane(tableOnline,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        paneOnline.setPreferredSize(new Dimension(300, 500));

        GridBagConstraints gcReg = new GridBagConstraints();

        gcReg.fill = GridBagConstraints.HORIZONTAL;
        gcReg.gridx = 0;
        gcReg.gridy = 0;
        panelRegistro.add(pane, gcReg);

        GridBagConstraints gcOnline = new GridBagConstraints();
        gcOnline.fill = GridBagConstraints.HORIZONTAL;
        gcOnline.gridx = 0;
        gcOnline.gridy = 1;
        panelOnline.add(paneOnline, gcOnline);

        panel.add(panelRegistro, BorderLayout.CENTER);
        panel.add(panelOnline, BorderLayout.EAST);

        return panel;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object boton = e.getSource();
        if (boton.equals(this.start)) {
            if (checkPort(this.inPort)&&this.sp.availablePort(Integer.parseInt(this.inPort.getText()))) {
                sp.startSM(Integer.parseInt(this.inPort.getText()));
                this.inPort.setEditable(false);
                this.start.setEnabled(false);
                this.stop.setEnabled(true);
                this.estadoEste.setIcon(this.iconConectado);
                enableOptionOut();
                this.setTitle("Server Port: " + Integer.parseInt(this.inPort.getText()));
            }
        } else if (boton.equals(this.stop)) {
            sp.stopThisServer();
            this.inPort.setEditable(true);
            this.start.setEnabled(true);
            this.stop.setEnabled(false);
            this.estadoEste.setIcon(this.iconDesconectado);
            this.estadoServer1.setIcon(this.iconDesconectado);
            this.estadoServer2.setIcon(this.iconDesconectado);
            disableOptionOut();
            this.setTitle("Server");
        } else if (boton.equals(this.startOutServer1)) {
            if (checkPort(this.outPortOutServer1) && checkHost(this.outHostOutServer1)) {
                sp.startOutServer1(Integer.parseInt(this.outPortOutServer1.getText()),
                        this.outHostOutServer1.getText());
                this.outPortOutServer1.setEditable(false);
                this.outHostOutServer1.setEditable(false);
                this.startOutServer1.setEnabled(false);
                this.stopOutServer1.setEnabled(true);
            }
        } else if (boton.equals(this.stopOutServer1)) {
            sp.stopOutServer1("this");
            this.outPortOutServer1.setEditable(true);
            this.outHostOutServer1.setEditable(true);
            this.startOutServer1.setEnabled(true);
            this.stopOutServer1.setEnabled(false);
            this.estadoServer1.setIcon(this.iconDesconectado);

        } else if (boton.equals(this.startOutServer2)) {
            if (checkPort(this.outPortOutServer2) && checkHost(this.outHostOutServer2)) {
                sp.startOutServer2(Integer.parseInt(this.outPortOutServer2.getText()),
                        this.outHostOutServer2.getText());
                this.outPortOutServer2.setEditable(false);
                this.outHostOutServer2.setEditable(false);
                this.startOutServer2.setEnabled(false);
                this.stopOutServer2.setEnabled(true);
            }

        } else if (boton.equals(this.stopOutServer2)) {
            sp.stopOutServer2("this");
            this.outPortOutServer2.setEditable(true);
            this.outHostOutServer2.setEditable(true);
            this.startOutServer2.setEnabled(true);
            this.stopOutServer2.setEnabled(false);
            this.estadoServer2.setIcon(this.iconDesconectado);

        }
    }
    //Comprueba que el puerto sea un Integer y que cumpla una
    // longitud determinada.

    private boolean checkPort(JTextField port) {
        int i;
        try {
            i = Integer.parseInt(port.getText());
            return port.getText().length() > 0 && port.getText().length() < 6;
        } catch (NumberFormatException e) {
            port.requestFocus();
            JOptionPane.showMessageDialog(null, "Puerto no valido o vacío.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

    }

    //Comprueba que el host tenga una longitud determinada.
    private boolean checkHost(JTextField host) {
        if (host.getText().length() > 0) {
            return true;
        } else {
            host.requestFocus();
            JOptionPane.showMessageDialog(null, "Host no valido o vacío.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    //Crea el JPanel de las estadisticas
    private JPanel getPanelEstadisticas() {
        JPanel pEstadisticas = new JPanel();
        pEstadisticas.setLayout(new BorderLayout());
        pEstadisticas.add(this.estadisticas.getTableHeader(), BorderLayout.NORTH);
        pEstadisticas.add(this.estadisticas, BorderLayout.CENTER);
        return pEstadisticas;

    }

    //Habilita la botonera y TextFilds de los servidores externos.
    private void enableOptionOut() {
        this.outPortOutServer1.setEditable(true);
        this.outPortOutServer2.setEditable(true);
        this.outHostOutServer1.setEditable(true);
        this.outHostOutServer2.setEditable(true);
        this.startOutServer1.setEnabled(true);
        this.startOutServer2.setEnabled(true);

    }

    //Deshabilita la botonera y TextFilds de los servidores externos.
    private void disableOptionOut() {
        this.outPortOutServer1.setEditable(false);
        this.outPortOutServer1.setText("");
        this.outPortOutServer2.setEditable(false);
        this.outPortOutServer2.setText("");
        this.outHostOutServer1.setEditable(false);
        this.outHostOutServer1.setText("");
        this.outHostOutServer2.setEditable(false);
        this.outHostOutServer2.setText("");
        this.startOutServer1.setEnabled(false);
        this.startOutServer2.setEnabled(false);
        this.stopOutServer1.setEnabled(false);
        this.stopOutServer2.setEnabled(false);

    }

    //Setea el estado del servidor1 a conectado.
    public void outServer1Conectado(int port, String host) {
        this.estadoServer1.setIcon(this.iconConectado);
        this.outPortOutServer1.setEditable(false);
        this.outPortOutServer1.setText(Integer.toString(port));
        this.outHostOutServer1.setEditable(false);
        this.outHostOutServer1.setText(host);
        this.startOutServer1.setEnabled(false);
        this.stopOutServer1.setEnabled(true);
    }

    //Setea el estado del servidor1 a conectado.
    public void outServer2Conectado(int port, String host) {
        this.estadoServer2.setIcon(this.iconConectado);
        this.outPortOutServer2.setEditable(false);
        this.outPortOutServer2.setText(Integer.toString(port));
        this.outHostOutServer2.setEditable(false);
        this.outHostOutServer2.setText(host);
        this.startOutServer2.setEnabled(false);
        this.stopOutServer2.setEnabled(true);
    }

    //Cambiar el estado a parado el OutServer1
    public void cambiarEstadoAParadoS1() {
        this.outPortOutServer1.setEditable(true);
        this.outHostOutServer1.setEditable(true);
        this.startOutServer1.setEnabled(true);
        this.stopOutServer1.setEnabled(false);
        this.estadoServer1.setIcon(this.iconDesconectado);
    }

    //Cambia el icono de estado a parado del servidor1
    public void conexionPerdidaS1() {
        this.estadoServer1.setIcon(this.iconDesconectado);
    }

    //Cambiar el estado a parado el OutServer2
    public void cambiarEstadoAParadoS2() {
        this.outPortOutServer2.setEditable(true);
        this.outHostOutServer2.setEditable(true);
        this.startOutServer2.setEnabled(true);
        this.stopOutServer2.setEnabled(false);
        this.estadoServer2.setIcon(this.iconDesconectado);
    }

    //Cambia el icono de estado a parado del servidor2
    public void conexionPerdidaS2() {
        this.estadoServer2.setIcon(this.iconDesconectado);
    }

    //Añade una linea al chat.
    public void addMsg(String line) {
        try {
            Date date = new Date();
            String msg = dateFormat.format(date) + line + "\n";
            this.doc.insertString(this.doc.getLength(), msg, this.me);

        } catch (BadLocationException exc) {
        }
    }

    public void refresh() {
        this.tOnline.setRowCount(0);
        Object rowData[] = new Object[1];
        for (String s : this.sp.getNicknames()) {
            rowData[0] = s;
            this.tOnline.addRow(rowData);
        }
        this.tOnline.fireTableDataChanged();
    }

}
