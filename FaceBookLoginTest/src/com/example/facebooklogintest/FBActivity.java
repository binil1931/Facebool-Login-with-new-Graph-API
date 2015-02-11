package com.example.facebooklogintest;

import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;

public class FBActivity extends FragmentActivity {

	private LoginButton loginBtn;
	private Button postImageBtn;
	private Button updateStatusBtn;

	private TextView userName;
	TextView user_email;

	private UiLifecycleHelper uiHelper;

	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");

	private static String message = "Sample status posted from android app";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		uiHelper = new UiLifecycleHelper(this, statusCallback);
		uiHelper.onCreate(savedInstanceState);

		setContentView(R.layout.activity_facebook);

		userName = (TextView) findViewById(R.id.user_name);
		user_email = (TextView) findViewById(R.id.user_email);
		
		
		
		loginBtn = (LoginButton) findViewById(R.id.fb_login_button);
		loginBtn.setUserInfoChangedCallback(new UserInfoChangedCallback() {
			@Override
			public void onUserInfoFetched(GraphUser user) {
				if (user != null) {
					
					
					Log.e("tag", "getId " +user.getId());
					Log.e("tag", "getBirthday " +user.getBirthday());
					Log.e("tag", "getUsername " +user.getUsername());
					Log.e("tag", "getLink " +user.getLink());
					Log.e("tag", "getLocation " +user.getLocation());
					Log.e("tag", "getInnerJSONObject " +user.getInnerJSONObject());
					
					JSONObject profile;
					try {
						profile = new JSONObject(user.getInnerJSONObject().toString());
					
						String email = profile.getString("email");
						user_email.setText(email);
						userName.setText("Hello, " + user.getName());
						Log.e("tag", "email " +email);
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					

					// getting email of the user
					
				} else {
					userName.setText("You are not logged");
				}
			}
		});
		
	

		postImageBtn = (Button) findViewById(R.id.post_image);
		postImageBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				postImage();
			}
		});

		updateStatusBtn = (Button) findViewById(R.id.update_status);
		updateStatusBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		buttonsEnabled(false);
	}

	private Session.StatusCallback statusCallback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			if (state.isOpened()) {
				buttonsEnabled(true);
				Log.d("FacebookSampleActivity", "Facebook session opened");
			} else if (state.isClosed()) {
				buttonsEnabled(false);
				Log.d("FacebookSampleActivity", "Facebook session closed");
			}
		}
	};

	public void buttonsEnabled(boolean isEnabled) {
		postImageBtn.setEnabled(isEnabled);
		updateStatusBtn.setEnabled(isEnabled);
	}

	public void postImage() {
		if (checkPermissions()) {
			Log.e("tag ", "postImage ");
			Bitmap img = BitmapFactory.decodeResource(getResources(),
					R.drawable.ic_launcher);
			Request uploadRequest = Request.newUploadPhotoRequest(
					Session.getActiveSession(), img, new Request.Callback() {
						@Override
						public void onCompleted(Response response) {
							Toast.makeText(FBActivity.this,
									"Photo uploaded successfully",
									Toast.LENGTH_LONG).show();
						}
					});
			uploadRequest.executeAsync();
		} else {
			requestPermissions();
		}
	}

	public void postStatusMessage() {
		if (checkPermissions()) {
			Request request = Request.newStatusUpdateRequest(
					Session.getActiveSession(), message,
					new Request.Callback() {
						@Override
						public void onCompleted(Response response) {
							if (response.getError() == null)
								Toast.makeText(FBActivity.this,
										"Status updated successfully",
										Toast.LENGTH_LONG).show();
						}
					});
			request.executeAsync();
		} else {
			requestPermissions();
		}
	}

	public boolean checkPermissions() {
		Session s = Session.getActiveSession();
		if (s != null) {
			return s.getPermissions().contains("publish_actions");
		} else
			return false;
	}

	public void requestPermissions() {
		Session s = Session.getActiveSession();
		if (s != null)
			s.requestNewPublishPermissions(new Session.NewPermissionsRequest(
					this, PERMISSIONS));
	}

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
		buttonsEnabled(Session.getActiveSession().isOpened());
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onSaveInstanceState(Bundle savedState) {
		super.onSaveInstanceState(savedState);
		uiHelper.onSaveInstanceState(savedState);
	}

}