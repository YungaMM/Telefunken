import org.javagram.TelegramApiBridge;
import org.javagram.response.AuthCheckedPhone;
import org.javagram.response.AuthSentCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Loader {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        TelegramApiBridge bridge = new TelegramApiBridge("149.154.167.50:443",36025,"5530d508831b42dc0df01f9b31c2978b");

        System.out.println("Please, type phone number");
        String userPhone = reader.readLine().trim();
        AuthCheckedPhone checkedPhone = bridge.authCheckPhone(userPhone);
        if(checkedPhone.isRegistered()){
            AuthSentCode sentCode = bridge.authSendCode(userPhone);

        }

    }
}
