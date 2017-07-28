import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.loon.framework.javase.game.action.sprite.Animation;
import org.loon.framework.javase.game.action.sprite.GIFAnimation;
import org.loon.framework.javase.game.core.graphics.LFont;
import org.loon.framework.javase.game.core.graphics.LImage;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;

public class Resource {
	private static final LImage backgroundpic;
	private static final LImage maingroundpic;// 登陆页背景图片
	private static final LImage star; // 卡片级别图片
	private static final LImage upStar; // 卡片可升级图片
	private static final LImage scoopFrame; // 卡片可升级图片
	private static final LImage actionFlag;
	private static final LImage scoop1;
	private static final LImage chooseImage; 
	private static final LImage quit1;
	private static final LImage quit2;
	private static final LImage lost = new LImage("image/lost.png");
	private static final LImage win = new LImage("image/win.png");
	private static final LImage backgroundpic1;
	private static final LImage shadow; // 阴影图片
	private static final LImage stagePic = new LImage("image/stagePic.png");
	private static final LImage timePic = new LImage("image/timePic.png");
	private static String dropImage = "image/drop.gif"; // 太阳图片
	private static int bulletAnimationNum = 39;
	private static int plantAnimationNum = 81;
	private static int zombieAnimationNum = 5;
	private static int magicAnimationNum = 11;
	private static int cardNum = 82;
	private static Animation[] bulletAnimation = new Animation[bulletAnimationNum];
	private static Animation[] plantAnimation = new Animation[plantAnimationNum];
	private static Animation[] zombieAnimation = new Animation[zombieAnimationNum];
	private static Animation[] magicAnimation = new Animation[magicAnimationNum];
	private static LImage[] allCardImage = new LImage[cardNum];
	private static String[] cardInfomation = new String[400];
	private static List<int[]> cardInfo = new ArrayList<int[]>();
	private static List<int[]> plantInfo = new ArrayList<int[]>();
	private static List<int[]> zombieInfo = new ArrayList<int[]>();
	private static final int cardIntNum = 26; // 每个卡片信息数组的大小
	private static final int plantIntNum = 50; // 每个植物信息数组的大小
	private static final int zombieIntNum = 58; // 每个僵尸信息数组的大小

	public static String getDropImage() {
		return dropImage;
	}

	static {
		initCardInfomation();
		star = new LImage("image/star.png");
		upStar = new LImage("image/upstar.gif");
		scoopFrame = new LImage("image/scoopframe.png");
		scoop1 = new LImage("image/scoop1.png");
		shadow = new LImage("image/shadow.png");
		backgroundpic1 = new LImage("image/background2.jpg");
		quit1 = new LImage("image/quit1.png");
		quit2 = new LImage("image/quit2.png");
		maingroundpic = new LImage("image/mainbackground.jpg");
		backgroundpic = new LImage("image/background.jpg");
		actionFlag = new LImage("image/actionFlag.png");
		chooseImage = new LImage("image/chooseImage.jpg");
		for (int i = 0; i < magicAnimationNum; i++) {
			if (i == 4 || i == 5 || i == 6 || i == 7) {
				magicAnimation[i] = Animation.getDefaultAnimation(
						"image\\magic" + (i + 1) + ".png", 136, 95, 10);

			} else {
				magicAnimation[i] = new GIFAnimation("image/magic" + (i + 1)
						+ ".gif").getAnimation();
			}

		}
	}

	public static LImage getCardImage(int cardID) {
		if (null == Resource.allCardImage[cardID - 1]) {
			Resource.allCardImage[cardID - 1] = new LImage("image/card"
					+ cardID + ".png");
		}
		return Resource.allCardImage[cardID - 1];

	}

	private static Animation arrowAnimation = Animation.getDefaultAnimation(
			"image\\arrow.png", 19, 80, 10);

	public static Animation getArrowAnimation() {
		return arrowAnimation;
	}

	public static LImage getShadow() {
		return shadow;
	}

	Resource() {
	}



	public static LImage getActionFlag() {
		return actionFlag;
	}

