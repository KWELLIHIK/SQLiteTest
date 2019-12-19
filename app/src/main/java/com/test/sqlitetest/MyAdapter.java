package com.test.sqlitetest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>
{
    private ArrayList<Abonent> abonents;
    public OnItemClickListener itemClickListener;
    private ArrayList<Abonent> copy;
    //----------------------------------------------------------------------------------------------
    public MyAdapter(ArrayList<Abonent> abonents, OnItemClickListener itemClickListener)
    {
        this.abonents = abonents;
        this.copy = new ArrayList<Abonent>();
        this.copy.addAll(abonents);
        this.itemClickListener = itemClickListener;
    }
    //----------------------------------------------------------------------------------------------
    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item,
                viewGroup,false);
        return new ViewHolder(v);
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder viewHolder, int i)
    {
        viewHolder.itemName.setText(abonents.get(i).getName());
        viewHolder.itemNumber.setText(abonents.get(i).getNumber());
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public int getItemCount()
    {
        return abonents.size();
    }
    //----------------------------------------------------------------------------------------------
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView itemName;
        private TextView itemNumber;

        private ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemNumber = itemView.findViewById(R.id.item_number);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            itemClickListener.onItemClick(v, getLayoutPosition(), false, abonents);
        }
    }
    public interface OnItemClickListener
    {
        void onItemClick(View v, int position, boolean isLongClick, ArrayList<Abonent> abonents);
    }
    //----------------------------------------------------------------------------------------------
    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        abonents.clear();
        if (charText.length() == 0)
        {
            abonents.addAll(copy);
        } else
            {
            for (Abonent wp : copy)
            {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText) || wp.getNumber().contains(charText))
                {
                    abonents.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
