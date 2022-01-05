package Server;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {
    final static String key = "1243CFEBD819AA6B1C717DE870459F7B";

    protected BlockingQueue<BufferedImage> queue = null;
    protected Socket socket = null;

    public Consumer(BlockingQueue queue, Socket socket) {
        this.queue = queue;
        this.socket = socket;
    }

    @Override
    public void run() {
        //创建加密器
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = null;

        try {
            // 初始化AES加密参数
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }

        try {
            DataOutputStream dos = new DataOutputStream(
                    new BufferedOutputStream(socket.getOutputStream()));
            while (true) {
                // 从共享队列中取出一帧图像
                BufferedImage img = queue.take();
                // 将图像写入二进制输出流
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(img, "jpg", byteArrayOutputStream);
                // 使用加密器，得到加密后的数据
                byte[] imgdata = cipher.doFinal(
                        byteArrayOutputStream.toByteArray());
                //先发送数据长度,再发送数据
                dos.writeInt(imgdata.length);
                dos.write(imgdata);
            }
        } catch (SocketException s)
        {
            System.out.println("Client disconnected.");
        }
        catch (IOException | BadPaddingException | IllegalBlockSizeException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
