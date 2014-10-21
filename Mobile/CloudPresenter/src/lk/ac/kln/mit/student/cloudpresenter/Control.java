package lk.ac.kln.mit.student.cloudpresenter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Locale;

import lk.ac.kln.mit.student.cloudpresenter.controller.DeviceManager;
import lk.ac.kln.mit.student.cloudpresenter.model.Device;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.graphics.AvoidXfermode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Control extends FragmentActivity implements ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	Device device;
	boolean detected= false;
	private sendMouseControl mc = null;
	private float StartPosX= 0;
	private float StartPosY= 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		Intent i = getIntent();
		Bundle bun = i.getExtras();

		this.device = DeviceManager.getDevice(bun.getString("device"));
		this.setTitle(this.getTitle()+":"+this.device.getDeviceName());
		
		/*SendControlString obh = new SendControlString(Control.this,
				device.getIpAddress()) ;
		obh.execute("Request Control");*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.control, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
		if(!detected && tab.getPosition()==1){
			Toast.makeText(Control.this, "Mouse", Toast.LENGTH_SHORT).show();
			GestureOverlayView gv = (GestureOverlayView) findViewById(R.id.mousePad);
			gv.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					
					switch(event.getAction()) {
					case (MotionEvent.ACTION_DOWN) :
						StartPosX = event.getAxisValue(MotionEvent.AXIS_X);
						StartPosY = event.getAxisValue(MotionEvent.AXIS_Y);
			            mc = new sendMouseControl(Control.this, StartPosX,StartPosY, device.getIpAddress());
						mc.execute(0f,StartPosX,StartPosY);
			            return true;
			        case (MotionEvent.ACTION_MOVE) :
			        	mc = new sendMouseControl(Control.this, StartPosX,StartPosY, device.getIpAddress());
					   	mc.execute(1f,event.getAxisValue(MotionEvent.AXIS_X),event.getAxisValue(MotionEvent.AXIS_Y));
			        	return true;
			        case (MotionEvent.ACTION_UP) :
			        	mc = new sendMouseControl(Control.this, StartPosX,StartPosY, device.getIpAddress());
					   	mc.execute(2f,event.getAxisValue(MotionEvent.AXIS_X),event.getAxisValue(MotionEvent.AXIS_Y));
			        	return true;
			        case (MotionEvent.ACTION_CANCEL) :
			        	mc = new sendMouseControl(Control.this, StartPosX,StartPosY, device.getIpAddress());
					   	mc.execute(3f,event.getAxisValue(MotionEvent.AXIS_X),event.getAxisValue(MotionEvent.AXIS_Y));
			        	return true;
			       default :
			        	return false;
				}
			}});
			detected =true;
		}
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = null;
			if (Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER))
					.equals("1")) {
				rootView = inflater.inflate(R.layout.fragment_control_dummy,
						container, false);

			} else if (Integer.toString(
					getArguments().getInt(ARG_SECTION_NUMBER)).equals("2")) {
				rootView = inflater.inflate(R.layout.fragment_control_mouse,
						container, false);

			}
			return rootView;

		}
	}

	public void keyPress(View view) {
		String command=((Button) view).getText().toString();
		if(command.equals("Spc")){
			command=" ";
		} else if(command.equals("Ent")){
			command ="\r";
		}
		else if(command.equals("bksp")){
			command ="\b";
		}
		//int keycode = KeyPres
		SendControlString obh = new SendControlString(Control.this,
				device.getIpAddress()) ;
		obh.execute(command);
		
		ToggleButton shift = (ToggleButton) findViewById(R.id.btnShift);
		

	}

	public class SendControlString extends AsyncTask<String, String, String> {

		Context context;
		String hostip;
		InetAddress host;
		boolean stop = false;


		public SendControlString(Context context, String ip) {
			// TODO Auto-generated constructor stub
			this.context = context;
			this.hostip = ip;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
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
		}

	}
	
	public void mouseClick(View view){
		String command=((Button) view).getText().toString();
		SendControlString obh = new SendControlString(Control.this,
				device.getIpAddress()) ;
		obh.execute("Mouse:"+command);
	}
	
	
	
	public static class sendMouseControl extends AsyncTask<Float, Void, Boolean>{

		private static float startPosX =0;
		private static float startPosY =0;
		private Context context;
		private static boolean touching =false;
		String hostip;
		InetAddress host;
		
		public sendMouseControl(Context context, float x, float y, String ip){
			this.context=context;
			startPosX=x;
			startPosY=y;
			hostip=ip;
		}
		@Override
		protected Boolean doInBackground(Float... params) {
			try {
				InetAddress address = InetAddress.getByName(hostip);
				Socket theSocket = new Socket(address, 6065);
				DataOutputStream dOut = new DataOutputStream(theSocket.getOutputStream());
				float accelX = (params[1]-startPosX)/startPosX	;
				float accelY = (params[2]-startPosY)/startPosY;	
				int status = params[0].intValue();
				String str = "";
				switch(status){
				case (0) :
					str = "Open";
					break;
				case (1) :
					str = "Move";
					break;
				default :
					str = "Close";
				}
			
				dOut.writeUTF("Mouse:"+str+":"+accelX+":"+accelY+":");
				dOut.close();
				theSocket.close();
				return true;
		} catch (UnknownHostException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			return false;
		}
				
	}
	
	

}
