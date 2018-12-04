/**
 * 
 * UNIVERSIDADE DO VALE DO ITAJAÍ
 * Redes de Computadores 1
 * 
 * Uso de TCP / UDP / HTTP
 * 
 * @author Gabriel Hegler Klok
 * @author Maykon Anschau Oliveira
 * 
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SmartGateway {
    
    public static void main(String[] args){     
        if(args.length != 3){
            System.err.println("Usage: java kansocket.SmartGateway <ip of server UDP> <port connection UDP> <port server TCP>");
            System.exit(-1);
        }

        String ipOfServer = args[0];
        int portConnection = Integer.parseInt(args[1]);
        int portServer = Integer.parseInt(args[2]);
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                startServerTCP(portServer);
            }
        }).start();
        
        interval(2000);
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                startClientUDP(ipOfServer, portConnection, "oieeeee");
            }
        }).start();
    }
    
    private static void startServerTCP(int port){
        boolean exit = false;
        DataInputStream dataInput;
        DataOutputStream dataOutput;
        String data;
        
        try {         
        
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Listening to port " + String.valueOf(port));

            do {            

                Socket socket = serverSocket.accept();

                System.out.println("Received message from " +
                        socket.getInetAddress().getHostName() + ": " + socket.getPort());
                
                dataInput = new DataInputStream(socket.getInputStream());
                data = dataInput.readUTF();
                
                if(data.equalsIgnoreCase("exit")){
                    exit = true;
                }
                
                System.out.println("Echoing " + data);
                
                dataOutput = new DataOutputStream(socket.getOutputStream());
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
    
    
    private static void startClientUDP(String ip, int port, String message){        
        try {            
            DatagramSocket socket = new DatagramSocket();

            DatagramPacket datagram = new DatagramPacket(message.getBytes(), 0, message.getBytes().length, InetAddress.getByName(ip), port);

            socket.send(datagram);
            socket.close();
            
        } catch (Exception e) {
            System.err.println("An exception ocourred: "+e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    private static void interval(long value){
        try {
            Thread.sleep(value);
        } catch (Exception e) {
            System.err.println("Error in sleep: "+e.getMessage());
            System.exit(-1);
        } 
    }
}
