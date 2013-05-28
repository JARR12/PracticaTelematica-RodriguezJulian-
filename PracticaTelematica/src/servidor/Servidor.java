package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

public class Servidor extends Thread 
{
	
    private static Vector clientes;
    private Socket conexion;
    private String nombreCliente;
    private static List listaDeNombres = new ArrayList();
    
    
    public Servidor(Socket socket) 
    {
        this.conexion = socket;
    }
    
    public boolean almacenar(String nombre)
    {
       for (int i=0; i< listaDeNombres.size(); i++)
       {
         if(listaDeNombres.get(i).equals(nombre))
           return true;
       }
       listaDeNombres.add(nombre);
       return false;
    }
    
    public void remove(String oldName) 
    {
       for (int i=0; i< listaDeNombres.size(); i++)
       {
         if(listaDeNombres.get(i).equals(oldName))
           listaDeNombres.remove(oldName);
       }
    }
    
    public static void main(String args[]) 
    {
    	
        clientes = new Vector();
        
        try 
        {
            ServerSocket server = new ServerSocket(5554);
            System.out.println("Servidor en el socket 5554");
            
            while (true) 
            {
                Socket conexion = server.accept();
                Thread t = new Servidor(conexion);
                t.start();
            }
        } 
        catch (IOException e) 
        {
            System.out.println("IOException: " + e);
        }
    }
    
    public void run()
    {
    	
        try 
        {
        	
            BufferedReader entrada = new BufferedReader(new InputStreamReader(this.conexion.getInputStream()));
            PrintStream salida = new PrintStream(this.conexion.getOutputStream());
            this.nombreCliente = entrada.readLine();
            
            if (almacenar(this.nombreCliente))
            {
              salida.println("Nombre ya existente");
              clientes.add(salida);
              this.conexion.close();
              return;
            } 
            else 
            {
               System.out.println(this.nombreCliente + " Se ha conectado");
            }
            
            if (this.nombreCliente == null) 
            {
                return;
            }
            
            clientes.add(salida);
            String msg = entrada.readLine();
            
            while (msg != null && !(msg.trim().equals("")))
            {
                sendToAll(salida, " Escribe :: ", msg);
                msg = entrada.readLine();
            }
            
            System.out.println(this.nombreCliente + " Conexion Terminada ");
            sendToAll(salida, " Se ha desconectado", " ");
            remove(this.nombreCliente);
            clientes.remove(salida);
            this.conexion.close();
            
        } 
        catch (IOException e) 
        {
            System.out.println("Fallo de conexion"+" IOException: " + e);
        }
        
    }
    
    
    public void sendToAll(PrintStream salida, String a, String msg) throws IOException
    {
    	
        Enumeration e = clientes.elements();
        
        while (e.hasMoreElements()) 
        {
            PrintStream chat = (PrintStream) e.nextElement();
            if (chat != salida) 
            {
                chat.println(this.nombreCliente + a + msg);
            }
        }
      }
    

}
