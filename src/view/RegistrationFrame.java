package view;

import org.javagram.TelegramApiBridge;
import org.javagram.response.AuthCheckedPhone;
import org.telegram.api.TLAbsUserStatus;
import org.telegram.api.TLUserSelf;
import org.telegram.api.TLUserStatusEmpty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class RegistrationFrame extends JFrame {
    private static final int FRAME_HEIGHT = 800;
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_MIN_HEIGHT = 200;
    private static final int FRAME_MIN_WIDTH = 350;

    private static final int YOU_CAN_SWITCH = 0;
    private static final int YOU_CAN_SWITCH_FOR_REGISTRATION = 1;
    private static final int YOU_MUST_REQUEST_FOCUS = 2;

    private static final String FRAME_TITLE = "Telefunken";

    private EnterPhone enterPhonePanel = new EnterPhone();
    private EnterConfirmationCode confirmCodePanel = new EnterConfirmationCode();
    private Registration registrationPanel = new Registration();

    private TelegramApiBridge apiBridge;
    private TLUserSelf userSelf = new TLUserSelf();

    public RegistrationFrame(TelegramApiBridge apiBridge) throws HeadlessException {
        this.apiBridge = apiBridge;
        setContentPanel(enterPhonePanel);
        setListeners();
        createFrame();
    }

    private void setListeners() {
        enterPhonePanel.addListenerForChangeForm(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    switchPhoneToCode();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        confirmCodePanel.addListenerForChangeForm(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    switchCodeToRegistration();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        registrationPanel.addListenerForChangeForm(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endOfRegistration();
            }
        });
    }

    private void switchPhoneToCode() throws IOException {
        String phone = enterPhonePanel.getPhone();
        int status = canSwitchPhoneToCode();

        if (status == YOU_CAN_SWITCH || status == YOU_CAN_SWITCH_FOR_REGISTRATION) {
            confirmCodePanel.setPhoneLabel(phone);
            setContentPanel(confirmCodePanel);
            confirmCodePanel.transferFocusToCode();

            apiBridge.authSendCode(phone);
            userSelf.setPhone(phone);
            userSelf.setStatus(new TLAbsUserStatus() {
                @Override
                public int getClassId() {
                    return status;
                }
            });
        } else if (status == YOU_MUST_REQUEST_FOCUS) {
            enterPhonePanel.transferFocusToPhone();
        }
    }

    private void switchCodeToRegistration() throws IOException {
        char[] smsCode = confirmCodePanel.getCodeField();
        int status = canSwitchPhoneToCode();
        if (status == YOU_CAN_SWITCH) {
            setContentPanel(registrationPanel);
        } else if (status == YOU_MUST_REQUEST_FOCUS) {
            confirmCodePanel.transferFocusToCode();
        }
    }

    private void endOfRegistration() {

    }

    private int canSwitchPhoneToCode() throws IOException {
        String phone = enterPhonePanel.getPhone();
        if (phone == null) {
            dialogErrorTextField();
            return YOU_MUST_REQUEST_FOCUS;
        } else
            return phoneIsRegistered(enterPhonePanel.getFormattedPhone());
    }

    private int phoneIsRegistered(String phone) {
        AuthCheckedPhone checkedPhone;
        try {
            checkedPhone = apiBridge.authCheckPhone(phone);
        } catch (IOException e1) {
            e1.printStackTrace();
            if (e1.getMessage().equals("PHONE_NUMBER_INVALID")) {
                dialogErrorTextField();
            }
            return YOU_MUST_REQUEST_FOCUS;
        }

        if (checkedPhone.isRegistered()) {
            return YOU_CAN_SWITCH;
        } else if (dialogSignUp()) {
            return YOU_CAN_SWITCH_FOR_REGISTRATION;
        } else
            return YOU_MUST_REQUEST_FOCUS;
    }

    private void dialogErrorTextField() {
        JOptionPane.showMessageDialog(
                RegistrationFrame.this,
                "Пожалуйста, корректно заполните поле!",
                "Внимание!",
                JOptionPane.ERROR_MESSAGE);
    }

    private boolean dialogSignUp() {
        int option = JOptionPane.showConfirmDialog(
                this.getContentPane(),
                "Для продолжения работы необходимо пройти регистрацию!",
                "Телефонный номер не зарегестрирован!",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        return option == JOptionPane.YES_OPTION;
    }

    private void setContentPanel(Container panel) {
        setContentPane(panel);
        revalidate();
        repaint();
    }

    private void createFrame() {
        setTitle(FRAME_TITLE);
        setMinimumSize(new Dimension(FRAME_MIN_WIDTH, FRAME_MIN_HEIGHT));
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
