import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.loon.framework.javase.game.core.LSystem;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.core.timer.LTimer;

public class Robot {
	private int fieldID;
	private int objectID;
	private int stage; // 关卡
	private static boolean onLeft = !Main.isLeft;
	private Main main;
	private int second, minute, hour; // 总的记录时间
	private boolean freshAll=true;
	private boolean deadAll=true;
	private LTimer freshTimer = new LTimer();
	private int freshTime = 3000;// 刷新的间隔时间
	 List<Robot> freshRobotList = new ArrayList<Robot>();

	public Robot(int fieldID, int objectID, Main main) {
		this.fieldID = fieldID;
		this.objectID = objectID;
		this.main = main;
		Main.fields.getAllField().get(fieldID).locateRobot(objectID);
	}

	public Robot(int fieldID, int objectID) {
		this.fieldID = fieldID;
		this.objectID = objectID;
	}

	public Robot(Main main) {
		this.main = main;

	}
	
	public void freshRobot(long timer) {
		if (this.deadAll && this.freshAll) {
			this.stage++;
			adpterStage(this.stage);
		}
		if (this.freshRobotList.size() != 0) {
			if (this.freshTimer.action(timer)) {
				Robot r = this.freshRobotList.get(0);
				Main.fields.getAllField().get(r.getFieldID()).locateRobot(
						r.getObjectID());
				this.freshRobotList.remove(0);
			}
			this.freshAll=false;
		}else{
			this.freshAll=true;
			this.checkAllDead();
		}
		
	
	}

	private void checkAllDead() {
		int allDead=0;
		for(Zombie z : main.zombies.getAllZombies()){
				if (z.isRobot())allDead++;
		}
		if(allDead>0){
			this.deadAll=false;
		}else {
			this.deadAll=true;
		}
	}

	private void adpterStage(int stage) {
		switch (stage) {
		case 1:
			this.freshTimer.setDelay(3500);
			addRandomZombie(10, 301);
			addRandomZombie(10, 306);
			addRandomZombie(10, 311);
			addRandomZombie(10, 316);
			addRandomZombie(10, 321);
			break;
		case 2:
			this.freshTimer.setDelay(1000);
			this.addRankZombie(60, 301);
			break;	
		case 3:
			this.freshTimer.setDelay(3500);
			addRandomZombie(10, 302);
			addRandomZombie(10, 307);
			addRandomZombie(10, 312);
			addRandomZombie(10, 317);
			addRandomZombie(10, 322);
			break;	
		case 4:
			this.freshTimer.setDelay(1000);
			this.addRankZombie(60, 302);
			break;	
		case 5:
			this.freshTimer.setDelay(3000);
			addRandomZombie(10, 303);
			addRandomZombie(10, 308);
			addRandomZombie(10, 313);
			addRandomZombie(10, 318);
			addRandomZombie(10, 323);
			break;	
		case 6:
			this.freshTimer.setDelay(1000);
			this.addRankZombie(80, 303);
			break;	
		case 7:
			this.freshTimer.setDelay(3000);
			addRandomZombie(10, 304);
			addRandomZombie(10, 309);
			addRandomZombie(10, 314);
			addRandomZombie(10, 319);
			addRandomZombie(10, 324);
			break;	
		case 8:
			this.freshTimer.setDelay(1000);
			this.addRankZombie(80, 304);
			break;	
			
		case 9:
			this.freshTimer.setDelay(3000);
			addRandomZombie(10, 305);
			addRandomZombie(10, 310);
			addRandomZombie(10, 315);
			addRandomZombie(10, 320);
			addRandomZombie(10, 325);
			break;	
		
		case 10:
			this.freshTimer.setDelay(2000);
			this.addRankZombie(100, 305);
			break;	
		
		case 11:
			this.freshTimer.setDelay(1500);
			this.addRankZombie(100, 305);
			break;

		case 12:
			this.freshTimer.setDelay(1400);
			this.addRankZombie(100, 305);
			break;			

		case 13:
			this.freshTimer.setDelay(1200);
			this.addRankZombie(100, 305);
			break;

		case 14:
			this.freshTimer.setDelay(1000);
			this.addRankZombie(100, 305);
			break;

		case 15:
			this.freshTimer.setDelay(900);
			this.addRankZombie(100, 305);
			break;

		case 16:
			this.freshTimer.setDelay(800);
			this.addRankZombie(100, 305);
			break;			

		case 17:
			this.freshTimer.setDelay(700);
			this.addRankZombie(100, 305);
			break;			

		case 18:
			this.freshTimer.setDelay(600);
			this.addRankZombie(100, 305);
			break;			

		case 19:
			this.freshTimer.setDelay(500);
			this.addRankZombie(100, 305);
			break;

		case 20:
			this.freshTimer.setDelay(400);
			this.addRankZombie(100, 305);
			break;

		case 21:
			this.freshTimer.setDelay(300);
			this.addRankZombie(100, 305);
			break;

		case 22:
			this.freshTimer.setDelay(250);
			this.addRankZombie(100, 305);
			break;			
			

		case 23:
			this.freshTimer.setDelay(200);
			this.addRankZombie(100, 305);
			break;			

		case 24:
			this.freshTimer.setDelay(150);
			this.addRankZombie(100, 305);
			break;			
		default: 
		this.freshTimer.setDelay(100);
		this.addRankZombie(200+200*this.stage, 305);
		break;			
		}

	}

