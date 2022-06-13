package com.example.smate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.smate.Classes.FeedItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {

    private static final int IMAGE_REQUEST = 1;
    FeedItem UploadItem = new FeedItem();
    ArrayList<SliderItem> urls = new ArrayList<>();
    SliderView etSlider;
    Thread uploadImageThread,uploadRequestThread;
    ArrayList<Uri> imageUri = new ArrayList<>();
    ViewGroup viewGroup;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    ArrayList<StorageTask> uploadTask = new ArrayList<>();
    String category;
    DatabaseReference fdb = FirebaseDatabase.getInstance().getReference().child("home");
    FloatingActionButton btnAdd;
    ArrayList<FeedItem> list = new ArrayList<>();
    RecyclerView listView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        viewGroup = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        listView = findViewById(R.id.listView);
        btnAdd = findViewById(R.id.btnAdd);

        category = getIntent().getStringExtra("category");


        FeedAdapter listAdapter = new FeedAdapter(this,list);
        listView.setAdapter(listAdapter);
        layoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(layoutManager);

        fdb.child(category).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot Snap : snapshot.getChildren())
                {
                    ArrayList<SliderItem> tempUrl = new ArrayList<>();
                    FeedItem item = new FeedItem();
                    item.setContentDesc(Snap.child("contentDesc").getValue().toString());
                    for(DataSnapshot urlSnap : Snap.child("url").getChildren())
                    {
                        tempUrl.add(new SliderItem(urlSnap.getValue().toString()));
                    }
                    item.setUrl(tempUrl);
                    item.setPublisher(Snap.child("publisher").getValue().toString());
                    item.setDownload(Snap.child("download").getValue().toString());
                    item.setRating(Integer.parseInt(Snap.child("rating").getValue().toString()));
                    list.add(item);
                }
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        uploadImageThread=new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FeedActivity.this, "Thread started", Toast.LENGTH_SHORT).show();
                    }
                });
                uploadFile();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FeedActivity.this, "Thread finished", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        uploadRequestThread = new Thread(new Runnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis();
                while (System.currentTimeMillis() < time+10000)
                {
                    try {
                        UploadItem.setRating(0);
                        UploadItem.setDownload("null");
                        if(urls.size() != imageUri.size())
                            continue;
                        UploadItem.setUrl(urls);
                        HashMap<String,Object> uploadItemMap = new HashMap<>();
                        HashMap<String,Object> urlMap = new HashMap<>();
                        for(int i=0;i<UploadItem.getUrl().size();i++)
                        {
                            urlMap.put(""+(i+1),UploadItem.getUrl().get(i).getImageUrl());
                        }
                        uploadItemMap.put("url",urlMap);
                        uploadItemMap.put("rating",UploadItem.getRating());
                        uploadItemMap.put("download",UploadItem.getDownload());
                        uploadItemMap.put("contentDesc",UploadItem.getContentDesc());
                        uploadItemMap.put("publisher",user.getPhoneNumber());

                        UploadRequest(uploadItemMap);
                        break;
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_feed_input,viewGroup,false);
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                Button btnRequest;
                TextInputEditText etDesc;
                etDesc = popupView.findViewById(R.id.etDesc);
                etSlider = popupView.findViewById(R.id.etSlider);
                btnRequest = popupView.findViewById(R.id.btnRequest);

                etSlider.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imageUri.clear();
                        OpenFileChooser();
                    }
                });
                btnRequest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        uploadImageThread.start();
                        String desc = etDesc.getText().toString().trim();
                        UploadItem.setContentDesc(desc);
                        uploadRequestThread.start();
                    }
                });

            }
        });
    }

    private void UploadRequest(Map uploadItem) {
        fdb.child(category).push().updateChildren(uploadItem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(FeedActivity.this, "Upload Success", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadFile()
    {
        if(imageUri == null)
        {
            Toast.makeText(this, "Please select some photos", Toast.LENGTH_SHORT).show();
        }

        else {
            final StorageReference fileReference = FirebaseStorage.getInstance().getReference(category);

            for(int i=0;i<imageUri.size();i++)
            {
                Bitmap bmp = null;
                try {
                    bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri.get(i));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                byte[] fileInBytes = baos.toByteArray();

                uploadTask.add( fileReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri.get(i))).putBytes(fileInBytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                urls.add(new SliderItem(uri.toString()));
                            }
                        });
                    }
                }));
            }
            UploadItem.setUrl(urls);
        }
    }
    private String getFileExtension(Uri uri)
    {
        ContentResolver cR = this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void OpenFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK )
        {
            if(data.getClipData()!=null)
            {
                int count = data.getClipData().getItemCount();
                for(int i = 0; i < count && i<5; i++) {
                    imageUri.add(data.getClipData().getItemAt(i).getUri());
                }
            }
            else if( data != null && data.getData() != null)
            {
                imageUri.add(data.getData());
            }

            SliderUriAdapter adapter = new SliderUriAdapter(FeedActivity.this, imageUri);
            etSlider.setSliderAdapter(adapter);
            etSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
            etSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
            etSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
            etSlider.setIndicatorSelectedColor(Color.WHITE);
            etSlider.setIndicatorUnselectedColor(Color.GRAY);
            etSlider.setScrollTimeInSec(4); //set scroll delay in seconds :
            etSlider.startAutoCycle();
        }
    }
}