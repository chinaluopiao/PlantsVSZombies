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


public class ChatMsg implements Msg {
	private int mainID;
	private String  textContent,netName;
	private Main main = null;
	private int  chatNetID ;
	static List<ChatMsg> chatList = new Vector<ChatMsg>();
	public ChatMsg(int mainID,int chatNetID, String netName, String textContent) {
		this.mainID = mainID;
		this.textContent = textContent;
		this.chatNetID = chatNetID;
		this.netName  = netName;
	}
	public ChatMsg(Main main){
		this.main = main;
	}	
	public int getMainID() {
		return mainID;
	}
	public void setMainID(int mainID) {
		this.mainID = mainID;
	}
	public String getTextContent() {
		return textContent;
	}
	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
	}
	public int getChatNetID() {
		return chatNetID;
	}
	public void setChatNetID(int chatNetID) {
		this.chatNetID = chatNetID;
	}
	int msgType = Msg.CHAT_MSG;
	@Override
	public void parse(DataInputStream dis) {
		try {
			int id = dis.readInt();
			if (Main.netID == id) {
				return;
			}
			this.chatNetID=dis.readInt();
			this.netName = dis.readUTF();
			this.textContent =dis.readUTF();
			boolean exist=false;
			for(ChatMsg cm  :main.getChatTextList()){
				if(cm.getMainID() == id && cm.getChatNetID() ==this.chatNetID)exist=true;
			}
			if(!exist){
				ChatMsg cm = new ChatMsg(id,this.chatNetID,this.netName,this.textContent);
				ChatMsg.chatList.add(cm);	
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
			dos.writeInt(this.chatNetID);
			dos.writeUTF(Main.netName);
			dos.writeUTF(this.textContent);
		
			
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
