/*
 * @author: Rouqi Chen, Fernando Rodriguez, Ethan Marschean,
 * Alex Ketavongsa, Rachael Simmonds
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
import java.text.*;
import javax.mail.*;

public class Client extends JFrame implements ActionListener{
      
   private Socket cs = null;
   private Scanner sc = null;
   private PrintWriter pw = null;
    //Panels
   JPanel south = new JPanel();
   JPanel north = new JPanel();
   JPanel jp1 = new JPanel();
   JPanel jp2 = new JPanel();
   JPanel jp3 = new JPanel();
   
   //Booleans
   private boolean disconnectBol = false;
   public boolean clientDis = false;
      
      //buttons
   JButton jb = new JButton("Connect");
   JButton send = new JButton("Send");
   JButton jbSendToMail = new JButton("Send to email");
   JButton dark = new JButton("Dark");
   JButton light = new JButton("Ultra-Light");
   JButton outRun = new JButton("Outrun");
   JButton cb1 = new JButton("Color-Blind 1");
   JButton cb2 = new JButton("Color Blind 2");
   JButton cb3 = new JButton("Color Blind 3");
   JButton rit = new JButton("RIT");
   
      //labels
   JLabel jlNick = new JLabel("Nickname");
      //text fields
   JTextField jtf = new JTextField(20);
   JTextField jtf2 = new JTextField(20);
   
   JTextField jtfNick = new JTextField(19);
      //text areas
   JTextArea area = new JTextArea(10,40);
   JScrollPane sp = new JScrollPane(area);
      //Menu Bar
   JMenuBar jmbBar = new JMenuBar();
   JMenu jmOptions = new JMenu("Options");
   JMenuItem jmiThemes = new JMenuItem("Themes");
   JMenuItem jmiSendToEmail = new JMenuItem("Send To Email");
   JMenuItem save = new JMenuItem("Save Log");
      
      
      /*
       * class client
       * gui
       */
   public Client(){
      
         //set gui
      
      north.setLayout(new GridLayout(0,1));
      north.add(jmbBar, BorderLayout.NORTH);
      jmbBar.add(Box.createHorizontalGlue());
      jmbBar.add(jmOptions);
      jmOptions.add(jmiThemes);
      jmOptions.add(jmiSendToEmail);
      jmOptions.add(save);
      add(north, BorderLayout.NORTH);
      add(south, BorderLayout.SOUTH);
      south.setLayout (new GridLayout(0,1));
      south.add(jp3);
      south.add(jp1);
      south.add(jp2);
      jp1.add(jb);
      jp3.add(jlNick);
      jp3.add(jtfNick);
      jp1.add(jtf);
      jp2.add(send);
      jp2.add(jtf2);
      jtf2.setEnabled(false);
      area.setEnabled(false);
      send.setEnabled(false);
         
      add(sp, BorderLayout.CENTER);
      jb.addActionListener(this);
      send.addActionListener(this);
      jmiSendToEmail.addActionListener(this);
      save.addActionListener(this);
      jmiThemes.addActionListener(this);
         
      pack();
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setTitle("!Failure Client");
      setVisible(true); 
      setLocationRelativeTo(null);
   }  
      
      /* 
       * buttons
       * connect, disconnect, and sent
       */
   public void actionPerformed(ActionEvent ae) {
      switch(ae.getActionCommand()) {
         case "Connect":
            doConnect();
            break;
         case "Disconnect":
            disconnectBol = true;  
            doDisconnect();
            clientDis = true;
            break;
         case "Send":
            doSend();
            break;
         case "Send To Email":
            SendEmailGUI obj = new SendEmailGUI();
            break;
         case "Save Log":
            saveLog();
            break;
         case "Themes" :  Object[] colors = {"Dark", 
            "Ultra-Light", 
               "Outrun", 
                  "Dueteranope", 
                     "Protanope", 
                        "Tritanerope", 
                           "RIT",};
           int n = JOptionPane.showOptionDialog(null,
                    "Which color do you like?",
                    "Choose a color",
                     JOptionPane.DEFAULT_OPTION,
                     JOptionPane.QUESTION_MESSAGE,
                     null,
                     colors,
                     colors[0]);
                              
                     if (n!=-1)
                     
                        setTheme((String)colors[n]);
                     
                     break;
      
           
      }
   }
   
   /* 
    * connect clients
    */ 
   private void doConnect() {
   
      jtf.setEnabled(false);
      send.setEnabled(true);
      jtf2.setEnabled(true);
      try {
         cs = new Socket(jtf.getText(), 16789);
         sc = new Scanner(new InputStreamReader(cs.getInputStream()));
         pw = new PrintWriter(new OutputStreamWriter(cs.getOutputStream()));
         
      }
      catch(IOException ioe) {
         area.append("IO Exception: " + ioe + "\n");
         return;
      }
      
      pw.println(jtfNick.getText()); 
      pw.flush();
      
      myThread th = new myThread();
      th.start();
      jb.setText("Disconnect");
      jtfNick.setEnabled(false);
   }
   
   /* 
    * disconnect when close window
    */
   private void doDisconnect() {
   
      send.setEnabled(false);
      jtf2.setEnabled(false);
      jtfNick.setEnabled(true);
      jtf.setEnabled(true);
      
      try {
         
         cs.close();
         sc.close();
         pw.close();
      }
      catch(IOException ioe) {
         area.append("IO Exception: " + ioe + "\n");
         return;
      }
      
      jb.setText("Connect");
      disconnectBol = false;
   }
   
   /* 
    * send message
    * showing what you send
    */
   private void doSend(){
      try{
         pw.println(jtf2.getText());
         jtf2.setText("");
         pw.flush();
      } catch(Exception e){
         System.out.println("There was an error: " + e);
      }
      
   }
   
   private void saveLog() {
      SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
      DateFormat currentTime = new SimpleDateFormat("hh:mm:ss");
      Date date = new Date();  
      System.out.println(formatter.format(date));
      String chatMessages = area.getText();
      String timeStamp = "This chat log was saved on " + formatter.format(date);
      
      try {
         FileWriter writeOut = new FileWriter("chat_log.txt", false);
         writeOut.write(timeStamp + "\n");
         writeOut.write("\n --- Start of Log --- \n");
         writeOut.write("\n" + chatMessages);
         writeOut.write("\n --- End of Log ---");
         writeOut.close();
      }
      
       
      catch(IOException ioe) {
         System.out.println("IO Exception");
      }
   }
   
   private void setTheme(String newTheme) 
    {
      boolean changeTheme = true;
      String theme = newTheme;     // Options :   dark  :  ultra-light  :  outrun  : Color Blind 1-3 : RIT :  default
      Color background = null;
      Color foreground = null;
      Color accentBackground = null;
      Color accentForeground = null;
      Color clear = null;
      
         switch (theme) 
         {
            case "Dark" :     // dark theme
                              background = Color.DARK_GRAY;
                              foreground = Color.WHITE;
                              accentBackground = Color.GRAY;
                              accentForeground = Color.WHITE;
                              clear = new Color(0,0,0,0);
                              changeTheme = true;
                              break;
                              
            case "Ultra-Light" : // Ultra-Light theme
                              background = Color.WHITE;
                              foreground = Color.BLACK;
                              accentBackground = Color.WHITE;
                              accentForeground = Color.BLACK;
                              clear = new Color(0,0,0,0);
                              changeTheme = true;
                              break;
                              
            case "Outrun" :     // Outrun theme
                              background = new Color (50, 4 , 77);
                              foreground = new Color (78, 225 , 215);
                              accentBackground = new Color (238, 45 , 137);
                              accentForeground = new Color (78, 225 , 215);
                              clear = new Color(0,0,0,0);
                              changeTheme = true;
                              break;
                              
            case "Dueteranope" :     // dueteranope theme
                              background = new Color (51, 51, 255);
                              foreground = new Color (179, 179, 0);
                              accentBackground = new Color (179, 179, 0);
                              accentForeground = new Color (255, 255, 230);
                              clear = new Color(0,0,0,0);
                              changeTheme = true;
                              break;
                              
            case "Protanope" :     // protanope theme
                              background = new Color (128, 128, 0);
                              foreground = new Color (255, 255, 102);
                              accentBackground = new Color (51, 102, 255);
                              accentForeground = new Color (255, 255, 102);
                              clear = new Color(0,0,0,0);
                              changeTheme = true;
                              break;
                              
            case "Tritanerope" :     // tritanerope theme
                              background = new Color (255, 0, 255);
                              foreground = new Color (0, 0, 0);
                              accentBackground = new Color (102, 204, 255);
                              accentForeground = new Color (0, 0, 0);
                              clear = new Color(0,0,0,0);
                              changeTheme = true;
                              break;
                              
            case "RIT" :     // RIT theme
                              background = new Color (255, 102, 0);
                              foreground = new Color (77, 40, 0);
                              accentBackground = new Color (0, 0 , 0);
                              accentForeground = new Color (255, 225, 255);
                              clear = new Color(0,0,0,0);
                              changeTheme = true;
                              break;
           
            default :         // no theme change
                              changeTheme = false;
                              this.getContentPane().setBackground( clear );
                              this.getRootPane().setBorder(BorderFactory.createMatteBorder(20, 20, 20, 20, clear));
                              clear = new Color(0,0,0,0);
                              background = clear;
                              foreground = clear;
                              accentBackground = clear;
                              accentForeground = clear;
                              break;
                              
         }
         if (changeTheme) 
         {
            // backgrounds
            this.getContentPane().setBackground( background );
            this.getRootPane().setBorder(BorderFactory.createMatteBorder(20, 20, 20, 20, background));
            
            //  panels
            Color panelColor = UIManager.getColor ( "Panel.background" );
            north.setBackground( clear );
            south.setBackground( clear );
            jp1.setBackground( clear );
            jp2.setBackground( clear );
            jp3.setBackground( clear );

            // text areas/fields/scroll pane
            Color textAreaColor = UIManager.getColor ( "TextArea.background" );
            jtf.setBackground( accentBackground );
            jtf.setForeground( accentForeground );

            jtf2.setBackground( accentBackground);
            jtf2.setForeground( accentForeground );

            jtfNick.setBackground( accentBackground );
            jtfNick.setForeground( accentForeground );

            area.setBackground( accentBackground );
            area.setForeground( accentForeground );

            sp.setBackground( accentBackground );
            sp.setForeground( accentForeground );
            
            //  buttons
            jb.setBackground( accentBackground );
            jb.setForeground( accentForeground );
            
            send.setBackground( accentBackground );
            send.setForeground( accentForeground );
           
            dark.setForeground( accentForeground );
            dark.setBackground( accentBackground );
            
            light.setForeground( accentForeground );
            light.setBackground( accentBackground );
            
            outRun.setForeground( accentForeground );
            outRun.setBackground( accentBackground );
            
            cb1.setForeground( accentForeground );
            cb1.setBackground( accentBackground );
            
            cb2.setForeground( accentForeground );
            cb2.setBackground( accentBackground );
            
            cb3.setForeground( accentForeground );
            cb3.setBackground( accentBackground );
               
            rit.setForeground( accentForeground );
            rit.setBackground( accentBackground );

            //  labels 
            jlNick.setForeground( foreground );
            jlNick.setBackground( accentBackground);

            //  bar
            jmbBar.setForeground( foreground );
            jmbBar.setBackground( background );
            jmOptions.setForeground( foreground );
            jmOptions.setBackground( background );
            jmiThemes.setForeground( foreground );
            jmiThemes.setBackground( background );
            jmiSendToEmail.setForeground( foreground );
            jmiSendToEmail.setBackground( background );
            save.setForeground( foreground );
            save.setBackground( background );
          

      } // End of if
   } // End of switch

      
   /*6
    * main
    */   
   public static void main(String [] args){
      LoginGui login = new LoginGui();
       
   }
   
   /* 
    * threads
    * clients can communicate
    */
   public class myThread extends Thread{
   
      public void run(){
         while(sc.hasNextLine()){
            String reply = sc.nextLine();
            area.append(reply+"\n");      
         }
         if(disconnectBol == false){
            doDisconnect();
            area.append("The Server has been closed unexpectedly ... \n");
         }else{
            area.append("Client disconnected ... Have A Nice Day!\n");
         }
      }
   }
}
