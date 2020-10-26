/*
 * @author: Rouqi Chen, Fernando Rodriguez, Ethan Marschean,
 * Alex Katavongsa, Rachael Simmonds
 * Dots Class
 * ISTE.121.01/03
 * @version 20190506
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;


public class Dots extends JFrame {  
   //attributes
   private int playerscore1 = 0;
	private int playerscore2 = 0;
   private boolean keepgoing = true;
   private int count = 0;
   private int win[][] = new int[8][8];
   private int count2;
   private int count3=0;
   private String playername1;
	private String playername2;
	private JLabel player1;
	private JLabel player2;
   private JRadioButton[][] buttonArray = new JRadioButton[8][8];
   private boolean h[][] = new boolean[8][8];
	private boolean v[][] = new boolean[8][8];
   private String name = "records.bin";
   
   /*
    * Dots class
    * Set gui
    * by Rouqi Chen
    */
   public Dots() {
      setSize(900,700);
      setLocationRelativeTo(null);
      setTitle("Connect the dot");
      setDefaultCloseOperation(HIDE_ON_CLOSE);
      
      JMenuBar jmb = new JMenuBar();
      JMenu file = new JMenu("File");
      jmb.add(file);
      JMenuItem newgame = new JMenuItem("New Game");
      JMenuItem save = new JMenuItem("Save");
      JMenuItem xit = new JMenuItem("Exit");
      JMenuItem help = new JMenuItem("Rule");
      JMenuItem load = new JMenuItem("Load");
      
      //set gui
      file.add(newgame);
      file.add(save);
      file.add(load);
      file.add(help);
      file.add(xit);
      
      //players
      Player players = new Player();
		add(players, BorderLayout.NORTH);
      
      Game game = new Game();
      add(game, BorderLayout.CENTER);
      Thread th = new Thread(game);
      th.start();
      
      //exit action listener
      xit.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent ae)
         {
            getWinner();
            JOptionPane.showMessageDialog(null,"bye!");
            setVisible(false);
         }
      
      });
      
      // close
      addWindowListener(new WindowAdapter()
      {
      public void windowClosing(WindowEvent we)
      {  
            getWinner();
           JOptionPane.showMessageDialog(null, "bye!");
         }  });
      
      
      
      //rule
      help.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent ae)
         {
            JOptionPane.showMessageDialog(null,"1. Each time you can only connect 2 dots to a line. If you connect the line to a box then you gain one point."
                                                +"\n 2. If your name is red, then it's your time to connect the dots."
                                                +"\n 3. If you gained one point, then you got one extra term."
                                                +"\n 4. The save button save the game for the next time, when you click on the load you will access the archive");

         }
      
      });
      
      //new game
      newgame.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent ae)
         {
            dispose();
            new Dots();
         }
      
      });
      
      //save game for next time.
      save.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent ae) {
            try{
               
          
                FileOutputStream fos = new FileOutputStream(name);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                DataOutputStream dos = new DataOutputStream(bos);
                
                dos.writeUTF(playername1);
                dos.writeUTF(playername2);
                dos.writeInt(playerscore1);
                dos.writeInt(playerscore2);
                dos.writeInt(count);
                dos.writeInt(count2);
                dos.writeInt(count3);
                for (int i = 0; i < 8; i++) {
   				   for (int j = 0; j < 8; j++) {
                     dos.writeBoolean(h[i][j]);
                     dos.writeBoolean(v[i][j]);
                     dos.writeInt(win[i][j]);
                  }
               }
               dos.flush();
               dos.close();
               
            
            }
            catch(Exception e) {
               System.out.println(e);
            }
            
         }
      });
      
      //load game, read data from saved records.
       load.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            try{
                
               FileInputStream fis = new FileInputStream(name);
               BufferedInputStream bis = new BufferedInputStream(fis);
               DataInputStream dis = new DataInputStream(bis);
               
               playername1 = dis.readUTF();
               playername2 = dis.readUTF();
               playerscore1 = dis.readInt();
               playerscore2 = dis.readInt();
               
               player1.setText(playername1 + ": " + playerscore1 + "                              ");
   			   player2.setText(playername2 + ": " + playerscore2);
               count = dis.readInt();
               count2 = dis.readInt();
               count3 = dis.readInt();
               
               for (int i = 0; i < 8; i++) {
      				for (int j = 0; j < 8; j++) {
                    h[i][j] =  dis.readBoolean();
                    v[i][j] =  dis.readBoolean();
                    win[i][j] = dis.readInt(); 
                  }
             
               invalidate();
               validate();
               repaint();
               }
            
            }
            catch(Exception e) {
               System.out.println(e);
            }                 
         }
      
      });
      
      
      
      
      
      setJMenuBar(jmb);
      setResizable(false);
      setVisible(true);
   
   }
   
   /*
    * Get winner after the game
    * by rouqi chen
    */
   public void getWinner() {
       if (playerscore1 > playerscore2) {
						JOptionPane.showMessageDialog(null,playername1 + " is the winner!");
					}
					else if (playerscore1 < playerscore2) {
						JOptionPane.showMessageDialog(null,playername2 + " is the winner!");
					}
					else if (playerscore1 == playerscore2) {
						JOptionPane.showMessageDialog(null, "The game is a tie with " + playerscore1 + " points!");
					}

         
   }
   
   /*
    * Main 
    * run game
    * by rouqi chen
    */
   public static void main(String [] args) {
      new Dots();
   }
   
   /*
    * player class
    * set player's name
    * by rouqi chen
    */
  class Player extends JPanel {

		private Font font = new Font("Serif", Font.BOLD, 30);
      private int allow=0;
      private int allow2=0;
		public Player() {
      
      try{
      
			playername1 = JOptionPane.showInputDialog("Player 1 enter your name: ");
            if(playername1.equals("")) {
            while(allow==0) {
               JOptionPane.showMessageDialog(null,"You have to enter name!");
               playername1 = JOptionPane.showInputDialog("Player 1 enter your name: ");
               if(!playername1.equals("")) {
                  allow = 1;
               }
            }  
         }
         
			playername2 = JOptionPane.showInputDialog("Player 2 enter your name: ");
         if(playername2.equals("")) {
            while(allow2==0) {
               JOptionPane.showMessageDialog(null,"You have to enter name!");
               playername2 = JOptionPane.showInputDialog("Player 2 enter your name: ");
               if(!playername2.equals("")) {
                  allow2 = 1;
               }
            }  
         }
      player1 = new JLabel(playername1+": "+playerscore1+"                              ");
		player2 = new JLabel(playername2+": "+playerscore2);
		player1.setFont(font);
		player2.setFont(font);
         
      //set gui
		add(player1);	
		add(player2);
      }//end try
      catch(NullPointerException e){
         System.exit(0);

      }//player name null if no name entered.

		}

	}
   
   /*
    * class Game
    * set up game
    * by rouqi chen
    */
   class Game extends JPanel implements Runnable {
       public Game() {
         setLayout(new GridLayout(8,8));
         for (int i = 0; i < 8; i++) {
   			for (int j = 0; j < 8; j++) {
   
   				buttonArray[i][j] = new JRadioButton();
               buttonArray[i][j].setHorizontalAlignment(SwingConstants.CENTER);
               h[i][j]=false;
               v[i][j]=false;
               win[i][j]=0;
   				add(buttonArray[i][j]);
   			}
         }  
      
    }//end class
    
    /*
     * paint 
     * connet dots 
     * count
     * by rouqi chen
     */
    public void paint(Graphics g) {

			super.paint(g);
         
			for (int i = 0; i < 8; i++)
				for (int j = 0; j < 7; j++) {
					if (buttonArray[i][j].isSelected() && buttonArray[i][j + 1].isSelected()&&h[i][j]==false) {
	               h[i][j] = true;
                  count++;
                  count3++;
						deselect();
					} else if(buttonArray[i][j].isSelected() && buttonArray[i][j + 1].isSelected()&&h[i][j]==true){
                   deselect();
                  JOptionPane.showMessageDialog(null,"You can not connect this two!");
					}
				}
            
         for (int i = 0; i < 7; i++)
				for (int j = 0; j < 8; j++) {
					if (buttonArray[i][j].isSelected() && buttonArray[i+1][j].isSelected()&&v[i][j]==false) {
	               v[i][j] = true;
                  count++;
                  count3++;
						deselect();
					}
               else if(buttonArray[i][j].isSelected() && buttonArray[i+1][j].isSelected()&&v[i][j]==true){
                   deselect();
                  JOptionPane.showMessageDialog(null,"You can not connect this two!");
					}
				}
            
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 7; j++) {
               if(h[i][j]==true){
               g.drawLine(buttonArray[i][j].getX()+55,buttonArray[i][j].getY()+38,buttonArray[i][j].getX()+168,buttonArray[i][j].getY()+38);
               }
            }
         }
         for (int i = 0; i < 7; i++) {
   			for (int j = 0; j < 8; j++) {
               if(v[i][j]==true){
                  g.drawLine(buttonArray[i][j].getX()+55,buttonArray[i][j].getY()+38,buttonArray[i][j].getX()+55,buttonArray[i][j].getY()+113);
               }  
            }
         }
        
      }//end paint

   /*
    * deselect
    * by rouqi chen
    */
   public void deselect() {

			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					buttonArray[i][j].setSelected(false);
				}

			}
     }//end deselect
   
   
   /*
    * run result for the game
    * set color for players
    * by rouqi chen
    */
    public void run() {
      while(true) {
         if(count%2==0) {
           player2.setForeground(Color.BLACK);
           player1.setForeground(Color.RED);
           count2=1;
         }
         else if(count%2==1) {
           player1.setForeground(Color.BLACK);
           player2.setForeground(Color.RED);
           count2=2;
         }
         //Current player is red
         repaint();
            for (int i = 0; i < 7; i++) {
				   for (int j = 0; j < 7; j++) {
                  if(h[i][j] == true && h[i+1][j] == true && v[i][j] == true && v[i][j+1] == true && count2==1&&win[i][j]==0) {
                     playerscore1++;
                     player1.setText(playername1 + ": " + playerscore1 + "                              ");
                     win[i][j]=1;
                     count--;
                  }
                  else if(h[i][j] == true && h[i+1][j] == true && v[i][j] == true && v[i][j+1] == true && count2==2&&win[i][j]==0) {
                     playerscore2++;
                     player2.setText(playername2 + ": " + playerscore2);
                     win[i][j]=1;
                     count--;
                  }
               //Score for player 2
               }
            }
               
            if(count3==112) {   
               //get winner 
                getWinner();  
            }              
         }
      }
   }
}//end 