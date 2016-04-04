import org.javagram.TelegramApiBridge;
import org.javagram.response.AuthAuthorization;
import org.javagram.response.AuthCheckedPhone;
import org.javagram.response.object.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserAuthorization {
    private AuthAuthorization authorization;
    private User user;

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
            return true;
        }
        else {
            System.out.println("Your phone number is not authenticated");
            return false;
        }
    }

    public AuthAuthorization getAuthorization() {
        return authorization;
    }

    public User getUser() {
        return user;
    }

    private String inputNumber() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String text = reader.readLine();
        String pattern = "[0-9]+";
        return formatTxt(pattern, text);
    }

    private String formatTxt (String pattern, String text){
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        String result = "";
        while (m.find()) {
            result += text.substring(m.start(), m.end());
        }
        return result;
    }

}
