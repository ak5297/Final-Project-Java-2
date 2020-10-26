/*
 * @author: Rouqi Chen, Fernando Rodriguez, Ethan Marschean,
   Alex Ketavongsa, Rachael Simmonds
 * Client Class
 * ISTE.121.01/03
 * @version 20190319
 */
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

public class LoginGui extends JFrame {
      
   Register rg = new Register(); 
   Login lg = new Login();
      
   JPanel north = new JPanel();
   JPanel jp1 = new JPanel();
   JPanel jp2 = new JPanel();
   JPanel jp3 = new JPanel();
      

            //text fields
   JTextField jtf = new JTextField(20);
   JPasswordField jtf2 = new JPasswordField(20);

      
   JPanel northPanel = new JPanel();
   JPanel centerPanel = new JPanel();
   JLabel username = new JLabel("Username:");
   JLabel password = new JLabel("Password:");
   JTextField name = new JTextField(null,12);
   JPasswordField word = new JPasswordField(null,12);
   JButton register = new JButton("Register");
   JButton login = new JButton("Login");

   
   File text = new File("Text.txt");
   File file = new File("Registered_list.csv");
   BufferedReader br = null;
   FileWriter fw = null;
   int line = 0;
   String a[];


      
      /*
       * class client
       * gui
       */
   public LoginGui(){
      
      setLocationRelativeTo(null);
         
   
      word.setEchoChar('\u2022');
      add(northPanel, BorderLayout.NORTH);
      northPanel.setLayout(new GridLayout(3,2));
      northPanel.add(username);
      northPanel.add(name);
      northPanel.add(password);
      northPanel.add(word);
      username.setHorizontalAlignment(SwingConstants.RIGHT);
      password.setHorizontalAlignment(SwingConstants.RIGHT);
   
   
         
      add(centerPanel, BorderLayout.CENTER);
      centerPanel.setLayout(new FlowLayout());
      centerPanel.add(register);
      centerPanel.add(login);
         
      register.addActionListener(rg);
      login.addActionListener(lg);
   
      pack();
      setDefaultCloseOperation(HIDE_ON_CLOSE);
      setTitle("!Failure Client");
      setVisible(true); 
         
      
   }  
      

      
   /*6
    * main
    */   
   public static void main(String [] args){
      new LoginGui();
   }

   
   class Register implements ActionListener
   {
      public void actionPerformed(ActionEvent ae)
      {
         if(ae.getActionCommand().equals("Register"))
         {
            line=0;
            int num = 0;
            try{
               if(file.exists() == true)
               {
                  if(name.getText().equals(""))
                  {JOptionPane.showMessageDialog(null, "Username can't be blank!");}
                  else if(word.getText().equals(""))
                  {JOptionPane.showMessageDialog(null, "Password can't be blank!");}
                  else{
                     fw =new FileWriter(file,true);// when the file is already exists
                     br = new BufferedReader(new FileReader(file));// create a bufferedreader
                     while (br.readLine() != null)//read line
                     {
                        line++;  
                     }
                     a=new String[line];
                     br = new BufferedReader(new FileReader(file));// create a bufferedreader
                     for(int i = 0;i<(line);i++)
                     {
                        a[i] = br.readLine();
                     }
                  
                     for(int i=0;i<line;i++)
                     {
                        if(a[i].equals(name.getText()))
                        {JOptionPane.showMessageDialog(null, "Username registered!");  
                           num=-1;
                           break;
                        }
                        else{
                           i++;}
                     }
                     if(num == -1)
                     {}
                     else{ 
                     //call method in here
                        String user = getSHA(name.getText());
                        String pass = getSHA(word.getText());
                        fw.write("\n"+user);
                        fw.write("\n"+pass);
                        fw.flush();
                        fw.close();
                        name.setText(null);
                        word.setText(null);
                        JOptionPane.showMessageDialog(null, "Username register success!");
                     
                     }
                   
                  
                  }
               
                
               }//if file.exists == true
               else
               {  
                  if(name.getText().equals(null))
                  {JOptionPane.showMessageDialog(null, "Username can't be blank!");}
                  else if(word.getText().equals(null))
                  {JOptionPane.showMessageDialog(null, "Password can't be blank!");}
                  else{
                     fw=new FileWriter(file);
                     String user = getSHA(name.getText());
                     String pass = getSHA(word.getText());
                     fw.write("\n"+user);
                     fw.write("\n"+pass);
                     fw.flush();
                     fw.close();
                  
                     name.setText(null);
                     word.setText(null);
                     JOptionPane.showMessageDialog(null, "Username register success!");
                  
                  }
               }//else
            }//try
            catch(FileNotFoundException fe)
            {
               JOptionPane.showMessageDialog(null, "Missing file or no data to read.");//file not found exception
            
            
            }
            catch(IOException ioe)
            {
               JOptionPane.showMessageDialog(null, "ERROR!");//IOexception
            
            }
         }
      }
   }//class register
   class Login implements ActionListener
   {
      int allow;
      public void actionPerformed(ActionEvent ae)
      {
         
         if(ae.getActionCommand().equals("Login"))
         {
            try{
               int num = 0;
               line=0;
            
               fw =new FileWriter(file,true);// when the file is already exists
               br = new BufferedReader(new FileReader(file));// create a bufferedreader
               while (br.readLine() != null)//read line
               {
                  line++;  
               }
               a=new String[line];
               br = new BufferedReader(new FileReader(file));// create a bufferedreader
               for(int i = 0;i<(line);i++)
               {
                  a[i] = br.readLine();
               }
               if(name.getText().equals(""))
               {JOptionPane.showMessageDialog(null, "Username can't be blank!");}
               else if(word.getText().equals(""))
               {JOptionPane.showMessageDialog(null, "Password can't be blank!");}
               else{
                  for(int i=0;i<line;i++)
                  {
                     if(a[i].equals(getSHA(name.getText())) && a[i+1].equals(getSHA(word.getText())))
                     {  
                     
                        num=1;
                     
                        break;   
                     }else{
                     
                     
                     
                        i++;
                     }
                  
                  
                  }
                  if(num == 1)
                  {
                     JOptionPane.showMessageDialog(null, "Login success!");
                     Client obj = new Client();
                     setVisible(false);
                  
                  }else
                  {
                     JOptionPane.showMessageDialog(null, "Wrong username or password!");
                  
                     setAllow(0);
                  
                  }
               }
            
            }//try
            catch(FileNotFoundException fe)
            {
               JOptionPane.showMessageDialog(null, "Missing file or no data to read.");//file not found exception
            
            
            }
            catch(IOException ioe)
            {
               JOptionPane.showMessageDialog(null, "ERROR!");//IOexception
            
            }
         
         }
      
      }//actionPerformed
      public int getAllow()
      {
         return allow;
      }
      public void setAllow(int i)
      {
         allow = i;
      }      
   }//class login
        
        
        //method of hashing
        
   public static String getSHA(String input) 
    { 
  
        try { 
  
            // Static getInstance method is called with hashing SHA 
            MessageDigest md = MessageDigest.getInstance("SHA-256"); 
  
            // digest() method called 
            // to calculate message digest of an input 
            // and return array of byte 
            byte[] messageDigest = md.digest(input.getBytes()); 
  
            // Convert byte array into signum representation 
            BigInteger no = new BigInteger(1, messageDigest); 
  
            // Convert message digest into hex value 
            String hashtext = no.toString(16); 
  
            while (hashtext.length() < 32) { 
                hashtext = "0" + hashtext; 
            } 
  
            return hashtext; 
        } 
  
        // For specifying wrong message digest algorithms 
        catch (NoSuchAlgorithmException e) { 
            System.out.println("Exception thrown"
                               + " for incorrect algorithm: " + e); 
  
            return null; 
        } 
    }   
   
}
