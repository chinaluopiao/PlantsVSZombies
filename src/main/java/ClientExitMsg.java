import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;


public class ClientExitMsg implements Msg {
	Main main;
	 public ClientExitMsg(Main main ){
		 this.main=main;
	 }
	 
	int msgType =Msg.CLIENT_EXIT_MSG;
	@Override
	public void parse(DataInputStream  dis) {
	}
	@Override
	public void send(DatagramSocket ds, String IP, int udpPort) {
		   ByteArrayOutputStream baos = new ByteArrayOutputStream();
	         DataOutputStream  dos = new DataOutputStream(baos);
			 try {
				dos.writeInt(msgType);
				dos.writeUTF(Main.nc.getIP());
			} catch (IOException e) {
				e.printStackTrace();
			}
			byte[] buf = baos.toByteArray();
			try {
				DatagramPacket dp = new DatagramPacket(buf,buf.length,new InetSocketAddress(IP,udpPort));
				ds.send(dp);
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		
	}
	
	
}
