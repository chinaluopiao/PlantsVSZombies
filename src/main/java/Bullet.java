import org.loon.framework.javase.game.action.map.shapes.RectBox;
import org.loon.framework.javase.game.action.map.shapes.Vector2D;
import org.loon.framework.javase.game.action.sprite.Animation;
import org.loon.framework.javase.game.action.sprite.Sprite;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.core.timer.LTimer;

public class Bullet extends Sprite implements ObjectIn {
	private int bulletID;
	private int[] bInfo;
	private int mx; // 子弹的地图x坐标
	private int da; // 子弹攻击力
	static final int LINE = 1; // 子弹类型常量
	static final int PARABOLA = 2; // 子弹类型常量
	static final int LOWERSHOTSPEED = 3; // 子弹类型常量
	static final int LOWERMOVESPEED = 4; // 子弹类型常量
	static final int STOP = 5; // 子弹类型常量(没有位移的子弹动画)(辣椒)
	static final int ROWBOMB = 6; // 子弹类型常量（行炸弹）
	static final int SPECIAL_RAY = 7; // 子弹类型常量（行炸弹）
	private int bulletType; // 子弹发射方式
	private int fn; // 子弹帧数
	private int speed; // 子弹飞行速度
	private static Bullets bullets = null;
	private int imageTime;
	private LTimer imageTimer; // update刷新频率
	private LTimer synchronizedTimer = new LTimer(300); // 同步计时器
	private LTimer moveTimer = new LTimer(30); // 处理移动
	private static Main main = null;
	private Animation animation;
	private boolean onLeft = true;
	private boolean hited = false;
	private boolean gc;
	private RectBox rect = new RectBox();
	private int bxr = 0, byr = 0, bwr = 0, bhr = 0;// 碰撞检测盒偏移常量
	private int bx, by; // 碰撞检测盒
	private int n;
	private int angle; // 子弹发射角度
	private int x, y; // 子弹初始的x,y值
	private float xin, yin, tan, v0;// 子弹按角度飞行，x,y上的增量
	private static final double PI = 3.1415926;
	static final int infoStart = 24;
	private float count;
	private int rowID;
	private int mainID;
	private int bCustom1, bCustom2, bCustom3;
	private int magicAnimationID;
	private int tempInt;
	private boolean tempBoolean;
	private int animationID; // bullet图片的id
	private static final int objectType = ObjectIn.BULLET;
	private static int netPublicID;
	private int netBulletID;
	private boolean netObject;
	private int plantType;
	private boolean rowCollisioned; // 是否按照行检测碰撞

	public Bullet(boolean onLeft, int rowID, int x, int y, int bulletID,
			Bullets bullets) {
		this.setLayer(7);
		this.x = x;
		this.bullets = bullets;
		this.y = y;
		this.rowID = rowID;
		this.onLeft = onLeft;
		this.bulletID = bulletID;
		assignment();
		init();
		Bullet.netPublicID++;
		if (Bullet.netPublicID > 60000)
			Bullet.netPublicID = 0;
		this.netBulletID = Bullet.netPublicID;
		plantType = Resource.getPlantInfo(bulletID)[1];
		if (plantType != Plant.MULITANGLE && plantType != Plant.PARABOLA
				&& plantType != Plant.SPIN) {
			NewBulletMsg msg = new NewBulletMsg(this.netBulletID, onLeft, x
					- Main.mpx, y, bulletID, this.da, this.angle,
					(int) this.v0, this.rowID, main);
			main.nc.send(msg);
		}
		bullets.getAllBullets().add(this);
	}

	// 接收网络初始化信息
	public Bullet(int netBulletID, boolean onLeft, int x, int y, int bulletID,
			int da, int angle, int v0, int rowID, Bullets bullets) {
		this.setLayer(7);
		this.x = x;
		this.da = da;
		this.angle = angle;
		this.v0 = v0;
		this.rowID = rowID;
		Bullet.bullets = bullets;
		this.y = y;
		this.onLeft = onLeft;
		this.bulletID = bulletID;
		assignment();
		init();
		Bullet.netPublicID++;
		this.netBulletID = netBulletID;
		this.netObject = true;
	}

