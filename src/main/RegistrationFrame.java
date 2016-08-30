package main;

import org.javagram.TelegramApiBridge;
import org.javagram.response.AuthCheckedPhone;
import org.javagram.response.object.User;
import org.javagram.response.object.UserContact;
import view.ContactsList;
import view.EnterConfirmationCode;
import view.EnterPhone;
import view.Registration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class RegistrationFrame extends JFrame {
    private static final int FRAME_HEIGHT = 800;
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_MIN_HEIGHT = 200;
    private static final int FRAME_MIN_WIDTH = 350;

    private static final int YOU_CAN_SWITCH = 1;
    private static final int YOU_CAN_SWITCH_FOR_REGISTRATION = 2;
    private static final int YOU_MUST_REQUEST_FOCUS = 0;

    private static final String FRAME_TITLE = "Telefunken";
    private static final String PHONE_NUMBER_INVALID = "Неверно введен номер телефона!";

    private EnterPhone enterPhonePanel = new EnterPhone();
    private EnterConfirmationCode confirmCodePanel = new EnterConfirmationCode();
    private Registration registrationPanel = new Registration();

    private TelegramApiBridge apiBridge;
    private User user;

    private boolean userRegistered;

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
                switchPhoneToCode();
            }
        });

        confirmCodePanel.addListenerForChangeForm(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchCodeToRegistration();
            }
        });

        registrationPanel.addListenerForChangeForm(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endOfRegistration();
            }
        });
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

    //***********************************************************************
    private void switchPhoneToCode() {
        String phone = enterPhonePanel.getPhone();

        if (phone == null) {
            showErrorMessage(PHONE_NUMBER_INVALID);
            enterPhonePanel.transferFocusToPhone();
        } else {
            try {
                AuthCheckedPhone checkedPhone = apiBridge.authCheckPhone(phone);
                if (checkedPhone.isRegistered()) {
                    userRegistered = true;
                    openConfirmCodePanel(phone);
                    sendCode(phone);
                } else {
                    if (dialogSignUp(phone)) {
                        userRegistered = false;
                        openConfirmCodePanel(phone);
                        sendCode(phone);
                    } else {
                        enterPhonePanel.transferFocusToPhone();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                showErrorMessage(textError(e));
                enterPhonePanel.transferFocusToPhone();
            }
        }
    }

    private void openConfirmCodePanel(String phone) {
        confirmCodePanel.setPhoneLabel(phone);
        setContentPanel(confirmCodePanel);
        confirmCodePanel.transferFocusToCode();
    }

    private void switchCodeToRegistration() {
        if (userRegistered) {
            try {
                String smsCode = confirmCodePanel.getCode();
                user = apiBridge.authSignIn(smsCode).getUser();
                endOfAuthorisation();
            } catch (IOException e) {
                e.printStackTrace();
                showErrorMessage(textError(e));
                confirmCodePanel.transferFocusToCode();
            }
        } else
            setContentPanel(registrationPanel);
    }

    private void endOfRegistration() {
        String smsCode = String.valueOf(confirmCodePanel.getCode());
        String firstName = registrationPanel.getFirstName();
        String lastName = registrationPanel.getLastName();
        try {
            user = apiBridge.authSignUp(smsCode, firstName, lastName).getUser();
            endOfAuthorisation();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage(textError(e));
        }
    }

    private void endOfAuthorisation() throws IOException {
        ArrayList<UserContact> userContacts = apiBridge.contactsGetContacts();
        ContactsList contactsList = new ContactsList(userContacts);
        setContentPanel(contactsList);
    }

    private void sendCode(String phone) {
        try {
            apiBridge.authSendCode(phone);
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage(e.getMessage());
        }
    }

    private String textError(IOException exception) {
        String dialogText;
        switch (exception.getMessage()) {
            case "PHONE_CODE_INVALID":
                dialogText = "Неверно введен СМС код!";
                break;
            case "PHONE_CODE_EXPIRED":
                dialogText = "СМС код просрочен!";
                break;
            case "FIRSTNAME_INVALID":
                dialogText = "Неверный ввод имени!";
                break;
            case "LASTNAME_INVALID":
                dialogText = "Неверный ввод фамилии!";
                break;
            case "PHONE_NUMBER_INVALID":
                dialogText = PHONE_NUMBER_INVALID;
                break;
            default:
                dialogText = "Unknown error";
        }
        return dialogText;
    }

    private void showErrorMessage(String text) {
        JOptionPane.showMessageDialog(
                RegistrationFrame.this,
                text,
                "Внимание!",
                JOptionPane.ERROR_MESSAGE);
    }

    private boolean dialogSignUp(String phone) {
        String text = phone + " не зарегестрирован!\n" +
                "Для продолжения работы необходимо пройти регистрацию!";
        int option = JOptionPane.showConfirmDialog(
                this.getContentPane(),
                text,
                "Телефонный номер не зарегестрирован!",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        return option == JOptionPane.YES_OPTION;
    }
}
