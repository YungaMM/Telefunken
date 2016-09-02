package view.dialogs;

import view.undecorated.ComponentResizerAbstract;
import view.undecorated.DecorationForFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class BasicDialog extends JDialog {
    public static final int DEFAULT_WIDTH = 450;
    public static final int DEFAULT_HEIGHT = 170;

    public static final int CLOSED_OPTION = -1;
    public static final int OK_OPTION = 0;
    public static final int NO_OPTION = 1;
    public static final int CANCEL_OPTION = 2;
    public static final int YES_OPTION = 3;

    public static final int DEFAULT_OPTION = -1;
    public static final int YES_NO_CANCEL_OPTION = 1;
    public static final int OK_CANCEL_OPTION = 2;
    public static final int YES_NO_OPTION = 3;

    private boolean yesBtnOption;
    private boolean noBtnOption;
    private boolean cancelBtnOption;
    private int resultValue;

    private DecorationForFrame undecoratedFrame;
    private DecorationForDialog dialogPanel;

    public BasicDialog(JFrame parentFrame) {
        super(parentFrame, true);
    }

    public static int showConfirmDialog(JFrame parentFrame, String title, String message, int buttonOption){
        BasicDialog dialog = new BasicDialog(parentFrame);
        dialog.createDialog(title,message,buttonOption);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);

        return dialog.getResultValue();
    }

    public static void showMessageDialog(JFrame parentFrame, String title, String message){
        BasicDialog dialog = new BasicDialog(parentFrame);
        dialog.createDialog(title,message,OK_OPTION);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }

    private void createDialog(String title, String message, int buttonOption) {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        dialogPanel = new DecorationForDialog(message);
        setButton(buttonOption);
        undecoratedFrame = new DecorationForFrame(
                this, ComponentResizerAbstract.KEEP_RATIO_CENTER);
        undecoratedFrame.setContentPanel(dialogPanel);
        undecoratedFrame.setTitle(title);
        undecoratedFrame.getMinimizeButton().setVisible(false);
        closeWindow();
        setListeners();
    }

    private void setListeners() {
        dialogPanel.addActionListenerForOk(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setResultValue(OK_OPTION);
                dispose();
            }
        });

        dialogPanel.addActionListenerForYes(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setResultValue(YES_OPTION);
                dispose();
            }
        });

        dialogPanel.addActionListenerForNo(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setResultValue(NO_OPTION);
                dispose();
            }
        });

        dialogPanel.addActionListenerForCancel(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setResultValue(CANCEL_OPTION);
                dispose();
            }
        });
    }

    private void setResultValue(int resultValue){
        this.resultValue = resultValue;
    }

    private int getResultValue(){
        return resultValue;
    }

    private void closeWindow() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        undecoratedFrame.addActionListenerForClose(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setResultValue(CLOSED_OPTION);
                BasicDialog.this.dispose();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                super.windowClosed(windowEvent);
                setResultValue(CLOSED_OPTION);
                dispose();
            }
        });
    }

    private void setButton(int btnOption) {
        switch (btnOption) {
            case YES_NO_CANCEL_OPTION:
                dialogPanel.getYesButton().setVisible(true);
                dialogPanel.getNoButton().setVisible(true);
                dialogPanel.getCancelButton().setVisible(true);
                break;
            case YES_NO_OPTION:
                dialogPanel.getYesButton().setVisible(true);
                dialogPanel.getNoButton().setVisible(true);
                break;
            case OK_CANCEL_OPTION:
                dialogPanel.getOkButton().setVisible(true);
                dialogPanel.getCancelButton().setVisible(true);
                break;
            case OK_OPTION:
                dialogPanel.getOkButton().setVisible(true);
                break;
        }
    }
}
