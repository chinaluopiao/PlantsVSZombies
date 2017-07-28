import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.loon.framework.javase.game.GameScene;
import org.loon.framework.javase.game.core.graphics.Deploy;

public class MainServer {
	public static int  ID =1;
	public static final int TCP_PORT = 8888;
	public static final int UDP_PORT = 6667;
	private static String localIP;
	private static int clientNum;
	DataInputStream dis =null;
	static List<Client> clients = new ArrayList<Client>();
	public MainServer(){
		this.start();
	}
	public void start() {
		new Thread(new UdpThread()).start();
		ServerSocket ss=null;
		try {
			ss = new ServerSocket(TCP_PORT);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//显示本机的ip地址
				localIP =GetPublicIP.GetPublicIP("http://www.bliao.com/ip.phtml", "IP_Temp.tmp");
				String sa = null;
				try {
					sa = InetAddress.getLocalHost().getHostAddress().toString();
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				}
				if(null ==localIP)localIP="你小子没上网吧?"+sa;
				ServerScreen.localIP=localIP;
		while (true) {
			Socket s = null;
			try {
				s = ss.accept();
				DataInputStream dis = new DataInputStream(s.getInputStream());
				String IP = s.getInetAddress().getHostAddress();
				int udpPort = dis.readInt();
				Client c = new Client(IP, udpPort);
				clients.add(c);
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				dos.writeInt(ID++);
				System.out.println("A Client Connect ! Addr:"
						+ s.getInetAddress().getHostAddress() + ":" + s.getPort()+"----udpPort:"+udpPort);

			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(s!=null){
					try {
						s.close();
						s=null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
		}

	}

	public static void main(String[] args) {
		GameScene frame1 = new GameScene("主机", 300, 300);
		Deploy deploy = frame1.getDeploy();
		deploy.setFPS(30);
		deploy.setScreen(new ServerScreen());
		deploy.setShowFPS(true);
		deploy.setLogo(false);
		deploy.mainLoop();
		frame1.showFrame();
		new MainServer();
	}

	private class Client {
		String IP;
		int udpPort;
		
		public Client(String IP, int udpPort) {
			this.IP = IP;
			this.udpPort = udpPort;

		}
	}
	
	public class UdpThread implements Runnable{
			byte[] buf =new byte[1024];
		@Override
		public void run() {
			DatagramSocket ds =null;
			try {
				ds = new DatagramSocket(UDP_PORT);
			} catch (SocketException e) {
			
				e.printStackTrace();
			}	
			System.out.println("UDP_PORT "+UDP_PORT);
			while(ds!=null){
				DatagramPacket dp =new DatagramPacket(buf ,buf.length);
				try {
					ds.receive(dp);
					ByteArrayInputStream bais = new ByteArrayInputStream(buf, 0, dp
							.getLength());
					dis = new DataInputStream(bais);
					int msgType=0;
					String  ip=null ;
					try {
						msgType = dis.readInt();
						 ip = dis.readUTF();
					} catch (IOException e) {
						e.printStackTrace();
					}
				
					if(msgType == Msg.CLIENT_EXIT_MSG){
						for (int i = 0;i<clients.size();i++){
							Client c = clients.get(i);
							if(c.IP.equals(ip)){
								clients.remove(c);
							}
						}	
						return;
					}
					for (int i = 0;i<clients.size();i++){
						Client c = clients.get(i);
						dp.setSocketAddress(new InetSocketAddress(c.IP,c.udpPort));
						ds.send(dp);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
}
