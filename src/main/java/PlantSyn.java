
public class PlantSyn {
	 private int mainID,netObjectID ,hp ,type;

	public PlantSyn(int mainID,int type ,int hp, int netObjectID) {
		this.hp = hp;
		this.mainID = mainID;
		this.type = type ; 
		this.netObjectID = netObjectID;
	}
	
	public PlantSyn(int mainID,int type ,int netObjectID) {
		this.type = type ; 
		this.netObjectID = netObjectID;
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
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getMainID() {
		return mainID;
	}

	public void setMainID(int mainID) {
		this.mainID = mainID;
	}
	
}
