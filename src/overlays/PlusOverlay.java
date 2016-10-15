package overlays;

import components.GuiHelper;
import resources.Images;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Created by HerrSergio on 26.06.2016.
 */
public class PlusOverlay extends JPanel {
    private JButton plusButton;
    private JPanel rootPanel;

    {
        GuiHelper.decorateAsImageButton(plusButton, Images.getPlus());
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        rootPanel = this;

        //Альтернтивное решение
        //plusButton = new ImageButton(Images.getPlus());
    }

    public void addActionListener(ActionListener actionListener) {
        plusButton.addActionListener(actionListener);
    }

    public void removeActionListener(ActionListener actionListener) {
        plusButton.removeActionListener(actionListener);
    }
}
