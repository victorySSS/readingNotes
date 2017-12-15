import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.PrintWriter;

 
public class Server {
    
    private static final int PORT = 8888;
    private List<Socket> mList = new ArrayList<Socket>();
    private ServerSocket server = null;
    private ExecutorService myExecutorService = null;
    
    
    public static void main(String[] args) {
        new Server();
    }

    public Server()
    {
        try
        {
            server = new ServerSocket(PORT);
            
            myExecutorService = Executors.newCachedThreadPool();
            System.out.println("Server Is Running...\n");
            Socket client = null;
            while(true)
            {
                client = server.accept();
                mList.add(client);
                myExecutorService.execute(new Service(client));
            }
            
        }catch(Exception e){e.printStackTrace();}
    }
    




    //------------------------------------------------------------
    class Service implements Runnable
    {
        private Socket socket;
        private BufferedReader in = null;
        private String msg = "";
        private String loginName;
        private String loginPassword;
        
        public Service(Socket socket) {
            this.socket = socket;
            try
            {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 msg = "User:" +this.socket.getInetAddress() + "~join the APP "  
                            +"Online nubmers:" +mList.size();  
                System.out.println(msg);
                //this.sendmsg();
            }catch(IOException e){e.printStackTrace();}
        }
        
        
        
        @Override
        public void run() {
            try{
                while(true)
                {
                    msg=in.readLine();
                    
                    System.out.println(msg);
                    // System.out.println(loginName);
                    // System.out.println(loginPassword);
                    
                    
                    
                    
                    if(msg!=null)
                    {
                        if(msg.equals("bye"))
                        {
                            
                            System.out.println("~~~~~~~~~~~~");
                            mList.remove(socket);
                            in.close();
                            System.out.println ( "User:" + socket.getInetAddress()  
                                    + " quit" +"Online nums："+mList.size());  
                            socket.close();  
                            //this.sendmsg();  
                            break;
                        }
                        else if(msg.equals("login")){
                            loginName = in.readLine();
                            loginPassword = in.readLine();
                            System.out.println(loginName);
                            System.out.println(loginPassword);
                            if(loginName.equals("zcc")&&loginPassword.equals("zxcv")){
                                this.sendmsg("OK");
                                System.out.println("OK");
                            }
                            else{ 
                              //this.sendmsg(); 
                               System.out.println(socket.getInetAddress() + "   :wrong" );
                               this.sendmsg("Wrong");
                            }
                        }
                        else if(msg.equals("register")){
                            loginName = in.readLine();
                            loginPassword = in.readLine();
                        }
                    }

                    
                }
            }catch(Exception e){e.printStackTrace();}
        }
        
        //send message to client
        public void sendmsg(String words)
        {
            
                PrintWriter pout = null;  
                try {  
                    pout = new PrintWriter(new BufferedWriter(  
                            new OutputStreamWriter(socket.getOutputStream(),"UTF-8")),true);  
                    pout.println(words);  
                    pout.flush();
                }catch (IOException e) {e.printStackTrace();}  
            
        }
        
    }
}
   
