package avivaviad.gifcamera.model;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import avivaviad.gifcamera.R;

/**
 * Created by Aviad on 25/09/2017.
 */

public class AdapterGifGrid extends RecyclerView.Adapter<AdapterGifGrid.ViewHolder> {

    private List<GifObject> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private ItemLongClickListener mLongClickListener;
    private List<Integer> selectedIds = new ArrayList<>();

    public AdapterGifGrid(Context context, List<GifObject> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_gif, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String tag = mData.get(position).getmGifTag();
        Glide.with(mInflater.getContext()).load(mData.get(position).getmGifSrc()).into(holder.imgGif);
      //  holder.txtTag.setText(tag);
        int id = Integer.parseInt(mData.get(position).getTimeStamp());

        if (selectedIds.contains(id)){
            //if item is selected then,set foreground color of FrameLayout.
            holder.rootView.setForeground(new ColorDrawable(ContextCompat.getColor(mInflater.getContext(),R.color.colorControlActivated)));
        }
        else {
            //else remove selected item color.
            holder.rootView.setForeground(new ColorDrawable(ContextCompat.getColor(mInflater.getContext(),android.R.color.transparent)));
        }
    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    public void setdata(List<GifObject> mArrGifs) {
        mData = mArrGifs;
        notifyDataSetChanged();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
       // TextView txtTag;
        ImageView imgGif;
        FrameLayout rootView;

        ViewHolder(View itemView) {
            super(itemView);
           // txtTag = (TextView) itemView.findViewById(R.id.gif_tag);
            imgGif = (ImageView) itemView.findViewById(R.id.gif_img);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            rootView = (FrameLayout) itemView.findViewById(R.id.root_view);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            if (mLongClickListener != null) {
                mLongClickListener.onLongItemClick(view, getAdapterPosition());
            }
            return false;
        }
    }

    // convenience method for getting data at click position
    public GifObject getItem(int position) {
        if (mData == null || mData.get(position) == null) {
            return null;
        }
        return mData.get(position);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    public void setLongClickListener(ItemLongClickListener itemLongClickListener) {
        this.mLongClickListener = itemLongClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
    public interface ItemLongClickListener {
        void onLongItemClick(View view, int position);
    }

    public void setSelectedIds(List<Integer> selectedIds) {
        this.selectedIds = selectedIds;
        notifyDataSetChanged();
    }

}