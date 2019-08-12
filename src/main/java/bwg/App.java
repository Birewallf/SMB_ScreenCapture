package bwg;

/**
 * App in
 * @author  ssvs
 */
public class App {
    public static void main(String[] args) {
        String hostname = "127.0.0.1";
        String folder = "_Shara";
        new ScreenCaptureThread(hostname, folder).run();
    }
}
