package com.sozmi.dispatcher.model.server;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Connection {
    /**
     * Maximum size of buffer
     */
    public static final int BUFFER_SIZE = 128;
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private final String LOG_TAG = "SOCKET";
    private InetSocketAddress socketAddress = null;

    /**
     * Constructor with Host, Port and MAC Address
     *
     * @param host ip адрес
     * @param port номер порта
     */
    public Connection(String host, int port) {
        this.socketAddress = new InetSocketAddress(host, port);
    }

    private void connectWithServer() {
        try {
            if (socket == null || socket.isClosed()) {
                Log.d(LOG_TAG, "Соединение с сокетом:" + socketAddress.toString() + " устанавливается");
                socket = new Socket(socketAddress.getAddress(), socketAddress.getPort());
                Log.d(LOG_TAG, "Соединение с сокетом:" + socketAddress.toString() + " установлено");
                out = new PrintWriter(socket.getOutputStream());
                Log.d(LOG_TAG, "Буффер для отправки данных создан");
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Log.d(LOG_TAG, "Буффер для получения данных создан");
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    public void disConnectWithServer() {
        Log.d(LOG_TAG, "Начало закрытия сокета");
        Log.d(LOG_TAG, "socket!=null:" + (socket != null));
        if (socket != null) {

            if (socket.isConnected()) {
                try {
                    in.close();
                    out.close();
                    socket.close();
                    Log.d(LOG_TAG, "Сокет закрыт");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, e.getMessage());
                }
            }
        }
    }

    public void sendData(String message) {
        if (message != null) {
            connectWithServer();
            out.write(message);
            out.flush();
            Log.d(LOG_TAG, "Отправлено сообщение:" + message);
        }
    }

public boolean isReady() throws IOException {
        return in.ready();
}
    public String getData() throws IOException {
        Log.d(LOG_TAG, "Начало чтения данных из буффера");
        StringBuilder message = new StringBuilder();
        int charsRead;
        char[] buffer = new char[BUFFER_SIZE];

        while ((charsRead = in.read(buffer)) != -1) {
            Log.d(LOG_TAG, "charsRead:" + charsRead);
            message.append(new String(buffer).substring(0, charsRead));
            Log.d(LOG_TAG, "Получены данные:" + message);
            if (!in.ready()) {
                break;
            }
        }

        disConnectWithServer(); // disconnect server

        return message.toString();
    }
}