	public static LImage getQuit1() {
		return quit1;
	}
	
	public static LImage getQuit2() {
		return quit2;
	}
	
	public static LImage getStagePic() {
		return stagePic;
	}

	public static LImage getTimePic() {
		return timePic;
	}

	public static String[] getCardInfomation() {
		return cardInfomation;
	}

	public static LImage getChooseImage() {
		return chooseImage;
	}

	public static LImage getBackGroundPic() {
		return backgroundpic;
	}
	
	public static LImage getScoopFrame() {
		return scoopFrame;
	}

	public static LImage getStar() {
		return star;
	}

	public static LImage getMaingroundpic() {
		return maingroundpic;
	}

	public static LImage getScoop1() {
		return scoop1;
	}

	public static LImage getMixCardFrame() {
		return backgroundpic1;
	}

	public static LImage getUpStar() {
		return upStar;
	}

	public static int[] getCardInfo(int cardID) {
		int[] i = Resource.cardInfo.get(cardID - 1);
		return i;
	}

	public static Animation getPlantAnimation(int plantID) {
		if (null == plantAnimation[plantID - 1]) {
			plantAnimation[plantID - 1] = new GIFAnimation("image/plant"
					+ plantID + ".gif").getAnimation();
		}
		return (Animation) plantAnimation[plantID - 1].clone();
	}

	public static Animation getBulletAnimation(int bulletID) {
		if (null == bulletAnimation[bulletID - 1]) {
			bulletAnimation[bulletID - 1] = new GIFAnimation("image/b"
					+ bulletID + ".gif").getAnimation();
		}
		return (Animation) bulletAnimation[bulletID - 1].clone();
	}

	public static Animation getZombieAnimation(int zombieID) {
		if (null == zombieAnimation[zombieID - 1]) {
			zombieAnimation[zombieID - 1] = new GIFAnimation("image/zombie"
					+ zombieID + ".gif").getAnimation();
		}
		return (Animation) zombieAnimation[zombieID - 1].clone();
	}

	public static Animation getMagicAnimation(int magicID) {
		return (Animation) magicAnimation[magicID - 1].clone();

	}

	public static int[] getPlantInfo(int plantID) {
		return Resource.plantInfo.get(plantID - 1);
	}

	public static int[] getZombieInfo(int zombieID) {
		return Resource.zombieInfo.get(zombieID - 1);
	}

	public static LImage getLost() {
		return lost;
	}

	public static LImage getWin() {
		return win;
	}

