import java.awt.Color;

import org.loon.framework.javase.game.action.map.shapes.RectBox;
import org.loon.framework.javase.game.action.map.shapes.Vector2D;
import org.loon.framework.javase.game.action.sprite.Animation;
import org.loon.framework.javase.game.action.sprite.Sprite;
import org.loon.framework.javase.game.core.graphics.LFont;
import org.loon.framework.javase.game.core.graphics.LImage;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.core.timer.LTimer;

public class Zombie extends Sprite implements ObjectIn {
	private int zombieID;
	private int[] i = null;
	private int da;
	private int df;
	private int speed;
	private int hp;
	private int shotCdTime;
	private int shotType;
	private int cardID;
	private int animationID;
	private int type;
	private int x, y;
	private int mx;
	private int rowID;
	private int fieldID;
	private boolean dead;
	private int zombieType;
	private Zombies zombies = null;
	private LTimer imageTimer = null;
	private LTimer hitedTimer = null;
	private LTimer checkTimer = new LTimer(30); // 处理数据层内容
	private LTimer shotCdTimer = null;
	private LTimer buffTimer = null; // 减行走速度buff计时器
	private LTimer buffTimer1 = null; // 减攻击速度buff计时器
	private LTimer synchronizedTimer = new LTimer(400); // 同步计时器
	private int shadowX, shadowY; // 画阴影的x，y偏移量
	private static final int hitedTime = 100;
	private int buffTime = 10000; // buff持续时间
	private LTimer deadTimer = null; // 死亡动画播放的时间
	private Animation animation = null;
	private Main main = null;
	static final int NORMOL = 1; // 僵尸类型常量
	static final int SPECIAL = 2; // 僵尸类型常量
	private int netObjectID;
	private float maxHp;
	private boolean hited = false;
	private boolean hitedPic = false;
	private boolean collisioned = false;// 判断是否碰撞
	private boolean isRobot;
	private boolean myAttack = false;// 判断是否可攻击的转换开关
	private boolean moveAble = true;
	private boolean myMove = true;
	private boolean buffEnd = true, buff1End = true; // 判断buff效果是否结束()
	private boolean gc; // 判断此植物 是否要被null
	private int speedTemp, buffSpeedTemp, buffShotCdTemp, buffDaTemp,
			buffDfTemp;
	private static LImage shadow = Resource.getShadow();
	private static int sw = shadow.getWidth();// 僵尸阴影的宽和高
	private boolean onLeft = true;
	private boolean attackAble;
	private int cxr, cyr, cwr, chr;// 碰撞检测盒偏移常量
	private int zxr, zyr; // 植物图片的x,y偏移
	private int cx, cy; // 碰撞检测盒
	private int bulletValue, bulletValue1; // 减行走速度，攻击速度buff开关
	private int mainID;
	private String netName;
	private static final int objectType = ObjectIn.ZOMBIE;
	private static final LFont l = new LFont("Arial Black", 0, 12); // 画netName的字体
	private int attbfn, attefn, deadbfn, deadefn, movebfn, moveefn; // 僵尸移动，攻击，
	// 死亡的起始
	// ，结束帧数
	static final int NUM = 1; // 参数类型常量（数值）
	static final int PERCENT = 2; // 参数类型常量(百分比)
	private int bfn, efn, n;// 临时变量动画，起始结束帧数，以及变量帧数
	private int nxr, nyr; // 网名坐标的x,y偏移值
	private RectBox rect;
	private boolean netObject;
	private int zcustom1, zcustom2, zcustom3, zcustom4, zcustom5;
	private int powerType;
	private int moveImageTime, attackImageTime, deadImageTime, powerImageTime,
			powerbfn, powerefn;
	private int powerCd, powerValue1, powerValue2, powerValue3;
	static final int SPECIAL_CHUANCI = 1; // Special僵尸类型常量
	static final int SPECIAL_ZHIMING = 2; // Special僵尸类型常量
	private int tempInt;
	private boolean tempBoolean;

