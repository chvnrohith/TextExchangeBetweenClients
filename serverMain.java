//Venkata Naga Rohith Chintakrinda (VXC210001)
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//The server main class is to communicate with the clients to send/receive data
public class serverMain {

	public static ServerSocket serverSocket;
	private static Socket clientSocket = null;
	//port used to connect and communicate
	public static final int port = 8061;
	static String path = "F3.txt";

	private static ArrayList<ClientHandler> clients = new ArrayList<>();
	private static ExecutorService pool = Executors.newFixedThreadPool(4);
	
	/* In this main function, server socket connection is created and it accepts the client connection
	and redirects to the clientHandler class to perform remaining actions. */
	public static void main(String[] args) throws IOException {

		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Heyy!! Server started.");
			System.out.println("Port "+ port +" is now open to connect.");
		} catch (Exception e) {
			System.err.println("Port already in use.");
			System.exit(1);
		}

		while (!serverSocket.isClosed()) {
			try {
				clientSocket = serverSocket.accept();
				System.out.println("Accepted client connection");
				ClientHandler t = new serverMain().new ClientHandler(clientSocket,clients);
				clients.add(t);
				pool.execute(t);

				
			} catch (Exception e) {
				System.err.println("Error in connection attempt.");
			}
		}

		
	}
	
	// This class is mainly used to handle the data transmission between the client and the server.
	public class ClientHandler implements Runnable {

		private BufferedReader in = null;
		private  DataOutputStream dataOutputStream = null;
		private Socket client;
		private  DataInputStream dataInputStream = null;
		private  ArrayList<ClientHandler> clients;
		
		//This constructor is used to create the Input and output streams.
		public ClientHandler(Socket client, ArrayList<ClientHandler> clients) throws IOException {
			this.client = client;
			this.clients = clients;
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			dataInputStream = new DataInputStream(client.getInputStream());
			dataOutputStream = new DataOutputStream(client.getOutputStream());
		}
		
		@Override
		public void run() {
			try {
				
				receiveFile(path);
				if(clients.size()==2){
					sendtoAllFile(path, clients);
					client.close();
					System.exit(1);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//This function is used to receive data from the client and write it to the file.
		private void receiveFile(String fileName) throws Exception {

			int bytes = 0;
			FileOutputStream fileOutputStream = new FileOutputStream(fileName,true);

			long size = dataInputStream.readLong();
			byte[] buffer = new byte[100];
			while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
				fileOutputStream.write(buffer, 0, bytes);
				String string = new String(buffer);
				System.out.println("No. of bytes sent from the client: "+bytes);
		        System.out.println("Data Received from Client: "+string);
				size -= bytes;
			}
			System.out.println("File " + fileName + " created with the data from client.");

			fileOutputStream.close();
		}


		//This function is used to read the data from the file and send the data to all the clients that are connected
		public void sendtoAllFile(String fileName, ArrayList<ClientHandler> clients) throws Exception {
			int bytes = 0;
			File file = new File(fileName);
			FileInputStream fileInputStream = null ;
			for (ClientHandler aclient : clients) {
				// send file size
				fileInputStream = new FileInputStream(file);
				aclient.dataOutputStream.writeLong(file.length());
				// break file into chunks
				byte[] buffer = new byte[200];
				while ((bytes = fileInputStream.read(buffer)) != -1) {
					aclient.dataOutputStream.write(buffer, 0, bytes);
					aclient.dataOutputStream.flush();
				}

			}
			System.out.println("File " + fileName + " sent to both the clients");
			fileInputStream.close();

		}

	}


}

