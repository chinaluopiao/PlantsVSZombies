
public class MsgActionSyn {
	private int mainID;
	private boolean action ;
	private boolean onLeft;
	public MsgActionSyn(boolean action,boolean onLeft, int mainID) {
		this.action = action;
		this.mainID = mainID;
		this.onLeft =onLeft;
	}
	public int getMainID() {
		return mainID;
	}
	public void setMainID(int mainID) {
		this.mainID = mainID;
	}
	public boolean isAction() {
		return action;
	}
	public void setAction(boolean action) {
		this.action = action;	
	}
	public void setOnLeft(boolean onLeft) {
		this.onLeft = onLeft;
	}
	public boolean isOnLeft() {
		return onLeft;
	} 
	
		
}
