import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class NetClient {
	Main main = null;
	private String IP=null;
	private int udpPort;
	DatagramSocket ds = null;
	DataInputStream dis =null;
	DataOutputStream dos =null;
	Socket s = null;
	
	public NetClient(Main main) {
		this.main = main;
	}

	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}
	
	public String getIP() {
		return IP;
	}

	public void connect(String IP, int port) {
			this.IP=IP;
		try {
			ds = new DatagramSocket(udpPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		try {
			s = new Socket(IP, port);
			dos = new DataOutputStream(s.getOutputStream());
			dos.writeInt(udpPort);
			DataInputStream dis = new DataInputStream(s.getInputStream());
			int id = dis.readInt();
			main.netID = id;
			NetMsg nm = new NetMsg(id,main.netName);
			main.netMsgs.netMsgs.add(nm);
			s.close();
			System.out.println(" id:"+ id);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (s != null) {
				try {
					s.close();
					s = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		ClientNewMsg msg = new ClientNewMsg(this.main);
		this.send(msg);
		new Thread(new UDPRecvThread()).start();
	}
	
	public void disconnect() {
		try {
			dos.close();
			dis.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(Msg msg) {
		msg.send(ds, IP, MainServer.UDP_PORT);

	}

	private class UDPRecvThread implements Runnable {
		byte[] buf = new byte[1024];

		@Override
		public void run() {

			while (ds != null) {
				DatagramPacket dp = new DatagramPacket(buf, buf.length);
				try {
					ds.receive(dp);
					parse(dp);
			//		System.out.println("a packet received! from server");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		private void parse(DatagramPacket dp) {
			ByteArrayInputStream bais = new ByteArrayInputStream(buf, 0, dp
					.getLength());
			dis = new DataInputStream(bais);
			int msgType=0;
			try {
				msgType = dis.readInt();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Msg msg =null;
			switch (msgType) {
			case Msg.CLIENT_NEW_MSG:
			
				msg = new ClientNewMsg(main);
				msg.parse(dis);
				break;
			case Msg.LOCATE_PLANT_MSG:
				 msg = new LocateMsg(main);
				msg.parse(dis);
				break;
				
			case Msg.CLIENT_TURN_MSG:
				 msg = new ClientTurnMsg(main);
				 msg.parse(dis);
				break;	
			
			case Msg.REMOVE_OBJECT_MSG:
				 msg=new RemoveObjectMsg(main);
				 msg.parse(dis);
				 break;
				 
			case Msg.DROP_MSG:
				msg = new DropMsg(main);
				msg.parse(dis);	 
				 break;
				 
			case Msg.OBJECT_MSG:
				msg= new ObjectMsg(main);
				msg.parse(dis);
				break;
			
			case Msg.SYNCHRONIZED_MSG:
				msg= new SynchronizedMsg(main);
				msg.parse(dis);
				break;

			case Msg.NEW_BULLET_MSG:
				msg= new NewBulletMsg(main);
				msg.parse(dis);
				break; 	

			case Msg.CHAT_MSG:
				msg= new ChatMsg(main);
				msg.parse(dis);
				break; 		
				
			case Msg.ROBOT_NEW_MSG:
				msg= new RobotNewMsg(main);
				msg.parse(dis);
				break; 		
					
			}

		}

	}

}
