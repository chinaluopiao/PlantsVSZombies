import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class DropMsg implements Msg {
	private int netDropID;
	private Main main = null;
	private int dropID, msgID, netID;
	private int x;
	static final int SHOW = 1;
	static final int CLICKED = 2;

	public DropMsg(int netID, int msgID, int dropID, int netDropID, int x,
			Main main) {
		this.msgID = msgID;
		this.main = main;
		this.netID = netID;
		this.x = x;
		this.netDropID = netDropID;
		this.dropID = dropID;

	}

	public DropMsg(int netID, int msgID, int dropID, int netDropID, Main main) {
		this.msgID = msgID;
		this.netID = netID;
		this.main = main;
		this.netDropID = netDropID;
		this.dropID = dropID;

	}

	public DropMsg(Main main) {
		this.main = main;

	}

	int msgType = Msg.DROP_MSG;

	@Override
	public void parse(DataInputStream dis) {
		try {

			int id = dis.readInt();
			if (Main.netID == id) {
				return;
			}
			int netID = dis.readInt();
			int msgID = dis.readInt();
			int dropID = dis.readInt();
			int netDropID = dis.readInt();
			if (msgID == DropMsg.SHOW) {
				int x = dis.readInt();
				if (dropID == Drop.FLOWER) {
					new Drop(Drop.FLOWER, netID, netDropID, x, main);
				}
			} else {
				synchronized (Drop.allDrops) {
					for (Drop d : Drop.allDrops) {
						if (d.getMainID() == netID) {
							if (d.getNetDropID() == netDropID) {
								d.setGc(true);
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
			dos.writeInt(this.netID);
			dos.writeInt(this.msgID);
			dos.writeInt(this.dropID);
			dos.writeInt(this.netDropID);
			if (this.msgID == DropMsg.SHOW) {
				dos.writeInt(this.x);
			}
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
