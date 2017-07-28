import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.loon.framework.javase.game.GameScene;
import org.loon.framework.javase.game.action.sprite.ISprite;
import org.loon.framework.javase.game.action.sprite.Sprite;
import org.loon.framework.javase.game.core.LSystem;
import org.loon.framework.javase.game.core.graphics.Deploy;
import org.loon.framework.javase.game.core.graphics.LFont;
import org.loon.framework.javase.game.core.graphics.LImage;
import org.loon.framework.javase.game.core.graphics.Screen;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.core.graphics.window.LText;
import org.loon.framework.javase.game.core.timer.LTimer;

public class Main extends Screen implements Runnable {
	static int netID;
	static String IP;
	static int udpPort;
	static boolean action;
	static String netName;
	static int port;
	static int mySunNum = 100;// 太阳数
	static Bullets bullets = null;
	static Plants plants = null;
	static Zombies zombies = null;
	static NetMsgs netMsgs = null;
	static Robot robot =null;
	static NetClient nc = null;
	static Drop drop = null;
	static Fields fields = null;
	static boolean isLeft = true;
	static boolean isHoldCard = false;// 拿起卡片标记
	static boolean begin, chooseBegin; // 游戏是否正式开始,和是否选择完卡片的开关
	static int holdCardRange = -1; // 拿起卡片在卡片框中的id
	static int cfRange = -1; // 鼠标在移动到卡片在数组中的位置
	public static int mpx; // 背景图片的实际坐标
	static int sx, sy = 0; // 鼠标在屏幕中的坐标
	private static int mx = 0; // 鼠标在地图上的坐标
	private static int fieldID = -1; // 当前鼠标所在的土地块id
	static final int MCFX = 10, MCFY = 0;// 组合卡片框的坐标
	private static final int MOUSER = 2;// 鼠标向右卷轴
	private static final int MOUSEL = 1;// 鼠标向左卷轴
	private static final LFont l = new LFont("Arial Black", 0, 12); // 画主太阳数的字体
	private static final LFont chatFont = new LFont("楷体_GB2312", 0, 15); // 画主太阳数的字体
	private static final int MAPMOVEVALUE = 25;// 每次地图卷轴的距离
	private static LImage bgp = Resource.getBackGroundPic(); // 主背景图片
	private static LImage mcf = Resource.getMixCardFrame(); // 组合卡片框图片
	private static StringBuffer sunNum = new StringBuffer();
	CardsFrame cf = null;
	private boolean init = false;
	List<ObjectIn> allSprites = new ArrayList<ObjectIn>();
	boolean clickObject;// 判断是否点击了物体
	private StringBuffer sbi = new StringBuffer("a");
	private int chooseID; // 选择的卡片id
	int[] choosedCard = new int[10]; // 被选择的10个卡片
	private int chooseTimer = 200; // 选择卡片的时间为30秒
	private LText chatText = new LText("",20,500,700,20); //聊天框
	private boolean  openChatText ; //判断聊天框是否已经打开
	private List<ChatMsg> chatTextList= new ArrayList<ChatMsg>();
	private static int chatNetID=0;
	private int chatDeleteTimer; //处理聊天记录的消失
	static boolean hasRobot;
	private LTimer sunTimer = new LTimer(5000);
	private boolean  normalWindow; //此窗口是否为窗口化
	// 临时变量区域

	// 临时变量区域
	public Main() {
		new SetFrame(this);
		Resource.readInfo();
		this.updateFullScreen();
		plants = new Plants(this);
		bullets = new Bullets(this);
		zombies = new Zombies(this);
		netMsgs = new NetMsgs(this);
		nc = new NetClient(this);
		nc.setUdpPort(udpPort);
		nc.connect(IP, port);
		new Thread(this).start();
		chatText.setVisible(false);
		chatText.setBackground(Color.white);
		chatText.setAlpha(0.3f);
		this.add(chatText);
		
	}

	public static void main(String[] args) {
		GameScene frame = new GameScene("植物大战僵尸流星版", 800, 600);
		Deploy deploy = frame.getDeploy();
		deploy.setFPS(10);
		deploy.setScreen(new Main());
		deploy.setShowFPS(true);
		deploy.setLogo(false);
		deploy.mainLoop();
		frame.setCursor("image/cursors1.png");
		frame.setIconImage("image/icon.png");
		frame.showFrame();
		
	}

