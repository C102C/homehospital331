package com.krl.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class NetworkConnectThread extends Thread {
	private static final String TAG = "NetworkConnectThread";
	private static final String mDefaultIP = "192.168.1.24";
	private static final int mDefaultPort = 55885;
	private String mRemoteIP = mDefaultIP;
	private int mRemotePort = mDefaultPort;
	private String account = "";
	private String passWord = "";
	public static final int STATE_CLOSE = 0;
	public static final int STATE_CONNECTED = 1;
	public static final int STATE_CONNECTING = 2;
	private int mBufferOffset = 0;
	private int mBufferSize = 1024;
	private Socket mSocket = null;
	private InputStream mInputStream = null;
	private OutputStream mOutputStream = null;
	private byte[] mReadBuffer = new byte[mBufferSize];
	private NetworkProtocolHandler mProtocolHandler = null;
	private Handler mHandler = null;
	private ArrayList<byte[]> mSendPackets = new ArrayList<byte[]>();

	// ecg.koanruler.com 8585 demo demo

	public enum ConnectionState {
		DisConnected, Connected, UnknownHost, SocketTimeout, IOException;
	}

	private ConnectionState mState = ConnectionState.DisConnected;

	public NetworkConnectThread(Handler handler, byte[] defaultPacket) {
		mHandler = handler;
		if (defaultPacket != null)
			mSendPackets.add(defaultPacket);
		mSocket = new Socket();
	}

	public ConnectionState getConnectionState() {
		return mState;
	}

	public boolean isConnected() {
		return mState == ConnectionState.Connected ? true : false;
	}

	public void disConnect() {
		mState = ConnectionState.DisConnected;
	}

	public OutputStream getOutputStream() {
		return mOutputStream;
	}

	public void setServerInfo(String ip, int port) {
		mRemoteIP = ip;
		mRemotePort = port;
	}

	public void setLoginAccount(String account, String passWord) {
		this.account = account;
		this.passWord = passWord;
	}

	public void addPacket(byte[] defaultPacket) {
		mSendPackets.add(defaultPacket);
	}

	public boolean connect() {
		if (isConnected()) {
			return true;
		}
		try {
			SocketAddress remoteAddr = new InetSocketAddress(mRemoteIP,
					mRemotePort);
			mSocket.connect(remoteAddr, 30000);
			mInputStream = mSocket.getInputStream();
			mOutputStream = mSocket.getOutputStream();
			mState = ConnectionState.Connected;
		} catch (UnknownHostException e) {
			mState = ConnectionState.UnknownHost;
			Log.e(TAG, e.toString());
		} catch (SocketTimeoutException e) {
			mState = ConnectionState.SocketTimeout;
			Log.e(TAG, e.toString());
		} catch (IOException e) {
			mState = ConnectionState.IOException;
			Log.e(TAG, e.toString());
		} finally {
			connectionStateChanged(mState);
		}
		if (isConnected()) {
			// if (null == mProtocolHandler)
			mProtocolHandler = new NetworkProtocolHandler(mOutputStream,
					mHandler);
			// else
			// mProtocolHandler.reset();
			sendPackets();
		} else {
			this.clean();
		}
		return isConnected();
	}

	public boolean login() {
		try {
			byte[] buffer = NetworkPacketFactory.make_login(
					account.getBytes("GBK"), passWord.getBytes("GBK"));
			this.mOutputStream.write(buffer, 0, buffer.length);
		} catch (IOException e) {
			mState = ConnectionState.IOException;
			this.clean();
			connectionStateChanged(mState);
			Log.e(TAG, e.toString());
			return false;
		}
		return true;
	}

	@Override
	public void run() {
		if (!connect())
			return;
		if (!login())
			return;

		int bytes = 0;
		while (isConnected()) {
			if (mInputStream != null) {
				try {
					Log.e(TAG, "bytes:" + bytes);
					bytes = mInputStream.read(mReadBuffer, mBufferOffset,
							mBufferSize - mBufferOffset);
					if (bytes == -1) {
						break;
					}
					Log.e(TAG, "bytes:" + bytes);
					mBufferOffset = mProtocolHandler.Parse(mReadBuffer,
							mBufferOffset, bytes);
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					mState = ConnectionState.IOException;
					connectionStateChanged(mState);
					e.printStackTrace();
					break;
				}
			}
		}
		Log.e(TAG, "Thread stopped");
		this.clean();
		mSendPackets.clear();
		mProtocolHandler = null;

	}

	public void cancel() {
		mState = ConnectionState.DisConnected;
		this.clean();
	}

	private void clean() {
		try {
			if (null != mInputStream)
				mInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (null != mOutputStream)
				mOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (mSocket != null)
				mSocket.shutdownInput();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (mSocket != null)
				mSocket.shutdownOutput();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (mSocket != null)
				mSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mInputStream = null;
		mOutputStream = null;
		mSocket = null;
	}

	public boolean write(byte[] buffer, int offset, int count) {
		try {
			this.mOutputStream.write(buffer, offset, count);
		} catch (IOException e) {
			mState = ConnectionState.IOException;
			this.clean();
			connectionStateChanged(mState);
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void sendPackets() {
		if (this.mSendPackets.size() > 0) {
			for (int i = 0; i < mSendPackets.size(); i++) {
				this.write(this.mSendPackets.get(i));
			}
			mSendPackets.clear();
		}
	}

	public boolean write(byte[] buffer) {
		try {
			this.mOutputStream.write(buffer, 0, buffer.length);
		} catch (IOException e) {
			mState = ConnectionState.IOException;
			this.clean();
			connectionStateChanged(mState);
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void connectionStateChanged(ConnectionState state) {
		if (null != mHandler) {
			Message msg = this.mHandler.obtainMessage(MESSAGE.MESSAGE_CONNECT);
			Bundle data = new Bundle();
			data.putInt(MESSAGE.MESSAGE_KEY_CONNECT, state.ordinal());
			msg.setData(data);
			mHandler.sendMessage(msg);
		}
	}

	public NetworkProtocolHandler GetProtocolHandler() {
		return mProtocolHandler;
	}
}
