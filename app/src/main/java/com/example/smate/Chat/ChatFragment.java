package com.example.smate.Chat;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.smate.Classes.UserProfile;
import com.example.smate.HostActivity;
import com.example.smate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ChatFragment extends Fragment {

    View view;
    RecyclerView chatList;
    UserProfile profile = new UserProfile();
    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("chat");
    ArrayList<ChatMessage> MessageList = new ArrayList<>();
    EditText etMessage;
    ImageButton btnSendMessage;
    public ChatFragment() {
        // Required empty public constructor
    }
    public ChatFragment(String category,String node) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("home").child(category).child(node).child("review");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        chatList = view.findViewById(R.id.ChatList);
        etMessage = view.findViewById(R.id.etMessage);
        btnSendMessage = view.findViewById(R.id.btnSendMessage);

        ChatAdapter adapter = new ChatAdapter(getActivity(),MessageList);
        chatList.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatList.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference().child("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    profile = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).getValue(UserProfile.class);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Thread chatThread = new Thread(new Runnable() {
            @Override
            public void run() {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        MessageList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            ChatMessage temp = dataSnapshot.getValue(ChatMessage.class);
                            MessageList.add(temp);
                        }
                        adapter.notifyDataSetChanged();

                        chatList.scrollToPosition(MessageList.size()-1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        chatThread.start();

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = etMessage.getText().toString().trim();
                if(!message.isEmpty())
                {
                    ChatMessage SendMessage = new ChatMessage();
                    Date date = new Date();
                    SendMessage.setMessage(message);
                    SendMessage.setSenderPhone(profile.getPhoneNumber());
                    SendMessage.setSenderName(profile.getName());
                    SimpleDateFormat TimeFormat = new SimpleDateFormat("hh.mm a");
                    SendMessage.setTime(TimeFormat.format(date));
                    SimpleDateFormat DateFormat = new SimpleDateFormat("dd.mm.yy");
                    SendMessage.setDate(DateFormat.format(date));
                    databaseReference.push().setValue(SendMessage);
                    etMessage.setText("");
                    adapter.notifyDataSetChanged();
                }
            }
        });


        return view;
    }
}