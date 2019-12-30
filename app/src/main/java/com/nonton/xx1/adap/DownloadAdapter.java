package com.nonton.xx1.adap;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nonton.xx1.R;
import com.nonton.xx1.mdl.CommonModels;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.DOWNLOAD_SERVICE;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.OriginalViewHolder> {

    String DOWNLOAD_DIRECTORY="/NONTONXXI";


    private List<CommonModels> items = new ArrayList<>();
    private Context ctx;

    private ServerApater.OnItemClickListener mOnItemClickListener;

    private DownloadAdapter.OriginalViewHolder viewHolder;



    public DownloadAdapter(Context context, List<CommonModels> items) {
        this.items = items;
        ctx = context;
    }


    @Override
    public DownloadAdapter.OriginalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DownloadAdapter.OriginalViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_server, parent, false);
        vh = new DownloadAdapter.OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final DownloadAdapter.OriginalViewHolder holder, final int position) {

        final CommonModels obj = items.get(position);
        holder.name.setText("Download");



        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                long downloadFileRef = downloadFile(Uri.parse(obj.getStremURL()), DOWNLOAD_DIRECTORY, obj.getTitle()+".mp4");
                if (downloadFileRef != 0) {
                    Toast.makeText(ctx,"Starting download",Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(ctx,"File is not available for download",Toast.LENGTH_LONG).show();

                }




            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public CardView cardView;

        public OriginalViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            cardView=v.findViewById(R.id.card_view_home);
        }
    }


    private long downloadFile(Uri uri, String fileStorageDestinationUri, String fileName) {

        long downloadReference = 0;

        DownloadManager downloadManager = (DownloadManager)ctx.getSystemService(DOWNLOAD_SERVICE);
        try {
            DownloadManager.Request request = new DownloadManager.Request(uri);

            //Setting title of request
            request.setTitle(fileName);

            //Setting description of request
            request.setDescription("Your file is downloading");

            //set notification when download completed
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            //Set the local destination for the downloaded file to a path within the application's external files directory
            request.setDestinationInExternalPublicDir(fileStorageDestinationUri, fileName);

            request.allowScanningByMediaScanner();

            //Enqueue download and save the referenceId
            downloadReference = downloadManager.enqueue(request);
        } catch (IllegalArgumentException e) {
            Toast.makeText(ctx,"Download link is broken or not availale for download",Toast.LENGTH_LONG).show();

            Log.e(TAG, "Line no: 455,Method: downloadFile: Download link is broken");

        }
        return downloadReference;
    }

}