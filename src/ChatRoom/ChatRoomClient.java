package ChatRoom;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;


import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;

public class ChatRoomClient extends JFrame {

	private JList userList;
	
	private JPanel contentPane;
	private JTextField textField_IP;
	private JTextField textField_PORT;
	private JTextField textField_nickname;
	private JButton button_join_logout;
	private JLabel label_1;
	private JScrollPane jsp_online_users;
	private JTextArea textPane_online_users;
	private JLabel label_2;
	private JTextArea textPane_get_message;
	private JScrollPane jsp_get_message;
	private JTextArea textPane_send_message;
	private JScrollPane jsp_send_message;
	private JLabel label_3;
	private JButton button_send_message;
	private boolean isConnected;

	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;
	private DefaultListModel listModel;
	private HashMap<String, UserBean> onlineUsers;
	private MesssageThread messsageThread;
	private JButton button_close;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatRoomClient frame = new ChatRoomClient();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ChatRoomClient() {
		setTitle("聊天室客户端");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 691, 559);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		onlineUsers=new HashMap<String, UserBean>();
		
		listModel=new DefaultListModel();
		userList=new JList(listModel);
		JLabel lblNewLabel = new JLabel("聊天室配置");
		lblNewLabel.setFont(new Font("黑体", Font.PLAIN, 17));
		lblNewLabel.setBounds(31, 14, 91, 31);
		contentPane.add(lblNewLabel);
		
		JLabel lblIp = new JLabel("IP：");
		lblIp.setFont(new Font("黑体", Font.PLAIN, 17));
		lblIp.setBounds(31, 44, 53, 31);
		contentPane.add(lblIp);
		
		JLabel lblPort = new JLabel("PORT：");
		lblPort.setFont(new Font("黑体", Font.PLAIN, 17));
		lblPort.setBounds(31, 83, 53, 31);
		contentPane.add(lblPort);
		
		textField_IP = new JTextField("127.0.0.1");
		textField_IP.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		textField_IP.setBounds(84, 44, 175, 33);
		contentPane.add(textField_IP);
		textField_IP.setColumns(10);
		
		textField_PORT = new JTextField();
		textField_PORT.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		textField_PORT.setColumns(10);
		textField_PORT.setBounds(84, 83, 175, 33);
		contentPane.add(textField_PORT);
		
		JLabel label = new JLabel("昵称：");
		label.setFont(new Font("黑体", Font.PLAIN, 17));
		label.setBounds(31, 123, 53, 31);
		contentPane.add(label);
		
		textField_nickname = new JTextField();
		textField_nickname.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		textField_nickname.setColumns(10);
		textField_nickname.setBounds(84, 123, 175, 33);
		contentPane.add(textField_nickname);
		
