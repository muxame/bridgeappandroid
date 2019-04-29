package net.bridgeint.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.bridgeint.app.R;
import net.bridgeint.app.activities.DocumentsActivity;
import net.bridgeint.app.utils.Constants;

import java.util.List;


/**
 * Created by devicebee on 08/01/2018.
 */

public class ImageAdapter extends
        RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Context ctx;
    private List<String> listItems;
    private int list;
    private DocumentsActivity documentUploadfragment;

    public ImageAdapter(Context context, List<String> listItems, int size) {
        this.listItems = listItems;
        this.list = size;
        this.ctx = context;
    }

    public ImageAdapter(Context context, List<String> listItems, int size, DocumentsActivity docuFragment) {
        this.listItems = listItems;
        this.list = size;
        this.ctx = context;
        this.documentUploadfragment = docuFragment;
    }

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_img, parent, false);
        ImageAdapter.ViewHolder viewHolder = new ImageAdapter.ViewHolder(ctx, contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ImageAdapter.ViewHolder holder, int position) {
        final String item = listItems.get(position);


        Picasso.with(ctx).load(Constants.IMAGE_URL + item).placeholder(R.drawable.logo_update).resize(200, 200).error(R.drawable.logo).into(holder.img);

        holder.del_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItems.remove(item);
                list = list - 1;
                documentUploadfragment.updateList();
                notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public com.makeramen.roundedimageview.RoundedImageView img;
        public ImageView del_img;
        public Context ctx;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(Context context, View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            del_img = itemView.findViewById(R.id.delete_img);
            ctx = context;
        }

        @Override
        public void onClick(View v) {

        }
    }
}
