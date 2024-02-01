package lk.ijse.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {

    public static final List<ClientHandler> clientHandlerList = new ArrayList<>();
    private  Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String clientName;


    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
        clientName = inputStream.readUTF();
        clientHandlerList.add(this);
    }
@Override
    public void run(){
        while (socket.isConnected()){
            try {
                String message = inputStream.readUTF();
                if (message.equals("image")){
                    receiveImage();
                }else {
                    for (ClientHandler handler : clientHandlerList){
                        if (!handler.clientName.equals(clientName)){
                            handler.sendMessage(clientName,message);
                        }
                    }
                }
            } catch (IOException e) {
                clientHandlerList.remove(this);
//                throw new RuntimeException(e);
                break;
            }
        }
    }

    private void sendMessage(String clientName, String message) throws IOException {
        outputStream.writeUTF(clientName + ": " + message);
        outputStream.flush();

    }

    private void receiveImage() throws IOException {
        int size = inputStream.readInt();
        byte[] bytes = new byte[size];
        inputStream.readFully(bytes);
        for (ClientHandler handler : clientHandlerList){
            if (!handler.clientName.equals(clientName)){
                handler.sendImage(clientName,bytes);
            }
        }
    }
    private void sendImage(String clientName, byte[] bytes) throws IOException {
        outputStream.writeUTF("image");
        outputStream.writeUTF(clientName);
        outputStream.writeInt(bytes.length);
        outputStream.write(bytes);
        outputStream.flush();
    }
}

