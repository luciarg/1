/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidormensajeria;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author andres
 */
public class Estadisticas extends JTable implements Runnable {

    private ServerProject sp;
    private DefaultTableModel modelo;
    private String[] header = {"Description", "Value"};
    private String[][] datos = {
        {"Mis clientes actuales", ""},
        {"Servidores conectados actuales", ""},
        {"Historico de mis Clientes", ""},
        {"Historico Servidores", ""},
        {"Clientes totales", ""},
        {"Mensajes totales", ""},
        {"Mis mensajes", ""},
        {"Mensajes servidor 1", ""},
        {"Mensajes servidor 2", ""}};

    public Estadisticas(ServerProject sp) {
        this.sp = sp;
        initTable();
        refresh();
    }

    @Override
    public void run() {
        while (true) {
            refresh();

            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Estadisticas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void refresh() {
        modelo.setValueAt(sp.getMyClients(), 0, 1);
        modelo.setValueAt(sp.getServersConnected(), 1, 1);
        modelo.setValueAt(sp.getHistoricoMyClients(), 2, 1);
        modelo.setValueAt(sp.getHistoricoServersConnected(), 3, 1);
        modelo.setValueAt(sp.getNicknames().size(), 4, 1);
        modelo.setValueAt(sp.getTotalMessages(), 5, 1);
        modelo.setValueAt(sp.getMyMessages(), 6, 1);
        modelo.setValueAt(sp.getServer1Messages(), 7, 1);
        modelo.setValueAt(sp.getServer2Messages(), 8, 1);
    }

    private void initTable() {
        modelo = new DefaultTableModel(datos, header) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.setModel(modelo);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        TableColumn column;
        for (int i = 0; i < this.getColumnCount(); i++) {
            column = this.getColumnModel().getColumn(i);
            column.setPreferredWidth(200);
            column.setCellRenderer(centerRenderer);
        }
        this.setRowHeight(50);

    }

}
