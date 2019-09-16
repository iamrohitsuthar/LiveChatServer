package com.chatroom.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.chatroom.client.ClientModel;
import com.chatroom.configuration.Config;
import com.chatroom.models.Request;
import com.chatroom.models.Response;
import com.chatroom.others.LogFileWriter;
import com.chatroom.others.Message;
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
	private Request request = null;
	private Response response = null;
	private MessageListener messageListener;
	private JPanel optionsButtonsHolder;
	private JLabel jLabel_logout;
	private JLabel jLabel_exit;
	private JLabel jLabel_live;
	private int tracker; //used for storing last maximum value of scroll bar
	private boolean isSenderMsg;
	private boolean isReadMode;
	private int scrollDistance = 300;

	
	public ChatActivity(ClientModel clientModel) throws IOException {
		this.clientModel = clientModel;
		jFrame = new JFrame("CHATROOM Chats");
		jPanel = new JPanel();
		jPanelChatWindow = new JPanel();
		optionsButtonsHolder = new JPanel();
		
		messageListener = new MessageListener();
		messageListener.start();
		
		//creating compound border for Text Field to specify left margin to the text
		Border lineBorder = BorderFactory.createLineBorder(Color.blue, 1);
		Border emptyBorder = new EmptyBorder(0,10,0,0); //left margin for text
		compoundBorder = new CompoundBorder(lineBorder,emptyBorder);
		
		leftBubble = new TextBubbleBorder(Config.colorPrimary,2, 10, 16); //left chat bubble border
		rightBubble = new TextBubbleBorder(Config.colorPrimary,2, 10, 16,false); //right chat bubble border
		
		BufferedImage logout = ImageIO.read(this.getClass().getResource("/logout.png"));
		BufferedImage exit = ImageIO.read(this.getClass().getResource("/exit.png"));
		BufferedImage liveUsers = ImageIO.read(this.getClass().getResource("/live.png"));
		
		
		jLabel_logout = new JLabel(new ImageIcon(logout));
		jLabel_logout.setPreferredSize(new Dimension(50,50));
		jLabel_logout.setToolTipText("Logout");
		jLabel_exit = new JLabel(new ImageIcon(exit));
		jLabel_exit.setPreferredSize(new Dimension(50,50));
		jLabel_exit.setToolTipText("Exit from group");
		jLabel_live = new JLabel(new ImageIcon(liveUsers));
		jLabel_live.setPreferredSize(new Dimension(50,50));
		jLabel_live.setToolTipText("Current online users");
		
		optionsButtonsHolder.add(jLabel_exit);
		optionsButtonsHolder.add(jLabel_live);
		optionsButtonsHolder.add(jLabel_logout);	
		
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
		displayStatusMessages("You successfully created and joined the room");
		displayStatusMessages("NOTE: You've entered the server. <br/>"
				+ "For personal message type '@user_name your_message' without quotes");
		
		request = new Request(Request.Type.STATUS_MSG.ordinal(),clientModel.getClientID(),clientModel.getRoomId(),"joined the chat");
		ClientModel.objectOutputStream.writeObject(request);
		ClientModel.objectOutputStream.flush();
		
	}
	
	private void updateScrollbarPosition() {
		jScrollPane.getVerticalScrollBar().setValue(jScrollPane.getVerticalScrollBar().getMaximum());
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
				if(jTfMessageHere.getText().equals("Type your message here"))
					jTfMessageHere.setText("");
				jTfMessageHere.setForeground(Color.black);	
			}
		});
		
		jBtnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tracker = jScrollPane.getVerticalScrollBar().getMaximum();
				setSenderMessage();
				isSenderMsg = true;
			}
		});
		
		jLabel_logout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				try {
					request = new Request(Request.Type.MSG.ordinal(),clientModel.getClientID(),clientModel.getRoomId(),"sv_logout");
					ClientModel.objectOutputStream.writeObject(request);
					ClientModel.objectOutputStream.flush();
				} catch (IOException e1) {
					e1.printStackTrace(new PrintWriter(Config.errors));
					LogFileWriter.Log(Config.errors.toString());
				}
			}
		});
		
		jLabel_exit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				try {
					request = new Request(Request.Type.MSG.ordinal(),clientModel.getClientID(),clientModel.getRoomId(),"sv_exit");
					ClientModel.objectOutputStream.writeObject(request);
					ClientModel.objectOutputStream.flush();
				} catch (IOException e1) {
					e1.printStackTrace(new PrintWriter(Config.errors));
					LogFileWriter.Log(Config.errors.toString());
				}
			}
		});
		
		jLabel_live.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				try {
					request = new Request(Request.Type.MSG.ordinal(),clientModel.getClientID(),clientModel.getRoomId(),"sv_showusers");
					ClientModel.objectOutputStream.writeObject(request);
					ClientModel.objectOutputStream.flush();
					
				} catch (IOException e1) {
					e1.printStackTrace(new PrintWriter(Config.errors));
					LogFileWriter.Log(Config.errors.toString());
				}
				
			}
		});
		
		jScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			
			//for updating scroll bar position on message received
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				JScrollBar jsb = (JScrollBar) e.getAdjustable();
				int e1 = jsb.getModel().getExtent();

				if((jsb.getValue() + e1) <= jsb.getMaximum()-scrollDistance) {
					isReadMode = true;
					scrollDistance = 10;
				}
				else {
					isReadMode = false;
					scrollDistance = 300;
				}
				
				if(isSenderMsg || (!isReadMode && tracker != jsb.getMaximum())) {
					updateScrollbarPosition();
					tracker = jsb.getMaximum();
					
				}
				if(isSenderMsg)
					isSenderMsg = false;
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
		    	tracker = jScrollPane.getVerticalScrollBar().getMaximum();
				setSenderMessage();
				isSenderMsg = true;
		    	jTfMessageHere.setForeground(Color.gray);
				jTfMessageHere.setText("Type your message here");
				jBtnSend.requestFocus();
		    }
		});
	}
	
	private void setSenderMessage() {
		try {
			if(!jTfMessageHere.getText().equals("Type your message here") && jTfMessageHere.getText().trim().length() > 0 && jTfMessageHere.getText().trim().length() <= 300) {
				//sending message to the server
				request = new Request(Request.Type.MSG.ordinal(),clientModel.getClientID(),clientModel.getRoomId(),jTfMessageHere.getText());
				ClientModel.objectOutputStream.writeObject(request);
				ClientModel.objectOutputStream.flush();
				//displaying on the user UI window
				jLabelMessage = new JLabel();
				String temp;
				if(jTfMessageHere.getText().trim().length() <= 3)
					temp = "20px";
				else if(jTfMessageHere.getText().trim().length() <= 51)
					temp = "auto";
				else
					temp = "300px";
				String text = String.format("<html><div><p style=\"width:%s;word-break: break-all;\">%s</p></div></html>", temp ,jTfMessageHere.getText());
				jLabelMessage.setText(text);
				jLabelMessage.setBorder(rightBubble);
				rightBubbleConstraints.gridy = i;
				jPanelChatWindow.add(jLabelMessage,rightBubbleConstraints);
				jPanelChatWindow.revalidate();
				jPanelChatWindow.repaint();
				i++;
				clearTextMessage();
			}
			else {
				UIManager.put("OptionPane.okButtonText", "OK");
				JOptionPane.showMessageDialog(null, "The message length between 1-300 characters.", null, JOptionPane.ERROR_MESSAGE);
			}
		}
		catch(IOException e) {
			e.printStackTrace(new PrintWriter(Config.errors));
			LogFileWriter.Log(Config.errors.toString());
		}
		
	}
	
	private void clearTextMessage() {
		jTfMessageHere.setForeground(Color.gray);
		jTfMessageHere.setText("Type your message here");
		jBtnSend.requestFocus();
	}
	
	private void displayStatusMessages(String message) {
		jLabelMessage = new JLabel();
		String text = String.format("<html><div style=\"width:%dpx;text-align:center;\">%s</div></html>",400, message);
		jLabelMessage.setText(text);
		centerConstraints.gridy = i;
		jPanelChatWindow.add(jLabelMessage,centerConstraints);
		jPanelChatWindow.revalidate();
		jPanelChatWindow.repaint();
		i++;
	}
	
	private void setReceiverMessage(String senderName, String message) {
		jLabelMessage = new JLabel();
		String temp;
		if(message.trim().length() <= 3)
			temp = "20px";
		else if(message.trim().length() <= 51)
			temp = "auto";
		else
			temp = "300px";
		String text = String.format("<html><div style=\"font-size:8px;text-align:right;\">%3$s</div><div style=\"width:%1$spx;\"><p style=\"width:%s;word-break: break-all;\">%2$s</p></div></html>", temp, message, senderName + "<br>");
		jLabelMessage.setText(text);		
		jLabelMessage.setBorder(leftBubble);
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
		jScrollPane.setBorder(BorderFactory.createEmptyBorder());
		jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jScrollPane.setBounds(0, 0, 864, 490);
		JPanel p1 = new JPanel(null);
		p1.setPreferredSize(new Dimension(864,530));
		p1.add(jScrollPane);
		jFrame.add(BorderLayout.CENTER,p1);
		
		jFrame.add(BorderLayout.NORTH,optionsButtonsHolder);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setSize(864,614);
		jFrame.setLocationRelativeTo(null);
		jFrame.setVisible(true);
		jFrame.setResizable(false);		
		
		//removing focus from edit text and set it to the button
		jFrame.getRootPane().setDefaultButton(jBtnSend);
		jBtnSend.requestFocus();
		
		ListeningEvents();
	}
	
	
	class MessageListener extends Thread{
		boolean isContinue = true;
		public void run()
		{
			while(true && isContinue ) {
				try {
					response = (Response) ClientModel.objectInputStream.readObject();
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							if(response.getId() == Response.Type.STATUS_MSG.ordinal() || response.getId() == Response.Type.LOGOUT.ordinal())
								displayStatusMessages(response.getContents());
							else if(response.getId() == Response.Type.GEN.ordinal()) {
								String data = "List of Online Users \n";
								int i = 1;
								data += i + ". You \n";
								String temp = response.getContents();
								if(temp.length() != 0 && !temp.equals("")) {
									String arrayOFNames[] = temp.split(",");
									for (String string : arrayOFNames) {
										i++;
										data += i + ". " + string + "\n";
									}
								}
								UIManager.put("OptionPane.okButtonText", "OK");
								JOptionPane.showMessageDialog(null, data);
							}
							else {
								String msg = response.getContents();
								String name = msg.substring(0, msg.indexOf(" "));
								if(response.getId() == Response.Type.P_MSG.ordinal())
									name += "(PM)";
								msg = msg.substring(msg.indexOf(" ")+1);
								setReceiverMessage(name,msg);
							}
							if(response.getContents().equals("sv_exit_successful")) {
								isContinue = false;
								clientModel.setRoomId(-1);
								SwingUtilities.invokeLater(new Runnable() {
									   public void run() {
										try {
											new MainMenuOptions(clientModel);
										} catch (IOException e) {
											e.printStackTrace(new PrintWriter(Config.errors));
											LogFileWriter.Log(Config.errors.toString());
										}
										   jFrame.dispose();
									   }
									});
							}
							else if(response.getId() == Response.Type.LOGOUT.ordinal()) {
								isContinue = false;
								clientModel.setRoomId(-1);
								clientModel.setClientID(-1);
								SwingUtilities.invokeLater(new Runnable() {
								   public void run() {
									try {
										new SignInActivity(clientModel);
									} catch (IOException e) {
										e.printStackTrace(new PrintWriter(Config.errors));
										LogFileWriter.Log(Config.errors.toString());
									}
									   jFrame.dispose();
								   }
								});
						}
							
						}
					});
					
					
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace(new PrintWriter(Config.errors));
					LogFileWriter.Log(Config.errors.toString());
					break;
				}
			}
		}
	}
}