	public Zombie(boolean onLeft, int netObjectID, int zombieID, int fieldID,
			int x, int y, Zombies zombies) {
		this.setLayer(5);
		this.x = x;
		this.onLeft = onLeft;
		this.fieldID = fieldID;
		this.netObjectID = netObjectID;
		this.zombies = zombies;
		this.y = y;
		this.zombieID = zombieID;
		assignment();
		this.init();
		this.mainID = Main.netID;
		zombies.getAllZombies().add(this);
		LocateMsg msg = new LocateMsg(this.onLeft, this.fieldID,
				this.netObjectID, this.zombieID + 300, x - main.mpx, y, main);
		main.nc.send(msg);

	}

	// 网络接收初始化
	public Zombie(boolean onLeft, int netObjectID, int zombieID, int fieldID,
			int x, int y, Zombies zombies, boolean net) {
		this.setLayer(5);
		this.x = x;
		this.onLeft = onLeft;
		this.netObjectID = netObjectID;
		this.zombies = zombies;
		this.fieldID = fieldID;
		this.y = y;
		this.zombieID = zombieID;
		assignment();
		this.init();
		this.netObject = true;
	}

	// 处理机器人
	public Zombie(int netObjectID, boolean onLeft, int zombieID, int fieldID,
			int x, int y, Zombies zombies) {
		this.setLayer(5);
		this.netObjectID = netObjectID;
		this.onLeft = onLeft;
		this.fieldID = fieldID;
		this.zombies = zombies;
		this.x = x;
		this.y = y;
		this.zombieID = zombieID;
		assignment();
		this.init();
		zombies.getAllZombies().add(this);
		this.isRobot = true;
		RobotNewMsg rnm = new RobotNewMsg(this.fieldID, this.netObjectID,
				this.zombieID + 300, main);
		main.nc.send(rnm);
	}

	// 处理网络机器人
	public Zombie(int netObjectID, boolean onLeft, int zombieID, int fieldID,
			int x, int y, Zombies zombies, boolean net) {
		this.setLayer(5);
		this.netObjectID = netObjectID;
		this.onLeft = onLeft;
		this.fieldID = fieldID;
		this.zombies = zombies;
		this.zombieID = zombieID;
		this.x = x;
		this.y = y;
		assignment();
		this.init();
		this.isRobot = true;
		this.netObject = true;
	}

	private void assignment() {
		i = Resource.getZombieInfo(zombieID);
		zombieType = i[1];
		shotCdTime = i[2];
		animationID = i[3];
		hp = i[3];
		speed = i[4];
		hp = i[5];
		this.maxHp = hp;
		da = i[6];
		df = i[7];
		cxr = i[8];
		cyr = i[9];
		cwr = i[10];
		chr = i[11];
		zxr = i[12];
		zyr = i[13];
		shadowX = i[14];
		shadowY = i[15];
		zcustom1 = i[16];
		zcustom2 = i[17];
		zcustom3 = i[18];
		zcustom4 = i[19];
		zcustom5 = i[20];
		this.powerType = i[21];
		this.moveImageTime = i[24];
		movebfn = i[25];
		moveefn = i[26];
		this.attackImageTime = i[27];
		attbfn = i[28];
		attefn = i[29];
		this.powerImageTime = i[30];
		this.powerbfn = i[31];
		this.powerefn = i[32];
		this.deadImageTime = i[33];
		this.deadbfn = i[34];
		this.powerValue1 = i[46];
		this.powerValue2 = i[47];
		this.powerValue3 = i[48];
		this.powerCd = i[49];
		animation = Resource.getZombieAnimation(animationID);
		this.setAnimation(animation);
	}

	private void init() {
		shotCdTimer = new LTimer(shotCdTime);
		buffTimer = new LTimer(this.buffTime);
		buffTimer1 = new LTimer(this.buffTime);
		imageTimer = new LTimer(this.moveImageTime);
		this.myMove = true;
		if (main == null) {
			main = zombies.getMain();
		}
		efn = this.moveefn;
		bfn = this.movebfn;
		n = bfn;
		this.mainID = main.netID;
		this.netName = main.netName;
		if (this.cxr == 0)
			cx = x;
		if (this.cyr == 0)
			cy = y;
		if (this.cwr == 0)
			cwr = this.getWidth();
		if (this.chr == 0)
			chr = this.getHeight();
		if (this.onLeft) {
			x = x + zxr;
			y = y + zyr;
			nxr = cxr;
		} else {
			int w = this.getWidth();
			int h = this.getHeight();
			shadowX = w - sw - shadowX;
			nxr = w - cwr - cxr;
			x = x - zxr;
			y = y + zyr;
			cxr = w - cxr - cwr;
		}
		nyr = cyr - 2;
	}

