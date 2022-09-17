Repository for the assignments in the Software Product Lines course

To run the ChatApp run the following commands:
- cd src/ 
Start the server 
- java spl.Server  

Start a client or a number of clients
- java spl.GUI
- Select a color in the GUI
- Connect to the server
- Type /auth password in the chat to authenticate
- Type in the chat to communicate
- The encrypted chat is logged to src/chat_logs.txt

tested with: openjdk 11.0.16 2022-07-19

### Week 2 Task 5
We selected a Strategy Pattern for the encryption so that a different encryption algorithm can be chosen, as a paid feature for example. By using the Strategy pattern it allows us to easily change from implementation without changing a lot of code.

For the second pattern we chose the Factory Pattern which allows us to construct the implementation based on the parameter that was provided.
For instance if the "rot13" value was provided then the factory can construct this rot13 encryption implementation and use that for encrypting/decrypting the messages.

The features can be selected by the user using commandline parameters in the same way as for task 4.

**NOTE**

To enable encryption you have to provide "rot13" or "reverse" (the encryption methods) as the first argument in the list of arguments. To enable chat color you provide "usernamecolors" as the second argument.
To disable either feature you can 