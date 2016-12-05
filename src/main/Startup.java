package main;

import java.awt.EventQueue;
import java.util.Random;

import user_interface.MasterWindow;

public class Startup {

	public static Random rand = new Random();
	public static MasterWindow masterWindow;
	
	public static void main(String [] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new ImageLoader();
					masterWindow = new MasterWindow();
					masterWindow.pack();
					masterWindow.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
