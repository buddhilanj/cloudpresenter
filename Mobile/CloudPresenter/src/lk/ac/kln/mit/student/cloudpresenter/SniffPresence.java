package lk.ac.kln.mit.student.cloudpresenter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import lk.ac.kln.mit.student.cloudpresenter.controller.DeviceManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.util.Log;
import android.widget.Toast;

public class SniffPresence extends IntentService {

	private InetAddress localdevice;
	private int port = 4000;
	private InetAddress multicastgroup;
	private MulticastSocket ms;
	private byte[] buffer = new byte[65509];
	private static boolean running;
	private MulticastLock multicastLock;
	
	public SniffPresence() {
		super("SniffPresence");
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		if(!running){
			running=false;
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		multicastLock = wifi.createMulticastLock("multicastLock");
		multicastLock.setReferenceCounted(true);
		multicastLock.acquire();
		running=true;
		Toast.makeText(this, "Sniff starting", Toast.LENGTH_SHORT).show();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		try {
			NetworkInterface np = NetworkInterface.getByName("wlan0");
			Enumeration<InetAddress> enumIpAddr = np.getInetAddresses();
			while (enumIpAddr.hasMoreElements()) {
				InetAddress iptemp = enumIpAddr.nextElement();
				if (!iptemp.isLoopbackAddress()
						&& (iptemp instanceof Inet4Address)) {
					localdevice = iptemp;
				}

			}
			multicastgroup = InetAddress.getByName("224.0.0.1");

			ms = new MulticastSocket(port);
			ms.joinGroup(multicastgroup);
			while (true) {
				buffer = new byte[65509];
				System.gc();
				DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
				ms.receive(dp);
				String s = new String(dp.getData());
				Log.i("Sniffed",s.trim());
				DeviceManager.detectPresence(s.trim(), dp.getAddress()
						.getHostAddress());
			}
		} catch (SocketException ex) {
			System.err.println(ex);
		} catch (IOException ex) {
			System.err.println(ex);
		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (multicastLock != null) {
		    multicastLock.release();
		    multicastLock = null;
		}
		Toast.makeText(this, "Sniff destroyed", Toast.LENGTH_SHORT).show();
		running=false;
		super.onDestroy();
		
	}
	
	public static boolean isRunning(){
		return running;
	}

}
