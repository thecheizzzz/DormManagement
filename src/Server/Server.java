package Server;

import java.net.ServerSocket;

public class Server {
	private static ServerSocket server;
	
	public Server() {
		try {
			server = new ServerSocket(3199);
			System.out.println("Running...");
			while(true) {
				ThreadHandler s = new ThreadHandler(server.accept());
				s.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new Server();
	}

}
