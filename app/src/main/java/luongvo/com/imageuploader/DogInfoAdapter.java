package luongvo.com.imageuploader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by luongvo on 07/06/2017.
 */

public class DogInfoAdapter extends RecyclerView.Adapter<DogInfoAdapter.CustomViewHolder> {
    private List<DogInfo> dogInfos;
    private Context context;

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView titleView, bodyView;
        public CustomViewHolder(View view) {
            super(view);
            this.titleView = (TextView)view.findViewById(R.id.info_title);
            this.bodyView = (TextView) view.findViewById(R.id.info_body);
        }
    }

    public DogInfoAdapter(Context context, List<DogInfo> dogInfos) {
        this.dogInfos = dogInfos;
        this.context = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dog_info_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        DogInfo dogInfo = dogInfos.get(position);
        holder.titleView.setText(dogInfo.getTitle());
        holder.bodyView.setText(dogInfo.getBody());
    }


    @Override
    public int getItemCount() {
        return (null != dogInfos ? dogInfos.size() : 0);
    }


}
