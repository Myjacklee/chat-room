package ChatRoom;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextPane;

public class ChatRoomClient extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JButton btnNewButton;
	private JLabel label_1;
	private JTextPane textPane;
	private JLabel label_2;
	private JTextPane textPane_1;
	private JTextPane textPane_2;
	private JLabel label_3;
	private JButton button;

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
		
		textField = new JTextField();
		textField.setBounds(93, 44, 128, 24);
		contentPane.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(93, 73, 128, 24);
		contentPane.add(textField_1);
		
		JLabel label = new JLabel("昵称：");
		label.setFont(new Font("黑体", Font.PLAIN, 17));
		label.setBounds(31, 99, 53, 31);
		contentPane.add(label);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(93, 103, 128, 24);
		contentPane.add(textField_2);
		
		btnNewButton = new JButton("加入聊天室");
		btnNewButton.setBounds(31, 143, 107, 27);
		contentPane.add(btnNewButton);
		
		label_1 = new JLabel("当前在线人员");
		label_1.setFont(new Font("黑体", Font.PLAIN, 17));
		label_1.setBounds(31, 196, 128, 31);
		contentPane.add(label_1);
		
		textPane = new JTextPane();
		textPane.setBounds(31, 228, 190, 261);
		contentPane.add(textPane);
		
		label_2 = new JLabel("聊天室实时动态");
		label_2.setFont(new Font("黑体", Font.PLAIN, 17));
		label_2.setBounds(277, 14, 128, 31);
		contentPane.add(label_2);
		
		textPane_1 = new JTextPane();
		textPane_1.setBounds(277, 44, 366, 261);
		contentPane.add(textPane_1);
		
		textPane_2 = new JTextPane();
		textPane_2.setBounds(277, 342, 366, 108);
		contentPane.add(textPane_2);
		
		label_3 = new JLabel("文本编辑框");
		label_3.setFont(new Font("黑体", Font.PLAIN, 17));
		label_3.setBounds(277, 313, 128, 31);
		contentPane.add(label_3);
		
		button = new JButton("发送");
		button.setBounds(563, 463, 80, 27);
		contentPane.add(button);
	}

}
