package com.example.mars.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mars.R;
import com.example.mars.data.CourseContentData;
import com.example.mars.data.CourseData;
import com.example.mars.data.UserData;
import com.example.mars.database.DatabaseHandler;
import com.example.mars.util.Constants;
import com.example.mars.util.MyPreferences;
import com.example.mars.util.Validation;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class LoginActivity extends AppCompatActivity {
    Button btn_login;
    TextView tv_signup;
    EditText et_name, et_password;
    FirebaseAuth firebaseAuth;
    RelativeLayout loading_view;
    boolean iStatus;
    CallbackManager callbackManager;

    LoginButton loginButton;
    GoogleSignInClient mGoogleSignInClient;
    MyPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new MyPreferences(this);

        if (preferences.getBool(MyPreferences.IS_LOGIN)) {
            startCustomerActivity();
        }
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        tv_signup = findViewById(R.id.tv_signup);
        et_name = findViewById(R.id.et_name);
        et_password = findViewById(R.id.et_password);
        loading_view = findViewById(R.id.loading_view);
//Fb login
        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email"));
        callbackManager = CallbackManager.Factory.create();



        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("getAccessToken", loginResult.getAccessToken().getToken());
                        getFbInfo(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.v("LoginActivity", "cancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.v("LoginActivity", exception.getCause().toString());
                    }
                });