	public void recordTime() {
		this.second++;
		if (this.second == 1800) {
			this.minute++;
			this.second = 0;
		}

		if (this.minute == 60) {
			this.minute = 0;
			this.hour++;
		}
	}

	public void drawInfo(LGraphics g) {
		g.setAlpha(0.8f);
		g.drawImage(Resource.getStagePic(), 20,500);
		g.drawImage(Resource.getTimePic(), 20,525);
		g.setAlpha(1f);
		g.drawString(this.stage+"", 73, 518);
		g.drawString(this.hour + ":" + this.minute + ":"
				+ (this.second / 30), 63, 542);
	}

	// 在某一草地，添加某一物体
	private void addZombie(int fieldID, int objectID) {
		Main.fields.getAllField().get(fieldID).locateRobot(objectID);
	}

	private void addRandomZombie(int num, int objectID) {
		for (int i = 0; i < num; i++) {
			int fieldID = LSystem.getRandom(1, 5);
			if (!Main.isLeft) {
				switch(fieldID){
				case 1 :
					fieldID = 0;
					break;
				case 2 :
					fieldID = 9;
					break;
				case 3 :
					fieldID = 18;
					break;
				case 4 :
					fieldID = 27;
					break;
				case 5 :
					fieldID = 36;
					break;
				}
				Robot r = new Robot(fieldID, objectID);
				this.freshRobotList.add(r);
			} else {
				switch(fieldID){
				case 1 :
					fieldID = 53;
					break;
				case 2 :
					fieldID = 62;
					break;
				case 3 :
					fieldID = 71;
					break;
				case 4 :
					fieldID = 80;
					break;
				case 5 :
					fieldID = 89;
					break;
				}
				Robot r = new Robot(fieldID, objectID);
				this.freshRobotList.add(r);
			}
		}

	}
	
	//刷新处于同一级别的僵尸
	public void addRankZombie(int num ,int rank){
		for (int i = 0; i < num; i++) {
			int rankTemp= LSystem.getRandom(1, 5);
			rankTemp = rank +(rankTemp-1)*5; 
			addRandomZombie(1,  rankTemp);
		}
		
	}
	
	
	private void addPlantAttack() {

	}

	private void addMixAttack() {

	}


	public int getFieldID() {
		return fieldID;
	}

	public int getObjectID() {
		return objectID;
	}

}
