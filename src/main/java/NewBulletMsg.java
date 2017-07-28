import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.List;
import java.util.Vector;

public class NewBulletMsg implements Msg {
	private int netBulletID;
	private boolean onLeft;
	private int x, y;
	private int bulletID;
	private int da, angle, v0, rowID;
	private Main main = null;
	static List<Bullet> bulletsList = new Vector<Bullet>();
	private boolean keys;
	private int plantType;

	public NewBulletMsg(int netBulletID, boolean onLeft, int x, int y,
			int bulletID, int da, int angle, int v0, int rowID, Main main) {
		this.x = x;
		this.da = da;
		this.angle = angle;
		this.v0 = v0;
		this.rowID = rowID;
		this.y = y;
		this.onLeft = onLeft;
		this.netBulletID = netBulletID;
		this.bulletID = bulletID;
		this.main = main;
		this.plantType = Resource.getPlantInfo(bulletID)[1];

	}

	public NewBulletMsg(Main main) {
		this.main = main;

	}

	int msgType = Msg.NEW_BULLET_MSG;

	@Override
	public void parse(DataInputStream dis) {
		try {
			int id = dis.readInt();
			if (main.netID == id) {
				return;
			}
			int netBulletID = dis.readInt();
			boolean onLeft = dis.readBoolean();
			int bulletID = dis.readInt();
			int x = dis.readInt();
			int y = dis.readInt();
			int rowID = dis.readInt();
			int da  = dis.readInt();
			int plantType = dis.readInt();
			int  angle =dis.readInt();
			int v0 = dis.readInt();
			Bullet b = new Bullet(netBulletID,onLeft, x + Main.mpx, y, bulletID,da,angle,v0,rowID,Main.bullets);	
				switch(plantType){
				case Plant.MULITANGLE:
					b.doRotate(angle);
					break;
				case Plant.SPIN :
					b.doRotate(angle);
					 break;
				case Plant.PARABOLA :
					b.doParabola(v0, angle);
					break;
				}
					NewBulletMsg.bulletsList.add(b);
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
			dos.writeInt(this.netBulletID);
			dos.writeBoolean(this.onLeft);
			dos.writeInt(this.bulletID);
			dos.writeInt(this.x);
			dos.writeInt(this.y);
			dos.writeInt(this.rowID);
			dos.writeInt(this.da);
			dos.writeInt(this.plantType);
				dos.writeInt(this.angle);
				dos.writeInt(this.v0);

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
