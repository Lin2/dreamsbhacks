package com.example.dream_sbhacks;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> al;
    private ArrayAdapter<String> arrayAdapter;
    private int i;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        availableMatches();

        al = new ArrayList<>();

        arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.helloText, al );

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                al.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                //Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
               // Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();

            }
        });

    }
    public void availableMatches(){
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users");
        userDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists())
                {
                    int userEntryCount = 0;
                    if (!snapshot.getKey().equals(userId)){
                       // al.add(snapshot.child("name").getValue().toString());
                        arrayAdapter.notifyDataSetChanged();
                        int entryCount = 0;
                        while(snapshot.child("entry" + (entryCount + 1)).exists()) {
                            al.add(snapshot.child("entry" + (entryCount + 1)).getValue().toString());
                            entryCount++;
                        }
                    } else {
                       /* int entryCount = 0;
                        while(snapshot.child("entry" + (entryCount + 1)).exists()) {
                            entryCount++;
                        }
                        userEntryCount = entryCount;
                        System.out.println(entryCount);*/
                    }

                    final String curDream;

                    Bundle extras = getIntent().getExtras();
                    if (extras != null) {
                        curDream = extras.getString("LastString");
                    } else {
                        curDream = null;
                    }

                    arrayAdapter.notifyDataSetChanged();
                    Collections.sort(al, new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                           // System.out.println(curDream + ": " + o1 + ": " + o2 + ": " + (stringComp(o1, curDream) - stringComp(o2, curDream)));
                            return (int)(10 * (stringComp(o2, curDream) - stringComp(o1, curDream)));
                        }
                    });
                    arrayAdapter.notifyDataSetChanged();
                    }
                }


            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void logoutUser(View view){
        mAuth.signOut();
        Intent intent = new Intent (MainActivity.this, ChooseLoginorRegistrationActivity.class);
        finish();
        return;
    }

    public static double stringComp(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return 0;
        }
        s1 += " ";
        s2 += " ";
        Set<String> set = new HashSet<>();
        int count1 = 0;
        int count2 = 0;
        int totalScore = 0;
        int start = 0;
        for (int i = 0; i < s1.length(); i++) {
            if (Character.toUpperCase(s1.charAt(i)) < 'A' || Character.toUpperCase(s1.charAt(i)) > 'Z') {
                if (i != start) {
                    String w = s1.substring(start, i);
                    w = w.toUpperCase();
                    if (w.endsWith("ING")) {
                        w = w.substring(0, w.length() - 3);
                    } else if (w.endsWith("ED")) {
                        w = w.substring(0, w.length() - 2);
                    } else if (w.endsWith("S")) {
                        w = w.substring(0, w.length() - 1);
                    }
                    count1++;
                    set.add(w);
                }
                start = i + 1;
            }
        }

        start = 0;
        for (int i = 0; i < s2.length(); i++) {
            if (Character.toUpperCase(s2.charAt(i)) < 'A' || Character.toUpperCase(s2.charAt(i)) > 'Z') {
                if (i != start) {
                    String w = s2.substring(start, i);
                    w = w.toUpperCase();
                    if (w.endsWith("ING")) {
                        w = w.substring(0, w.length() - 3);
                    } else if (w.endsWith("ED")) {
                        w = w.substring(0, w.length() - 2);
                    } else if (w.endsWith("S")) {
                        w = w.substring(0, w.length() - 1);
                    }
                    count2++;

                    if(set.contains(w)) {
                        totalScore++;
                    }
                }
                start = i + 1;
            }
        }

        return ((double)totalScore)/count2;
    }
}