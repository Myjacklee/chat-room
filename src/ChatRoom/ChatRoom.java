package ChatRoom;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.omg.CORBA.PUBLIC_MEMBER;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;

public class ChatRoom extends JFrame {
	private JButton button_create_room;
	private JButton button_close_room;
	private JButton button_send_message;
	private JPanel contentPane;
	protected JTextField textField_IP;
	protected JTextField textField_PORT;
	protected JTextField textField_room_name;
	protected JTextArea textPane_online_users;
	protected JTextArea textPane_message;
	protected JTextArea textArea_send_message;
	private JScrollPane jsp_online_user;
	private JScrollPane jsp_message;
	private JScrollPane jsp_send_message;
	private boolean isConnected=false;
	private ArrayList<ClientThread> clients;
	private ServerSocket serverSocket;
	private ServerThread serverThread;
	private DefaultListModel listModel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatRoom chatRoom= new ChatRoom();
					chatRoom.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ChatRoom() {
		setTitle("聊天室 服务器端");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 695, 635);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		listModel = new DefaultListModel();
		
		JLabel lblIp = new JLabel("参数配置");
		lblIp.setFont(new Font("黑体", Font.PLAIN, 17));
		lblIp.setBounds(23, 18, 70, 33);
		contentPane.add(lblIp);
		
		JLabel lblIp_1 = new JLabel("IP：");
		lblIp_1.setVerticalAlignment(SwingConstants.BOTTOM);
		lblIp_1.setFont(new Font("黑体", Font.PLAIN, 17));
		lblIp_1.setBounds(23, 96, 46, 20);
		contentPane.add(lblIp_1);
		
		textField_IP = new JTextField();
		textField_IP.setBounds(106, 94, 140, 24);
		contentPane.add(textField_IP);
		textField_IP.setColumns(10);
		
		JLabel lblPort = new JLabel("PORT：");
		lblPort.setVerticalAlignment(SwingConstants.BOTTOM);
		lblPort.setFont(new Font("黑体", Font.PLAIN, 17));
		lblPort.setBounds(23, 133, 57, 20);
		contentPane.add(lblPort);
		
