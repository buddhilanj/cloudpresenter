package lk.ac.kln.mit.student.cloudpresenter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import lk.ac.kln.mit.student.cloudpresenter.controller.DeviceManager;
import lk.ac.kln.mit.student.cloudpresenter.util.SystemUiHider;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class DetectDevice extends Activity {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	private ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_detect_device);

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mControlsHeight == 0) {
								mControlsHeight = controlsView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
							controlsView
									.animate()
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						}

						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		findViewById(R.id.scan_button).setOnTouchListener(
				mDelayHideTouchListener);
		Toast.makeText(getApplicationContext(), "Starting Intent Service",
				Toast.LENGTH_SHORT).show();
		if (!BroadcastPresence.isRunning()) {
			Intent broadcaster = new Intent(DetectDevice.this,
					BroadcastPresence.class);
			startService(broadcaster);
		}
		if (!SniffPresence.isRunning()) {
			Intent sniffer = new Intent(DetectDevice.this, SniffPresence.class);
			startService(sniffer);
		}
		list = (ListView) findViewById(R.id.deviceListMain);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent i = new Intent(DetectDevice.this, Control.class);
				Toast.makeText(getApplicationContext(),
						((TextView) arg1).getText(), Toast.LENGTH_SHORT).show();
				i.putExtra("device", ((TextView) arg1).getText());
				SendRequestString scs = new SendRequestString(DetectDevice.this, DeviceManager.getDevice(((TextView) arg1).getText().toString()).getIpAddress(), i);
				scs.execute("Request Control");
			}
		});

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(1000);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}

	public void scandevices(View view) {
		if (SniffPresence.isRunning()) {
			Intent sniffer = new Intent(DetectDevice.this, SniffPresence.class);
			stopService(sniffer);
		}
		if (!SniffPresence.isRunning()) {
			Intent sniffer = new Intent(DetectDevice.this, SniffPresence.class);
			startService(sniffer);
		}

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					list.post(new Runnable() {

						@Override
						public void run() {
							ArrayAdapter<String> adapter = new ArrayAdapter<String>(
									DetectDevice.this,
									android.R.layout.simple_list_item_1,
									android.R.id.text1, DeviceManager
											.getDeviceList()) {
								@Override
								public View getView(int position,
										View convertView, ViewGroup parent) {
									View view = super.getView(position,
											convertView, parent);

									TextView textView = (TextView) view
											.findViewById(android.R.id.text1);

									/* YOUR CHOICE OF COLOR */
									textView.setTextColor(Color.BLACK);

									return view;
								}
							};
							list.setAdapter(adapter);

						}
					});
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();

	}
	
	public class SendRequestString extends AsyncTask<String, String, String> {

		Context context;
		String hostip;
		InetAddress host;
		boolean stop = false;
		ProgressDialog pgd;
		Intent control;

		public SendRequestString(Context context, String ip, Intent intent) {
			// TODO Auto-generated constructor stub
			this.context = context;
			this.hostip = ip;
			control = intent;
			pgd = new ProgressDialog(context);
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pgd.setMessage("Waiting until accpetance");
			pgd.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				stop = false;
				InetAddress address = InetAddress.getByName(hostip);
				Socket theSocket = new Socket(address, 6064);
				DataOutputStream dOut = new DataOutputStream(theSocket.getOutputStream());
				dOut.writeUTF(params[0]);
				if(params[0].equals("Request Control")){
					DataInputStream dIn = new DataInputStream(theSocket.getInputStream());
					String decision = dIn.readUTF();
					if(decision.equals("Accepted")){
						stop =false;
					}
					else if(decision.equals("Rejected")){
						stop =true;
					}
					dIn.close();
				}
				dOut.close();
			
				theSocket.close();
			} catch (UnknownHostException ex) {
				ex.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pgd.dismiss();
			if(!stop){
				startActivity(control);
			}
			else{
				final AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(DetectDevice.this);
                dlgAlert.setMessage("Remote request was rejected");
                dlgAlert.setTitle("Rejected!");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(false);
                dlgAlert.create().show();
                dlgAlert.setPositiveButton("Ok",
                   new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int which) {
                         //dismiss the dialog 
                           
                       }
                   });
			}
		}

	}

}
