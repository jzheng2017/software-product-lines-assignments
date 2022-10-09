package spl; 

import spl.services.ChatService; 
import spl.services.MessageTransformer; 
import spl.services.ColorService;

import javax.swing.*; 
import java.awt.*; 
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import java.awt.event.KeyEvent; 
import java.util.List; 


public  class  GUI  extends Interface {
	
	
	private void colorCmb() {
		  String[] choices = {"Red", "Green", "Blue"};
	        final JComboBox<String> cb = new JComboBox<String>(choices);
	        cb.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                usernameColor = (String) cb.getSelectedItem();
	                ColorService.getInstance().setColor(usernameColor);
	            }
	        });
	        cb.setVisible(true);
	        optionsPane.add(cb);
	}
}
