# CHATROOM - LiveChatServer 
[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)  [![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com)       [![](https://img.shields.io/github/release/iamrohitsuthar/LiveChatServer)](https://github.com/iamrohitsuthar/LiveChatServer/releases/tag/v4.0)                        [![](https://img.shields.io/github/license/iamrohitsuthar/LiveChatServer)](https://github.com/iamrohitsuthar/LiveChatServer/blob/master/LICENSE)
<table>
<tr>
<td>
LiveChatServer is a Live CHATROOM which allows multiple users (Clients) to chat with each other.
</td>
</tr>
</table>

<img src="http://iamrohitsuthar.000webhostapp.com/android/github/LiveChatServer/login.png" width="738">

## Features

- Multiuser Live Chat
- Awesome GUI + Console mode option
- Password Encryption
- Error Tracking by writing them into a log file

## Usage

1. First run the Server Program by providing the four parameters - Port No, Datbase host, Database username, Database password

    ```sh
    java -jar server.jar port_no datbase_host your_database_username your_database_password
    ```
2. Run the Client program by providing the server host and port no
  
    ```sh
    java -jar client.jar server_host server_port_no
    ```
3. The above command opens the GUI mode. If you want to open the standard console mode then specify extra **--console** option while running the client program

    ```sh
    java -jar client.jar server_host server_port_no --console
    ```  
    
### Console Commands or hidden commands ( can be typed as a message )

1. For Sending personal message in live chat group
    
    ```sh
    @username **message**
    ```
    ( this will show up as a **PM** on that particular user's screen )
2. For Exiting from Live Chat Group
    
    ```sh
    sv_exit
    ```
3. For logout

    ```sh
    sv_logout
    ```
4. For getting the list of online users in room

    ```sh
    sv_showusers
    ```
    
### It's not a BUG. It's a feature!
  #### Create a SECRET ROOM!!
    
    i) First create a new chat room by leaving the name of the chatroom empty or by providing spaces.
    ii) Now you can join the above created room by specifying empty name or the spaces you provided while creating the room in join room option.
    iii) This room won't be visible / displayed in the view rooms list!
    
    
    
### Collaborate with us!
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

If you find a bug (the application couldn't handle the query and / or gave undesired results), kindly open an issue [here](https://github.com/iamrohitsuthar/LiveChatServer/issues/new).

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


## License
See [LICENSE](LICENSE)
