package net.hitdetector;

import android.bluetooth.BluetoothDevice;

import java.util.Set;

public interface BluetoothAdapterWrapper {

    Set<BluetoothDevice> getBondedDevices();

    void cancelDiscovery();

    boolean isDiscovering();

    void startDiscovery();
}
