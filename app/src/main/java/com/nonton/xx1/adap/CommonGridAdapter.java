package com.nonton.xx1.adap;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nonton.xx1.DetailsActivity;
import com.nonton.xx1.R;
import com.nonton.xx1.mdl.CommonModels;
import com.nonton.xx1.utl.ApiResources;
import com.squareup.picasso.Picasso;
import com.startapp.android.publish.adsCommon.Ad;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.adListeners.AdDisplayListener;

import java.util.ArrayList;
import java.util.List;

public class CommonGridAdapter extends RecyclerView.Adapter<CommonGridAdapter.OriginalViewHolder> {

    private List<CommonModels> items = new ArrayList<>();
    private Context ctx;


    public CommonGridAdapter(Context context, List<CommonModels> items) {
        this.items = items;
        ctx = context;
    }


    @Override
    public CommonGridAdapter.OriginalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonGridAdapter.OriginalViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_image_albums, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CommonGridAdapter.OriginalViewHolder holder, final int position) {

        final CommonModels obj = items.get(position);

        holder.name.setText(obj.getTitle());
        holder.imdb.setText(obj.getImdbrating());
        Picasso.get().load(obj.getImageUrl()).into(holder.image);

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ApiResources.startappstatus.equals("1")){

                    StartAppAd startAppAd = new StartAppAd(ctx);
                    startAppAd.showAd(new AdDisplayListener() {
                        @Override
                        public void adHidden(Ad ad) {



                            Intent intent=new Intent(ctx,DetailsActivity.class);
                            intent.putExtra("vType",obj.getVideoType());
                            intent.putExtra("id",obj.getId());
                            ctx.startActivity(intent);



                        }

                        @Override
                        public void adDisplayed(Ad ad) {

                        }

                        @Override
                        public void adClicked(Ad ad) {

                        }

                        @Override
                        public void adNotDisplayed(Ad ad) {
                            Intent intent=new Intent(ctx,DetailsActivity.class);
                            intent.putExtra("vType",obj.getVideoType());
                            intent.putExtra("id",obj.getId());
                            ctx.startActivity(intent);


                        }
                    });



                }else {
                    Intent intent=new Intent(ctx,DetailsActivity.class);
                    intent.putExtra("vType",obj.getVideoType());
                    intent.putExtra("id",obj.getId());
                    ctx.startActivity(intent);



                }





            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView name;
        public View lyt_parent;
        public TextView imdb;

        public OriginalViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.image);
            name = v.findViewById(R.id.name);
            imdb=v.findViewById(R.id.tvimdb);
            lyt_parent = v.findViewById(R.id.lyt_parent);
        }

    }

}