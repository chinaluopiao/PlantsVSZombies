import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Iterator;

public class LocateMsg implements Msg {
	private int objectID;
	private int x, y;
	private int netObjectID;
	private Main main = null;
	private int fieldID;
	private boolean onLeft;

	public LocateMsg(boolean onLeft, int fieldID, int netObjectID,
			int objectID, int x, int y, Main main) {
		this.main = main;
		this.netObjectID = netObjectID;
		this.objectID = objectID;
		this.x = x;
		this.y = y;
		this.fieldID = fieldID;
		this.onLeft = onLeft;
	}

	public LocateMsg(Main main) {
		this.main = main;
	}

	public LocateMsg(int fieldID, int objectID, Main main) {
		this.main = main;
		this.objectID = objectID;
		this.fieldID = fieldID;
	}

	int msgType = Msg.LOCATE_PLANT_MSG;

	@Override
	public void parse(DataInputStream dis) {
		try {
			int id = dis.readInt();
			if (Main.netID == id) {
				return;
			}
			boolean onLeft = dis.readBoolean();
			int netObjectID = dis.readInt();
			int fieldID = dis.readInt();
			int objectID = dis.readInt();
			int x = dis.readInt();
			int y = dis.readInt();
			String s = dis.readUTF();
			boolean add = true;
			if (objectID > 300) {
				for (Zombie z : main.zombies.allZombies) {
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
			} else {
				synchronized (main.plants.allPlants) {
					synchronized (Plants.newPlantList) {
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
						if (p.getNetPlantID() == netObjectID
								&& p.getMainID() == id) {
							add = false;
							break;
						}
					}
				}
			}
			if (add) {
				Field filed = main.fields.getAllField().get(fieldID);
				filed.setHandleAble(false);
				filed.adapterNet(id, onLeft, netObjectID, objectID, fieldID, x,
						y, s);
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
			dos.writeBoolean(this.onLeft);
			dos.writeInt(this.netObjectID);
			dos.writeInt(this.fieldID);
			dos.writeInt(this.objectID);
			dos.writeInt(this.x);
			dos.writeInt(this.y);
			dos.writeUTF(Main.netName);
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
