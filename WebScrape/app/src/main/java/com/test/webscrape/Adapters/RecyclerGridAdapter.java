package com.test.webscrape.Adapters;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.test.webscrape.DataModel.Products;
import com.test.webscrape.R;

import java.util.ArrayList;

public class RecyclerGridAdapter extends RecyclerView.Adapter<RecyclerGridAdapter.GridViewHolder> {
    public static final int SPAN_COUNT_ONE = 1;
    public static final int SPAN_COUNT_TWO = 2;

    private static final int LIST_VIEW = 1;
    private static final int GRID_VIEW = 2;

    private GridLayoutManager mLayoutManager;
    private ArrayList<Products> products;
    int pos;

    private RecyclerGridAdapter.OnItemClickListener mListener;

    public RecyclerGridAdapter() {
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onSaveClick(int position);
    }

    public void setOnItemClickListener(RecyclerGridAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    class GridViewHolder extends RecyclerView.ViewHolder{
        TextView productDescrption;
        TextView NewPrice;
        TextView OldPrice;
        ImageView img;
        ProgressBar progressBar;
        ImageView imgLogo;
        ImageView save;
        public ImageView saved;
        TextView discount;

        public GridViewHolder(@NonNull View itemView, final RecyclerGridAdapter.OnItemClickListener listener, int viewType) {
            super(itemView);

            productDescrption = itemView.findViewById(R.id.productDesc);
            NewPrice = itemView.findViewById(R.id.newPrice);
            OldPrice = itemView.findViewById(R.id.oldPrice);
            progressBar = itemView.findViewById(R.id.imageProgress);
            img = itemView.findViewById(R.id.productImage);
            imgLogo = itemView.findViewById(R.id.websiteLogo);
            save = itemView.findViewById(R.id.save);
            discount = itemView.findViewById(R.id.discount);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
            //click to save
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onSaveClick(position);
                        }
                    }
                }
            });
        }
    }

    public RecyclerGridAdapter(ArrayList<Products> prod, GridLayoutManager gridLayoutManager){
        products = prod;
        mLayoutManager = gridLayoutManager;
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v;
        if (viewType == LIST_VIEW){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item,parent,false);
        }
        return new RecyclerGridAdapter.GridViewHolder(v,mListener,viewType);
    }

    @Override
    public void onBindViewHolder(final GridViewHolder holder, int position){

        Products currentProduct = products.get(position);
        holder.OldPrice.setText(currentProduct.getOldPrice());
        holder.OldPrice.setPaintFlags(holder.OldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.discount.setText(currentProduct.getDiscountPercentage());
        holder.NewPrice.setText(currentProduct.getNewPrice());
        holder.productDescrption.setText(currentProduct.getProductDesc());
        holder.imgLogo.setContentDescription(currentProduct.getImgLogoDesc());
        Picasso.get().load(currentProduct.getImageProduct()).into(holder.img, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onError(Exception e) {
                holder.progressBar.setVisibility(View.GONE);
                holder.img.setImageResource(R.drawable.fail_image_load);
            }
        });

        Picasso.get().load(currentProduct.getImageLogo()).into(holder.imgLogo);

        if (products.get(position).isImageChanged()){
            holder.save.setImageResource(R.drawable.ic_saved);
        } else {
            holder.save.setImageResource(R.drawable.save);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int spanCount = mLayoutManager.getSpanCount();
        if (spanCount == SPAN_COUNT_ONE){
            return LIST_VIEW;
        } else {
            return GRID_VIEW;
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    //used for adding saved icon
    public void changeImage(int index) {
        products.get(index).setImageChanged(true);
        notifyItemChanged(index);
    }

    //used for removing saved icon
    public void removeImage(int index){
        products.get(index).setImageChanged(false);
        notifyItemChanged(index);
    }
}
