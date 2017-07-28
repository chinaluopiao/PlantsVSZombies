import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class ObjectMsg implements Msg {
	private int type;
	private int netObjectID;
	private Main main = null;
	private int angle;
	static final int PARABOLA = 1;
	static final int SPIN = 2;

	public ObjectMsg(int type, int netObjectID, int angle, Main main) {
		this.main = main;
		this.netObjectID = netObjectID;
		this.type = type;
		this.angle = angle;
	}

	public ObjectMsg(Main main) {
		this.main = main;

	}

	int msgType = Msg.OBJECT_MSG;

	@Override
	public void parse(DataInputStream dis) {
		try {
			int id = dis.readInt();
			if (main.netID == id) {
				return;
			}
			int type = dis.readInt();
			int netObjectID = dis.readInt();
			int angle = dis.readInt();
			synchronized (main.plants.allPlants) {
				for (Plant p : main.plants.allPlants) {
					if (p.getNetPlantID() == netObjectID) {
						p.setAngle(angle);
						break;
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
			dos.writeInt(main.netID);
			dos.writeInt(this.type);
			dos.writeInt(this.netObjectID);
			dos.writeInt(this.angle);
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
