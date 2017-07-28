import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class Plants {
	List<Plant> allPlants = new ArrayList<Plant>();
	List<Plant> rangeAttackPlants = new ArrayList<Plant>();
	static List<Plant> newPlantList = new Vector<Plant>();
	private int plantsID;
	private Main main = null;

	public Plants(Main main) {
		this.main = main;

	}

	public Main getMain() {
		return main;
	}

	// 所有植物的地图坐标
	public void mapMove(int reverse) {
		for (int i = 0; i < allPlants.size(); i++) {
			allPlants.get(i).mapMove(reverse);
		}

	}

	public void doAllPlantsUpdate(long timer) {
		synchronized (this.allPlants) {
			Iterator iter = this.allPlants.iterator();
			while (iter.hasNext()) {
				Plant p = (Plant) iter.next();
				if (p.isGc()) {
					RemoveObjectMsg msg = new RemoveObjectMsg(
							RemoveObjectMsg.PLANT, p.getNetPlantID(), main);
					main.nc.send(msg);
					iter.remove();
				} else {
					p.update(timer);
				}
			}
		}
	}

	public void addNetPlant() {
		synchronized (this.newPlantList) {
			Iterator<Plant> ite = this.newPlantList.iterator();
			while (ite.hasNext()) {
				Plant p = (Plant) ite.next();
				this.allPlants.add(p);
				ite.remove();

			}
		}
	}

	public void plantSyn() {
		synchronized (SynchronizedMsg.plantSynList) {
			synchronized (this.allPlants) {
				Iterator<PlantSyn> ite = SynchronizedMsg.plantSynList
						.iterator();
				while (ite.hasNext()) {
					PlantSyn phs = (PlantSyn) ite.next();
					for (Plant p : this.allPlants) {
						if (p.getNetPlantID() == phs.getNetObjectID()
								&& p.getMainID() == phs.getMainID()) {
							if (phs.getType() == SynchronizedMsg.PLANTHP) {
								p.setHp(phs.getHp());
								break;
							} else {
								p.setTemp(true);
								p.setTemp1(true);
								p.setClicked(true);
								break;
							}
						}
					}
					ite.remove();
				}

			}
		}

	}

	// 处理范围攻击的植物碰撞检测
	public void checkPlantRangeAttack() {
		for (Zombie z : main.zombies.allZombies) {
			for (Plant p : this.rangeAttackPlants) {
				if (z.isOnLeft() != p.isOnLeft() && p.isFireable()) {
					if (p.isRowCollisioned() && p.getRowID() != z.getRowID()) {
					} else {
						boolean hited = z.getCollisionBox().intersects(
								p.getRect());
						if (hited) {
							p.setHit(true);
							if (p.getPlantType() == Plant.MINE) {
								if (!p.isTemp()) {
								} else {
									z.setHited(true, p.getObjectID());
								}
							} else {
								z.setHited(true, p.getObjectID());
							}

						}

					}
				}
			}

		}

		Iterator iter = this.rangeAttackPlants.iterator();
		while (iter.hasNext()) {
			Plant p = (Plant) iter.next();
			if (p.isGc()) {
				iter.remove();
			}
		}

	}

}
