import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class ClientTurnMsg implements Msg {

	private int netID;
	private Main main = null;
	private boolean isLeft;
	private int locateID;
	private boolean action;
	public ClientTurnMsg(boolean isLeft,boolean action, Main main) {
		this.main = main;
		//this.locateID=locateID;
		this.isLeft = isLeft;
		this.action=action;
	}

	public ClientTurnMsg(Main main) {
		this.main = main;
		
	}

	int msgType = Msg.CLIENT_TURN_MSG;

	@Override
	public void parse(DataInputStream dis) {
		try {
			int id = dis.readInt();
			if (main.netID == id) {
				return;
			}
			boolean isLeft = dis.readBoolean();
			boolean isAction =dis.readBoolean();
			for (NetMsg c : main.netMsgs.netMsgs) {
				if(c.getNetID()==id){
					c.setAction(isAction);
						c.setLeft(isLeft);
					
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
			dos.writeBoolean(this.isLeft);
			dos.writeBoolean(this.action);
		
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
