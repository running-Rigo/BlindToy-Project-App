package com.example.blindtoy_projekt_b.Bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class BtConnectionService {
    private static final String TAG = "L_BtConnectionService";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final BluetoothAdapter myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothSocket btSocket = null;
    private OutputStream outputStream = null;


    private Context context;

    private BluetoothDevice hc05 = null;

    public LiveData<String> connectionStatus;
    private MutableLiveData<String> mutableConnectionsStatus = new MutableLiveData<>("");

    public BtConnectionService(Context context) {
        this.context = context;
        setConnectionStatus("undefined");
    }


    private void setConnectionStatus(String blindSightStatus) {
        mutableConnectionsStatus.setValue(blindSightStatus);
        connectionStatus = mutableConnectionsStatus;
    }


    public void checkForBlindDog() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.d(TAG, myBluetoothAdapter.getBondedDevices().toString());
        try {
            hc05 = myBluetoothAdapter.getRemoteDevice("98:D3:71:F6:AC:80");
            Log.d(TAG, hc05.getName());
            setConnectionStatus("paired");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            setConnectionStatus("not known");
        }
    }

    public void setStatusConnecting(){
        setConnectionStatus("connecting");
    }

    public void connectToBlindSight() {
        int counter = 0;
        do {
            try {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                btSocket = hc05.createRfcommSocketToServiceRecord(MY_UUID);
                Log.d("TAG", btSocket.toString());
                btSocket.connect();
                Thread.sleep(2000);
                Log.d("TAG", String.valueOf(btSocket.isConnected()));
                setConnectionStatus("connected");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                setConnectionStatus("failed");
                //close the socket
                try {
                    btSocket.close();
                    Log.d(TAG, "run: Closed Socket.");
                } catch (IOException ex) {
                    Log.d(TAG, "connectThread: run: Unable to close connection in socket" + ex.getMessage());
                }
                Log.d(TAG, "run: couldn't connect to UUID: " + MY_UUID);
            }
            counter++;
        }while (!btSocket.isConnected() && counter < 3);

        try {
            outputStream = btSocket.getOutputStream();
            outputStream.write((byte)'0');
        } catch (IOException e) {
            e.printStackTrace();
        }

        //ConnectThread connectThreadAsync = new ConnectThread(hc05, MY_UUID);
        //connectThreadAsync.doInBackground();
    }

    public void sendChar(char character){
        try {
            outputStream.write((byte)character);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //this thread tries to connect to another devices  server-socket (not in the Main-Thread!)
    private class ConnectThread extends AsyncTask<Void,Void, Boolean> {

        private BluetoothSocket mmSocket;
        private BluetoothDevice bluetoothDevice;
        private UUID deviceUUID;

        public ConnectThread(BluetoothDevice device, UUID uuid) {
            Log.d(TAG, "ConnectThread: started.");
            bluetoothDevice = device;
            deviceUUID = uuid;
        }



            /*
            BluetoothSocket tmp = null;
            Log.d(TAG, "RUN mConnectTread");
            //get a bt-socket for a connection with the given btDevice:
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            try {
                Log.d(TAG, "trying to create RfcommSocket using UUID");
                //tmp = temporary variable
                tmp = bluetoothDevice.createRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                Log.d(TAG, "ConnectThread: Couldn't create RfcommkSocket" + e.getMessage());
            }
            mmSocket = tmp;
            myBluetoothAdapter.cancelDiscovery();
            try {
                //this is a blocking call and will only return on a succesful connection or an exception
                mmSocket.connect();
                Log.d(TAG, "run: ConnectThread connected"); //because it's a blocking call, it only goes to that point if it was successful;
            } catch (IOException e) {
                //close the socket
                try {
                    mmSocket.close();
                    Log.d(TAG, "run: Closed Socket.");
                } catch (IOException ex) {
                    Log.d(TAG, "connectThread: run: Unable to close connection in socket" + ex.getMessage());
                }
                Log.d(TAG, "run: couldn't connect to UUID: " + MY_UUID);
            }
            //
            connected(mmSocket, bluetoothDevice);
        }

             */

        @Override
        protected Boolean doInBackground(Void... voids) {
            BluetoothSocket btSocket = null;
            Boolean connectionSuccess = false;
            try {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
                btSocket = hc05.createRfcommSocketToServiceRecord(MY_UUID);
                Log.d(TAG, btSocket.toString());
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            int counter = 0;
            do {
                try {
                    btSocket.connect();
                    Thread.sleep(1000);
                    Log.d(TAG, ("Connection " + btSocket.isConnected()));
                    connectionSuccess = true;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                counter++;
            } while (!btSocket.isConnected() && counter < 3);
           return connectionSuccess;
        }

        @Override
        protected void onPostExecute (Boolean result){
            if(result){
                Log.d(TAG,"onPostExecute called: result = true");
                setConnectionStatus("connected");
            }
            else{
                Log.d(TAG,"onPostExecute called: result = false");
            }
        }

    /*
    private class ConnectedThread extends Thread{
        private final BluetoothSocket mySocket;
        private final InputStream myInStream;
        private final OutputStream myOutStream;

        public ConnectedThread(BluetoothSocket socket){
            Log.d(TAG,"connectedThread: starting.");
            mySocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            //at this point, connection was succesfull so dismiss the progressdialog:
            progressDialog.dismiss();
            try {
                tmpIn = mySocket.getInputStream();
                tmpOut = mySocket.getOutputStream();
            } catch (IOException e){
                e.printStackTrace();
            }

            myInStream = tmpIn;
            myOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024]; //buffer store for the stream;
            int bytes; //bytes returned from read()
            //keep listening to the inputStream until an exception occurs
            while (true) {
                //Read from the inputStream:
                try {
                    bytes = myInStream.read(buffer);
                    String inComingMessage = new String(buffer, 0, bytes);
                    Log.d(TAG, "InputStream: " + inComingMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                    break; //end the loop!
                }

            }
        }

        //sending Data to remote device:
        public void write(byte[] bytes){
            String text = new String(bytes, Charset.defaultCharset());
            Log.d(TAG,"write: Writing to outputstream: " + text);
            try{
                myOutStream.write(bytes);
            } catch (IOException e){
                Log.d(TAG,"write: Error writing to outputstream." + e.getMessage());
            }
        }


        //connection-shutdown (method called from activity):
        public void cancel(){
            try{
                mySocket.close();;
            }catch (IOException ex){

            }
        }
    }
    private void connected(BluetoothSocket mySocket, BluetoothDevice myDevice){
        Log.d(TAG,"connected: Starting");
        connectedThread = new ConnectedThread(mySocket);
        connectedThread.start();
    }

    //write the ConnectedThread from the Outside in an unsynchronized manner
    public void write(byte[] out){
        //Create temporary object
        ConnectedThread r;
        //Synchronize a copy of the ConnectedThread
        r = connectedThread;
        Log.d(TAG,"write: Write called.");
        r.write(out);
    }

     */
    }
}
