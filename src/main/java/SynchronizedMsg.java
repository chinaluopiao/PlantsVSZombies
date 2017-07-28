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

public class SynchronizedMsg implements Msg {
	private int netObjectID, x,fieldID;
	private Main main = null;
	private int type;
	private int hp;
	private boolean onLeft;
	static List<BulletMoveSyn> bulletMoveList = new Vector<BulletMoveSyn>();
	static List<PlantSyn> plantSynList = new Vector<PlantSyn>();
	static List<ZombieSyn> zombieSynList = new Vector<ZombieSyn>();
	static List<MsgActionSyn> msgActionList = new Vector<MsgActionSyn>();
	static final int BULLETMOVE = 1;
	static final int PLANTHP = 2;
	static final int NETMSGACTION = 3;
	static final int PLANTSPECIALCLICK=4;
	static final int ZOMBIESYN=6;
	
	private boolean action; 
	public SynchronizedMsg(int type, int netObjectID, int x, Main main) {
		this.main = main;
		this.type = type;
		this.x = x;
		this.netObjectID = netObjectID;
	}

	public SynchronizedMsg(int type, int netObjectID, int hp, Main main,
			boolean Synhp) {
		this.main = main;
		this.type = type;
		this.netObjectID = netObjectID;
		this.hp = hp;
	}
	
	public SynchronizedMsg(int type,boolean action,boolean onLeft, Main main) {
		this.main = main;
		this.type = type;
		this.action =action;
		this.onLeft = onLeft;
	}
	
	//处理special植物的点击
	public SynchronizedMsg(int type,int netObjectID, Main main) {
		this.main = main;
		this.type = type;
		this.netObjectID=netObjectID;
	}
	//处理zombie属性同步
	public SynchronizedMsg(int type, int netObjectID, int x,int hp, Main main) {
		this.main = main;
		this.type = type;
		this.x = x;
		this.hp=hp;
		this.netObjectID = netObjectID;
	}
	
	public SynchronizedMsg(Main main) {
		this.main = main;

	}

	int msgType = Msg.SYNCHRONIZED_MSG;

	@Override
	public void parse(DataInputStream dis) {
		try {

			int id = dis.readInt();
			if (Main.netID == id) {
				return;
			}
			int type = dis.readInt();
			boolean action=false;
			boolean onLeft =false;
			int netObjectID=0;
			if(type ==SynchronizedMsg.NETMSGACTION){
			 action= dis.readBoolean();
			 onLeft = dis.readBoolean() ;
			}else{
				 netObjectID = dis.readInt();	
					if(type ==SynchronizedMsg.PLANTSPECIALCLICK){
						PlantSyn phs = new PlantSyn(id,type,netObjectID);
						SynchronizedMsg.plantSynList.add(phs);
					}
			}
			if(type ==SynchronizedMsg.ZOMBIESYN){
				int x = dis.readInt();
				int hp = dis.readInt();
		
				ZombieSyn zs = new ZombieSyn(id,netObjectID,hp,x);
				SynchronizedMsg.zombieSynList.add(zs);
			}
			if(type ==SynchronizedMsg.BULLETMOVE){
				int x = dis.readInt();
				BulletMoveSyn t = new BulletMoveSyn(id, netObjectID, x);
				SynchronizedMsg.bulletMoveList.add(t);
			}
			if(type ==SynchronizedMsg.PLANTHP){
				int hp = dis.readInt();
				PlantSyn phs = new PlantSyn(id,type,hp,netObjectID);
				SynchronizedMsg.plantSynList.add(phs);
			}
			if(type ==SynchronizedMsg.NETMSGACTION){
				MsgActionSyn mas = new MsgActionSyn(action ,onLeft,id);
				SynchronizedMsg.msgActionList.add(mas);	
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
			dos.writeInt(this.type);
			if(this.type ==SynchronizedMsg.NETMSGACTION){
				dos.writeBoolean(action);
				dos.writeBoolean(this.onLeft);
			}else{
				dos.writeInt(this.netObjectID);		
			}
			if(this.type == SynchronizedMsg.ZOMBIESYN){
				dos.writeInt(this.x);
				dos.writeInt(this.hp);
			}
			if (this.type == SynchronizedMsg.BULLETMOVE)
				dos.writeInt(this.x);
			if (this.type == SynchronizedMsg.PLANTHP)
				dos.writeInt(this.hp);
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
