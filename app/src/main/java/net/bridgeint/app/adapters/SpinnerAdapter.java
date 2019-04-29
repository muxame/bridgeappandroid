package net.bridgeint.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.bridgeint.app.R;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<String> {
    int groupid;
    Activity context;
    ArrayList<String> list;
    ArrayList<Integer> listImages;
    LayoutInflater inflater;

    public SpinnerAdapter(Activity context, int groupid, int id, ArrayList<String>
            list, ArrayList<Integer>
                                  listImages) {
        super(context, id, list);
        this.list = list;
        this.listImages = listImages;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupid = groupid;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = inflater.inflate(groupid, parent, false);
        ImageView imageView = itemView.findViewById(R.id.img);

        imageView.setImageResource(listImages.get(position));
        TextView textView = itemView.findViewById(R.id.txt);
        textView.setText(list.get(position));
        return itemView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup
            parent) {
        return getView(position, convertView, parent);

    }
}