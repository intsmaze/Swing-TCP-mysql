package cn.com.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DeleteGoods extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private String url;
	private int port;
	private static final long serialVersionUID = 1L;
	Socket s = null;
	BufferedWriter bufOut = null;
	BufferedReader bufIn = null;
	public static double price = 0;

	public static void main(String[] args) {
		DeleteGoods cm = new DeleteGoods("127.0.01", 1000);
		JFrame jf = new JFrame();
		jf.setBounds(10, 10, 700, 650);
		jf.add(cm);
		jf.setVisible(true);
	}

	private JLabel jl = new JLabel("编号：");
	private JTextField jf = new JTextField();
	private JButton jb = new JButton("删除");
	private JLabel jl2 = new JLabel();

	public DeleteGoods(String url, int port) {
		this.url = url;
		this.port = port;
		this.initialFrame();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jb) {
			try {
				String number = jf.getText().trim();
				s = new Socket(url, port);
				bufOut = new BufferedWriter(new OutputStreamWriter(
						s.getOutputStream()));
				bufIn = new BufferedReader(new InputStreamReader(
						s.getInputStream()));

				bufOut.write(11 + "\n");// 服务器端根据8来执行删除售货员功能
				bufOut.flush();

				bufOut.write(number + "\n"); // 向服务器传递数据
				bufOut.flush();

				if (bufIn.readLine().equalsIgnoreCase("ok")) {
					JOptionPane.showMessageDialog(this, "删除成功！", " ",
							JOptionPane.ERROR_MESSAGE);
					s.close();
				} else {
					JOptionPane.showMessageDialog(this, "删除失败！", " ",
							JOptionPane.ERROR_MESSAGE);
					s.close();
				}

			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public void initialFrame() {
		this.setLayout(null);

		jl.setBounds(60, 10, 130, 30);
		this.add(jl);
		jf.setBounds(160, 10, 130, 30);
		this.add(jf);
		jl2.setBounds(60, 150, 100, 30);
		this.add(jl2);
		jb.setBounds(360, 10, 130, 30);
		this.add(jb);
		jb.addActionListener(this);
	}

}
