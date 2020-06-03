package ChatRoom;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextPane;

public class ChatRoom extends JFrame {

	private JPanel contentPane;
	private JTextField textField_IP;
	private JTextField textField_PORT;
	private JTextField textField_room_name;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatRoom frame = new ChatRoom();
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
	public ChatRoom() {
		setTitle("聊天室 服务器端");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 695, 574);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
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
		
		JButton btnNewButton = new JButton("创建聊天室");
		btnNewButton.setBounds(23, 190, 110, 35);
		contentPane.add(btnNewButton);
		
		JLabel label = new JLabel("聊天室动态");
		label.setFont(new Font("黑体", Font.PLAIN, 17));
		label.setBounds(362, 18, 103, 33);
		contentPane.add(label);
		
		JTextPane textPane_message = new JTextPane();
		textPane_message.setBounds(361, 55, 290, 436);
		contentPane.add(textPane_message);
		
		JLabel label_1 = new JLabel("当前在线用户");
		label_1.setFont(new Font("黑体", Font.PLAIN, 17));
		label_1.setBounds(23, 240, 121, 33);
		contentPane.add(label_1);
		
		JTextPane textPane_online_users = new JTextPane();
		textPane_online_users.setBounds(23, 280, 290, 212);
		contentPane.add(textPane_online_users);
		
		JLabel label_2 = new JLabel("聊天室名：");
		label_2.setVerticalAlignment(SwingConstants.BOTTOM);
		label_2.setFont(new Font("黑体", Font.PLAIN, 17));
		label_2.setBounds(23, 57, 85, 20);
		contentPane.add(label_2);
		
		textField_room_name = new JTextField();
		textField_room_name.setColumns(10);
		textField_room_name.setBounds(106, 55, 139, 24);
		contentPane.add(textField_room_name);
	}
}
