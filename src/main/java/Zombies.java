import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class Zombies {
	public List<Zombie> allZombies = new ArrayList<Zombie>();
	private Main main = null;
	private List<Plant> plants = null;
	static List<Zombie> newZombieList = new Vector<Zombie>();

	public Zombies(Main main) {
		this.main = main;
		plants = main.plants.allPlants;
	}

	public List<Zombie> getAllZombies() {
		return allZombies;
	}

	public Main getMain() {
		return main;
	}

	public void mapMove(int reverse) {
		for (int i = 0; i < allZombies.size(); i++) {
			allZombies.get(i).mapMove(reverse);

		}

	}

	public void addNetZombie() {
		synchronized (Zombies.newZombieList) {
			synchronized (this.allZombies) {
				Iterator<Zombie> ite = Zombies.newZombieList.iterator();
				while (ite.hasNext()) {
					Zombie z = (Zombie) ite.next();
					this.allZombies.add(z);
					ite.remove();
				}
			}
		}
	}

	public void doAllZombiesUpdate(long timer) {
		Iterator iter = this.allZombies.iterator();
		while (iter.hasNext()) {
			Zombie z = (Zombie) iter.next();

			if (z.isGc()) {
				RemoveObjectMsg msg = new RemoveObjectMsg(
						RemoveObjectMsg.ZOMBIE, z.getNetObjectID(), main);
				main.nc.send(msg);
				iter.remove();
			} else {
				z.update(timer);
			}
		}
		

		
	}

	public void checkHit() {
		for (int i = 0; i < this.allZombies.size(); i++) {
			boolean hited = false;
			boolean hitTem = false;
			Zombie z = allZombies.get(i);
			for (int j = 0; j < plants.size(); j++) {
				Plant p = plants.get(j);
				if (p.getPassAble() == 0) {
					if (z.isOnLeft() != p.isOnLeft()) {
						hited = z.isRectToRect(p);
						// System.out.println(hited+","+p.getRowID()+","+z.
						// getRowID());
						if (hited && p.getRowID() == z.getRowID()) {
							hitTem = true;
							z.setCollisioned(true);
							if (z.isAttackAble()) {
								p.setAttacked(true, z.getZombieID());
								// 处理反弹伤害
								if (p.isBounce())
									z.doBounceHarm(p.getCustom4());
							}
						}
					}
				}

			}
			if (hitTem == false) {
				z.setCollisioned(false);
			}
		}
	}

	public void SynchronizedZombie() {
		synchronized (SynchronizedMsg.zombieSynList) {
			Iterator<ZombieSyn> iter = SynchronizedMsg.zombieSynList.iterator();
			while (iter.hasNext()) {
				ZombieSyn zs = iter.next();
				int mainID = zs.getMainID();
				int x = zs.getX();
				int hp = zs.getHp();
				int netObjectID = zs.getNetObjectID();
				for (Zombie z : Main.zombies.getAllZombies()) {
					if (z.getMainID() == mainID) {
						if (z.getNetObjectID() == netObjectID) {
							z.setX(x + Main.mpx);
							z.setHp(hp);
						}
					}
				}
				iter.remove();
			}

		}

	}

}
