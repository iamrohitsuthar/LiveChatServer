package com.chatroom.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.InsetsUIResource;

import com.chatroom.client.ClientModel;
import com.chatroom.configuration.Config;
import com.chatroom.models.Request;
import com.chatroom.models.Response;
import com.chatroom.others.LogFileWriter;
import com.chatroom.others.Message;

public class MainMenuOptions {
	private JLabel jLabel;
	private JLabel jLabelTitle;
	private JFrame jFrame;
	private JButton jBtnCreateRoom;
	private JButton jBtnJoinRoom;
	private JButton jBtnViewRooms;
	private JButton jBtnLogout;
	private BufferedImage iconLogo;
	private ClientModel clientModel;
	private Request request;
	private Response response;

	@SuppressWarnings("serial")
	public MainMenuOptions(ClientModel cm) throws IOException {
		clientModel = cm;
		jFrame = new JFrame("CHATROOM");
		
		iconLogo = ImageIO.read(this.getClass().getResource("/logo.png"));
		
		jFrame.setContentPane(new JPanel() {
			BufferedImage myImage = ImageIO.read(this.getClass().getResource("/background.png"));
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(myImage, 0, 0, this);
			}
		});
		
		jBtnCreateRoom = new JButton("CREATE ROOM");
		jBtnJoinRoom = new JButton("JOIN ROOM");
		jBtnViewRooms = new JButton("VIEW ROOMS");
		jBtnLogout = new JButton("LOGOUT");
		
		jLabelTitle = new JLabel("WELCOME TO CHATROOM - A LIVE CHAT SOFTWARE");
		
