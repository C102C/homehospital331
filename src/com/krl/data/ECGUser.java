package com.krl.data;

public class ECGUser {
	private String account;
	private String passWord;
	private int centerId;
	private int checkStationId;

	public ECGUser(String account, String passWord) {
		this.account = account;
		this.passWord = passWord;
	}

	public int getCenterId() {
		return centerId;
	}

	public void setCenterId(int centerId) {
		this.centerId = centerId;
	}

	public int getCheckStationId() {
		return checkStationId;
	}

	public void setCheckStationId(int checkStationId) {
		this.checkStationId = checkStationId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
}
