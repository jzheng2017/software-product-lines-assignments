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
We added the following optional parameters to the program: 

1. usernamecolors   - when given, every message send will be prepended by the selected color 
2. rot13            - when given, the program will use the rotate13 algoirthm for encryption/decryption.
3. reverse          - when given, the program will use the reverse string algoirthm for encryption/decryption.

Example command to run the program with rotate13 encryption and colors as username: "java spl/GUI.java usernamecolors rot13" 
