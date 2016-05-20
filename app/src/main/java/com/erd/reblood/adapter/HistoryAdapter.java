package com.erd.reblood.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.toolbox.ImageLoader;
import com.erd.reblood.R;
import com.erd.reblood.app.AppController;
import com.erd.reblood.model.History;

import java.util.List;


/**
 * Created by E.R.D on 4/2/2016.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.VersionViewHolder> {
    private List<History> restaurantList;
    Context context;
    OnItemClickListener clickListener;
    ImageLoader imageLoader;

    public HistoryAdapter(Context context) {
        this.context = context;
    }

    public HistoryAdapter(List<History> restaurantList, OnItemClickListener listener) {
        this.restaurantList = restaurantList;
        this.clickListener = listener;
    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_history, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);
        imageLoader = AppController.getInstance().getImageLoader();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VersionViewHolder versionViewHolder, int i) {
        final History model = restaurantList.get(i);
        versionViewHolder.bind(model, clickListener);

        //TextDrawable Code
        String firstLetter = String.valueOf(model.getMerchantName().charAt(0));
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(model.getMerchantName());
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstLetter, color);

        versionViewHolder.picRes.setImageDrawable(drawable);

    }

    @Override
    public int getItemCount() {
        return restaurantList == null ? 0 : restaurantList.size();
    }

    public void animateTo(List<History> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<History> newModels) {
        for (int i = restaurantList.size() - 1; i >= 0; i--) {
            final History model = restaurantList.get(i);
            if (!newModels.contains(model)) {
            removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<History> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final History model = newModels.get(i);
            if (!restaurantList.contains(model)) {
            addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<History> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final History model = newModels.get(toPosition);
            final int fromPosition = restaurantList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
            moveItem(fromPosition, toPosition);
            }
        }
    }

    public History removeItem(int position) {
        final History model = restaurantList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, History model) {
        restaurantList.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final History model = restaurantList.remove(fromPosition);
        restaurantList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    class VersionViewHolder extends RecyclerView.ViewHolder {
        CardView cardItemLayout;
        TextView titleR, nameR, descR;
        ImageView picRes;

        public VersionViewHolder(View itemView) {
            super(itemView);

            cardItemLayout = (CardView) itemView.findViewById(R.id.cardlist_item);
            picRes = (ImageView) itemView.findViewById(R.id.iv_firstLetter);
            titleR = (TextView) itemView.findViewById(R.id.title);
            nameR = (TextView) itemView.findViewById(R.id.name);
            descR = (TextView) itemView.findViewById(R.id.desc);

        }

        public void bind(final History model, final OnItemClickListener listener) {
            titleR.setText(model.getMerchantName());
            nameR.setText(model.getStoreName());
            //picRes.setImageUrl(model.getGambar(), imageLoader);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(model);
                }
            });
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(History model);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

}