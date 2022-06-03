package Client;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;


public class userinterface extends JFrame{

	private JPanel container; 
	private JLabel lblMasv, lblTen, lblPhong;
	private JTextField tfMasv, tfTen, tfPhong, tfTim;
	private JButton btnThem, btnSua, btnXoa, btnRefresh, btnTim;
	
	private JTable table; //bang
	private JScrollPane scroll; //thanh keo
	private DefaultTableModel model; // rows
	
	private ArrayList<sinhvien> dssinhvien = new ArrayList<>();
	
	public userinterface() {
		setTitle("Quan li ktx"); // dat ten cho frame
		setSize(500, 700); // set chieu dai , chieu rong cho frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // tat frame khi nhan vao dau X
		
		
		container = new JPanel();
		container.setLayout(null);
		
		lblMasv = new JLabel("Ma sv"); // tao lbl co noi dung la ma sv
		lblMasv.setBounds(20, 20, 150, 30); // cach canh trai20, canh tren 20, chieu dai 150, chieu cao 30.
		container.add(lblMasv);
		tfMasv = new JTextField();
		tfMasv.setBounds(200, 20, 150, 30);
		container.add(tfMasv);
		
		lblTen = new JLabel("ten"); // tao lbl co noi dung la ma sv
		lblTen.setBounds(20, 50, 150, 30); // cach canh trai20, canh tren 20, chieu dai 150, chieu cao 30.
		container.add(lblTen);
		tfTen = new JTextField();
		tfTen.setBounds(200, 50, 150, 30);
		container.add(tfTen);
		
		lblPhong = new JLabel("phong"); // tao lbl co noi dung la ma sv
		lblPhong.setBounds(20, 100, 150, 30); // cach canh trai20, canh tren 20, chieu dai 150, chieu cao 30.
		container.add(lblPhong);
		tfPhong = new JTextField();
		tfPhong.setBounds(200, 100, 150, 30);
		container.add(tfPhong);
		
		tfTim = new JTextField();
		tfTim.setBounds(20, 200, 150, 30);
		container.add(tfTim);
		btnTim = new JButton("Tim");
		btnTim.setBounds(200, 200, 100, 30);
		btnTim.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				tim(tfTim.getText().toString());
			}
		});
		container.add(btnTim);
		
		btnThem = new JButton("Them");
		btnThem.setBounds(20, 150, 100, 30);
		btnThem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				themsv(tfMasv.getText().toString(),
						tfTen.getText().toString(),
						tfPhong.getText().toString());
				hienthi(); // sau khi add thi cai bang se duoc refresh
			}
		});
		container.add(btnThem);
		
		btnXoa = new JButton("Xoa");
		btnXoa.setBounds(200, 150, 100, 30);
		btnXoa.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// cai showconfirmdialog se hien thi 1 frame voi 2 lua chonj yes va no
				// yes tuong ung voi 0, no tuong ung voi 1
				// luu lua chon vao check
				int check = JOptionPane.showConfirmDialog(container, "Xoa sinh vien nay", "Thong bao", JOptionPane.YES_NO_OPTION,2);
				if(check == JOptionPane.YES_OPTION) { //yes_option tuong ung voi 0 
					//neu chon yes thi thuc hien ham xoa
					xoa();
				}
			}
		});
		container.add(btnXoa);
		
		btnSua = new JButton("Sua");
		btnSua.setBounds(350, 150, 100, 30);
		btnSua.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//tao doi tuong sinh vien chua cac thong tin can sua
				// thong tin can sua duoc lay tu text field
				sinhvien sv = new sinhvien(tfMasv.getText().toString(),
						tfTen.getText().toString(), 
						tfPhong.getText().toString());
				
				int check = JOptionPane.showConfirmDialog(container, "Xac nhan sua", "Thong bao", JOptionPane.YES_NO_OPTION, 2);
				if(check == JOptionPane.YES_OPTION) {
					sua(sv);// dung ham sua 
				}
			}
		});
		container.add(btnSua);
		
		btnRefresh = new JButton("Refresh");
		btnRefresh.setBounds(350, 250, 100, 30);
		btnRefresh.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// khi an vao thi load lai cai bang
				hienthi();
				//clear het cac text field
				tfMasv.setText("");
				tfTen.setText("");
				tfPhong.setText("");
				tfTim.setText("");
			}
		});
		container.add(btnRefresh);
		
		scroll = new JScrollPane();
		table = new JTable();
		scroll.setBounds(0, 400, 500, 300);
		table.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null, null}
				},
				new String[] {
						"ma sv", "ten", "phong"
				}
		));
		table.addMouseListener(new MouseAdapter() {
			//them action cho table
			public void mousePressed(MouseEvent e) {
				// lay row duoc click va luu vao selectedRow
				int selectedRow = table.getSelectedRow();
				//do trong collection dssinhvien cai index tuong ung va getmasv
				tfMasv.setText(dssinhvien.get(selectedRow).getMasv());
				tfTen.setText(dssinhvien.get(selectedRow).getTensv());
				tfPhong.setText(dssinhvien.get(selectedRow).getPhong());
			}
		});
		scroll.add(table);
		scroll.setViewportView(table);
		container.add(scroll);
		model = (DefaultTableModel) table.getModel();
		hienthi();
		add(container);
	}
	
	protected void tim(String masv) {
		ArrayList<sinhvien> dstimduoc = new ArrayList<>(); // tao 1 cai collection de luu danh sach sv tim duoc
		dstimduoc.clear(); // dong nay thua, khong thich thi xoa
		for(sinhvien sv : dssinhvien) {
			if(sv.getMasv().contains(masv)) {
				dstimduoc.add(sv); // sv nao co id chua cac ki tu m da nhap thi dc luu vao
			}
		}
		model.setRowCount(0);
		for(sinhvien sv : dstimduoc) { // hien thi ds tim duoc ra table
			model.addRow(new Object[] {
					sv.getMasv(), sv.getTensv(), sv.getPhong()
			});
		}
	}

	protected void sua(sinhvien sv) {
		Socket socket = null; // ket noi voi server
		DataOutputStream dataOut = null; // gui text
		ObjectOutputStream outStream = null; // gui doi tuong
		try {
			socket = new Socket("127.0.0.1", 3199);
			dataOut = new DataOutputStream(socket.getOutputStream());//tao duong truyen de gui yeu cau
			dataOut.writeUTF("sua"); // gui yeu cau them
			
			//tao duong truyen de gui object sinh vien
			outStream = new ObjectOutputStream(socket.getOutputStream());
			outStream.writeObject(sv);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void xoa() {
		Socket socket = null; // ket noi voi server
		DataOutputStream dataOut = null; // gui text
		try {
			socket = new Socket("127.0.0.1", 3199);
			dataOut = new DataOutputStream(socket.getOutputStream());//tao duong truyen de gui yeu cau
			dataOut.writeUTF("xoa"); // gui yeu cau them
			//gui tiep id cua sv can xoa den server
			dataOut.writeUTF(tfMasv.getText().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void themsv(String masv, String ten, String phong) { // truyen vao ham nay 3 thong tin de dua len server
		Socket socket = null; // ket noi voi server
		DataOutputStream dataOut = null; // gui text
		ObjectOutputStream outStream = null; // gui doi tuong
		try {
			socket = new Socket("127.0.0.1", 3199);
			dataOut = new DataOutputStream(socket.getOutputStream());//tao duong truyen de gui yeu cau
			dataOut.writeUTF("them"); // gui yeu cau them
			
			// tao doi tuong sinh vien, chua cac info duoc dua vao method nay
			sinhvien sv =  new sinhvien(masv, ten, phong);
			//tao duong truyen de gui doi tuong sinh vien tren
			outStream = new ObjectOutputStream(socket.getOutputStream());
			//gui doi tuong
			outStream.writeObject(sv);
			//hien bang thong bao da them thanh cong
			JOptionPane.showMessageDialog(container, "Da them", "Thong bao", 2);
			// hien thong bao nay trong container, thong bao co noi dung "Da them"
			//Frame thong bao co title "Thong bao", frame thong bao co icon 2-icon cham than
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void hienthi() {
		dssinhvien.clear();
		Socket socket = null; // ket noi voi server
		DataOutputStream dataOut = null; // gui text
		ObjectInputStream inStream = null; // nhan doi tuong
		try {
			socket = new Socket("127.0.0.1", 3199);
			dataOut = new DataOutputStream(socket.getOutputStream()); // tao duong truyen text
			dataOut.writeUTF("danhsach"); // gui yeu cau "danhsach" den server
			//
			inStream = new ObjectInputStream(socket.getInputStream());// tao duong truyen de nhan object
			dssinhvien = (ArrayList<sinhvien>) inStream.readObject(); // nhan cai list duoc gui tu server va luu vao collection da tao
			//eps kieeur 
			model.setRowCount(0);
			for(sinhvien sv : dssinhvien) {
				 // bat dau tu row dau tien
				model.addRow(new Object[] {
						sv.getMasv(), sv.getTensv(), sv.getPhong()
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		userinterface us =  new userinterface();
		us.setVisible(true);
	}
}
