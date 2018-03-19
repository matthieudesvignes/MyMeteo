package iut.desvignes.mymeteo;

import android.arch.paging.PagedListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;

/**
 * Created by androidS4 on 19/03/18.
 */

public class MeteoAdapter extends PagedListAdapter<MeteoModel, MeteoAdapter.TownViewHolder>{

    static DiffUtil.ItemCallback<MeteoModel> diffCallback = new DiffUtil.ItemCallback<MeteoModel>() {
        @Override public boolean areItemsTheSame(MeteoModel oldItem, MeteoModel newItem) {
            return oldItem.getId() == newItem.getId();
        }
        @Override public boolean areContentsTheSame(MeteoModel oldItem, MeteoModel newItem) {
            return oldItem.equals(newItem);
        }
    };

    MeteoPresenter presenter;
    ExecutorService service;

     public MeteoAdapter(ExecutorService service, MeteoPresenter presenter){
         super(diffCallback);
         this.service = service;
         this.presenter = presenter;
     }

    @Override
    public TownViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.items_list_layout, parent, false);
        return new TownViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TownViewHolder holder, int position) {
        MeteoModel town = this.getItem(position);
        if(town != null) holder.displayTown(town);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    public class TownViewHolder extends RecyclerView.ViewHolder {
        private TextView nameView, tempView;
        private ImageView imageView;

        public TownViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.itemTownNameView);
            tempView = itemView.findViewById(R.id.textViewTemp);
            imageView = itemView.findViewById(R.id.imageView);
        }

        public void displayTown(MeteoModel town) {
            nameView.setText(town.getTownName());
            tempView.setText(Double.toString(town.getTemperature()));
            imageView.setImageResource(presenter.getImageID());
        }
    }
}
