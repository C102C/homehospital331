package com.health;

import java.util.ArrayList;
import java.util.List;

public class ContactHelp extends BaseActivity {

	public static boolean saveContact(String name, String phone) {
		String names = cache.getContactNames();
		if (names == null)// 第一个用户
			names = name;
		else
			names = names + "\t" + name;
		if (cache.saveContactNames(names))// 保存成功了姓名再存电话
			return cache.saveContact(name, phone);
		return false;
	}

	/***
	 * 获取联系人列表
	 * 
	 * @return
	 */
	public static List<String[]> getContacts() {
		List<String[]> list = new ArrayList<String[]>();
		String names = cache.getContactNames();
		if (names == null)
			return null;
		String[] nameArray = names.split("\t");
		for (String name : nameArray) {
			list.add(new String[] { "电话:" + cache.getContact(name) ,"姓名:" + name, });
		}
		return list;
	}

	}
