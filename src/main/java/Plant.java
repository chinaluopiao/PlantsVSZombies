import java.awt.Color;

import org.loon.framework.javase.game.action.map.shapes.RectBox;
import org.loon.framework.javase.game.action.sprite.Animation;
import org.loon.framework.javase.game.action.sprite.Sprite;
import org.loon.framework.javase.game.core.LSystem;
import org.loon.framework.javase.game.core.graphics.LFont;
import org.loon.framework.javase.game.core.graphics.LImage;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.core.timer.LTimer;

public class Plant extends Sprite implements ObjectIn {
	private int netPlantID; // 植物的网络ID
	private int objectID; // 植物的类型id
	static final int ATTACK = 1; // 植物的类型常量
	static final int DEFENED = 2; // 植物的类型常量
	static final int FLOWER = 3; // 植物的类型常量
	static final int MULITANGLE = 4; // 植物的类型常量
	static final int BOMB = 5; // 植物的类型常量
	static final int SPIKE = 6; // 植物的类型常量
	static final int MINE = 7; // 植物的类型常量
	static final int SPIN = 8; // 植物的类型常量
	static final int DEBUFF = 9; // 植物的类型常量
	static final int PARABOLA = 10; // 植物的类型常量
	static final int AOBUFF = 11; // 植物的类型常量
	static final int DFATTACK = 12; // 植物的类型常量
	static final int ROWBOMB = 13; // 植物的类型常量
	static final int SPECIAL_CLOSE = 14; // 植物的类型常量
	static final int SPECIAL_RAY = 15; // 植物的类型常量
	static final int BRAIN = 20; // 植物的类型常量
	private int[] info;
	private Animation animation;
	private int xr, yr; // 子弹发射位置的x,y修正'
	private int bulletID;
	private int animationID;
	private int rowID; // 此植物所在的行号
	private static int netDropID;
	private LTimer imageTimer = null;
	private LTimer attackedTimer = null;
	private int imageTime; // 主动画刷新频率
	private int x, y;
	private static Plants plants;
	private int plantType;
	private boolean fireable = true;
	private boolean attacked;
	private boolean gc; // 判断此植物 是否要被null
	private boolean NetObject; // 判断此植物是否是由其它的主机传过来的
	private LTimer fireCD = null;
	private static Bullets bullets = null;
	private int hp;
	private boolean onLeft = true;
	private boolean hitedPic;
	private int fieldID;
	private static final int attackedTime = 100; // 被击中动画的播放时间
	private static final int checkBuffTime = 100; // 检测buff效果的间隔时间
	private static final int upBuffHpTime = 3000; // 增加buffhp的间隔时间
	private LTimer upBuffHpTimer = new LTimer(upBuffHpTime);
	private LTimer checkBuffTimer = null;
	private LTimer synchronizedTimer = new LTimer(1000); // 同步计时器
	private int da;
	private int df;
	private int shotCdTime;
	private int fn; // 动画的总帧数
	private static Bullets b;
	private static final LFont l = new LFont("Arial Black", 0, 12); // 画netName的字体
	private static Main main;
	private RectBox rect = new RectBox();
	private int cxr = 0, cyr = 0, cwr = 0, chr = 0;// 碰撞检测盒偏移常量
	private int cx, cy; // 碰撞检测盒
	private int pxr, pyr; // 植物的x,y坐标偏移
	private static final LImage shadow = Resource.getShadow();
	private static final int sw = 86;// 僵尸阴影的宽和高
	private int mainID;
	private String netName;
	private int shadowX, shadowY; // 画阴影的x，y偏移量
	private int nxr, nyr; // 网名坐标的x,y偏移值
	private int angle;
	private int custom1, custom2, custom3, custom4;// 自定义参数
	private int bfn, efn, n;// 临时变量动画，起始结束帧数，以及变量帧数
	private int passAble; // 0不可通过 ，1可通过
	private boolean hit; // 此植物是否打中了僵尸
	private boolean temp, temp1, temp2,temp3,temp4;
	private boolean isRobot;
	private boolean clicked, clickedAble; // 判断此植物是否被点击
	private boolean bounce; // 判断此植物是否有反弹的效果
	private float maxHp;// 植物的最大hp值
	private int field; // checkBuff中的临时field号
	static final int BUFFDA = 1; // buff植物的类型
	static final int BUFFDF = 2; // buff植物的类型
	static final int BUFFSP = 3; // buff植物的类型
	static final int BUFFHP = 4; // buff植物的类型
	static final int LOWERDA = 1; // debuff植物的类型
	static final int LOWERMOVESPEED = 2; // debuff植物的类型
	static final int LOWERDF = 3; // debuff植物的类型
	static final int LOWERSHOTSPEED = 4; // debuff植物的类型
	private static final int objectType = ObjectIn.PLANT;
	static final double PI = 3.1415;
	private int tempBuffDa, tempBuffDf, tempBuffSp, tempBuffImageTime,
			tempBuffHp;// buff效果的初始临时变量
	private boolean isBuffDa, isBuffDf, isBuffSp, isBuffHp;// buff效果的初始临时变量
	private static Animation arrowAnimation = Resource.getArrowAnimation(); // 点击箭头的图片
	static final int DFATXIXUE = 1;// 防攻型植物的类型
	static final int DFATBOUNCE = 2;// 防攻型植物的类型
	static final int CLICKFLOWER = 1;// 太阳花的类型
	static final int RECOVER = 2;// 太阳花的类型
	private boolean rowCollisioned; // 是否按照行检测碰撞
	private int locateX; //用于同步的初始植物x坐标

