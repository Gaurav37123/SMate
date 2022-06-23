package com.example.smate.Home;

import static android.os.Environment.DIRECTORY_DOCUMENTS;
import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smate.Classes.NotesItem;
import com.example.smate.FeedAdapter;
import com.example.smate.R;

import java.io.File;
import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NotesViewHolder> {

    ArrayList<NotesItem> NotesList = new ArrayList<>();
    Context context;
    public NoteAdapter(Context context, ArrayList<NotesItem> list)
    {
        this.context = context;
        NotesList = list;
    }
    public class NotesViewHolder extends RecyclerView.ViewHolder{

        TextView pdfItemName;
        CardView notesItem;
        ImageButton btnNoteDownload;
        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            pdfItemName = itemView.findViewById(R.id.pdfItemName);
            notesItem = itemView.findViewById(R.id.NotesItem);
            btnNoteDownload = itemView.findViewById(R.id.btnNoteDownload);
        }
    }
    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_item_layout,parent,false);
        return new NotesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        int i=position;
        holder.pdfItemName.setText(NotesList.get(position).getName());
        Log.d("Name", NotesList.get(position).getName());
        holder.btnNoteDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadManager downloadmanager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(NotesList.get(i).getUrl());
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalFilesDir(context, DIRECTORY_DOCUMENTS, NotesList.get(i).getName() + ".pdf");
                downloadmanager.enqueue(request);
            }
        });
    }

    @Override
    public int getItemCount() {
        return NotesList.size();
    }
}
