package Client;

import java.io.Serializable;

public class sinhvien implements Serializable{ // implements cai seri.. thi moi truyen object qua socket duoc
	private String masv;
	private String tensv;
	private String phong;
	
	public sinhvien(String masv, String tensv, String phong) {
		this.masv = masv;
		this.tensv = tensv;
		this.phong = phong;
	}
	public String getMasv() {
		return masv;
	}
	public void setMasv(String masv) {
		this.masv = masv;
	}
	public String getTensv() {
		return tensv;
	}
	public void setTensv(String tensv) {
		this.tensv = tensv;
	}
	public String getPhong() {
		return phong;
	}
	public void setPhong(String phong) {
		this.phong = phong;
	}
	
	
	
}
