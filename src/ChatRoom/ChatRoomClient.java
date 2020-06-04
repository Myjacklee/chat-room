package ChatRoom;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextPane;

public class ChatRoomClient extends JFrame {

	private JTextArea textPane = new JTextArea();
	private JScrollPane JSP=new JScrollPane(textPane);

	
	
	
	private JPanel contentPane;
	private JTextField textField_IP;
	private JTextField textField_PORT;
	private JTextField textField_nickname;
	private JButton button_join_logout;
	private JLabel label_1;
	private JScrollPane jsp_online_users;
	private JTextPane textPane_online_users;
	private JLabel label_2;
	private JTextPane textPane_get_message;
	private JScrollPane jsp_get_message;
	private JTextArea textPane_send_message;
	private JScrollPane jsp_send_message;
	private JLabel label_3;
	private JButton button_send_message;

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
		
		textField_IP = new JTextField();
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
		
		label_1 = new JLabel("当前在线人员");
		label_1.setFont(new Font("黑体", Font.PLAIN, 17));
		label_1.setBounds(31, 196, 128, 31);
		contentPane.add(label_1);
		
		textPane_online_users = new JTextPane();
		textPane_online_users.setEditable(false);
		jsp_online_users=new JScrollPane(textPane_online_users);
		jsp_online_users.setBounds(31,228,190,261);
		contentPane.add(jsp_online_users);
		
		label_2 = new JLabel("聊天室实时动态");
		label_2.setFont(new Font("黑体", Font.PLAIN, 17));
		label_2.setBounds(277, 14, 128, 31);
		contentPane.add(label_2);
		
		textPane_get_message = new JTextPane();
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
		
		
		

	}
}
