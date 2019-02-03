/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package authjavaapplication;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author malek
 */
public class Server {

    private static final String DBURL = "jdbc:derby://localhost:1527/AuthDB;create=true";
    private static final String DBUSER = "userdb";
    private static final String DBPASS = "passdb";

    private final int PORT = 9888;
    private final String HOSTNAME = "localhost";
    
    private static Connection conn = null;
    private static Statement stmt = null;
    
    ServerSocket serversocket;
    Socket client;
    int bytesRead;
    BufferedReader input;
    PrintWriter output;

    public void start() throws IOException {
        System.out.println("Connection Starting on: " + PORT);
        // Opening a Socket in the Server Side.
        serversocket = new ServerSocket(PORT);

        // Waiting for connection from client.
        client = serversocket.accept();

        System.out.println("Waiting for Client");
        // Here the server has recived a Message. Starting Login.
            try {
                Login();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private static void createConnection() {
        // Creating a Connection to Derby Database
        try {
            conn = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Database Connection Created");

    }

    public void Login() throws Exception {
        // Reading Client Input
        input = new BufferedReader(new InputStreamReader(client.getInputStream()));

        String username = input.readLine();
        System.out.println("Username input : " + username);
        String password = input.readLine();
        System.out.println("Password input : " + password);
        
        output = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
        
        // Executing a query to Get Username/password Comb. From database.
        stmt = conn.createStatement();

        ResultSet rs;
        rs = stmt.executeQuery("SELECT USERNAME,PASSWORD FROM USERS WHERE USERNAME = '"+username+"'");

        // Verifying the Username/password and Returning the result.
        if (rs.next()) {
            if (username.equals(rs.getString("USERNAME")) && password.equals(rs.getString("PASSWORD"))) {
                               output.println("Welcome, " + username);
            } else {
                output.println("Login Failed");
            }
        }
        output.flush();
        output.close();

    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            createConnection();
            server.start();
        } catch (IOException e) {

        }
    }

}
