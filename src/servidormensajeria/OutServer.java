/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidormensajeria;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andres
 */
public class OutServer extends Thread {

    private ServerProject sp;
    private int port;
    private String ip;
    private boolean state;

    public OutServer(ServerProject sp, int port, String ip) {
        this.state = true;
        this.sp = sp;
        this.port = port;
        this.ip = ip;
    }

    //Intenta establecer una conexion si no tenemos un server y añadirlo.
    @Override
    public void run() {
        while (state) {
            try {
                Thread.sleep(1000);
                if (!sp.onAllServers() && !sp.checkIfExistServer(this.port, this.ip) && state) {
                    try {

                        Socket sock = new Socket(ip, port);
                        PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
                        out.println("$SERVER$" + sp.getPortSM() + "$" + sock.getInetAddress().getHostAddress() + "$");
                         Thread.sleep(1000);
                        this.sp.addServer(sock, port, ip);
                        

                    } catch (IOException ex) {
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(OutServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnknownHostException ex) {
                Logger.getLogger(OutServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setState(boolean state) {
        this.state = state;
    }

    //Devuelve los parametros pasados son iguales que los de este.
    public boolean compareParameters(int port, String host) {
        if (this.port == port && this.ip.equals(host)) {
            return true;
        } else {
            return false;
        }
    }

}