	@Override
	public void draw(LGraphics g) {
		if (begin) {
			if (init == false) {
				fields = new Fields(this);
				cf = new CardsFrame(this);
				if (!Main.isLeft)
					Main.mpx = -1500;
				init = true;
				if(this.hasRobot)this.robot = new Robot(this);
				this.initBrain();
			}
			g.drawImage(bgp, mpx, 0);
			g.drawImage(mcf, MCFX, MCFY);
			cf.drawScoop(g);
			cf.drawCardInfo(g);
			cf.draw(g);
			drawString(g);
			drawMousePic(g);
			this.sortSpites(g);
			Main.bullets.drawBullet(g);
			Drop.drawAllDrops(g);
			if(Main.hasRobot)robot.drawInfo(g);
		} else {
			if (!Main.chooseBegin) {
				g.drawImage(Resource.getMaingroundpic(), 0, 0);
				netMsgs.drawLoginString(g);
				SynchronizedMsg sm = new SynchronizedMsg(SynchronizedMsg.NETMSGACTION,Main.action,Main.isLeft,this);
				this.nc.send(sm);
			} else {
				g.drawImage(Resource.getChooseImage(), 0, 0);
				drawChooseCard(g);
			}
		}
		//画聊天的内容
		this.drawChatContent(g);
	}
	//画聊天的内容
	public void  drawChatContent(LGraphics g) {
		//处理聊天记录的消失
		this.chatDeleteTimer++;
		if(this.chatDeleteTimer>=150){
			if(this.chatTextList.size()!=0){
				this.chatTextList.remove(this.chatTextList.size()-1);
			}
				this.chatDeleteTimer=0;
		}
		
		//处理消息同步
		synchronized(ChatMsg.chatList){
			Iterator<ChatMsg> ite = ChatMsg.chatList.iterator();
			while (ite.hasNext()) {
				ChatMsg cm = (ChatMsg) ite.next();
				this.chatTextList.add(0,cm);
					ite.remove();
			}
		}
		
		int t= 0;
	LFont lf = g.getLFont();
	Color c = g.getColor();
	g.setColor(Color.BLACK);
	g.setFont(this.chatFont);	
		
	for(ChatMsg s : this.chatTextList){
		 t++;
		 String sa=s.getTextContent() ;
		 if(!sa.equals("")){
			 g.drawString(s.getNetName()+":"+sa,20,500-t*20);		
		 }
	}
	g.setFont(lf);
	g.setColor(c);
	
	}
	
	// 处理自动选择卡片
	private void autoChoose() {
		for (int i = 0; i < this.choosedCard.length; i++) {
			if (this.choosedCard[i] == 0) {
				for (int t = 1; t < this.choosedCard.length + 1; t++) {
					if (this.checkExistCard(t) == -1) {
						this.choosedCard[i] = t;
						break;
					}
				}
			}
		}

	}

	// 画选择卡片的卡片
	private void drawChooseCard(LGraphics g) {
		this.chooseTimer--;
		if (this.chooseTimer <= 0) {
			this.autoChoose();
			this.begin = true;
			this.setFPS(30);
		}
		LFont l = new LFont("GulimChe", 0, 11);
		LFont f = g.getLFont();
		Color c = g.getColor();
		g.setColor(Color.BLACK);
		g.setFont(l);
		g.drawImage(mcf, MCFX, MCFY);
		for (int j = 0; j < this.choosedCard.length; j++) {
			if (this.choosedCard[j] != 0) {
				int imageID = Resource.getCardInfo(this.choosedCard[j])[15];
				LImage image = Resource.getCardImage(imageID);
				g.drawImage(image, 85 + (j * 50), 8);
			}
		}
		boolean b = false;
		for (int i = 0; i < Resource.getCardInfo().size(); i++) {
			int[] info = Resource.getCardInfo(i + 1);
			int cardID = info[0];
			int imageID = info[15];
			int sunNum = info[6];
			sbi.delete(0, sbi.length());
			sbi.append(sunNum);
			LImage image = Resource.getCardImage(imageID);
			int t = i / 10;
			int w = i;
			if (9 * t != 0) {
				if (w > 9 * t)
					w -= 9 * t + 1 * t;
			}
			g.drawImage(image, 24 + (w * 52), 90 + (t * 79));
			g.drawString(sbi.toString(), 38 + (w * 52), 155 + (t * 79));
			if (this.checkExistCard(cardID) != -1) {
				double d = g.getAlpha();
				g.setAlpha(0.6F);
				g.rectFill(24 + (w * 52), 90 + (t * 79), 50, 70, Color.BLACK);
				g.setAlpha(d);
			}
			int checkID = getChooseCardID(24 + (w * 52), 90 + (t * 79));
			if (checkID != -1 && !b) {
				this.chooseID = cardID;
				b = true;
			}
		}
		if (!b)
			this.chooseID = -1;
		sbi.delete(0, sbi.length());
		sbi.append("请选择卡片，" + this.chooseTimer / 10 + "秒后游戏开始");
		LFont lf = new LFont("楷体_GB2312", LFont.STYLE_BOLD, 25);
		g.setFont(lf);
		g.setColor(Color.DARK_GRAY);
		g.drawString(sbi.toString(), 85, 580);
		g.setFont(f);
		g.setColor(c);
	}

