import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Iterator;

public class RobotNewMsg implements Msg {
	private int objectID;
	private int netObjectID;
	private Main main = null;
	private int fieldID;

	public RobotNewMsg(Main main) {
		this.main = main;
	}

	public RobotNewMsg(int fieldID, int netObjectID, int objectID, Main main) {
		this.main = main;
		this.objectID = objectID;
		this.netObjectID = netObjectID;
		this.fieldID = fieldID;
	}

	int msgType = Msg.ROBOT_NEW_MSG;

	@Override
	public void parse(DataInputStream dis) {
		try {
			int id = dis.readInt();
			if (Main.netID == id) {
				return;
			}
			int netObjectID = dis.readInt();
			int fieldID = dis.readInt();
			int objectID = dis.readInt();
			boolean add = true;
			if (objectID > 300) {
				synchronized (Main.zombies.allZombies) {
					synchronized (Zombies.newZombieList) {
						for (Zombie z : Main.zombies.allZombies) {
							if (z.getNetObjectID() == netObjectID
									&& z.getMainID() == id) {
								add = false;
								break;
							}
						}
						for (Zombie z : Zombies.newZombieList) {
							if (z.getNetObjectID() == netObjectID
									&& z.getMainID() == id) {
								add = false;
								break;
							}
						}
					}
				}
			} else {
				synchronized (main.plants.allPlants) {
					Iterator iter = main.plants.allPlants.iterator();
					while (iter.hasNext()) {
						Plant p = (Plant) iter.next();
						if (p.getNetPlantID() == netObjectID
								&& p.getMainID() == id) {
							add = false;
							break;
						}
					}
				}
				for (Plant p : Plants.newPlantList) {
					if (p.getNetPlantID() == netObjectID && p.getMainID() == id) {
						add = false;
						break;
					}
				}
			}

			if (add) {
				Field filed = Main.fields.getAllField().get(fieldID);
				filed.setHandleAble(false);
				filed.locateNetRobot(objectID, id, netObjectID);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgType);
			dos.writeInt(Main.netID);
			dos.writeInt(this.netObjectID);
			dos.writeInt(this.fieldID);
			dos.writeInt(this.objectID);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] buf = baos.toByteArray();
		try {
			DatagramPacket dp = new DatagramPacket(buf, buf.length,
					new InetSocketAddress(IP, udpPort));
			ds.send(dp);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
