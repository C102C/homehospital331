package com.krl.data;

import android.os.Parcel;
import android.os.Parcelable;

public class ServerInfo implements Parcelable {
	private String ip;
	private int port;

	public ServerInfo(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public ServerInfo(Parcel pl) {
		ip = pl.readString();
		port = pl.readInt();
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void readFromParcel(Parcel in) {
		ip = in.readString();
		port = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(ip);
		dest.writeInt(port);
	}

	public static final Parcelable.Creator<ServerInfo> CREATOR = new Parcelable.Creator<ServerInfo>() {

		@Override
		public ServerInfo createFromParcel(Parcel source) {
			return new ServerInfo(source);
		}

		@Override
		public ServerInfo[] newArray(int size) {
			return new ServerInfo[size];
		}

	};
}
