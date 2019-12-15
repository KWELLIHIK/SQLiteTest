package com.test.sqlitetest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements Filterable
{
    private ArrayList<Abonent> abonents;
    private ArrayList<Abonent> abonentsFiltered;
    private MyAdapterListener listener;
    //----------------------------------------------------------------------------------------------
    public MyAdapter(ArrayList<Abonent> abonents, MyAdapterListener listener)
    {
        this.abonents = abonents;
        this.listener = listener;
        this.abonentsFiltered = abonents;
    }
    //----------------------------------------------------------------------------------------------
    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item, viewGroup, false);
        return new ViewHolder(v);
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder viewHolder, int i)
    {
        final Abonent abonent = abonentsFiltered.get(i);
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
    class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView itemName;
        private TextView itemNumber;

        private ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemNumber = itemView.findViewById(R.id.item_number);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    listener.onAbonentSelected(abonentsFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                String charString = charSequence.toString();
                if(charString.isEmpty())
                {
                    abonentsFiltered = abonents;
                }else
                {
                    ArrayList<Abonent> filteredList = new ArrayList<>();
                    for(Abonent row : abonents)
                    {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if(row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getNumber().contains(charSequence))
                        {
                            filteredList.add(row);
                        }
                    }
                    abonentsFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = abonentsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                abonentsFiltered = (ArrayList<Abonent>) filterResults.values;
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }
    //----------------------------------------------------------------------------------------------
    public interface MyAdapterListener
    {
        void onAbonentSelected(Abonent abonent);
    }
}