	// 检测输入的卡片编号是否在以选中的卡片组中
	private int checkExistCard(int t) {
		int result = -1;
		for (int i = 0; i < this.choosedCard.length; i++) {
			if (this.choosedCard[i] == t) {
				result = i;
				break;
			}
		}
		return result;
	}

	private int getChooseCardID(int x, int y) {
		if (x < sx && sx < x + 50 && y < sy && sy < y + 70) {
			return 1;
		}
		return -1;
	}

	private void checkDropClick() {
		ListIterator<Drop> iter = Drop.allDrops.listIterator();
		while (iter.hasNext()) {
			Drop d = iter.next();
			if (this.onClick(d))
				d.beHited();
		}

	}

	@Override
	public void leftClick(MouseEvent e) {
		// 处理登陆界面时的单击判定
		if (!begin) {
			if (!this.chooseBegin) {
				//处理单击退出按钮
				if(sx>680 && sx<780 && sy>20 && sy<74){
					ClientExitMsg msg=	new ClientExitMsg(this);
					Main.nc.send(msg);
					LSystem.exit();
				}
				// 精灵图层为1时表示左键点击，为0时表示左键未点击
				netMsgs.sp.setLayer(1);
				netMsgs.sp2.setLayer(1);
				return;
			} else {
				if (this.chooseID != -1) {
					int minAbleID = -1;
					// 检测选中的卡片是否已经被选过
					int t = this.checkExistCard(this.chooseID);
					if (t != -1) {
						this.choosedCard[t] = 0;
						return;
					}
					// 检测空位的最小下标
					for (int i = 0; i < this.choosedCard.length; i++) {
						if (this.choosedCard[i] == 0) {
							minAbleID = i;
							break;
						}
					}
					if (minAbleID != -1)
						this.choosedCard[minAbleID] = this.chooseID;
				}

			}
			return;

		}

		this.checkDropClick();
		// 左键点击拿起卡片
		if (isHoldCard == false) {
			if (this.clickObject == true) {
				this.clickObject = false;
				fields.redoAllClick();
			} else if // 处理object的单击
			(this.fieldID != -1 && !this.clickObject) {
				fields.getAllField().get(fieldID).doClick();
			}
			// 处理plant的单击
			if (cfRange == -2) {
				isHoldCard = true;
				Main.holdCardRange = -2;
				return;
			}
			if (cfRange != -1) {
				if (cf.getAllCards().get(cfRange).isCdOK()) {
					isHoldCard = true;
				}
			}

		} else {
			locatePlant();

		}
	}

	@Override
	public void middleClick(MouseEvent arg0) {

	}

	@Override
	public void rightClick(MouseEvent arg0) {
		// 右键取消拿起
		if (isHoldCard == true) {
			isHoldCard = false;
			fieldID = -1;
			cfRange = -1;
			this.holdCardRange = -1;
		} else {
			// 升级卡片
			if (cfRange != -1 && cfRange != -2) {
				Card c = cf.getAllCards().get(cfRange);
				if (c.isUpAble() && c.getLv() < 4 && c.isCdOK()) {
					cf.drawUpMagic();
					c.up();
				}
			}
			isHoldCard = false;
			fieldID = -1;
			cfRange = -1;
			this.holdCardRange = -1;
		}
	}

	// 处理所有精灵的更新

	@Override
	public synchronized void mouseMoved(MouseEvent e) {

		sx = e.getX();
		sy = e.getY();
		runMouseMoved();
	}
	
