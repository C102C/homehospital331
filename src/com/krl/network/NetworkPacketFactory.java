package com.krl.network;

import com.krl.data.DataRecord;

public class NetworkPacketFactory
{
	public static byte [] make_login(byte [] account, byte [] password)
	{
		byte [] data = new byte[128];
		System.arraycopy(account, 0, data, 0, account.length);
		System.arraycopy(password, 0, data, 64, password.length);
		NetworkPacket packet = new NetworkPacket();
		return packet.Packet(NetworkPacketType.LOGIN, (byte) 0, (byte) 0, data);
	}

	public static byte [] make_sync_patient_info(byte [] lastSyncTime)
	{
		NetworkPacket packet = new NetworkPacket();
		return packet.Packet(NetworkPacketType.SYNC_PATIENT_INFO, (byte) 0, (byte) 0, lastSyncTime);
	}

	public static byte [] make_patient_info(byte res, byte param, DataRecord dr)
	{
		NetworkPacket packet = new NetworkPacket();
		return packet.Packet(NetworkPacketType.PATIENT_INFO, res, param, dr.toBytes());
	}

	public static byte [] make_sync_data_state(byte [] syncTime)
	{
		NetworkPacket packet = new NetworkPacket();
		return packet.Packet(NetworkPacketType.GETDATASTATE, (byte) 0, (byte) 0, syncTime);
	}

	public static byte [] make_upload_file_query(byte [] data)
	{
		NetworkPacket packet = new NetworkPacket();
		return packet.Packet(NetworkPacketType.UPLOAD_DATAFILE, (byte) 0, (byte) 1, data);
	}

	public static byte [] make_upload_file_start(byte [] data)
	{
		NetworkPacket packet = new NetworkPacket();
		return packet.Packet(NetworkPacketType.UPLOAD_DATAFILE, (byte) 0, (byte) 2, data);
	}

	public static byte [] make_upload_file_data(byte [] data, int size)
	{
		NetworkPacket packet = new NetworkPacket();
		return packet.Packet(NetworkPacketType.UPLOAD_DATAFILE, (byte) 0, (byte) 3, data, 0, size);
	}

	public static byte [] make_upload_file_data(byte [] data, int size, byte [] buffer, int [] len)
	{
		NetworkPacket packet = new NetworkPacket();
		return packet.Packet(NetworkPacketType.UPLOAD_DATAFILE, (byte) 0, (byte) 3, data, 0, size, buffer, len);
	}

	public static byte [] make_upload_file_end(byte [] data)
	{
		NetworkPacket packet = new NetworkPacket();
		return packet.Packet(NetworkPacketType.UPLOAD_DATAFILE, (byte) 0, (byte) 4, data);
	}

	public static byte [] make_sync_report_state(byte [] lastSyncTime)
	{
		NetworkPacket packet = new NetworkPacket();
		return packet.Packet(NetworkPacketType.SYNC_REPORT_STATE, (byte) 0, (byte) 0, lastSyncTime);
	}
}
