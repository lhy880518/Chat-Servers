package application;

import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Client {
	
	Socket socket;
	
	public Client(Socket socket) {
		this.socket = socket;
		receive();
	}
	
	// Ŭ���̾�Ʈ�κ��� �޽����� ���� �޴� �޼ҵ��Դϴ�.
	public void receive() {
		Runnable thread = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					while(true) {
						InputStream in = socket.getInputStream();
						byte[] buffer = new byte[512];
						int length = in.read(buffer);
						while(length == -1) throw new IOException();
						System.out.println("[�޼��� ���� ����]"
								+ socket.getRemoteSocketAddress()
								+ ": " + Thread.currentThread().getName());
						String message = new String(buffer ,0 ,length, "UTF-8");
						for(Client client : Main.clients) {
							System.out.println("1\n");
							client.send(message);
						}
					}
				}catch(Exception e) {
					e.printStackTrace();
					try {
						System.out.println("�޼��� ���� ���� "
								+ socket.getRemoteSocketAddress()
								+ ": " + Thread.currentThread().getName());
					}catch (Exception e2) {
						
					}
				}
			}
		};
		Main.threadPool.submit(thread);
	}
	
	// Ŭ���̾�Ʈ�κ��� �޽����� �����ϴ� �޼ҵ��Դϴ�.
	public void send(String message) {
		Runnable thread = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					System.out.println("���� �޼��� : "+message);
					OutputStream out = socket.getOutputStream();
					byte[] buffer = message.getBytes("UTF-8");
					out.write(buffer);
					out.flush();
				}catch (Exception e) {
					// TODO: handle exception
					try {
						System.out.println("�޼��� �۽� ���� "
								+ socket.getRemoteSocketAddress()
								+ ": " + Thread.currentThread().getName());
						Main.clients.remove(Client.this);
					}catch (Exception e2) {
						// TODO: handle exception
						e2.printStackTrace();
					}
				}
				
			}
		};
		Main.threadPool.submit(thread);
	}
}
