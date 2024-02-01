package lk.ijse.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import lk.ijse.controller.ChatWallFormController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.RemoteException;

public class Client implements Runnable, Serializable {

    private String name;
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private ChatWallFormController chatWallFormController;

    public Client(String name) throws IOException {
        this.name = name;

        socket = new Socket("localhost", 3201);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());

        outputStream.writeUTF(name);
        outputStream.flush();
    }
    public void run(){
        try {
            outputStream.writeUTF("New Member Joined To Chat");
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (true){
            try {
                String message = inputStream.readUTF();
                if (message.equals("image")){
                    receiveImage();
                }else {
                    chatWallFormController.writeMessage(message);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void receiveImage() throws IOException {
        String utf = inputStream.readUTF();
        int size = inputStream.readInt();
        byte[] bytes = new byte[size];
        inputStream.readFully(bytes);
        chatWallFormController.setImage(bytes,utf);
    }

    public String getName() {
        return name;
    }
}