	@Override
	public void createUI(LGraphics g) {
		// 检测游戏结束
		this.checkGameOver(g);
		drawNetName(g);
		// 画僵尸阴影
		double d = g.getAlpha();
		g.setAlpha(0.5f);
		if (shadowX != 0 && shadowY != 0)
			g.drawImage(shadow, x + shadowX, y + shadowY);
		g.setAlpha(d);

		if (hitedPic) {
			drawHited(1, g);
		} else {

			if (!this.onLeft) {
				g.drawMirrorImage(animation.getSpriteImage().serializablelImage
						.getImage(), x, y);

			} else {
				g.drawImage(animation.getSpriteImage().serializablelImage
						.getImage(), x, y);
			}

		}
		if (this.hp >= 0)
			drawStatusBar(g);

	}

	// 僵尸动画
	public void update(long timer) {
		// 处理僵尸的出生同步
		if (this.synchronizedTimer.action(timer)) {
			if (!this.netObject && !this.isRobot) {
				LocateMsg msg = new LocateMsg(this.onLeft, this.fieldID,
						this.netObjectID, this.zombieID + 300, x - main.mpx, y,
						main);
				main.nc.send(msg);
			}
			if (!this.netObject && this.isRobot) {
				RobotNewMsg rnm = new RobotNewMsg(this.fieldID,
						this.netObjectID, this.zombieID + 300, main);
				main.nc.send(rnm);
			}
			if (!this.netObject) {
				SynchronizedMsg sm = new SynchronizedMsg(
						SynchronizedMsg.ZOMBIESYN, this.netObjectID, this.x
								- Main.mpx, this.hp, main);
				main.nc.send(sm);
			}
		}

		// 处理僵尸的移动
		if (this.checkTimer.action(timer)) {
			if (this.myMove) {
				if (onLeft) {
					x += speed;

				} else {
					x -= speed;
				}
			}
			if (this.hp <= 0) {
				dead();
			}
		}

		// 处理攻击间隔
		if (shotCdTimer.action(timer)) {
			this.attackAble = true;
		} else {
			this.attackAble = false;
		}
		// 处理buff效果的结束
		// 减移动速度
		if (null != buffTimer && buffTimer.action(timer) && !this.buffEnd) {
			this.speed = this.buffSpeedTemp;
			this.da = this.buffSpeedTemp;
			this.bulletValue = -1;
			this.buffEnd = true;
			this.moveImageTime = i[24];
			if (this.myMove)
				this.imageTimer.setDelay(this.moveImageTime);
		}
		// 处理从攻击转为移动的速度重置
		if (null != buffTimer && this.myAttack && !this.buffEnd) {
			this.speed = this.tempInt;
		}

		// 减攻击速度
		if (null != buffTimer1 && buffTimer1.action(timer) && !this.buff1End) {
			this.shotCdTime = this.buffShotCdTemp;
			this.df = this.buffDfTemp;
			this.bulletValue1 = -1;
			this.buff1End = true;
			this.attackImageTime = i[27];
			if (this.myAttack)
				this.imageTimer.setDelay(this.attackImageTime);
			this.shotCdTimer.setDelay(this.shotCdTime);
		}

		if (imageTimer.action(timer)) {

			// 处理杀死植物后的移动
			if (this.collisioned == false && myAttack == true && !dead) {
				this.moveAble = true;
				this.myMove();
			}

			// 处理攻击
			if (this.hited == true) {
				this.hitedPic = true;
				hitedTimer = new LTimer(this.hitedTime);
				this.hited = false;
			}
			if (this.collisioned && myAttack == false && !dead) {
				attack();
			}

			// 处理移动
			if (n == efn) {
				// 处理死亡
				if (n == efn && n > deadbfn && this.deadImageTime != 0) {
					if (deadTimer.action(timer)) {
						this.gc = true;
					}
					return;
				}
				n = bfn;
			} else {
				n++;
			}

			this.setCurrentFrameIndex(n);
		}

		// 画被击中图片的cd
		if (hitedPic) {
			if (hitedTimer.action(timer))
				this.hitedPic = false;

		}
	}

