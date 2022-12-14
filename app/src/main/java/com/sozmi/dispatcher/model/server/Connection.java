package com.sozmi.dispatcher.model.server;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;


/**
 * Класс, выполняющий взаимодействие с сервером
 */
public class Connection {
    private static final int BUFFER_SIZE = 4096;
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private final String LOG_TAG = "SOCKET";
    private final InetSocketAddress socketAddress;

    /**
     * Конструктор с хостом и портом
     *
     * @param host ip адрес
     * @param port номер порта
     */
    public Connection(String host, int port) {
        this.socketAddress = new InetSocketAddress(host, port);
    }


    private void connect() throws NetworkException {
        try {
            if (socket == null || socket.isClosed()) {
                Log.d(LOG_TAG, "Соединение с сокетом:" + socketAddress + " устанавливается");
                socket = new Socket(socketAddress.getAddress(), socketAddress.getPort());
                Log.d(LOG_TAG, "Соединение с сокетом:" + socketAddress + " установлено");
                out = new PrintWriter(socket.getOutputStream());
                Log.d(LOG_TAG, "Буффер для отправки данных создан");
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Log.d(LOG_TAG, "Буффер для получения данных создан");
                if (!socket.isConnected()) {
                    throw new NetworkException("Не удалось установить соединение с сервером. Пожалуйста, попробуйте позже");
                }
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
            throw new NetworkException("Не удалось установить соединение с сервером. Пожалуйста, попробуйте позже");

        }
    }

    public void disconnect() {
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

    public void sendData(String message) throws NetworkException {
        if (message != null) {
            connect();
            out.write(message);
            out.flush();
            Log.d(LOG_TAG, "Отправлено сообщение:" + message);

        }
    }

    public boolean isReady() throws IOException {
        return in.ready();
    }

    public String getData() throws NetworkException {
        Log.d(LOG_TAG, "Начало чтения данных из буффера");
        StringBuilder message = new StringBuilder();
        int charsRead;
        char[] buffer = new char[BUFFER_SIZE];
        int i = 0;
        try {


            while (!isReady()) {
                if (i >= 60)
                    throw new NetworkException("Не удалось соединиться с сервером. Превышено время ожидания. ");
                Log.d("SOCKET", "await data" + i + "s");
                Thread.sleep(1000);
                i++;
            }
            while ((charsRead = in.read(buffer)) != -1) {
                Log.d(LOG_TAG, "charsRead:" + charsRead);
                message.append(new String(buffer).substring(0, charsRead));
                Log.d(LOG_TAG, "Получены данные:" + message);
                if (!in.ready()) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new NetworkException("Не удалось прочитать полученные данные. Пожалуйста попробуйте позже.");
        } catch (InterruptedException e) {
            throw new NetworkException("Произошла ошибка потока. Пожалуйста, попробуйте позже.");
        }
        return message.toString().trim();
    }
}