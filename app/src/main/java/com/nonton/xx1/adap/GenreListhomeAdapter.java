package com.nonton.xx1.adap;

import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nonton.xx1.ItemMovieActivity;
import com.nonton.xx1.R;
import com.nonton.xx1.mdl.CommonModels;

import java.util.ArrayList;
import java.util.List;

public class GenreListhomeAdapter  extends RecyclerView.Adapter<GenreListhomeAdapter.OriginalViewHolder> {

    private List<CommonModels> items = new ArrayList<>();
    private Context ctx;
    private String type;
    private int c=0;


    public GenreListhomeAdapter(Context context, List<CommonModels> items,String type) {
        this.items = items;
        ctx = context;
        this.type=type;
    }


    @Override
    public GenreListhomeAdapter.OriginalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GenreListhomeAdapter.OriginalViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlistgenre, parent, false);
        vh = new GenreListhomeAdapter.OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(GenreListhomeAdapter.OriginalViewHolder holder, final int position) {

        final CommonModels obj = items.get(position);
        holder.name.setText(obj.getTitle());

        holder.lyt_parent.setCardBackgroundColor(ctx.getResources().getColor(getColor()));

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ctx, ItemMovieActivity.class);
                intent.putExtra("id",obj.getId());
                intent.putExtra("title",obj.getTitle());
                intent.putExtra("type",type);
                ctx.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        private CardView lyt_parent;


        public OriginalViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.genreku);
            lyt_parent=v.findViewById(R.id.genreparentlayout);
        }
    }

    private int getColor(){

        int colorList[] = {R.color.red_400,R.color.blue_400,R.color.indigo_400,R.color.orange_400,R.color.light_green_400,R.color.blue_grey_400};

        if (c>=6){
            c=0;
        }

        int color = colorList[c];
        c++;

        return color;

    }


}