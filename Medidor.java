/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kansocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 *
 * @author klok
 */
public class Medidor {

    public static void main(String[] args){
        if(args.length != 3){
            System.err.println("Usage: java Medidor <ip address> <port> <message>");
            System.exit(-1);
        }
        
        try {
            
            String ipServer = args[0];
            int portServer = Integer.parseInt(args[1]);
            String message = args[2];
            
            Socket socket = new Socket(ipServer, portServer);
            
            DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());
            dataOutput.writeUTF(message);
            
            DataInputStream dataInput = new DataInputStream(socket.getInputStream());
            String data = dataInput.readUTF();
            
            socket.close();
            
            if (data.equals(message)) {
                System.out.println("Successfull");
            }else{
                System.out.println("send: " + message);
                System.out.println("Received: " + data);
            }
            
        } catch (Exception e) {  
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
