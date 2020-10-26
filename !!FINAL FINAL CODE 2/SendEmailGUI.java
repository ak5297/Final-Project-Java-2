/**
 * @authors Rachael Simmonds, Ethan Marschean, Alex Ketavongsa, 
 * Fernando Rodriguez, and Rouqi Chen 
 * @version 20190411
 * Prof. Patric
 * ISTE 121
**/

/* 
 * Imports 
 */
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.activation.*;
import java.util.*;
import java.util.regex.Matcher; 
import java.util.regex.Pattern;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;


/* 
 * The SendEmailGUI class instantiates the components needed to create the JFrame.
 * The JTextField and JButton let the user enter the destination email and attach files.
 * Written by Ethan  
 */    
public class SendEmailGUI extends JFrame implements ActionListener {
   //JLabels
   JLabel jlsenderEmail = new JLabel("Sender Email:");
   JLabel jlreceiverEmail = new JLabel("Receiver Email:");
   //Button
   JButton jbEmailSend = new JButton("Send Email");
   //Text Fields
   JTextField jtfSEmail = new JTextField(20);
   JTextField jtfREmail = new JTextField(20);
   //JPanels
   JPanel jpCent = new JPanel();
   
  /* 
   * The SendEmailGUI constructor adds the components to the JFrame.
   * Written by Ethan
   */
   public SendEmailGUI(){
      jpCent.setLayout(new FlowLayout());
      jtfSEmail.setText("iste121project@gmail.com");
      jtfSEmail.setEnabled(false);
      jpCent.add(jlsenderEmail);
      jpCent.add(jtfSEmail);
      jpCent.add(jlreceiverEmail);
      jpCent.add(jtfREmail);
      jpCent.add(jbEmailSend);
      add(jpCent, BorderLayout.CENTER);
      
      setSize(260,170);
      setDefaultCloseOperation(HIDE_ON_CLOSE);
      setTitle("From !Failure");
      setVisible(true);
      setLocationRelativeTo(null);
      jbEmailSend.addActionListener(this);
   }
   
  /* 
   * The action listener checks for a valid email in the receiver email field, it uses regex and checks if
   * the entry contains invalid characters or more than one symbol (@ or .) consecutively before sending the email.
   * Written by Ethan 
   */
   public void actionPerformed(ActionEvent ae) {
   
      String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                            "[a-zA-Z0-9_+&*-]+)*@" + 
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                            "A-Z]{2,7}$"; 
                            
      Pattern pat = Pattern.compile(emailRegex); 

      if(pat.matcher(jtfREmail.getText()).matches()){
         String receiverEmail = jtfREmail.getText();
         emailDoingMethod(receiverEmail);
      }
      else{
         JFrame Errorframe = new JFrame();
         JOptionPane.showMessageDialog(Errorframe, "Please Enter a Vaild Email.");
      }
   }
   
   
  /* 
   * This method does the backend processing and communication between the GUI and email server.
   * Written by Alex
   */
   public void emailDoingMethod(String receiverEmail){
   
      //Email account information
      final String username = "iste121project@gmail.com";
      final String password = "tigers121";
      
      //Email server information
      Properties prop = new Properties();
      prop.put("mail.smtp.host", "smtp.gmail.com");
      prop.put("mail.smtp.port", "587");
      prop.put("mail.smtp.auth", "true");
      prop.put("mail.smtp.starttls.enable", "true");
   
      //Get the session object - authenticates if the email/password of sending account is correct
      Session session = Session.getInstance(prop, 
         new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(username, password);
            }
         });
     
      //Creates a MimeMessage - which sets the from/to email address, subject line, and body text
      try {
         Message message = new MimeMessage(session);
         message.setFrom(new InternetAddress("iste121project@gmail.com"));
         message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse((String)receiverEmail));
            
         message.setSubject("Message from Team !Failure");
          
         //JFileChooser gives the option to choose a file from the computer and attach it to email                  
         JFileChooser jfc = new JFileChooser();
         int returnValue = jfc.showOpenDialog(null);
         
         //If JFileChooser is selected, then add the attachment to the message body part 
         if(returnValue == JFileChooser.APPROVE_OPTION){
 
            Multipart multipart = new MimeMultipart();         
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Hello, Here is your message. Thank you for using our program!");
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            DataSource sauce = new FileDataSource(jfc.getSelectedFile());
            messageBodyPart.setDataHandler(new DataHandler(sauce));
            messageBodyPart.setFileName(jfc.getSelectedFile().getName());
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            
            //Send the email
            Transport.send(message);
            JFrame Sentframe = new JFrame();
            JOptionPane.showMessageDialog(Sentframe, "Email Sent.");
         }
         
         //If JFileChooser 'cancel' button is clicked, then close the window and print error 
         if(returnValue == JFileChooser.CANCEL_OPTION){
         JOptionPane.showMessageDialog(null, "You did not choose a file!");
         
         //If an unexpected error occurs, print out an error message
         }
         if(returnValue == JFileChooser.ERROR_OPTION){
            JOptionPane.showMessageDialog(null, "There was an Error with the JFileChooser");
            setVisible(false);
         }
         
      } catch (MessagingException e) {
         e.printStackTrace();
      } //end of catch block
   } //end of emailDoingMethod
} //end of SendEmailGUI class