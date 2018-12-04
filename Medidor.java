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
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Medidor {

    public static void main(String[] args){
        if(args.length != 3){
            System.err.println("Usage: java kansocket.Medidor <ip of server TCP> <port connection TCP> <port server UDP>");
            System.exit(-1);
        }
        
        String ipOfServer = args[0];
        int portConnection = Integer.parseInt(args[1]);
        int portServer = Integer.parseInt(args[2]);
            
        new Thread(new Runnable() {
            @Override
            public void run() {
                startServerUDP(portServer);
            }
        }).start();
                   
        interval(2000);      
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                startClientTCP(ipOfServer, portConnection);
            }
        }).start();
    }
    
    private static void startServerUDP(int port){
        boolean exit = false;
        String message;
        DatagramSocket socket;
        DatagramPacket datagram;
        
        try {
            
            socket = new DatagramSocket(port);
            System.out.println("Listening to port " + String.valueOf(port));
            
            do {                
                datagram = new DatagramPacket(new byte[1024], 1024);
                socket.receive(datagram);
                
                message = new String(datagram.getData()).trim();
                
                if (message.equalsIgnoreCase("exit")) {
                    exit = true;
                }
                
                System.out.println("Received " + message + " from " +
                        datagram.getAddress() + ":" + datagram.getPort());
                
            } while (!exit);
            
            socket.close();
            
        } catch (Exception e) {
            System.err.println("An exception ocourred: "+e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        } 
    }
    
    private static void startClientTCP(String server, int port){
        Scanner scanner = new Scanner(System.in);
        String message, data = "";
        boolean stop = false;
        DataOutputStream dataOutput;
        DataInputStream dataInput;
        Random random = new Random();
        int consumo = 0;
        
        try {
            
            Socket socket;
            
            do {
                System.out.print("Aguardando próxima aferição");
                for(int i = 0; i < 5; i++){
                    System.out.print(".");
                    Thread.sleep(1000);
                }
                consumo += random.nextInt(20);
                message = String.valueOf( consumo );     
                
                System.out.println("Consumo atual enviado: " + message);
                
                socket = new Socket(server, port);

                dataOutput = new DataOutputStream(socket.getOutputStream());
                dataOutput.writeUTF(message);

                dataInput = new DataInputStream(socket.getInputStream());
                data = dataInput.readUTF();

                socket.close();

                if(!data.equalsIgnoreCase(message)){
                    System.out.println("Message is not valid!");
                }
                
            } while (!stop);
            
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
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
