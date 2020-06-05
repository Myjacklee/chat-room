package ChatRoom;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import learn.User;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.ListModel;

public class ChatRoomClient extends JFrame {

	private JTextArea textPane = new JTextArea();
	private JScrollPane JSP=new JScrollPane(textPane);
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
		lblPort.setBounds(31, 72, 53, 31);
		contentPane.add(lblPort);
		
		textField_IP = new JTextField("127.0.0.1");
		textField_IP.setBounds(93, 44, 128, 24);
		contentPane.add(textField_IP);
		textField_IP.setColumns(10);
		
		textField_PORT = new JTextField();
		textField_PORT.setColumns(10);
		textField_PORT.setBounds(93, 73, 128, 24);
		contentPane.add(textField_PORT);
		
		JLabel label = new JLabel("昵称：");
		label.setFont(new Font("黑体", Font.PLAIN, 17));
		label.setBounds(31, 99, 53, 31);
		contentPane.add(label);
		
		textField_nickname = new JTextField();
		textField_nickname.setColumns(10);
		textField_nickname.setBounds(93, 103, 128, 24);
		contentPane.add(textField_nickname);
		
		button_join_logout = new JButton("加入聊天室");
		button_join_logout.setBounds(31, 143, 107, 27);
		contentPane.add(button_join_logout);
		button_join_logout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
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
						// TODO: handle exception
						throw new Exception("端口号不符合要求！应该为整数！");
					}
					ip=textField_IP.getText().trim();
					nickname=textField_nickname.getText().trim();
					if("".equals(nickname)||"".equals(ip)){
						throw new Exception("姓名和服务器不能为空！");
					}
					boolean connectFlag=connectServer(port,ip,nickname);
					if(connectFlag){
						throw new Exception("与服务器连接失败！");
					}
					setTitle("当前用户："+nickname);
					button_send_message.setEnabled(true);
					button_join_logout.setEnabled(false);
					button_close.setEnabled(true);
				} catch (Exception e2) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null, e2.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		label_1 = new JLabel("当前在线人员");
		label_1.setFont(new Font("黑体", Font.PLAIN, 17));
		label_1.setBounds(31, 196, 128, 31);
		contentPane.add(label_1);
		
		textPane_online_users = new JTextArea();
		textPane_online_users.setEditable(false);
		jsp_online_users=new JScrollPane(textPane_online_users);
		jsp_online_users.setBounds(31,228,190,261);
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
		button_send_message.setBounds(563, 463, 80, 27);
		contentPane.add(button_send_message);
		button_send_message.setEnabled(false);
		
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
					button_close.setEnabled(false);
					button_join_logout.setEnabled(true);
					button_send_message.setEnabled(false);
				} catch (Exception e3) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(null,e3.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		button_close.setBounds(144, 143, 107, 27);
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
		// TODO Auto-generated method stub
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
			// TODO: handle exception
			e.printStackTrace();
			isConnected=true;
			return false;
		}
	}

	protected boolean connectServer(int port, String ip, String nickname) {
		// TODO Auto-generated method stub
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
			// TODO: handle exception
			textPane_get_message.append("与IP："+ip+" 端口号："+port+" 的服务器连接失败\r\n");
			isConnected=false;
			return false;
		}
	}
	//发送消息
	private void sendMessage(String string) {
		// TODO Auto-generated method stub
		writer.println(string);
		writer.flush();
	}

	class MesssageThread extends Thread{
		private BufferedReader reader;
		private JTextArea textArea;
		public MesssageThread(BufferedReader reader,JTextArea textArea) {
			// TODO Auto-generated constructor stub
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
					StringTokenizer stringTokenizer=new StringTokenizer(message,"/@");
					String command=stringTokenizer.nextToken();
					if("CLOSE".equals(command)){
						textPane_get_message.append("服务器已经关闭!\r\n");
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
					}else if("USERLIST".equals(command)){	//加载当前在线的用户列表
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
						textPane_get_message.append(message+"\r\n");
					}
				} catch (Exception e) {
					// TODO: handle exception
					JOptionPane.showConfirmDialog(null,e.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
}
