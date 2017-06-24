package com.NxIndustries.Sapphire.gesture;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.NxIndustries.Sapphire.R;

@SuppressLint("NewApi")
public class GestureMainActivity extends PreferenceActivity  implements Runnable{


	Preference layoutGestures[] = new Preference[10000];
	Integer id[] = new Integer[10000];
	G2LDataBaseHandler dbh = null;
	GestureLibrary gestureLib = null;
	ProgressDialog p;
	int layoutIndex = 0;
	boolean unreg = false;
	Context context = null;

	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {

		context = this;
		dbh = new G2LDataBaseHandler(getApplicationContext(), null, null, 1);
		try{
		}catch(Exception E){
		}try{
			dbh.close();
		}catch(Exception E){
		}
		getOverflowMenu();
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.gesture_main);
		registerForContextMenu(getListView());
		p  = ProgressDialog.show(this, "Loading ", "Loading Gesture List....", true);
		Thread t = new Thread(this);
		t.start();
	}


	private void getOverflowMenu() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			java.lang.reflect.Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if(menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		initGestures();
		p.dismiss();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Thread t = new Thread(this);
		t.start();
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
									ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0,1,0,"Edit");
		menu.add(0,2,0,"Delete");
		menu.add(0,3,0,"Cancel");
	}

	@SuppressWarnings("rawtypes")
	private void initGestures(){
		String appPath = Environment.getExternalStorageDirectory() +"/.g2l/";
		File appDir = new File(appPath);
		if(!(appDir.exists() && appDir.isDirectory())){
			appDir.mkdir();
		}
		File storeFile = new File(appPath+"/gestures");
		if(!storeFile.exists()){
			try {
				storeFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		gestureLib = GestureLibraries.fromFile(storeFile);
		gestureLib.load();

		Iterator iter = null;
		try{
			Set<String> gestureNames = gestureLib.getGestureEntries();
			iter = gestureNames.iterator();
		}catch(Exception E){
			return;
		}
		layoutIndex = 0;

		@SuppressWarnings("deprecation")
		PreferenceScreen targetCategory = (PreferenceScreen)findPreference("prefScreen");
		targetCategory.removeAll();
		G2LDataBaseHandler dbh = new G2LDataBaseHandler(getApplicationContext(), "name",null, 1);

		while (iter.hasNext()) {
			String gestureName = iter.next().toString();
			File imagePath = new File(appPath+"/"+gestureName+".png");
			Bitmap bitmap;
			if(imagePath.exists()){
				bitmap = BitmapFactory.decodeFile(appPath+"/"+gestureName+".png");
			}else{
				bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_home);
			}

			String actionName = dbh.getActionOption(Integer.parseInt(gestureName));
			if(actionName == ""){
				dbh.deleteGesture(Integer.parseInt(gestureName));
				continue;
			}
			layoutGestures[layoutIndex] = new Preference(this);
			if(android.os.Build.VERSION.SDK_INT >= 11) {
				layoutGestures[layoutIndex].setIcon(new BitmapDrawable(getResources(),bitmap));
			}else{
			}
			layoutGestures[layoutIndex].setTitle(actionName);
			id[layoutIndex] = Integer.parseInt(gestureName);
			targetCategory.addPreference(layoutGestures[layoutIndex]);
			layoutIndex++;
		}
		try{
			dbh.close();
		}catch(Exception E){

		}
		if(layoutIndex == 0){
			layoutGestures[layoutIndex] = new Preference(this);
			layoutGestures[layoutIndex].setTitle("No Sketches");
			layoutGestures[layoutIndex].setSummary("You havn't made any sketches yet. Try making a simple line or letter to launch an action in create a Sketch!");

			targetCategory.addPreference(layoutGestures[layoutIndex]);
			unregisterForContextMenu(getListView());
			unreg = true;
		}else if(unreg){
			registerForContextMenu(getListView());
		}
	}


	public boolean onContextItemSelected(MenuItem item) {
		int menuId = item.getItemId();
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch(menuId){
			case 1: editGesture(info.position);break;
			case 2: AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Confirmation");
				builder.setMessage("Do you really want to delete this ?");
				builder.setPositiveButton("Yes", new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						deleteGesture(info.position);
					}
				}).setNegativeButton("No", null);
				builder.show();
		}
		return true;
	}

	private void editGesture(int position) {
		Intent i=new Intent(GestureMainActivity.this, AddGesture.class);
		Bundle b = new Bundle();
		Log.d("INFO.POSITION",""+id[position]);
		b.putInt("id", id[position]);
		i.putExtras(b);
		startActivityForResult(i, 0);
	}

	private boolean deleteGesture(int position) {
		try{
			gestureLib.removeEntry(""+id[position]);
			gestureLib.save();
			dbh.deleteGesture(id[position]);
			resortFrom(position);
			dbh.close();
		}catch(Exception E){
			E.printStackTrace();
		}
		PreferenceScreen targetCategory = (PreferenceScreen)findPreference("prefScreen");
		targetCategory.removePreference(targetCategory.getPreference(position));
		return false;

	}

	private void resortFrom(int position) {
		for(int i = position; i < id.length-1; i++){
			id[i] = id[i+1];
		}
		layoutIndex --;
		if(position == 0 && layoutIndex == 0){
			PreferenceScreen targetCategory = (PreferenceScreen)findPreference("prefScreen");
			layoutGestures[layoutIndex] = new Preference(this);
			layoutGestures[layoutIndex].setTitle("Sketches");
			layoutGestures[layoutIndex].setSummary("0 detected");
			targetCategory.addPreference(layoutGestures[layoutIndex]);
			unregisterForContextMenu(getListView());
			unreg = true;

		}
	}
}