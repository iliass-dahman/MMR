package com.example.mmr.medic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.mmr.R;
import com.example.mmr.VolleySingleton;
import com.example.mmr.patient.NoteListAdapter;
import com.example.mmr.patient.Notes;
import com.example.mmr.patient.OnlineMedListAdapter;
import com.example.mmr.patient.OnlineMeds;
import com.example.mmr.shared.SharedModel;

import java.util.Vector;

public class MedicNoteList extends AppCompatActivity {
    RecyclerView recyclerView;
    private RequestQueue queue;
    TextView emplty;
    private String cin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //make it fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_medic_note_list);

        if (getIntent().hasExtra("cin"))
            cin=getIntent().getStringExtra("cin");

        recyclerView=findViewById(R.id.doc_note_list);

        emplty=findViewById(R.id.empty_view_note_list);
        recyclerView.setVisibility(View.GONE);
        emplty.setVisibility(View.VISIBLE);

        queue = VolleySingleton.getInstance(this).getRequestQueue();
        new SharedModel(this,queue).getOnlineMedAndNote(cin, new SharedModel.LoadHomeInfoCallBack() {
            @Override
            public void onSuccess(Vector<Object> vector) {
                Notes notes=(Notes) vector.get(1);

                NoteListAdapter noteListAdapter = new NoteListAdapter(notes.getNotes());
                // Attach the adapter to the recyclerview to populate items
                recyclerView.setAdapter(noteListAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                if (notes.getNotes().isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emplty.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emplty.setVisibility(View.GONE);
                }
            }

            @Override
            public void onErr(String message) {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }
        });
    }
}