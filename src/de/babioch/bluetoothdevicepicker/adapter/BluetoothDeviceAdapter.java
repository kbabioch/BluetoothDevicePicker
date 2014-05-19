package de.babioch.bluetoothdevicepicker.adapter;

import java.util.Locale;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.babioch.bluetoothdevicepicker.R;


public class BluetoothDeviceAdapter extends ArrayAdapter<BluetoothDevice> {

    private int resource;
    private LayoutInflater inflater;

    public BluetoothDeviceAdapter(Context context, int resource)
    {

        super(context, resource);
        this.resource = resource;
        inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        ViewHolder holder = null;

        if (convertView == null) {

            convertView = inflater.inflate(resource, parent, false);

            holder = new ViewHolder();

            holder.textDeviceName = (TextView) convertView.findViewById(R.id.textDeviceName);
            holder.textDeviceStatus = (TextView) convertView.findViewById(R.id.textDeviceStatus);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder)convertView.getTag();

        }

        BluetoothDevice device = getItem(position);
        holder.textDeviceName.setText(device.getName());
        holder.textDeviceStatus.setText(getNameOfState(device.getBondState()));

        return convertView;

    }

    private String getNameOfState(int bondState)
    {

        Locale l = Locale.getDefault();
        switch (bondState) {

            case BluetoothDevice.BOND_NONE:
                return getContext().getString(R.string.dialog_bluetooth_status_none).toUpperCase(l);

            case BluetoothDevice.BOND_BONDING:
                return getContext().getString(R.string.dialog_bluetooth_status_bonding).toUpperCase(l);

            case BluetoothDevice.BOND_BONDED:
                return getContext().getString(R.string.dialog_bluetooth_status_bonded).toUpperCase(l);

        }

        return null;

    }

    static class ViewHolder {

        TextView textDeviceName;
        TextView textDeviceStatus;

    }

}

