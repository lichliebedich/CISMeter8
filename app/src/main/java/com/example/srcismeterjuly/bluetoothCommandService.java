package com.example.srcismeterjuly;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class bluetoothCommandService {
	// Debugging
    //private static final String TAG = "BluetoothCommandService";
    private static final boolean D = true;

    // Unique UUID for this application
    //private static final UUID MY_UUID = UUID.fromString("04c6093b-0000-1000-8000-00805f9b34fb");
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    
    
    
    // Member fields
    private final BluetoothAdapter mAdapter;
    private final Handler mHandler;
    private Threadconnect_1 mThreadconnect_1;
    private Threadconnect_2 mThreadconnect_2;
    private int mState;
//    private BluetoothDevice mSavedDevice;
//    private int mConnectionLostCount;
    
    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
    
    // Constants that indicate command to computer
    public static final int EXIT_CMD = -1;
    public static final int VOL_UP = 1;
    public static final int VOL_DOWN = 2;
    public static final int MOUSE_MOVE = 3;
    

    public bluetoothCommandService(Context context, Handler handler) {
    	mAdapter = BluetoothAdapter.getDefaultAdapter();
    	mState = STATE_NONE;
    	//mConnectionLostCount = 0;
    	mHandler = handler;
    }
    
    private synchronized void setState(int state) {
        if (D) Log.d("setState :", mState + " -> " + state);
        mState = state;

        // Give the new state to the Handler so the UI Activity can update
     //   mHandler.obtainMessage(RemoteBluetooth.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    public synchronized int getState() {
        return mState;
    }

    public synchronized void start() {
        if (D) Log.d("start Bluetooth", "start");

        // Cancel any thread attempting to make a connection
        if (mThreadconnect_1 != null) {mThreadconnect_1.cancel_1(); mThreadconnect_1 = null;}

        // Cancel any thread currently running a connection
        if (mThreadconnect_2 != null) {mThreadconnect_2.cancel_2(); mThreadconnect_2 = null;}

        setState(STATE_LISTEN);
    }
    
    public synchronized void connect_1(BluetoothDevice device) {
    	if (D) Log.d("connect_1 to: ", device+"");

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mThreadconnect_1 != null) {
            	mThreadconnect_1.cancel_1();
            	mThreadconnect_1 = null;
            	}
        }

        // Cancel any thread currently running a connection
        if (mThreadconnect_2 != null) {
        	mThreadconnect_2.cancel_2(); 
        	mThreadconnect_2 = null;
        	}

        // Start the thread to connect with the given device
        mThreadconnect_1 = new Threadconnect_1(device);
        mThreadconnect_1.start();
        setState(STATE_CONNECTING);
    }

    public synchronized void connect_2(BluetoothSocket socket, BluetoothDevice device) {
        if (D) Log.d("connect_2-->>","BluetoothSocket:"+socket+"device:"+device);

        // Cancel the thread that completed the connection
        if (mThreadconnect_1 != null) {
        	mThreadconnect_1.cancel_1(); 
        	mThreadconnect_1 = null;
        	}

        // Cancel any thread currently running a connection
        if (mThreadconnect_2 != null) {
        	mThreadconnect_2.cancel_2(); 
        	mThreadconnect_2 = null;
        	}

        // Start the thread to manage the connection and perform transmissions
        mThreadconnect_2 = new Threadconnect_2(socket);
        mThreadconnect_2.start();

   
        
        setState(STATE_CONNECTED);
    }

    /**
     * Stop all threads
     */
    
    public synchronized void stop() {
        if (D) Log.d("stop:","D:"+D);
        if (mThreadconnect_1 != null) {mThreadconnect_1.cancel_1(); mThreadconnect_1 = null;}
        if (mThreadconnect_2 != null) {mThreadconnect_2.cancel_2(); mThreadconnect_2 = null;}
        
        setState(STATE_NONE);
    }
 
    public void write(byte[] out) {
        // Create temporary object
        Threadconnect_2 r;
        // Synchronize a copy of the Threadconnect_2
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mThreadconnect_2;
        }
        // Perform the write unsynchronized        
        r.write(out);
    }
    
    public void write(int out) {
    	// Create temporary object
        Threadconnect_2 r;
        // Synchronize a copy of the Threadconnect_2
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mThreadconnect_2;
        }
        // Perform the write unsynchronized
        r.write(out);
    }
    
    public void write(String out) {
        // Create temporary object
        Threadconnect_2 r;
        // Synchronize a copy of the Threadconnect_2
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mThreadconnect_2;
        }
        // Perform the write unsynchronized
        r.write(out);
    }
    
  
    private void connectionFailed() {
        setState(STATE_LISTEN);

    }

    private void connectionLost() {
        	setState(STATE_LISTEN);

    }
    

  private class Threadconnect_1 extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        
        public Threadconnect_1(BluetoothDevice device) {
        	 Log.d("create Threadconnect_1",device+"");
	        mmDevice = device;
	        BluetoothSocket tmp = null;
	        Method m = null;
	        try {
	            m = device.getClass().getMethod("createRfcommSocket",new Class[] {int.class});
	        } catch (SecurityException e) {e.printStackTrace();
	        } catch (NoSuchMethodException e) {e.printStackTrace();} 
	        try {
	            tmp = (BluetoothSocket) m.invoke(device, 1);
	        } catch (IllegalArgumentException e) {e.printStackTrace();
	        } catch (IllegalAccessException e) {e.printStackTrace();
	        } catch (InvocationTargetException e) {e.printStackTrace();}
	        mmSocket = tmp;
        }
        public void run() {
         //   Log.i("BEGIN run mThreadconnect_1","");
            setName("Threadconnect_1");
            mAdapter.cancelDiscovery();
            try {
                mmSocket.connect();
            } catch (IOException e) {

		            connectionFailed();
		            
					try {
						mmSocket.close();
					} catch (IOException e2) {Log.e("Bluetooth_1:", "unable to close() socket during connection failure", e2);}
					
					bluetoothCommandService.this.start();
					return;
            }//catch

            // Reset the Threadconnect_1 because we're done
            synchronized (bluetoothCommandService.this) {mThreadconnect_1 = null;}

            // Start the connect_2 thread
            connect_2(mmSocket, mmDevice);
        }
        public void cancel_1() {
            try {
            	//Log.i("Fn cancel_1 Threadconnect_1:", "mmSocket.close");
                mmSocket.close();
            } catch (IOException e) {Log.e("cancel_1 Bluetooth:", "close() of connect socket failed", e);}
        }
    }


    private class Threadconnect_2 extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public Threadconnect_2(BluetoothSocket socket) {
            Log.d("create Threadconnect_2","");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {Log.e("Threadconnect_2:", "temp sockets not created", e);}

            mmInStream  = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
        	// Log.i("BEGIN run mThreadconnect_2","");
            byte[] buffer = new byte[1024];
            
            // Keep listening to the InputStream while connected
            while (true) {
                try {
                	// Read from the InputStream
                    int bytes = mmInStream.read(buffer);
                	} catch (IOException e) {Log.e("Bluetooth_2:", "disconnected", e);
							                 connectionLost();
							                 break;
                      						}
            }//while
        }

        public void write(byte[] buffer) {
            try {
            //	Log.e("Fn write byte Threadconnect_2:", buffer+"|");
                mmOutStream.write(buffer);
            } catch (IOException e) {
                Log.e("Error write byte:", "Exception during write", e);
            }
        }
        public void write(int out) {
        	try {
        		//Log.e("write int Threadconnect_2:", out+"|");
                mmOutStream.write(out);
            } catch (IOException e) {Log.e("Error write int:", "Exception during write", e);}
        }
        
        
      /* public void write(String message) {
        	try {
        			int code;
        			for(int i = 0; i < message.length(); i++) {
        			code = (int)message.charAt(i);
        			if ((0xE01<=code) && (code <= 0xE5B ))			
        				mmOutStream.write((char)(code - 0xD60));
        			else
        				mmOutStream.write(code);			
        			}
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }*/
		public void write(String message) {
			try {
				 //Log.e("Fn write String Threadconnect_2:", message);
				 byte[] largemessage = message.getBytes("TIS-620") ;
				 mmOutStream.write(largemessage);
								      							      			
			   //Log.w("largemessage:", largemessage[largemessage.length]+"/");
			} catch (IOException e) {Log.e("Error write String:", "Exception during write", e);}
		}
        
        public void cancel_2() {
            try {
            	//Log.e("Fn cancel_2 Threadconnect_2", "mmSocket.close");
            	//mmOutStream.write(EXIT_CMD);
                mmSocket.close();
            } catch (IOException e) {Log.e("cancel_2 Bluetooth:", "close() of connect socket failed", e);}
        }
    }
}
