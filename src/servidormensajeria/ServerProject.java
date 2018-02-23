/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidormensajeria;

import clientServer.Client;
import clientServer.Server;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author andres
 */
public class ServerProject {

    private ServerManager sm;
    private List<Client> clients;
    private Server server1, server2;
    private OutServer os1, os2;
    private GUI gui;
    private Estadisticas estadisticas;
    private int portSM, myClients, serversConnected, historicoMyClients,
            historicoServersConnected, totalMessages,
            myMessages, server1Messages, server2Messages;
    private ArrayList<String> nicknames;

    public ServerProject() {
        clients = new ArrayList<>();
        this.nicknames = new ArrayList<>();
        estadisticas = new Estadisticas(this);
        new Thread(estadisticas).start();
        gui = new GUI(this, estadisticas);
    }

    //Metodo para añadir un nuevo Cliente al array e iniciando su hilo.
    public synchronized Client addClient(Socket s, String nick) {
        Client client = new Client(s, this, nick);
        clients.add(client);
        new Thread(client).start();
        this.gui.addMsg(" Client connected " + s.getInetAddress().toString() + " " + s.getPort());
        this.myClients++;
        this.historicoMyClients++;
        return client;
    }

    //Metodo para añadir un nuevo Servidor e iniciando su hilo.
    public void addServer(Socket s, int port, String host) {

        if (getOutServerByParameters(port, host) != null) {
            if (this.os1 != null) {
                if (getOutServerByParameters(port, host).equals(this.os1)) {
                    if (this.server1 == null) {
                        this.server1 = new Server(s, this, port);
                        new Thread(server1).start();
                        this.gui.addMsg(" Server 1 connected " + s.getInetAddress().toString() + " " + s.getPort());
                        this.gui.outServer1Conectado(port, host);

                    }
                }
            }
            if (os2 != null) {
                if (getOutServerByParameters(port, host).equals(os2)) {
                    if (server2 == null) {
                        server2 = new Server(s, this, port);
                        new Thread(server2).start();
                        this.gui.addMsg(" Server 2 connected " + s.getInetAddress().toString() + " " + s.getPort());
                        this.gui.outServer2Conectado(port, host);
                    }
                }
            }

        } else {
            if (this.server1 == null) {
                this.server1 = new Server(s, this, port);
                new Thread(server1).start();
                this.gui.addMsg(" Server 1 connected " + s.getInetAddress().toString() + " " + s.getPort());
                if (this.os1 == null) {
                    startOutServer1(port, host);
                } else {
                    stopOutServer1("this");
                    startOutServer1(port, host);
                }
                this.gui.outServer1Conectado(port, host);

            } else if (server2 == null) {
                server2 = new Server(s, this, port);
                new Thread(server2).start();
                this.gui.addMsg(" Server 2 connected " + s.getInetAddress().toString() + " " + s.getPort());
                if (this.os2 == null) {
                    startOutServer2(port, host);
                } else {
                    stopOutServer2("this");
                    startOutServer2(port, host);
                }
                this.gui.outServer2Conectado(port, host);
            }
        }
        this.serversConnected++;
        this.historicoServersConnected++;
    }
//Devuelve si el puerto esta en uso

