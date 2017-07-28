import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.loon.framework.javase.game.core.graphics.device.LGraphics;

public class Bullets {
	public List<Bullet> allBullets = new Vector<Bullet>();
	private Main main = null;
	private boolean hited = false;

	public Bullets(Main main) {
		this.main = main;
	}

	public List<Bullet> getAllBullets() {
		return allBullets;
	}

	public Main getMain() {
		return main;
	}

	public void mapMove(int reverse) {
		for (int i = 0; i < allBullets.size(); i++) {
			allBullets.get(i).mapMove(reverse);
		}

	}

	public void doAllBulletsUpdate(long timer) {
		Iterator<Bullet> iter = this.allBullets.iterator();
		while (iter.hasNext()) {
			Bullet b = (Bullet) iter.next();
			if (b.isGc()) {
				RemoveObjectMsg msg = new RemoveObjectMsg(
						RemoveObjectMsg.BULLET, b.getNetBulletID(), main);
				main.nc.send(msg);
				iter.remove();
			} else {
				b.update(timer);
			}
		}

	}
	
	public  void  drawBullet(LGraphics g){
		for(Bullet b : this.allBullets){
			 b.createUI(g);
		}
		
		
		
		
	}
	public void addNetBullet() {
		synchronized (NewBulletMsg.bulletsList) {
			synchronized (this.allBullets) {
				Iterator<Bullet> iter = NewBulletMsg.bulletsList.iterator();
				while (iter.hasNext()) {
					Bullet b = (Bullet) iter.next();
					this.allBullets.add(b);
					iter.remove();
				}
			}
		}
	}

	public void SynchronizedBulletMove() {
		synchronized (SynchronizedMsg.bulletMoveList) {
			Iterator<BulletMoveSyn> iter = SynchronizedMsg.bulletMoveList
					.iterator();
			while (iter.hasNext()) {
				BulletMoveSyn bms = iter.next();
				int mainID = bms.getMainID();
				int x = bms.getX();
				int netObjectID = bms.getNetObjectID();
				for (Bullet b : Main.bullets.allBullets) {
					if (b.getMainID() == mainID) {
						if (b.getNetBulletID() == netObjectID) {
							b.setX(x + Main.mpx);
						}
					}
				}
				iter.remove();
			}

		}

	}

	public  synchronized void checkHit() {
		List<Zombie> az = main.zombies.getAllZombies();
			for (int i = 0; i < az.size(); i++) {
				for (int j = 0; j < allBullets.size(); j++) {
					Bullet b = allBullets.get(j);
					if (az.get(i).isOnLeft() != b.isOnLeft()) {
						if (b.isRowCollisioned() && (b.getRowID() != az.get(i).getRowID())) {
						} else {
						hited = az.get(i).isRectToRect(b);
						if (hited) {
							b.setHited(true);
							az.get(i).setHited(true, b.getBulletID());
						}
					}

				}
			}
		}
	
		for (Bullet b : this.allBullets) {
			for (Plant p : Main.plants.allPlants) {
				if (b.isOnLeft() != p.isOnLeft() && p.getPlantType()!=Plant.BRAIN) {
					if (b.isRowCollisioned() && (b.getRowID() != p.getRowID())) {
					} else {
						hited = p.isRectToRect(b);
						if (hited) {
							b.setHited(true);
							p.setPlantAttacked(true, b.getBulletID());
						}
					}

				}

			}
		}
	}
}
