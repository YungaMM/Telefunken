package resources;

import java.awt.*;
import java.io.InputStream;

//https://lingualift.com/blog/best-free-cyrillic-fonts/
public class Fonts {
    private static Font regularFont;
    private static Font lightFont;
    private static Font semiBold;

    public static Font getLightFont() {
        if (lightFont == null) {
            lightFont = loadFont("OpenSansLight.ttf");
        }
        return lightFont;
    }

    public static Font getRegularFont() {
        if (regularFont == null) {
            regularFont = loadFont("OpenSansRegular.ttf");
        }
        return regularFont;
    }

    public static Font getSemiBoldFont() {
        if (semiBold == null) {
            semiBold = loadFont("OpenSansSemiBold.ttf");
        }
        return semiBold;
    }

    private static Font loadFont(String name) {
        try(InputStream inputStream = Fonts.class.getResourceAsStream("/fonts/" + name)) {
            return Font.createFont(Font.TRUETYPE_FONT, inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font(Font.SANS_SERIF, Font.PLAIN, 24);
        }
    }
}
