package Server;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Screenshot {
    static Robot robot;
    static Dimension screenSize;
    static Rectangle captureRect;

    Screenshot()
    {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        //设定截图参数
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        captureRect = new Rectangle(100, 50,
                screenSize.width - 200, screenSize.height - 100);
    }

    public BufferedImage imageGet() throws IOException {
        // 生成一帧图像
        return robot.createScreenCapture(captureRect);
    }
}
