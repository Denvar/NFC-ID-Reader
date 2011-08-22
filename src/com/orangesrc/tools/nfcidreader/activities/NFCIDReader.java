package com.orangesrc.tools.nfcidreader.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.TextView;

import com.orangesrc.tools.nfcidreader.R;
import com.orangesrc.tools.nfcidreader.Util.Util;

public class NFCIDReader extends Activity {
	
	private PendingIntent pendingIntent;
	private IntentFilter[] intentFiltersArray;
	private String[][] techListsArray;
	private NfcAdapter mAdapter;
	private TextView tvNFCID;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfcidreader);
        
        tvNFCID = (TextView) findViewById(R.id.nfcidreader_text_id);
        
		getData(getIntent());
        
        if(Build.VERSION.SDK_INT >= 10) {
			pendingIntent = PendingIntent.getActivity(
					this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
			
	        mAdapter = NfcAdapter.getDefaultAdapter(this); 
	        
	        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
		    try {
		    	ndef.addDataType("*/*");    
		    } catch(MalformedMimeTypeException e) {
		        throw new RuntimeException("Malformed Mime Type", e);
		    }
		    intentFiltersArray = new IntentFilter[] { ndef };
		     
		    techListsArray = new String[][] { new String[] { MifareClassic.class.getName(), NfcA.class.getName(), NdefFormatable.class.getName() } };	  
		}
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	
    	// NFC Foreground
		if(Build.VERSION.SDK_INT >= 10) {
			enableForegroundDespatch(this);
		}
    }
    
    @Override
	protected void onPause() {
		super.onPause();
		
		// NFC Foreground
		if(Build.VERSION.SDK_INT >= 10) {
			disableForegroundDespatch(this);
		}		
	}
    
    @Override
	public void onNewIntent(Intent intent) {
    	getData(intent);
	}
    
	private void getData(Intent intent) {
		// NFC Background
	    String action = intent.getAction();

		if(Build.VERSION.SDK_INT >= 10) {
		    if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
		 	    Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		 	    handleNfc(tagFromIntent);	 	    
		    } 	    
		}
	}
	
	public void enableForegroundDespatch(Activity activity) {
		if(Build.VERSION.SDK_INT >= 10) {
			if(mAdapter!=null && mAdapter.isEnabled()) {
				mAdapter.enableForegroundDispatch(activity, pendingIntent, intentFiltersArray, techListsArray);
			}
		}
	}
	
	public void disableForegroundDespatch(Activity activity) {
		if(Build.VERSION.SDK_INT >= 10) {
			if(mAdapter != null && mAdapter.isEnabled()) {
				mAdapter.disableForegroundDispatch(activity);
			}
		}
	}
	
	private void handleNfc(Tag tag) {
		if(Build.VERSION.SDK_INT >= 10) {
		    String TagUID = Util.ConvertbyteArrayToHexString(tag.getId());	

		    tvNFCID.setText(TagUID);
		    
	    	notifySuccessfulScan();	                                                                                                                                             
		}
	}

	private void notifySuccessfulScan() {
		setVolumeControlStream(AudioManager.STREAM_MUSIC); 
		
        MediaPlayer mp = MediaPlayer.create(this, R.raw.scan);
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        
		mp.start();
		v.vibrate(1000);
	}
}