	public static void readInfo() {
		// 处理卡片信息
		BufferedReader reader = null;
		int[] info = new int[cardIntNum];
		int i = 0;
		try {
			reader = new BufferedReader(new InputStreamReader(Resource.class
					.getResourceAsStream("config/cardInfo.txt")));
			String line = reader.readLine(); // 读入一行
			while (line != null) { // 如果没读完，继续
				// 用","拆分
				StringTokenizer st = new StringTokenizer(line, ",");
				while (st.hasMoreTokens()) {

					int x = Integer.parseInt(st.nextToken());
					info[i] = x;
					i++;
					// System.out.println("i:"+i+",x:"+x);

				}
				Resource.cardInfo.add(info.clone());
				i = 0;
				line = reader.readLine(); // 读入下一入
			}
		} catch (java.io.IOException ioe) {
			// 出错处理
		} finally { // 关闭文件

			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 处理植物信息
		reader = null;
		info = new int[plantIntNum];
		i = 0;
		try {
			reader = new BufferedReader(new InputStreamReader(Resource.class
					.getResourceAsStream("config/plantInfo.txt")));
			String line = reader.readLine(); // 读入一行
			while (line != null) { // 如果没读完，继续
				// 用","拆分
				StringTokenizer st = new StringTokenizer(line, ",");
				while (st.hasMoreTokens()) {

					int x = Integer.parseInt(st.nextToken());
					info[i] = x;
					i++;

				}
				Resource.plantInfo.add(info.clone());
				i = 0;
				line = reader.readLine(); // 读入下一入
			}
		} catch (java.io.IOException ioe) {
			// 出错处理
		} finally { // 关闭文件

			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 处理僵尸信息

		reader = null;
		info = new int[zombieIntNum];
		i = 0;
		try {
			reader = new BufferedReader(new InputStreamReader(Resource.class
					.getResourceAsStream("config/zombieInfo.txt")));
			String line = reader.readLine(); // 读入一行
			while (line != null) { // 如果没读完，继续
				// 用","拆分
				StringTokenizer st = new StringTokenizer(line, ",");
				while (st.hasMoreTokens()) {
					int x = Integer.parseInt(st.nextToken());
					info[i] = x;
					i++;

				}
				Resource.zombieInfo.add(info.clone());
				i = 0;
				line = reader.readLine(); // 读入下一入
			}
		} catch (java.io.IOException ioe) {
			// 出错处理
		} finally { // 关闭文件

			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static List<int[]> getCardInfo() {
		return cardInfo;
	}

	/**
	 * @功能 中文乱码的处理
	 * @参数列表: srcString String
	 * @return String 返回一个String对象
	 */
	public static String getChineseString(String srcString) {
		byte[] b = new byte[srcString.length()];
		try {
			b = srcString.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return (new String(b));
	}

	/**
	 * 将源字符串自动按指定宽度换行显示
	 * 
	 * @param g
	 *            画笔
	 * @param sourceString
	 *            源字符串
	 * @param viewX
	 *            显示的x坐标
	 * @param viewY
	 *            显示的y坐标
	 * @param width
	 *            指定的显示宽度
	 * @param font
	 *            字体
	 * @param color
	 *            字体颜色
	 */
	public static void cutString(LGraphics g, String sourceString, int viewX,
			int viewY, int width, LFont font, Color c) {
		int totalNum = sourceString.length();// 字符总数
		int preWorldMaxWidth = font.charWidth('国');// 单个字符最大宽度

		int worldHeight = font.getHeight();// 字符的高度

		int preLineMinNum = width / preWorldMaxWidth;// 每行最少字符数

		int remanentNum = totalNum;// 剩余的字符数

		int curLineNum;// 当前行真正显示的字符数

		int row = 0;// 显示的行数
		g.setColor(c);
		while (true) {
			if (remanentNum <= 0)
				break;
			if (remanentNum >= preLineMinNum) {
				curLineNum = preLineMinNum;
				while (font.subStringWidth(sourceString,
						totalNum - remanentNum, curLineNum) <= width
						- preWorldMaxWidth) {
					if (curLineNum >= remanentNum)
						break;
					curLineNum++;
				}
				g.drawSubString(sourceString, totalNum - remanentNum,
						curLineNum, viewX, viewY + (row * worldHeight), 0);
				remanentNum -= curLineNum;
				row++;
			} else {
				g.drawSubString(sourceString, totalNum - remanentNum,
						remanentNum, viewX, viewY + (row * worldHeight), 0);
				remanentNum = 0;
			}
		}
	}

	// 初始化卡片信息内容
	public static void initCardInfomation() {
		cardInfomation[0] = "一个各方面发展都比较平均的植物，比较适合前期使用，升级后各方面均能得到提升";
		cardInfomation[1] = "一个各方面发展都比较平均的植物，比较适合前期使用，升级后各方面均能得到提升";
		cardInfomation[2] = "一个各方面发展都比较平均的植物，比较适合前期使用，升级后各方面均能得到提升";
		cardInfomation[3] = "一个各方面发展都比较平均的植物，比较适合前期使用，升级后各方面均能得到提升";
		cardInfomation[4] = "一个各方面发展都比较平均的植物，比较适合前期使用，升级后各方面均能得到提升";
		cardInfomation[5] = "拥有较高防御能力的攻击型植物，但是攻击力较低，非常适合前期使用，升级后防御力将得到进一步提升";
		cardInfomation[6] = "拥有较高防御能力的攻击型植物，但是攻击力较低，非常适合前期使用，升级后防御力将得到进一步提升";
		cardInfomation[7] = "拥有较高防御能力的攻击型植物，但是攻击力较低，非常适合前期使用，升级后防御力将得到进一步提升";
		cardInfomation[8] = "拥有较高防御能力的攻击型植物，但是攻击力较低，非常适合前期使用，升级后防御力将得到进一步提升";
		cardInfomation[9] = "拥有较高防御能力的攻击型植物，但是攻击力较低，非常适合前期使用，升级后防御力将得到进一步提升";
		cardInfomation[10] = "各方面都很不错的植物，可以充当战斗中的主力，升级后各方面能力均可得到提升";
		cardInfomation[11] = "各方面都很不错的植物，可以充当战斗中的主力，升级后各方面能力均可得到提升";
		cardInfomation[12] = "各方面都很不错的植物，可以充当战斗中的主力，升级后各方面能力均可得到提升";
		cardInfomation[13] = "各方面都很不错的植物，可以充当战斗中的主力，升级后各方面能力均可得到提升";
		cardInfomation[14] = "各方面都很不错的植物，可以充当战斗中的主力，升级后各方面能力均可得到提升";
		cardInfomation[15] = "拥有极快攻击速度的植物，升级后将进一步提升攻击速度";
		cardInfomation[16] = "拥有极快攻击速度的植物，升级后将进一步提升攻击速度";
		cardInfomation[17] = "拥有极快攻击速度的植物，升级后将进一步提升攻击速度";
		cardInfomation[18] = "拥有极快攻击速度的植物，升级后将进一步提升攻击速度";
		cardInfomation[19] = "拥有极快攻击速度的植物，升级后将进一步提升攻击速度";
		cardInfomation[20] = "具有发射多颗子弹能力的植物，升级后可以增加发射子弹的数量";
		cardInfomation[21] = "具有发射多颗子弹能力的植物，升级后可以增加发射子弹的数量";
		cardInfomation[22] = "具有发射多颗子弹能力的植物，升级后可以增加发射子弹的数量";
		cardInfomation[23] = "具有发射多颗子弹能力的植物，升级后可以增加发射子弹的数量";
		cardInfomation[24] = "具有发射多颗子弹能力的植物，升级后可以增加发射子弹的数量";
		cardInfomation[25] = "小型防御型植物，升级后将提升防御力和生命值";
		cardInfomation[26] = "小型防御型植物，升级后将提升防御力和生命值";
		cardInfomation[27] = "小型防御型植物，升级后将提升防御力和生命值";
		cardInfomation[28] = "小型防御型植物，升级后将提升防御力和生命值";
		cardInfomation[29] = "小型防御型植物，升级后将提升防御力和生命值";
		cardInfomation[30] = "大型防御型植物，升级后将大幅提升防御力和生命值";
		cardInfomation[31] = "大型防御型植物，升级后将大幅提升防御力和生命值";
		cardInfomation[32] = "大型防御型植物，升级后将大幅提升防御力和生命值";
		cardInfomation[33] = "大型防御型植物，升级后将大幅提升防御力和生命值";
		cardInfomation[34] = "大型防御型植物，升级后将大幅提升防御力和生命值";
		cardInfomation[35] = "拥有增加周围四格植物攻击力能力的植物，升级后将进一步提高增加的攻击力";
		cardInfomation[36] = "拥有增加周围四格植物攻击力能力的植物，升级后将进一步提高增加的攻击力";
		cardInfomation[37] = "拥有增加周围四格植物攻击力能力的植物，升级后将进一步提高增加的攻击力";
		cardInfomation[38] = "拥有增加周围四格植物攻击力能力的植物，升级后将进一步提高增加的攻击力";
		cardInfomation[39] = "拥有增加周围四格植物攻击力能力的植物，升级后将进一步提高增加的攻击力";
		cardInfomation[40] = "拥有增加周围四格植物防御力能力的植物，升级后将进一步提高增加的防御力";
		cardInfomation[41] = "拥有增加周围四格植物防御力能力的植物，升级后将进一步提高增加的防御力";
		cardInfomation[42] = "拥有增加周围四格植物防御力能力的植物，升级后将进一步提高增加的防御力";
		cardInfomation[43] = "拥有增加周围四格植物防御力能力的植物，升级后将进一步提高增加的防御力";
		cardInfomation[44] = "拥有增加周围四格植物防御力能力的植物，升级后将进一步提高增加的防御力";
		cardInfomation[45] = "拥有增加周围四格植物攻击速度的能力的植物，升级后将进一步提高增加的攻击速度";
		cardInfomation[46] = "拥有增加周围四格植物攻击速度的能力的植物，升级后将进一步提高增加的攻击速度";
		cardInfomation[47] = "拥有增加周围四格植物攻击速度的能力的植物，升级后将进一步提高增加的攻击速度";
		cardInfomation[48] = "拥有增加周围四格植物攻击速度的能力的植物，升级后将进一步提高增加的攻击速度";
		cardInfomation[49] = "拥有增加周围四格植物攻击速度的能力的植物，升级后将进一步提高增加的攻击速度";
		cardInfomation[50] = "每隔一段时间就能增加周围四格植物一定的生命值，升级后将进一步提高增加的生命值";
		cardInfomation[51] = "每隔一段时间就能增加周围四格植物一定的生命值，升级后将进一步提高增加的生命值";
		cardInfomation[52] = "每隔一段时间就能增加周围四格植物一定的生命值，升级后将进一步提高增加的生命值";
		cardInfomation[53] = "每隔一段时间就能增加周围四格植物一定的生命值，升级后将进一步提高增加的生命值";
		cardInfomation[54] = "每隔一段时间就能增加周围四格植物一定的生命值，升级后将进一步提高增加的生命值";
		cardInfomation[55] = "能够进行抛物线攻击的植物，左键点击植物能够调整植物的发射角度，升级后将大幅提高火力值";
		cardInfomation[56] = "能够进行抛物线攻击的植物，左键点击植物能够调整植物的发射角度，升级后将大幅提高火力值";
		cardInfomation[57] = "能够进行抛物线攻击的植物，左键点击植物能够调整植物的发射角度，升级后将大幅提高火力值";
		cardInfomation[58] = "能够进行抛物线攻击的植物，左键点击植物能够调整植物的发射角度，升级后将大幅提高火力值";
		cardInfomation[59] = "能够进行抛物线攻击的植物，左键点击植物能够调整植物的发射角度，升级后将大幅提高火力值";
		cardInfomation[60] = "能够对一条直线上的植物进行攻击，升级后将提高攻击范围和攻击力";
		cardInfomation[61] = "能够对一条直线上的植物进行攻击，升级后将提高攻击范围和攻击力";
		cardInfomation[62] = "能够对一条直线上的植物进行攻击，升级后将提高攻击范围和攻击力";
		cardInfomation[63] = "能够对一条直线上的植物进行攻击，升级后将提高攻击范围和攻击力";
		cardInfomation[64] = "能够对一条直线上的植物进行攻击，升级后将提高攻击范围和攻击力";
		cardInfomation[65] = "一种防守反击型的植物，能对来犯的敌人实施有效的打击，每次打击，有一定比率的吸血效果，升级后将提高吸血的比率";
		cardInfomation[66] = "一种防守反击型的植物，能对来犯的敌人实施有效的打击，每次打击，有一定比率的吸血效果，升级后将提高吸血的比率";
		cardInfomation[67] = "一种防守反击型的植物，能对来犯的敌人实施有效的打击，每次打击，有一定比率的吸血效果，升级后将提高吸血的比率";
		cardInfomation[68] = "一种防守反击型的植物，能对来犯的敌人实施有效的打击，每次打击，有一定比率的吸血效果，升级后将提高吸血的比率";
		cardInfomation[69] = "一种防守反击型的植物，能对来犯的敌人实施有效的打击，每次打击，有一定比率的吸血效果，升级后将提高吸血的比率";
		cardInfomation[70] = "一种防守反击型的植物，能对来犯的敌人实施打击，每次受到僵尸的攻击会反弹一定比率的伤害，升级后将提高反弹的比率";
		cardInfomation[71] = "一种防守反击型的植物，能对来犯的敌人实施打击，每次受到僵尸的攻击会反弹一定比率的伤害，升级后将提高反弹的比率";
		cardInfomation[72] = "一种防守反击型的植物，能对来犯的敌人实施打击，每次受到僵尸的攻击会反弹一定比率的伤害，升级后将提高反弹的比率";
		cardInfomation[73] = "一种防守反击型的植物，能对来犯的敌人实施打击，每次受到僵尸的攻击会反弹一定比率的伤害，升级后将提高反弹的比率";
		cardInfomation[74] = "一种防守反击型的植物，能对来犯的敌人实施打击，每次受到僵尸的攻击会反弹一定比率的伤害，升级后将提高反弹的比率";
		cardInfomation[75] = "每隔一段时间能掉落一个可以采集的太阳，升级后可以提高采集的太阳数";
		cardInfomation[76] = "每隔一段时间能掉落一个可以采集的太阳，升级后可以提高采集的太阳数";
		cardInfomation[77] = "每隔一段时间能掉落一个可以采集的太阳，升级后可以提高采集的太阳数";
		cardInfomation[78] = "每隔一段时间能掉落一个可以采集的太阳，升级后可以提高采集的太阳数";
		cardInfomation[79] = "每隔一段时间能掉落一个可以采集的太阳，升级后可以提高采集的太阳数";
		cardInfomation[80] = "每隔一段时间自动得到一定数量的太阳，升级后可以提高得到的太阳数";
		cardInfomation[81] = "每隔一段时间自动得到一定数量的太阳，升级后可以提高得到的太阳数";
		cardInfomation[82] = "每隔一段时间自动得到一定数量的太阳，升级后可以提高得到的太阳数";
		cardInfomation[83] = "每隔一段时间自动得到一定数量的太阳，升级后可以提高得到的太阳数";
		cardInfomation[84] = "每隔一段时间自动得到一定数量的太阳，升级后可以提高得到的太阳数";
		cardInfomation[85] = "能使受到攻击的僵尸减缓移动速度，和一定的攻击力，升级后可以进一步提高特殊效果的能力";
		cardInfomation[86] = "能使受到攻击的僵尸减缓移动速度，和一定的攻击力，升级后可以进一步提高特殊效果的能力";
		cardInfomation[87] = "能使受到攻击的僵尸减缓移动速度，和一定的攻击力，升级后可以进一步提高特殊效果的能力";
		cardInfomation[88] = "能使受到攻击的僵尸减缓移动速度，和一定的攻击力，升级后可以进一步提高特殊效果的能力";
		cardInfomation[89] = "能使受到攻击的僵尸减缓移动速度，和一定的攻击力，升级后可以进一步提高特殊效果的能力";
		cardInfomation[90] = "能使受到攻击的僵尸减缓攻击速度，和一定的防御力，升级后可以进一步提高特殊效果的能力";
		cardInfomation[91] = "能使受到攻击的僵尸减缓攻击速度，和一定的防御力，升级后可以进一步提高特殊效果的能力";
		cardInfomation[92] = "能使受到攻击的僵尸减缓攻击速度，和一定的防御力，升级后可以进一步提高特殊效果的能力";
		cardInfomation[93] = "能使受到攻击的僵尸减缓攻击速度，和一定的防御力，升级后可以进一步提高特殊效果的能力";
		cardInfomation[94] = "能使受到攻击的僵尸减缓攻击速度，和一定的防御力，升级后可以进一步提高特殊效果的能力";
		cardInfomation[95] = "一个具有全方位攻击能力的植物，升级后将进一步提高攻击，防御能力";
		cardInfomation[96] = "一个具有全方位攻击能力的植物，升级后将进一步提高攻击，防御能力";
		cardInfomation[97] = "一个具有全方位攻击能力的植物，升级后将进一步提高攻击，防御能力";
		cardInfomation[98] = "一个具有全方位攻击能力的植物，升级后将进一步提高攻击，防御能力";
		cardInfomation[99] = "一个具有全方位攻击能力的植物，升级后将进一步提高攻击，防御能力";
		cardInfomation[100] = "一个具有极短卡片重置时间植物，升级后将进一步减少卡片重置时间";
		cardInfomation[101] = "一个具有极短卡片重置时间植物，升级后将进一步减少卡片重置时间";
		cardInfomation[102] = "一个具有极短卡片重置时间植物，升级后将进一步减少卡片重置时间";
		cardInfomation[103] = "一个具有极短卡片重置时间植物，升级后将进一步减少卡片重置时间";
		cardInfomation[104] = "一个具有极短卡片重置时间植物，升级后将进一步减少卡片重置时间";
		cardInfomation[105] = "拥有极高攻击力的植物，但是生命值较少，防御力也较低，升级后将进一步提高攻击力";
		cardInfomation[106] = "拥有极高攻击力的植物，但是生命值较少，防御力也较低，升级后将进一步提高攻击力";
		cardInfomation[107] = "拥有极高攻击力的植物，但是生命值较少，防御力也较低，升级后将进一步提高攻击力";
		cardInfomation[108] = "拥有极高攻击力的植物，但是生命值较少，防御力也较低，升级后将进一步提高攻击力";
		cardInfomation[109] = "拥有极高攻击力的植物，但是生命值较少，防御力也较低，升级后将进一步提高攻击力";
		cardInfomation[110] = "能对一定范围内的敌人，瞬间造成巨大的伤害，升级后将进一步提高伤害力";
		cardInfomation[111] = "能对一定范围内的敌人，瞬间造成巨大的伤害，升级后将进一步提高伤害力";
		cardInfomation[112] = "能对一定范围内的敌人，瞬间造成巨大的伤害，升级后将进一步提高伤害力";
		cardInfomation[113] = "能对一定范围内的敌人，瞬间造成巨大的伤害，升级后将进一步提高伤害力";
		cardInfomation[114] = "能对一定范围内的敌人，瞬间造成巨大的伤害，升级后将进一步提高伤害力";
		cardInfomation[115] = "具有恐怖杀伤力的地雷，在没有长出来之前极其脆弱，升级后将进一步提高伤害力";
		cardInfomation[116] = "具有恐怖杀伤力的地雷，在没有长出来之前极其脆弱，升级后将进一步提高伤害力";
		cardInfomation[117] = "具有恐怖杀伤力的地雷，在没有长出来之前极其脆弱，升级后将进一步提高伤害力";
		cardInfomation[118] = "具有恐怖杀伤力的地雷，在没有长出来之前极其脆弱，升级后将进一步提高伤害力";
		cardInfomation[119] = "具有恐怖杀伤力的地雷，在没有长出来之前极其脆弱，升级后将进一步提高伤害力";
		cardInfomation[120] = "无视敌人进攻的地刺，能对敌人造成一定次数的伤害，升级后将进一步提高伤害次数";
		cardInfomation[121] = "无视敌人进攻的地刺，能对敌人造成一定次数的伤害，升级后将进一步提高伤害次数";
		cardInfomation[122] = "无视敌人进攻的地刺，能对敌人造成一定次数的伤害，升级后将进一步提高伤害次数";
		cardInfomation[123] = "无视敌人进攻的地刺，能对敌人造成一定次数的伤害，升级后将进一步提高伤害次数";
		cardInfomation[124] = "无视敌人进攻的地刺，能对敌人造成一定次数的伤害，升级后将进一步提高伤害次数";
		
		cardInfomation[125] = "经过一段时间的聚气后，可以点击释放出强大的火焰，升级后可以减短聚气时间";
		cardInfomation[126] = "经过一段时间的聚气后，可以点击释放出强大的火焰，升级后可以减短聚气时间";
		cardInfomation[127] = "经过一段时间的聚气后，可以点击释放出强大的火焰，升级后可以减短聚气时间";
		cardInfomation[128] = "经过一段时间的聚气后，可以点击释放出强大的火焰，升级后可以减短聚气时间";
		cardInfomation[129] = "经过一段时间的聚气后，可以点击释放出强大的火焰，升级后可以减短聚气时间";
		
		cardInfomation[130] = "经过一段时间的聚气后，可以点击释放出超强的光线，升级后可以减短聚气时间";
		cardInfomation[131] = "经过一段时间的聚气后，可以点击释放出超强的光线，升级后可以减短聚气时间";
		cardInfomation[132] = "经过一段时间的聚气后，可以点击释放出超强的光线，升级后可以减短聚气时间";
		cardInfomation[133] = "经过一段时间的聚气后，可以点击释放出超强的光线，升级后可以减短聚气时间";
		cardInfomation[134] = "经过一段时间的聚气后，可以点击释放出超强的光线，升级后可以减短聚气时间";
		
		
		cardInfomation[300] = "各方面能力比较平均的僵尸，适合初期使用，升级后全面提升能力";
		cardInfomation[301] = "各方面能力比较平均的僵尸，适合初期使用，升级后全面提升能力";
		cardInfomation[302] = "各方面能力比较平均的僵尸，适合初期使用，升级后全面提升能力";
		cardInfomation[303] = "各方面能力比较平均的僵尸，适合初期使用，升级后全面提升能力";
		cardInfomation[304] = "各方面能力比较平均的僵尸，适合初期使用，升级后全面提升能力";
		cardInfomation[305] = "各方面能力比较平均的僵尸，适合中期使用，升级后全面提升能力";
		cardInfomation[306] = "各方面能力比较平均的僵尸，适合中期使用，升级后全面提升能力";
		cardInfomation[307] = "各方面能力比较平均的僵尸，适合中期使用，升级后全面提升能力";
		cardInfomation[308] = "各方面能力比较平均的僵尸，适合中期使用，升级后全面提升能力";
		cardInfomation[309] = "各方面能力比较平均的僵尸，适合中期使用，升级后全面提升能力";
		cardInfomation[310] = "防御力非常搞的僵尸，适合当肉盾使用，升级后可以提升防御力";
		cardInfomation[311] = "防御力非常搞的僵尸，适合当肉盾使用，升级后可以提升防御力";
		cardInfomation[312] = "防御力非常搞的僵尸，适合当肉盾使用，升级后可以提升防御力";
		cardInfomation[313] = "防御力非常搞的僵尸，适合当肉盾使用，升级后可以提升防御力";
		cardInfomation[314] = "防御力非常搞的僵尸，适合当肉盾使用，升级后可以提升防御力";
		cardInfomation[315] = "拥有穿刺能力的僵尸，升级后可以提高穿刺的效果";
		cardInfomation[316] = "拥有穿刺能力的僵尸，升级后可以提高穿刺的效果";
		cardInfomation[317] = "拥有穿刺能力的僵尸，升级后可以提高穿刺的效果";
		cardInfomation[318] = "拥有穿刺能力的僵尸，升级后可以提高穿刺的效果";
		cardInfomation[319] = "拥有穿刺能力的僵尸，升级后可以提高穿刺的效果";
		cardInfomation[320] = "拥有致命一击能力的僵尸，可以有一定概率触发双倍攻击，升级后可以提高触发概率";
		cardInfomation[321] = "拥有致命一击能力的僵尸，可以有一定概率触发双倍攻击，升级后可以提高触发概率";
		cardInfomation[322] = "拥有致命一击能力的僵尸，可以有一定概率触发双倍攻击，升级后可以提高触发概率";
		cardInfomation[323] = "拥有致命一击能力的僵尸，可以有一定概率触发双倍攻击，升级后可以提高触发概率";
		cardInfomation[324] = "拥有致命一击能力的僵尸，可以有一定概率触发双倍攻击，升级后可以提高触发概率";
	
	}

}
