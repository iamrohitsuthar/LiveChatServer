package com.chatroom.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import com.chatroom.client.ClientModel;
import com.chatroom.configuration.Config;
import com.chatroom.others.TextBubbleBorder;

public class ChatActivity {
	private JFrame jFrame;
	private JButton jBtnSend;
	private JTextField jTfMessageHere;
	private CompoundBorder compoundBorder;
	private JPanel jPanel;
	private JPanel jPanelChatWindow;
	private int i = 0;
	private JScrollPane jScrollPane;
	private JLabel jLabelMessage;
	private AbstractBorder leftBubble;
	private AbstractBorder rightBubble;
	private GridBagConstraints leftBubbleConstraints;
	private GridBagConstraints rightBubbleConstraints;
	private GridBagConstraints centerConstraints;
	private ClientModel clientModel;
	
	public ChatActivity(ClientModel clientModel) throws IOException {
		this.clientModel = clientModel;
		jFrame = new JFrame("CHATROOM Chats");
		jPanel = new JPanel();
		jPanelChatWindow = new JPanel();

		//creating compound border for Text Field to specify left margin to the text
		Border lineBorder = BorderFactory.createLineBorder(Color.blue, 1);
		Border emptyBorder = new EmptyBorder(0,10,0,0); //left margin for text
		compoundBorder = new CompoundBorder(lineBorder,emptyBorder);
		
		leftBubble = new TextBubbleBorder(Config.colorPrimary,2, 10, 16); //left chat bubble border
		rightBubble = new TextBubbleBorder(Config.colorPrimary,2, 10, 16,false); //right chat bubble border
		
		rightBubbleConstraints = new GridBagConstraints(0, i, 1, 1, 1.0, 0,
	            GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(
	                    0, 0, 5, 0), 0, 0);
		
		leftBubbleConstraints = new GridBagConstraints(0, i, 1, 1, 1.0, 0,
	            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(
	                    0, 0, 5, 0), 0, 0);
		
		centerConstraints = new GridBagConstraints(0, i, 1, 1, 1.0, 0,
	            GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
	                    0, 0, 5, 0), 0, 0);
		
		jTfMessageHere = new JTextField("Type your message here");
		jBtnSend = new JButton("Send");
		
		initializeAllWithProperties();

	}
	
	private void ListeningEvents() {
		jTfMessageHere.addFocusListener(new FocusListener() {	
			@Override
			public void focusLost(FocusEvent e) {
				if(jTfMessageHere.getText().length() == 0) {
					jTfMessageHere.setForeground(Color.gray);
					jTfMessageHere.setText("Type your message here");
				}
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				jTfMessageHere.setText("");
				jTfMessageHere.setForeground(Color.black);	
			}
		});
		
		jBtnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(i%2 == 0)
					setSenderMessage();
				else
					setReceiverMessage();
			}
		});
		
		int condition = JComponent.WHEN_FOCUSED;
		InputMap inputMap = jTfMessageHere.getInputMap(condition);
		ActionMap actionMap = jTfMessageHere.getActionMap();
		KeyStroke enterKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);   
		inputMap.put(enterKey, enterKey.toString());
		actionMap.put(enterKey.toString(), new AbstractAction() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	setSenderMessage();
		    	jTfMessageHere.setForeground(Color.gray);
				jTfMessageHere.setText("Type your message here");
				jBtnSend.requestFocus();
		    }
		});
		
	}
	
	private void setSenderMessage() {
		if(!jTfMessageHere.getText().equals("Type your message here") && jTfMessageHere.getText().trim()	.length() > 0) {
			jLabelMessage = new JLabel();
			String text = String.format("<html><div style=\"width:%dpx;\">%s</div></html>",150, jTfMessageHere.getText());
			jLabelMessage.setText(text);
			jLabelMessage.setBorder(rightBubble);
			rightBubbleConstraints.gridy = i;
			jPanelChatWindow.add(jLabelMessage,rightBubbleConstraints);
			jPanelChatWindow.revalidate();
			jPanelChatWindow.repaint();
			i++;
			clearTextMessage();
		}
	}
	
	private void clearTextMessage() {
		jTfMessageHere.setForeground(Color.gray);
		jTfMessageHere.setText("Type your message here");
	}
	
	private void displayStatusMessages(String message) {
		jLabelMessage = new JLabel();
		String text = String.format("<html><div style=\"width:%dpx;text-align:center;\">%s</div></html>",300, message);
		jLabelMessage.setText(text);
		//jLabelMessage.setBorder(new LineBorder(Config.colorPrimary,2));
		centerConstraints.gridy = i;
		jPanelChatWindow.add(jLabelMessage,centerConstraints);
		jPanelChatWindow.revalidate();
		jPanelChatWindow.repaint();
		i++;
	}
	
	private void setReceiverMessage() {
		jLabelMessage = new JLabel();
		
		String text = String.format("<html><div style=\"width:%1$dpx;font-size:8px;text-align:right;\">%3$s</div><div style=\"width:%1$dpx;font-size:12px;font-weight:bold;font-family:serif;\">%2$s</div></html>", 150, jTfMessageHere.getText(),"Rohit Suthar <br>");
		jLabelMessage.setText(text);
		
		jLabelMessage.setBorder(leftBubble);
		jLabelMessage.setFont(new Font("Serif",Font.BOLD,12));
		leftBubbleConstraints.gridy = i;
		jPanelChatWindow.add(jLabelMessage,leftBubbleConstraints);
		jPanelChatWindow.revalidate();
		jPanelChatWindow.repaint();
		i++;
		clearTextMessage();
	}
	
	private void initializeAllWithProperties() {
		//User name text field
		jTfMessageHere.setPreferredSize(new Dimension(550,35));
		jTfMessageHere.setBackground(Color.WHITE);
		jTfMessageHere.setBorder(compoundBorder);
		jTfMessageHere.setForeground(Color.gray);
		
		//sign Up button
		jBtnSend.setPreferredSize(new Dimension(50,35));
		jBtnSend.setBackground(Color.WHITE);
		jBtnSend.setBorder(new LineBorder(Color.blue, 3));
		jBtnSend.setFocusPainted(false);
		jBtnSend.requestFocus();
		
		//adding text field and send button at the bottom
		jPanel.setLayout(new GridLayout(1,2));
		jPanel.add(jTfMessageHere);
		jPanel.add(jBtnSend);
		jPanel.setOpaque(false);
		
		jPanelChatWindow.setOpaque(false);
		jPanelChatWindow.setLayout(new GridBagLayout());
		
		jFrame.setLayout(new BorderLayout());
		jFrame.add(BorderLayout.PAGE_END,jPanel);
		
		//jFrame.add(BorderLayout.CENTER,jPanelChatWindow);
		
		jScrollPane = new JScrollPane(jPanelChatWindow);
		jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jScrollPane.setBounds(0, 0, 864, 560);
		JPanel p1 = new JPanel(null);
		p1.setPreferredSize(new Dimension(864,600));
		p1.add(jScrollPane);
		jFrame.add(BorderLayout.CENTER,p1);
		
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setSize(864,614);
		jFrame.setLocationRelativeTo(null);
		jFrame.setVisible(true);
		jFrame.setResizable(false);		
		
		//removing focus from editext and set it to the button
		jFrame.getRootPane().setDefaultButton(jBtnSend);
		jBtnSend.requestFocus();
		
		ListeningEvents();
	}
	
//	public static void main(String args[]) throws IOException {
//		new ChatActivity();
//	}	
}

