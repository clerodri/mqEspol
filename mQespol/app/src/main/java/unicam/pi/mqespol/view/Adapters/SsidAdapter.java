package unicam.pi.mqespol.view.Adapters;

import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import unicam.pi.mqespol.R;

public class SsidAdapter extends RecyclerView.Adapter<SsidAdapter.SsidHolder> {
    private List<ScanResult> wifiList ;
    private int itemSelected =-1;

    public SsidAdapter(List<ScanResult> wifiList) {
        this.wifiList = wifiList;
    }

    @NonNull
    @Override
    public SsidHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ssid_item,parent,false);
        return new SsidHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull SsidHolder holder, int position) {

            String curretSsid= wifiList.get(position).SSID;
            holder.tvSsid.setText(curretSsid);

            if(itemSelected ==position){
                holder.itemView.setBackgroundColor(Color.GREEN);
            }
    }


    @Override
    public int getItemCount() {
        return wifiList.size();
    }

    public void setSSID(List<ScanResult> wifiList){
        this.wifiList=wifiList;
        notifyDataSetChanged();
    }

    public int getItemSelected() {
        return itemSelected;
    }

    class SsidHolder extends RecyclerView.ViewHolder{
        private TextView tvSsid;
        private ConstraintLayout rowItem;

        public SsidHolder( View itemView) {
            super(itemView);
            tvSsid = itemView.findViewById(R.id.tvSsid);
            this.rowItem=itemView.findViewById(R.id.rowItem);
            rowItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        setSingleSelection(getAdapterPosition());
                    Toast.makeText(itemView.getContext(), "Selected: "+tvSsid.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        private void setSingleSelection(int adapterPosition){
            if(adapterPosition==RecyclerView.NO_POSITION) return;
            notifyItemChanged(itemSelected);
            itemSelected = adapterPosition;
            notifyItemChanged(itemSelected);
        }
    }
}
