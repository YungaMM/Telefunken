package main;

import gui.BasicFrame;
import org.javagram.TelegramApiBridge;
import org.javagram.dao.ApiBridgeTelegramDAO;
import org.javagram.dao.DebugTelegramDAO;
import org.javagram.dao.TelegramDAO;
import resources.Config;

import javax.swing.*;
import java.io.IOException;

public class Loader {

    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if(info.getName().equals("Nimbus")) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }
                    //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    TelegramDAO telegramDAO =  new ApiBridgeTelegramDAO(
                            Config.SERVER, Config.APP_ID, Config.APP_HASH);
                    new DebugTelegramDAO();
                    BasicFrame frame = new BasicFrame(telegramDAO);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        });
    }

//    private static void startRegistration() {
//        TelegramApiBridge apiBridge;
//        apiBridge = getApiBridge();
//        if (apiBridge != null) {
//            BasicFrame basicFrame = new BasicFrame(apiBridge);
////            basicFrame.setLocationRelativeTo(null);
//            basicFrame.setVisible(true);
//            basicFrame.showPhonePanel();
//        }
//    }
//
//    private static TelegramApiBridge getApiBridge() {
//        try {
//            return new TelegramApiBridge("149.154.167.50:443",
//                    36025, "5530d508831b42dc0df01f9b31c2978b");
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}