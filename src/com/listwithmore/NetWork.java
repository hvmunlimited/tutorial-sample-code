package com.listwithmore;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;

public class NetWork {
	NetworkInfo ActiveNetworkInfo;
	NetWork(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
		ActiveNetworkInfo = connMgr.getActiveNetworkInfo();

		
	}
	
	public boolean isOnline(){
		
		if (ActiveNetworkInfo != null && ActiveNetworkInfo.isConnected()) {
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isWifi() {
		
		if (ActiveNetworkInfo.getType() == 1) {
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isMobile() {
		
		if (ActiveNetworkInfo.getType() == 0) {
			return true;
		}
		else{
			return false;
		}
	}
	
	public long trafficInBytes() {
		long recieved = TrafficStats.getMobileRxBytes();
		return recieved;
		
	}
	
	public long trafficOutBytes() {
		long sent = TrafficStats.getMobileTxBytes();
		return sent;
		
	}

}
