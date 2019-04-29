package net.bridgeint.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import net.bridgeint.app.R;
import net.bridgeint.app.activities.ChatActivity;
import net.bridgeint.app.activities.ImagesActivity;
import net.bridgeint.app.models.BannerModelForAdapter;
import net.bridgeint.app.models.ChatModel.Message;
import net.bridgeint.app.models.MessageModel;
import net.bridgeint.app.utils.Constants;
import net.bridgeint.app.utils.Utility;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    Context context = null;
    ArrayList<MessageModel> chatList = new ArrayList<>();
    ChatActivity fragment;
    private final int SENDING_CELL_TEXT = 0;
    private final int RECEIVING_CELL_TEXT = 1;
    private final int SENDING_CELL_IMAGE = 2;
    private final int RECEIVING_CELL_IMAGE = 3;
    private final int SENDING_CELL_AUDIO = 4;
    private final int RECEIVING_CELL_AUDIO = 5;
    private final int DATE_CELL = 6;
    Picasso mPicasso;
    ArrayList<BannerModelForAdapter> banners2 = new ArrayList<>();

    public ChatAdapter(ChatActivity fragment, Context context, ArrayList<MessageModel> chatList) {
        this.context = context;
        this.fragment = fragment;
        this.chatList = chatList;
        mPicasso = Picasso.with(context);
        mPicasso.setIndicatorsEnabled(true);
    }

    @Override
    public int getItemViewType(int position) {
        if (chatList.get(position).getId() == -1) {
            return DATE_CELL;
        } else {
            if (chatList.get(position).isSenderMessage()) {
                if (chatList.get(position).getFileType() == Constants.TEXT_TYPE) {
                    return SENDING_CELL_TEXT;
                } else if (chatList.get(position).getFileType() == Constants.IMAGE_TYPE) {
                    return SENDING_CELL_IMAGE;
                } else {
                    return SENDING_CELL_AUDIO;
                }
            } else {
                if (chatList.get(position).getFileType() == Constants.TEXT_TYPE) {
                    return RECEIVING_CELL_TEXT;
                } else if (chatList.get(position).getFileType() == Constants.IMAGE_TYPE) {
                    return RECEIVING_CELL_IMAGE;
                } else {
                    return RECEIVING_CELL_AUDIO;
                }
            }
        }
    }

    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == SENDING_CELL_TEXT) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_chat_send_msg, parent, false);
        } else if (viewType == SENDING_CELL_IMAGE) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_chat_send_image, parent, false);
        } else if (viewType == SENDING_CELL_AUDIO) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_chat_send_audio, parent, false);
        } else if (viewType == RECEIVING_CELL_TEXT) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_chat_recieve_msg, parent, false);
        } else if (viewType == RECEIVING_CELL_IMAGE) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_chat_recieve_image, parent, false);
        } else if (viewType == RECEIVING_CELL_AUDIO) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_chat_recieve_audio, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_date_item, parent, false);
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ChatAdapter.ViewHolder viewHolder, int position) {
        MessageModel model = chatList.get(position);
        if (viewHolder.getItemViewType() == DATE_CELL) {
            viewHolder.date.setText(model.getTime());
        } else {
            Log.e("time", new Gson().toJson(model));
            String time = Utility.getMessageTime(model.getTime());
            if (time.contains("-")) {
                String[] split = model.getTime().split(" ");
                time = split[1];
            } else {
                time = model.getTime();
            }
            try {
                SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                _24HourSDF.setTimeZone(TimeZone.getDefault());
                _12HourSDF.setTimeZone(TimeZone.getDefault());
                Date _24HourDt = _24HourSDF.parse(time);
                time = _12HourSDF.format(_24HourDt);
            } catch (Exception exp) {
            }

            switch (viewHolder.getItemViewType()) {
                case SENDING_CELL_TEXT:
                    viewHolder.message.setText(model.getText());
                    viewHolder.sending_time.setText(time);
                    ChangeMessageStatus(model, viewHolder.statuc_icon);
                    break;
                case SENDING_CELL_IMAGE:
                    if (model.getFilePath() == null) {
                        mPicasso.load(model.getText()).placeholder(R.drawable.place_holder).resize(Constants.MIDIEUM_WIDTH, Constants.MIDIEUM_HEIGHT).centerCrop().into(viewHolder.image);
                    } else {
                        mPicasso.load(R.drawable.place_holder).into(viewHolder.image);
                    }
                    viewHolder.sending_time.setText(time);
                    ChangeMessageStatus(model, viewHolder.statuc_icon);
                    viewHolder.image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ImagesActivity.class);
                            intent.putExtra("position", 0);
                            intent.putExtra("from", "chat");
                            banners2 = new ArrayList<>();
                            banners2.add(new BannerModelForAdapter(model.getText(), false));
                            intent.putParcelableArrayListExtra("banners", banners2);
                            context.startActivity(intent);
                        }
                    });
                    break;
                case SENDING_CELL_AUDIO:
                    viewHolder.sending_time.setText(time);
                    if (model.getFilePath() != null) {
                        viewHolder.bindHolderToVoice(model.getFilePath());
                    } else {
                        viewHolder.bindHolderToVoice(model.getText());
                    }
                    ChangeMessageStatus(model, viewHolder.statuc_icon);
                    break;
                case RECEIVING_CELL_TEXT:
                    viewHolder.message.setText(model.getText());
                    viewHolder.recieving_time.setText(time);
                    break;
                case RECEIVING_CELL_IMAGE:
                    if (model.getFilePath() == null) {
                        mPicasso.load(model.getText()).placeholder(R.drawable.place_holder).resize(Constants.MIDIEUM_WIDTH, Constants.MIDIEUM_HEIGHT).centerCrop().into(viewHolder.image);
                    } else {
                        Uri recieve_uri = Uri.fromFile(new File(model.getFilePath()));
                        mPicasso.load(recieve_uri).placeholder(R.drawable.place_holder).into(viewHolder.image);
                        viewHolder.image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, ImagesActivity.class);
                                intent.putExtra("position", 0);
                                intent.putExtra("from", "chat");
                                banners2 = new ArrayList<>();
                                banners2.add(new BannerModelForAdapter(model.getText(), false));
                                intent.putParcelableArrayListExtra("banners", banners2);
                                context.startActivity(intent);
                            }
                        });
                    }
                    viewHolder.recieving_time.setText(time);
                    break;
                case RECEIVING_CELL_AUDIO:
                    break;
            }

        }
    }

    private void ChangeMessageStatus(MessageModel model, ImageView statuc_icon) {

        if (model.getIsRead() == 1) {
            statuc_icon.setVisibility(View.VISIBLE);
            statuc_icon.setImageResource(R.drawable.ic_view);
//            statuc_icon.setColorFilter(Color.argb(255, 255, 255, 255));
        } else {
            if (model.getMessage_status() == Constants.MESSAGE_STATE_PENDING) {
                statuc_icon.setVisibility(View.GONE);
            } else if (model.getMessage_status() == Constants.MESSAGE_STATE_SENT) {
                statuc_icon.setVisibility(View.VISIBLE);
                statuc_icon.setImageResource(R.drawable.ic_done_white_48dp);
                statuc_icon.setColorFilter(Color.argb(255, 255, 255, 255));
            } else if (model.getMessage_status() == Constants.MESSAGE_STATE_RECIEVED) {
                statuc_icon.setVisibility(View.VISIBLE);
                statuc_icon.setImageResource(R.drawable.ic_done_all_white_48dp);
                statuc_icon.setColorFilter(Color.argb(255, 255, 255, 255));
            }else {
                statuc_icon.setVisibility(View.VISIBLE);
                statuc_icon.setImageResource(R.drawable.ic_view);
            }

//            else {
//                statuc_icon.setVisibility(View.VISIBLE);
//                statuc_icon.setImageResource(R.drawable.ic_done_all_white_48dp);
////            statuc_icon.setColorFilter(context.getResources().getColor(R.color.colorPrimary));
//            }


        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public void updateMessage(Message message) {
        int updatingCellIndex = -1;
        for (int i = 0; i < chatList.size(); i++) {
            if (chatList.get(i).getTempId().equals(message.getTempId())) {
                updatingCellIndex = i;
                break;
            }
        }
        if (updatingCellIndex != -1) {
            String time;
            chatList.remove(updatingCellIndex);
            if (message.getCreated().contains("-")) {
                String[] split = message.getCreated().split(" ");
                time = split[0];
            } else {
                time = message.getCreated();
            }
            if (!fragment.dates.containsKey(time)) {
                fragment.dates.put(time, "found");
                time = Utility.convertDate(time);
                chatList.add(updatingCellIndex, new MessageModel(-1, message.getMessage(), time, message.getStatus(), Constants.TEXT_TYPE, null, message.getTempId() + "", false,0));
            }
            if (message.getType().equals(Constants.AUDIO_TYPE + "")) {
                chatList.add(updatingCellIndex, new MessageModel(0, Constants.IMAGE_URL + message.getMessage(), message.getCreated(), message.getStatus(), Constants.AUDIO_TYPE, null, message.getTempId(), true,0));
            } else if (message.getType().equals(Constants.IMAGE_TYPE + "")) {
                chatList.add(updatingCellIndex, new MessageModel(0, Constants.IMAGE_URL + message.getMessage(), message.getCreated(), message.getStatus(), Constants.IMAGE_TYPE, null, message.getTempId(), true,0));
            } else {
                chatList.add(updatingCellIndex, new MessageModel(0, message.getMessage(), message.getCreated(), message.getStatus(), Constants.TEXT_TYPE, null, message.getTempId(), true,0));
            }
            //notifyItemChanged(updatingCellIndex);
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView message, sending_time, recieving_time, date;
        ImageView statuc_icon, playBtn;
        RoundedImageView image;
        SeekBar seekBar;
        // For MediaPlayer
        double timeElapsed = 0;
        boolean isPlaying;
        MediaPlayer mPlayer;
        Handler durationHandler = new Handler();
        Runnable updateSeekBarTime;

        public ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);
            date = view.findViewById(R.id.date);
            message = view.findViewById(R.id.textArea);
            sending_time = view.findViewById(R.id.sending_time);
            recieving_time = view.findViewById(R.id.recieving_time);
            statuc_icon = view.findViewById(R.id.statuc_icon);
            playBtn = view.findViewById(R.id.playBtn);
            seekBar = view.findViewById(R.id.seekBar);
            isPlaying = false;
            mPlayer = new MediaPlayer();
        }

        public void bindHolderToVoice(final String path) {

            seekBar.setClickable(false);

            seekBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mPlayer.isPlaying()) {
                        SeekBar sb = (SeekBar) v;
                        mPlayer.seekTo(sb.getProgress());
                    }
                    return false;
                }
            });

            updateSeekBarTime = new Runnable() {
                public void run() {
                    if (mPlayer != null) {
                        if (mPlayer.isPlaying()) {
                            timeElapsed = mPlayer.getCurrentPosition();
                            seekBar.setProgress((int) timeElapsed);
                            durationHandler.postDelayed(this, 100);
                        } else {
                            mPlayer.pause();
                            isPlaying = false;
                            seekBar.setProgress(0);
                            playBtn.setImageResource(R.drawable.ic_play_circle);
                        }
                    }
                }
            };

            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    isPlaying = false;
                    playBtn.setImageResource(R.drawable.ic_play_circle);
                    seekBar.setProgress(0);
                }
            });

            playBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utility.isNetConnected(context)) {
                        if (!isPlaying) {
                            isPlaying = true;
                            playBtn.setImageResource(R.drawable.ic_stop_circle);
                            try {
                                mPlayer = new MediaPlayer();
                                mPlayer.setDataSource(path);
                                mPlayer.prepare();
                                seekBar.setMax(mPlayer.getDuration());
                            } catch (IOException e) {
                                Log.e("tag", "Start playing prepare() failed");
                                isPlaying = false;
                                playBtn.setImageResource(R.drawable.ic_play_circle);
                            }
                            mPlayer.start();
                            timeElapsed = mPlayer.getCurrentPosition();
                            seekBar.setProgress((int) timeElapsed);
                            durationHandler.postDelayed(updateSeekBarTime, 100);
                        } else {
                            isPlaying = false;
                            playBtn.setImageResource(R.drawable.ic_play_circle);
                            mPlayer.pause();
                        }
                    } else {
                        Utility.showToast(context, "Net Connection Lost");
                    }
                }
            });
        }
    }
}