package edu.hitsz.android;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Client {
	public PersonInfo[] family = new PersonInfo[5];
	public HealthRecord record = null;
	public HealthInfo[] health = null;
	public String devMac1;	//血压
	public String devMac2;	//心电
	public String devMac3;	//血氧
	public String devMac4;	//脂肪
	public String devMac5;	//血糖
	public String devMac6;	//胎心
	public String devMac7;	//体重
	
	public static String URL="http://www.lkang.org/service.asmx";	//服务的网址
	public static String NAMESPACE="http://www.lkang.org/";		//服务的命名空间
	
	public static String INSERT_BPWITHPULSE_METHOD="insertBPWithpulse";	//血压包括脉率
	public static String INSERT_BPWITHOUTPULSE_METHOD="insertBPWithoutpulse";	//血压不包括脉率
	public static String INSERT_ECG_METHOD="insertECG";	//心电
	public static String INSERT_FAT_METHOD="insertFat";	//脂肪
	public static String INSERT_TEMPERATURE_METHOD="insertTemperature";	//体温
	public static String INSERT_WEIGHT_METHOD="insertWeight";	//体重
	public static String INSERT_GLU_METHOD="insertGLU";			//血糖
	public static String INSERT_BLOODOXYGEN_METHOD="insertBO";	//血氧
	public static String INSERT_PULSE_METHOD="insertPulse";	//脉率
	public static String INSERT_HEART_METHOD="insertHeart";	//胎心
	public static String GetFamilyInfo_METHOD="getFamilyInfo";	//获得家庭信息
	public static String GetHealthRecord_METHOD="getHealthRecord";	//获得健康记录
	public static String GetHealthInfo_METHOD="getHealthInfo";//获得健康咨询
	
	
	////以下插入数据，也可以采用http 的get方法传输数据
	public String insertBPWithpulse(String cardnum, String systolic, String diastolic, String pulse) throws Exception{
		SoapObject result = null;
		HashMap<String, String> para = new HashMap<String, String>();
		para.put("userid", cardnum);
		para.put("systolic", systolic);
		para.put("diastolic", diastolic);
		para.put("pulse", pulse);		
		result = Post(URL, NAMESPACE, INSERT_BPWITHPULSE_METHOD, para);	
 
		return result.getProperty(0).toString(); 
		
	}
	
	public String insertBPWithoutpulse(String cardnum, String systolic, String diastolic)throws Exception{
		SoapObject result = null;
		HashMap<String, String> para = new HashMap<String, String>();
		para.put("userid", cardnum);
		para.put("systolic", systolic);
		para.put("diastolic", diastolic);		
		result = Post(URL, NAMESPACE, INSERT_BPWITHOUTPULSE_METHOD, para);
		return result.getProperty(0).toString(); 
		 

	}
	
	public String insertECG(String cardnum, byte[]buffer) throws Exception{
		SoapObject result = null;
		HashMap<String, String> para = new HashMap<String, String>();
		String temp = Base64.encode(buffer);
		para.put("userid", cardnum);
		para.put("ecg", temp);
		result = Post(URL, NAMESPACE, INSERT_ECG_METHOD, para);
		return result.getProperty(0).toString(); 
	}
	
	public String insertFat(String cardnum, String fatcontent) throws Exception{
		SoapObject result = null;
		HashMap<String, String> para = new HashMap<String, String>();
		para.put("userid", cardnum);
		para.put("fatcontent", fatcontent);
		result = Post(URL, NAMESPACE, INSERT_FAT_METHOD,para);
		return result.getProperty(0).toString(); 

	}
	
	public String insertTemperature(String cardnum, String temperature) throws Exception{
		SoapObject result = null;
		HashMap<String, String> para = new HashMap<String, String>();
		para.put("userid", cardnum);
		para.put("temperature", temperature);		
		result = Post(URL, NAMESPACE,INSERT_TEMPERATURE_METHOD, para);

		return result.getProperty(0).toString(); 
	}
	
	public String insertWeight(String cardnum, String weight) throws Exception{
		SoapObject result = null;
		HashMap<String, String> para = new HashMap<String, String>();
		para.put("userid", cardnum);
		para.put("weight", weight);
		result = Post(URL, NAMESPACE, INSERT_WEIGHT_METHOD, para);	
		return result.getProperty(0).toString(); 
	}
	
	public String insertGLU(String cardnum, String gluContent) throws Exception{
		SoapObject result = null;
		HashMap<String, String> para = new HashMap<String, String>();
		para.put("userid", cardnum);
		para.put("gluContent", gluContent);		
		result = Post(URL, NAMESPACE, INSERT_GLU_METHOD, para);
		return result.getProperty(0).toString(); 
	}
 
	public String insertBO(String cardnum, String boContent) throws Exception{
		SoapObject result = null;
		HashMap<String, String> para = new HashMap<String, String>();
		para.put("userid", cardnum);
		para.put("boContent", boContent);		
		result = Post(URL, NAMESPACE,INSERT_BLOODOXYGEN_METHOD, para);
		return result.getProperty(0).toString(); 
	}
	
	public String insertPulse(String cardnum, String pulse) throws Exception{
		SoapObject result = null;
		HashMap<String, String> para = new HashMap<String, String>();
		para.put("userid", cardnum);
		para.put("pulse", pulse);		
		result = Post(URL, NAMESPACE,INSERT_PULSE_METHOD, para);
		return result.getProperty(0).toString(); 
	}
	
	public String insertHeart(String cardnum, String heart) throws Exception{
		SoapObject result = null;
		HashMap<String, String> para = new HashMap<String, String>();
		para.put("userid", cardnum);
		para.put("heart", heart);		
		result = Post(URL, NAMESPACE,INSERT_HEART_METHOD, para);
		return result.getProperty(0).toString(); 
	}
	
	
	//////////////////////////////////////////////////////////////////////////
	////////////////用以下方法获得数据                              //////////////////////////////
	//////////////////////////////////////////////////////////////////////////
	
	
	////以下获得数据
public void getFamilyInfo(String clientMac) throws Exception{
		
		SoapObject result = null, detail = null;
		HashMap<String, String> para = new HashMap<String, String>();
		para.put("clientMac", clientMac);
		result = Post(URL, NAMESPACE, GetFamilyInfo_METHOD, para);		
	
		detail = (SoapObject) result.getProperty(0);
		
		String name = null;
		String gender =null;
		String cardnum=null;
		String birthday = null;
		
		for(int i = 0; i < 5; i++){
			if(detail.getProperty(0+i*5)!=null)
				name = detail.getProperty(0+i*5).toString();
			else
				name = String.format("用户%d", i);
			if(detail.getProperty(1+i*5)!=null)
				cardnum = detail.getProperty(1+i*5).toString();
			else
				throw new Exception("未获得分配的卡号");
			if(detail.getProperty(2+i*5)!=null)
				gender = detail.getProperty(2+5*i).toString();
			else gender="男";
			if(detail.getProperty(3+i*5)!= null)
				birthday = detail.getProperty(3+i*5).toString();
			else{
				final Calendar c = Calendar.getInstance();
				String myear=String.valueOf(c.get(Calendar.YEAR));
		        birthday = myear;
			}


			family[i] = new PersonInfo(name, cardnum, gender,birthday,Base64.decode(detail.getProperty(4+5*i).toString()));
		}		
		this.devMac1 = detail.getProperty(25).toString();
		this.devMac2 = detail.getProperty(26).toString();
		this.devMac3 = detail.getProperty(27).toString();
		this.devMac4 = detail.getProperty(28).toString();
		this.devMac5 = detail.getProperty(29).toString();
		this.devMac6 = detail.getProperty(30).toString();
		this.devMac7 = detail.getProperty(31).toString();		
	}
	
	public boolean getHealthRecord(String cardnum) throws Exception{
		SoapObject result = null, detail = null;
		HashMap<String, String> para = new HashMap<String, String>();
		para.put("userid", cardnum);		
		result = Post(URL, NAMESPACE, GetHealthRecord_METHOD, para);
		String bp = null;
		String bpAdvice = null;
		String bo = null;
		String boAdvice = null;
		String fat = null;
		String fatAdvice = null;
		String pulse=null;
		String pulseAdvice = null;
		String heart=null;
		String heartAdvice = null;
		String glu = null;
		String gluAdvice = null;
		String weight = null;
		String weightAdvice=null;
		String temperature=null;
		String temperatureAdvice = null;
		
		detail = (SoapObject)result.getProperty(0);
		if(detail.getProperty(0).toString().equals("ok")){
			if(detail.getProperty(1)!=null)
				bp = detail.getProperty(1).toString();
			if(detail.getProperty(2)!=null)
				bpAdvice = detail.getProperty(2).toString();
			if(detail.getProperty(3)!=null)
				bo = detail.getProperty(3).toString();
			if(detail.getProperty(4)!=null)
				boAdvice = detail.getProperty(4).toString();
			if(detail.getProperty(5)!=null)
				fat = detail.getProperty(5).toString();
			if(detail.getProperty(6)!=null)
				fatAdvice = detail.getProperty(6).toString();
			if(detail.getProperty(7)!=null)
				pulse = detail.getProperty(7).toString();
			if(detail.getProperty(8)!=null)
				pulseAdvice = detail.getProperty(8).toString();
			if(detail.getProperty(9)!=null)
				heart = detail.getProperty(9).toString();
			if(detail.getProperty(10)!=null)
				heartAdvice = detail.getProperty(10).toString();
			if(detail.getProperty(11)!=null)
				glu = detail.getProperty(11).toString();
			if(detail.getProperty(12)!=null)
				gluAdvice = detail.getProperty(12).toString();
			if(detail.getProperty(13)!=null)
				weight = detail.getProperty(13).toString();
			if(detail.getProperty(14)!=null)
				weightAdvice = detail.getProperty(14).toString();
			if(detail.getProperty(15)!=null)
				temperature = detail.getProperty(15).toString();
			if(detail.getProperty(16)!=null)
				temperatureAdvice = detail.getProperty(16).toString();	
			this.record = new HealthRecord(bp, bpAdvice, bo, boAdvice, fat, fatAdvice, pulse, pulseAdvice, heart, heartAdvice, glu, gluAdvice, weight, weightAdvice, temperature, temperatureAdvice);
			return true;
		}
		else
			return false;
	}
	public void getHealthInfo() throws Exception{
		SoapObject result = null, detail = null;
		HashMap<String, String> para = new HashMap<String,String>();
		result = Post(URL, NAMESPACE, GetHealthInfo_METHOD, para);
		detail = (SoapObject)result.getProperty(0);
		int  num = Integer.parseInt(detail.getProperty(0).toString());
		
		health = new HealthInfo[num];
		
		for(int i = 0; i < num; i++){
			health[i] = new HealthInfo(detail.getProperty(1+i*2).toString(), detail.getProperty(2+i*2).toString());
		}		
	}
	
	
	
	
	private SoapObject Post(String URL, String namespace, String method, HashMap<String, String> params) throws Exception{
		SoapObject result=null;
		
		SoapObject rpc = new SoapObject(namespace, method); 
		HttpTransportSE ht = new HttpTransportSE(URL); 
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);		
		for(Map.Entry<String, String> m: params.entrySet()){
			PropertyInfo pi = new PropertyInfo();
			pi.setName(m.getKey());
			pi.setValue(m.getValue());
			pi.setType(String.class);
			rpc.addProperty(pi);
		}	
		ht.debug = true;   
	    envelope.bodyOut = rpc;   
	    envelope.dotNet = true;   
	    envelope.setOutputSoapObject(rpc);  
		ht.call(namespace+method, envelope);	
		result = (SoapObject)envelope.bodyIn;
		return result;
	}
	

}
