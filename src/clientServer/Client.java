/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientServer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import servidormensajeria.ServerProject;

/**
 *
 * @author andres
 */
public class Client implements Runnable {

    protected ServerProject sp;
    protected Socket socket;
    protected Scanner in;
    protected PrintWriter out;
    private String nickname;

    public Client(Socket socket, ServerProject server, String nick) {
        try {
            this.sp = server;
            this.socket = socket;
            this.nickname = nick;
            this.in = new Scanner(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            addNicknames(this.sp.getNicknames());
            processMessages();
            this.socket.close();
            System.out.println("Client disconnected.");

        } catch (IOException ex) {
            System.out.println("Client disconnected.");
        }

    }

    //Recepcion de mensajes y reenvio al resto de clientes y al servidor,
    //al perder la conexion se elimina el cliente.
    protected void processMessages() throws IOException {
        while (this.in.hasNextLine()) {
            String line = in.nextLine();
            switch (line.trim().toUpperCase().substring(0, 7)) {
                case "$MENSAJ":
                    this.sp.broadcastMsg(this, this.nickname + ": " + line.substring(8, line.length()));
                    this.sp.setMyMessages(this.sp.getMyMessages() + 1);
                    break;
            }

        }
        sp.disconnectClient(this);
    }

    //Envia que añada los nicknames proporcionados.
    public void addNicknames(ArrayList<String> nicks) {
        if (!nicks.isEmpty()) {
            StringBuilder outNicks = new StringBuilder();
            outNicks.append("$SERADD$");
            for (String s : nicks) {
                outNicks.append("$" + s + "$");
            }
            out.println(outNicks.toString());
        }
    }

    //Envia que elimine los nicknames proporcionados.
    public void remNicknames(ArrayList<String> nicks) {
        if (!nicks.isEmpty()) {
            StringBuilder outNicks = new StringBuilder();
            outNicks.append("$SERREM$");
            for (String s : nicks) {
                outNicks.append("$" + s + "$");
            }
            out.println(outNicks.toString());
        }
    }

    //Envia que añada el nickname proporcionados.
    public void addNickname(String nick) {
        StringBuilder outNicks = new StringBuilder();
        outNicks.append("$CLIADD$");
        outNicks.append("$" + nick + "$");
        out.println(outNicks.toString());
    }

    //Envia que elimine el nickname proporcionados.
    public void remNickname(String nick) {
        StringBuilder outNicks = new StringBuilder();
        outNicks.append("$CLIREM$");
        outNicks.append("$" + nick + "$");
        out.println(outNicks.toString());
    }

    //Envio de mensajes al cliente.
    public void txMsg(String msg) {
        out.println(msg);
    }

    public void stopSocket() {
        try {
            this.socket.close();
        } catch (IOException ex) {

        }
    }

    public Socket getSocket() {
        return socket;
    }

    public String getNickname() {
        return nickname;
    }

}
