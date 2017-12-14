import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
 
public class Server {
   public static void main(String[] args) {
      try {
         ServerSocket ss = new ServerSocket(8888);
         System.out.println("opening the Server....");
         Socket s = ss.accept();
         System.out.println("Client:"+s.getInetAddress().getLocalHost()+"have connected to the Server");
         
         BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
         //read the message from the client
         String UserName = br.readLine();
         String PassWord = br.readLine();

         System.out.println("Name:"+UserName);
         System.out.println("PassWord:"+PassWord);



         BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
         bw.write("OK\n");
         bw.flush();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}