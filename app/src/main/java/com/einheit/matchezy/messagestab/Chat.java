package com.einheit.matchezy.messagestab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.einheit.matchezy.R;
import com.einheit.matchezy.Utility;
import com.einheit.matchezy.profilescreen.ProfilePage;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {

    private static final int REQUEST_IMAGE = 2001;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> mFirebaseAdapter;
    private DatabaseReference mFirebaseDatabaseReference;
    ImageView sendButton;
    private EditText mMessageEditText;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ImageView mAddMessageImageView;
    private ProgressBar mProgressBar;
    private Uri outputFileUri;
    JSONObject userData = null;
    JSONObject currentUserData = null;
    String uploadedTime = null;

    ImageView backbtn;
    TextView chat_head_name;
    CircleImageView chat_head_image;


    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        ImageView messageImageView;

        TextView messageTextViewReceived;
        ImageView messageImageViewReceived;

        LinearLayout textLayoutSent;
        LinearLayout imageLayoutSent;

        LinearLayout textLayoutReceived;
        LinearLayout imageLayoutReceived;

        RelativeLayout receivedMessageLayout;
        RelativeLayout sentMessageLayout;

        TextView timeTextViewForText;
        TextView timeTextViewForImage;

        TextView timeTextViewForTextReceived;
        TextView timeTextViewForImageReceived;

        TextView dateView;

        ImageView statusImageViewText;
        ImageView statusImageViewImage;

        TextView unreadMessagesCounter;

        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            messageImageView = (ImageView) itemView.findViewById(R.id.messageImageView);

            messageTextViewReceived = (TextView) itemView.findViewById(R.id.messageTextViewReceived);
            messageImageViewReceived = (ImageView) itemView.findViewById(R.id.messageImageViewReceived);

            receivedMessageLayout = itemView.findViewById(R.id.receivedTextLayout);
            sentMessageLayout = itemView.findViewById(R.id.sentTextLayout);

            textLayoutSent = itemView.findViewById(R.id.textLayoutSent);
            imageLayoutSent = itemView.findViewById(R.id.imageLayoutSent);

            textLayoutReceived = itemView.findViewById(R.id.textLayoutReceived);
            imageLayoutReceived = itemView.findViewById(R.id.imageLayoutReceived);

            timeTextViewForText = itemView.findViewById(R.id.timeTextViewForText);
            timeTextViewForImage = itemView.findViewById(R.id.timeTextViewForImage);

            timeTextViewForTextReceived = itemView.findViewById(R.id.timeTextViewForTextReceived);
            timeTextViewForImageReceived = itemView.findViewById(R.id.timeTextViewForImageReceived);

            dateView = itemView.findViewById(R.id.dateView);

            statusImageViewText = itemView.findViewById(R.id.statusImageViewText);
            statusImageViewImage = itemView.findViewById(R.id.statusImageViewImage);

            unreadMessagesCounter = itemView.findViewById(R.id.unreadMessagesCounter);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        sendButton = (ImageView) findViewById(R.id.sendButton);
        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAddMessageImageView = findViewById(R.id.addMessageImageView);

        mMessageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);

        try {
            userData = new JSONObject(getIntent().getStringExtra("userdata"));
            currentUserData = new JSONObject(getSPData("userdata"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        backbtn = findViewById(R.id.backbtn);
        chat_head_name = findViewById(R.id.chat_head_name);
        chat_head_image = findViewById(R.id.chat_head_image);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        chat_head_name.setText(userData.optString("name"));

        Glide.with(getApplicationContext()).load(userData.optString("profileImageURL")).into(chat_head_image);

        chat_head_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Chat.this, ProfilePage.class)
                        .putExtra("fromStatusCode", Utility.FROM_MATCHED)
                        .putExtra("user_id", userData.optString("user_id"))
                        .putExtra("userData", userData.toString());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference messagesRef = mFirebaseDatabaseReference.child(userData.optString("matched_id"))
                .orderByChild("uploadedTime").getRef();

        Query query = mFirebaseDatabaseReference.child(userData.optString("matched_id"))
                .orderByChild("uploadedTime");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));

        SnapshotParser<Message> parser = new SnapshotParser<Message>() {
            @Override
            public Message parseSnapshot(DataSnapshot dataSnapshot) {
                Message friendlyMessage = dataSnapshot.getValue(Message.class);
                if (friendlyMessage != null) {
                    friendlyMessage.setId(dataSnapshot.getKey());
                }
                return friendlyMessage;
            }
        };

        FirebaseRecyclerOptions<Message> options =
                new FirebaseRecyclerOptions.Builder<Message>()
                        .setQuery(messagesRef, parser)
                        .build();

        final RequestOptions finalRequestOptions = requestOptions;
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(options) {

            @Override
            public MessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new MessageViewHolder(inflater.inflate(R.layout.item_message, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(final MessageViewHolder viewHolder,
                                            final int position,
                                            final Message friendlyMessage) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                Date date = new Date(friendlyMessage.getMessageTime());
                final String time = dateFormat.format(date);

                uploadedTime = null;

                if(friendlyMessage.getUploadedTime() != -1) {
                    Date uploadedDate = new Date(friendlyMessage.getUploadedTime());
                    uploadedTime = dateFormat.format(uploadedDate);
                }

                if(position > 0) {
                    if (DateFormat.getDateInstance().format(date).equals(DateFormat.getDateInstance().
                            format(new Date(mFirebaseAdapter.getItem(position - 1).getMessageTime())))) {
                        viewHolder.dateView.setVisibility(View.GONE);
                    } else {
                        viewHolder.dateView.setText(DateFormat.getDateInstance().format(date).replace("-", " "));
                        viewHolder.dateView.setVisibility(View.VISIBLE);
                    }
                } else {
                    viewHolder.dateView.setText(DateFormat.getDateInstance().format(date).replace("-", " "));
                    viewHolder.dateView.setVisibility(View.VISIBLE);
                }

                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                if (friendlyMessage.getText() != null) {
                    if (friendlyMessage.getFrom().equals(getSPData("user_id"))) {
                        viewHolder.receivedMessageLayout.setVisibility(View.GONE);
                        viewHolder.sentMessageLayout.setVisibility(View.VISIBLE);
                        viewHolder.messageTextView.setText(friendlyMessage.getText());
                        viewHolder.textLayoutSent.setVisibility(TextView.VISIBLE);
                        viewHolder.imageLayoutSent.setVisibility(ImageView.GONE);

                        if(friendlyMessage.getUploadedTime() != -1) {
                            viewHolder.statusImageViewText.setImageResource(R.drawable.chat_tick_ic);
                        } else viewHolder.statusImageViewText.setImageResource(R.drawable.chat_loading_ic);

                        viewHolder.timeTextViewForText.setText(time);

                    } else {
                        viewHolder.receivedMessageLayout.setVisibility(View.VISIBLE);
                        viewHolder.sentMessageLayout.setVisibility(View.GONE);
                        viewHolder.messageTextViewReceived.setText(friendlyMessage.getText());
                        viewHolder.textLayoutReceived.setVisibility(TextView.VISIBLE);
                        viewHolder.imageLayoutReceived.setVisibility(ImageView.GONE);

                        if(friendlyMessage.getUploadedTime() != -1) {
                            viewHolder.timeTextViewForTextReceived.setText(uploadedTime);
                        } else {
                            viewHolder.timeTextViewForTextReceived.setText(time);
                        }

                    }

                } else if (friendlyMessage.getImageUrl() != null && !friendlyMessage.getImageUrl().equals("qwe")) {

                    String imageUrl = friendlyMessage.getImageUrl();
                    if (imageUrl.startsWith("gs://")) {
                        StorageReference storageReference = FirebaseStorage.getInstance()
                                .getReferenceFromUrl(imageUrl);
                        storageReference.getDownloadUrl().addOnCompleteListener(
                                new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            String downloadUrl = task.getResult().toString();
                                            if (friendlyMessage.getFrom().equals(getSPData("user_id"))) {
                                                viewHolder.receivedMessageLayout.setVisibility(View.GONE);
                                                viewHolder.sentMessageLayout.setVisibility(View.VISIBLE);
                                                Glide.with(viewHolder.messageImageView.getContext())
                                                        .load(downloadUrl)
                                                        .apply(finalRequestOptions)
                                                        .into(viewHolder.messageImageView);

                                                if(friendlyMessage.getUploadedTime() != -1) {
                                                    viewHolder.statusImageViewImage.setImageResource(R.drawable.chat_tick_ic);
                                                } else viewHolder.statusImageViewImage.setImageResource(R.drawable.chat_loading_ic);

                                                viewHolder.timeTextViewForImage.setText(time);

                                            } else {
                                                viewHolder.receivedMessageLayout.setVisibility(View.VISIBLE);
                                                viewHolder.sentMessageLayout.setVisibility(View.GONE);
                                                Glide.with(viewHolder.messageImageViewReceived.getContext())
                                                        .load(downloadUrl)
                                                        .apply(finalRequestOptions)
                                                        .into(viewHolder.messageImageViewReceived);

                                                if(friendlyMessage.getUploadedTime() != -1) {
                                                    viewHolder.timeTextViewForImageReceived.setText(uploadedTime);
                                                } else {
                                                    viewHolder.timeTextViewForImageReceived.setText(time);
                                                }

                                            }
                                        } else {
                                            Log.e("ASD", "Getting download url was not successful.",
                                                    task.getException());
                                        }
                                    }
                                });
                    } else {
                        if (friendlyMessage.getFrom().equals(getSPData("user_id"))) {
                            viewHolder.receivedMessageLayout.setVisibility(View.GONE);
                            viewHolder.sentMessageLayout.setVisibility(View.VISIBLE);
                            Glide.with(viewHolder.messageImageView.getContext())
                                    .load(friendlyMessage.getImageUrl())
                                    .apply(finalRequestOptions)
                                    .into(viewHolder.messageImageView);

                            if(friendlyMessage.getUploadedTime() != -1) {
                                viewHolder.statusImageViewImage.setImageResource(R.drawable.chat_tick_ic);
                            } else viewHolder.statusImageViewImage.setImageResource(R.drawable.chat_loading_ic);

                            viewHolder.timeTextViewForImage.setText(time);

                        } else {
                            viewHolder.receivedMessageLayout.setVisibility(View.VISIBLE);
                            viewHolder.sentMessageLayout.setVisibility(View.GONE);
                            Glide.with(viewHolder.messageImageViewReceived.getContext())
                                    .load(friendlyMessage.getImageUrl())
                                    .apply(finalRequestOptions)
                                    .into(viewHolder.messageImageViewReceived);

                            if(friendlyMessage.getUploadedTime() != -1) {
                                viewHolder.timeTextViewForImageReceived.setText(uploadedTime);
                            } else {
                                viewHolder.timeTextViewForImageReceived.setText(time);
                            }

                        }
                    }
                    if (friendlyMessage.getFrom().equals(getSPData("user_id"))) {
                        viewHolder.receivedMessageLayout.setVisibility(View.GONE);
                        viewHolder.sentMessageLayout.setVisibility(View.VISIBLE);
                        viewHolder.textLayoutSent.setVisibility(TextView.GONE);
                        viewHolder.imageLayoutSent.setVisibility(ImageView.VISIBLE);
                    } else {
                        viewHolder.receivedMessageLayout.setVisibility(View.VISIBLE);
                        viewHolder.sentMessageLayout.setVisibility(View.GONE);
                        viewHolder.imageLayoutReceived.setVisibility(ImageView.VISIBLE);
                        viewHolder.textLayoutReceived.setVisibility(TextView.GONE);
                    }
                    viewHolder.messageImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Chat.this, ChatImage.class);
                            intent.putExtra("imageUrl", friendlyMessage.getImageUrl());
                            startActivity(intent);
                        }
                    });
                }

                viewHolder.sentMessageLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(position < mFirebaseAdapter.getItemCount()) {

                            Log.e("ASD", position + " - " + mFirebaseAdapter.getRef(position).getKey());

                            /*mFirebaseDatabaseReference.child("qwerty").child(mFirebaseAdapter.getRef(position)
                                    .getKey()).child("text").setValue("QWERTY");*/
                        };
                    }
                });

            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {

                    mMessageRecyclerView.scrollToPosition(positionStart);
                    /*RecyclerView.ViewHolder holder = mMessageRecyclerView.findViewHolderForAdapterPosition(lastVisiblePosition);
                    if(holder != null) {
                        TextView counter = holder.itemView.findViewById(R.id.unreadMessagesCounter);
                        counter.setVisibility(View.VISIBLE);
                        counter.setText(String.format("%d unread messages", itemCount));
                    }*/
                }
            }
        });

        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);

        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    sendButton.setEnabled(true);
                } else {
                    sendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mMessageEditText.getText().toString().trim().isEmpty()
                        || !(mMessageEditText.getText().toString().trim().length() == 0)) {
                    Message friendlyMessage = new Message(getSPData("user_id"), userData.optString("user_id"),
                            mMessageEditText.getText().toString().trim(), null, currentUserData.optString("name"),
                            userData.optString("name"));
                    mFirebaseDatabaseReference.child(userData.optString("matched_id")).
                            push().setValue(friendlyMessage, new DatabaseReference.CompletionListener() {
                        public void onComplete(DatabaseError error, DatabaseReference ref) {
                            if (error == null) {
                                ref.child("uploadedTime").setValue(new Date().getTime());
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Unable to send the message", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    mFirebaseDatabaseReference.child("notifications").push().setValue(friendlyMessage);
                    mMessageEditText.setText("");
                }
            }
        });

        mAddMessageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(Chat.this);
                View sheetView = getLayoutInflater().inflate(R.layout.dialog_image_chooser, null);
                mBottomSheetDialog.setContentView(sheetView);
                mBottomSheetDialog.show();

                LinearLayout fromCamera = (LinearLayout) sheetView.findViewById(R.id.bottom_sheet_camera);
                LinearLayout fromGallery = (LinearLayout) sheetView.findViewById(R.id.bottom_sheet_gallery);

                fromCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        mBottomSheetDialog.dismiss();
                        File file = new File(getApplicationContext().getExternalCacheDir(),
                                String.valueOf(System.currentTimeMillis()) + ".jpg");
                        outputFileUri = Uri.fromFile(file);
                        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                        startActivityForResult(captureIntent, 1001);
                    }
                });

                fromGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent galleryIntent = new Intent();
                        galleryIntent.setType("image/*");
                        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                        mBottomSheetDialog.dismiss();
                        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), 1002);
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1002) {

                if (data != null) {
                    outputFileUri = data.getData();
                    Log.e("ASd", outputFileUri.toString());
                }

            }
            if (requestCode == 1001) {
                Log.e("ASd", outputFileUri.getLastPathSegment());

            }

            if (outputFileUri != null) {
                Message tempMessage = new Message(getSPData("user_id"), userData.optString("user_id"), null,
                        "qwe", currentUserData.optString("name"), userData.optString("name"));
                mFirebaseDatabaseReference.child(userData.optString("matched_id")).push()
                        .setValue(tempMessage, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError,
                                                   DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    String key = databaseReference.getKey();
                                    StorageReference storageReference =
                                            FirebaseStorage.getInstance()
                                                    .getReference(userData.optString("matched_id"))
                                                    .child(key)
                                                    .child(outputFileUri.getLastPathSegment());

                                    putImageInStorage(storageReference, outputFileUri, key);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Unable to send the message", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void putImageInStorage(final StorageReference storageReference, Uri uri, final String key) {
        storageReference.putFile(uri).addOnCompleteListener(Chat.this,
                new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (task.isSuccessful()) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;
                                    Message friendlyMessage =
                                            new Message(getSPData("user_id"), userData.optString("user_id"), null,
                                                    downloadUrl.toString(), currentUserData.optString("name"),
                                                    userData.optString("name"));
                                    friendlyMessage.setUploadedTime(new Date().getTime());
                                    mFirebaseDatabaseReference.child(userData.optString("matched_id")).child(key)
                                            .setValue(friendlyMessage, new DatabaseReference.CompletionListener() {
                                        public void onComplete(DatabaseError error, DatabaseReference ref) {
                                            if (error == null) {
                                                ref.child("uploadedTime").setValue(new Date().getTime());
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(), "Unable to send the message", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    mFirebaseDatabaseReference.child("notifications").push().setValue(friendlyMessage);
                                }
                            });

                        } else {
                            Log.e("qwe", "Image upload task was not successful.",
                                    task.getException());
                        }
                    }
                });
    }

    @Override
    public void onPause() {
        mFirebaseAdapter.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAdapter.startListening();
    }

    @Override
    protected void onDestroy() {
        mFirebaseAdapter.stopListening();
        super.onDestroy();
    }

    private void storeSPData(String key, boolean data) {

        SharedPreferences mUserData = this.getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor mUserEditor = mUserData.edit();
        mUserEditor.putBoolean(key, data);
        mUserEditor.commit();

    }

    private String getSPData(String key) {

        SharedPreferences mUserData = this.getSharedPreferences("UserData", MODE_PRIVATE);
        String data = mUserData.getString(key, "");

        return data;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
