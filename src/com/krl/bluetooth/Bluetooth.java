package com.krl.bluetooth;

import java.util.UUID;
public class Bluetooth
{
	// public static final UUID MY_UUID =
	// UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
	public static final UUID	MY_UUID					= UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	public static final String	CONFIGFILE				= "bluetooth.cfg";
	public static final String	EXTRA_DEVICE_ADDRESS	= "device_address";
	public static final String	TAG_DEVICE				= "Static ECG";
	public static final int		REQUEST_CONNECT_DEVICE	= 0x1;
	public static final int		REQUEST_ENABLE_BT		= 0x2;
	public static final int		STATE_CLOSEED			= 0;
	public static final int		STATE_CONNECTED			= 1;
	public static final byte []	ECG						= { (byte) 0xf5, 0x0a, 0x01, 0x00, 0x00 };
	public static final byte []	VCG						= { (byte) 0xf5, 0x0a, 0x02, 0x00, 0x00 };
	public static final byte []	STOP					= { (byte) 0xf5, 0x0a, 0x00, 0x00, 0x00 };
	public static final byte []	BATTERY					= { (byte) 0xf5, 0x0a, 0x03, 0x00, 0x00 };
	public static final int		MESSAGE_CLOSED			= 1;
	public static final int		MESSAGE_CONNECTED		= 2;
	public static final int		MESSAGE_DATA			= 3;
	public static final String	MESSAGE_KEY_DATA		= "data";
	public static final String	MESSAGE_KEY_SIZE		= "size";
}
