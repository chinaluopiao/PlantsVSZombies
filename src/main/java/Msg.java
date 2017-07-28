import java.io.DataInputStream;
import java.net.DatagramSocket;


public interface Msg {
		public static final int CLIENT_NEW_MSG =1;
		public static final int LOCATE_PLANT_MSG =2;
		public static final int CLIENT_TURN_MSG =3;
		public static final int REMOVE_OBJECT_MSG =4;
		public static final int DROP_MSG=5;
		public static final int OBJECT_MSG=6;
		public static final int SYNCHRONIZED_MSG=7;
		public static final int NEW_BULLET_MSG=8;
		public static final int CLIENT_EXIT_MSG = 9;
		public static final int CHAT_MSG = 10;
		public static final int ROBOT_NEW_MSG = 11;
		
		public void send(DatagramSocket ds,String IP,int udpPort);
		public void parse(DataInputStream dis);
		
}
