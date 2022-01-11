package Server;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Screenshot {
    private static Robot robot = null;
    private static Dimension screenSize;
    private static Rectangle captureRect;
    static {
        try {
            robot = new Robot();
            screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            captureRect = new Rectangle(100, 50,
                    screenSize.width - 200, screenSize.height - 100);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

//    public static setcaptureRect(int x, int y)
//    {
////        captureRect = new Rectangle(100, 50,
////                screenSize.width - 200, screenSize.height - 100);
//    }

    public static BufferedImage imageGet() throws IOException {
        // 生成一帧图像
        return robot.createScreenCapture(captureRect);
    }
}
