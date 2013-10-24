package com.health.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.health.pc300.PC300;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 负责提供蓝牙连接服务
 * 
 * @author jiqunpeng
 * 
 *         创建时间：2013-10-16 上午9:07:52
 */
public class BluetoothService {

	private final BluetoothAdapter btAdapter;// 蓝牙适配器
	private final Handler handler;// 传递信息给界面的handler
	private int state;// 蓝牙适配器的状态
	private ConnectThread connectThread = null;// 连接线程
	private ConnectedThread connectedThread = null;// 蓝牙通道管理线程

	private static final String UUIDS = "00001101-0000-1000-8000-00805F9B34FB";
	private static final UUID MY_UUID = UUID.fromString(UUIDS);// 建立蓝牙连接的UUID

	// 控制信息和状态
	public static final int STATE_NONE = 0; // 空闲状态
	public static final int STATE_CONNECTING = 2; // 正在连接
	public static final int STATE_CONNECTED = 3; // 已经建立蓝牙连

	public static final int MESSAGE_STATE_CHANGE = 1;// 适配器状态改变信号
	public static final int MESSAGE_READ = 2;// 读入数据信号
	public static final int MESSAGE_WRITE = 3;// 写入数据信号
	public static final int MESSAGE_TOAST = 4;// 显示给用户的提示的信号

	public static final String TOAST = "toast";// handler传递过去的关键字

	// 调试信息
	private static final String TAG = "BluetoothService";
	private static final boolean DEBUG = true;

	public BluetoothService(Handler handler) {
		this.btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (btAdapter.getState() == BluetoothAdapter.STATE_OFF)
			btAdapter.enable();// 打开蓝牙
		this.handler = handler;
		this.state = STATE_NONE;
	}

	/**
	 * 根据设备名字找蓝牙
	 * 
	 * @param name
	 * @return
	 */
	public BluetoothDevice getBluetoothDevice(String name) {
		Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
		for (BluetoothDevice d : pairedDevices) {
			String n = d.getName();
			if (name.equals(n)) {
				return d;
			}
		}
		return null;
	}
	/**
	 * 根据地址名，获取设备
	 * @param address
	 * @return
	 */
	public BluetoothDevice getRemoteDeviceByAddress(String address) {
		return btAdapter.getRemoteDevice(address);
	}

	/**
	 * 关闭所有蓝牙连接和处理线程
	 */
	public synchronized void stop() {
		if (DEBUG)
			Log.d(TAG, "stop");
		if (connectedThread != null) {
			connectedThread.shutdown();
			connectedThread.cancel();
			connectedThread = null;
		}
		if (connectThread != null) {
			connectThread.cancel();
			connectThread = null;
		}
		setState(STATE_NONE);
	}

	/**
	 * 以同步方式获取状态
	 * 
	 * @return
	 */
	public synchronized int getState() {
		return this.state;
	}

