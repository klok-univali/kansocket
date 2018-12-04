/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kansocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author klok
 */
public class SmartGateway {
    
    public static void main(String[] args){
        
        if(args.length != 1){
            System.err.println("Usage: java SmartGateway <port>");
            System.exit(-1);
        }
        
        try {
            int port = Integer.parseInt(args[0]);
            boolean exit = false;
            
            ServerSocket serverSocket = new ServerSocket(port);
            
            do {                
                
                Socket socket = serverSocket.accept();
                
                System.out.println("Received message from " +
                        socket.getInetAddress().getHostName() + ": " + socket.getPort());
                
                DataInputStream dataInput = new DataInputStream(socket.getInputStream());
                String data = dataInput.readUTF();
                
                if(data.equalsIgnoreCase("exit")){
                    exit = true;
                }
                
                System.out.println("Echoing " + data);
                
                DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());
                dataOutput.writeUTF(data);
                
                socket.close();
                
            } while (!exit);
            
            serverSocket.close();
            
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    
}
