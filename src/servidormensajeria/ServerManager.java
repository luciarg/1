/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidormensajeria;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andres
 */
public class ServerManager extends Thread {

    private ServerProject serverProject;
    private int port;
    private boolean state;
    private ServerSocket ss;

    public ServerManager(ServerProject serverProject, int port) {
        this.serverProject = serverProject;
        this.port = port;
        this.state = true;
    }

    //Escucha la posibles conexiones.
    @Override
    public void run() {
        try {
            ss = new ServerSocket(port);
            while (state) {
                System.out.println("Waiting for a client...");
                Socket s = ss.accept();
                System.out.println("Client connection from "
                        + s.getInetAddress().getHostAddress());
                checkIfIsClientOrServer(s);
            }
        } catch (IOException ex) {
            System.out.println("Servidor parado.");
        }
    }

    //Comprueba si el elemento es un servidor o un cliente y lo añade.
    private void checkIfIsClientOrServer(Socket s) {
        try {
            if (state) {
                Scanner scanner = new Scanner(new InputStreamReader(s.getInputStream()));
                if (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    System.out.println("Processing type of item to connect...");
                    switch (line.trim().toUpperCase().substring(0, 7)) {
                        case "$SERVER":
                            String aux = line.trim().toUpperCase().substring(8, line.length());
                            String port = aux.substring(0, aux.indexOf("$"));
                            String host = aux.substring(aux.indexOf("$") + 1, aux.indexOf("$") + 1 + aux.substring(aux.indexOf("$") + 1, aux.length()).indexOf("$"));
                            if (!serverProject.onAllServers()
                                    && !serverProject.checkIfExistServer(Integer.parseInt(port), host)) {

                                serverProject.addServer(s, Integer.parseInt(port), host);
                            } else {
                                s.close();
                            }
                            break;
                        case "$CLIENT":
                            String nick = line.trim().substring(8, line.length() - 1);
                            if (this.serverProject.getNicknames().contains(nick)) {
                                PrintWriter out = out = new PrintWriter(s.getOutputStream(), true);
                                out.println("$CANCEL$");
                            } else {
                                this.serverProject.addNickname(nick);
                                this.serverProject.broadcastAddNickname(this.serverProject.addClient(s, nick), nick);
                            }

                            break;
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setState(boolean state) {
        try {
            this.state = state;
            this.ss.close();
        } catch (IOException ex) {

        }
    }

}
