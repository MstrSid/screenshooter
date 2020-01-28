import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyThread extends Thread  {
    public int threadNumber;
    long startTime = 0;
    long endTime = 0;
    long tranferTime = 0;
    String ACCESS_TOKEN = "";/*your acces token dropbox here*/

    @Override
    public void run() {
        for (; ; ) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMDD_HHmmss");
            Date date = new Date();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            BufferedImage image = null;
            try {
                startTime  = System.currentTimeMillis();
                image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            } catch (AWTException e) {
                e.printStackTrace();
            }
            try {
                ImageIO.write(image, "png", os);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("****************************************");
            System.out.println("Screenshot taken, size: " + image.getWidth() + "x" + image.getHeight());

            DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
            DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

            InputStream in = null;
            in = new ByteArrayInputStream(os.toByteArray());;
            try {
                client.files().uploadBuilder("/"+formatter.format(date)+".png")
                        .uploadAndFinish(in);
                endTime = System.currentTimeMillis();
                tranferTime = endTime - startTime;
                System.out.println(tranferTime);
            } catch (DbxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Screenshot done");
            try {
                sleep(5000-tranferTime > 0 ? 5000-tranferTime : 0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
