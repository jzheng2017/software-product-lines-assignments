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

### Week 2 Task 4
We have chosen to use parameter passing because it leads to cleaner code and we do not have to worry about a global state so we can test/debug the methods separately.

We use parameters to select different features, there are no invalid configurations in this system. In bigger systems the parameters would have to be checked before starting the system and an error would be shown to the user specifying the mistake in the selected configuration.

We have chosen to not implement variability for the Authentication feature, since every user within our system is required to authenticate.

We added the following optional parameters to the program: 

1. usernamecolors   - when given, every message send will be prepended by the selected color 
2. rot13            - when given, the program will use the rotate13 algoirthm for encryption/decryption.
3. reverse          - when given, the program will use the reverse string algoirthm for encryption/decryption.

Example command to run the program with rotate13 encryption and colors as username: "java spl/GUI.java usernamecolors rot13" 
