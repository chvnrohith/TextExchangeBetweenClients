//Venkata Naga Rohith Chintakrinda (VXC210001)
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;

//This is the client1 class to send/receive the data to the server 
public class ClientMain1 {

	private static Socket socket;
	private static String fileName;
	private static BufferedReader in;
	private static BufferedWriter out;
	private static PrintStream os;
	private static DataOutputStream dataOutputStream = null;
	private static DataInputStream dataInputStream = null;
	public static String sendpath = "F1.txt";
	public static String receivepath = "F3.txt";

	public static void main(String[] args) throws IOException {
		while (true) {
			try {
				//The client is connected to the dc01 machine where the server is running, that is present on the utdallas server
				socket = new Socket("dc01.utdallas.edu", 8061);
				
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

				dataInputStream = new DataInputStream(socket.getInputStream());
				dataOutputStream = new DataOutputStream(socket.getOutputStream());

				os = new PrintStream(socket.getOutputStream());

				sendFile(sendpath);	
				
				//Below is used to receive data chunk in bytes from the file that is present on the server.

				int bytesRead;
				InputStream in = socket.getInputStream();
				long size = 0;
				DataInputStream clientData = new DataInputStream(in);

				OutputStream output = new FileOutputStream(receivepath);
				size = clientData.readLong();
				byte[] buffer = new byte[200];
				while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
					output.write(buffer, 0, bytesRead);
					String string = new String(buffer);
					System.out.println("No. of bytes sent from the client: "+bytesRead);
			        System.out.println("Data Received from Client: "+string);
					size -= bytesRead;
				}

				output.close();
				in.close();

				System.out.println("File " + receivepath + " received from Server.");

				dataInputStream.close();
				dataOutputStream.close();

				socket.close();
				System.exit(1);

			} catch (Exception e) {
				System.err.println("Cannot connect to the server, try again later.");
				System.exit(1);
			}
		}

	}


	//This function is used to send the data chunk in bytes to the server
	private static void sendFile(String path) throws Exception {
		int bytes = 0;
		File file = new File(path);
		FileInputStream fileInputStream = new FileInputStream(file);

		// send file size
		dataOutputStream.writeLong(file.length());
		// break file into chunks
		byte[] buffer = new byte[100];
		while ((bytes = fileInputStream.read(buffer)) != -1) {
			dataOutputStream.write(buffer, 0, bytes);
			dataOutputStream.flush();
		}
		fileInputStream.close();
	}

}
;
