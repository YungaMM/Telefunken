package main;

import org.javagram.TelegramApiBridge;
import view.ContactsList;
import view.MyFrame;
import view.RegistrationFrame;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class Loader {

    public static void main(String[] args) throws IOException {
        MyFrame myFrame = new MyFrame();

        myFrame.setLocationRelativeTo(null);
        myFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        myFrame.setVisible(true);

//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                runRegistration();
//            }
//        });
    }

    private static void runRegistration() {
        TelegramApiBridge apiBridge;
        do {
            apiBridge = getApiBridge();
            if (apiBridge != null) {
                RegistrationFrame registrationFrame = new RegistrationFrame(apiBridge);
                registrationFrame.setVisible(true);
            } else if (!dialogConnectServer()){
                System.exit(0);
            }
        } while (apiBridge == null);
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

    private static boolean dialogConnectServer() {
        int option = JOptionPane.showConfirmDialog(
                null,
                "Повторить подключение к серверу?",
                "Ошибка подключения к серверу Telegram",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        return option == JOptionPane.YES_OPTION;
    }

}



