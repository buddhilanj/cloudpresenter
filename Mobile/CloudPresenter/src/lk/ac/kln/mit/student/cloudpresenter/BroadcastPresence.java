package lk.ac.kln.mit.student.cloudpresenter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BroadcastPresence extends IntentService {

	private InetAddress localdevice;
	private int port = 4000;
	private InetAddress multicastgroup;
	private MulticastSocket ms;
	private boolean alive;
	private static boolean running;

	public BroadcastPresence() {
		super("BroadcastPresence");
		// TODO Auto-generated constructor stub
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		running=true;
		Toast.makeText(this, "broadcast starting", Toast.LENGTH_SHORT).show();
		return super.onStartCommand(intent, flags, startId);
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
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		try {

			NetworkInterface np = NetworkInterface.getByName("wlan0");
			Enumeration<InetAddress> enumIpAddr = np.getInetAddresses();
			while (enumIpAddr.hasMoreElements()) {
				InetAddress iptemp = enumIpAddr.nextElement();
				if (!iptemp.isLoopbackAddress()	&& (iptemp instanceof Inet4Address)) {
					localdevice = iptemp;
				}
				
			}
			multicastgroup = InetAddress.getByName("224.0.0.1");
			ms = new MulticastSocket();
			ms.setTimeToLive(16);
			ms.joinGroup(multicastgroup);
			//ms.bind(localAddr);
			alive = true;

			while (!alive) {
				Thread.sleep(5000);
			}
			while (alive) {
				String message = "remote:" + android.os.Build.MODEL + ":"
						+ localdevice.getHostAddress() + ":";
				byte[] data = message.getBytes();
				Log.i("CloudPresenter",ms.toString());
				DatagramPacket dp = new DatagramPacket(data, data.length,
						multicastgroup, port);
				ms.send(dp);
				
				Thread.sleep(5000);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("InterruptedException", e.getMessage());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("UnknownHostException", e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("IOException", e.getMessage());
		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "broadcast destroyed", Toast.LENGTH_SHORT).show();
		running=false;
		super.onDestroy();
	}
	public static boolean isRunning(){
		return running;
	}

}
