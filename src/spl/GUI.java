package spl;

import spl.services.ChatService;
import spl.services.EncryptionServiceFactory;
import spl.services.EncryptionType;
import spl.services.FileLogService;
import spl.services.ReverseStringEncryptionService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GUI {
    // Connect status constants
    final static int DISCONNECTED = 0;
    final static int BEGIN_CONNECT = 1;
    final static int CONNECTED = 2;
    private static final Logger logger = Logger.getLogger(GUI.class.getName());
    private static ChatService chatService;
    // Various GUI components and info
    public static JFrame mainFrame = null;
    public static JTextPane chatText = null;
    public static JTextField chatLine = null;
    public static JLabel statusBar = null;
    public static JButton connectButton = null;
    public static JButton disconnectButton = null;
    public static JButton redButton = null;
    public static JButton greenButton = null;
    public static JButton blueButton = null;
    // Connection info
    public static String hostIP = "localhost";
    public static int port = 1234;
    public static int connectionStatus = DISCONNECTED;
    public static boolean isHost = true;
    public static Client user = new Client(hostIP, port, "Bob");
    public static String usernameColor = "Red";

    private static JPanel initOptionsPane() {
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


        String[] choices = {"Red", "Green", "Blue"};
        final JComboBox<String> cb = new JComboBox<String>(choices);
        cb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                usernameColor = (String) cb.getSelectedItem();
                /*
                 * String colorString = (String) cb.getSelectedItem();
                 * if(colorString == "Red") { usernameColor = Color.red; } else if (colorString
                 * == "Green") { usernameColor = Color.green; } else if (colorString == "Blue")
                 * { usernameColor = Color.blue; }
                 */
            }
        });
        cb.setVisible(true);

        buttonPane.add(connectButton);
        buttonPane.add(disconnectButton);
        optionsPane.add(cb);
        optionsPane.add(buttonPane);

        return optionsPane;
    }

    private static void initGUI() {
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
                user.sendMessage("[" + usernameColor + "]: " + chatLine.getText());
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

    private static void updateChat() {
        while (true) {
            if (Client.IS_AUTHENTICATED) {
                List<String> chatLines = chatService.readAll();
                chatText.setText(String.join("\n", chatLines));
            } else {
                logger.log(Level.WARNING, "You are not authenticated to read the chat logs!");
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                logger.log(Level.WARNING, "Could not update chat", e);
            }
        }
    }

    public static void main(String[] args) {
        chatService = new ChatService(new FileLogService(), EncryptionServiceFactory.createEncryptionService(EncryptionType.toEnum(args[0])));
        initGUI();
        updateChat();
    }
}

// Action adapter for easy event-listener coding
class ActionAdapter implements ActionListener {
    public void actionPerformed(ActionEvent e) {
    }
}
