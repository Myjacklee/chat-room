package ChatRoom;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Toolkit;
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
import java.util.Date;
import java.text.SimpleDateFormat;
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
	protected JTextField textField_PORT;
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
	private JList userList;
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
		setResizable(false);
		setTitle("OO聊天室 服务器端");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().createImage(this.getClass().getResource("../icon/OO.png")));

		setBounds(100, 100, 695, 560);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		listModel = new DefaultListModel();
		userList=new JList(listModel);
		userList.setFont(new Font("华文楷体", Font.PLAIN, 20));
		userList.setLocation(27, 0);
		
		JLabel lblIp = new JLabel("参数配置");
		lblIp.setFont(new Font("黑体", Font.PLAIN, 17));
		lblIp.setBounds(27, 13, 70, 33);
		contentPane.add(lblIp);
		
		JLabel lblPort = new JLabel("PORT：");
		lblPort.setVerticalAlignment(SwingConstants.BOTTOM);
		lblPort.setFont(new Font("黑体", Font.PLAIN, 17));
		lblPort.setBounds(27, 53, 57, 20);
		contentPane.add(lblPort);
		
		textField_PORT = new JTextField();
		textField_PORT.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		textField_PORT.setColumns(10);
		textField_PORT.setBounds(81, 47, 73, 33);
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
		button_create_room.setBounds(27, 90, 110, 35);
		contentPane.add(button_create_room);
		button_create_room.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(isConnected){
					JOptionPane.showMessageDialog(null, "已处于连接状态，不能重复连接！","错误",JOptionPane.ERROR_MESSAGE);
				}
				try {
					Integer port=Integer.parseInt(textField_PORT.getText());
					serverStart(port);
					textPane_message.append(getTime()+"服务器启动成功！当前端口号："+port+"\r\n");
					JOptionPane.showMessageDialog(null, "服务器启动成功！");
					textField_PORT.setEnabled(false);
					button_create_room.setEnabled(false);	
					button_close_room.setEnabled(true);
					button_send_message.setEnabled(true);
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		JLabel label = new JLabel("聊天室动态");
		label.setFont(new Font("黑体", Font.PLAIN, 17));
		label.setBounds(283, 13, 103, 33);
		contentPane.add(label);
		
		textPane_message = new JTextArea();
		textPane_message.setLineWrap(true);
		textPane_message.setFont(new Font("华文楷体", Font.PLAIN, 20));
		jsp_message=new JScrollPane(textPane_message);
		jsp_message.setBounds(283, 55, 368, 275);
		contentPane.add(jsp_message);
		textPane_message.setEditable(false);
		textPane_message.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				int length=textPane_message.getText().length();
				textPane_message.setCaretPosition(length);
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		JLabel label_1 = new JLabel("当前在线用户");
		label_1.setFont(new Font("黑体", Font.PLAIN, 17));
		label_1.setBounds(23, 138, 121, 33);
		contentPane.add(label_1);
		
		jsp_online_user=new JScrollPane(userList);
		jsp_online_user.setBounds(23, 178, 242, 320);
		contentPane.add(jsp_online_user);
		
		button_close_room = new JButton("关闭聊天室");
		button_close_room.setBounds(155, 90, 110, 35);
		contentPane.add(button_close_room);
		button_close_room.setEnabled(false);
		
	
		textArea_send_message = new JTextArea();
		textArea_send_message.setLineWrap(true);
		textArea_send_message.setFont(new Font("华文楷体", Font.PLAIN, 20));
		jsp_send_message=new JScrollPane(textArea_send_message);
		jsp_send_message.setBounds(283, 363, 368, 95);
		contentPane.add(jsp_send_message);
		
		JLabel label_3 = new JLabel("消息编辑框");
		label_3.setFont(new Font("黑体", Font.PLAIN, 17));
		label_3.setBounds(283, 329, 368, 33);
		contentPane.add(label_3);
		
		button_send_message = new JButton("发送");
		button_send_message.setBounds(556, 463, 95, 35);
		contentPane.add(button_send_message);
		button_send_message.setEnabled(false);
		
		//打击发送按钮出发的服务时间
		button_send_message.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
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
					JOptionPane.showMessageDialog(null, "消息不能为空！","错误",JOptionPane.ERROR_MESSAGE);

				}
				sendServerMessage(message);
				textPane_message.append(getTime()+"[服务器]:"+message+"\r\n");
				textArea_send_message.setText("");				
			}
		});
		//单击按钮停止服务器事件
		button_close_room.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!isConnected){
					JOptionPane.showMessageDialog(null, "服务器还未启动，无需停止！","错误",JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					closeServer();
					button_create_room.setEnabled(true);
					button_close_room.setEnabled(false);
					button_send_message.setEnabled(false);
					textField_PORT.setEditable(true);
					textPane_message.append("服务器成功停止！\r\n");
					JOptionPane.showMessageDialog(null, "服务器成功停止！");
				} catch (Exception e3) {
					JOptionPane.showMessageDialog(null, "停止服务器发生异常！","错误",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	private String getTime(){
		Date date=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return "["+sdf.format(date)+"]";
	}
	protected void sendServerMessage(String message) {
		for(int i=clients.size()-1;i>=0;i--){
			clients.get(i).getWriter().println("[服务器]:"+message);
			clients.get(i).getWriter().flush();
		}
	}
	protected void closeServer() {
		try {
			//首先接收用户连接请求的线程，以拒绝新的用户连接
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
			e.printStackTrace();
			isConnected=true;
		}
	}
	protected void serverStart(Integer port) throws IOException {
		try {
			clients=new ArrayList<ClientThread>();	
			serverSocket=new ServerSocket(port);
			serverThread=new ServerThread(serverSocket);
			serverThread.start();	
			isConnected=true;
		} catch (BindException e) {
			isConnected=false;
			throw new BindException("端口号已被占用，请换一个！");
		}catch (Exception e) {
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
					textPane_message.append(getTime()+clientThread.getUser().getName()+" "+clientThread.getUser().getIp()+" 上线啦！\r\n");
				} catch (IOException e) {
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
				writer.println("[服务器]"+user.getName()+" "+user.getIp()+"与服务器连接成功！");
				writer.flush();
				//反馈当前在线用户信息
				if(clients.size()>0){
					String temp="";
					for(int i=clients.size()-1;i>=0;i--){
						temp+=(clients.get(i).getUser().getName()+"@"+clients.get(i).getUser().getIp())+"@";
					}
					writer.println("USERLIST@"+clients.size()+"@"+temp);
					writer.flush();
				}
				//向所有的在线用户发送该用户的上线动态
				for(int i= clients.size()-1;i>=0;i--){
					clients.get(i).getWriter().println("ADD@"+user.getName()+"@"+user.getIp());
					clients.get(i).getWriter().flush();
				}
			} catch (IOException e) {
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
						textPane_message.append(getTime()+this.getUser().getName()+" "+this.getUser().getIp()+" 下线\r\n");
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
				e.printStackTrace();
			}
		}
	}
	//接收到各个用户的消息后转发消息
	public void dispatcherMessage(String message) {
		StringTokenizer stringTokenizer=new StringTokenizer(message,"@");
		String sourse=stringTokenizer.nextToken();
		String owner=stringTokenizer.nextToken();
		String content=stringTokenizer.nextToken();
		message="["+sourse+"]:"+content;
		textPane_message.append(getTime()+message+"\r\n");
		if("ALL".equals(owner)){
			for(int i=clients.size()-1;i>=0;i--){
				if(clients.get(i).getUser().getName().equals(sourse)){
					continue;
				}
				clients.get(i).getWriter().println(message);
				clients.get(i).getWriter().flush();
			}
		}
	}
}
