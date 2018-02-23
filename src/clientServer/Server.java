/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientServer;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import servidormensajeria.ServerProject;

/**
 *
 * @author andres
 */
public class Server extends Client {

    private int portOutSM;
    private ArrayList<String> nicknames;

    public Server(Socket socket, ServerProject server, int portOutSM) {
        super(socket, server, null);
        this.portOutSM = portOutSM;
        this.nicknames = new ArrayList<>();

    }

    @Override
    public void run() {
        try {
            this.addNicknames(this.sp.getNicknames());
            processMessages();
            this.socket.close();
            System.out.println("Server disconnected.");

        } catch (IOException ex) {
            System.out.println("Server disconnected.");
        }

    }

//Recepcion de mensajes y reenvio a los clientes, en caso de perdida de conexion
// se elimina el servidor.    
    @Override
    protected void processMessages() throws IOException {

        while (in.hasNextLine()) {
            String line = in.nextLine();
            switch (line.trim().toUpperCase().substring(0, 7)) {
                case "$MENSAJ":
                    sp.broadcastMsg(this, line.substring(8, line.length()));
                    break;
                case "$SERADD":
                    List<Integer> positionsAddServer = new LinkedList<>();
                    int positionAddServer = line.indexOf("$", 0);

                    while (positionAddServer != -1) {
                        positionsAddServer.add(positionAddServer);
                        positionAddServer = line.indexOf("$", positionAddServer + 1);
                    }
                    ArrayList<String> newsNicks = new ArrayList<>();
                    for (int i = 2; i <= positionsAddServer.size() - 1; i += 2) {
                        newsNicks.add(line.substring(positionsAddServer.get(i) + 1, positionsAddServer.get(i + 1)));
                    }
                    this.nicknames.addAll(newsNicks);
                    this.sp.addNicknames(newsNicks);
                    this.sp.broadcastAddNicknames(this, newsNicks);
                    break;
                case "$SERREM":
                    List<Integer> positionsRemServer = new LinkedList<>();
                    int positionRemServer = line.indexOf("$", 0);

                    while (positionRemServer != -1) {
                        positionsRemServer.add(positionRemServer);
                        positionRemServer = line.indexOf("$", positionRemServer + 1);
                    }
                    ArrayList<String> delNicks = new ArrayList<>();
                    for (int i = 2; i <= positionsRemServer.size() - 1; i += 2) {
                        delNicks.add(line.substring(positionsRemServer.get(i) + 1, positionsRemServer.get(i + 1)));

                    }
                    this.nicknames.removeAll(delNicks);
                    this.sp.remNicknames(delNicks);
                    this.sp.broadcastRemNicknames(this, this.sp.getNicknames());
                    break;
                case "$CLIADD":
                    List<Integer> positionsAddClient = new LinkedList<>();
                    int positionAddClient = line.indexOf("$", 0);

                    while (positionAddClient != -1) {
                        positionsAddClient.add(positionAddClient);
                        positionAddClient = line.indexOf("$", positionAddClient + 1);
                    }

                    this.nicknames.add(line.substring(positionsAddClient.get(2) + 1, positionsAddClient.get(2 + 1)));
                    this.sp.addNickname(line.substring(positionsAddClient.get(2) + 1, positionsAddClient.get(2 + 1)));

                    this.sp.broadcastAddNickname(this, line.substring(positionsAddClient.get(2) + 1, positionsAddClient.get(2 + 1)));
                    break;
                case "$CLIREM":
                    List<Integer> positionsRemClient = new LinkedList<>();
                    int positionRemClient = line.indexOf("$", 0);

                    while (positionRemClient != -1) {
                        positionsRemClient.add(positionRemClient);
                        positionRemClient = line.indexOf("$", positionRemClient + 1);
                    }
                    String stRem = line.substring(positionsRemClient.get(2) + 1, positionsRemClient.get(2 + 1));
                    this.nicknames.remove(stRem);
                    this.sp.remNickname(stRem);

                    this.sp.broadcastRemNickname(this, stRem);
                    break;
                case "$CERRAR":
                    sp.stopOutServer(this, "out");
                    break;
            }
        }
        sp.disconnectServer(this);
    }

    public int getPortOutSM() {
        return portOutSM;
    }

    public ArrayList<String> getNicknames() {
        return nicknames;
    }

}
