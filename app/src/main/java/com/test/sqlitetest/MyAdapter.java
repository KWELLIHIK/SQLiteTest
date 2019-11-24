package com.test.sqlitetest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>
{
    String[] names;
    String[] numbers;

    public MyAdapter(String[] names, String[] numbers)
    {
        this.names = names;
        this.numbers = numbers;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder viewHolder, int i)
    {
        viewHolder.itemName.setText(names[i]);
        viewHolder.itemNumber.setText(numbers[i]);
    }

    @Override
    public int getItemCount()
    {
        return names.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView itemName;
        private TextView itemNumber;

        private ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemNumber = itemView.findViewById(R.id.item_number);
        }
    }
}