	public Plant(int objectID, boolean onLeft, int netPlantID, int fieldID,
			int x, int y, Plants plants) {

		this.setLayer(5);
		this.objectID = objectID;
		this.netPlantID = netPlantID;
		this.x = x;
		this.y = y;
		this.fieldID = fieldID;
		this.onLeft = onLeft;
		assignment();
		if (b == null) {
			this.plants = plants;
			main = plants.getMain();
			b = this.plants.getMain().bullets;
		}
		init();
		this.mainID = Main.netID;
		this.plants.allPlants.add(this);
		this.locateX =x-Main.mpx;
		LocateMsg msg = new LocateMsg(this.onLeft, this.fieldID,
				this.netPlantID, objectID, this.locateX, y, main);
		main.nc.send(msg);

	}

	// 处理网络接收初始化
	public Plant(int objectID, boolean onLeft, int netPlantID, int fieldID,
			int x, int y, Plants plants, boolean net) {

		this.setLayer(5);
		this.objectID = objectID;
		this.netPlantID = netPlantID;
		this.x = x;
		this.y = y;
		this.fieldID = fieldID;
		this.onLeft = onLeft;
		assignment();
		if (b == null) {
			this.plants = plants;
			main = plants.getMain();
			b = this.plants.getMain().bullets;
		}
		init();
		this.NetObject = true;
	}

	// 处理大脑
	public Plant(int objectID, boolean onLeft, int x, int y, Plants plants) {
		this.setLayer(5);
		this.objectID = objectID;
		this.x = x;
		this.y = y;
		this.onLeft = onLeft;
		assignment();
		if (b == null) {
			this.plants = plants;
			main = plants.getMain();
			b = this.plants.getMain().bullets;
		}
		init();
		this.mainID = Main.netID;
		this.plants.allPlants.add(this);
		this.NetObject = true;
	}

	private void assignment() {
		info = Resource.getPlantInfo(objectID);
		this.plantType = info[1];
		this.imageTime = info[2];
		this.shotCdTime = info[3];
		this.animationID = info[4];
		animation = Resource.getPlantAnimation(animationID);
		this.hp = info[5];
		this.maxHp = this.hp;
		this.da = info[6];
		this.df = info[7];
		this.cxr = info[8];
		this.cyr = info[9];
		this.cwr = info[10];
		this.chr = info[11];
		this.pxr = info[12];
		this.pyr = info[13];
		this.shadowX = info[14];
		this.shadowY = info[15];
		this.xr = info[16];
		this.yr = info[17];
		this.custom1 = info[18];
		this.custom2 = info[19];
		this.custom3 = info[20];
		this.custom4 = info[21];
		this.bulletID = this.objectID;
	}

