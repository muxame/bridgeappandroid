package net.bridgeint.app.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.bridgeint.app.R;
import net.bridgeint.app.models.UploadModel;
import net.bridgeint.app.singelton.Singelton;

import java.util.ArrayList;

public class Upload_Adapter extends RecyclerView.Adapter<Upload_Adapter.ViewHolder> {
    private ArrayList<UploadModel> uploadList;
    private Context ctx;
    private int lastClicked = -1;
    ArrayList<Boolean> listChecks = new ArrayList<>();
    private static Upload_Adapter.MyClickListener myClickListener;
    Singelton temp = Singelton.getInstance();
    private ImageAdapter imageAdapter;

    //update
    public interface MyClickListener {
        void onItemClick(int position);
    }

    @Override
    public Upload_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_document, parent, false);
        Upload_Adapter.ViewHolder viewHolder = new Upload_Adapter.ViewHolder(ctx, contactView);
        return viewHolder;
    }

    public Upload_Adapter(Context context, ArrayList<UploadModel> listItems) {
        this.uploadList = listItems;
        ctx = context;
//        ApplyModel.highSchoolTranscript = new ArrayList<>();
//        ApplyModel.recommendationLetters = new ArrayList<>();
//        ApplyModel.passportCopies = new ArrayList<>();
//        ApplyModel.personalStatment = new ArrayList<>();
//        ApplyModel.englishProfeciencyTest = new ArrayList<>();

        for (int i = 0; i < listItems.size(); i++) {
            listChecks.add(false);
        }

    }

    public void setOnItemClickListener(Upload_Adapter.MyClickListener myClickListener) {
        Upload_Adapter.myClickListener = myClickListener;
    }

    RecyclerView rvImages;


    private void setImages(ArrayList<String> listImages) {

    }


    @Override
    public void onBindViewHolder(final Upload_Adapter.ViewHolder holder, final int position) {

//        UploadModel uploadModel = uploadList.get(position);
        RelativeLayout lo = holder.loDocument;
        ImageView ibDocument = holder.ibDocument;
        TextView tvTitle = holder.tvTitle;
        TextView tvCopies = holder.tvCopies;
        tvTitle.setText(uploadList.get(position).getName());

//        tvCopies.setText(ApplyModel.passportCopies.size() + " copies");


        ArrayList<String> stringArrayList = new ArrayList<>();
        for (UploadModel.ImageBean imageBean:uploadList.get(position).getImage()
             ) {
            stringArrayList.add(imageBean.getName());
        }
        imageAdapter = new ImageAdapter(ctx,stringArrayList ,uploadList.get(position).getImage().size());
        holder.img_recycler.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
        holder.img_recycler.setAdapter(imageAdapter);
        tvCopies.setText(uploadList.get(position).getImage().size() + " "+ctx.getResources().getString(R.string.copies));

//        if (position == 0) {
//            if (uploadList.size() > 0) {
//                for (int j = 0; j < uploadList.size(); j++) {
//                    if (uploadList.get(j).getImage().size() > 0) {
//                        List<UploadModel.ImageBean> imageBeans = uploadList.get(j).getImage();
//                        for (int i = 0; i < imageBeans.size(); i++) {
//                            ApplyModel.highSchoolTranscript.add(imageBeans.get(i).getName());
//                        }
//                    }
//                }
//            }
//            if (ApplyModel.highSchoolTranscript == null) {
//                tvCopies.setText("0 copies");
//            } else {
//                tvCopies.setText(ApplyModel.highSchoolTranscript.size() + " copies");
//                imageAdapter = new ImageAdapter(ctx, ApplyModel.highSchoolTranscript, ApplyModel.highSchoolTranscript.size());
//                holder.img_recycler.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
//                holder.img_recycler.setAdapter(imageAdapter);
//                imageAdapter.notifyDataSetChanged();
//            }
//        } else if (position == 1) {
//            if (uploadList.size() > 0) {
//                for (int j = 0; j < uploadList.size(); j++) {
//
//
//                    if (uploadList.get(j).getImage().size() > 0) {
//                        List<UploadModel.ImageBean> imageBeans = uploadList.get(j).getImage();
//                        for (int i = 0; i < imageBeans.size(); i++) {
//                            ApplyModel.recommendationLetters.add(imageBeans.get(i).getName());
//                        }
//                    }
//                }
//            }
//            if (ApplyModel.recommendationLetters == null) {
//                tvCopies.setText("0 copies");
//            } else {
//                tvCopies.setText(ApplyModel.recommendationLetters.size() + " copies");
//                imageAdapter = new ImageAdapter(ctx, ApplyModel.recommendationLetters, ApplyModel.recommendationLetters.size());
//                holder.img_recycler.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
//                holder.img_recycler.setAdapter(imageAdapter);
//
//                imageAdapter.notifyDataSetChanged();
//            }
//        } else if (position == 2) {
//            if (uploadList.size() > 0 && uploadList != null) {
//                for (int j = 0; j < uploadList.size(); j++) {
//                    if (uploadList.get(j).getImage().size() > 0) {
//                        List<UploadModel.ImageBean> imageBeans = uploadList.get(j).getImage();
//                        for (int i = 0; i < imageBeans.size(); i++) {
//                            ApplyModel.passportCopies.add(imageBeans.get(i).getName());
//                        }
//                    }
//                }
//            }
//            if (ApplyModel.passportCopies == null) {
//                tvCopies.setText("0 copies");
//            } else {
//                tvCopies.setText(ApplyModel.passportCopies.size() + " copies");
//                imageAdapter = new ImageAdapter(ctx, ApplyModel.passportCopies, ApplyModel.passportCopies.size());
//                holder.img_recycler.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
//                holder.img_recycler.setAdapter(imageAdapter);
//                imageAdapter.notifyDataSetChanged();
//            }
//        } else if (position == 3) {
//            if (uploadList.size() > 0 && uploadList != null) {
//                for (int j = 0; j < uploadList.size(); j++) {
//
//
//                    if (uploadList.get(j).getImage().size() > 0) {
//                        List<UploadModel.ImageBean> imageBeans = uploadList.get(j).getImage();
//                        for (int i = 0; i < imageBeans.size(); i++) {
//                            ApplyModel.englishProfeciencyTest.add(imageBeans.get(i).getName());
//                        }
//                    }
//                }
//            }
//            if (ApplyModel.englishProfeciencyTest == null) {
//                tvCopies.setText("0 copies");
//            } else {
//                tvCopies.setText(ApplyModel.englishProfeciencyTest.size() + " copies");
//                imageAdapter = new ImageAdapter(ctx, ApplyModel.englishProfeciencyTest, ApplyModel.englishProfeciencyTest.size());
//                holder.img_recycler.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
//                holder.img_recycler.setAdapter(imageAdapter);
//                imageAdapter.notifyDataSetChanged();
//            }
//        } else if (position == 4) {
//            if (uploadList.size() > 0 && uploadList != null) {
//                for (int j = 0; j < uploadList.size(); j++) {
//
//
//                    if (uploadList.get(j).getImage().size() > 0) {
//
//                        List<UploadModel.ImageBean> imageBeans = uploadList.get(j).getImage();
//                        for (int i = 0; i < imageBeans.size(); i++) {
//                            ApplyModel.personalStatment.add(imageBeans.get(i).getName());
//                        }
//                    }
//                }
//            }
//            if (ApplyModel.personalStatment == null) {
//                tvCopies.setText("0 copies");
//            } else {
//                tvCopies.setText(ApplyModel.personalStatment.size() + " copies");
//                imageAdapter = new ImageAdapter(ctx, ApplyModel.personalStatment, ApplyModel.personalStatment.size());
//                holder.img_recycler.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
//                holder.img_recycler.setAdapter(imageAdapter);
//                imageAdapter.notifyDataSetChanged();
//            }
//        }
        int drawable = 0;
        switch (position) {
            case 0:
                drawable = R.drawable.ic_transcript;
                break;

            case 1:
                drawable = R.drawable.ic_recommendation;
                break;

            case 2:
                drawable = R.drawable.ic_passport;
                break;

            case 3:
                drawable = R.drawable.ic_test;
                break;

            case 4:
                drawable = R.drawable.ic_personal;
                break;

            case 5:
                drawable = R.drawable.ic_personal;
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

    }

    @Override
    public int getItemCount() {
        return uploadList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RelativeLayout loDocument;
        public ImageView ibDocument;
        public Button ibAdd;
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

    public void updateList(ArrayList<UploadModel> uploadModels) {
        this.uploadList = uploadModels;
        notifyDataSetChanged();
    }
}
