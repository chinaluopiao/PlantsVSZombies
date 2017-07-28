import org.loon.framework.javase.game.core.graphics.LImage;



public class Card {

	private int cardID;
	private int objectID;
	private int sunNum;// 此级卡片所需的太阳
	private int upNum;// 卡片升级所需的太阳
	private int lv;
	private int[] info;
	private  String infomation = null;
	private boolean Plant = true;
	private int imageID;
	private float cdTime; // 卡片的cd时间
	private float cd = 0; // 卡片已经经过的cd时间
	private boolean cdOK = false; // 卡片cd是否已经好了
	private LImage cardImage = null;
	private boolean upAble = false;

	public Card(int cardID) {
		this.cardID = cardID;
		if (this.cardID > 100)
			this.Plant = false;
		adapter(cardID);

	}

	public String getInfomation() {
		return this.infomation;
	}

	public boolean isPlant() {
		return Plant;
	}

	public int getLv() {
		return lv;
	}

	public int getSunNum() {
		return sunNum;
	}

	float getCdTime() {
		return cdTime;
	}

	public boolean isCdOK() {
		return cdOK;
	}

	public void setCd(float cd) {
		this.cd = cd;
	}

	public float getCd() {
		return cd;
	}

	public void setCdOK(boolean cdOK) {
		this.cdOK = cdOK;
	}

	public int getCardID() {
		return cardID;
	}

	public LImage getCardImage() {
		return cardImage;
	}

	public int getUpNum() {
		return upNum;
	}

	public int getObjectID() {
		return objectID;
	}

	// 适配卡片信息
	private void adapter(int cardID) {
		this.info = Resource.getCardInfo(cardID);
		this.objectID = info[1];
		this.cdTime = info[20];
		this.sunNum = info[6];
		this.imageID = info[15];
		this.upNum = info[11];
		this.cardImage = Resource.getCardImage(this.imageID);
		this.infomation =  Resource.getCardInfomation()[this.objectID-1];
	}

	public void jugdeUp() {
		if (Main.mySunNum >= info[11 + this.lv]) {
			this.upAble = true;
		} else {
			this.upAble = false;
		}
	}

	public boolean isUpAble() {
		return upAble;
	}

	public void setUpAble(boolean upAble) {
		this.upAble = upAble;
	}

	// 卡片升级时的数据处理
	public void up() {
		Main.mySunNum -= this.upNum;
		lv++;
		this.upNum = info[11 + lv];
		this.objectID = info[1 + lv];
		this.cdTime = info[20 + lv];
		this.sunNum = info[6 + lv];
		this.imageID = info[15 + lv];
		this.cardImage = Resource.getCardImage(this.imageID);
		this.cd=0;
		this.cdOK=false;
		this.infomation =  Resource.getCardInfomation()[this.objectID-1];
	}
}
