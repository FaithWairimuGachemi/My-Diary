package com.example.mydiary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.EntryViewHolder> {

    private final List<DiaryEntry> entries;
    private final OnEntryMenuItemClickListener listener;

    public interface OnEntryMenuItemClickListener {
        void onDeleteClick(int position);
    }

    public EntryAdapter(List<DiaryEntry> entries, OnEntryMenuItemClickListener listener) {
        this.entries = entries;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_item, parent, false);
        return new EntryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
        DiaryEntry entry = entries.get(position);
        holder.titleTextView.setText(entry.getTitle());
        holder.dateTextView.setText(entry.getDate());

        holder.menuButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.inflate(R.menu.entry_menu);
            popup.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.action_view) {
                    viewEntry(v.getContext(), entry, position);
                    return true;
                } else if (itemId == R.id.action_edit) {
                    editEntry(v.getContext(), entry, position);
                    return true;
                } else if (itemId == R.id.action_delete) {
                    listener.onDeleteClick(position);
                    return true;
                }
                return false;
            });
            popup.show();
        });
    }

    private void viewEntry(Context context, DiaryEntry entry, int position) {
        Intent intent = new Intent(context, ViewEntryActivity.class);
        intent.putExtra("entry_title", entry.getTitle());
        intent.putExtra("entry_content", entry.getContent());
        intent.putExtra("entry_position", position);
        ((Activity) context).startActivityForResult(intent, MainActivity.VIEW_ENTRY_REQUEST);
    }

    private void editEntry(Context context, DiaryEntry entry, int position) {
        Intent intent = new Intent(context, ViewEntryActivity.class);
        intent.putExtra("entry_title", entry.getTitle());
        intent.putExtra("entry_content", entry.getContent());
        intent.putExtra("entry_position", position);
        intent.putExtra("edit_mode", true);
        ((Activity) context).startActivityForResult(intent, MainActivity.VIEW_ENTRY_REQUEST);
    }


    @Override
    public int getItemCount() {
        return entries.size();
    }

    static class EntryViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView dateTextView;
        ImageButton menuButton;

        public EntryViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            dateTextView = itemView.findViewById(R.id.date_text_view);
            menuButton = itemView.findViewById(R.id.menu_button);
        }
    }
}
