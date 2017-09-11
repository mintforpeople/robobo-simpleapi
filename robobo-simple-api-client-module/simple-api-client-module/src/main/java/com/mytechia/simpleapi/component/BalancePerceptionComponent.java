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

import com.mytechia.simpleapi.BalancePerceptionAction;
import com.mytechia.simpleapi.util.ComActUtil;

import java.util.HashSet;

import static com.mytechia.simpleapi.BalancePerceptionAction.ON_ACCELERATION;
import static com.mytechia.simpleapi.BalancePerceptionAction.ON_ORIENTATION_CHANGED;
import static com.mytechia.simpleapi.BalancePerceptionAction.SET_DETECTION_THRESHOLD;
import static com.mytechia.simpleapi.ServiceCommand.BALANCE_PERCEPTION;



public class BalancePerceptionComponent extends ASimpleAPIComponent {

    private static final String LOG_TAG = "BalanceComponent";
    private final SparseArray<Enum> mActionValues;
    private HashSet<IBalancePerceptionListener> listeners;


    public BalancePerceptionComponent(Context context, IComponentListener listener) {
        super(context, BALANCE_PERCEPTION, BalancePerceptionComponent.class, LOG_TAG, listener);
        this.mActionValues = ComActUtil.enum2HashCode(BalancePerceptionAction.values());
        this.listeners = new HashSet<>();
    }


    @Override
    protected void handleHandlerMessage(Message msg) {
        BalancePerceptionAction action = (BalancePerceptionAction) mActionValues.get(msg.what);
        Log.d(LOG_TAG, "handleClientMessage(): " + action.name());
        switch (action) {
            case ON_ACCELERATION_CHANGE:
                onAccelerationChange();
                break;

            case ON_ACCELERATION:
                onAcceleration(msg.getData());
                break;

            case ON_ORIENTATION_CHANGED:
                onOrientationChanged(msg.getData());
                break;

            default:
                Log.e(LOG_TAG, "Unrecognized action");
        }
    }


    @Override
    protected void onStartUp() {

    }


    public void addListener(IBalancePerceptionListener listener) {
        listeners.add(listener);
    }


    public void removeListener(IBalancePerceptionListener listener) {
        listeners.remove(listener);
    }


    public void setDetectionThreshold(int threshold) throws RemoteException {
        Bundle params = new Bundle();
        params.putInt(SET_DETECTION_THRESHOLD.getParameters().getParamKey(0), threshold);
        sendHandler(SET_DETECTION_THRESHOLD, params);
    }


    private void onAccelerationChange() {
        for (IBalancePerceptionListener listener: listeners)
            listener.onAccelerationChange();
    }


    private void onAcceleration(Bundle data) {
        Integer xaccel = data.getInt(ON_ACCELERATION.getParameters().getParamKey(0));
        Integer yaccel = data.getInt(ON_ACCELERATION.getParameters().getParamKey(1));
        Integer zaccel = data.getInt(ON_ACCELERATION.getParameters().getParamKey(2));
        for (IBalancePerceptionListener listener: listeners)
            listener.onAcceleration(xaccel, yaccel, zaccel);
    }


    private void onOrientationChanged(Bundle data) {
        Float yaw = data.getFloat(ON_ORIENTATION_CHANGED.getParameters().getParamKey(0));
        Float pitch = data.getFloat(ON_ORIENTATION_CHANGED.getParameters().getParamKey(1));
        Float roll = data.getFloat(ON_ORIENTATION_CHANGED.getParameters().getParamKey(2));
        for (IBalancePerceptionListener listener: listeners)
            listener.onOrientationChanged(yaw, pitch, roll);
    }


}