//Gmail LOgin

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        firebaseAuth = FirebaseAuth.getInstance();
        tv_signup.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignupActivity.class)));
        btn_login.setOnClickListener(v -> {
            if (Validation.isEmptyData(et_name)) {
                if (Validation.isValidEmail(et_name)) {
                    if (Validation.isEmptyData(et_password)) {
                        signinUser(et_name.getText().toString());
                    }
                }
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        releaseKeyHash();

    }

    private void startCustomerActivity() {
        Intent intent = new Intent(LoginActivity.this, CourseListActivity.class);
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finishAffinity();

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 102);
    }

    private void getFbInfo(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {
                            Log.d("LOG_TAG", "fb json object: " + object);
                            Log.d("LOG_TAG", "fb graph response: " + response);

                            String id = object.getString("id");
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String image_url = "http://graph.facebook.com/" + id + "/picture?type=large";

                            preferences.saveString(MyPreferences.USER_NAME,first_name+ " "+last_name);
                            String email;
                            if (object.has("email")) {
                                email = object.getString("email");
                            }
                            addCourse();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email,gender,birthday"); // id,first_name,last_name,email,gender,birthday,cover,picture.type(large)
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            preferences.saveString(MyPreferences.USER_NAME,account.getDisplayName());

            addCourse();
            // Signed in successfully, show authenticated UI.
//            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
        }
    }

    private void signinUser(String child) {
        loading_view.setVisibility(View.VISIBLE);
        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        ArrayList<UserData> userList = new ArrayList<>();
        iStatus = false;

        firebaseDatabase.child(Constants.DB_USER_TABLE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    HashMap<String, String> users = new HashMap();
                    users = (HashMap<String, String>) snap.getValue();
                    UserData userData = new UserData(users.get("strImagePath"), users.get("firstName"), users.get("lastName"), users.get("email"), users.get("password"));
                    userList.add(userData);
                    if (userData.getEmail() != null) {
                        if (userData.getEmail().equals(et_name.getText().toString()) && userData.getPassword().equals(et_password.getText().toString())) {
                            iStatus = true;
                            preferences.saveString(MyPreferences.USER_NAME,userData.getFirstName()+" "+userData.getLastName());
                        }
                    }
                }
                if (iStatus) {
                    showToast("Login successfully!");

                    addCourse();

                } else {
                    showToast("Email or Password is incorrect");
                }

                loading_view.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                loading_view.setVisibility(View.GONE);
            }
        });
    }

    private void addCourse() {
        DatabaseHandler handler = new DatabaseHandler(this);
        handler.addCourse(new CourseData(1, "CSS", true, "$90"));
        handler.addCourse(new CourseData(2, "HTML", true, "$200"));
        handler.addCourse(new CourseData(3, "JAVA", true, "$50"));
        handler.addCourse(new CourseData(4, "JAVA SCRIPT", true, "$250"));
        handler.addCourse(new CourseData(5, "MY SQL DATABASE", true, "$150"));
        handler.addCourse(new CourseData(6, "PHP", false, "0"));
//        startActivity(new Intent(LoginActivity.this,CourseListActivity.class));
        addCourseVideo();
    }

    private void addCourseVideo() {
        DatabaseHandler handler = new DatabaseHandler(this);
        handler.addCourseContent(new CourseContentData(1, "LEC-1: introduction to css", "https://www.youtube.com/watch?v=qKoajPPWpmo&list=PLr6-GrHUlVf8JIgLcu3sHigvQjTw_aC9C"));
        handler.addCourseContent(new CourseContentData(1, "LEC-2:  Changing font type, size, color", "https://www.youtube.com/watch?v=UO0ZPL8yMpU&list=PLr6-GrHUlVf8JIgLcu3sHigvQjTw_aC9C&index=2"));
        handler.addCourseContent(new CourseContentData(1, "LEC-3: Multiple selectors and writing rule for more than one element", "https://www.youtube.com/watch?v=JT0gyzbpD2U&list=PLr6-GrHUlVf8JIgLcu3sHigvQjTw_aC9C&index=3"));
        handler.addCourseContent(new CourseContentData(1, "LEC-4: Add a line to header and border property", "https://www.youtube.com/watch?v=hCoMjvtsyPA&list=PLr6-GrHUlVf8JIgLcu3sHigvQjTw_aC9C&index=4"));
        handler.addCourseContent(new CourseContentData(1, "LEC-5:inheritance and overriding ", "https://www.youtube.com/watch?v=Bj2ZCfDPP2A&list=PLr6-GrHUlVf8JIgLcu3sHigvQjTw_aC9C&index=5"));
        handler.addCourseContent(new CourseContentData(1, "LEC-6 using class in css", "https://www.youtube.com/watch?v=u4dLB9NP3IA&list=PLr6-GrHUlVf8JIgLcu3sHigvQjTw_aC9C&index=6"));
        handler.addCourseContent(new CourseContentData(1, "LEC-7:background image property", "https://www.youtube.com/watch?v=9vo8nTQ7s5A&list=PLr6-GrHUlVf8JIgLcu3sHigvQjTw_aC9C&index=21"));
        handler.addCourseContent(new CourseContentData(1, "LEC-8: the id attribute", "https://www.youtube.com/watch?v=ZuuEdXXzG7I&list=PLr6-GrHUlVf8JIgLcu3sHigvQjTw_aC9C&index=22"));
        handler.addCourseContent(new CourseContentData(1, "LEC-9: web colors", "https://www.youtube.com/watch?v=sbtD9AP9W70&list=PLr6-GrHUlVf8JIgLcu3sHigvQjTw_aC9C&index=13"));

        handler.addCourseContent(new CourseContentData(2, "LEC-1: introduction of html", "https://www.youtube.com/watch?v=dD2EISBDjWM&list=PLr6-GrHUlVf_ZNmuQSXdS197Oyr1L9sPB&index=1"));
        handler.addCourseContent(new CourseContentData(2, "LEC-2 creating the first web page ", "https://www.youtube.com/watch?v=-USAeFpVf_A&list=PLr6-GrHUlVf_ZNmuQSXdS197Oyr1L9sPB&index=2"));
        handler.addCourseContent(new CourseContentData(2, "LEC-3 line breaks ,spacing and comments", "https://www.youtube.com/watch?v=i3GE-toQg-o&list=PLr6-GrHUlVf_ZNmuQSXdS197Oyr1L9sPB&index=3"));
        handler.addCourseContent(new CourseContentData(2, "LEC -4 :ordered list and unordered list", "https://www.youtube.com/watch?v=09oErCBjVns&list=PLr6-GrHUlVf_ZNmuQSXdS197Oyr1L9sPB&index=4"));
        handler.addCourseContent(new CourseContentData(2, "LEC-5 : creating table ", "https://www.youtube.com/watch?v=wvR40su_XBM&list=PLr6-GrHUlVf_ZNmuQSXdS197Oyr1L9sPB&index=5"));
        handler.addCourseContent(new CourseContentData(2, "LEC-6 :creating a web link", "https://www.youtube.com/watch?v=U4UHoiK6Oo4&list=PLr6-GrHUlVf_ZNmuQSXdS197Oyr1L9sPB&index=6"));
        handler.addCourseContent(new CourseContentData(2, "LEC-7:creatinga web link with the same page", "https://www.youtube.com/watch?v=bCt2FnyY7AE&list=PLr6-GrHUlVf_ZNmuQSXdS197Oyr1L9sPB&index=7"));
        handler.addCourseContent(new CourseContentData(2, "LEC-8: adding image to web page ", "https://www.youtube.com/watch?v=Zy4KJeVN7Gk&list=PLr6-GrHUlVf_ZNmuQSXdS197Oyr1L9sPB&index=8"));

        handler.addCourseContent(new CourseContentData(3, "LAC-1:java classes and object", "https://www.youtube.com/watch?v=cXj1hHdMNk4&list=PL9ooVrP1hQOHb4bxoHauWVwNg4FweDItZ&index=16"));
        handler.addCourseContent(new CourseContentData(3, "LAC-2:java inheritance", "https://www.youtube.com/watch?v=nixQyPIAnOQ&list=PL9ooVrP1hQOHb4bxoHauWVwNg4FweDItZ&index=17"));
        handler.addCourseContent(new CourseContentData(3, "LAC-3:java method: overloading and overriding", "https://www.youtube.com/watch?v=jg4MpYr1TBc&list=PL9ooVrP1hQOHb4bxoHauWVwNg4FweDItZ&index=18"));
        handler.addCourseContent(new CourseContentData(3, "LAC-4:String manipulation in java", "https://www.youtube.com/watch?v=N63JCXwdd14&list=PL9ooVrP1hQOHb4bxoHauWVwNg4FweDItZ&index=19"));
        handler.addCourseContent(new CourseContentData(3, "LAC-5:java abstract class and method", "https://www.youtube.com/watch?v=5X0Y--92pMI&list=PL9ooVrP1hQOHb4bxoHauWVwNg4FweDItZ&index=21"));
        handler.addCourseContent(new CourseContentData(3, "LAC-6: Multithreading in java", "https://www.youtube.com/watch?v=TCd8QIS-2KI&list=PL9ooVrP1hQOHb4bxoHauWVwNg4FweDItZ&index=22"));
        handler.addCourseContent(new CourseContentData(3, "LAC-7: reading and writing file in java", "https://www.youtube.com/watch?v=SslMi6ptwH8&list=PL9ooVrP1hQOHb4bxoHauWVwNg4FweDItZ&index=23"));
        handler.addCourseContent(new CourseContentData(3, "LAC-8: serialization interface", "https://www.youtube.com/watch?v=6B6vp0jZnb0&list=PL9ooVrP1hQOHb4bxoHauWVwNg4FweDItZ&index=24"));
        handler.addCourseContent(new CourseContentData(3, "LAC-9:Exception handling", "https://www.youtube.com/watch?v=W-N2ltgU-X4&list=PL9ooVrP1hQOHb4bxoHauWVwNg4FweDItZ&index=25"));

        handler.addCourseContent(new CourseContentData(4, "LEC -1: HELLO WORLD in java script", "https://www.youtube.com/watch?v=ranSYb-EhrU&list=PL4cUxeGkcC9i9Ae2D9Ee1RvylH38dKuET&index=3"));
        handler.addCourseContent(new CourseContentData(4, "LEC-2:java script variables", "https://www.youtube.com/watch?v=u0Mq3FzpsmI&list=PL4cUxeGkcC9i9Ae2D9Ee1RvylH38dKuET&index=8"));
        handler.addCourseContent(new CourseContentData(4, "LEC-3: basic mathematical operators", "https://www.youtube.com/watch?v=_MC0Gw07w8M&list=PL4cUxeGkcC9i9Ae2D9Ee1RvylH38dKuET&index=9"));
        handler.addCourseContent(new CourseContentData(4, "LEC-4: if statements", "https://www.youtube.com/watch?v=Lp-Du2fKoug&list=PL4cUxeGkcC9i9Ae2D9Ee1RvylH38dKuET&index=13"));
        handler.addCourseContent(new CourseContentData(4, "LEC-5: else if statements", "https://www.youtube.com/watch?v=1v1Bk3Q02Sc&list=PL4cUxeGkcC9i9Ae2D9Ee1RvylH38dKuET&index=14"));
        handler.addCourseContent(new CourseContentData(4, "LEC-6: logical operators", "https://www.youtube.com/watch?v=mbT7sSmVUS8&list=PL4cUxeGkcC9i9Ae2D9Ee1RvylH38dKuET&index=16"));
        handler.addCourseContent(new CourseContentData(4, "LEC-7: while loops", "https://www.youtube.com/watch?v=PpbFyLTtpWI&list=PL4cUxeGkcC9i9Ae2D9Ee1RvylH38dKuET&index=17"));
        handler.addCourseContent(new CourseContentData(4, "LEC-8: for loops", "https://www.youtube.com/watch?v=U87UmD-5h4o&list=PL4cUxeGkcC9i9Ae2D9Ee1RvylH38dKuET&index=18"));
        handler.addCourseContent(new CourseContentData(4, "LEC-9: Break and continue", "https://www.youtube.com/watch?v=QSuTH0C_3_Y&list=PL4cUxeGkcC9i9Ae2D9Ee1RvylH38dKuET&index=19"));
        handler.addCourseContent(new CourseContentData(4, "LEC-10: functions", "https://www.youtube.com/watch?v=KH57lIgwe2g&list=PL4cUxeGkcC9i9Ae2D9Ee1RvylH38dKuET&index=21"));

        handler.addCourseContent(new CourseContentData(5, "LEC-1:How to create database in Mysql", "https://www.youtube.com/watch?v=fHMh27nluTg&list=PLS1QulWo1RIY4auvfxAHS9m_fZJ2wxSse&index=5"));
        handler.addCourseContent(new CourseContentData(5, "LEC-2: how to create tables in Mysql", "https://www.youtube.com/watch?v=nmOaoC5I2p0&list=PLS1QulWo1RIY4auvfxAHS9m_fZJ2wxSse&index=6"));
        handler.addCourseContent(new CourseContentData(5, "LEC-3: How to insert data in MYSQLtable", "https://www.youtube.com/watch?v=vJTStapcRiE&list=PLS1QulWo1RIY4auvfxAHS9m_fZJ2wxSse&index=7"));
        handler.addCourseContent(new CourseContentData(5, "LEC-4:Using SELECT Statement to Query Data In MySQL", "https://www.youtube.com/watch?v=ZqlSPEIHBPg&list=PLS1QulWo1RIY4auvfxAHS9m_fZJ2wxSse&index=8"));
        handler.addCourseContent(new CourseContentData(5, "LEC-5:MySQL SELECT DISTINCT Statement", "https://www.youtube.com/watch?v=OqyTwG3kIOM&list=PLS1QulWo1RIY4auvfxAHS9m_fZJ2wxSse&index=9"));
        handler.addCourseContent(new CourseContentData(5, "LEC-6: MySQL WHERE Clause with Examples", "https://www.youtube.com/watch?v=cfY9BkYdAh0&list=PLS1QulWo1RIY4auvfxAHS9m_fZJ2wxSse&index=10"));
        handler.addCourseContent(new CourseContentData(5, "LEC-7:MYsql update table", "https://www.youtube.com/watch?v=v3RIEfBbqIw&list=PLS1QulWo1RIY4auvfxAHS9m_fZJ2wxSse&index=15"));
        handler.addCourseContent(new CourseContentData(5, "LEC-8:Delete database and table", "https://www.youtube.com/watch?v=QLagu8NmsLE&list=PLS1QulWo1RIY4auvfxAHS9m_fZJ2wxSse&index=16"));

        handler.addCourseContent(new CourseContentData(6, "LEC-1:your first php file", "https://www.youtube.com/watch?v=ABcXbZLm5G8&list=PL4cUxeGkcC9gksOX3Kd9KPo-O68ncT05o&index=3"));
        handler.addCourseContent(new CourseContentData(6, "LEC-2: variables and constants", "https://www.youtube.com/watch?v=2CXme275t9k&list=PL4cUxeGkcC9gksOX3Kd9KPo-O68ncT05o&index=4"));
        handler.addCourseContent(new CourseContentData(6, "LEC-3: Strings", "https://www.youtube.com/watch?v=U2EliFC9NrQ&list=PL4cUxeGkcC9gksOX3Kd9KPo-O68ncT05o&index=5"));
        handler.addCourseContent(new CourseContentData(6, "LEC-4:Numbers", "https://www.youtube.com/watch?v=lT2AvQ17F_w&list=PL4cUxeGkcC9gksOX3Kd9KPo-O68ncT05o&index=6"));
        handler.addCourseContent(new CourseContentData(6, "LEC-5:Arrays", "https://www.youtube.com/watch?v=bWygRxrlD44&list=PL4cUxeGkcC9gksOX3Kd9KPo-O68ncT05o&index=7"));
        handler.addCourseContent(new CourseContentData(6, "LEC-6:loops", "https://www.youtube.com/watch?v=TBUgZ84tTgU&list=PL4cUxeGkcC9gksOX3Kd9KPo-O68ncT05o&index=9"));
        handler.addCourseContent(new CourseContentData(6, "LEC-7:Booleans and comparisons", "https://www.youtube.com/watch?v=hxYQA-nuIXY&list=PL4cUxeGkcC9gksOX3Kd9KPo-O68ncT05o&index=10"));
        handler.addCourseContent(new CourseContentData(6, "LEC-8: Conditional statement", "https://www.youtube.com/watch?v=E1ms4qpfy78&list=PL4cUxeGkcC9gksOX3Kd9KPo-O68ncT05o&index=11"));

        preferences.saveBool(MyPreferences.IS_LOGIN, true);
        startCustomerActivity();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    private void releaseKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.sampleapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}
