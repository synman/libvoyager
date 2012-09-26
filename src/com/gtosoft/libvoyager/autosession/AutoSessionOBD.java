package com.gtosoft.libvoyager.autosession;

import android.util.Log;

import com.gtosoft.libvoyager.session.HybridSession;
import com.gtosoft.libvoyager.util.EventCallback;
import com.gtosoft.libvoyager.util.GeneralStats;
import com.gtosoft.libvoyager.util.RoutineScan;

/**
 * Auto sessions are something I dreamed up today at the book store. 
 * They will run at a higher level than the traditional OBD/moni sessions. 
 * They will provide a fully automated presence. In other words, if you instantiate an OBD Auto-Session, it will 
 * start up, configure stuff, and work all by itself, without any user intervention. 
 * 
 * The purpose of these is to facilitate services using libvoyager to run on their own. 
 * Auto Sessions will allow outside forces to change their state. For example a TCP/IP adapter. 
 * 
 * @author Brad Hein / GTOSoft LLC
 *
 */

public class AutoSessionOBD {

	GeneralStats	mgStats = new GeneralStats();
	boolean DEBUG = false;
	HybridSession 	hs;
	EventCallback  	mParentOOBMessageHandler = null;
	RoutineScan     rs;
	
	public AutoSessionOBD(HybridSession hsession,EventCallback newParentOOBMessageHandler) {
		hs = hsession;
		mParentOOBMessageHandler = newParentOOBMessageHandler;

		// set session type to OBD. That's all we'll be doing here. 
		hs.setActiveSession(HybridSession.SESSION_TYPE_OBD2);

		rs = new RoutineScan(hs.getOBDSession(), hs.getPIDDecoder());
		
		rs.addDPN("RPM");
		rs.addDPN("SPEED");
		
		msg ("AutoSessionOBD initialized.");
	}

	public GeneralStats getStats () {
		// TODO: add any last-minute stats here. 
		return mgStats;
	}
	
	
	private void sendOOBMessage (String dataName, String dataValue) {
		if (mParentOOBMessageHandler == null)
			return;

		mParentOOBMessageHandler.onOOBDataArrived(dataName, dataValue);
	}

	private void msg (String m) {
		Log.d("AutoSessionOBD",m);
	}
	
	public void shutdown () {
		rs.removeAllDPNs();
	}
	
}
