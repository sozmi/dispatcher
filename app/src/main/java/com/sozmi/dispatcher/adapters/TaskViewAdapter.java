package com.sozmi.dispatcher.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sozmi.dispatcher.R;
import com.sozmi.dispatcher.databinding.FragmentItemTaskBinding;
import com.sozmi.dispatcher.model.Server;
import com.sozmi.dispatcher.model.objects.Task;

import java.util.List;

/**
 * получает данные и выдает View для отображения пункта списка.
 */
public class TaskViewAdapter extends RecyclerView.Adapter<TaskViewAdapter.ViewHolder> {

    private final List<Task> mValues;


    public TaskViewAdapter(List<Task> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentItemTaskBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    //В методе адаптера onBindViewHolder() связываем используемые текстовые метки с данными
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Task task = mValues.get(position);
        holder.mNameView.setText(task.getName());
        holder.mImageViewStatus.setImageResource(task.getImageGroupTask());
        holder.mImageViewStatus.setBackgroundResource(task.getBackgroundStatusTask());
        holder.mPanelTime.setVisibility(View.INVISIBLE);
        holder.mInfoButton.setOnClickListener(view1 -> {
        });
    }


    /**
     * @return количество элементов списка.
     */
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    //Класс MyViewHolder на основе ViewHolder служит для оптимизации ресурсов.
    // Он служит своеобразным контейнером для всех компонентов, которые входят в элемент списка.
    // При этом RecyclerView создаёт ровно столько контейнеров, сколько нужно для отображения на экране.
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mNameView;
        public final ImageView mImageViewStatus;
        public final Button mInfoButton;
        public final LinearLayout mPanelTime;
        public final ProgressBar mProgressBarTask;

        public ViewHolder(FragmentItemTaskBinding binding) {
            super(binding.getRoot());
            mNameView = binding.nameTask;
            mImageViewStatus = binding.imageGroupTask;
            mInfoButton = binding.infoTask;
            mPanelTime =binding.panelTimerTask;
            mProgressBarTask =binding.progressBarTask;
        }
    }
}

