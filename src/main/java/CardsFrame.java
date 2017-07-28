
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.loon.framework.javase.game.core.graphics.LFont;
import org.loon.framework.javase.game.core.graphics.LImage;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;

public class CardsFrame {
	private Main main = null;
	static int[] cardRangeID = null; // 卡片框中卡片位置对应的卡片id
	private static int CARDSNUM = 10; // 组合卡片框中的卡片数量
	private static final int CARDY = 8; // 组合框中卡片的y坐标
	private static int CARD1PX = 78;// 第一个卡片放置的x坐标
	private static final int CARDWIDTH = 50;// 卡片的宽
	private static final int CARDHIGHT = 70;// 卡片的高
	private static final int CARDSUNNUMX = 12, CARDSUNNUMY = 65 + CARDY;// 画卡片的太阳数坐标偏移
	private static final int CARDSTARX = 10, CARDSTARY = 10;// 画卡片的级别数坐标偏移
	private static final int CARDUPX = 33, CARDUPY = 63;// 画卡片的升级标记坐标偏移
	private static int upTimer;
	private static final LImage star = Resource.getStar(); // 卡片级别图片
	private int cdPicHight = 0; // cd图片的高
	private float cdTime = 0; // 卡片的cd时间
	private float cd = 0; // 卡片已经经过的cd时间
	private boolean cdOK = false; // 卡片cd是否已经好了
	private boolean uptem = false; // 卡片升级动画的开关
	private LImage mouseImage = null;
	private int miWidth = 0; // 鼠标图片的宽
	private int miHight = 0; // 鼠标图片的高
	private int miX = 0; // 鼠标图片的X
	private int miY = 0; // 鼠标图片的Y
	private int temnum = 0;
	private int cardID;
	private int objectID;
	private int xr,yr ; //鼠标叠影图片的x,y偏移
	private StringBuffer  sbi = new StringBuffer("升级需要太阳：");
	private StringBuffer  sb = new StringBuffer();
	private static final String s = "升级需要太阳";
	private static final String s1 = "右击卡片升级";
	private static final LFont l = new LFont("GulimChe", 0, 11); 
	private static final LFont ls = new LFont("楷体_GB2312", LFont.STYLE_BOLD, 20);
	List<Card> allCards = new ArrayList<Card>();

	public CardsFrame(Main main) {
		this.main = main;
		CARD1PX += main.MCFX;
		initCardID();
	}

	// 画卡片的cd图像
	public void draw(LGraphics g) {
		//处理可升级红闪光
		this.upTimer++;
		if (this.upTimer == 20) {
			this.uptem = !this.uptem;
			upTimer = 0;
		}
		for (int i = 0; i < allCards.size(); i++) {
			Card temCard = allCards.get(i);
			this.cdTime = temCard.getCdTime();
			cd = temCard.getCd();
			temCard.jugdeUp();
			// 画卡片的静态图片
			g.drawImage(temCard.getCardImage(), CARD1PX + (i * 50), CARDY);

			// 画卡片所需的太阳数
			g.setFont(l);
			g.setColor(Color.BLACK);
			sb.delete(0, sb.length());
			sb.append(temCard.getSunNum());
			g.drawString(sb.toString(), CARD1PX + (i * 50) + CARDSUNNUMX,
					CARDSUNNUMY);
			// 画卡片可升级标记
			if (temCard.isUpAble() && this.uptem && temCard.isCdOK()) {
				g.drawImage(Resource.getUpStar(), CARD1PX + (i * 50) + CARDUPX,
						CARDUPY);
			}

			// 画卡片级别图案

			g.scale(0.5, 0.5);
			for (int j = 0; j < temCard.getLv(); j++) {
				g.drawImage(star, (CARD1PX + (i * 50) + j * CARDSTARX) * 2,
						CARDY + 2);
			}
			g.scale(2, 2);

			// 画拿起卡片时的卡片图案
			if (cdTime == cd && Main.mySunNum >= temCard.getSunNum()) {
				temCard.setCdOK(true);
				if (main.cfRange == i && main.isHoldCard) {
					main.holdCardRange = main.cfRange;
					double d = g.getAlpha();
					g.setAlpha(0.6F);
					g.rectFill(CARD1PX + (i * 50), CARDY, CARDWIDTH, CARDHIGHT,
							Color.BLACK);
					g.setAlpha(d);
				}

			} else {
				double d = g.getAlpha();
				g.setAlpha(0.3F);
				g.rectFill(CARD1PX + (i * 50), CARDY, CARDWIDTH, CARDHIGHT,
						Color.BLACK);
				g.setAlpha(0.5F);
				cdPicHight = (int) (CARDHIGHT * ((cdTime - cd) / cdTime));
				g.rectFill(CARD1PX + (i * 50), CARDY, CARDWIDTH, cdPicHight,
						Color.BLACK);
				g.setAlpha(d);
				if (cd < temCard.getCdTime())temCard.setCd(cd + 1);
				temCard.setCdOK(false);
			}
		}
	}

