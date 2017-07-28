import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.loon.framework.javase.game.action.sprite.Animation;
import org.loon.framework.javase.game.action.sprite.Sprite;
import org.loon.framework.javase.game.core.LSystem;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.core.timer.LTimer;

public class Drop extends Sprite {
	private int x, y;
	private int speed = 2;
	private int dropID;
	private int netDropID;
	private boolean hited = false;
	private static String dropImage = Resource.getDropImage();
	private LTimer time = null;
	private static Animation animation = Animation.getDefaultAnimation(
			dropImage, 78, 68, 15);
	static final int FLOWER = 1;
	private static Main main = null;
	private static int backSpeed = 20; // 点中太阳花后的返回速度
	private boolean onLeft;
	private static final int DROP_RANGE = 800; // 随机掉落的宽度
	private int randBegin; // 随机掉落的起始点坐标
	private int upSunNum = 50; // 点击到所增加的太阳数
	static List<Drop> allDrops = new ArrayList<Drop>();
	static List<Drop> addNetDrops = new ArrayList<Drop>();
	private int mainID;
	private boolean  gc ;
	public Drop(int dropID, int netDropID, Main main) {
		this.dropID = dropID;
		this.netDropID = netDropID;
		this.main = main;
		this.onLeft = Main.isLeft;
		this.mainID = Main.netID;
		// 随机得到x的初始坐标
		if (this.onLeft) {
			this.randBegin = 10;
		} else {
			this.randBegin = 1590;
		}
		this.x = LSystem.getRandom(randBegin, randBegin + this.DROP_RANGE);
		this.setLocation(this.x, y);
		// 处理太阳花的网络
		if (this.dropID == Drop.FLOWER) {
			// System.out.println(this.x+"发送");
			Msg msg = new DropMsg(this.mainID, DropMsg.SHOW, this.dropID,
					this.netDropID, this.x, main);
			main.nc.send(msg);
		}
		this.x += main.mpx;
		animation.setRunning(true);
		this.setAnimation(animation);
		time = new LTimer(30);
		allDrops.add(this);
	}
	//	处理网络太阳花
	public Drop(int dropID, int mainID, int netDropID, int x, Main main) {
		this.mainID = mainID;
		this.x = x;
		this.setLocation(this.x, y);
		this.x += main.mpx;
		this.dropID = dropID;
		this.netDropID = netDropID;
		this.main = main;
		animation.setRunning(true);
		this.setAnimation(animation);
		time = new LTimer(30);
		this.addNetDrops.add(this);
	}

	public void createUI(LGraphics g) {
		if (hited) {
			y -= backSpeed;
		}
		g.drawImage(animation.getSpriteImage().serializablelImage.getImage(),
				x, y);

	}

	public void update(long timer) {
		this.setY(y);
		// 植物动画
		if (y >= 610 || y <= -10)
			dead();
		if (time.action(timer)) {
			int n = this.getCurrentFrameIndex();
			n++;
			if (n == 22)
				n = 0;
			this.setCurrentFrameIndex(n);
			if (hited == false) {
				y += speed;
			}

		}
	}

	private void mapMove(int reverse) {
		// 修正地图移动时的x
		x += reverse;
	}

	public void beHited() {
		if (this.hited == true) {
			return;
		} else {
			this.hited = true;
			main.mySunNum += upSunNum;
		}
	}

	public void dead() {
		this.gc =true;
	}

	public static void drawAllDrops(LGraphics g) {
			for (Drop d : Drop.allDrops) {
				d.createUI(g);
			}
		
	}
	
	public static void addNetDrops() {
		synchronized (Drop.addNetDrops) {
			Iterator<Drop> ite = Drop.addNetDrops.iterator();
			while (ite.hasNext()) {
				Drop d = (Drop) ite.next();
				Drop.allDrops.add(d);
				ite.remove();
			}
		}
	}

	
	public static void mapMoveDrops(int reverse) {
			for (Drop d : Drop.allDrops) {
				d.mapMove(reverse);
			}
	}
	
	public static void updateAllDrops(long timer) {
		synchronized (Drop.allDrops) {
			Iterator iter = Drop.allDrops.iterator();
			while (iter.hasNext()) {
				Drop d = (Drop) iter.next();
				if (d.isGc()) {
					Msg msg = new DropMsg(d.mainID, DropMsg.CLICKED, Drop.FLOWER,
							d.netDropID, main);
					main.nc.send(msg);
					iter.remove();
				} else {
					d.update(timer);
				}
			}
		}
			for (Drop d : Drop.allDrops) {
								d.update(timer);
			}
		
	}

	
	// 处理太阳花升级后的太阳数的增长
	public void doUpSunNum(int num) {
		this.upSunNum = num;
	}

	public int getNetDropID() {
		return netDropID;
	}

	public int getMainID() {
		return mainID;
	}
	public boolean isGc() {
		return gc;
	}
	public void setGc(boolean gc) {
		this.gc = gc;
	}
	public boolean isHited() {
		return hited;
	}
	public void setHited(boolean hited) {
		this.hited = hited;
	}
		
}
