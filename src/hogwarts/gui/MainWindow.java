package hogwarts.gui;

import hogwarts.Hogwarts;
import hogwarts.model.Castle;
import hogwarts.model.Wizard;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class MainWindow extends JFrame {
    
    private final WizardView wizardView;
    
    public MainWindow() {
        setTitle("Hogwarts");
        setLayout(new BorderLayout());
        wizardView = new WizardView();
        add(wizardView, BorderLayout.CENTER);
        pack();
    }
    
    public void setStartingWizard(Wizard w) {
        wizardView.setWizard(w);
    }
    
    public static void main(String[] args) {
        Castle hogwarts = Hogwarts.makeHogwarts();
        MainWindow win = new MainWindow();
        win.setStartingWizard(hogwarts.lookup("Harry Potter"));
        win.setVisible(true);
    }
}