	public List<Card> getAllCards() {
		return allCards;
	}

	// 得到鼠标在卡片框中的移动的位置
	public void getRange(int x, int y) {

		if (y > CARDY && y < CARDY + 70) {

			if (x > 595 && x < 665) {
				main.cfRange = -2;
				return;
			}
			for (int i = 0; i < allCards.size(); i++) {
				temnum = CARD1PX + (i * 50);
				if (x > temnum && x < temnum + 50) {
					main.cfRange = i;
					return;
				}

			}
			main.cfRange = -1;
		} else {

			main.cfRange = -1;
		}
	}

	// 获得对应卡片的鼠标图片
	public void drawMousePic(int cfRange, int x, int y, LGraphics g) {
		if (cfRange != -2) {
			// 如果此卡片是怪物
			cardID = cardRangeID[cfRange];
			objectID=this.allCards.get(cfRange).getObjectID();
			if(objectID>300){
				int  animationID=Resource.getZombieInfo(objectID-300)[3];
				mouseImage = Resource.getZombieAnimation(animationID).getSpriteImage(0).getLImage();

			}else{

				int  animationID=Resource.getPlantInfo(objectID)[4];
				mouseImage =Resource.getPlantAnimation(animationID).getSpriteImage(0).getLImage();	
			}
		} else {
			// 画扫把的鼠标图片
			mouseImage = Resource.getScoop1();
		}

		miWidth = mouseImage.getWidth();
		miHight = mouseImage.getHeight();
		// 左边，右边画不同的镜像图像 ，怪物和植物的图像相反
		if (cfRange == -2) {
			miX = x - miWidth / 2;
			miY = y - miHight / 2;
			g.drawImage(mouseImage, miX, miY);
			return;
		}
		
		int[] i;
		if(objectID<=300 ){
			 i = Resource.getPlantInfo(objectID);
		}else{
			 i = Resource.getZombieInfo(objectID-300);
		}
			int xr=i[22];
			int yr;
			if(!Main.isLeft){
				if(xr!=0)xr= miWidth-i[22];	
			}
			yr=i[23];	
			if(xr==0){
				miX = x - miWidth / 2;
			}else{
				miX=x-xr;
			}
			if(yr==0){
				miY = y - miHight / 2;
			}else{
				miY=y-yr;
			}
				
		
		if (Main.isLeft) {
				g.drawImage(mouseImage, miX, miY);
		} else {
				g.drawMirrorImage(mouseImage, miX, miY);
			
		}

	}

	private void initCardID() {
		
		int[] i =main.choosedCard;
		this.cardRangeID = i;
		allCards.add(new Card(i[0]));
		allCards.add(new Card(i[1]));
		allCards.add(new Card(i[2]));
		allCards.add(new Card(i[3]));
		allCards.add(new Card(i[4]));
		allCards.add(new Card(i[5]));
		allCards.add(new Card(i[6]));
		allCards.add(new Card(i[7]));
		allCards.add(new Card(i[8]));
		allCards.add(new Card(i[9]));
	}

	public void drawScoop(LGraphics g) {
		g.drawImage(Resource.getScoopFrame(), 595, 0);
		if (main.holdCardRange != -2) {
			g.drawImage(Resource.getScoop1(), 590, -8);
		}

	}
  //画卡片升级时的魔法效果
   public void drawUpMagic(){
	   int i = Main.cfRange;
	  new Magic(601, (i * 50)+60,-10,main);

   }

   //画卡片的信息显示
   public void drawCardInfo(LGraphics g){
	   
	   if(Main.cfRange!=-1 && Main.cfRange!=-2){
		  Card c =  this.allCards.get(Main.cfRange);
		LImage image = c.getCardImage();
		g.setAlpha(0.5f);
		   g.fillRect(665, 0, 135, 200); 
		   g.setAlpha(1f);
		   Color t = g.getColor();
		   LFont f = g.getLFont();
		   g.drawImage(image, 670,0);
		   g.setColor(Color.RED);
		   g.drawString(s1, 720, 20);
		   g.drawString(s.toString(), 720, 40);
		   sbi.delete(0, sbi.length());
		   sbi.append(c.getUpNum());
		   g.setColor(Color.YELLOW);
		   g.drawString(sbi.toString(), 750, 60);
		  Resource.cutString(g, c.getInfomation(), 665, 80, 250,ls,Color.BLUE);
		  g.setColor(t);
		  g.setFont(f);
		  
	   }
	    
	   
   }
   
   
}
