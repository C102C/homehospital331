package com.health.bean;

public class User {
	private String cardNo;
	private String userID;
	private String birthday;
	private String sex;
	private String imageUrl;
	private String email;
	private String nickName;
	private String customerGuid;
	private String name;
	private String userGuid;
	private String mobile;
	private String password;
	

	public User(String name, String cardNo, String sex, String birthday,
			String password) {
		this(name, cardNo, sex, birthday);
		this.setPassword(password);
	}

	public User(String name, String cardNo, String sex, String birthday) {
		this(name, cardNo);
		this.sex = sex;
		this.birthday = birthday;
	}

	public User(String name, String cardNo) {
		this.name = name;
		this.cardNo = cardNo;
	}

	public User(String cardNo, String userID, String birthday, String sex,
			String imageUrl, String email, String nickName,
			String customerGuid, String name, String userGuid, String mobile) {
		super();
		this.cardNo = cardNo;
		this.userID = userID;
		this.birthday = birthday;
		this.sex = sex;
		this.imageUrl = imageUrl;
		this.email = email;
		this.nickName = nickName;
		this.customerGuid = customerGuid;
		this.name = name;
		this.userGuid = userGuid;
		this.mobile = mobile;
	}

	public String getUserID() {
		return userID;
	}

	public String getCardNo() {
		return cardNo;
	}

	public String getBirthday() {
		return birthday;
	}

	public String getSex() {
		return sex;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getEmail() {
		return email;
	}

	public String getNickName() {
		return nickName;
	}

	public String getCustomerGuid() {
		return customerGuid;
	}

	public String getName() {
		return name;
	}

	public String getUserGuid() {
		return userGuid;
	}

	public String getMobile() {
		return mobile;
	}

	/***
	 * 复制一个对象
	 * 
	 * @return
	 */
	public User copy() {
		return new User(cardNo, userID, birthday, sex, imageUrl, email,
				nickName, customerGuid, name, userGuid, mobile);
	}

	/***
	 * 获取所有属性
	 * 
	 * @return
	 */
	private String[] getAttrs() {
		return new String[] { cardNo, userID, birthday, sex, imageUrl, email,
				nickName, customerGuid, name, userGuid, mobile };
	}

	/***
	 * 将当前对象的所有属性的source替换为desc,由于替换null
	 * 
	 * @param source
	 * @param desc
	 */
	public void replace(String source, String desc) {
		String[] attrs = getAttrs();
		for (int i = 0; i < attrs.length; i++) {
			String attr = attrs[i];
			if (attr != null)
				attr = attr.trim();
			if (attr == null || attr.length() == 0 || attr.equals(source))
				attrs[i] = desc;
		}
		setAttrs(attrs);
	}

	/***
	 * 设置属性
	 * 
	 * @param attrs
	 */
	private void setAttrs(String[] attrs) {
		int i = 0;
		cardNo = attrs[i++];
		userID = attrs[i++];
		birthday = attrs[i++];
		sex = attrs[i++];
		imageUrl = attrs[i++];
		email = attrs[i++];
		nickName = attrs[i++];
		customerGuid = attrs[i++];
		name = attrs[i++];
		userGuid = attrs[i++];
		mobile = attrs[i++];
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
