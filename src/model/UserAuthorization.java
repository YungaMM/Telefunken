package model;

import org.javagram.TelegramApiBridge;
import org.javagram.response.AuthAuthorization;
import org.javagram.response.AuthCheckedPhone;
import org.javagram.response.object.User;
import org.javagram.response.object.UserContact;
import view.RegistrationFrame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class UserAuthorization {
    private AuthAuthorization authorization;
    private User user;

    private BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public boolean userAuthorization(TelegramApiBridge apiBridge) throws IOException {
        System.out.println("Please, type phone number");
        String userPhone = inputNumber();

        AuthCheckedPhone checkedPhone = apiBridge.authCheckPhone(userPhone);
        if (checkedPhone.isRegistered()) {
            apiBridge.authSendCode(userPhone);

            System.out.println("Please, type SMS code");
            String smsCode = inputNumber();

            authorization = apiBridge.authSignIn(smsCode);
            user = authorization.getUser();
            System.out.println("FirstName: " + user.getFirstName());
            System.out.println("LastName: " + user.getLastName());
            System.out.println("UserPhone: " + user.getPhone());

            printAllContact(apiBridge);

            return true;
        }
        else {
            System.out.println("Your phone number is not authenticated");
            return false;
        }
    }

    private void printAllContact(TelegramApiBridge apiBridge) throws IOException {
        ArrayList<UserContact> userContacts = apiBridge.contactsGetContacts();
        for (UserContact contactInfo:userContacts){
            System.out.println("Фамилия: " + contactInfo.getFirstName());
            System.out.println("Имя: " + contactInfo.getLastName());
            System.out.println("Номер телефона: " + contactInfo.getPhone());
        }
    }

    public AuthAuthorization getAuthorization() {
        return authorization;
    }

    public User getUser() {
        return user;
    }

    private String inputNumber() throws IOException {
        return bufferedReader.readLine().replaceAll("[^0-9]+", "");
    }

}
