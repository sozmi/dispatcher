package com.sozmi.dispatcher.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sozmi.dispatcher.databinding.FragmentItemTaskBinding;
import com.sozmi.dispatcher.fragment.TaskFragment;
import com.sozmi.dispatcher.model.objects.StatusTask;
import com.sozmi.dispatcher.model.objects.Task;
import com.sozmi.dispatcher.model.system.MyFM;
import com.sozmi.dispatcher.model.system.Tag;

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
        holder.mImageViewStatus.setBackgroundResource(task.getColorStatus());
        holder.mPanelTime.setVisibility(View.INVISIBLE);
        holder.mInfoButton.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Tag.TaskID.toString(), task);
            MyFM.OpenFragment(new TaskFragment(), bundle);
        });
        holder.mProgressBarTask.setMax(task.getExecute_time());
        holder.mProgressBarTask.setProgress(task.getCurrentTime());
        holder.mTimerView.setText(task.getCurrentTimeToString());
        if(task.getStatusTask()== StatusTask.execute)
            holder.mPanelTime.setVisibility(View.VISIBLE);
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

        public final TextView mNameView, mTimerView;
        public final ImageView mImageViewStatus;
        public final Button mInfoButton;
        public final LinearLayout mPanelTime;
        public final ProgressBar mProgressBarTask;

        public ViewHolder(FragmentItemTaskBinding binding) {
            super(binding.getRoot());
            mNameView = binding.nameTask;
            mImageViewStatus = binding.imageGroupTask;
            mInfoButton = binding.infoTask;
            mPanelTime = binding.panelTimerTask;
            mProgressBarTask = binding.progressBarTask;
            mTimerView = binding.timerTask;
        }
    }
}

