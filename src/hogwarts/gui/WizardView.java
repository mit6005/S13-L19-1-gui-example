package hogwarts.gui;

import hogwarts.model.Wizard;

import java.awt.Component;
import java.awt.event.*;

import javax.swing.*;

public class WizardView extends JPanel {
    
    // this view's model
    private Wizard model = null;
    // may be null
    
    // view objects used to display this view
    private final JLabel picture;
    private final JTextField name;
    private final JList friends;
    private final DefaultListModel friendsModel;
    // rep invariant: all view objects != null
    
    /** Make a WizardView. */
    public WizardView() {
        // create the components
        picture = new JLabel("[picture]");
        name = new JTextField("Harry Potter");
        friendsModel = new DefaultListModel();
        friends = new JList(friendsModel);
        
        // create labels and other decorations that we don't need to access later,
        // so we don't save them in the rep
        final JLabel friendsLabel = new JLabel("Friends:");
        final JButton thrashButton = new JButton("Thrash");
        
        // define layout
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        //setPreferredSize(new Dimension(400,400));
        
        // get some margins around components by default
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        
        // place the components in the layout (which also adds them
        // as children of this view)
        layout.setHorizontalGroup(
                layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                            .addComponent(picture, 100, 100, 100)
                            .addComponent(name))
                .addGroup(layout.createParallelGroup()
                            .addComponent(friendsLabel)
                            .addComponent(friends, 0, GroupLayout.PREFERRED_SIZE, Integer.MAX_VALUE)
                            .addComponent(thrashButton))
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup()
                                .addComponent(picture, 100, 100, 100)
                                .addComponent(name, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addComponent(friendsLabel)
                    .addComponent(friends, 0, 200, Integer.MAX_VALUE)
                    .addComponent(thrashButton)
        );
        
        // by default list items are rendered with toString,
        // override to use wizards' names instead
        friends.setCellRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(
                    JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                String name = ((Wizard)value).getName();
                return super.getListCellRendererComponent(list, name, index, isSelected, cellHasFocus);
            }
        });
        
        // add listeners for user input
        friends.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                // respond only on double-click
                if (event.getClickCount() == 2) {
                    visitSelectedFriend();
                }
            }
        });
        
        friends.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_ENTER) {
                    visitSelectedFriend();
                }
            }
        });
        
        thrashButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                thrashWithSelectedFriend();
            }
        });
    }
    
    /** Sets the model shown by this view. */
    public void setWizard(Wizard w) {
        this.model = w;
        refresh();
    }
    
    /** Refresh this view to reflect changes in the model. */
    public void refresh() {
        name.setText(model.getName());
        picture.setIcon(new ImageIcon(model.getPhoto()));
        friendsModel.clear();
        for (Wizard friend : model.getFriends()) {
            friendsModel.addElement(friend);
        }
    }

    /** Switch the model to display the friend currently selected in the friends list.
     *  If no friend selected, has no effect. */
    private void visitSelectedFriend() {
        if (friends.isSelectionEmpty()) return; // no wizard selected, can't switch to it
        int selectedIndex = friends.getMinSelectionIndex();
        setWizard((Wizard) friendsModel.get(selectedIndex));        
    }

    /** Repeatedly friends and unfriends the currently-selected friend N times.
     *  If no friend selected, has no effect. */
    private void thrashWithSelectedFriend() {
        if (friends.isSelectionEmpty()) return; // no wizard selected, can't switch to it
        int selectedIndex = friends.getMinSelectionIndex();
        Wizard selectedWizard = (Wizard) friendsModel.get(selectedIndex);
        thrashForeground(model, selectedWizard);
    }

    private static final int NUM_THRASHES = 5;

    private void thrashForeground(Wizard source, Wizard target) {
        try {
            for (int j = 0; j < NUM_THRASHES; ++j) {
                // unfriend them
                source.defriend(target);
                refresh();
                Thread.sleep(1000); // wait for a second
                
                // then refriend them
                source.friend(target);
                refresh();
                Thread.sleep(1000); // wait for a second
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void thrashBackground(final Wizard source, final Wizard target) {
        Thread backgroundThread = new Thread(new Runnable() {
            public void run() {
                try {
                    for (int j = 0; j < NUM_THRASHES; ++j) {
                        // unfriend them
                        source.defriend(target);
                        refreshInUIThread();
                        Thread.sleep(1000); // wait for a second
                        
                        // then refriend them
                        source.friend(target);
                        refreshInUIThread();
                        Thread.sleep(1000); // wait for a second
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            
            private void refreshInUIThread() {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        refresh();
                    }
                });
            }
        });
        backgroundThread.start();
    }

}
