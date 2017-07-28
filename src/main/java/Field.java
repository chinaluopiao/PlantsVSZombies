import org.loon.framework.javase.game.core.graphics.LImage;

public class Field {
	private boolean handleAble; // 表示此格是否可以放下
	private boolean onLeft = true; // 表示是不是在左边
	private int x, y; // 此格的x,y坐标
	private int cx, cy;// 此格的中心坐标
	private int id; // 表示此格的id号
	private Fields fields = null;
	private static int netObjectID = 1; // 所有可放置物体的网络id号
	private int objectID = 0; // 本草地上放置的物体的id号
	private int netFieldObjectID; // 本草地上放置物体的netObjectID号
	private static Main main = null;
	private int mpx;
	private int rowID; // 此格所在的行数，用于碰撞检测
	private Plant myPlant;
	Magic mDa = null; // 此草地上的加攻魔法效果
	Magic mDf = null; // 此草地上的加防魔法效果
	Magic mSp = null; // 此草地上的加速魔法效果
	Magic mHp = null; // 此草地上的加血魔法效果

	// private static int PX ,PY; //此格的逻辑x,y坐标
	public Field(int id, int x, int y, boolean handleAble, Fields fields) {
		this.handleAble = handleAble;
		this.x = x;
		this.y = y;
		this.fields = fields;
		this.id = id;
		cx = x + Fields.FIELDWIDTH / 2;
		cy = y + Fields.FIELDHIGHT / 2;
		main = fields.getMain();
	}

	public Field() {

	}

	public boolean isHandleAble() {
		return handleAble;
	}

	public void setHandleAble(boolean handleAble) {
		this.handleAble = handleAble;
	}

