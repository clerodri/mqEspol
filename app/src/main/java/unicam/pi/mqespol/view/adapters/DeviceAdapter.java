package unicam.pi.mqespol.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import unicam.pi.mqespol.R;
import unicam.pi.mqespol.model.Device;


/**
 * Adapta del ReciclerView del Fragment ListDevice
 */
public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceHolder> {
    private List<Device> devices= new ArrayList<>();
    @NonNull
    @Override
    public DeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_item,parent,false);
        return new DeviceHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceHolder holder, int position) {
            Device curretDevice= devices.get(position);
            holder.tvTopic.setText(curretDevice.getTopic());
            holder.tvMessage.setText(curretDevice.getMessage());
            holder.tvName.setText(curretDevice.getName());
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public Device getDeviceAt(int position){
        return devices.get(position);
    }

    public void setDevices(List<Device> devices){
        this.devices = devices;
        System.out.println(devices.size());
       // notifyItemInserted(0);
       notifyDataSetChanged();
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
