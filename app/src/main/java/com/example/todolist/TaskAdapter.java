package com.example.todolist;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private ArrayList<String> taskList;
    private Context context;
    private OnTaskChangedListener listener;

    public interface OnTaskChangedListener {
        void onTaskListChanged();
    }

    public TaskAdapter(Context context, ArrayList<String> taskList, OnTaskChangedListener listener) {
        this.context = context;
        this.taskList = taskList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        String task = taskList.get(position);
        holder.tvTaskName.setText(task);

        holder.btnEdit.setOnClickListener(v -> showEditDialog(position));
        holder.btnDelete.setOnClickListener(v -> {
            taskList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, taskList.size());
            if (listener != null) listener.onTaskListChanged();
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    private void showEditDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Task");

        final EditText editInput = new EditText(context);
        editInput.setText(taskList.get(position));
        builder.setView(editInput);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String edited = editInput.getText().toString().trim();
            if (!edited.isEmpty()) {
                taskList.set(position, edited);
                notifyItemChanged(position);
                if (listener != null) listener.onTaskListChanged();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView tvTaskName;
        ImageButton btnEdit, btnDelete;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTaskName = itemView.findViewById(R.id.tvTaskName);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