	public void connect(BluetoothDevice device) {
		if (DEBUG)
			Log.i(TAG, "connect to: " + device);
		if (state == STATE_CONNECTING) {// 取消正在建立的连接
			if (connectThread != null) {
				connectThread.cancel();
				connectThread = null;
			}
		}
		if (state == STATE_CONNECTED) {// 断开已经建立好连接
			if (connectedThread != null) {
				connectedThread.cancel();
				connectedThread = null;
			}
		}
		try {
			connectThread = new ConnectThread(device);
			connectThread.start();// 开始建立连接
			setState(STATE_CONNECTING);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 开启蓝牙已连接线程，管理蓝牙通道的读写
	 * 
	 * @param socket
	 * @param device
	 */
	private void connected(BluetoothSocket socket, BluetoothDevice device) {
		if (DEBUG)
			Log.i(TAG, "connected to" + device);
		if (state == STATE_CONNECTING) {// 取消正在建立的连接
			if (connectThread != null) {
				connectThread.cancel();
				connectThread = null;
			}
		}
		if (state == STATE_CONNECTED) {// 断开已经建立好连接
			if (connectedThread != null) {
				connectedThread.cancel();
				connectedThread = null;
			}
		}
		connectedThread = new ConnectedThread(socket);
		connectedThread.start();// 开启管理蓝牙通道的线程
		setState(STATE_CONNECTED);// 更新状态
	}

	/**
	 * 设置状态信息
	 * 
	 * @param state
	 */
	private void setState(int state) {
		if (DEBUG)
			Log.d(TAG, "setState() " + this.state + " -> " + state);
		this.state = state;
		// 将状态改变信息传递给UI界面
		handler.obtainMessage(MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
	}

	/**
	 * 处理连接建立失败，通知界面
	 */
	private void connectionFailed() {
		Message msg = handler.obtainMessage(MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString(TOAST, "设备连接失败");
		msg.setData(bundle);
		handler.sendMessage(msg);
		setState(STATE_NONE);// 设置状态为空闲
	}

	/**
	 * 处理连接端口，通知Activity
	 */
	public void connectionLost() {
		Message msg = handler.obtainMessage(MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString(TOAST, "设备连接断开");
		msg.setData(bundle);
		handler.sendMessage(msg);
		setState(STATE_NONE);
	}

	/**
	 * 往蓝牙通道中写入数据
	 * 
	 * @param buffer
	 */
	public void write(byte[] out) {
		ConnectedThread ct = null;
		synchronized (this) {// 以同步方式取得连接管理线程的引用
			if (state != STATE_CONNECTED)
				return;
			ct = connectedThread;
		}
		ct.write(out);
	}

	/**
	 * 建立连接的线程
	 * 
	 * @author jiqunpeng
	 * 
	 *         创建时间：2013-10-16 上午9:31:36
	 */
	private class ConnectThread extends Thread {
		private BluetoothDevice device;
		private BluetoothSocket socket;

		@SuppressLint("NewApi")
		public ConnectThread(BluetoothDevice device) throws SecurityException,
				NoSuchMethodException, IllegalArgumentException,
				IllegalAccessException, InvocationTargetException {
			this.device = device;
			if (btAdapter.getState() == BluetoothAdapter.STATE_OFF)
				btAdapter.enable();// 打开蓝牙
			BluetoothSocket tempSocket = null;
			int sdkVersion = Build.VERSION.SDK_INT;
			Method method = null;
			try {
				if (sdkVersion >= 10) {// 10以上的使用不安全连接
					tempSocket = device
							.createInsecureRfcommSocketToServiceRecord(MY_UUID);
					method = device.getClass().getDeclaredMethod(
							"createInsecureRfcommSocket",
							new Class[] { int.class });
				} else {
					tempSocket = device
							.createRfcommSocketToServiceRecord(MY_UUID);
					method = device.getClass().getMethod("createRfcommSocket",
							new Class[] { int.class });
				}
				tempSocket = (BluetoothSocket) method.invoke(device, 1);
			} catch (IOException e) {
				Log.e(TAG, "create() failed", e);
			}
			socket = tempSocket;
		}

		@Override
		public void run() {
			Log.i(TAG, "BEGIN connectThread");
			setName("ConnectThread");
			btAdapter.cancelDiscovery();// 建立连接前取消查找
			try {
				// 绑定没有配对过的蓝牙
				if (device.getBondState() != BluetoothDevice.BOND_BONDED)
					BluetoothDevice.class.getMethod("createBond", new Class[0])
							.invoke(device, new Object[0]);
				try {
					socket.connect();// 建立连接
				} catch (IOException connectException) {// 连接异常
					Log.e(TAG, "connect failure", connectException);
					connectionFailed();
					try {
						socket.close();
					} catch (IOException closeException) {// 关闭异常
						Log.e(TAG, "Can't close socket", closeException);
					}
					return;// 没有连接成功，返回
				}
			} catch (Exception bondException) {// 绑定异常
				// IllegalArgumentException|SecurityException
				// | IllegalAccessException |
				// InvocationTargetException
				// | NoSuchMethodException
				Log.e(TAG, "createBond error", bondException);
				return;// 没有连接成功，返回
			}
			// 连接建立成功，连接线程不再需要了
			synchronized (BluetoothService.this) {
				connectThread = null;
			}
			connected(socket, device);
		}

		public void cancel() {
			try {
				socket.close();
			} catch (IOException e) {
				Log.e(TAG, "close socket error", e);
			}
		}
	}

	/**
	 * 管理连接好的通道的线程，处理i/o操作
	 * 
	 * @author jiqunpeng
	 * 
	 *         创建时间：2013-10-16 下午3:35:54
	 */
	private class ConnectedThread extends Thread {
		private BluetoothSocket socket;
		private InputStream inStream = null;
		private OutputStream outStream = null;

		private boolean stop = false;

		public ConnectedThread(BluetoothSocket socket) {
			Log.i(TAG, "create ConnectedThread");
			this.socket = socket;
			try {
				inStream = socket.getInputStream();
				outStream = socket.getOutputStream();
			} catch (IOException e) {
				Log.e(TAG, "sockets stream not created", e);
			}
		}

		@Override
		public void run() {
			while (stop == false) {
				try {
					if (inStream.available() > 0) {
						byte[] buffer = new byte[256];
						int bytes;
						bytes = inStream.read(buffer);
						byte[] contend = new byte[bytes];// 只传递有效数据内容
						for (int i = 0; i < bytes; i++) {
							contend[i] = buffer[i];
						}
						handler.obtainMessage(MESSAGE_READ, bytes, -1, contend)
								.sendToTarget();// 传递给界面更新数据
					}
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (IOException e) {// 读数据异常
					Log.e(TAG, "Can't read from socket", e);
					connectionLost();// 连接断开
				} catch (InterruptedException e) {
					Log.e(TAG, "sleep interrupted", e);
				}
			}
		}

		/**
		 * 往流中写入数据
		 * 
		 * @param buffer
		 */
		public void write(byte[] buffer) {
			try {
				outStream.write(buffer);
				outStream.flush();
				handler.obtainMessage(MESSAGE_WRITE, -1, -1, buffer)
						.sendToTarget();// 传递状态
			} catch (IOException e) {
				connectionLost();
				Log.e(TAG, "write error", e);
			}
		}

		/**
		 * 关闭已经建立连接
		 */
		public void cancel() {
			try {
				socket.close();
			} catch (IOException e) {
				Log.e(TAG, "close socket error", e);
			}
		}

		/**
		 * 关闭流
		 */
		public void shutdown() {
			stop = true;
			try {
				if (inStream != null)
					inStream.close();
				if (outStream != null)
					outStream.close();
			} catch (IOException e) {
				Log.e(TAG, "close() of stream failed.", e);
			}

		}
	}

}
