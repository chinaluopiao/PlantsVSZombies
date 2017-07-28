

public class NetMsg {

	private String netName =null;
	private int netID;
	private boolean  Left=true;
	private boolean  action =false ; 
	public NetMsg(int netID,String netName){
		this.netID=netID;
		this.netName=netName;
	}


	public boolean isAction() {
		return action;
	}


	public void setAction(boolean action) {
		this.action = action;
	}


	public String getNetName() {
		return netName;
	}


	

	public boolean isLeft() {
		return Left;
	}
	public void setLeft(boolean left) {
		Left = left;
	}
	public int getNetID() {
		return netID;
	}
	@Override
	public String toString() {
	   String temp = netName; 
	  return temp;	
	}
}
