/*******************************************************************************
 *
 *   Copyright 2017 Miguel Vilar <mavilarr@gmail.com>
 *
 *   This file is part of Robobo Simple API.
 *
 *   Robobo Simple API is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Robobo Simple API is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Robobo Simple API.  If not,
 *   see <http://www.gnu.org/licenses/>.
 *
 ******************************************************************************/

package com.mytechia.simpleapi.component;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;

import com.mytechia.simpleapi.IMessengerHandler;
import com.mytechia.simpleapi.IncomingHandler;
import com.mytechia.simpleapi.ServiceCommand;
import com.mytechia.simpleapi.util.ComActUtil;

import static com.mytechia.simpleapi.ServiceCommand.CLIENT;
import static com.mytechia.simpleapi.ServiceCommand.ERROR;
import static com.mytechia.simpleapi.ServiceCommand.ROB_NAME;
import static com.mytechia.simpleapi.ServiceCommand.SHUTDOWN;
import static com.mytechia.simpleapi.ServiceCommand.STARTUP;



public abstract class ASimpleAPIComponent implements IMessengerHandler {

    private final String LOG_TAG;

    private boolean mBound;
    private Context mContext;
    private ServiceConnection mConnection;
    private ServiceCommand mClientType;
    private Class mClientClass;

    private SparseArray<Enum> mCmdValues;
    private Messenger mMessenger;
    private Messenger mService;

    private String mRobName;
    private boolean mRobBound;
    private Messenger mHandler;
    private boolean mHandlerBound;

    private IComponentListener listener;



    protected ASimpleAPIComponent(Context context, ServiceCommand clientType, Class clientClass,
                                  String logTag, IComponentListener listener) {
        this.mBound = false;
        this.mContext = context;
        this.mConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                onConnectedService(new Messenger(service));
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                onDisconnectedService();
            }

        };
        this.mClientType = clientType;
        this.mClientClass = clientClass;
        this.LOG_TAG = logTag;
        this.mHandler = null;
        this.mHandlerBound = false;
        this.mRobName = null;
        this.mRobBound = false;
        this.listener = listener;
    }


    protected abstract void handleHandlerMessage(Message msg);


    protected abstract void onStartUp() throws RemoteException;


    @Override
    public void handleMessage(Message msg) {
        if (mHandlerBound) {
            handleHandlerMessage(msg);
        } else if (mBound){
            ServiceCommand cmd = (ServiceCommand) mCmdValues.get(msg.what);
            Log.d(LOG_TAG, "handleMessage(): " + cmd.name());
            switch (cmd) {
                case OK:
                    if (!mRobBound) {
                        mRobBound = true;
                        sendService(mClientType);
                    } else if (!mHandlerBound) {
                        mHandlerBound = true;
                        try {
                            onStartUp();
                            listener.onComponentStarted(mClientClass);
                        } catch (RemoteException e) {
                            listener.onComponentError(mClientClass,
                                    "Error starting up the component: " + e.getMessage());
                        }
                    }
                    break;

                case ERROR:
                    mHandlerBound = false;
                    mHandler = null;
                    listener.onComponentError(mClientClass, msg.getData().getString(ERROR.name()));
                    break;

                case CLIENT:
                    mHandler = msg.replyTo;
                    try {
                        sendHandler(STARTUP);
                    } catch (RemoteException e) {
                        Log.e(LOG_TAG, "Error sending startup: " + e.getMessage());
                    }
                    break;

                case ROB_NAME:
                    Bundle opt = new Bundle();
                    opt.putString(ROB_NAME.name(), mRobName);
                    sendService(ROB_NAME, opt);
                    break;

                default:
                    Log.e(LOG_TAG, "Unrecognized command: " + cmd.name());
            }
        }
    }


    public void bind(String robName) {
        if (!mBound) {
            this.mRobName = robName;
            Intent intent = new Intent();
            intent.setClassName("com.mytechia.simpleapi.service.app",
                    "com.mytechia.simpleapi.service.app.RoboboSimpleAPIService");
            mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }


    public void unbind() {
        if (mBound) {
            if (mHandlerBound) {
                try {
                    sendHandler(SHUTDOWN);
                } catch (RemoteException e) {
                    Log.e(LOG_TAG, "Error sending shutdown: " + e.getMessage());
                }
                mHandlerBound = false;
            }
            mContext.unbindService(mConnection);
            mRobName = null;
            mRobBound = false;
            mBound = false;
            mHandlerBound = false;
        }
    }


    private void onConnectedService(Messenger service) {
        Log.i(LOG_TAG, "Connected to Simple API Service");

        mBound = true;
        mService = service;
        mCmdValues = ComActUtil.enum2HashCode(ServiceCommand.values());

        mMessenger = new Messenger(new IncomingHandler(this));
        sendService(CLIENT);
    }


    private void onDisconnectedService() {
        String errorMsg = "Disconnected from Simple API Service";
        Log.e(LOG_TAG, errorMsg);

        mBound = false;
        mService = null;
        mCmdValues = null;

        mRobBound = false;
        mRobName = null;
        mHandlerBound = false;
        mHandler = null;

        // Prevent client from rebinding
        mContext.unbindService(mConnection);

        //listener.onComponentError(mClientType, errorMsg);
        listener.onComponentError(mClientClass, errorMsg);
    }


    private void send(Messenger dest, Message msg) throws RemoteException {
        msg.replyTo = mMessenger;
        if (dest != null)
            dest.send(msg);
        else
            throw new RemoteException("Destination messenger is not available");
    }


    private void sendService(Message msg) {
        ServiceCommand cmd = (ServiceCommand) mCmdValues.get(msg.what);
        msg.replyTo = mMessenger;
        try {
            send(mService, msg);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Error sending " + cmd.name() + " message");
        }
    }


    private void sendService(ServiceCommand cmd, Bundle data) {
        Message msg = Message.obtain(null, cmd.hashCode());
        msg.setData(data);
        sendService(msg);
    }


    private void sendService(ServiceCommand cmd) {
        sendService(cmd, null);
    }


    protected void sendHandler(Message msg) throws RemoteException {
        send(mHandler, msg);
    }


    protected void sendHandler(Enum action, Bundle data) throws RemoteException {
        Message msg = Message.obtain(null, action.hashCode());
        msg.setData(data);
        try {
            sendHandler(msg);
        } catch (RemoteException e) {
            throw new RemoteException("Handler is no longer available");
        }
    }


    protected void sendHandler(Enum action) throws RemoteException {
        sendHandler(action, null);
    }


}
