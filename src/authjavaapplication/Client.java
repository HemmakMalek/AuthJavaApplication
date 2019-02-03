/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package authjavaapplication;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;

public class Client {
    
    private final int PORT = 9888;
    private final String HOSTNAME = "localhost";
    
    Socket socket;
    BufferedReader read;
    PrintWriter output;
    
    public void startClient() throws UnknownHostException, IOException{
        // Connecting to the Server.
        socket = new Socket(HOSTNAME, PORT);

        output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        
        // Prompt to Enter Username/Password Combination.
        String username = JOptionPane.showInputDialog(null, "Enter Username");

        output.println(username);

        String password = JOptionPane.showInputDialog(null, "Enter Password");

        output.println(password);
        output.flush();

        // Reading Server Response.
        read = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String response = read.readLine();
        System.out.println("Server response: " + response);

        JOptionPane.showMessageDialog(null, response);
    }

    public static void main(String args[]){
        Client client = new Client();
        try {
            client.startClient();
        } catch (UnknownHostException e) {

        } catch (IOException e) {
                
        }
    }
}
