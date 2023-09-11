//Name: Venkata Naga Rohith Chintakrinda
// Net ID: vxc210001

The brief steps to run this project:

1>: Here in order to connect to the UT Dallas DC machines dc01.utdallas.edu,dc02.utdallas.edu and dc03.utdallas.edu PUTTY has been used here.

2>: Next, Create a directory in home  and create three folders with one Server side (D1) and  two Client sides (D2, D3).

3>: Finally, Run server side code in one dc machine-dc01.utdallas.edu and client side codes in any other two dc machines.
     I have used dc02.utdallas.edu and dc03.utdallas.edu.

4>: The makefile has been created to compile the java files in the home directory(serverMain, ClientMain1 and ClientMain2).

5>: In order to run the Java files, run the below commands in respective DC Machines terminals:
	a) For Server : java serverMain
	b) For Client1: java ClientMain1
	c) For Client2: java ClientMain2

6>: After running the above commands, we can observe that:
	> The Server side has 600 bytes of data with the file F3 in which the data append from F1 and F2.
	> The two clients has the identical file F3 as in server side. 


