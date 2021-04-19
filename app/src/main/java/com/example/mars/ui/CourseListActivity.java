package com.example.mars.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mars.R;
import com.example.mars.data.CourseContentData;
import com.example.mars.data.CourseData;
import com.example.mars.database.DatabaseHandler;
import com.example.mars.ui.adapter.AdapterCourseList;

import java.util.ArrayList;

public class CourseListActivity extends AppCompatActivity {
    RecyclerView rv_course_list;
    AdapterCourseList adapter;
    ArrayList<CourseData> data;
    DatabaseHandler databaseHandler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list_activity);
        rv_course_list=findViewById(R.id.rv_course_list);

        databaseHandler=new DatabaseHandler(this);


        data=new ArrayList<>();
        initView(data);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
            // REQUEST_CODE_LOCATION should be defined on your app level
            ActivityCompat.requestPermissions(this, permissions, 101);
        }
//        addCourseVideo();
    }

    private void addCourseVideo(){
        DatabaseHandler handler=new DatabaseHandler(this);
        handler.addCourseContent(new CourseContentData(1,"LEC-1: introduction to css","https://www.youtube.com/watch?v=qKoajPPWpmo&list=PLr6-GrHUlVf8JIgLcu3sHigvQjTw_aC9C"));
        handler.addCourseContent(new CourseContentData(1,"LEC-2:  Changing font type, size, color","https://www.youtube.com/watch?v=UO0ZPL8yMpU&list=PLr6-GrHUlVf8JIgLcu3sHigvQjTw_aC9C&index=2"));
        handler.addCourseContent(new CourseContentData(1,"LEC-3: Multiple selectors and writing rule for more than one element","https://www.youtube.com/watch?v=JT0gyzbpD2U&list=PLr6-GrHUlVf8JIgLcu3sHigvQjTw_aC9C&index=3"));
        handler.addCourseContent(new CourseContentData(1,"LEC-4: Add a line to header and border property","https://www.youtube.com/watch?v=hCoMjvtsyPA&list=PLr6-GrHUlVf8JIgLcu3sHigvQjTw_aC9C&index=4"));
        handler.addCourseContent(new CourseContentData(1,"LEC-5:inheritance and overriding ","https://www.youtube.com/watch?v=Bj2ZCfDPP2A&list=PLr6-GrHUlVf8JIgLcu3sHigvQjTw_aC9C&index=5"));
        handler.addCourseContent(new CourseContentData(1,"LEC-6 using class in css","https://www.youtube.com/watch?v=u4dLB9NP3IA&list=PLr6-GrHUlVf8JIgLcu3sHigvQjTw_aC9C&index=6"));
        handler.addCourseContent(new CourseContentData(1,"LEC-7:background image property","https://www.youtube.com/watch?v=9vo8nTQ7s5A&list=PLr6-GrHUlVf8JIgLcu3sHigvQjTw_aC9C&index=21"));
        handler.addCourseContent(new CourseContentData(1,"LEC-8: the id attribute","https://www.youtube.com/watch?v=ZuuEdXXzG7I&list=PLr6-GrHUlVf8JIgLcu3sHigvQjTw_aC9C&index=22"));
        handler.addCourseContent(new CourseContentData(1,"LEC-9: web colors","https://www.youtube.com/watch?v=sbtD9AP9W70&list=PLr6-GrHUlVf8JIgLcu3sHigvQjTw_aC9C&index=13"));

        handler.addCourseContent(new CourseContentData(2,"LEC-1: introduction of html","https://www.youtube.com/watch?v=dD2EISBDjWM&list=PLr6-GrHUlVf_ZNmuQSXdS197Oyr1L9sPB&index=1"));
        handler.addCourseContent(new CourseContentData(2,"LEC-2 creating the first web page ","https://www.youtube.com/watch?v=-USAeFpVf_A&list=PLr6-GrHUlVf_ZNmuQSXdS197Oyr1L9sPB&index=2"));
        handler.addCourseContent(new CourseContentData(2,"LEC-3 line breaks ,spacing and comments","https://www.youtube.com/watch?v=i3GE-toQg-o&list=PLr6-GrHUlVf_ZNmuQSXdS197Oyr1L9sPB&index=3"));
        handler.addCourseContent(new CourseContentData(2,"LEC -4 :ordered list and unordered list","https://www.youtube.com/watch?v=09oErCBjVns&list=PLr6-GrHUlVf_ZNmuQSXdS197Oyr1L9sPB&index=4"));
        handler.addCourseContent(new CourseContentData(2,"LEC-5 : creating table ","https://www.youtube.com/watch?v=wvR40su_XBM&list=PLr6-GrHUlVf_ZNmuQSXdS197Oyr1L9sPB&index=5"));
        handler.addCourseContent(new CourseContentData(2,"LEC-6 :creating a web link","https://www.youtube.com/watch?v=U4UHoiK6Oo4&list=PLr6-GrHUlVf_ZNmuQSXdS197Oyr1L9sPB&index=6"));
        handler.addCourseContent(new CourseContentData(2,"LEC-7:creatinga web link with the same page","https://www.youtube.com/watch?v=bCt2FnyY7AE&list=PLr6-GrHUlVf_ZNmuQSXdS197Oyr1L9sPB&index=7"));
        handler.addCourseContent(new CourseContentData(2,"LEC-8: adding image to web page ","https://www.youtube.com/watch?v=Zy4KJeVN7Gk&list=PLr6-GrHUlVf_ZNmuQSXdS197Oyr1L9sPB&index=8"));

        handler.addCourseContent(new CourseContentData(3,"LAC-1:java classes and object","https://www.youtube.com/watch?v=cXj1hHdMNk4&list=PL9ooVrP1hQOHb4bxoHauWVwNg4FweDItZ&index=16"));
        handler.addCourseContent(new CourseContentData(3,"LAC-2:java inheritance","https://www.youtube.com/watch?v=nixQyPIAnOQ&list=PL9ooVrP1hQOHb4bxoHauWVwNg4FweDItZ&index=17"));
        handler.addCourseContent(new CourseContentData(3,"LAC-3:java method: overloading and overriding","https://www.youtube.com/watch?v=jg4MpYr1TBc&list=PL9ooVrP1hQOHb4bxoHauWVwNg4FweDItZ&index=18"));
        handler.addCourseContent(new CourseContentData(3,"LAC-4:String manipulation in java","https://www.youtube.com/watch?v=N63JCXwdd14&list=PL9ooVrP1hQOHb4bxoHauWVwNg4FweDItZ&index=19"));
        handler.addCourseContent(new CourseContentData(3,"LAC-5:java abstract class and method","https://www.youtube.com/watch?v=5X0Y--92pMI&list=PL9ooVrP1hQOHb4bxoHauWVwNg4FweDItZ&index=21"));
        handler.addCourseContent(new CourseContentData(3,"LAC-6: Multithreading in java","https://www.youtube.com/watch?v=TCd8QIS-2KI&list=PL9ooVrP1hQOHb4bxoHauWVwNg4FweDItZ&index=22"));
        handler.addCourseContent(new CourseContentData(3,"LAC-7: reading and writing file in java","https://www.youtube.com/watch?v=SslMi6ptwH8&list=PL9ooVrP1hQOHb4bxoHauWVwNg4FweDItZ&index=23"));
        handler.addCourseContent(new CourseContentData(3,"LAC-8: serialization interface","https://www.youtube.com/watch?v=6B6vp0jZnb0&list=PL9ooVrP1hQOHb4bxoHauWVwNg4FweDItZ&index=24"));
        handler.addCourseContent(new CourseContentData(3,"LAC-9:Exception handling","https://www.youtube.com/watch?v=W-N2ltgU-X4&list=PL9ooVrP1hQOHb4bxoHauWVwNg4FweDItZ&index=25"));

        handler.addCourseContent(new CourseContentData(4,"LEC -1: HELLO WORLD in java script","https://www.youtube.com/watch?v=ranSYb-EhrU&list=PL4cUxeGkcC9i9Ae2D9Ee1RvylH38dKuET&index=3"));
        handler.addCourseContent(new CourseContentData(4,"LEC-2:java script variables","https://www.youtube.com/watch?v=u0Mq3FzpsmI&list=PL4cUxeGkcC9i9Ae2D9Ee1RvylH38dKuET&index=8"));
        handler.addCourseContent(new CourseContentData(4,"LEC-3: basic mathematical operators","https://www.youtube.com/watch?v=_MC0Gw07w8M&list=PL4cUxeGkcC9i9Ae2D9Ee1RvylH38dKuET&index=9"));
        handler.addCourseContent(new CourseContentData(4,"LEC-4: if statements","https://www.youtube.com/watch?v=Lp-Du2fKoug&list=PL4cUxeGkcC9i9Ae2D9Ee1RvylH38dKuET&index=13"));
        handler.addCourseContent(new CourseContentData(4,"LEC-5: else if statements","https://www.youtube.com/watch?v=1v1Bk3Q02Sc&list=PL4cUxeGkcC9i9Ae2D9Ee1RvylH38dKuET&index=14"));
        handler.addCourseContent(new CourseContentData(4,"LEC-6: logical operators","https://www.youtube.com/watch?v=mbT7sSmVUS8&list=PL4cUxeGkcC9i9Ae2D9Ee1RvylH38dKuET&index=16"));
        handler.addCourseContent(new CourseContentData(4,"LEC-7: while loops","https://www.youtube.com/watch?v=PpbFyLTtpWI&list=PL4cUxeGkcC9i9Ae2D9Ee1RvylH38dKuET&index=17"));
        handler.addCourseContent(new CourseContentData(4,"LEC-8: for loops","https://www.youtube.com/watch?v=U87UmD-5h4o&list=PL4cUxeGkcC9i9Ae2D9Ee1RvylH38dKuET&index=18"));
        handler.addCourseContent(new CourseContentData(4,"LEC-9: Break and continue","https://www.youtube.com/watch?v=QSuTH0C_3_Y&list=PL4cUxeGkcC9i9Ae2D9Ee1RvylH38dKuET&index=19"));
        handler.addCourseContent(new CourseContentData(4,"LEC-10: functions","https://www.youtube.com/watch?v=KH57lIgwe2g&list=PL4cUxeGkcC9i9Ae2D9Ee1RvylH38dKuET&index=21"));

        handler.addCourseContent(new CourseContentData(5,"LEC-1:How to create database in Mysql","https://www.youtube.com/watch?v=fHMh27nluTg&list=PLS1QulWo1RIY4auvfxAHS9m_fZJ2wxSse&index=5"));
        handler.addCourseContent(new CourseContentData(5,"LEC-2: how to create tables in Mysql","https://www.youtube.com/watch?v=nmOaoC5I2p0&list=PLS1QulWo1RIY4auvfxAHS9m_fZJ2wxSse&index=6"));
        handler.addCourseContent(new CourseContentData(5,"LEC-3: How to insert data in MYSQLtable","https://www.youtube.com/watch?v=vJTStapcRiE&list=PLS1QulWo1RIY4auvfxAHS9m_fZJ2wxSse&index=7"));
        handler.addCourseContent(new CourseContentData(5,"LEC-4:Using SELECT Statement to Query Data In MySQL","https://www.youtube.com/watch?v=ZqlSPEIHBPg&list=PLS1QulWo1RIY4auvfxAHS9m_fZJ2wxSse&index=8"));
        handler.addCourseContent(new CourseContentData(5,"LEC-5:MySQL SELECT DISTINCT Statement","https://www.youtube.com/watch?v=OqyTwG3kIOM&list=PLS1QulWo1RIY4auvfxAHS9m_fZJ2wxSse&index=9"));
        handler.addCourseContent(new CourseContentData(5,"LEC-6: MySQL WHERE Clause with Examples","https://www.youtube.com/watch?v=cfY9BkYdAh0&list=PLS1QulWo1RIY4auvfxAHS9m_fZJ2wxSse&index=10"));
        handler.addCourseContent(new CourseContentData(5,"LEC-7:MYsql update table","https://www.youtube.com/watch?v=v3RIEfBbqIw&list=PLS1QulWo1RIY4auvfxAHS9m_fZJ2wxSse&index=15"));
        handler.addCourseContent(new CourseContentData(5,"LEC-8:Delete database and table","https://www.youtube.com/watch?v=QLagu8NmsLE&list=PLS1QulWo1RIY4auvfxAHS9m_fZJ2wxSse&index=16"));

        handler.addCourseContent(new CourseContentData(6,"LEC-1:your first php file","https://www.youtube.com/watch?v=ABcXbZLm5G8&list=PL4cUxeGkcC9gksOX3Kd9KPo-O68ncT05o&index=3"));
        handler.addCourseContent(new CourseContentData(6,"LEC-2: variables and constants","https://www.youtube.com/watch?v=2CXme275t9k&list=PL4cUxeGkcC9gksOX3Kd9KPo-O68ncT05o&index=4"));
        handler.addCourseContent(new CourseContentData(6,"LEC-3: Strings","https://www.youtube.com/watch?v=U2EliFC9NrQ&list=PL4cUxeGkcC9gksOX3Kd9KPo-O68ncT05o&index=5"));
        handler.addCourseContent(new CourseContentData(6,"LEC-4:Numbers","https://www.youtube.com/watch?v=lT2AvQ17F_w&list=PL4cUxeGkcC9gksOX3Kd9KPo-O68ncT05o&index=6"));
        handler.addCourseContent(new CourseContentData(6,"LEC-5:Arrays","https://www.youtube.com/watch?v=bWygRxrlD44&list=PL4cUxeGkcC9gksOX3Kd9KPo-O68ncT05o&index=7"));
        handler.addCourseContent(new CourseContentData(6,"LEC-6:loops","https://www.youtube.com/watch?v=TBUgZ84tTgU&list=PL4cUxeGkcC9gksOX3Kd9KPo-O68ncT05o&index=9"));
        handler.addCourseContent(new CourseContentData(6,"LEC-7:Booleans and comparisons","https://www.youtube.com/watch?v=hxYQA-nuIXY&list=PL4cUxeGkcC9gksOX3Kd9KPo-O68ncT05o&index=10"));
        handler.addCourseContent(new CourseContentData(6,"LEC-8: Conditional statement","https://www.youtube.com/watch?v=E1ms4qpfy78&list=PL4cUxeGkcC9gksOX3Kd9KPo-O68ncT05o&index=11"));

    }
    @Override
    protected void onResume() {
        super.onResume();
        data=new ArrayList<>();
        data.addAll(databaseHandler.getAllCourse());
        initView(data);
    }

    private void initView(ArrayList<CourseData> data){
        adapter = new AdapterCourseList(this,data);
        rv_course_list.setHasFixedSize(true);
        rv_course_list.setLayoutManager(new LinearLayoutManager(this));
        rv_course_list.setAdapter(adapter);
    }

}
