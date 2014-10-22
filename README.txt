CS6238Project
============================
Author: Lei Shao, Ruiting Qu
============================
Objective:
============================
The purpose of this program is to implement the strengthened authentication scheme described in the password hardening paper.
============================

Recommended Running Environment:

============================
System Environment:	

The final version of program works fine on OS X 10.9.2

Programming Language:

Java version "1.6.0_65"
Java(TM) SE Runtime Environment (build 1.6.0_65-b14-462-11M4609)
Java HotSpot(TM) 64-Bit Server VM (build 20.65-b04-462, mixed mode)

Programming Platform/Tools

Eclipse Standard/SDK
Version: Kepler Service Release 2

Imported Libraries

No external libraries imported, just JRE System Library[JavaSE - 1.6]

============================
To run the program:
============================
You can use provided JAR file, or:

1.Import the whole folder “KeystrokeHarden” as a project into Eclipse(It is actually the folder contains all files for the project), choose the imported project, right click then choose “exported” as “runnable JAR file”. Name it “Keystroke.jar”. Now having the JAR file, the test file for this program need to be put at the same folder of this JAR file. If you export JAR file by yourself, make sure to move the test file to right place.

Our test file “testfile.txt” is provided together with the JAR file “Keystroke.jar” we exported. So for convenience we highly recommend to directly run our JAR file.

To run the JAR file, open your terminal(We were under OS X 10.9.2).
Go to the directory of JAR and testfile.txt, then type:

java -jar Keystroke.jar

Make sure you are under the similar environment, with same version of java SE. Or we can not ensure that you can successfully run our program.

Warning: We also provide another test file named “testfile2.txt”. If you want to test it, you need to go into our source code to change the file input directory, or simply change this file’s name (or your own one) to “testfile.txt”. Since this input file name is fixed in our program.
============================
Usage of program
============================
After excecuting our program. It firstly come to the Initialization section.
The command line will prompt you to enter your username. That is for registering a user.
Just input it, make sure you input correct one that you want to register for, since it will not be shown as plaintext in the console.

For our test file, input “shaolei” as username.
Then the console prompt to input 8 digits password,
input any one you like, but you need to remember it. Also a digit checking will happen, so if you don’t input a 8 digits number, you need to redo it again.

Now the Initialization finished, which means you have registered an account.
Now it’s time to login…
To simplify, you only need to input your chosen username & password once. Then the test file will automatically be loading and running the authentication.
If you want to have a good result, input “shaolei”(This is the one that appears most in mine. Or you can input the one contains in your own test file if you test with your file)
then input the previous set password…

then ,  enter!!!

The authentication for all log-in attempts will done in several seconds or less.
The sequence number will be shown for each request,

If you input wrong username, which is not exist in the test file, all request will show “Wrong username”.
If you input wrong password, the ones for your username will not successfully login.

If you do all things correctly
You can see, for request from other username, it will show “Wrong username”
for right username, 
the first 8 requests will always be correct, since the history file is not full.
After 8 successful requests, 
If the new request has proper feature vector, it will show “Login success!!”, and update history file , compute the mean / standard deviation, encrypt the history file with new Hpwd, update polynomial, instruction table, etc…for detail you can read the report.
If  feature vector is not proper, it will show “Loggin failed”


Warning: since the login authentication is combined with Initialization, to have expected result, you need to keep them consistent. Which means, only if you login as the user/password you registered for can our program give you a proper authentication. That seems make sense.

Or you can also try some funny things…well…maybe surprising results…
