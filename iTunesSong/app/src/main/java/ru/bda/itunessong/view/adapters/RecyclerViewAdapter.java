package ru.bda.itunessong.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ru.bda.itunessong.R;
import ru.bda.itunessong.model.data.Result;
import ru.bda.itunessong.model.data.SongsData;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    public final static int VERTICAL_ORIENTATION = 0;
    public final static int HORIZONTAL_ORIENTATION = 1;
    public final static int TABLE_TYPE = 2;
    public final static int LIST_TYPE = 3;

    private SongsData songsData = new SongsData();

    private int orientation;
    private int typeView = TABLE_TYPE;

    private Context context;

    public RecyclerViewAdapter(Context context) {

        this.context = context;
    }

    public void setSongsData(SongsData songsData) {
        this.songsData = songsData;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Result song = songsData.getResults().get(position);
        if (typeView == TABLE_TYPE) {
            int count = getCount(orientation);
            int widhtScreen =  ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
            int changedWidth = widhtScreen / count;
            holder.listLayout.setVisibility(View.GONE);
            holder.tableLayout.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = holder.tableLayout.getLayoutParams();
            params.height = changedWidth;
            params.width = changedWidth;
            holder.tableLayout.setLayoutParams(params);
            Picasso.with(context)
                    .load(song.getArtworkUrl100())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .centerCrop()
                    .resize(changedWidth, changedWidth)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(holder.image);
            holder.text.setText(song.getArtistName() + " - " + song.getTrackName());

        } else if (typeView == LIST_TYPE) {
            holder.listLayout.setVisibility(View.VISIBLE);
            holder.tableLayout.setVisibility(View.GONE);
            Picasso.with(context)
                    .load(song.getArtworkUrl100())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(holder.imageList);
            holder.textList.setText(song.getArtistName() + " - " + song.getTrackName());
        }
    }

    private int getCount(int orientation) {
        return orientation == VERTICAL_ORIENTATION ? 2 : 3;
    }

    @Override
    public int getItemCount() {
        return songsData.getResults() == null ? 0 : songsData.getResults().size();
    }

    public void setLayoutType(int orientation, int typeView) {
        this.orientation = orientation;
        this.typeView = typeView;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout tableLayout;
        LinearLayout listLayout;
        ImageView image;
        TextView text;
        ImageView imageList;
        TextView textList;

        public ViewHolder(View itemView) {
            super(itemView);
            tableLayout = (RelativeLayout) itemView.findViewById(R.id.table_layout);
            listLayout = (LinearLayout) itemView.findViewById(R.id.list_layout);
            image = (ImageView) itemView.findViewById(R.id.image);
            text = (TextView) itemView.findViewById(R.id.text);
            imageList = (ImageView) itemView.findViewById(R.id.image_list);
            textList = (TextView) itemView.findViewById(R.id.text_list);
        }
    }
}