		initializeAllWithProperties();

	}
	
	private void logOut() {
		try {
			request = new Request(Request.Type.LOGOUT.ordinal(),clientModel.getClientID(),clientModel.getRoomId(),"");
			ClientModel.objectOutputStream.writeObject(request);
			ClientModel.objectOutputStream.flush();
			response = (Response) ClientModel.objectInputStream.readObject();
			if( response.getSuccess())
			{
				clientModel.setRoomId(-1);
				clientModel.setClientID(-1);
				new SignInActivity(clientModel);
				jFrame.dispose();
			}
			else
			{
				JOptionPane.showMessageDialog(null, response.getContents(), null, JOptionPane.ERROR_MESSAGE);
			}
		}
		catch(Exception e) {
			e.printStackTrace(new PrintWriter(Config.errors));
			LogFileWriter.Log(Config.errors.toString());
		}
	}
	
	private void ListeningEvents() {
		jBtnCreateRoom.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				displayAlertDialog(1);
			}
		});
		
		jBtnJoinRoom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				displayAlertDialog(2);
			}
		});
		
		jBtnViewRooms.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					new ViewRoomsActivity(clientModel);
					jFrame.dispose();
				} catch (IOException e1) {
					e1.printStackTrace(new PrintWriter(Config.errors));
					LogFileWriter.Log(Config.errors.toString());
				}
				
			}
		});
		jBtnLogout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					logOut();
				} catch (Exception e1) {
					e1.printStackTrace(new PrintWriter(Config.errors));
					LogFileWriter.Log(Config.errors.toString());
				}
			}
		});
	}
	
	private void initializeAllWithProperties() {
		//create room button
		jBtnCreateRoom.setPreferredSize(new Dimension(150,35));
		jBtnCreateRoom.setBackground(Color.WHITE);
		jBtnCreateRoom.setBorder(new LineBorder(Config.colorPrimary, 3));
		jBtnCreateRoom.setFocusPainted(false);
		
		//join room button
		jBtnJoinRoom.setPreferredSize(new Dimension(150,35));
		jBtnJoinRoom.setBackground(Color.WHITE);
		jBtnJoinRoom.setBorder(new LineBorder(Config.colorPrimary, 3));
		jBtnJoinRoom.setFocusPainted(false);
		
		//view all rooms button
		jBtnViewRooms.setPreferredSize(new Dimension(150,35));
		jBtnViewRooms.setBackground(Color.WHITE);
		jBtnViewRooms.setBorder(new LineBorder(Config.colorPrimary, 3));
		jBtnViewRooms.setFocusPainted(false);
		
		//logout button
		jBtnLogout.setPreferredSize(new Dimension(150,35));
		jBtnLogout.setBackground(Color.WHITE);
		jBtnLogout.setBorder(new LineBorder(Config.colorPrimary, 3));
		jBtnLogout.setFocusPainted(false);
		
		jFrame.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		Insets textTitle = new Insets(4, 100, 20, 4);
		Insets logoInsets = new InsetsUIResource(0, 200, 50, 0);
		Insets buttonInsets = new Insets(4, 200, 20, 4);
		
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 1.0;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = logoInsets;
		c.gridy = 1;
		
		//setting logo
		jLabel = new JLabel(new ImageIcon(iconLogo));
		jLabel.setBounds(0, 0, 100, 100);
		jLabel.setPreferredSize(new Dimension(150,150));
		jFrame.add(jLabel,c);
		
		//setting title
		c.gridx = 0;
		c.gridy = 2;
		c.insets = textTitle;
		jFrame.add(jLabelTitle,c);
		
		//setting create room button
		c.gridx = 0;
		c.gridy = 3;
		c.insets = buttonInsets;
		jFrame.add(jBtnCreateRoom,c);
		
		//setting join room button
		c.gridx = 0;
		c.gridy = 4;
		jFrame.add(jBtnJoinRoom,c);
		
		//setting view rooms button
		c.gridx = 0;
		c.gridy = 5;
		jFrame.add(jBtnViewRooms,c);
		
		//setting logout button
		c.gridx = 0;
		c.gridy = 6;
		jFrame.add(jBtnLogout,c);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setSize(864,614);
		jFrame.setResizable(false);
		jFrame.setLocationRelativeTo(null);
		jFrame.setVisible(true);
		
		ListeningEvents();
	}
	
	private void createAndJoinRoom(String rName, boolean create)
	{
		try {
			if(create)
				request = new Request(Request.Type.CREATE_ROOM.ordinal(),clientModel.getClientID(),clientModel.getRoomId(),rName);
			else
				request = new Request(Request.Type.JOIN_ROOM.ordinal(),clientModel.getClientID(),clientModel.getRoomId(),rName);
			
			ClientModel.objectOutputStream.writeObject(request);
			ClientModel.objectOutputStream.flush();
			Object obj =  ClientModel.objectInputStream.readObject();
			if( obj.getClass() == Response.class )
				response = (Response) obj;
			else
			{
				throw new Exception("Object returned is not of type Response. but of " + obj.getClass().toString() );
			}
			if( response.getSuccess())
			{
				int hashIndex = response.getContents().indexOf('#');
				clientModel.setRoomId(Integer.parseInt(response.getContents().substring(hashIndex+1, response.getContents().indexOf(" ", hashIndex))));
				new ChatActivity(clientModel);
				jFrame.dispose();
			}
			else
			{
				UIManager.put("OptionPane.okButtonText", "OK");
				JOptionPane.showMessageDialog(null, response.getContents(), null, JOptionPane.ERROR_MESSAGE);
			}
		}
		catch(Exception e) {
			e.printStackTrace(new PrintWriter(Config.errors));
			LogFileWriter.Log(Config.errors.toString());
		}
	}
	
	private void displayAlertDialog(int which) {
		JPanel jPanel = new JPanel();
		jPanel.setSize(new Dimension(200, 64));
		jPanel.setLayout(null);
		
		JTextField jTextField = new JTextField();
		jTextField.setBackground(Color.white);
		jTextField.setBounds(0, 0, 250, 35);
		
		jTextField.setHorizontalAlignment(SwingConstants.CENTER);
		jPanel.add(jTextField);
		
		if(which == 1)
			UIManager.put("OptionPane.okButtonText", "Create Room");
		else
			UIManager.put("OptionPane.okButtonText", "Join Room");
		
		int choice = JOptionPane.showOptionDialog(null, jPanel, "Enter Room Name", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
		if(choice == JOptionPane.OK_OPTION) {
			try {
				if(which == 1)
					createAndJoinRoom(jTextField.getText().toString(), true);
				else
					createAndJoinRoom(jTextField.getText().toString(), false);
			}
			catch(Exception e) {
				e.printStackTrace(new PrintWriter(Config.errors));
				LogFileWriter.Log(Config.errors.toString());
			}
		}
	}
}

