package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class BasicFrame extends JFrame{
    private static final int FRAME_HEIGHT = 800;
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_MIN_HEIGHT = 200;
    private static final int FRAME_MIN_WIDTH = 350;

    private static final String FRAME_TITLE = "Telefunken";
    private static final String KEY_CTRL_ENTER_ACTION = "CTRL+ENTER";

    private EnterPhone enterPhonePanel = new EnterPhone();
    private EnterConfirmationCode confirmCodePanel = new EnterConfirmationCode();
    private Registration registrationPanel = new Registration();

    public BasicFrame() throws HeadlessException {
        setContentPanel(enterPhonePanel);
        createFrame();

        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openForm();
            }
        };
        enterPhonePanel.addListenerForChangeForm(action);
        confirmCodePanel.addListenerForChangeForm(action);
        registrationPanel.addListenerForChangeForm(action);

        KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_MASK);
        JComponent component = this.getRootPane();
        component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                key,
                KEY_CTRL_ENTER_ACTION);
        component.getActionMap().put(
                KEY_CTRL_ENTER_ACTION,
                action);

    }

    private void openForm(){

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
