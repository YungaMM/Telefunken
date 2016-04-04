import org.javagram.TelegramApiBridge;

import java.io.IOException;

public class Loader {

    public static void main(String[] args) throws IOException {
        TelegramApiBridge bridge = new TelegramApiBridge("149.154.167.50:443",
                36025, "5530d508831b42dc0df01f9b31c2978b");

        UserAuthorization authorization = new UserAuthorization();
        authorization.userAuthorization(bridge);
    }

}
