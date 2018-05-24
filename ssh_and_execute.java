package ssh_connect;
import com.jcraft.jsch.*;
import java.awt.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.*;

public class Exec{
	 public static void main(String[] arg){
		    
		    try{
		      JSch jsch=new JSch();

		      String host=null;
		      if(arg.length>0){
		        host=arg[0];
		      }
		      else{
		        host=JOptionPane.showInputDialog("Enter username@hostname",
		                                         System.getProperty("user.name")+
		                                         "@localhost"); 
		      }
		      String user=host.substring(0, host.indexOf('@'));
		      host=host.substring(host.indexOf('@')+1);

		      Session session=jsch.getSession(user, host, 22);

		      String passwd = JOptionPane.showInputDialog("Enter password");
		      session.setPassword(passwd);

		      UserInfo ui = new MyUserInfo(){
		        public void showMessage(String message){
		          JOptionPane.showMessageDialog(null, message);
		        }
		        public boolean promptYesNo(String message){
		          Object[] options={ "yes", "no" };
		          int foo=JOptionPane.showOptionDialog(null, 
		                                               message,
		                                               "Warning", 
		                                               JOptionPane.DEFAULT_OPTION, 
		                                               JOptionPane.WARNING_MESSAGE,
		                                               null, options, options[0]);
		          return foo==0;
		        }

		        // If password is not given before the invocation of Session#connect(),
		        // implement also following methods,
		        //   * UserInfo#getPassword(),
		        //   * UserInfo#promptPassword(String message) and
		        //   * UIKeyboardInteractive#promptKeyboardInteractive()

		      };

		      session.setUserInfo(ui);

		      // It must not be recommended, but if you want to skip host-key check,
		      // invoke following,
		      // session.setConfig("StrictHostKeyChecking", "no");

		      //session.connect();
		      session.connect(30000);   // making a connection with timeout.

		      Channel channel=session.openChannel("shell");
		      
			  OutputStream ops = channel.getOutputStream();
		            PrintStream ps = new PrintStream(ops, true);

					channel.connect();
		             ps.println("ls"); 
		             ps.println("cd ..");
					ps.println("ls"); 
					ps.println("sudo su - user");
					ps.println("whoami");			
					//ps.println("cp"+path+" "+region_source);
                 ps.close();

		             InputStream in=channel.getInputStream();
		             byte[] bt=new byte[1024];


		             while(true)
		             {

		             while(in.available()>0)
		             {
		             int i=in.read(bt, 0, 1024);
		             if(i<0)
		              break;
		                String str=new String(bt, 0, i);
		              //displays the output of the command executed.
		                System.out.print(str);


		             }
		             if(channel.isClosed())
		             {

		                 break;
		            }
		             Thread.sleep(1000);
		             channel.disconnect();
		             session.disconnect();   
		             }

		    }
		    catch(Exception e){
		      System.out.println(e);
		    }
		  }

		  public static abstract class MyUserInfo
		                          implements UserInfo, UIKeyboardInteractive{
		    public String getPassword(){ return null; }
		    public boolean promptYesNo(String str){ return false; }
		    public String getPassphrase(){ return null; }
		    public boolean promptPassphrase(String message){ return false; }
		    public boolean promptPassword(String message){ return false; }
		    public void showMessage(String message){ }
		    public String[] promptKeyboardInteractive(String destination,
		                                              String name,
		                                              String instruction,
		                                              String[] prompt,
		                                              boolean[] echo){
		      return null;
		    }
		  }
}