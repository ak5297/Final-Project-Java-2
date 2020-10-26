/**
 * @authors Rachael Simmonds, Ethan Marschean, Alex Katavongsa, 
 * Fernado Rodriguez, and Rouqi Chen 
 * @version 20190411
 * Prof. Patric
 * ISTE 121
**/
 
/**
 * Imports
**/
//import java.net.*; - Imports below are necessary for checkstyle!
import java.net.ServerSocket;
import java.net.Socket;
import java.net.BindException;
//import java.io.*; - Imports below are necessary for checkstyle!
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
//import java.util.*; - Imports below are necessary for checkstyle!
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main Program Class 
**/
public class Server
{
   // Private attributes
   private static PrintWriter opw;
   private ArrayList<PrintWriter> pp = new ArrayList<PrintWriter>();
   private int count = 0;
   private String nick = "";
   
   /**
    * Server constructor accept client
   **/
   
   public Server()
   {
      try
      {
         ServerSocket ss = new ServerSocket(16789);
         Socket cs = null;
         System.out.println("Server is Running");
         while (true)
         {
            cs = ss.accept();
            opw = new PrintWriter(new OutputStreamWriter(cs.getOutputStream()));
            MyThread th = new MyThread(cs, "Client" + " " + (count + 1), opw);
            opw.println("Welcome, you joined the chat! You are Client "
               + (count + 1));
               
            opw.flush();
            pp.add(opw);
            count++;
            th.start();
                         
         } // End of while
      }catch (BindException be)
      {
         System.out.println("System stop running!");
      }catch (IOException e)
      {
         e.toString();
      } // End of catch
   } // End of constructor
   
   /**
    * Inner class myThread
    * threads send messages
   **/
   class MyThread extends Thread
   { 
      private Socket cs;
      private String name;
      private BufferedReader br = null;
      private PrintWriter topw;
       
      /**
       * Constuctor
      **/       
      MyThread(Socket s, String st, PrintWriter _topw)
      {   
         cs = s;
         name = st;
         topw = _topw;
      } // End of constructor
      
      public void run()
      {  
         System.out.println("Client" + count + " is Connected");
         int localCount = count;
         try
         {
            br = new BufferedReader(new InputStreamReader(cs.getInputStream()));
         } // End of try
         catch (IOException e)
         {
            e.toString();
         } // End of catch
         try{
            nick = br.readLine();
            if(!nick.equals("")){
               name = nick;
            }
         }catch(IOException ioe)
         {
            System.out.println("There was an IO Error: " + ioe);
         }
         
         while (true)
         {
            String message = "";
            boolean closed = false;
            
            try{
               message = br.readLine();
               
               if(message == null){
               try{
                  pp.remove(topw);
                  cs.close();
                  closed = true;
                  message = ("has Disconnected.");
                  br.close();
               }catch(IOException ioe2){
                  System.out.println("There was an IO Error: " + ioe2);
               }               
}
               
            } catch(IOException ioe){
              
               try{
                  pp.remove(topw);
                  cs.close();
                  closed = true;
                  message = (" has Disconnected.");
                  br.close();
               }catch(IOException ioe2){
                  System.out.println("There was an IO Error: " + ioe2);
               }               
            }
                     
               
            for (int i = 0; i < pp.size(); i++) 
            { 
                        
               try
               {
                  sleep(1);
               } // End of try
               catch (InterruptedException e)
               {
                  e.toString();
               } // End of catch
                  
               pp.get(i).println(name + ": " + message);
               pp.get(i).flush();
                                
            } // End of for loop
                      
            if (closed)
            {
               System.out.println("Client " + localCount + " is Disconncted");
               break;
            } // End of if
         
         } // End of While loop
      
      } // End of Run method
      
   } // End of Inner Class myThreads

   /**
    * Main program to run Server Class
    * @param args *args used to run arguments*
   **/
   public static void main(String [] args)
   {
      new Server();  
   } // End of Main

} // End of Server Class
