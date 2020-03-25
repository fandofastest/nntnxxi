package com.nonton.xx1.adap;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
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

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.OriginalViewHolder> {

    private List<CommonModels> items = new ArrayList<>();
    private Context ctx;


    public HomePageAdapter(Context context, List<CommonModels> items) {
        this.items = items;
        ctx = context;
    }


    @Override
    public HomePageAdapter.OriginalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HomePageAdapter.OriginalViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_home_view, parent, false);
        vh = new HomePageAdapter.OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(HomePageAdapter.OriginalViewHolder holder, final int position) {

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




                            Intent intent=new Intent(ctx, DetailsActivity.class);
                            intent.putExtra("vType",obj.getVideoType());
                            intent.putExtra("id",obj.getId());

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                            Intent intent=new Intent(ctx, DetailsActivity.class);
                            intent.putExtra("vType",obj.getVideoType());
                            intent.putExtra("id",obj.getId());

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ctx.startActivity(intent);

                        }
                    });



                }else {
                    Intent intent=new Intent(ctx, DetailsActivity.class);
                    intent.putExtra("vType",obj.getVideoType());
                    intent.putExtra("id",obj.getId());

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
        public MaterialRippleLayout lyt_parent;
        public TextView imdb;


        public OriginalViewHolder(View v) {
            super(v);

            image = v.findViewById(R.id.image);
            name = v.findViewById(R.id.name);
            imdb=v.findViewById(R.id.tvimdb);
            lyt_parent=v.findViewById(R.id.lyt_parent);
        }
    }

}
