package bwg;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * App in
 * @author  ssvs
 *
 * //logger.info("Create directory: " + "\\\\" + remotehostname +"\\_Shara\\_devops\\screenscap\\" + hostname);
 * //new File("\\\\" + remotehostname +"\\_Shara\\_devops\\screenscap\\" + hostname).mkdir();
 */
public class App {
    private static boolean TRANSLATIONFLAG = true;
    private static Robot robot;
    private static String remotehostname;

    public static void main(String[] args) {
        String hostname = "192.168.0.1";
        new App(hostname);
    }

    private App(String hostname){
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        App.remotehostname = hostname;
        process();
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.info("App init");
    }

    private void process(){
        new Thread(new Runnable() {
            private Logger logger = Logger.getLogger(this.getClass().getName());
            private Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
            @Override
            public void run() {
                logger.info("Capture thread init start");

                /// Get host name
                String hostname = "unknown";
                try
                {
                    InetAddress inetAddress;
                    inetAddress = InetAddress.getLocalHost();
                    hostname = inetAddress.getHostName();
                    logger.info("Hostname: " + hostname);
                }
                catch (UnknownHostException ex)
                {
                    System.out.println("Hostname can not be resolved");
                    return;
                }

                /// Create directory on remote host
                try {
                    logger.info("Create directory: " + "\\\\" + remotehostname +"\\screencap\\" + hostname);
                    new File("\\\\" + remotehostname +"\\screencap\\" + hostname).mkdir();
                } catch (Exception er){
                    logger.warning("Exception");
                    return;
                }

                /// Start capture circle
                int iterator = 0;
                while (TRANSLATIONFLAG) {
                    /// get screen
                    BufferedImage bufferedImage = robot.createScreenCapture(new Rectangle(size));
                    try {
                        Date date = new Date();
                        SimpleDateFormat formatForDateNow = new SimpleDateFormat("MM.dd.yyyy hh.mm.ss");
                        /// save file
                        File outputFile = new File(
                                "\\\\"
                                        + remotehostname
                                        + "\\screencap\\"
                                        + hostname +"\\"
                                        + formatForDateNow.format(date)
                                        + " scap-"
                                        + iterator
                                        + ".png");
                        boolean ignore = ImageIO.write(bufferedImage, "png", outputFile);
                    } catch (IOException e) {
                        logger.warning("IOException");
                    }
                    try {
                        Thread.sleep(15000);
                    } catch (InterruptedException e) {
                        logger.warning("InterruptedException");
                    }

                    /// 2 day circle
                    iterator ++;
                    /*iterator ++;
                    if (iterator > 11520)
                        iterator = 0;*/
                    if (!TRANSLATIONFLAG)
                        break;
                }
            }
        }).start();
    }
}
