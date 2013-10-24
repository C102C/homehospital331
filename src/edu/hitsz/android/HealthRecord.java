package edu.hitsz.android;
//个人健康记录
public class HealthRecord {
	public String bp;			//血压
	public String bpAdvice;		//血压建议
	public String bo;			//血氧
	public String boAdvice;		//血氧建议
	public String fat;			//脂肪
	public String fatAdvice;	//脂肪建议
	public String pulse;		//脉率
	public String pulseAdvice;	//脉率建议
	public String heart;		//胎心
	public String heartAdvice;	//胎心建议
	public String glu;			//血糖
	public String gluAdvice;	//血糖建议
	public String weight;		//体重
	public String weightAdvice; //体重建议
	public String temperature;	//体温
	public String temperatureAdvice; //体温建议
	public HealthRecord(String bp, String bpAdvice, String bo, String boAdvice, String fat, String fatAdvice, String pulse, String pulseAdvice,
			String heart, String heartAdvice, String glu, String gluAdvice, String weight, String weightAdvice, String temperature, String temperatureAdvice){
		this.bp = bp;
		this.bpAdvice = bpAdvice;
		this.bo = bo;
		this.boAdvice = boAdvice;
		this.fat = fat;
		this.fatAdvice = fatAdvice;
		this.pulse = pulse;
		this.pulseAdvice = pulseAdvice;
		this.heart = heart;
		this.heartAdvice = heartAdvice;
		this.glu = glu;
		this.gluAdvice = gluAdvice;
		this.weight = weight;
		this.weightAdvice = weightAdvice;
		this.temperature = temperature;
		this.temperatureAdvice = temperatureAdvice;
	}
	
}
