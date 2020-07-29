package com.example.groomzoom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BookingAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> values;
    private final ArrayList<String> availability;

    public BookingAdapter(Context context, ArrayList<String> values, ArrayList<String> availability){
        super(context, R.layout.rowlayout, values);
        this.context = context;
        this.values = values;
        this.availability = availability;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.rowlayout, parent, false);

        // Displaying a textview
        TextView textView =  (TextView) rowView.findViewById(R.id.label);
        TextView tv2 = (TextView) rowView.findViewById(R.id.tvAvailable);
        textView.setText(values.get(position));
        tv2.setText(availability.get(position));

        return rowView;
    }
}
