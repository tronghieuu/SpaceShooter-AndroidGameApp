package com.hieulam.spaceshooter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Rank> mRanks;
    private Context mContext;
    private Activity mActivity;
    private OnItemClickListener mListener;

    // for load more
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private boolean isLoading;
    private int visibleThreshold = 6;
    private int lastVisibleItem, totalItemCount;

    public interface OnItemClickListener {
        void onItemClick(Rank item);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void add(int position, Rank item) {
        mRanks.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(Rank item) {
        int position = mRanks.indexOf(item);
        mRanks.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RankAdapter(Context context, List<Rank> ranks, RecyclerView recyclerView) {

        mContext = context;
        mActivity = (Activity)context;
        mRanks = ranks;

        // load more
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.item_ranking, parent, false);
            return new ViewHolderRow(view);
        }
        return null;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if (holder instanceof ViewHolderRow) {
            Rank rank = mRanks.get(position);

            ViewHolderRow userViewHolder = (ViewHolderRow) holder;

            userViewHolder.tvName.setText(rank.getName());
            userViewHolder.tvHighScore.setText(rank.getHighScore());
            if(rank.getRank() == 1) {
                userViewHolder.tvRank.setTextColor(Color.parseColor("#E9C957"));
            } else if(rank.getRank() == 2) {
                userViewHolder.tvRank.setTextColor(Color.parseColor("#878681"));
            } else if(rank.getRank() == 3) {
                userViewHolder.tvRank.setTextColor(Color.parseColor("#cd7f32"));
            }

            userViewHolder.tvRank.setText("#"+rank.getRank());
            new DownloadImageFromInternet(userViewHolder.imageView)
                    .execute(rank.getImage());

            // binding item click listner
            userViewHolder.bind(mRanks.get(position), mListener);
        } else if (holder instanceof ViewHolderLoading) {
            ViewHolderLoading loadingViewHolder = (ViewHolderLoading) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mRanks == null ? 0 : mRanks.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    public void setOnItemListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return mRanks.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setLoaded() {
        isLoading = false;
    }

    private class ViewHolderLoading extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ViewHolderLoading(View view) {
            super(view);
            progressBar = view.findViewById(R.id.progressBar);
        }
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {

        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView){
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolderRow extends RecyclerView.ViewHolder {
        public TextView tvName, tvHighScore, tvRank;
        public ImageView imageView;


        public ViewHolderRow(View v) {
            super(v);
            tvHighScore = v.findViewById(R.id.tvRankHighScore);
            imageView = v.findViewById(R.id.imageView);
            tvName = v.findViewById(R.id.tvName);
            tvRank = v.findViewById(R.id.tvRankNumber);
        }

        public void bind(final Rank item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
