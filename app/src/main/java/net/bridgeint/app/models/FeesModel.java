package net.bridgeint.app.models;

public class FeesModel{
	private String fees;
	private String count;

	public void setFees(String fees){
		this.fees = fees;
	}

	public String getFees(){
		return fees;
	}

	public void setCount(String count){
		this.count = count;
	}

	public String getCount(){
		return count;
	}

	@Override
 	public String toString(){
		return 
			"FeesModel{" + 
			"fees = '" + fees + '\'' + 
			",count = '" + count + '\'' + 
			"}";
		}
}