	private void assignment() {
		bInfo = Resource.getPlantInfo(bulletID);
		this.bulletType = bInfo[infoStart];
		this.imageTime = bInfo[infoStart + 1];
		this.speed = bInfo[infoStart + 2];
		this.bxr = bInfo[infoStart + 3];
		this.byr = bInfo[infoStart + 4];
		this.bwr = bInfo[infoStart + 5];
		this.bhr = bInfo[infoStart + 6];
		this.bCustom1 = bInfo[infoStart + 7];
		this.bCustom2 = bInfo[infoStart + 8];
		this.bCustom3 = bInfo[infoStart + 9];
		this.animationID = bInfo[infoStart + 10];
		this.da = bInfo[6];
		this.magicAnimationID = bInfo[44];
		animation = Resource.getBulletAnimation(animationID);
	}

	@Override
	public void createUI(LGraphics g) {
		if (this.bulletType == Bullet.SPECIAL_RAY) {
			if (this.bulletType == Bullet.SPECIAL_RAY && !this.tempBoolean) {
				if (this.onLeft) {
					x += this.bCustom1;
				} else {
					x -= this.bCustom1;
				}
			}
			for (int i = 0; i < this.tempInt; i++) {
				if (this.onLeft) {
					g.drawImage(animation.getSpriteImage().serializablelImage
							.getImage(), x - i * this.bCustom1, y);
				} else {
					g.drawImage(animation.getSpriteImage().serializablelImage
							.getImage(), x + i * this.bCustom1, y);
				}

			}

			if (!this.tempBoolean) {
				this.tempInt++;
			}else{
				this.tempBoolean=false;
			}
			return;
		}

		if (this.onLeft) {
			g.drawImage(animation.getSpriteImage().serializablelImage
					.getImage(), x, y);
		} else {
			g.drawMirrorImage(animation.getSpriteImage().serializablelImage
					.getImage(), x, y);
		}
	}

	public int getDa() {
		return da;
	}

	public int getRowID() {
		return rowID;
	}

	public void setRowID(int rowID) {
		this.rowID = rowID;
	}

	public boolean isRowCollisioned() {
		return rowCollisioned;
	}

	public boolean isGc() {
		return gc;
	}

	public int getMainID() {
		return mainID;
	}

	public void setMainID(int mainID) {
		this.mainID = mainID;
	}

	public int getBulletID() {
		return bulletID;
	}

	public void setDa(int da) {
		this.da = da;
	}

	public int getBCustom1() {
		return bCustom1;
	}

	public int getBCustom2() {
		return bCustom2;
	}

	public int getNetBulletID() {
		return netBulletID;
	}

	public void setHited(boolean hited) {
		this.hited = hited;
	}

