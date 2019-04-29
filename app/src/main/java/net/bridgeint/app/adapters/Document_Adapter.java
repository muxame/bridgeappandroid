package net.bridgeint.app.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.bridgeint.app.R;
import net.bridgeint.app.activities.DocumentsActivity;
import net.bridgeint.app.models.WriteOnly.ApplyModel;
import net.bridgeint.app.singelton.Singelton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ufraj on 10/21/2016.
 */
public class Document_Adapter extends
        RecyclerView.Adapter<Document_Adapter.ViewHolder> {
    private List<String> listItems;
    private Context ctx;
    private int lastClicked = -1;
    ArrayList<Boolean> listChecks = new ArrayList<>();
    private static MyClickListener myClickListener;
    Singelton temp = Singelton.getInstance();
    private ImageAdapter imageAdapter;
    private DocumentsActivity documentUploadfragment;
    //update
    public interface MyClickListener {
        void onItemClick(int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_document, parent, false);
        ViewHolder viewHolder = new ViewHolder(ctx, contactView);
        return viewHolder;
    }

    public Document_Adapter(Context context, List<String> listItems) {
        this.listItems = listItems;
        ctx = context;

        for (int i = 0; i < listItems.size(); i++) {
            listChecks.add(false);
        }

    }
    public Document_Adapter(Context context, List<String> listItems, DocumentsActivity fragment) {
        this.listItems = listItems;
        ctx = context;
this.documentUploadfragment = fragment;
        for (int i = 0; i < listItems.size(); i++) {
            listChecks.add(false);
        }

    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        Document_Adapter.myClickListener = myClickListener;
    }

    RecyclerView rvImages;


    private void setImages(ArrayList<String> listImages) {

    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            final String item = listItems.get(position);

            LinearLayout lo = holder.loDocument;
            ImageView ibDocument = holder.ibDocument;
            TextView tvTitle = holder.tvTitle;
            TextView tvCopies = holder.tvCopies;
            tvTitle.setText(item);


            if (position == 0) {
                if (ApplyModel.highSchoolTranscript == null) {
                    holder.ibClear.setVisibility(View.INVISIBLE);
                    tvCopies.setText("0 "+ctx.getResources().getString(R.string.copies));
                } else {
                    tvCopies.setText(ApplyModel.highSchoolTranscript.size() + " "+ctx.getResources().getString(R.string.copies));
                    imageAdapter = new ImageAdapter(ctx, ApplyModel.highSchoolTranscript, ApplyModel.highSchoolTranscript.size(),documentUploadfragment);
                    holder.img_recycler.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
                    holder.img_recycler.setAdapter(imageAdapter);
                    if (ApplyModel.highSchoolTranscript != null && ApplyModel.highSchoolTranscript.size() > 0){
                        holder.ibClear.setImageResource(R.drawable.ic_check_upload_doc);
                        holder.ibClear.setVisibility(View.VISIBLE);
                    }else {
                        holder.ibClear.setVisibility(View.INVISIBLE);
                        holder.ibClear.setImageResource(R.drawable.ic_cancel);
                    }
                    imageAdapter.notifyDataSetChanged();
                }
            } else if (position == 1) {
                if (ApplyModel.recommendationLetters == null) {
                    holder.ibClear.setVisibility(View.INVISIBLE);
                    tvCopies.setText("0 "+ctx.getResources().getString(R.string.copies));
                } else {
                    tvCopies.setText(ApplyModel.recommendationLetters.size() + " "+ctx.getResources().getString(R.string.copies));
                    imageAdapter = new ImageAdapter(ctx, ApplyModel.recommendationLetters, ApplyModel.recommendationLetters.size(),documentUploadfragment);
                    holder.img_recycler.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
                    holder.img_recycler.setAdapter(imageAdapter);
                    if (ApplyModel.recommendationLetters != null && ApplyModel.recommendationLetters.size() > 0){
                        holder.ibClear.setImageResource(R.drawable.ic_check_upload_doc);
                        holder.ibClear.setVisibility(View.VISIBLE);
                    }else {
                        holder.ibClear.setVisibility(View.INVISIBLE);
                        holder.ibClear.setImageResource(R.drawable.ic_cancel);
                    }

                    imageAdapter.notifyDataSetChanged();
                }
            } else if (position == 2) {
                if (ApplyModel.passportCopies == null) {
                    holder.ibClear.setVisibility(View.INVISIBLE);
                    tvCopies.setText("0 "+ctx.getResources().getString(R.string.copies));
                } else {
                    tvCopies.setText(ApplyModel.passportCopies.size() + " "+ctx.getResources().getString(R.string.copies));
                    imageAdapter = new ImageAdapter(ctx, ApplyModel.passportCopies, ApplyModel.passportCopies.size(),documentUploadfragment);
                    holder.img_recycler.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
                    holder.img_recycler.setAdapter(imageAdapter);
                    if (ApplyModel.passportCopies != null && ApplyModel.passportCopies.size() > 0){
                        holder.ibClear.setImageResource(R.drawable.ic_check_upload_doc);
                        holder.ibClear.setVisibility(View.VISIBLE);
                    }else {
                        holder.ibClear.setVisibility(View.INVISIBLE);
                        holder.ibClear.setImageResource(R.drawable.ic_cancel);
                    }
                    imageAdapter.notifyDataSetChanged();
                }
            } else if (position == 3) {
                if (ApplyModel.englishProfeciencyTest == null) {
                    holder.ibClear.setVisibility(View.INVISIBLE);
                    tvCopies.setText("0 "+ctx.getResources().getString(R.string.copies));
                } else {
                    tvCopies.setText(ApplyModel.englishProfeciencyTest.size() + " "+ctx.getResources().getString(R.string.copies));
                    imageAdapter = new ImageAdapter(ctx, ApplyModel.englishProfeciencyTest, ApplyModel.englishProfeciencyTest.size(),documentUploadfragment);
                    holder.img_recycler.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
                    holder.img_recycler.setAdapter(imageAdapter);
                    if (ApplyModel.englishProfeciencyTest != null && ApplyModel.englishProfeciencyTest.size() > 0){
                        holder.ibClear.setImageResource(R.drawable.ic_check_upload_doc);
                        holder.ibClear.setVisibility(View.VISIBLE);
                    }else {
                        holder.ibClear.setVisibility(View.INVISIBLE);
                        holder.ibClear.setImageResource(R.drawable.ic_cancel);
                    }
                    imageAdapter.notifyDataSetChanged();
                }
            } else if (position == 4) {
                if (ApplyModel.personalStatment == null) {
                    holder.ibClear.setVisibility(View.INVISIBLE);
                    tvCopies.setText("0 "+ctx.getResources().getString(R.string.copies));
                } else {
                    tvCopies.setText(ApplyModel.personalStatment.size() + " "+ctx.getResources().getString(R.string.copies));
                    imageAdapter = new ImageAdapter(ctx, ApplyModel.personalStatment, ApplyModel.personalStatment.size(),documentUploadfragment);
                    holder.img_recycler.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
                    holder.img_recycler.setAdapter(imageAdapter);
                    if (ApplyModel.personalStatment != null && ApplyModel.personalStatment.size() > 0){
                        holder.ibClear.setImageResource(R.drawable.ic_check_upload_doc);
                        holder.ibClear.setVisibility(View.VISIBLE);
                    }else {
                        holder.ibClear.setVisibility(View.INVISIBLE);
                        holder.ibClear.setImageResource(R.drawable.ic_cancel);
                    }
                    imageAdapter.notifyDataSetChanged();
                }
            }else if (position == 5) {
                if (ApplyModel.extraDocuments == null) {
                    holder.ibClear.setVisibility(View.INVISIBLE);
                    tvCopies.setText("0 "+ctx.getResources().getString(R.string.copies));
                } else {
                    tvCopies.setText(ApplyModel.extraDocuments.size() + " "+ctx.getResources().getString(R.string.copies));
                    imageAdapter = new ImageAdapter(ctx, ApplyModel.extraDocuments, ApplyModel.extraDocuments.size(),documentUploadfragment);
                    holder.img_recycler.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
                    holder.img_recycler.setAdapter(imageAdapter);
                    if (ApplyModel.extraDocuments != null && ApplyModel.extraDocuments.size() > 0){
                        holder.ibClear.setImageResource(R.drawable.ic_check_upload_doc);
                        holder.ibClear.setVisibility(View.VISIBLE);
                    }else {
                        holder.ibClear.setVisibility(View.INVISIBLE);
                        holder.ibClear.setImageResource(R.drawable.ic_cancel);
                    }
                    imageAdapter.notifyDataSetChanged();
                }
            }
            int drawable = 0;
            switch (position) {
                case 0:
                    drawable = R.drawable.ic_transcript_update;
                    break;

                case 1:
                    drawable = R.drawable.ic_recommandation_latter;
                    break;

                case 2:
                    drawable = R.drawable.ic_passport_update;
                    break;

                case 3:
                    drawable = R.drawable.ic_english_update;
                    break;

                case 4:
                    drawable = R.drawable.ic_transcript_update;
                    break;
                case 5:
                    drawable = R.drawable.ic_transcript_update;
                    break;

                default:
                    break;
            }
            ibDocument.setImageResource(drawable);
            holder.ibAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    myClickListener.onItemClick(position);
                    //check(position);

                }
            });
        } catch (Exception e) {
            Log.e("adapter ", e.toString());
            e.printStackTrace();
        }
    }

    private void check(int position) {
        for (int i = 0; i < listChecks.size(); i++) {
            if (i == position) {
                if (lastClicked == position) {
                    listChecks.set(i, false);
                    lastClicked = -1;
                    temp.setFlagDegree(false);

                } else {
                    listChecks.set(i, true);
                    lastClicked = i;
                    temp.setFlagDegree(true);

                }


            } else {
                listChecks.set(i, false);
            }


        }
        notifyDataSetChanged();


    }


    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public LinearLayout loDocument;
        public ImageView ibDocument;
        public ImageView ibAdd;
        public ImageView ibClear;
        public TextView tvTitle;
        public TextView tvCopies;
        public RecyclerView img_recycler;
        public Context ctx;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(Context context, View itemView) {
            super(itemView);
            loDocument = itemView.findViewById(R.id.loDocument);
            ibDocument = itemView.findViewById(R.id.ibDocument);
            ibAdd = itemView.findViewById(R.id.ibAdd);
            ibClear = itemView.findViewById(R.id.ibClear);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCopies = itemView.findViewById(R.id.tvCopies);
            img_recycler = itemView.findViewById(R.id.rvImages);
            ctx = context;
        }

        @Override
        public void onClick(View v) {

        }
    }

    public void updateList() {
        notifyDataSetChanged();
    }
}
