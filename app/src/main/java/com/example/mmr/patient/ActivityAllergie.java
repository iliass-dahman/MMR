package com.example.mmr.patient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.mmr.R;
import com.example.mmr.VolleySingleton;
import com.example.mmr.shared.SharedModel;

import java.util.Vector;

public class ActivityAllergie extends AppCompatActivity {
    RecyclerView recyclerView;
    private RequestQueue queue;
    PatientSessionManager sessionManager;
    Vector<Allergie> allergies=new Vector<>();
    private String cin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //make it fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_alergie);

        sessionManager=new PatientSessionManager(this);
        if (getIntent().hasExtra("cin"))
            cin=getIntent().getStringExtra("cin");
        else {
            cin=sessionManager.getCinPatient();
        }

        recyclerView=findViewById(R.id.alergie_list);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        new SharedModel(this,queue).getAllergies(cin, new SharedModel.LoadHomeInfoCallBack() {
            @Override
            public void onSuccess(Vector<Object> vector) {
                for (Object obj: vector) {
                    allergies.add((Allergie)obj);
                }
                AllergieListAdapter medListAdapter = new AllergieListAdapter(allergies);
                recyclerView.setAdapter(medListAdapter);
                // Attach the adapter to the recyclerview to populate items
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }

            @Override
            public void onErr(String message) {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }
        });
    }
}