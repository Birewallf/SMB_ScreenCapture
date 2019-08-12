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

public class ScreenCaptureThread implements Runnable {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

    private static boolean TRANSLATIONFLAG = true;
    private static Robot robot;
    private String remotehostname;
    private String folder;

    ScreenCaptureThread(String remotehostname, String folder){
        logger.info("ScreenCaptureThread init");
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        this.remotehostname = remotehostname;
        this.folder = folder;
    }

    @Override
    public void run() {

        logger.info("Capture thread init start");

        /// Get host name
        String hostname;
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
            logger.info("Create directory: " + "\\\\" + remotehostname +"\\"+folder+"\\" + hostname);
            new File("\\\\" + remotehostname +"\\"+folder+"\\" + hostname).mkdir();
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
                                + "\\"
                                +folder
                                +"\\"
                                + hostname
                                +"\\"
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
}
