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

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;

import com.mytechia.simpleapi.InternalPerceptionAction;
import com.mytechia.simpleapi.util.ComActUtil;

import java.util.HashSet;

import static com.mytechia.simpleapi.InternalPerceptionAction.ON_NEW_OBO_BATTERY_STATUS;
import static com.mytechia.simpleapi.InternalPerceptionAction.ON_NEW_ROB_BATTERY_STATUS;
import static com.mytechia.simpleapi.InternalPerceptionAction.SET_REFRESH_INTERVAL;
import static com.mytechia.simpleapi.ServiceCommand.INTERNAL_PERCEPTION;



public class InternalPerceptionComponent extends ASimpleAPIComponent {

    private static final String LOG_TAG = "InternalComponent";
    private final SparseArray<Enum> mActionValues;
    private HashSet<IInternalPerceptionListener> listeners;


    public InternalPerceptionComponent(Context context, IComponentListener listener) {
        super(context, INTERNAL_PERCEPTION, InternalPerceptionComponent.class, LOG_TAG, listener);
        this.mActionValues = ComActUtil.enum2HashCode(InternalPerceptionAction.values());
        this.listeners = new HashSet<>();
    }


    @Override
    protected void handleHandlerMessage(Message msg) {
        InternalPerceptionAction action = (InternalPerceptionAction) mActionValues.get(msg.what);
        Log.d(LOG_TAG, "handleClientMessage(): " + action.name());
        switch (action) {
            case ON_NEW_OBO_BATTERY_STATUS:
                onNewOboBatteryStatus(msg.getData());
                break;

            case ON_NEW_ROB_BATTERY_STATUS:
                onNewRobBatteryStatus(msg.getData());
                break;

            default:
                Log.e(LOG_TAG, "Unrecognized action");
        }
    }


    @Override
    protected void onStartUp() {

    }


    public void setRefreshInterval(int millis) throws RemoteException {
        Bundle params = new Bundle();
        params.putInt(SET_REFRESH_INTERVAL.getParameters().getParamKey(0), millis);
        sendHandler(SET_REFRESH_INTERVAL, params);
    }


    public void addListener(IInternalPerceptionListener listener) {
        listeners.add(listener);
    }


    public void removeListener(IInternalPerceptionListener listener) {
        listeners.remove(listener);
    }


    private void onNewOboBatteryStatus(Bundle data) {
        Integer battery = data.getInt(ON_NEW_OBO_BATTERY_STATUS.getParameters().getParamKey(0));
        for (IInternalPerceptionListener listener: listeners)
            listener.onNewOboBatteryStatus(battery);
    }


    private void onNewRobBatteryStatus(Bundle data) {
        Integer battlevel = data.getInt(ON_NEW_ROB_BATTERY_STATUS.getParameters().getParamKey(0));
        Boolean charging= data.getBoolean(ON_NEW_ROB_BATTERY_STATUS.getParameters().getParamKey(1));
        for (IInternalPerceptionListener listener: listeners)
            listener.onNewRobBatteryStatus(battlevel, charging);
    }


}
