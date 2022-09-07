package com.aligit.base.widget.select_image;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.aligit.base.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author：luck
 * @date：2016-7-27 23:02
 * @describe：GridImageAdapter
 */
public class GridImageAdapter extends RecyclerView.Adapter<GridImageAdapter.ViewHolder> {
    public static final String TAG = "PictureSelector";
    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_PICTURE = 2;
    private final LayoutInflater mInflater;
    private final ArrayList<LocalMedia> list = new ArrayList<>();
    private int delImage;
    private int addImage;
    private int selectMax = 9;

    /**
     * 删除
     */
    public void delete(int position) {
        try {

            if (position != RecyclerView.NO_POSITION && list.size() > position) {
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, list.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GridImageAdapter(Context context, List<LocalMedia> result) {
        this(context, result, R.drawable.ic_delete_menu, R.drawable.ic_add_image);
    }

    public GridImageAdapter(Context context, List<LocalMedia> result, int delImage, int addImage) {
        this.mInflater = LayoutInflater.from(context);
        this.list.addAll(result);
        if (delImage == 0) delImage = R.drawable.ic_delete_menu;
        if (addImage == 0) addImage = R.drawable.ic_add_image;
        this.addImage = addImage;
        this.delImage = delImage;
    }

    public void setSelectMax(int selectMax) {
        this.selectMax = selectMax;
    }

    public int getSelectMax() {
        return selectMax;
    }

    public ArrayList<LocalMedia> getData() {
        return list;
    }

    public void remove(int position) {
        if (position < list.size()) {
            list.remove(position);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImg;
        ImageView mIvDel;
        TextView tvDuration;

        public ViewHolder(View view) {
            super(view);
            mImg = view.findViewById(R.id.fiv);
            mIvDel = view.findViewById(R.id.iv_del);
            tvDuration = view.findViewById(R.id.tv_duration);
        }
    }

    @Override
    public int getItemCount() {
        if (list.size() < selectMax) {
            return list.size() + 1;
        } else {
            return list.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowAddItem(position)) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PICTURE;
        }
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.gv_filter_image, viewGroup, false);
        return new ViewHolder(view);
    }

    private boolean isShowAddItem(int position) {
        int size = list.size();
        return position == size;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        //少于MaxSize张，显示继续添加的图标
        if (getItemViewType(position) == TYPE_CAMERA) {
            viewHolder.mImg.setImageResource(addImage);
            viewHolder.mImg.setOnClickListener(view -> {
                if (mItemClickListener != null) {
                    mItemClickListener.openPicture();
                }
            });
            viewHolder.mIvDel.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.mIvDel.setVisibility(View.VISIBLE);
            viewHolder.mIvDel.setImageResource(delImage);
            viewHolder.mIvDel.setOnClickListener(view -> {
                int index = viewHolder.getAbsoluteAdapterPosition();
                if (index != RecyclerView.NO_POSITION && list.size() > index) {
                    list.remove(index);
                    notifyItemRemoved(index);
                    notifyItemRangeChanged(index, list.size());
                }
            });
            LocalMedia media = list.get(position);
            int chooseModel = media.getChooseModel();
            String path = media.getAvailablePath();
            long duration = media.getDuration();
            viewHolder.tvDuration.setVisibility(PictureMimeType.isHasVideo(media.getMimeType())
                    ? View.VISIBLE : View.GONE);
            if (chooseModel == SelectMimeType.ofAudio()) {
                viewHolder.tvDuration.setVisibility(View.VISIBLE);
                viewHolder.tvDuration.setCompoundDrawablesRelativeWithIntrinsicBounds
                        (R.drawable.ps_ic_audio, 0, 0, 0);

            } else {
                viewHolder.tvDuration.setCompoundDrawablesRelativeWithIntrinsicBounds
                        (R.drawable.ps_ic_video, 0, 0, 0);
            }
            viewHolder.tvDuration.setText(DateUtils.formatDurationTime(duration));
            if (chooseModel == SelectMimeType.ofAudio()) {
                viewHolder.mImg.setImageResource(R.drawable.ps_audio_placeholder);
            } else {
                Glide.with(viewHolder.itemView.getContext())
                        .load(PictureMimeType.isContent(path) && !media.isCut() && !media.isCompressed() ? Uri.parse(path)
                                : path)
                        .centerCrop()
                        .placeholder(R.color.app_color_f6)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(viewHolder.mImg);
            }
            //itemView 的点击事件
            if (mItemClickListener != null) {
                viewHolder.itemView.setOnClickListener(v -> {
                    int adapterPosition = viewHolder.getAbsoluteAdapterPosition();
                    mItemClickListener.onItemClick(v, adapterPosition);
                });
            }
        }
    }

    private OnItemClickListener mItemClickListener;

    public void setOnItemClickListener(OnItemClickListener l) {
        this.mItemClickListener = l;
    }

    public interface OnItemClickListener {
        /**
         * Item click event
         *
         * @param v
         * @param position
         */
        void onItemClick(View v, int position);

        /**
         * Open PictureSelector
         */
        void openPicture();
    }
}
