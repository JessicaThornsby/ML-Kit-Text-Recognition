package com.jessicathornsby.myapplication;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.Manifest;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;

public class BaseActivity extends AppCompatActivity {
	public static final int WRITE_STORAGE = 100;
	public static final int SELECT_PHOTO = 102;
	public static final String ACTION_BAR_TITLE = "action_bar_title";
	public File photo;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setTitle(getIntent().getStringExtra(ACTION_BAR_TITLE));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.my_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.gallery_action:
				checkPermission(WRITE_STORAGE);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case WRITE_STORAGE:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					selectPicture();
				} else {
					requestPermission(this, requestCode, R.string.permission_request);
				}
				break;

		}
	}

	public static void requestPermission(final Activity activity, final int requestCode, int msg) {
		AlertDialog.Builder alert = new AlertDialog.Builder(activity);
		alert.setMessage(msg);
		alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				dialogInterface.dismiss();
				Intent permissonIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
				permissonIntent.setData(Uri.parse("package:" + activity.getPackageName()));
				activity.startActivityForResult(permissonIntent, requestCode);
			}
		});
		alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				dialogInterface.dismiss();
			}
		});
		alert.setCancelable(false);
		alert.show();
	}


	public void checkPermission(int requestCode) {
		switch (requestCode) {
			case WRITE_STORAGE:
				int hasWriteExternalStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
				if (hasWriteExternalStoragePermission == PackageManager.PERMISSION_GRANTED) {
					selectPicture();
				} else {
					ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
				}
				break;

		}
	}


	private void selectPicture() {
		photo = MyHelper.createTempFile(photo);
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, SELECT_PHOTO);
	}


	}
