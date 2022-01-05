package Server;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

public class Main {

    // 初始化截图器
    static Screenshot ss = new Screenshot();

    public static void main(String[] args) throws Exception {
        // 服务端监听socket初始化
        ServerSocket serverSocket = new ServerSocket(12345);
        while(true){
            // 阻塞等待客户端连接
            Socket clientSocket = serverSocket.accept();
            System.out.println(clientSocket.getInetAddress()+"/"+clientSocket.getPort());
            // 生产者-消费者共享队列初始化
            BlockingQueue<BufferedImage> queue = new ArrayBlockingQueue<>(1024);
            Producer producer = new Producer(queue, clientSocket);
            Consumer consumer = new Consumer(queue, clientSocket);

            Thread t1 = new Thread(producer);
            Thread t2 = new Thread(consumer);
            // 开启生产者-消费者线程
            t1.start();
            t2.start();

            t2.join();
            clientSocket.close();
        }
    }
}
