package unicam.pi.mqespol.view.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import unicam.pi.mqespol.R;
import unicam.pi.mqespol.model.Device;

public class DeviceAdapter extends ListAdapter<Device,DeviceAdapter.DeviceHolder> {
    public  DeviceAdapter() {
        super(DIFF_CALLBACK);
    }
    private static final  DiffUtil.ItemCallback<Device> DIFF_CALLBACK = new DiffUtil.ItemCallback<Device>() {
        @Override
        public boolean areItemsTheSame(@NonNull Device oldItem, @NonNull Device newItem) {
            return (oldItem.getId() == newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Device oldItem, @NonNull Device newItem) {
            return oldItem.getTopic().equals(newItem.getTopic()) && oldItem.getName().equals(newItem.getName()) && oldItem.getMessage().equals(newItem.getMessage()) ;
        }
    };

    @NonNull
    @Override
    public DeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_item,parent,false);
        return new DeviceHolder(itemview);
    }
    @Override
    public void onBindViewHolder(@NonNull DeviceHolder holder, int position) {
            Device curretDevice= getItem(position);
            holder.tvTopic.setText(curretDevice.getTopic());
            holder.tvMessage.setText(curretDevice.getMessage());
            holder.tvName.setText(curretDevice.getName());
    }



    public Device getDeviceAt(int position){
        return getItem(position);
    }



    class DeviceHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private TextView tvMessage;
        private TextView tvTopic;

        public DeviceHolder( View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTopic = itemView.findViewById(R.id.tvTopic);
        }
    }

}
