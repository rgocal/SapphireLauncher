package com.NxIndustries.Sapphire.gesture;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.NxIndustries.Sapphire.R;
import com.NxIndustries.Sapphire.settings.SettingsProvider;
import com.github.clans.fab.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AddGesture extends AppCompatActivity implements OnGesturePerformedListener, OnClickListener {

    private static final int PERMISSIONS_REQUEST_WRITE = 1;
    GestureOverlayView gov = null;

    FloatingActionButton btnSelectAction, imgClear;
    Button btnSave;


	Gesture userGesture = null;
	CheckBox chkEnableConfirmation = null;
	GestureLibrary gestureLib = null;
	int action = -4;
	String option = null;
	String desc = "";
	String appPath = "";
	boolean edit = false;
	int gestureId = -1;
    private int color;

    protected void onCreate(Bundle savedInstanceState) {
        edit = false;
        G2LDataBaseHandler dbh;
        new G2LDataBaseHandler(getApplicationContext(), null, null, 1);

        super.onCreate(savedInstanceState);
        {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSIONS_REQUEST_WRITE);
                }
            }
            setContentView(R.layout.create_gesture);

            Window window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.accent));

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            AppCompatActivity activity = this;
            activity.setSupportActionBar(toolbar);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setHomeButtonEnabled(true);
            activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_done_black_24dp);
            activity.getSupportActionBar().setTitle("Draw Your Sketch");
            activity.getSupportActionBar().setSubtitle("Create a letter or pattern");

            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
            toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

            btnSave = (Button) findViewById(R.id.btnSave);

            gov = (GestureOverlayView) findViewById(R.id.gestureOverlayView1);
            gov.addOnGesturePerformedListener(this);
            dbh = new G2LDataBaseHandler(getApplicationContext(), null, null, 1);
            try {
                if (dbh.getSettings("enable_multiple_strokes").equals("true")) {
                    gov.setGestureStrokeType(GestureOverlayView.GESTURE_STROKE_TYPE_MULTIPLE);
                } else {
                    gov.setGestureStrokeType(GestureOverlayView.GESTURE_STROKE_TYPE_SINGLE);
                }
            } catch (Exception E) {
                E.printStackTrace();
            }
            try {

                color = SettingsProvider.getInt(this, SettingsProvider.KEY_GESTURE_BACKGROUND, R.color.blue);
                dbh.close();
            } catch (Exception e) {
            }
            gov.setGestureColor(color);

            btnSelectAction = (FloatingActionButton) findViewById(R.id.idBtnSelectAction);
            btnSelectAction.setOnClickListener(this);
            try {
                imgClear = (FloatingActionButton) findViewById(R.id.idClear);
                imgClear.setOnClickListener(this);
            } catch (Exception e) {
            }
            try {
                chkEnableConfirmation = (CheckBox) findViewById(R.id.idConfirmationBeforeLaunch);
            } catch (Exception e) {

            }


            btnSave.setOnClickListener(this);

                    appPath = Environment.getExternalStorageDirectory() + "/.g2l/";
                    File appDir = new File(appPath);
                    if (!(appDir.exists() && appDir.isDirectory())) {
                        appDir.mkdir();
                    }

                    File storeFile = new File(appPath + "/gestures");
                    if (!storeFile.exists()) {
                        try {
                            storeFile.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    gestureLib = GestureLibraries.fromFile(storeFile);
                    gestureLib.load();
                    try {
                        Bundle b = getIntent().getExtras();
                        if (b == null) {
                            return;
                        } else {
                            edit = true;
                        }
                        int value = -1;
                        value = b.getInt("id");
                        if (value != -1) {
                            gestureId = value;
                            ArrayList<Gesture> p = gestureLib.getGestures("" + value);
                            if (p.size() > 0) {
                                userGesture = p.get(0);

                                try {
                                    int WIDTH, HEIGHT;
                                    WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                                    Display display = wm.getDefaultDisplay();
                                    Point size = new Point();
                                    if (android.os.Build.VERSION.SDK_INT >= 13) {
                                        display.getSize(size);
                                        WIDTH = size.x;
                                        HEIGHT = size.y;
                                    } else {
                                        WIDTH = display.getWidth();
                                        HEIGHT = display.getHeight();
                                    }

                                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                        int tmp = WIDTH;
                                        WIDTH = HEIGHT;
                                        HEIGHT = tmp;
                                    }
                                    if (android.os.Build.VERSION.SDK_INT >= 11) {
                                        gov.setLeft(-WIDTH);
                                        gov.setTop(-WIDTH);
                                    }

                                } catch (Exception E) {
                                    if (android.os.Build.VERSION.SDK_INT >= 11) {
                                        try {
                                            gov.setLeft(getResources().getDimensionPixelOffset(R.dimen.gesture_offset));
                                            gov.setTop(getResources().getDimensionPixelOffset(R.dimen.gesture_offset));
                                        } catch (Exception e) {

                                        }
                                    }
                                }

                                onResume();
                                dbh = new G2LDataBaseHandler(getApplicationContext(), null, null, 1);
                                Cursor c = dbh.getGestures(value);
                                if (c.moveToFirst()) {
                                    action = c.getInt(1);
                                    option = c.getString(2);
                                    desc = c.getString(3);
                                    if (c.getString(5).equals("true")) {
                                        chkEnableConfirmation.setChecked(true);
                                    } else {
                                        chkEnableConfirmation.setChecked(false);
                                    }
                                }

                            }
                        }
                    } catch (Exception e) {
                        Log.d("EXECPTION+++++", e.toString());
                }
            }

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case 2909: {
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Log.e("Permission", "Granted");
				} else {
					Log.e("Permission", "Denied");
				}
				return;
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(userGesture != null){
			gov.setGesture(userGesture);
		}
	}

	@Override
	public void onGesturePerformed(GestureOverlayView arg0, Gesture gesture) {
		userGesture = gesture;
		if(userGesture!=null){
			gov.setGesture(userGesture);
			gov.clear(false);
		}
	}
	@Override
	public void onClick(View view) {
        Log.d("SAVE", "INSIDE ON CLICK");
        if (view == btnSave) {
            Log.d("SAVE", "INSIDE SAVE IF");
            if (edit) {
                deleteGesture(gestureId);
            }
            saveGesture();
            return;
        } else if (view == btnSelectAction) {
            userGesture = gov.getGesture();
            Intent i = new Intent(this, SelectAction.class);
            startActivityForResult(i, 0);
            return;
        } else if (imgClear != null && view == imgClear) {
            clearUserGesture();
		}
	}

	private void clearUserGesture(){
		userGesture = null;
		gov.cancelClearAnimation();
		gov.clear(true);
	}


	private void deleteGesture(int position) {
		try{
			G2LDataBaseHandler dbh = new G2LDataBaseHandler(getApplicationContext(), null, null, 1);
			gestureLib.removeEntry(""+position);
			gestureLib.save();
			dbh.deleteGesture(position);
			dbh.close();
		}catch(Exception E){
			E.printStackTrace();
		}


	}

	public void onActivityResult (int requestCode, int resultCode, Intent data){

		if(data!=null){
			action = data.getIntExtra("action", -4);
			option = data.getStringExtra("option");
			desc = data.getStringExtra("desc");
		}else{
			action = -4;
		}
	}
	public void saveGesture(){
		Log.d("SAVE", "INSIDE SAVE");
		userGesture = gov.getGesture();
		if(userGesture != null && action != -4){
			int orientation = gov.getOrientation();
			G2LDataBaseHandler dh = new G2LDataBaseHandler(getApplicationContext(), "name", null, 1);
			int id = dh.getNextId();
			dh.addGestures(id, action, option, desc, orientation,chkEnableConfirmation.isChecked());
			gestureLib.addGesture(""+id, userGesture);
			gestureLib.save();

			try {
				FileOutputStream out = new FileOutputStream(appPath + id + ".png");
				userGesture.toBitmap(50, 30, 1, color).compress(CompressFormat.PNG, 90, out);
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			gov.setFadeEnabled(true);
			gov.setFadeOffset(1);
			gov.clearAnimation();
			gov.clear(true);
			gov.setFadeOffset(100000000);
			userGesture = null;
			action = -4;
			finish();
		}else if(userGesture == null){
			Toast.makeText(this, "Draw a valid gesture before saving", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(this, "Select an action before saving", Toast.LENGTH_SHORT).show();
		}

	}

}