		button_join_logout = new JButton("加入聊天室");
		button_join_logout.setBounds(31, 164, 110, 35);
		contentPane.add(button_join_logout);
		button_join_logout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int port;
				String nickname;
				String ip;
				if(isConnected){
					JOptionPane.showMessageDialog(null, "已处于连接状态，不能重复连接！","错误",JOptionPane.ERROR_MESSAGE);
				}
				try {
					try {
						port=Integer.parseInt(textField_PORT.getText().trim());
					} catch (NumberFormatException e2) {
						throw new Exception("端口号不符合要求！应该为整数！");
					}
					ip=textField_IP.getText().trim();
					nickname=textField_nickname.getText().trim();
					if("".equals(nickname)||"".equals(ip)){
						throw new Exception("姓名和服务器不能为空！");
					}
					boolean connectFlag=connectServer(port,ip,nickname);
					if(!connectFlag){
						throw new Exception("与服务器连接失败！");
					}
					setTitle(getTitle()+" 当前用户："+nickname);
					button_send_message.setEnabled(true);
					button_join_logout.setEnabled(false);
					button_close.setEnabled(true);
					textField_IP.setEditable(false);
					textField_PORT.setEditable(false);
					textField_nickname.setEditable(false);
					listModel.addElement(nickname);
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		label_1 = new JLabel("当前在线人员");
		label_1.setFont(new Font("黑体", Font.PLAIN, 17));
		label_1.setBounds(31, 206, 128, 31);
		contentPane.add(label_1);
		
		jsp_online_users=new JScrollPane(userList);
		jsp_online_users.setBounds(31,234,228,261);
		contentPane.add(jsp_online_users);
		
		label_2 = new JLabel("聊天室实时动态");
		label_2.setFont(new Font("黑体", Font.PLAIN, 17));
		label_2.setBounds(277, 14, 128, 31);
		contentPane.add(label_2);
		
		textPane_get_message = new JTextArea();
		textPane_get_message.setEditable(false);
		jsp_get_message=new JScrollPane(textPane_get_message);
		jsp_get_message.setBounds(277, 44, 366,261);
		contentPane.add(jsp_get_message);
		
		textPane_send_message=new JTextArea();
		jsp_send_message=new JScrollPane(textPane_send_message);
		jsp_send_message.setBounds(277, 342, 366, 108);
		contentPane.add(jsp_send_message);
		
		label_3 = new JLabel("文本编辑框");
		label_3.setFont(new Font("黑体", Font.PLAIN, 17));
		label_3.setBounds(277, 313, 128, 31);
		contentPane.add(label_3);
		
		button_send_message = new JButton("发送");
		button_send_message.setBounds(563, 463, 80, 35);
		contentPane.add(button_send_message);
		button_send_message.setEnabled(false);
		button_send_message.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!isConnected){
					JOptionPane.showMessageDialog(null,"还未连接聊天室服务器，不能够发送消息","错误",JOptionPane.ERROR_MESSAGE);
					return;
				}
				String message=textPane_send_message.getText().trim();
				if("".equals(message)){
					JOptionPane.showMessageDialog(null, "发送的消息不能够为空！","错误",JOptionPane.ERROR_MESSAGE);
					return;
				}
				sendMessage(textField_nickname.getText() + "@" + "ALL" + "@" + message);
				textPane_get_message.append(getTime()+"["+textField_nickname.getText()+"]"+message+"\r\n");
				textPane_send_message.setText("");
			}
		});
		
		button_close = new JButton("退出聊天室");
		button_close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!isConnected){
					JOptionPane.showMessageDialog(null, "已处于断开状态","错误",JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					boolean flag=closeConnection();
					if(flag==false){
						throw new Exception("断开连接发生异常");
					}
					JOptionPane.showMessageDialog(null, "成功断开连接");
					textPane_get_message.append(getTime()+"与服务器:"+textField_IP.getText()+"端口号:"+textField_PORT.getText()+" 成功断开连接");
					button_close.setEnabled(false);
					button_join_logout.setEnabled(true);
					button_send_message.setEnabled(false);
					textField_IP.setEditable(true);
					textField_nickname.setEditable(true);
					textField_PORT.setEditable(true);
					listModel.removeAllElements();
				} catch (Exception e3) {
					JOptionPane.showMessageDialog(null,e3.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		button_close.setBounds(149, 163, 110, 35);
		button_close.setEnabled(false);
		contentPane.add(button_close);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowsClosing(WindowEvent e){
				if(isConnected){
					closeConnection();
				}
				System.exit(0);
			}
		});
	}

	protected synchronized boolean closeConnection() {
		try {
			sendMessage("CLOSE");
			messsageThread.stop();
			if(reader!=null){
				reader.close();
			}
			if(writer!=null){
				writer.close();
			}
			if(socket!=null){
				socket.close();
			}
			isConnected=false;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			isConnected=true;
			return false;
		}
	}

	protected boolean connectServer(int port, String ip, String nickname) {
		try {
			//创建客户端socket
			socket =new Socket(ip,port);
			writer=new PrintWriter(socket.getOutputStream());
			reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			sendMessage(nickname+"@"+socket.getLocalAddress().toString());
			messsageThread=new MesssageThread(reader,textPane_get_message);
			messsageThread.start();
			isConnected=true;
			return true;
		} catch (Exception e) {	
			textPane_get_message.append(getTime()+"与IP："+ip+" 端口号："+port+" 的服务器连接失败\r\n");
			isConnected=false;
			return false;
		}
	}
	//发送消息
	private void sendMessage(String string) {
		writer.println(string);
		writer.flush();
	}
	private String getTime(){
		Date date=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return "["+sdf.format(date)+"]";
	}
	class MesssageThread extends Thread{
		private BufferedReader reader;
		private JTextArea textArea;
		public MesssageThread(BufferedReader reader,JTextArea textArea) {
			this.reader=reader;
			this.textArea=textArea;
		}
		public synchronized void closeCon() throws Exception{
			listModel.removeAllElements();
			if(reader!=null){
				reader.close();
			}
			if(writer!=null){
				writer.close();
			}
			if(socket!=null){
				socket.close();
			}
			isConnected=false;
		}
		public void run(){
			String message="";
			while(true){
				try {
					message=reader.readLine();
					StringTokenizer stringTokenizer=new StringTokenizer(message,"@");
					String command=stringTokenizer.nextToken();
					if("CLOSE".equals(command)){	//服务器关闭聊天室，所有在线用户全部下线
						textPane_get_message.append(getTime()+"服务器已经关闭!\r\n");
						closeCon();
						return;
					}else if("ADD".equals(command)){	//用户上线更新列表
						String username;
						String userIP;
						if((username=stringTokenizer.nextToken())!=null&&(userIP=stringTokenizer.nextToken())!=null){
							UserBean user=new UserBean(username, userIP);
							onlineUsers.put(username,user);
							listModel.addElement(username);
						}
					}else if("DELETE".equals(command)){  //有用户下线，更新用户列表
						String username=stringTokenizer.nextToken();
						UserBean user=onlineUsers.get(username);
						onlineUsers.remove(user);
						listModel.removeElement(username);
					}else if("USERLIST".equals(command)){	//连接成功之后，服务器发来当前在线的用户列表
						int size=Integer.parseInt(stringTokenizer.nextToken());
						String username="";
						String userIP="";
						for(int i=0;i<size;i++){
							username=stringTokenizer.nextToken();
							userIP=stringTokenizer.nextToken();
							UserBean userBean=new UserBean(username, userIP);
							onlineUsers.put(username, userBean);
							listModel.addElement(username);
						}
					}else{
						textPane_get_message.append(getTime()+message+"\r\n");
					}
				} catch (Exception e) {
					JOptionPane.showConfirmDialog(null,e.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
}
