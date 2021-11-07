Introduction
--
The Chatroom client and server is a school project made in PCSS on AAU CPH made by: Nicolaj Junget Dehn Frederiksen, Martin Øhlers Petersen, Nick Blume, Amalie Mørck, Christian Sonne Rosendahl and Sebastian Ottosen.

The Chatroom illustates basic function of multithreaded server.

When connected to the server with a username, it is possible to join the group chat.

Make sure you have installed the Java SE Development Kit before running this program

You can download it at: https://www.oracle.com/java/technologies/downloads/

Step-by-Step guide to initialize Server
--

(
This should be done, before starting the client
)

*
    Download the repository to you desktop as a zip file and unpack it here.
*
    Open up cmd by pressing windows+r and type cmd and press enter
*
    Use the function cd to change directory to the loacation of the server file in the project (Followed by pressing enter).
    
    (example: cd C:\Users\USER\Desktop\MiniP-master\MiniP-master\src)
*
    Then run the compiler on the java file by typing: javac Server.java (Folloeded by pressing enter).
*
    Then run the file by typing: java Server (Folloeded by pressing enter).
*
    The server should be running by now.
    
Step-by-Step guide to initialize Server
--

(
This should be done, after starting the server
)

*
    Open up another cmd by pressing windows+r and type cmd and press enter
*
    Use the function cd to change directory to the loacation of the server file in the project (Followed by pressing enter).
    
    (example: cd C:\Users\USER\Desktop\MiniP-master\MiniP-master\src)
*
    Then run the compiler on the java file by typing: javac Client.java (Folloeded by pressing enter).
*
    Then run the file by typing: java Client (Folloeded by pressing enter).
*
    The Client should be started by now and you should be able to chose a Username you wish to connect to the server with and press enter.
*
    The Client should be connected to the server now.
*
    You are now in the lobby, from here you can type 'join' in order to connect to the group chat.
*
    Once in group chat you should be able to chat with the users connected to the groupchat.
*
    if you wish to leave at any point type 'quit' and you will be disconnected from the server. (you will have to ruin the java file again in order to connect)

Note:

if you want to run multiple clients open up another cmd: change directory to the src folder using 'cd' and run java Client again.

