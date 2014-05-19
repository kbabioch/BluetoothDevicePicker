package de.babioch.bluetoothdevicepicker.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import de.babioch.bluetoothdevicepicker.R;
import de.babioch.bluetoothdevicepicker.adapter.BluetoothDeviceAdapter;


public class BluetoothDevicePickerDialog extends DialogFragment {

    public interface BluetoothDevicePickedListener {

        void bluetoothDevicePicked(BluetoothDevice device);

    }

    private static final String TAG = BluetoothDevicePickerDialog.class.getName();

    private static final int ENABLE_BLUETOOTH_REQUEST_CODE = 0;

    private static final String BUNDLE_FOUND_DEVICES = "FOUND_DEVICES";

    private BluetoothDevicePickedListener activity;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothBroadcastReceiver receiver;
    BluetoothDeviceAdapter adapterDevices;

    private ListView listDevices;

    @Override
    public void onAttach(Activity activity)
    {

        super.onAttach(activity);

        try {

            this.activity = (BluetoothDevicePickedListener) activity;

        } catch (ClassCastException e) {

            throw new ClassCastException(activity + " must implement " + BluetoothDevicePickedListener.class.getName());

        }

        Log.d(TAG, "Registering Bluetooth broadcast receiver");
        receiver = new BluetoothBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(receiver, filter);

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {

            // TODO Own exception?
            throw new RuntimeException("No bluetooth adapter available");

        }

        adapterDevices = new BluetoothDeviceAdapter(getActivity(), R.layout.list_item_bluetooth);

        if (savedInstanceState == null) {

            if (!bluetoothAdapter.isEnabled()) {

                Log.d(TAG, "Bluetooth is disabled, asking user to enable it");
                startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), ENABLE_BLUETOOTH_REQUEST_CODE);

            } else {

                bluetoothAdapter.startDiscovery();

            }

            for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {

                adapterDevices.add(device);

            }

        }  else {

            BluetoothDevice[] devices = (BluetoothDevice[]) savedInstanceState.getParcelableArray(BUNDLE_FOUND_DEVICES);
            adapterDevices.addAll(devices);

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (requestCode == ENABLE_BLUETOOTH_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {

                Log.d(TAG, "User enabled Bluetooth");
                bluetoothAdapter.startDiscovery();

            } else {

                // TODO Own exception?
                throw new RuntimeException("Bluetooth must be enabled for this dialog");

            }

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);

        listDevices = (ListView) view.findViewById(R.id.listDevices);
        listDevices.setAdapter(adapterDevices);
        listDevices.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                BluetoothDevice device = adapterDevices.getItem(position);

                Log.d(TAG, "Device selected: "  + device.getName() + " (" + device.getAddress() + ")");

                activity.bluetoothDevicePicked(device);
                dismiss();

            }

        });

        return view;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(R.string.dialog_bluetooth_title);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;

    }

    @Override
    public void onDestroyView()
    {

        super.onDestroyView();

        if (bluetoothAdapter != null) {

            Log.d(TAG, "Explicitly canceling Bluetooth discovery");
            bluetoothAdapter.cancelDiscovery();

        }

    }

    @Override
    public void onDetach()
    {

        super.onDetach();

        Log.d(TAG, "Unregistering Bluetooth broadcast receiver");
        getActivity().unregisterReceiver(receiver);

    }

    @Override
    public void onSaveInstanceState(Bundle bundle)
    {

        super.onSaveInstanceState(bundle);

        BluetoothDevice[] devices = new BluetoothDevice[adapterDevices.getCount()];

        for (int i = 0; i < adapterDevices.getCount(); i++) {

            devices[i] = adapterDevices.getItem(i);

        }

        bundle.putParcelableArray(BUNDLE_FOUND_DEVICES, devices);

    }

    private boolean deviceAlreadyDiscovered(BluetoothDevice device)
    {

        for (int i = 0; i < adapterDevices.getCount(); i++) {

            if (adapterDevices.getItem(i).equals(device)) {

                return true;

            }

        }

        return false;

    }

    private class BluetoothBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (!deviceAlreadyDiscovered(device)) {

                    Log.d(TAG, "New device found: " + device.getName() + " (" + device.getAddress() + ")");
                    adapterDevices.add(device);

                }

            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {

                Log.d(TAG, "Bluetooth discovery started");

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                Log.d(TAG, "Bluetooth discovery finished");
                bluetoothAdapter.startDiscovery();

            }

        }

    }

}
