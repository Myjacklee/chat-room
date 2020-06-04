package ChatRoom;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatRoomServer implements Runnable
{
	private String IP;
	private Integer POST;
	private String roomNameString;
	public ChatRoomServer(ChatRoom chatRoom) {
		// TODO Auto-generated constructor stub
		this.IP=chatRoom.textField_IP.getText();
		this.POST=Integer.valueOf(chatRoom.textField_PORT.getText());
		this.roomNameString=chatRoom.textField_room_name.getText();
	}
	@Override
	public void run(){
		try {
			ServerSocket serverSocket = new ServerSocket(POST);
			Socket socket=serverSocket.accept();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
