package bwg;

/**
 * App in
 * @author  ssvs
 */
public class App {
    public static void main(String[] args) {
        String hostname = "192.168.0.1";
        new ScreenCaptureThread(hostname).run();
    }
}