	public boolean isHited() {
		return hited;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getBCustom3() {
		return bCustom3;
	}

	public void setRowCollisioned(boolean rowCollisioned) {
		this.rowCollisioned = rowCollisioned;
	}

	public void setV0(float v0) {
		this.v0 = v0;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}

	public void setGc(boolean gc) {
		this.gc = gc;
	}

	public void update(long timer) {
		// 处理子弹同步
		if (this.synchronizedTimer.action(timer) && !this.netObject) {
			SynchronizedMsg msg = new SynchronizedMsg(
					SynchronizedMsg.BULLETMOVE, this.netBulletID, x - Main.mpx,
					main);
			main.nc.send(msg);
		}

		// 植物动画
		if (imageTimer.action(timer)) {
			n++;
			if (n == fn) {
				if (this.bulletType == Bullet.SPECIAL_RAY) {
					this.gc = true;
				}
				if (this.bulletType == Bullet.STOP) {
					this.dead();
					return;
				}
				n = 0;
			}
			this.setCurrentFrameIndex(n);
		}

		// 处理子弹移动
		if (this.moveTimer.action(timer)) {
			if (this.bulletType == Bullet.PARABOLA) {
				count += 0.1;
				float g = 4f;
				x += xin;
				y += (v0 * count + 0.5 * g * count * count);
				dead();
				return;
			}
			if (this.bulletType == Bullet.STOP) {
				return;
			}

			x += xin;
			y += yin;
			dead();
		}

	}

	public void mapMove(int reverse) {
		// 修正地图移动时的x
		x += reverse;
	}

	public void dead() {
		mx = x - main.mpx;
		if (this.bulletType == Bullet.PARABOLA && this.y > this.tempInt) {
			new Magic(this.bulletID, x, y, main);
			this.gc = true;
		}
		if ((this.mx < 0 || this.mx > 2800 || this.y > 600)
				&& this.bulletType != Bullet.ROWBOMB) {
			if (this.bulletType == Bullet.SPECIAL_RAY) {
				this.tempBoolean = true;
				return;
			}
			this.gc = true;

		}

		if (this.bulletType == Bullet.ROWBOMB && (mx < -200 || mx > 3000)) {
			this.gc = true;
		}
		if (this.hited) {
			if (this.bulletType == Bullet.SPECIAL_RAY) {
				this.tempBoolean = true;
				return;
			}
			if (this.bulletType == Bullet.ROWBOMB
					|| this.bulletType == Bullet.SPECIAL_RAY)
				return;
			if (this.magicAnimationID == 0) {
				this.gc = true;
			} else {
				new Magic(this.bulletID, x, y, main);
				this.gc = true;
			}

		}
		if (this.bulletType == Bullet.STOP)
			this.gc = true;
	}

	public void init() {
		this.setAnimation(animation);
		if (main == null) {
			main = bullets.getMain();
		}
		// 确定初始角度
		if (onLeft) {
			this.doRotate(0);
		} else {
			this.doRotate(180);
		}

		// 保存抛物线的起始y坐标
		if (this.bulletType == Bullet.PARABOLA) {
			this.tempInt = this.y;
		}
		if (this.bulletType == Bullet.ROWBOMB) {
		}

		if (this.bulletType == Bullet.SPECIAL_RAY) {
			// 复制子弹块的数量
			this.tempInt = 0;
			// 是否触碰到敌人
			this.tempBoolean = false;
		}

		fn = animation.getTotalFrames();
		this.imageTimer = new LTimer(this.imageTime);
		int Width = this.getWidth();
		int Height = this.getHeight();
		if (this.bxr == 0)
			bx = x;
		if (this.byr == 0)
			by = y;
		if (this.bwr == 0)
			bwr = Width;
		if (this.bhr == 0)
			bhr = Height;
		if (!this.onLeft && !this.netObject) {
			x -= Width;
		}

		this.mainID = Main.netID;
		this.rowCollisioned = true;
	}

	public boolean isOnLeft() {
		return onLeft;
	}

	public int getBulletType() {
		return bulletType;
	}

	public Vector2D getLocation() {
		Vector2D v = new Vector2D(x, y);
		return v;
	}

	public void doRotate(int angle) {
		if (plantType == Plant.MULITANGLE || plantType == Plant.SPIN
				&& !this.netObject) {
			NewBulletMsg msg = new NewBulletMsg(this.netBulletID, onLeft, x
					- Main.mpx, y, bulletID, this.da, angle, (int) this.v0,
					this.rowID, main);
			main.nc.send(msg);
		}
		if (plantType == Plant.MULITANGLE || plantType == Plant.SPIN) {
			this.rowCollisioned = false;
		}

		this.angle = angle;
		Double a = PI * angle / 180;
		xin = (int) (speed * Math.cos(a));
		yin = (int) (speed * Math.sin(a));
	}

	public void doParabola(float v0, int angle) {
		if (plantType == Plant.PARABOLA && !this.netObject) {
			NewBulletMsg msg = new NewBulletMsg(this.netBulletID, onLeft, x
					- Main.mpx, y, bulletID, this.da, angle, (int) v0,
					this.rowID, main);
			main.nc.send(msg);
		}
		this.v0 = -v0;
		this.angle = angle;
		if (!this.onLeft) {
			if (!tempBoolean && angle == 30) {
			} else {
				this.tempBoolean = true;
				this.angle = 90 - this.angle;
			}

		}
		Double a = PI * this.angle / 180;
		xin = (float) (v0 * Math.cos(a));
		yin = (float) (v0 * Math.sin(a));
		tan = yin / xin;
		if (!this.onLeft) {
			xin = -xin;
		}

	}

	// 覆写碰撞检测
	@Override
	public RectBox getCollisionBox() {
		// 处理碰撞范围
		doCollisionCoord();
		if (rect == null) {
			rect = new RectBox(bx, by, bwr, bhr);
		} else {
			rect.setBounds(bx, by, bwr, bhr);
		}
		return rect;
	}

	private void doCollisionCoord() {
		bx = x + bxr;
		by = y + byr;

	}

	@Override
	public int getObjectType() {
		// TODO Auto-generated method stub
		return this.objectType;
	}

	public void setNetObject(boolean netObject) {
		this.netObject = netObject;
	}

}
