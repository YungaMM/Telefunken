import org.javagram.TelegramApiBridge;
import org.javagram.response.AuthAuthorization;
import org.javagram.response.AuthCheckedPhone;
import org.javagram.response.object.User;
import org.telegram.api.TLContact;
import org.telegram.api.contacts.TLAbsContacts;
import org.telegram.api.contacts.TLContacts;
import org.telegram.api.contacts.TLContactsNotModified;
import org.telegram.tl.TLVector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Collator;
import java.util.ArrayList;

public class UserAuthorization {
    private AuthAuthorization authorization;
    private User user;

    private BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public boolean userAuthorization(TelegramApiBridge bridge) throws IOException {
        System.out.println("Please, type phone number");
        String userPhone = inputNumber();

        AuthCheckedPhone checkedPhone = bridge.authCheckPhone(userPhone);
        if (checkedPhone.isRegistered()) {
            bridge.authSendCode(userPhone);

            System.out.println("Please, type SMS code");
            String smsCode = inputNumber();

            authorization = bridge.authSignIn(smsCode);
            user = authorization.getUser();
            System.out.println("FirstName: " + user.getFirstName());
            System.out.println("LastName: " + user.getLastName());
            System.out.println("UserPhone: " + user.getPhone());

            printAllContact();

            return true;
        }
        else {
            System.out.println("Your phone number is not authenticated");
            return false;
        }
    }

    private void printAllContact() {
        TLContacts contacts = new TLContacts();
        TLVector<TLContact> tlVector = contacts.getContacts();
        TLContact contact = new TLContact();
        for (int i = 0; i < tlVector.size(); i++) {
            contact = tlVector.get(i);
//            System.out.println("Name: " + "    " + tlVector);
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
