# CHATROOM - LiveChatServer
<table>
<tr>
<td>
LiveChatServer is a Live CHATROOM which allows multiple users (Clients) to chat to each other.
</td>
</tr>
</table>

<img src="http://iamrohitsuthar.000webhostapp.com/android/github/LiveChatServer/login.png" width="738">

## Features

- Multiuser Live Chat
- Awesome GUI + Console mode option
- Password Encryption
- Logs Tracking by writing into the file

## Usage

1. First run the Server Program by providing the four parameters - Port No, Datbase host, Database username, Database password

    ```sh
    java -jar server.jar port_no datbase_host your_database_username your_database_password
    ```
2. Run the Client program by providing the server host and port no
  
    ```sh
    java -jar client.jar server_host server_port_no
    ```
3. The above command opens the GUI mode. If you want to open the standard console mode then specify extra **- -console** option while running the client program

    ```sh
    java -jar client.jar server_host server_port_no --console
    ```  
    
### Console Commands or hidden commands

1. For Sending personal message in live chat group
    
    ```sh
    @username **message**
    ```
2. For Exiting from Live Chat Group
    
    ```sh
    sv_exit
    ```
3. For logout

    ```sh
    sv_logout
    ```
4. For creating secret chat room

    ```sh
    i) First create the new chat room by leaving the name of the chatroom as empty or by providing spaces.
    ii) Now you can join the above created room by specifying empty name or the spaces you provided while creating the room in join room option.
    ```
    
    
### Development
Want to contribute? Great!<br/>

To fix a bug or enhance an existing module, follow these steps:

- Fork the repo
- Create a new branch (`git checkout -b improve-feature`)
- Make the appropriate changes in the files
- Add changes to reflect the changes made
- Commit your changes (`git commit -am 'Improve feature'`)
- Push to the branch (`git push origin improve-feature`)
- Create a Pull Request 
  
 
### Bug / Feature Request

If you find a bug (the website couldn't handle the query and / or gave undesired results), kindly open an issue [here](https://github.com/iamrohitsuthar/LiveChatServer/issues/new).

If you'd like to request a new function, feel free to do so by opening an issue [here](https://github.com/iamrohitsuthar/LiveChatServer/issues/new).

## Screenshots

#### Sign In Activity
<img src="http://iamrohitsuthar.000webhostapp.com/android/github/LiveChatServer/sign-in.png" width="738">

#### Main Menu Activity
<img src="http://iamrohitsuthar.000webhostapp.com/android/github/LiveChatServer/menu_options.png" width="738">

#### View Rooms Activty
<img src="http://iamrohitsuthar.000webhostapp.com/android/github/LiveChatServer/view_rooms.png" width="738">

#### Chat Activity
<img src="http://iamrohitsuthar.000webhostapp.com/android/github/LiveChatServer/chat_activity.png" width="738">