	@Override
	public void update(long timer) {
		super.update(timer);
		zombies.doAllZombiesUpdate(timer);
		bullets.doAllBulletsUpdate(timer);
		bullets.addNetBullet();
		bullets.SynchronizedBulletMove();
		Magic.doAllMagicsUpdate(timer);
		plants.doAllPlantsUpdate(timer);
		plants.checkPlantRangeAttack();
		plants.addNetPlant();
		plants.plantSyn();
		bullets.checkHit();
		zombies.checkHit();
		zombies.SynchronizedZombie();
		zombies.addNetZombie();
		Drop.updateAllDrops(timer);
		Drop.addNetDrops();
		//处理机器人的刷新
		 
		if(init && this.hasRobot ){
			if(this.netID == 1 || this.netID ==0)this.robot.freshRobot(timer);	
		}
		if(init && this.hasRobot ){
			this.robot.recordTime();	
		}
		
		//处理聊天的光标定位
		if(this.openChatText){
			int length = chatText.getText().length();
			chatText.setCaretPosition(length);
		}
		
	if(this.sunTimer.action(timer))Main.mySunNum+=20;
	}



	public void mapMove(int d) {

		// 处理地图移动时的x 偏移值
		if (d == 2) {
			mpx -= MAPMOVEVALUE;
			plants.mapMove(-MAPMOVEVALUE);
			bullets.mapMove(-MAPMOVEVALUE);
			zombies.mapMove(-MAPMOVEVALUE);
			Drop.mapMoveDrops(-MAPMOVEVALUE);
			Magic.mapMoveMagics(-MAPMOVEVALUE);

		} else {
			mpx += MAPMOVEVALUE;
			plants.mapMove(MAPMOVEVALUE);
			bullets.mapMove(MAPMOVEVALUE);
			zombies.mapMove(MAPMOVEVALUE);
			Drop.mapMoveDrops(MAPMOVEVALUE);
			Magic.mapMoveMagics(MAPMOVEVALUE);
		}

	}

	// 画主界面中的字符串
	private void drawString(LGraphics g) {
		g.setColor(Color.BLACK);
		g.setFont(l);
		sunNum.delete(0, sunNum.length());
		sunNum.append(this.mySunNum);
		g.drawString(sunNum.toString(), 20, 74);

	}

	// 画选中卡片时的鼠标图像
	private void drawMousePic(LGraphics g) {
		if (isHoldCard == true) {
			cf.drawMousePic(this.cfRange, sx, sy, g);
			if (fieldID != -1 && cfRange != -2)
				fields.drawDImage(this.cfRange, fieldID, mpx, g);
		}

	}

	private void locatePlant() {
		// 如果此时是拿起卡片，且在鼠标在相应的草地上
		if (this.fieldID != -1) {
			Field f = fields.getAllField().get(fieldID);
			// 如果鼠标所在草地可以放下
			if (f.isHandleAble() && holdCardRange != -2 && holdCardRange != -1) {
				fields.getAllField().get(fieldID).locate(this.holdCardRange);
				cf.getAllCards().get(holdCardRange).setCd(0f);
				cf.getAllCards().get(holdCardRange).setCdOK(false);
				holdCardRange = -1;
				isHoldCard = false;
				cfRange = -1;
				fieldID = -1;
			} else {
				if (this.holdCardRange == -2) {
					f.removeObject();
					fields.getAllField().get(fieldID).reDrawAllBuff();
				}
				holdCardRange = -1;
				isHoldCard = false;
				cfRange = -1;
				fieldID = -1;
			}
		}
		holdCardRange = -1;
		isHoldCard = false;
		cfRange = -1;
		fieldID = -1;
	}

