package Server;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {

    protected BlockingQueue<BufferedImage> queue = null;
    protected Socket socket = null;

    public Producer(BlockingQueue queue, Socket socket) {
        this.queue = queue;
        this.socket = socket;
    }

    @Override
    public void run() {
        //初始化截图器
        Screenshot ss = new Screenshot();
        while (true) {
            try {
                if(socket.isClosed()) {
                    // 如果连接已经关闭，退出线程，此时已不需要截图
                    break;
                }
                // 获取一帧图像放入共享队列中
                queue.put(ss.imageGet());
                Thread.sleep(50);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
