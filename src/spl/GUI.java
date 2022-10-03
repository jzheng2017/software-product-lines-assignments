package spl;  

import spl.services.ChatService;  
import spl.services.ColorService;  
import spl.services.MessageTransformer;  

import javax.swing.*;  
import java.awt.*;  
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  
import java.awt.event.KeyEvent;  
import java.util.List;  


public   class   GUI   extends Interface {
	
	
    // Various GUI components and info
    private JFrame mainFrame = null;

	

	
    private JTextPane chatText = null;

	

	
    private JTextField chatLine = null;

	

	
    private JLabel statusBar = null;

	

	
    private JButton connectButton = null;

	

	
    private JButton disconnectButton = null;

	

	
    private JButton redButton = null;

	

	
    private JButton greenButton = null;

	

	
    private JButton blueButton = null;

	

	


    public GUI(ChatService cs, MessageTransformer messageTransformer) {
        super(cs, messageTransformer);
    }

	

	

    @Override
    public void init() {
        initGUI();
    }

	

	

    @Override
    public void update() {
        updateChat();
    }

	

	

    private JPanel initOptionsPane() {
        ActionAdapter buttonListener = null;

        // Create an options pane
        JPanel optionsPane = new JPanel(new GridLayout(4, 1));

        // Connect/disconnect buttons
        JPanel buttonPane = new JPanel(new GridLayout(1, 2));
        buttonListener = new ActionAdapter() {
            public void actionPerformed(ActionEvent e) {
                // Request a connection initiation
                if (e.getActionCommand().equals("connect")) {
                    connectButton.setEnabled(false);
                    disconnectButton.setEnabled(true);
                    connectionStatus = BEGIN_CONNECT;
                    chatLine.setEnabled(true);
                    statusBar.setText("Online");
                    mainFrame.repaint();
                    user.connect();

                }
                // Disconnect
                else {
                    connectButton.setEnabled(true);
                    disconnectButton.setEnabled(false);
                    connectionStatus = DISCONNECTED;
                    chatLine.setText("");
                    chatLine.setEnabled(false);
                    statusBar.setText("Offline");
                    mainFrame.repaint();
                    user.disconnect();
                }
            }
        };
        connectButton = new JButton("Connect");
        connectButton.setMnemonic(KeyEvent.VK_C);
        connectButton.setActionCommand("connect");
        connectButton.addActionListener(buttonListener);
        connectButton.setEnabled(true);
        disconnectButton = new JButton("Disconnect");
        disconnectButton.setMnemonic(KeyEvent.VK_D);
        disconnectButton.setActionCommand("disconnect");
        disconnectButton.addActionListener(buttonListener);
        disconnectButton.setEnabled(false);

        //#if UsernameColors
        String[] choices = {"Red", "Green", "Blue"};
        final JComboBox<String> cb = new JComboBox<String>(choices);
        cb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                usernameColor = (String) cb.getSelectedItem();
                ColorService.getInstance().setColor(usernameColor);
                /*
                 * String colorString = (String) cb.getSelectedItem();
                 * if(colorString == "Red") { usernameColor = Color.red; } else if (colorString
                 * == "Green") { usernameColor = Color.green; } else if (colorString == "Blue")
                 * { usernameColor = Color.blue; }
                 */
            }
        });
        cb.setVisible(true);
        optionsPane.add(cb);
        //#endif

        buttonPane.add(connectButton);
        buttonPane.add(disconnectButton);
        optionsPane.add(buttonPane);

        return optionsPane;
    }

	

	

    private void initGUI() {
        // Set up the status bar
        statusBar = new JLabel();
        statusBar.setText("Offline");

        // Set up the options pane
        JPanel optionsPane = initOptionsPane();

        // Set up the chat pane
        JPanel chatPane = new JPanel(new BorderLayout());
        chatText = new JTextPane();
        chatText.setPreferredSize(new Dimension(200, 200));
        chatText.setEditable(false);
        chatText.setForeground(Color.blue);
        JScrollPane chatTextPane = new JScrollPane(chatText,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        chatLine = new JTextField();
        chatLine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                user.sendMessage(messageTransformer.transform(chatLine.getText()));
                chatLine.setText("");
            }
        });
        chatLine.setEnabled(false);
        chatPane.add(chatLine, BorderLayout.SOUTH);
        chatPane.add(chatTextPane, BorderLayout.CENTER);
        chatPane.setPreferredSize(new Dimension(300, 200));

        // Set up the main pane
        JPanel mainPane = new JPanel(new BorderLayout());
        mainPane.add(statusBar, BorderLayout.SOUTH);
        mainPane.add(optionsPane, BorderLayout.WEST);
        mainPane.add(chatPane, BorderLayout.CENTER);

        // Set up the main frame
        mainFrame = new JFrame("Simple TCP Chat");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setContentPane(mainPane);
        mainFrame.setSize(mainFrame.getPreferredSize());
        mainFrame.setLocation(200, 200);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

	

	

    private void updateChat() {
        while (true) {
            if (Client.IS_AUTHENTICATED) {
                List<String> chatLines = chatService.readAll();
                chatText.setText(String.join("\n", chatLines));
            } else {
                logger.write("You are not authenticated to read the chat logs!");
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                logger.write("Could not update chat. Cause: " + e.getMessage());
            }
        }
    }

	

	

    // Action adapter for easy event-listener coding
    private static   class   ActionAdapter   implements ActionListener {
		
		
        public void actionPerformed(ActionEvent e) {
        }


	}


}
