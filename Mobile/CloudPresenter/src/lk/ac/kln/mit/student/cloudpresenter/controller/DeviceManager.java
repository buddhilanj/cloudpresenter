package lk.ac.kln.mit.student.cloudpresenter.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import android.util.Log;

import lk.ac.kln.mit.student.cloudpresenter.model.Device;

public class DeviceManager {

	private static LinkedList<Device> devices = new LinkedList<Device>();
	private static ArrayList<String> devicelist = new ArrayList<String>();

	public static Device getDevice(String name) {
		for (Device device : devices) {
			if (name.equals(device.getDeviceName())) {
				return device;
			}
		}
		return null;
	}

	public synchronized static boolean removeFromList(Device device) {
		boolean removed = devices.remove(device);
		if (removed) {
			devicelist.remove(device.getDeviceName());
		}
		return removed;
	}

	public synchronized static String[] getDeviceList() {
		String[] array = new String[devicelist.size()];
		for (int i = 0; i < devicelist.size(); i++) {
			array[i] = devicelist.get(i);
		}

		return array;
	}

	public synchronized static void detectPresence(String data, String ipaddress) {
		String[] array = data.split(":");
		if (array.length > 2
				&& (array[0].equals("device") || array[0].equals("remote"))) {
			boolean present = false;
			Device detected = null;
			for (Device device : devices) {
				if (device.getIpAddress().equals(array[2])) {
					present = true;
					detected = device;
				}
			}
			if (!present) {
				Device d = new Device(ipaddress, array[1], array[0]);
				devices.add(d);
				devicelist.add(d.getDeviceName());
			} else {
				detected.setCreated(new Date().getTime());
				Log.i("Sniffed", array[1]);
			}
		}
	}
}
