package main;

import model.UserAuthorization;
import org.javagram.TelegramApiBridge;
import view.RegistrationFrame;

import javax.swing.*;
import java.io.IOException;

public class Loader {

    public static void main(String[] args) throws IOException {
        TelegramApiBridge apiBridge = new TelegramApiBridge("149.154.167.50:443",
                36025, "5530d508831b42dc0df01f9b31c2978b");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                RegistrationFrame registrationFrame = new RegistrationFrame(apiBridge);
                registrationFrame.setVisible(true);
            }
        });

//        UserAuthorization authorization = new UserAuthorization();
//        authorization.userAuthorization(apiBridge);
    }
}



