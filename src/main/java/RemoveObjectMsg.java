import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class RemoveObjectMsg implements Msg {
	private Main main = null;
	private int objectType;
	private int netObjectID;
	static final int PLANT = 1;
	static final int ZOMBIE = 2;
	static final int BULLET = 3;

	public RemoveObjectMsg(int objectType, int netObjectID, Main main) {
		this.objectType = objectType;
		this.main = main;
		this.netObjectID = netObjectID;
	}

	public RemoveObjectMsg(Main main) {
		this.main = main;

	}

	int msgType = Msg.REMOVE_OBJECT_MSG;

	@Override
	public void parse(DataInputStream dis) {
		try {
			int id = dis.readInt();
			if (main.netID == id) {
				return;
			}
			int objectType = dis.readInt();
			int netObjectID = dis.readInt();
			if (objectType == RemoveObjectMsg.PLANT) {
			synchronized(Main.plants.allPlants){	
				for (Plant p : Main.plants.allPlants) {
					if(p.getMainID() == id){
						if (p.getNetPlantID() == netObjectID) {
							p.setGc(true);
						main.fields.getAllField().get(p.getFieldID()).netRemoveObject();
							break;
						}
					}
				}
			}
				if (objectType == RemoveObjectMsg.ZOMBIE) {
					for (Zombie z : main.zombies.allZombies) {
						if (z.getMainID() == id) {
							if (z.getNetObjectID() == netObjectID) {
								z.setGc(true);
								break;
							}

						}
					}
				}
				
				if (objectType == RemoveObjectMsg.BULLET) {
					for (Bullet b : Main.bullets.allBullets) {
						if (b.getNetBulletID() == id) {
							if (b.getNetBulletID() == netObjectID) {
								b.setGc(true);
								break;
							}

						}
					}
				}
				
				

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
			dos.writeInt(this.objectType);
			dos.writeInt(this.netObjectID);

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
