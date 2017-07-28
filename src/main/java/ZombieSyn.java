
public class ZombieSyn {
	private int  mainID,netObjectID,hp,x;

	public ZombieSyn( int mainID, int netObjectID, int hp,int x) {
		this.hp = hp;
		this.mainID = mainID;
		this.netObjectID = netObjectID;
		this.x = x;
	}

	public int getMainID() {
		return mainID;
	}

	public void setMainID(int mainID) {
		this.mainID = mainID;
	}

	public int getNetObjectID() {
		return netObjectID;
	}

	public void setNetObjectID(int netObjectID) {
		this.netObjectID = netObjectID;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	
}
