# CHATROOM - LiveChatServer
<table>
<tr>
<td>
LiveChatServer is a Live CHATROOM which allows multiple users (Clients) to chat to each other.
</td>
</tr>
</table>

<img src="http://iamrohitsuthar.000webhostapp.com/android/github/LiveChatServer/login.png" width="738">

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
