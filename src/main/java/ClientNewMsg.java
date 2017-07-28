import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class ClientNewMsg implements Msg {
	Main main=null;
	int msgType = Msg.CLIENT_NEW_MSG;
	public ClientNewMsg() {
	}

	public ClientNewMsg(Main main) {
		this.main = main;

	}

	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgType);
			dos.writeInt(main.netID);
			dos.writeUTF(main.netName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] buf = baos.toByteArray();
		try {
			DatagramPacket dp = new DatagramPacket(buf, buf.length,new InetSocketAddress(IP, udpPort));
			ds.send(dp);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void parse(DataInputStream dis) {
		try {
			int id = dis.readInt();
			if (main.netID == id) {
				return;
			}
			String netName = dis.readUTF();
			boolean exist = false;
			for (int i = 0; i < main.netMsgs.netMsgs.size(); i++) {
				NetMsg nm = main.netMsgs.netMsgs.get(i);
				if (nm.getNetID() == id) {
					exist = true;
					break;
				}
				
			}
			if (exist ==false) {
				ClientNewMsg cnMsg = new ClientNewMsg(main);
				main.nc.send(cnMsg);
				NetMsg nmt = new NetMsg(id, netName);
				main.netMsgs.netMsgs.add(nmt);
				ClientTurnMsg ctMsg = new ClientTurnMsg(main.isLeft,main.action,main);
				main.nc.send(ctMsg);
			}
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}