	@Override
	public void run() {
		while (true) {
			//处理新客户端的同步
			if(!begin){
				ClientNewMsg msg = new ClientNewMsg(this);
				nc.send(msg);	
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}


	private void runMouseMoved() {
		mx = sx - mpx;
		// 判断鼠标在卡片框或草地中的位置
		if (init) {
			if (Main.isHoldCard == false) {
				cf.getRange(sx, sy);
			}
		
		if (null != this.fields)
			fieldID = fields.getRange(mx, sy);

		// 鼠标卷轴
		if (sx > 790 && mpx <= 0 && mpx >= -1580) {
			mapMove(MOUSER);
			this.mouseMove(789, sy);
		}
		if (sx < 10 && mpx < 0) {
			mapMove(MOUSEL);
			this.mouseMove(9, sy);

		}
		}else{
			//处理移动到退出按钮上
			if(sx>680 && sx<780 && sy>20 && sy<74){
				Main.netMsgs.setQuitMoved(true);
			}else{
				Main.netMsgs.setQuitMoved(false);
			}
			
		}
	}

	@Override
	public boolean onClick(ISprite sprite) {
		return sprite.isVisible()
				&& (sprite.getCollisionBox().contains(mx, sy));
	}

	// 处理登陆界面中的检测
	public String loginOnClick(Sprite sprite) {
		boolean b = sprite.isVisible()
				&& (sprite.getCollisionBox().contains(mx, sy));
		// 通过设置sp和sp2的图层不同，来判断点中的是哪个sprite
		if (b && sprite.getLayer() == 1) {
			return sprite.getSpriteName();
		}
		return null;
	}

	private void sortSpites(LGraphics g) {

		this.allSprites.clear();
		this.allSprites.addAll(plants.allPlants);
		this.allSprites.addAll(zombies.allZombies);
		this.allSprites.addAll(Magic.allMagics);

		Collections.sort(this.allSprites, new Comparator<ObjectIn>() {
			@Override
			public int compare(ObjectIn s1, ObjectIn s2) {
				switch (s1.getObjectType()) {
				case ObjectIn.PLANT:
					s1 = (Plant) s1;
					break;
				case ObjectIn.BULLET:
					s1 = (Bullet) s1;
					break;
				case ObjectIn.MAGIC:
					s1 = (Magic) s1;
					break;
				}
				switch (s2.getObjectType()) {
				case ObjectIn.PLANT:
					s2 = (Plant) s2;
					break;
				case ObjectIn.BULLET:
					s2 = (Bullet) s2;
					break;
				case ObjectIn.MAGIC:
					s2 = (Magic) s2;
					break;
				}

				if (s1.getRowID() < s2.getRowID()) {
					return -1;
				} else if (s1.getRowID() >= s2.getRowID()) {
					return 1;
				} else {
					return compare(s1.getLayer(), s2.getLayer());
				}

			}

			private int compare(int s1, int s2) {
				if (s1 < s2) {
					return -1;
				} else if (s1 > s2) {
					return 1;
				} else {
					return 0;
				}
			}

		});
		for (int i = 0; i < this.allSprites.size(); i++) {
			Sprite s = (Sprite) this.allSprites.get(i);
			s.createUI(g);

		}
   
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == 27){
			if(this.normalWindow){
				this.updateFullScreen();
				this.normalWindow=false;
			}else{
				this.updateNormalScreen();
				this.normalWindow=true;
			}
		}
		
		//如果按下删除键
		if(e.getKeyCode() == 8){
			if(this.openChatText){
			this.chatText.delete(this.chatText.getText().length()-1);
			}
		}
		
		//如果按下回车
		if(e.getKeyCode() == 10){
			if(!this.openChatText){
				chatText.setEnabled(true);
				chatText.setEditable(true);
				chatText.setVisible(true);	
				chatText.setFocusable(true);
				chatText.requestFocus();
				this.openChatText=true;
			}else{
				chatText.setVisible(false);
				this.openChatText=false;
				if(!chatText.getText().equals("")){
					ChatMsg cm = new ChatMsg(this.netID,chatNetID++,this.netName,chatText.getText());
					this.nc.send(cm);
					this.chatTextList.add(0,cm);
				}
				chatText.setText("");
			}
			
		}
	

	}

	public List<ChatMsg> getChatTextList() {
		return chatTextList;
	}
	
	
	private void initBrain(){
		Plant p1 =new Plant(136,true,10+mpx,100,plants);
		p1.setRowID(1);
		Plant p2 =new Plant(136,true,10+mpx,200,plants);
		p2.setRowID(2);
		Plant p3 =new Plant(136,true,10+mpx,300,plants);
		p3.setRowID(3);
		Plant p4 =new Plant(136,true,10+mpx,400,plants);
		p4.setRowID(4);
		Plant p5 =new Plant(136,true,10+mpx,500,plants);
		p5.setRowID(5);
		
		Plant p6 =new Plant(136,false,2350+mpx,100,plants);
		p6.setRowID(1);
		Plant p7 =new Plant(136,false,2350+mpx,200,plants);
		p7.setRowID(2);
		Plant p8 =new Plant(136,false,2350+mpx,300,plants);
		p8.setRowID(3);
		Plant p9 =new Plant(136,false,2350+mpx,400,plants);
		p9.setRowID(4);
		Plant p10 =new Plant(136,false,2350+mpx,500,plants);
		p10.setRowID(5);
	}
	
	public  void doGameOver(boolean isLeft, LGraphics g) {
			if(isLeft == this.isLeft){
				LImage lost = 	Resource.getLost() ;
				g.drawImage(lost, 100,100);
			}else{
				LImage win = 	Resource.getWin() ;
				g.drawImage(win, 100,100);
				
			}
		
	}
	
}
