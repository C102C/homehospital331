package com.krl.data;

public class DeviceInfo {
	private String deviceName;
	private String deviveMac;

	public DeviceInfo(String deviceName, String deviceMac) {
		this.deviceName = deviceName;
		this.deviveMac = deviceMac;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviveMac() {
		return deviveMac;
	}

	public void setDeviveMac(String deviveMac) {
		this.deviveMac = deviveMac;
	}
}
