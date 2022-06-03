package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Client.sinhvien;



public class ThreadHandler extends Thread{
	
	DataOutputStream dataOut = null; //gui text di
	DataInputStream dataIn = null; //nhan text
	ObjectInputStream inStream = null; //gui doi tuong
	ObjectOutputStream outStream = null; //nhan doi tuong 
	Socket socket = null; //ket noi voi server
	
	private ArrayList<sinhvien> dssinhvien = new ArrayList<>(); // tao 1 collection de luu danh sach sv
	
	
	public ThreadHandler(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			dataIn = new DataInputStream(socket.getInputStream()); // tao duong truyen nhan text
			String request = dataIn.readUTF(); //doc text duoc gui tu client va luu vao bien request
			switch(request) {
			case "danhsach":{ 
				//case nay duoc thuc thi khi client gui yeu cau "danhsach"
				try {
					dssinhvien.clear();
					dssinhvien = danhsachsv(); //chay ham danhsachsv de lay ds sinh vien, roi luu danh sach sinh vien do vao cai arraylist da tao
					outStream = new ObjectOutputStream(socket.getOutputStream()); //tao duong truyen object
					outStream.writeObject(dssinhvien); // gui danh sach sinh vien di
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			case "them":{
				//case nay duoc thuc thi khi client gui yeu cau "them"
				try {
					//tao duong truyen de nhan object
					inStream = new ObjectInputStream(socket.getInputStream());
					sinhvien sv = null;
					// nhan object va luu vao sv
					sv = (sinhvien) inStream.readObject();
					them(sv); //goi method them
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			case "xoa":{
				//case nay duoc thuc thi khi client gui yeu cau "xoa"
				try {
					//tao duong truyen de nhan ma sinh vien
					dataIn = new DataInputStream(socket.getInputStream());
					//nhan ma sinh vien tu client va luu vao masv
					String masv = dataIn.readUTF();
					//thuc hien ham xoa
					xoa(masv);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			case "sua":{
				//case nay duoc thuc thi khi client gui yeu cau "sua"
				try {
					//tao duong truyen de nhan doi tuong sinh vien
					inStream = new ObjectInputStream(socket.getInputStream());
					sua((sinhvien) inStream.readObject()); // lam gon, get object tu instream va dua vao ham sua luon
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void sua(sinhvien sv) {
		Connection con = null; // tao connection voi database
		PreparedStatement stm = null; //de chay lenh sql, dung prep.. de toi uu
		
		try {
			String dbURL = "jdbc:mysql://localhost:3306/qlktx"; // link den database
			String user = "root";//user name cua database
			String password = "lvt31415926@"; // mat khau database
			con = DriverManager.getConnection(dbURL, user, password); //tao ket noi den database
			String sql = "UPDATE sinhvien SET id = ?, ten = ?, phong = ?"; //cau lenh sql
			stm = con.prepareStatement(sql); //t ko biet
			stm.setString(1, sv.getMasv());// lay ma sv va thay vao cham hoi thu nhat
			stm.setString(2, sv.getTensv());
			stm.setString(3, sv.getPhong());
			stm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(stm != null) {
				try {
					stm.close();
				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
				
			if(con != null) {
				try {
					con.close();
				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
		}
	}

	private void xoa(String masv) {
		Connection con = null; // tao connection voi database
		PreparedStatement stm = null; //de chay lenh sql, dung prep.. de toi uu
		
		try {
			String dbURL = "jdbc:mysql://localhost:3306/qlktx"; // link den database
			String user = "root";//user name cua database
			String password = "lvt31415926@"; // mat khau database
			con = DriverManager.getConnection(dbURL, user, password); //tao ket noi den database
			String sql = "DELETE FROM sinhvien WHERE id = ?"; //cau lenh sql
			stm = con.prepareStatement(sql); //t ko biet
			stm.setString(1, masv);// lay ma sv va thay vao cham hoi thu nhat
			stm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(stm != null) {
				try {
					stm.close();
				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
				
			if(con != null) {
				try {
					con.close();
				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
		}
	}

	private void them(sinhvien sv) {
		Connection con = null; // tao connection voi database
		PreparedStatement stm = null; //de chay lenh sql, dung prep.. de toi uu
		
		try {
			String dbURL = "jdbc:mysql://localhost:3306/qlktx"; // link den database
			String user = "root";//user name cua database
			String password = "lvt31415926@"; // mat khau database
			con = DriverManager.getConnection(dbURL, user, password); //tao ket noi den database
			String sql = "INSERT INTO sinhvien VALUES (?, ?, ?)"; //cau lenh sql
			stm = con.prepareStatement(sql); //t ko biet
			stm.setString(1, sv.getMasv());// lay ma sv va thay vao cham hoi thu nhat
			stm.setString(2, sv.getTensv()); // lay ten va thay vao ? thu 2
			stm.setString(3, sv.getPhong()); // lay phong va thay va0 ? thu 3
			stm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(stm != null) {
				try {
					stm.close();
				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
				
			if(con != null) {
				try {
					con.close();
				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
		}
	}

	public static ArrayList<sinhvien> danhsachsv() { // ham danhsachsv tra ve 1 collection
		ArrayList<sinhvien> ds = new ArrayList<>(); // de luu ds sinh vien lay tu database 
		Connection con = null; // tao connection voi database
		Statement stm = null; //de chay lenh sql
		
		try {
			String dbURL = "jdbc:mysql://localhost:3306/qlktx"; // link den database
			String user = "root";//user name cua database
			String password = "lvt31415926@"; // mat khau database
			con = DriverManager.getConnection(dbURL, user, password); //tao ket noi den database
			String sql = "SELECT * FROM sinhvien"; //cau lenh sql
			stm = con.createStatement(); //t ko biet 
			ResultSet result = stm.executeQuery(sql); //thuc thi cau lenh, database tra ve 1 cai list 
			while(result.next()) { 
				sinhvien sv = new sinhvien(result.getString("id"), result.getString("ten"), result.getString("phong"));
				//tao 1 doi tuong sv  de luu thong tin lay dc tu cai list result
				ds.add(sv); // day doi tuong sinh vien vua tao vao ds 
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(stm != null) {
				try {
					stm.close();
				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
				
			if(con != null) {
				try {
					con.close();
				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
		}
		return ds;
	}
}
