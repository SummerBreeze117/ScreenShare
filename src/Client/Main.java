package Client;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.*;

public class Main {

    final static String key = "1243CFEBD819AA6B1C717DE870459F7B";

    public static void main(String[] args) throws Exception {
        //套接字初始化
        Socket socket = new Socket("localhost", 12345);

        JFrame frame = new JFrame("屏幕共享");

        //初始化AES解密器
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        DataInputStream dis = new DataInputStream(
                new BufferedInputStream(socket.getInputStream()));
        long startTime = System.currentTimeMillis();
        int cnt = 0;
        while (true) {
            // 读取该帧长度
            int len;
            try {
                len = dis.readInt();
            } catch (SocketException s) {
                System.out.println("Server closed.");
                break;
            }
            // 预分配该帧空间
            byte[] imgdata = new byte[len];
            //System.out.println("len:" + len);
            // 读取该帧数据
            try {
                dis.readFully(imgdata);
            } catch (SocketException s) {
                System.out.println("Server closed.");
                break;
            }
            // 解密该帧数据，得到图片对象
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(cipher.doFinal(imgdata));
            BufferedImage img = ImageIO.read(byteArrayInputStream);
            // 在前端刷新该帧图像
            if(img != null){
                JLabel jLabel = new JLabel(new ImageIcon(img));
                frame.getContentPane().add(jLabel);
                frame.pack();
                frame.setVisible(true);
                frame.remove(jLabel);
                cnt ++;
            }
            long endTime = System.currentTimeMillis();
            System.out.println("fps:" + (double)cnt / (double)(endTime - startTime) * 1000);
        }
    }
}