    public boolean availablePort(int port) {
        boolean result = true;
        try {
            (new ServerSocket(port)).close();
            
        } catch (IOException e) {
             JOptionPane.showMessageDialog(null, "Puerto en uso.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            result = false;
        }

        return result;
    }

    //Reenvio del mensaje a todos los clientes y servidores excepto el mismo.
    public synchronized void broadcastMsg(Client c, String msg) {

        for (Client one : clients) {
            if (!one.equals(c)) {
                one.txMsg("$MENSAJ$" + msg);
            }
        }
        if (server1 != null) {
            if (!server1.equals(c)) {
                server1.txMsg("$MENSAJ$" + msg);
            } else {
                this.server1Messages++;
            }
        }
        if (server2 != null) {
            if (!server2.equals(c)) {
                server2.txMsg("$MENSAJ$" + msg);
            } else {
                this.server2Messages++;
            }
        }
        this.totalMessages++;
    }

    //Reenvio nicknames a todos los clientes y servidores excepto el mismo.
    public synchronized void broadcastAddNicknames(Client c, ArrayList<String> nicks) {
        for (Client one : clients) {
            if (!one.equals(c)) {
                one.addNicknames(nicks);
            }
        }
        if (server1 != null) {
            if (!server1.equals(c)) {
                server1.addNicknames(nicks);
            }
        }
        if (server2 != null) {
            if (!server2.equals(c)) {
                server2.addNicknames(nicks);
            }
        }
    }

    //Reenvio nicknames a eliminar todos los clientes y servidores excepto el mismo.
    public synchronized void broadcastRemNicknames(Client c, ArrayList<String> nicks) {

        for (Client one : clients) {
            if (!one.equals(c)) {
                one.remNicknames(nicks);
            }
        }
        if (server1 != null) {
            if (!server1.equals(c)) {
                server1.remNicknames(nicks);
            }
        }
        if (server2 != null) {
            if (!server2.equals(c)) {
                server2.remNicknames(nicks);
            }
        }
    }

    //Reenvio nickname a todos los clientes y servidores excepto el mismo.
    public synchronized void broadcastAddNickname(Client c, String nicks) {

        for (Client one : clients) {
            if (!one.equals(c)) {
                one.addNickname(nicks);
            }
        }
        if (server1 != null) {
            if (!server1.equals(c)) {
                server1.addNickname(nicks);
            }
        }
        if (server2 != null) {
            if (!server2.equals(c)) {
                server2.addNickname(nicks);
            }
        }
    }

    //Reenvio nickname a todos los clientes y servidores excepto el mismo.
    public synchronized void broadcastRemNickname(Client c, String nicks) {

        for (Client one : clients) {
            if (!one.equals(c)) {
                one.remNickname(nicks);
            }
        }
        if (server1 != null) {
            if (!server1.equals(c)) {
                server1.remNickname(nicks);
            }
        }
        if (server2 != null) {
            if (!server2.equals(c)) {
                server2.remNickname(nicks);
            }
        }
    }

    public synchronized void clearNicknames() {
        this.nicknames.clear();
    }

    //comprueba que no exista el servidor.
    public synchronized boolean checkIfExistServer(int port, String host) throws UnknownHostException {

        if (server1 != null) {
            String hostServer1 = this.server1.getSocket().getInetAddress().getHostAddress();
            InetAddress address = InetAddress.getByName(host);
            if (hostServer1.equals(address.getHostAddress()) && this.server1.getPortOutSM() == port) {
                return true;
            }
        }
        if (server2 != null) {
            String hostServer1 = this.server2.getSocket().getInetAddress().getHostAddress();
            InetAddress address = InetAddress.getByName(host);
            if (hostServer1.equals(address.getHostAddress()) && this.server2.getPortOutSM() == port) {
                return true;
            }
        }
        return false;

    }

    //Eliminacion de un Cliente.
    public synchronized void disconnectClient(Client client) {
        this.gui.addMsg(" Client " + client.getNickname() + " disconnected");
        broadcastRemNickname(client, client.getNickname());
        this.nicknames.remove(client.getNickname());
        this.gui.refresh();
        clients.remove(client);
        this.myClients--;
    }

    //Eliminar servidor.
    public void disconnectServer(Server s) {
        this.gui.addMsg(" Server " + s.getSocket().getInetAddress() + " " + s.getSocket().getPort() + " disconnected");
        broadcastRemNicknames(s, s.getNicknames());
        this.nicknames.removeAll(s.getNicknames());
        this.gui.refresh();
        if (s.equals(this.server1)) {
            this.server1 = null;
            this.gui.conexionPerdidaS1();
            this.server1Messages = 0;
        } else if (s.equals(this.server2)) {
            this.server2 = null;
            this.gui.conexionPerdidaS2();
            this.server2Messages = 0;
        }
        this.serversConnected--;

    }

    //Devolver un OutServerActivo con el host y puerto proporcionado.
    public OutServer getOutServerByParameters(int port, String host) {
        if (this.os1 != null) {
            if (this.os1.compareParameters(port, host)) {
                return os1;
            }
        }
        if (this.os2 != null) {
            if (this.os2.compareParameters(port, host)) {
                return os2;
            }
        }
        return null;

    }

    //Devuelve si hay dos servidores.
    public synchronized boolean onAllServers() {
        if (this.server1 != null && this.server2 != null) {
            return true;
        } else {
            return false;
        }
    }

    //Buscar que servidor hay que parar y pararlo.
    public void stopOutServer(Server s, String opcion) {
        if (s.equals(this.server1)) {
            stopOutServer1(opcion);
        } else {
            stopOutServer2(opcion);
        }

    }

    //Iniciar OutServer1
    public void startOutServer1(int portOS, String hostOS) {
        this.gui.addMsg(" OutServer 1 start");
        this.os1 = new OutServer(this, portOS, hostOS);
        this.os1.start();

    }

    //Parar OutServer1
    public void stopOutServer1(String opcion) {
        this.os1.setState(false);
        this.os1 = null;
        if (this.server1 != null) {
            if (opcion.equals("this")) {
                this.server1.txMsg("$CERRAR$");
            } else if (opcion.equals("out")) {
                this.gui.cambiarEstadoAParadoS1();
            }
            this.server1.stopSocket();
            this.server1 = null;
            this.server1Messages = 0;
            this.gui.addMsg(" OutServer 1 stop");

        }
    }

    //Iniciar OutServer2
    public void startOutServer2(int portOS, String hostOS) {
        this.gui.addMsg(" OutServer 2 start");
        this.os2 = new OutServer(this, portOS, hostOS);
        this.os2.start();
    }

    //Parar OutServer2
    public void stopOutServer2(String opcion) {
        this.os2.setState(false);
        this.os2 = null;
        if (this.server2 != null) {
            if (opcion.equals("this")) {
                this.server2.txMsg("$CERRAR$");
            } else if (opcion.equals("out")) {
                this.gui.cambiarEstadoAParadoS2();
            }
            this.server2.stopSocket();
            this.server2 = null;
            this.server2Messages = 0;
            this.gui.addMsg(" OutServer 1 start");
        }
    }
    //Iniciar SM.

    public void startSM(int portSM) {

        this.portSM = portSM;
        this.sm = new ServerManager(this, portSM);
        this.sm.start();
        this.gui.addMsg(" ServerManager start");

    }

    //Para el servidor.
    public void stopThisServer() {
        this.portSM = 0;
        this.nicknames.clear();
        this.gui.refresh();
        for (Client c : this.clients) {
            c.stopSocket();
        }
        if (this.os1 != null) {
            stopOutServer1("this");
        }
        if (this.os2 != null) {
            stopOutServer2("this");
        }
        if (this.server1 != null) {
            this.server1.txMsg("$CERRAR$");
            this.server1.stopSocket();
            this.server1 = null;
        }
        if (this.server2 != null) {
            this.server2.txMsg("$CERRAR$");
            this.server2.stopSocket();
            this.server2 = null;
        }
        this.clients.clear();
        this.sm.setState(false);
        this.sm = null;
        clearEstadisticas();
        this.gui.addMsg(" Server stop");

    }

    //Limpia las estadisticas.
    private void clearEstadisticas() {
        this.myClients = 0;
        this.historicoMyClients = 0;
        this.historicoServersConnected = 0;
        this.totalMessages = 0;
        this.myMessages = 0;
        this.server1Messages = 0;
        this.server2Messages = 0;

    }

    public void addNickname(String nick) {
        this.nicknames.add(nick);
        this.gui.refresh();
    }

    public void addNicknames(ArrayList<String> nicks) {
        this.nicknames.addAll(nicks);
        this.gui.refresh();
    }

    public void remNickname(String nick) {
        this.nicknames.remove(nick);
        this.gui.refresh();
    }

    public void remNicknames(ArrayList<String> nicks) {
        this.nicknames.removeAll(nicks);
        this.gui.refresh();
    }

    public int getPortSM() {
        return portSM;
    }

    public int getMyClients() {
        return myClients;
    }

    public int getServersConnected() {
        return serversConnected;
    }

    public int getTotalMessages() {
        return totalMessages;
    }

    public int getMyMessages() {
        return myMessages;
    }

    public void setMyMessages(int myMessages) {
        this.myMessages = myMessages;
    }

    public int getServer1Messages() {
        return server1Messages;
    }

    public int getServer2Messages() {
        return server2Messages;
    }

    public int getHistoricoMyClients() {
        return historicoMyClients;
    }

    public int getHistoricoServersConnected() {
        return historicoServersConnected;
    }

    public ArrayList<String> getNicknames() {
        return nicknames;
    }

    public void refreshNicks() {
        this.gui.refresh();
    }

}
