package main;

import org.javagram.TelegramApiBridge;

import javax.swing.*;
import java.io.IOException;

public class Loader {

    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                startRegistration();
            }
        });
    }

    private static void startRegistration() {
        TelegramApiBridge apiBridge;
        apiBridge = getApiBridge();
        if (apiBridge != null) {
            BasicFrame basicFrame = new BasicFrame(apiBridge);
            basicFrame.setLocationRelativeTo(null);
            basicFrame.setVisible(true);
            basicFrame.showStartPanel();
        }
    }

    private static TelegramApiBridge getApiBridge() {
        try {
            return new TelegramApiBridge("149.154.167.50:443",
                    36025, "5530d508831b42dc0df01f9b31c2978b");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}