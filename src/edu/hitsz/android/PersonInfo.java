package edu.hitsz.android;
//个人信息
public class PersonInfo {
	public String name;	//姓名
	public String cardNum;//卡号
	public String gender;//性别
	public String age;//年龄
	public byte[] photo;
	public PersonInfo(String name, String cardNum, String gender, String age, byte[]photo){
		this.name = name;
		this.cardNum = cardNum;
		this.gender = gender;
		this.age = age;
		this.photo = photo;
	}
}