	public boolean isLeftSide() {
		return onLeft;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setRowID(int rowID) {
		this.rowID = rowID;
	}

	public int getCx() {
		return cx;
	}

	public int getCy() {
		return cy;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		String say = "id:" + id + ",x:" + x + ",y:" + this.y + ",左边:" + onLeft
				+ "行号" + this.rowID;
		return say;
	}

	public void locate(int holdCardRange) {
		if (holdCardRange == -2 || holdCardRange == -1)
			return;
		int cardID = CardsFrame.cardRangeID[holdCardRange];
		int objectID = main.cf.allCards.get(holdCardRange).getObjectID();
		LImage image = null;
		if (objectID > 300) {
			int animationID = Resource.getZombieInfo(objectID - 300)[3];
			image = Resource.getZombieAnimation(animationID).getSpriteImage(0)
					.getLImage();

		} else {
			int animationID = Resource.getPlantInfo(objectID)[4];
			image = Resource.getPlantAnimation(animationID).getSpriteImage(0)
					.getLImage();
		}
		int width, hight;
		width = image.getWidth();
		hight = image.getHeight();
		int x = cx - width / 2;
		int y = cy - hight / 2;
		Card temCard = main.cf.allCards.get(holdCardRange);
		// 根据此卡片对应的植物ID进行安放
		adapter(temCard.getObjectID(), x += main.mpx, y);
		int num = temCard.getSunNum();
		main.mySunNum -= num;
		if (netObjectID > 60000)
			netObjectID = 1;

	}

	public Plant getMyPlant() {
		return myPlant;
	}

	public void adapter(int objectID, int x, int y) {
		netObjectID++;
		this.netFieldObjectID = netObjectID;
		this.objectID = objectID;
		if (objectID <= 300) {
			this.handleAble = false;
			Plant p = new Plant(objectID, main.isLeft, netObjectID, this.id, x,
					y, main.plants);
			p.setRowID(this.rowID);
			this.myPlant = p;
		}

		if (objectID > 300) {
			int zombieID = objectID - 300;
			Zombie z = new Zombie(main.isLeft, netObjectID, zombieID, this.id,
					x, y, main.zombies);
			z.setRowID(this.rowID);
		}

	}

	public void adapterNet(int mainID, boolean onLeft, int netObjectID,
			int objectID, int fieldID, int x, int y, String netName) {
		this.netFieldObjectID = netObjectID;
		this.objectID = objectID;
		// 此处x,y不必通过网络传过来，有待优化
		LImage image = null;
		if (objectID > 300) {
			int animationID = Resource.getZombieInfo(objectID - 300)[3];
			image = Resource.getZombieAnimation(animationID).getSpriteImage(0)
					.getLImage();

		} else {
			int animationID = Resource.getPlantInfo(objectID)[4];
			image = Resource.getPlantAnimation(animationID).getSpriteImage(0)
					.getLImage();
		}
		int width, hight;
		width = image.getWidth();
		hight = image.getHeight();
		x = cx - width / 2;
		y = cy - hight / 2;
		if (objectID <= 300) {
			Plant p = null;
			p = new Plant(objectID, onLeft, netObjectID, fieldID, x + Main.mpx,
					y, Main.plants, true);
			this.myPlant = p;
			p.setRowID(this.rowID);
			p.setMainID(mainID);
			p.setNetName(netName);
			Plants.newPlantList.add(p);
		}

		if (objectID > 300) {
			Zombie z = null;
			int zombieID = objectID - 300;
			z = new Zombie(onLeft, netObjectID, zombieID, fieldID,
					x + Main.mpx, y, Main.zombies, true);
			z.setRowID(this.rowID);
			z.setNetName(netName);
			z.setMainID(mainID);
			z.setFieldID(fieldID);
			Zombies.newZombieList.add(z);
		}

	}

	public void locateRobot(int objectID) {
		netObjectID++;
		this.netFieldObjectID = netObjectID;
		this.objectID = objectID;
		LImage image = null;
		if (objectID > 300) {
			int animationID = Resource.getZombieInfo(objectID - 300)[3];
			image = Resource.getZombieAnimation(animationID).getSpriteImage(0)
					.getLImage();

		} else {
			int animationID = Resource.getPlantInfo(objectID)[4];
			image = Resource.getPlantAnimation(animationID).getSpriteImage(0)
					.getLImage();
		}
		int width, hight;
		width = image.getWidth();
		hight = image.getHeight();
		int x = cx - width / 2;
		int y = cy - hight / 2;
		if (objectID <= 300) {
			Plant p = new Plant(objectID, !Main.isLeft, netObjectID, this.id, x
					+ main.mpx, y, Main.plants);
			p.setRowID(this.rowID);
			p.setNetName("");
			this.myPlant = p;
		}
		if (objectID > 300) {
			int zombieID = objectID - 300;
			Zombie z = new Zombie(Field.netObjectID, !Main.isLeft, zombieID,
					this.id, x + main.mpx, y, Main.zombies);
			z.setRowID(this.rowID);
		}

	}

	public void locateNetRobot(int objectID, int mainID, int netObjectID) {
		this.netFieldObjectID = netObjectID;
		this.objectID = objectID;
		LImage image = null;
		if (objectID > 300) {
			int animationID = Resource.getZombieInfo(objectID - 300)[3];
			image = Resource.getZombieAnimation(animationID).getSpriteImage(0)
					.getLImage();

		} else {
			int animationID = Resource.getPlantInfo(objectID)[4];
			image = Resource.getPlantAnimation(animationID).getSpriteImage(0)
					.getLImage();
		}
		int width, hight;
		width = image.getWidth();
		hight = image.getHeight();
		int x = cx - width / 2;
		int y = cy - hight / 2;
		if (objectID <= 300) {
			Plant p = new Plant(objectID, !Main.isLeft, netObjectID, this.id, x
					+ main.mpx, y, Main.plants);
			p.setRowID(this.rowID);
			p.setNetName("");
			p.setMainID(mainID);
			p.setMainID(netObjectID);
			this.myPlant = p;
			main.plants.newPlantList.add(p);
		}
		if (objectID > 300) {
			int zombieID = objectID - 300;
			Zombie z = new Zombie(netObjectID, !Main.isLeft, zombieID,
					this.id, x + main.mpx, y, Main.zombies, true);
			z.setRowID(this.rowID);
			z.setMainID(mainID);
			main.zombies.newZombieList.add(z);
		}

	}

	// 扫帚清除本草地上的物体
	public void removeObject() {
		if (null == this.myPlant || myPlant.getMainID() != Main.netID) {
			return;
		}
		myPlant.dead();
		this.myPlant = null;
		this.reDrawAllBuff();
		RemoveObjectMsg msg = new RemoveObjectMsg(RemoveObjectMsg.PLANT,
				this.netFieldObjectID, main);
		main.nc.send(msg);
	}

	public void netRemoveObject() {
		if (myPlant != null) {
			myPlant.dead();
			this.myPlant = null;
			this.reDrawAllBuff();
		}
	}

	// 画本草地上的buff效果
	public void drawBuff(int objectID, int type) {
		// System.out.println(MagicID);
		switch (type) {
		case Plant.BUFFDA:
			mDa = new Magic(objectID, x - 30 + Main.mpx, y + 30, main);
			break;
		case Plant.BUFFDF:
			mDf = new Magic(objectID, x - 30 + Main.mpx, y + 30, main);
			break;
		case Plant.BUFFSP:
			mSp = new Magic(objectID, x - 30 + Main.mpx, y + 30, main);
			break;
		case Plant.BUFFHP:
			mHp = new Magic(objectID, x - 30 + Main.mpx, y + 30, main);
			break;

		}

	}

	public void reDrawBuff(int type) {
		switch (type) {
		case Plant.BUFFDA:
			if (null != mDa)
				mDa.setGc(true);
			break;
		case Plant.BUFFDF:
			if (null != mDf)
				mDf.setGc(true);
			break;
		case Plant.BUFFSP:
			if (null != mSp)
				mSp.setGc(true);
			break;
		case Plant.BUFFHP:
			if (null != mHp)
				mHp.setGc(true);
			break;
		}
	}

	public void setMyPlant(Plant myPlant) {
		this.myPlant = myPlant;
		if (null == myPlant)
			this.reDrawAllBuff();
	}

	public void reDrawAllBuff() {
		if (null != mDa)
			mDa.setGc(true);
		if (null != mDf)
			mDf.setGc(true);
		if (null != mSp)
			mSp.setGc(true);
		if (null != mHp)
			mHp.setGc(true);

	}

	public void doClick() {
		if (null != this.myPlant && this.myPlant.isClickedAble()) {
			this.myPlant.doClick();
			main.clickObject = true;
		}

	}

	public void redoClick() {
		if (null != this.myPlant) {
			this.myPlant.redoClick();
		}

	}

}
