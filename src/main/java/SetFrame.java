import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.loon.framework.javase.game.core.LSystem;




public class SetFrame extends Frame{
	Main main =null;

	public SetFrame(Main main){
		this.main=main;
	     ConnDialog dialog = new ConnDialog();
	     dialog.setVisible(true);
	}
	
	class ConnDialog extends Dialog {
		Button b = new Button("确定");
		TextField tfIP = new TextField("127.0.0.1", 12);
		TextField tfPort = new TextField("" + MainServer.TCP_PORT, 4);
		TextField myudpPort = new TextField("2223", 4);
		TextField myName = new TextField("your name", 8);
		public ConnDialog() {
			super(SetFrame.this, true);

			this.setLayout(new FlowLayout());
			this.add(new Label("IP:"));
			this.add(tfIP);
			/*this.add(new Label("Port:"));
			this.add(tfPort);
			this.add(new Label("UDP Port:"));
			this.add(myudpPort);*/
			this.add(new Label("昵称:"));
			this.add(myName);
			this.add(b);
			this.setLocation(300, 300);
			this.pack();
			this.addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosing(WindowEvent e) {
					setVisible(false);
					LSystem.exit();
				}
			});
			b.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					 main.IP = tfIP.getText();
					 main.port = 8888;
					 main.udpPort = 2223;
					 main.netName = myName.getText();
					setVisible(false);
				}
			});

		}
	}

}