	// 覆写碰撞检测
	@Override
	public RectBox getCollisionBox() {
		// System.out.println(this.cx+","+this.cwr);
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

	@Override
	public void createUI(LGraphics g) {
		drawNetName(g);
		// 画被击中时的画面
		this.setLocation(x, y);
		if (this.hitedPic) {
			drawHited(g);
		} else {
			// 画植物帧图片
			if (this.onLeft) {
				g.drawImage(animation.getSpriteImage().serializablelImage
						.getImage(), x, y);
			} else {
				g.drawMirrorImage(animation.getSpriteImage().serializablelImage
						.getImage(), x, y);
			}
		}
		// 画植物阴影
		if (shadowX == shadowY && shadowX == 0) {
		} else {
			double d = g.getAlpha();
			g.setAlpha(0.5f);
			g.drawImage(shadow, x + shadowX, y + shadowY);
			g.setAlpha(d);
		}

		// 处理被点击时的箭头
		if (this.clicked) {
			int angle = 0;
			if (this.onLeft && this.plantType == Plant.PARABOLA) {
				angle = 360 - (this.angle) + 90;
				if (angle > 360)
					angle -= 360;
				// parabola的可调整角度在第一象限内
				if (angle > 180) {
					angle = 0;
				} else {
					if (angle > 90)
						angle = 90;
				}

			}
			if (this.plantType == Plant.PARABOLA && !this.onLeft) {
				angle = 360 - (this.angle);
				if (angle > 360)
					angle -= 360;
				if (angle >= 0 && angle < 180) {
					angle = 360;
				} else if (angle >= 180 && angle <= 270) {
					angle = 270;
				}

			}
			if (this.plantType == Plant.SPIN) {
				angle = 360 - (this.angle) + 90;
				if (angle > 360)
					angle -= 360;
			}
			if (this.plantType != Plant.SPECIAL_CLOSE  && this.plantType != Plant.SPECIAL_RAY ) {
				g.drawImage(Plant.arrowAnimation.getSpriteImage().rotate(angle)
						.getBitmap(4), info[22] + x, info[23] + y - 60);
			}
		}
		if (this.hp > 0)
			drawStatusBar(g);

	}

	private void drawStatusBar(LGraphics g) {
		float t = this.hp / this.maxHp;
		t = t * 60;
		Color c = g.getColor();
		g.setColor(Color.RED);
		g.fillRect(this.x + nxr, this.y + nyr - 17, 60, 5);
		g.setColor(Color.GREEN);
		g.fillRect(this.x + nxr, this.y + nyr - 17, (int) t, 5);
		g.setColor(c);
	}

	public void update(long timer) {
		// 处理植物同步
		if (this.synchronizedTimer.action(timer) && !this.NetObject) {
			LocateMsg msg = new LocateMsg(this.onLeft, this.fieldID,
					this.netPlantID, objectID, this.locateX, y, main);
			main.nc.send(msg);
			SynchronizedMsg sm = new SynchronizedMsg(SynchronizedMsg.PLANTHP,
					this.netPlantID, this.hp, main, true);
			main.nc.send(sm);
		}

		// 处理buff植物的检测
		if (null != this.checkBuffTimer) {
			if (checkBuffTimer.action(timer)) {
				this.checkBuffRange();
			}
		}
		// 处理upBuffHpTimer
		if (this.upBuffHpTimer.action(timer) && this.isBuffHp) {
			this.buffUpHp();
		}

		// 处理攻击型植物
		if (this.plantType == Plant.ATTACK || this.plantType == Plant.DEBUFF
				|| this.plantType == Plant.PARABOLA
				|| this.plantType == Plant.MULITANGLE
				|| this.plantType == Plant.SPIN) {
			if (fireCD.action(timer)) {
				this.fire();
			}
		}
		// 处理太阳花的产花
		if (this.plantType == Plant.FLOWER) {
			if (fireCD.action(timer)) {
				// 如果不是自己的太阳花
				if (!this.NetObject)
					this.newSun();
			}
		}

		// 算点击抛物线时的角度
		if (this.plantType == Plant.PARABOLA && this.clicked) {
			this.angle = checkAngle();
			ObjectMsg msg = new ObjectMsg(ObjectMsg.PARABOLA, this.netPlantID,
					this.angle, main);
			main.nc.send(msg);

		}
		// 算点击spin植物的角度
		if (this.plantType == Plant.SPIN && this.clicked) {
			this.angle = this.checkAngle();
			ObjectMsg msg = new ObjectMsg(ObjectMsg.SPIN, this.netPlantID,
					this.angle, main);
			main.nc.send(msg);
		}

		// 处理dfattack的攻击
		if (this.plantType == Plant.DFATTACK) {
			if (this.hit)
				doDfAttackType();
			this.fireable = false;
			this.hit = false;
			if (fireCD.action(timer)) {
				attackRange();
				this.fireable = true;
			}

		}

		// 处理地刺的攻击
		if (this.plantType == Plant.SPIKE) {
			this.fireable = false;
			if (fireCD.action(timer)) {

				attackRange();
				this.fireable = true;
				if (this.hit) {
					custom1--;
					if (custom1 < 0)
						dead();
					this.hit = false;

				}
			}

		}
		// 处理special_close
		if (this.plantType == Plant.SPECIAL_CLOSE) {
			// 如果已到（temp=是否到了cd时间）
			if (fireCD.action(timer) && temp == false) {
				this.temp = true;
			}
			if (this.temp1 && this.clicked && !this.temp3) {
				if (!this.NetObject) {
					SynchronizedMsg msg = new SynchronizedMsg(
							SynchronizedMsg.PLANTSPECIALCLICK, this.netPlantID,
							main);
					main.nc.send(msg);
				}
				this.bfn = custom2 + 1;
				this.efn = fn;
				main.clickObject = false;
				this.temp3=true;
			}
			if (this.temp) {
				if (!this.temp1) {
					this.bfn = custom1;
					this.efn = custom2;
					this.temp1 = true;
				}
			} else {
				this.n = 0;
			}
			if (this.temp2) {
				this.fireable = true;
				attackRange();
			}

		}

		// 处理special_ray
		if (this.plantType == Plant.SPECIAL_RAY) {
			// 如果已到（temp=是否到了cd时间）
			if (fireCD.action(timer) && temp == false) {
				this.temp = true;
			}
			if (this.temp1 && this.clicked && !this.temp4) {
				if (!this.NetObject) {
					SynchronizedMsg msg = new SynchronizedMsg(
							SynchronizedMsg.PLANTSPECIALCLICK, this.netPlantID,
							main);
					main.nc.send(msg);
				}
				this.bfn = custom2 + 1;
				this.efn = fn;
				this.temp4=true;
				main.clickObject = false;
			}
			if (this.temp) {
				if (!this.temp1) {
					this.bfn = custom1;
					this.efn = custom2;
					this.temp1 = true;
				}
			} else {
				this.n = 0;
			}
			//处理光线的发射
			if (this.temp2 && !this.temp3 && !this.NetObject) {
				new Bullet(this.onLeft, this.rowID, x + xr, y + yr,
						this.bulletID, b);
				this.temp3 =true;
			}
		}

		// 处理地雷
		if (this.plantType == Plant.MINE) {
			attackRange();
			if (this.hit && n < 4) {
				this.efn = fn;
				this.bfn = 5;
				this.n = 4;
			}
			if (fireCD.action(timer) && temp == false) {
				temp = true;
				this.efn = 4;
				this.bfn = 1;
			}
			if (!temp) {
				this.n = -1;
			}
		}
		// 处理被击中
		if (this.attacked == true) {
			this.hitedPic = true;
			attackedTimer = new LTimer(attackedTime);
			this.attacked = false;
		}

		// 画被击中图片的cd
		if (hitedPic) {
			if (attackedTimer.action(timer))
				this.hitedPic = false;
		}

		if (imageTimer.action(timer)) {
			// 处理防御型植物
			if (this.plantType == Plant.DEFENED || this.plantType == Plant.MINE
					|| this.plantType == Plant.SPECIAL_CLOSE
					|| this.plantType == Plant.SPECIAL_RAY) {
				if (n == efn) {
					if ((this.plantType == Plant.SPECIAL_CLOSE || this.plantType == Plant.SPECIAL_RAY)
							&& n == fn) {
						this.temp = false;
						this.temp1 = false;
						this.temp2 = false;
						this.temp3=false;
						this.temp4 =false;
						this.efn = 0;
						this.bfn = 0;
						this.n = 0;
						this.clicked = false;
						return;
					}

					n = bfn;
				} else {
					if (n == custom4 && (this.plantType == Plant.SPECIAL_CLOSE || this.plantType == Plant.SPECIAL_RAY)) {
						this.temp2 = true;
					}
					n++;
				}

				if (this.plantType != Plant.SPECIAL_CLOSE && this.plantType != Plant.SPECIAL_RAY  ) {
					if (this.plantType == Plant.MINE) {
						if (n == fn && this.hit)
							dead();

					} else {

						float f = (float) hp / this.maxHp;
						if (f > 0.6) {
							bfn = 0;
							efn = custom1 - 1;
						}
						if (f < 0.6 && f >= 0.2 && n < custom1) {
							bfn = custom1;
							efn = custom2 - 1;
						}
						if (f < 0.2 && n < custom2) {
							bfn = custom2;
							efn = fn;

						}
					}
				}
			} else {

				if (n == fn) {
					if (this.plantType == Plant.BOMB) {
						this.fireable = true;
						this.attackRange();
						this.dead();
					}
					// 处理行炸弹
					if (this.plantType == Plant.ROWBOMB) {
						this.fire();
						this.dead();
						return;
					}
					n = 0;
				} else {
					n++;
				}

			}
			// 植物动画
			this.setCurrentFrameIndex(n);
			if (hp < 0) {
				this.dead();
			}
		}
	}

	private void doDfAttackType() {
		switch (this.custom3) {
		case Plant.DFATXIXUE:
			int num = this.da * this.custom4 / 100;
			this.upHp(num);
			break;

		}

	}

	// 计算点击物体出现的调整角度箭头的角度
	private int checkAngle() {
		double angle;
		float tx = main.sx + main.mpx;
		float ty = main.sy;
		float ix = info[22] + x + main.mpx;
		float iy = info[23] + y;
		double k = (tx - ix) / (ty - iy);
		if (tx - ix > 0) {
			angle = 0;
		} else {
			angle = -this.PI;
		}
		angle = Math.atan(k) + (this.PI / 2);
		if (ty - iy > 0) {
			angle = angle + this.PI;
		}
		angle = (angle * 180 / this.PI);
		// 射出的角度范围在第一象限
		if (this.plantType == Plant.PARABOLA) {
			if (this.onLeft) {
				if (angle > 180) {
					angle = 0;
				} else if (angle > 90) {
					angle = 90;
				}
			} else {
				if (angle >= 0 && angle <= 90) {
					angle = 90;
				} else if (angle >= 180 && angle <= 360) {
					angle = 180;
				}
				angle -= 90;
			}
		}

		return (int) angle;
	}

	public void mapMove(int reverse) {
		// 修正地图移动时的x
		x += reverse;
	}

	public void fire() {
		if (this.NetObject)
			return;
		// 处理spin的子弹
		if (this.plantType == Plant.SPIN) {
			int angle = 360 - (this.angle);
			if (angle > 360)
				angle -= 360;
			new Bullet(this.onLeft, this.rowID, x + xr, y + yr, this.bulletID,
					b).doRotate(angle);
			return;
		}

		// 处理行炸弹的子弹
		if (this.plantType == Plant.ROWBOMB) {
			new Bullet(this.onLeft, this.rowID, x + xr, y + yr, this.bulletID,
					b);
			return;
		}

		// 处理普通射击的植物
		if (this.plantType == Plant.ATTACK) {
			new Bullet(this.onLeft, this.rowID, x + xr, y + yr, this.bulletID,
					b);
			return;
		}

		// 处理散射花
		if (this.plantType == Plant.MULITANGLE) {

			int u = 0;
			int d = 1;
			for (int i = 0; i < custom1; i++) {
				if (i % 2 == 0) {
					if (this.onLeft) {
						new Bullet(this.onLeft, this.rowID, x + xr, y + yr,
								this.bulletID, b).doRotate(u * custom2);
					} else {
						new Bullet(this.onLeft, this.rowID, x + xr, y + yr,
								this.bulletID, b).doRotate(u * custom2 + 180);
					}
					u++;
				} else {
					if (this.onLeft) {
						new Bullet(this.onLeft, this.rowID, x + xr, y + yr,
								this.bulletID, b).doRotate(d * (-custom2));
					} else {
						new Bullet(this.onLeft, this.rowID, x + xr, y + yr,
								this.bulletID, b)
								.doRotate(d * (-custom2) + 180);
					}
					d++;
				}
			}

			return;
		}

		if (this.plantType == Plant.PARABOLA) {
			new Bullet(this.onLeft, this.rowID, x + xr, y + yr, this.bulletID,
					b).doParabola(this.custom1, this.angle);
			return;
		}

		if (this.plantType == Plant.DEBUFF) {
			new Bullet(this.onLeft, this.rowID, x + xr, y + yr, this.bulletID,
					b);
			return;
		}

	}

	public void dead() {
		this.gc = true;
		if (this.plantType == Plant.BRAIN)
			return;
		if (this.onLeft == main.isLeft)
			main.fields.getAllField().get(this.fieldID).setHandleAble(true);
		main.fields.getAllField().get(this.fieldID).setMyPlant(null);
		if (this.plantType == Plant.AOBUFF)
			this.checkReBuffRange();

	}

	public void init() {
		fireCD = new LTimer(shotCdTime);
		if (this.plantType == Plant.DFATTACK) {
			this.fireable = false;
			this.rowCollisioned = true;
			this.plants.rangeAttackPlants.add(this);
			if (this.custom3 == Plant.DFATBOUNCE)
				this.bounce = true;
		}

		if (this.plantType == Plant.AOBUFF) {
			if (!this.onLeft) {
				this.field = this.fieldID - 45;
			} else {
				this.field = this.fieldID;
			}
			this.checkBuffRange();
			this.checkBuffTimer = new LTimer(Plant.checkBuffTime);
		}

		if (this.plantType == Plant.DEFENED) {
			this.bfn = 0;
			this.efn = custom1 - 1;
		}

		if (this.plantType == Plant.MINE) {
			Plant.plants.rangeAttackPlants.add(this);
		}
		if (this.plantType == Plant.BOMB) {
			this.fireable = false;
			this.passAble = 1;
			this.plants.rangeAttackPlants.add(this);
		}

		if (this.plantType == Plant.SPIKE) {
			this.passAble = 1;
			this.fireable = false;
			this.rowCollisioned = true;
			this.plants.rangeAttackPlants.add(this);
		}
		if (this.plantType == Plant.PARABOLA) {
			this.clickedAble = true;
			this.angle = 30;
		}
		if (this.plantType == Plant.SPIN) {
			if (!this.onLeft)
				this.angle = 180;
			this.clickedAble = true;
		}
		fn = animation.getTotalFrames();
		this.netName = Main.netName;
		imageTimer = new LTimer(imageTime);
		this.setAnimation(animation);
		if (this.cxr == 0)
			cx = x;
		if (this.cyr == 0)
			cy = y;
		if (this.cwr == 0)
			cwr = this.getWidth();
		if (this.chr == 0)
			chr = this.getHeight();

		if (this.onLeft) {
			x = x + pxr;
			y = y + pyr;
			nxr = cxr;

		} else {
			int w = this.getWidth();
			int h = this.getHeight();
			shadowX = w - sw - shadowX;
			nxr = w - cwr - cxr;
			// nyr = h - chr - cyr;
			x = x - pxr;
			y = y + pyr;
			cxr = w - cxr - cwr;
			xr = w - xr;

		}
		nyr = cyr - 2;

		if (this.plantType == Plant.BRAIN) {
			this.shadowX = 0;
			this.shadowY = 0;
			this.netName = "";
		}
		if (this.plantType == Plant.SPECIAL_CLOSE) {
			this.bfn = 0;
			this.efn = 0;
			this.passAble = 0;
			this.clickedAble = true;
			this.fireable = false;
			this.shadowX = 0;
			this.shadowY = 0;
			Plant.plants.rangeAttackPlants.add(this);
		}

		if (this.plantType == Plant.SPECIAL_RAY) {
			this.bfn = 0;
			this.efn = 0;
			this.clickedAble = true;
			this.shadowX = 0;
			this.shadowY = 0;
		}

	}

	public boolean isGc() {
		return gc;
	}

	public int getPlantType() {
		return plantType;
	}

	public boolean isFireable() {
		return fireable;
	}

	public int getPassAble() {
		return passAble;
	}

	public boolean isBounce() {
		return bounce;
	}

	public void setX(int x) {
		this.x = x;
	}

	public boolean isRowCollisioned() {
		return rowCollisioned;
	}

	public void setTemp(boolean temp) {
		this.temp = temp;
	}

	public void setRowCollisioned(boolean rowCollisioned) {
		this.rowCollisioned = rowCollisioned;
	}

	public int getObjectID() {
		return objectID;
	}

	public boolean isRobot() {
		return isRobot;
	}

	public void setRobot(boolean isRobot) {
		this.isRobot = isRobot;
	}

	public int getDa() {
		return da;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}

	public int getAngle() {
		return angle;
	}
	
	public void setTemp2(boolean temp2) {
		this.temp2 = temp2;
	}

	public void setTemp3(boolean temp3) {
		this.temp3 = temp3;
	}

	public void setTemp4(boolean temp4) {
		this.temp4 = temp4;
	}

	public int getCustom1() {
		return custom1;
	}

	public void setNetObject(boolean netObject) {
		NetObject = netObject;
	}

	public void setGc(boolean gc) {
		this.gc = gc;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getCustom2() {
		return custom2;
	}

	public int getCustom3() {
		return custom3;
	}

	public int getNetPlantID() {
		return netPlantID;
	}

	public boolean isClickedAble() {
		return clickedAble;
	}

	public int getMainID() {
		return mainID;
	}

	public void setTemp1(boolean temp1) {
		this.temp1 = temp1;
	}

	public void setClicked(boolean clicked) {
		this.clicked = clicked;
	}

	public void setMainID(int mainID) {
		this.mainID = mainID;
	}

	public boolean isOnLeft() {
		return onLeft;
	}

	public void setHit(boolean hit) {
		this.hit = hit;
	}

	public int getRowID() {
		return rowID;
	}

	public void setRowID(int rowID) {
		this.rowID = rowID;
	}

	public int getCustom4() {
		return custom4;
	}

	public boolean isTemp() {
		return temp;
	}

	public void setNetName(String netName) {
		this.netName = netName;
	}

	private void checkHarm(int zombieDa) {
		int c = zombieDa - this.df;
		if (c >= 0) {
			this.hp -= c;
		}
	}

	private void checkRowBombHarm(int plantDa) {
		this.hp -= plantDa;
	}

	// 处理僵尸的致命一击伤害
	private void checkHarm(int zombieDa, int probability) {
		int t = LSystem.getRandom(0, 100);
		if (t <= probability)
			zombieDa += zombieDa;
		int c = zombieDa - this.df;
		if (c >= 0) {
			this.hp -= c;
		}
	}

	// 处理无视防御的伤害
	private void checkHarm(int zombieDa, int numType, int num) {
		int c = zombieDa;
		// 如果是按数值算
		if (numType == Zombie.NUM) {
			if (this.df > num) {
				int df = this.df - num;
				c = c - df;
				if (c >= 0)
					this.hp -= c;
			} else {
				this.hp -= c;
			}
		} else {
			// 如果是按百分比算
			int df = this.df - this.df * num / 100;
			if (df < 0) {
				this.hp -= c;
			} else {
				c = c - df;
				if (c >= 0)
					this.hp -= c;
			}

		}

	}

	// 处理被僵尸攻击
	public void setAttacked(boolean attacked, int zombieID) {
		int[] info = Resource.getZombieInfo(zombieID);
		int zombieDa = info[6];
		int zombieType = info[1];
		int zcustom1 = info[16];
		int zcustom2 = info[17];
		int zcustom3 = info[18];
		if (zombieType == Zombie.SPECIAL) {
			if (zcustom1 == Zombie.SPECIAL_CHUANCI) {
				this.attacked = attacked;
				this.checkHarm(zombieDa, zcustom2, zcustom3);
				return;
			}
			if (zcustom1 == Zombie.SPECIAL_ZHIMING) {
				this.attacked = attacked;
				this.checkHarm(zombieDa, zcustom2);
				return;
			}
		}
		this.attacked = attacked;
		this.checkHarm(zombieDa);
	}

	// 处理被植物攻击
	public void setPlantAttacked(boolean attacked, int plantID) {
		int[] info = Resource.getPlantInfo(plantID);
		int plantType = info[1];
		int plantDa = info[6];
		this.attacked = attacked;
		if (plantType == Plant.ROWBOMB) {
			this.checkRowBombHarm(plantDa);
		} else {
			this.checkHarm(plantDa);
		}

	}

	private void drawHited(LGraphics g) {
		double d = g.getAlpha();
		g.setAlpha(0.1f);
		g.drawImage(animation.getSpriteImage().serializablelImage.getImage(),
				x, y);
		g.setAlpha(d);
	}

	private void drawNetName(LGraphics g) {
		g.setFont(l);
		g.setColor(Color.BLACK);
		g.drawString(this.netName, this.x + nxr, this.y + nyr);
	}

	public RectBox getRect() {
		return rect;
	}

	// 产生太阳花
	private void newSun() {
		if (this.custom1 == 1) {
			netDropID++;
			if (netDropID > 60000)
				netDropID = 0;
			Drop d = new Drop(Drop.FLOWER, netDropID, main);
			d.doUpSunNum(this.custom2);
		} else {
			Main.mySunNum += this.custom2;
		}

	}

	private void attackRange() {
		if (this.plantType == Plant.SPIKE) {
			this.rect.setBounds(x, y, this.getWidth(), this.getHeight());
			return;
		}
		if (this.plantType == Plant.SPECIAL_CLOSE) {
			this.rect.setBounds(x + cxr, y + cyr, this.getWidth() - cxr, this
					.getHeight());
			return;
		}

		if (this.plantType == Plant.DFATTACK) {
			if (custom1 == 0)
				custom1 = this.getWidth();
			if (custom2 == 0)
				custom2 = this.getHeight();
			this.rect.setBounds(x + cxr, y + cyr, custom1, custom2);
			return;
		}
		if (custom1 == 0)
			custom1 = this.getWidth();
		if (custom2 == 0)
			custom2 = this.getHeight();
		this.rect.setBounds(x, y, custom1, custom2);

	}

	private void checkBuffRange() {
		// 如果此草地在第一行
		if (this.field >= 1 && this.field <= 7) {
			doPlantBuff(this.field - 1, objectID);
			doPlantBuff(this.field + 1, objectID);
			doPlantBuff(this.field + 9, objectID);
			return;
		}

		// 如果此草地在第五行
		if (this.field >= 37 && this.field <= 43) {
			doPlantBuff(this.field - 1, objectID);
			doPlantBuff(this.field + 1, objectID);
			doPlantBuff(this.field - 9, objectID);
			return;
		}

		// 如果此草地在第一列
		if (this.field == 9 || this.field == 18 || this.field == 27) {
			doPlantBuff(this.field - 9, objectID);
			doPlantBuff(this.field + 1, objectID);
			doPlantBuff(this.field + 9, objectID);
			return;
		}

		// 如果此草地在第九列
		if (this.field == 17 || this.field == 26 || this.field == 35) {
			doPlantBuff(this.field - 9, objectID);
			doPlantBuff(this.field - 1, objectID);
			doPlantBuff(this.field + 9, objectID);
			return;
		}

		// 如果此草地在最左上顶点
		if (this.field == 0) {
			doPlantBuff(1, objectID);
			doPlantBuff(9, objectID);
			return;
		}
		// 如果此草地在最右上顶点
		if (this.field == 8) {
			doPlantBuff(7, objectID);
			doPlantBuff(17, objectID);
			return;
		}
		// 如果此草地在最左下顶点
		if (this.field == 36) {
			doPlantBuff(27, objectID);
			doPlantBuff(37, objectID);
			return;
		}

		// 如果此草地在最右下顶点
		if (this.field == 44) {
			doPlantBuff(35, objectID);
			doPlantBuff(43, objectID);
			return;
		}

		// 如果此草地在中间
		doPlantBuff(this.field - 9, objectID);
		doPlantBuff(this.field - 1, objectID);
		doPlantBuff(this.field + 9, objectID);
		doPlantBuff(this.field + 1, objectID);

	}

	private void checkReBuffRange() {

		// 如果此草地在第一行
		if (this.field >= 1 && this.field <= 7) {
			redoPlantBuff(this.field - 1, objectID);
			redoPlantBuff(this.field + 1, objectID);
			redoPlantBuff(this.field + 9, objectID);
			return;
		}

		// 如果此草地在第五行
		if (this.field >= 37 && this.field <= 43) {
			redoPlantBuff(this.field - 1, objectID);
			redoPlantBuff(this.field + 1, objectID);
			redoPlantBuff(this.field - 9, objectID);
			return;
		}

		// 如果此草地在第一列
		if (this.field == 9 || this.field == 18 || this.field == 27) {
			redoPlantBuff(this.field - 9, objectID);
			redoPlantBuff(this.field + 1, objectID);
			redoPlantBuff(this.field + 9, objectID);
			return;
		}

		// 如果此草地在第九列
		if (this.field == 17 || this.field == 26 || this.field == 35) {
			redoPlantBuff(this.field - 9, objectID);
			redoPlantBuff(this.field - 1, objectID);
			redoPlantBuff(this.field + 9, objectID);
			return;
		}

		// 如果此草地在最左上顶点
		if (this.field == 0) {
			redoPlantBuff(1, objectID);
			redoPlantBuff(9, objectID);
			return;
		}
		// 如果此草地在最右上顶点
		if (this.field == 8) {
			redoPlantBuff(7, objectID);
			redoPlantBuff(17, objectID);
			return;
		}
		// 如果此草地在最左下顶点
		if (this.field == 36) {
			redoPlantBuff(27, objectID);
			redoPlantBuff(37, objectID);
			return;
		}

		// 如果此草地在最右下顶点
		if (this.field == 44) {
			redoPlantBuff(35, objectID);
			redoPlantBuff(43, objectID);
			return;
		}

		// 如果此草地在中间
		redoPlantBuff(this.field - 9, objectID);
		redoPlantBuff(this.field - 1, objectID);
		redoPlantBuff(this.field + 9, objectID);
		redoPlantBuff(this.field + 1, objectID);

	}

	private void redoPlantBuff(int field, int objectID) {
		Plant p = null;
		if (!this.onLeft) {
			p = main.fields.getAllField().get(field + 45).getMyPlant();
		} else {
			p = main.fields.getAllField().get(field).getMyPlant();
		}
		if (null == p)
			return;
		int custom1 = Resource.getPlantInfo(objectID)[18];
		int custom2 = Resource.getPlantInfo(objectID)[19];
		switch (custom1) {
		case Plant.BUFFDA:
			p.redoBuffDa();
			break;
		case Plant.BUFFDF:
			p.redoBuffDf();
			break;

		case Plant.BUFFHP:
			p.redoBuffHp();
			break;
		case Plant.BUFFSP:
			p.redoBuffSp();
			break;

		}
	}

	private void doPlantBuff(int field, int objectID) {
		Plant p = null;
		if (!this.onLeft) {
			p = main.fields.getAllField().get(field + 45).getMyPlant();
		} else {
			p = main.fields.getAllField().get(field).getMyPlant();
		}
		if (null == p)
			return;
		int custom1 = Resource.getPlantInfo(objectID)[18];
		int custom2 = Resource.getPlantInfo(objectID)[19];
		switch (custom1) {
		case Plant.BUFFDA:
			p.doBuffDa(objectID, custom2);
			break;
		case Plant.BUFFDF:
			p.doBuffDf(objectID, custom2);
			break;
		case Plant.BUFFSP:
			p.doBuffSp(objectID, custom2);
			break;
		case Plant.BUFFHP:
			p.doBuffHp(objectID, custom2);
			break;

		}
	}

	public int getFieldID() {
		return fieldID;
	}

	private void doBuffDa(int objectID, int custom2) {
		if (!this.isBuffDa) {
			this.isBuffDa = true;
			this.tempBuffDa = this.da;
			this.da = this.da * custom2 / 100 + this.da;
			main.fields.getAllField().get(this.fieldID).drawBuff(objectID,
					Plant.BUFFDA);
		}
	}

	private void redoBuffDa() {
		this.isBuffDa = false;
		this.da = this.tempBuffDa;
		main.fields.getAllField().get(this.fieldID).reDrawBuff(Plant.BUFFDA);

	}

	private void doBuffDf(int objectID, int custom2) {
		if (!this.isBuffDf) {
			this.isBuffDf = true;
			this.tempBuffDf = this.df;
			this.df = this.df * custom2 / 100 + this.df;
			main.fields.getAllField().get(this.fieldID).drawBuff(objectID,
					Plant.BUFFDF);
		}
	}

	private void redoBuffDf() {
		this.isBuffDf = false;
		this.df = this.tempBuffDf;
		main.fields.getAllField().get(this.fieldID).reDrawBuff(Plant.BUFFDF);

	}

	private void doBuffSp(int objectID, int custom2) {
		if (this.shotCdTime == 0)
			return;
		if (!this.isBuffSp) {
			this.isBuffSp = true;
			this.tempBuffSp = this.shotCdTime;
			this.tempBuffImageTime = this.imageTime;
			// 按照shotspeed的比例调整image的速度
			this.shotCdTime = this.shotCdTime - custom2 * this.shotCdTime / 100;
			this.imageTime = this.imageTime - this.imageTime * custom2 / 100;
			this.imageTimer.setDelay(this.imageTime);
			this.fireCD.setDelay(this.shotCdTime);
			main.fields.getAllField().get(this.fieldID).drawBuff(objectID,
					Plant.BUFFSP);
		}
	}

	private void redoBuffSp() {
		this.isBuffSp = false;
		this.shotCdTime = this.tempBuffSp;
		this.imageTime = this.tempBuffImageTime;
		this.imageTimer.setDelay(this.imageTime);
		this.fireCD.setDelay(this.shotCdTime);
		main.fields.getAllField().get(this.fieldID).reDrawBuff(Plant.BUFFSP);

	}

	private void doBuffHp(int objectID, int custom2) {
		if (!this.isBuffHp) {
			this.tempBuffHp = custom2;
			this.isBuffHp = true;
			main.fields.getAllField().get(this.fieldID).drawBuff(objectID,
					Plant.BUFFHP);
		}
	}

	private void redoBuffHp() {
		this.isBuffHp = false;
		main.fields.getAllField().get(this.fieldID).reDrawBuff(Plant.BUFFHP);
	}

	private void buffUpHp() {

		if (this.hp >= this.maxHp) {
			this.hp = (int) this.maxHp;
		} else {
			this.hp += this.tempBuffHp;
		}

	}

	public void doClick() {
		if (Main.netID == this.mainID)
			this.clicked = true;
	}

	public void redoClick() {
		this.clicked = false;
	}

	public void upHp(int num) {
		if (this.hp >= this.maxHp) {
			this.hp = (int) this.maxHp;
		} else {
			this.hp += num;
		}
	}

	@Override
	public int getObjectType() {
		return this.objectType;
	}

}
