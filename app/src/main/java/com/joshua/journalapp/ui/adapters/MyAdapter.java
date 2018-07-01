package com.joshua.journalapp.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joshua.journalapp.data.NoteContract;
import com.joshua.journalapp.R;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.NoteHolder> {
    final private ListItemClickListener listItemClickListener;
    Cursor cursor;
    Context context;


    public MyAdapter(Cursor cursor, Context context, ListItemClickListener listener) {
        this.cursor = cursor;
        listItemClickListener = listener;
        this.context = context;
    }

    @Override
    public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToPerentImmediatly = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToPerentImmediatly);
        NoteHolder noteHolder = new NoteHolder(view);
        return noteHolder;

    }

    @Override
    public void onBindViewHolder(NoteHolder holder, int position) {
        if (!cursor.moveToPosition(position))
            return;
        String details = cursor.getString(cursor.getColumnIndex(NoteContract.NotelistEntry.COLUMN_DETAILS));
        long id = cursor.getLong(cursor.getColumnIndex(NoteContract.NotelistEntry._ID));
        holder.titleView.setText(details);

        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) cursor.close();
        cursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, long id);
    }

    public class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleView;

        public NoteHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.tv_list_item);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            RecyclerView.ViewHolder viewHolder = NoteHolder.this;
            long id = (long) viewHolder.itemView.getTag();
            listItemClickListener.onListItemClick(clickedPosition, id);

        }
    }
}


