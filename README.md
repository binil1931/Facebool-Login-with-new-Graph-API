# Facebook Login Using Graph API

  The sample app for Facebook login using Graph API. After login you can access all the user data , such as his name,email,location,language ect...
  
## Steps

   1. Create a Facebook App in <a href="https://developers.facebook.com">Facebook Developer Console</a>.
   2. From there you will get App Id.
   3. Open String.xml 
   ```
    <string name="app_id">App Id</string>
   ```
   4. In Android Manifest add permission for Internet
     ```
       <uses-permission android:name="android.permission.INTERNET"/>
     ```
   5. Add Login Activity in Manifest
      
	  ```
    <activity
            android:name="com.facebook.LoginActivity"
            android:label="@string/app_name"
            android:theme="@android:style/Theme.Translucent.NoTitleBar" />
        
        <meta-data android:name="com.facebook.sdk.ApplicationId"
            android:value="@string/app_id" />
			
	  ```
