package com.example.smate.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.smate.Classes.NotesItem;
import com.example.smate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import aglibs.loading.skeleton.layout.SkeletonRecyclerView;

public class NotesActivity extends AppCompatActivity {

    Spinner etNoteBranch,etNoteSem;
    SkeletonRecyclerView notesListView;
    ArrayList<NotesItem> notesList = new ArrayList<>();
    String[] SemArray = new String[]{"1","2","3","4","5","6","7","8"};
    ArrayList<String> BranchArray = new ArrayList<>();
    NoteAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        etNoteBranch = findViewById(R.id.etNoteBranch);
        etNoteSem = findViewById(R.id.etNoteSem);
        notesListView = findViewById(R.id.notesListView);
        ArrayAdapter<String> SpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,SemArray);
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etNoteSem.setAdapter(SpinnerAdapter);
        etNoteSem.setSelection(0);
        Collections.addAll(BranchArray,getResources().getStringArray(R.array.branch_list));
        LoadList();

        adapter = new NoteAdapter(this,notesList);
        notesListView.setAdapter(adapter);
        notesListView.setLayoutManager(new LinearLayoutManager(this));

        etNoteBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                LoadList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        etNoteSem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                LoadList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    void LoadList()
    {
        notesListView.startLoading();
        String branch = etNoteBranch.getSelectedItem().toString();
        String sem = etNoteSem.getSelectedItem().toString();
        FirebaseDatabase.getInstance().getReference().child("home").child("Notes")
                .child(etNoteBranch.getSelectedItem().toString())
                .child(etNoteSem.getSelectedItem().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        notesList.clear();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            NotesItem temp = snap.getValue(NotesItem.class);
                            notesList.add(temp);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        notesListView.stopLoading();
    }
}