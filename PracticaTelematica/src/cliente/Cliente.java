package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente extends Thread {
	
    private Socket conexion;
    
    public Cliente(Socket socket) 
    {
        this.conexion = socket;
    }
    
    public static void main(String args[])
    {
        try {
        	
        	System.out.println("Digite la ip del servidor:");
        	Scanner leer = new Scanner(System.in);
        	String ip = leer.next();
            Socket socket = new Socket(ip , 10000);
            
            PrintStream salida = new PrintStream(socket.getOutputStream());
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Digite su nombre: ");
            String nombre = teclado.readLine();
            salida.println(nombre.toUpperCase());
            Thread thread = new Cliente(socket);
            thread.start();
            String msg;
            while (true)
            {
                System.out.print("Mensaje > ");
                msg = teclado.readLine();
                salida.println(msg);
            }
        } 
        catch (IOException e) 
        {
            System.out.println("Fallo de conexion" + " IOException: " + e);
        }
    }
    
    public void run()
    {
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(this.conexion.getInputStream()));
            String msg;
            while (true)
            {
                msg = entrada.readLine();
                if (msg == null) {
                    System.out.println("Conexion terminada");
                    System.exit(0);
                }
                System.out.println();
                System.out.println(msg);
                System.out.print("Responder > ");
            }
        } catch (IOException e) {
            System.out.println("Fallo en el envio del mensaje" + " IOException: " + e);
        }
    }
}