	public boolean isGc() {
		return gc;
	}

	public boolean isDead() {
		return dead;
	}

	public boolean isAttackAble() {
		return attackAble;
	}

	public int getRowID() {
		return rowID;
	}

	public void setRobot(boolean isRobot) {
		this.isRobot = isRobot;
	}

	public void setRowID(int rowID) {
		this.rowID = rowID;
	}

	public boolean isRobot() {
		return isRobot;
	}

	public int getMainID() {
		return mainID;
	}

	public void setMainID(int mainID) {
		this.mainID = mainID;
	}

	public void setFieldID(int fieldID) {
		this.fieldID = fieldID;
	}

	public int getZombieID() {
		return zombieID;
	}

	public Vector2D getLocation() {
		Vector2D v = new Vector2D(x, y);
		return v;
	}

	public void setCollisioned(boolean collisioned) {
		this.collisioned = collisioned;
	}

	public boolean isHited() {
		return hited;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getNetObjectID() {
		return netObjectID;
	}

	public void setNetObject(boolean netObject) {
		this.netObject = netObject;
	}

	public boolean isNetObject() {
		return netObject;
	}

	public int getDa() {
		return da;
	}

	public void setGc(boolean gc) {
		this.gc = gc;
	}

	public boolean isOnLeft() {
		return onLeft;
	}

	public void mapMove(int reverse) {
		// 修正地图移动时的x
		x += reverse;
	}

	public void dead() {
		if (dead == false) {
			if (this.deadImageTime == 0) {
				this.gc = true;
				return;
			}
			efn = this.animation.getTotalFrames();
			bfn = this.deadbfn;
			n = bfn;
			speed = 0;
			dead = true;
			deadTimer = new LTimer(this.deadImageTime);

		}

	}

	private void drawHited(int bulletID, LGraphics g) {
		double d = g.getAlpha();
		g.setAlpha(0.1f);
		if (this.onLeft) {
			g.drawImage(animation.getSpriteImage().serializablelImage
					.getImage(), x, y);
		} else {
			g.drawMirrorImage(animation.getSpriteImage().serializablelImage
					.getImage(), x, y);
		}
		g.setAlpha(d);
	}

	private void attack() {
		efn = this.attefn;
		bfn = this.attbfn;
		n = bfn;
		moveAble = false;
		speedTemp = speed;
		speed = 0;
		myAttack = true;
		this.myMove = false;
		this.imageTimer.setDelay(this.attackImageTime);

	}

	private void myMove() {
		efn = this.moveefn;
		bfn = this.movebfn;
		n = bfn;
		speed = speedTemp;
		n = 0;
		myAttack = false;
		this.myMove = true;
		this.imageTimer.setDelay(this.moveImageTime);
	}

	private void checkHarm(int bulletDa) {
		int c = bulletDa - this.df;
		if (c > 0 && hp > 0) {
			this.hp -= c;
		}
	}

	private void checkHarm(int bulletDa, boolean rowBomb) {
		if (hp > 0) {
			this.hp -= bulletDa;
		}
	}

	public void setHited(boolean hited) {
		this.hited = hited;
	}

	public void setHited(boolean hited, int bulletID) {
		int[] info = Resource.getPlantInfo(bulletID);
		int bulletDa = info[6];
		int bulletType = info[24];
		int bcustom1 = info[31];
		int bcustom2 = info[32];
		int bcustom3 = info[33];
		if (bulletType == Bullet.LOWERSHOTSPEED
				|| bulletType == Bullet.LOWERMOVESPEED) {
			this.doBulletType(bulletType, bcustom1, bcustom2, bcustom3);
		}
		this.hited = hited;
		if (bulletType == Bullet.ROWBOMB || bulletType == Bullet.SPECIAL_RAY
				|| bulletID == Plant.SPECIAL_CLOSE) {
			this.checkHarm(bulletDa, true);
		} else {
			checkHarm(bulletDa);
		}
	}

	// 处理子弹类型
	private void doBulletType(int bulletType, int bulletValue, int bcustom2,
			int bcustom3) {
		switch (bulletType) {
		case Bullet.LOWERMOVESPEED:
			if (this.bulletValue != bulletValue) {
				this.buffDaTemp = this.da;
				this.da -= bcustom2;
				if (this.da < 0)
					this.da = 0;
				buffSpeedTemp = this.speed;
				int speed = (bulletValue * this.speed) / 100;
				if (this.myMove) {
					this.speed = speed;
					if (this.speed <= 1)
						this.speed = 1;
				} else {
					this.tempInt = speed;
				}
				this.buffEnd = false;
				this.bulletValue = bulletValue;
				this.moveImageTime = this.moveImageTime
						+ (this.moveImageTime * bulletValue) / 100;
				if (this.myMove)
					this.imageTimer.setDelay(this.moveImageTime);
				int buffTime = bcustom3;
				this.buffTimer.setDelay(buffTime);
				this.buffTimer.setCurrentTick(0);

			} else {
				this.buffTimer.setCurrentTick(0);
			}
			break;
		case Bullet.LOWERSHOTSPEED:
			if (this.bulletValue1 != bulletValue) {
				this.buffDfTemp = this.df;
				this.df -= bcustom2;
				if (this.df < 0)
					this.df = 0;
				buffShotCdTemp = this.shotCdTime;
				this.shotCdTime = this.shotCdTime
						+ (bulletValue * this.shotCdTime) / 100;
				this.buff1End = false;
				this.bulletValue1 = bulletValue;
				this.attackImageTime = this.attackImageTime
						+ (this.attackImageTime * bulletValue) / 100;
				if (this.myAttack)
					this.imageTimer.setDelay(this.attackImageTime);
				int buffTime = bcustom3;
				this.buffTimer1.setDelay(buffTime);
				this.buffTimer1.setCurrentTick(0);
				this.shotCdTimer.setDelay(this.shotCdTime);
			} else {
				this.buffTimer1.setCurrentTick(0);
			}
			break;

		}

	}

	// 检测僵尸过界的游戏结束
	private void checkGameOver(LGraphics g) {
		if (this.isRobot) {
			if (Main.isLeft) {
				if (x < -50 + main.mpx)
					main.doGameOver(true, g);
			} else {
				if (x > 2450 + main.mpx)
					main.doGameOver(false, g);
			}
			return;
		}
		if (this.onLeft) {
			if (x > 2450 + main.mpx)
				main.doGameOver(false, g);
		} else {
			if (x < -50 + main.mpx)
				main.doGameOver(true, g);
		}

	}

	private void drawNetName(LGraphics g) {
		if (!this.isRobot) {
			g.setFont(l);
			g.setColor(Color.BLACK);
			g.drawString(this.netName, x + nxr, y + nyr);
		}
	}

	public void setNetName(String netName) {
		this.netName = netName;
	}

	private void drawStatusBar(LGraphics g) {
		float t = this.hp / this.maxHp;
		t = t * 60;
		Color c = g.getColor();
		g.setColor(Color.RED);
		g.fillRect(x + nxr, y + nyr - 17, 60, 5);
		g.setColor(Color.GREEN);
		g.fillRect(x + nxr, y + nyr - 17, (int) t, 5);

		g.setColor(c);
	}

	// 覆写碰撞检测
	@Override
	public RectBox getCollisionBox() {
		// 处理碰撞范围
		doCollisionCoord();
		if (rect == null) {
			rect = new RectBox(cx, cy, cwr, chr);
		} else {
			rect.setBounds(cx, cy, cwr, chr);
		}
		return rect;
	}

	private void doCollisionCoord() {
		cx = x + cxr;
		cy = y + cyr;

	}

	// 处理反弹伤害
	public void doBounceHarm(int custom4) {
		int num = custom4 * this.da / 100;
		this.hp -= num;
		this.hited = true;
	}

	@Override
	public int getObjectType() {
		return this.objectType;
	}

}
