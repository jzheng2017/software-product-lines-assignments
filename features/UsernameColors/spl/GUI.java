package spl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import spl.GUI;
import spl.services.ColorService;

public class GUI extends Interface {
	
	protected JPanel initOptionsPanel()
	{
		original();
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
	    return optionsPane;
	}
}