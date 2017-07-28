
public class BulletMoveSyn {
	private int mainID ,netObjectID,x;

	public BulletMoveSyn(int mainID, int netObjectID, int x) {
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

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

}