		textField_PORT = new JTextField();
		textField_PORT.setColumns(10);
		textField_PORT.setBounds(106, 131, 140, 24);
		contentPane.add(textField_PORT);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				if(isConnected){
					closeServer();
				}
				System.exit(0);
			}
		});
		
		button_create_room = new JButton("创建聊天室");
		button_create_room.setBounds(23, 190, 110, 35);
		contentPane.add(button_create_room);
		button_create_room.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(isConnected){
					JOptionPane.showMessageDialog(null, "已处于连接状态，不能重复连接！","错误",JOptionPane.ERROR_MESSAGE);
				}
				try {
					Integer port=Integer.parseInt(textField_PORT.getText());
					serverStart(port);
					textPane_message.append("服务器启动成功！当前端口号："+port+"\r\n");
					JOptionPane.showMessageDialog(null, "服务器启动成功！");
					textField_PORT.setEnabled(false);
					button_create_room.setEnabled(false);	
					button_close_room.setEnabled(true);
					button_send_message.setEnabled(true);
				} catch (Exception e2) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, e2.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		
		JLabel label = new JLabel("聊天室动态");
		label.setFont(new Font("黑体", Font.PLAIN, 17));
		label.setBounds(362, 18, 103, 33);
		contentPane.add(label);
		
		textPane_message = new JTextArea();
		jsp_message=new JScrollPane(textPane_message);
		jsp_message.setBounds(361, 55, 290, 275);
		contentPane.add(jsp_message);
		textPane_message.setEditable(false);
		
		JLabel label_1 = new JLabel("当前在线用户");
		label_1.setFont(new Font("黑体", Font.PLAIN, 17));
		label_1.setBounds(23, 240, 121, 33);
		contentPane.add(label_1);
		
		textPane_online_users = new JTextArea();
		jsp_online_user=new JScrollPane(textPane_online_users);
		jsp_online_user.setBounds(23, 280, 290, 258);
		contentPane.add(jsp_online_user);
		textPane_online_users.setEditable(false);
		
		JLabel label_2 = new JLabel("聊天室名：");
		label_2.setVerticalAlignment(SwingConstants.BOTTOM);
		label_2.setFont(new Font("黑体", Font.PLAIN, 17));
		label_2.setBounds(23, 57, 85, 20);
		contentPane.add(label_2);
		
		textField_room_name = new JTextField();
		textField_room_name.setColumns(10);
		textField_room_name.setBounds(106, 55, 139, 24);
		contentPane.add(textField_room_name);
		
		button_close_room = new JButton("关闭聊天室");
		button_close_room.setBounds(155, 190, 110, 35);
		contentPane.add(button_close_room);
		button_close_room.setEnabled(false);
		
	
		textArea_send_message = new JTextArea();
		jsp_send_message=new JScrollPane(textArea_send_message);
		jsp_send_message.setBounds(362, 363, 289, 125);
		contentPane.add(jsp_send_message);
		
		JLabel label_3 = new JLabel("消息编辑框");
		label_3.setFont(new Font("黑体", Font.PLAIN, 17));
		label_3.setBounds(362, 329, 103, 33);
		contentPane.add(label_3);
		
		button_send_message = new JButton("发送");
		button_send_message.setBounds(556, 503, 95, 35);
		contentPane.add(button_send_message);
		button_send_message.setEnabled(false);
		
		//打击发送按钮出发的服务时间
		button_send_message.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(!isConnected){
					JOptionPane.showMessageDialog(null, "服务器还未启动，不能够发送消息！","错误",JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(clients.size()==0){
					JOptionPane.showMessageDialog(null, "当前没有用户在线，无法发送消息","错误",JOptionPane.ERROR_MESSAGE);
					return;
				}
				String message=textArea_send_message.getText().trim();
				if(message==null||"".equals(message)){
					sendServerMessage(message);
					textPane_message.append("服务器:"+message+"\r\n");
					textArea_send_message.setText("");
				}
			}
		});
		
		//单击按钮停止服务器事件
		button_close_room.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(!isConnected){
					JOptionPane.showMessageDialog(null, "服务器还未启动，无需停止！","错误",JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					closeServer();
					button_create_room.setEnabled(true);
					button_close_room.setEnabled(false);
					button_send_message.setEnabled(false);
					textPane_message.append("服务器成功停止！\r\n");
					JOptionPane.showMessageDialog(null, "服务器成功停止！");
				} catch (Exception e3) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, "停止服务器发生异常！","错误",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	protected void sendServerMessage(String message) {
		// TODO Auto-generated method stub
		for(int i=clients.size()-1;i>=0;i--){
			clients.get(i).getWriter().println("服务器(多人消息):"+message);
			clients.get(i).getWriter().flush();
		}
	}

	protected void closeServer() {
		// TODO Auto-generated method stub
		try {
			if(serverThread!=null){
				serverThread.stop();
			}
			for(int i=clients.size()-1;i>=0;i--){
				//给所有在线用户发送关闭命令
				clients.get(i).getWriter().println("CLOSE");
				clients.get(i).getWriter().flush();
				//释放所有资源
				clients.get(i).stop();//停止这条为客户端服务的线程
				clients.get(i).reader.close();
				clients.get(i).writer.close();
				clients.get(i).socket.close();
				clients.remove(i);
			}
			if(serverSocket!=null){
				serverSocket.close();// 关闭服务器连接
			}
			listModel.removeAllElements();// 清空用户列表
			isConnected=false;
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
			isConnected=true;
		}
	}

	protected void serverStart(Integer port) throws IOException {
		// TODO Auto-generated method stub
		try {
			clients=new ArrayList<ClientThread>();	
			serverSocket=new ServerSocket(port);
			serverThread=new ServerThread(serverSocket);
			serverThread.start();	
			isConnected=true;
		} catch (BindException e) {
			// TODO: handle exception
			isConnected=false;
			throw new BindException("端口号已被占用，请换一个！");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			isConnected=false;
			throw new BindException("启动服务器异常！");
		}
		
	}	
	class ServerThread extends Thread{
		private ServerSocket serverSocket;
		//ServerThread构造方法
		public ServerThread(ServerSocket serverSocket){
			this.serverSocket=serverSocket;
		}
		public void run(){
			while(true){
				try {
					Socket socket =serverSocket.accept();
					ClientThread clientThread=new ClientThread(socket);
					clientThread.start();
					clients.add(clientThread);
					listModel.addElement(clientThread.getUser().getName());
					textPane_message.append(clientThread.getUser().getName()+" "+clientThread.getUser().getIp()+" 上线啦！\r\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			
		}
	}
	class ClientThread extends Thread{
		private Socket socket;
		private BufferedReader reader;
		private PrintWriter writer;
		private UserBean user;
		
		public BufferedReader getReader() {
			return reader;
		}

		public PrintWriter getWriter() {
			return writer;
		}

		public UserBean getUser() {
			return user;
		}

		public ClientThread(Socket socket){
			try {
				this.socket=socket;
				reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer=new PrintWriter(socket.getOutputStream());
				//接收客户端的基本信息
				String inf=reader.readLine();
				StringTokenizer stringTokenizer =new StringTokenizer(inf,"@");
				user=new UserBean(stringTokenizer.nextToken(),stringTokenizer.nextToken());
				//反馈当前连接成功信息
				writer.println(user.getName()+" "+user.getIp()+"与服务器连接成功！");
				writer.flush();
				//反馈当前在线用户信息
				if(clients.size()>0){
					String temp="";
					for(int i=clients.size()-1;i>=0;i--){
						temp+=(clients.get(i).getUser().getName()+"/"+clients.get(i).getUser().getIp())+"@";
					}
					writer.println("USERLIST@"+clients.size()+"@"+temp);
					writer.flush();
				}
				//向所有的在线用户发送该用户的上线动态
				for(int i= clients.size()-1;i>=0;i--){
					clients.get(i).getWriter().println("ADD@"+user.getName()+" "+user.getIp());
					clients.get(i).getWriter().flush();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		@Override
		public void run(){
			String message=null;
			try {
				while(true){
					message=reader.readLine();//接收客户端的消息
					if("CLOSE".equals(message)){
						textPane_message.append(this.getUser().getName()+" "+this.getUser().getIp()+" 下线\r\n");
						reader.close();
						writer.close();
						socket.close();
						//通知所有用户该用户的下线信息
						for(int i=clients.size()-1;i>=0;i--){
							clients.get(i).getWriter().println("DELETE@"+user.getName());
							clients.get(i).getWriter().flush();
						}
						listModel.removeElement(user.getName());
						for(int i=clients.size()-1;i>=0;i--){
							if(clients.get(i).getUser()==user){
								ClientThread temp=clients.get(i);
								clients.remove(i);
								temp.stop();
								return;
							}
						}
					}else{
						//将收到的消息转发出去
						dispatcherMessage(message);
					}
					
				}
			} catch (IOException e) {
				// TODO: handle exception
			}
			
		}
	}
	public void dispatcherMessage(String message) {
		// TODO Auto-generated method stub
		StringTokenizer stringTokenizer=new StringTokenizer(message,"@");
		String sourse=stringTokenizer.nextToken();
		String owner=stringTokenizer.nextToken();
		String content=stringTokenizer.nextToken();
		message="["+sourse+"]:"+content;
		textPane_message.append(message+"\r\n");
		if("ALL".equals(owner)){
			for(int i=clients.size()-1;i>=0;i++){
				clients.get(i).getWriter().println("(多人消息)"+message);
				clients.get(i).getWriter().flush();
			}
		}
	}
}
