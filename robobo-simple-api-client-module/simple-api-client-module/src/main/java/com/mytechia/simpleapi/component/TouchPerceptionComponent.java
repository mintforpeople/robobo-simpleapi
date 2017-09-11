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
import android.util.Log;
import android.util.SparseArray;

import com.mytechia.robobo.framework.hri.touch.TouchGestureDirection;
import com.mytechia.simpleapi.TouchPerceptionAction;
import com.mytechia.simpleapi.util.ComActUtil;

import java.util.HashSet;

import static com.mytechia.simpleapi.ServiceCommand.TOUCH_PERCEPTION;
import static com.mytechia.simpleapi.TouchPerceptionAction.CARESS;
import static com.mytechia.simpleapi.TouchPerceptionAction.FLING;
import static com.mytechia.simpleapi.TouchPerceptionAction.TAP;
import static com.mytechia.simpleapi.TouchPerceptionAction.TOUCH;



public class TouchPerceptionComponent extends ASimpleAPIComponent {

    private static final String LOG_TAG = "TouchComponent";
    private final SparseArray<Enum> mActionValues;
    private final SparseArray<Enum> directionValues;

    private HashSet<ITouchPerceptionListener> listeners;


    public TouchPerceptionComponent(Context context, IComponentListener listener) {
        super(context, TOUCH_PERCEPTION, TouchPerceptionComponent.class, LOG_TAG, listener);
        this.mActionValues = ComActUtil.enum2HashCode(TouchPerceptionAction.values());
        this.directionValues = ComActUtil.enum2HashCode(TouchGestureDirection.values());
        this.listeners = new HashSet<>();
    }

    @Override
    protected void handleHandlerMessage(Message msg) {
        TouchPerceptionAction action = (TouchPerceptionAction) mActionValues.get(msg.what);
        Log.d(LOG_TAG, "handleHandlerMessage(): " + action.name());
        switch (action) {
            case TAP:
                tap(msg.getData());
                break;

            case TOUCH:
                touch(msg.getData());
                break;

            case FLING:
                fling(msg.getData());
                break;

            case CARESS:
                caress(msg.getData());
                break;

            default:
                Log.e(LOG_TAG, "Unrecognized action");
        }
    }


    @Override
    protected void onStartUp() {

    }


    public void addListener(ITouchPerceptionListener listener) {
        listeners.add(listener);
    }


    public void removeListener(ITouchPerceptionListener listener) {
        listeners.remove(listener);
    }


    private void tap(Bundle data) {
        Integer x = data.getInt(TAP.getParameters().getParamKey(0));
        Integer y = data.getInt(TAP.getParameters().getParamKey(1));
        for (ITouchPerceptionListener listener: listeners)
            listener.tap(x, y);
    }


    private void touch(Bundle data) {
        Integer x = data.getInt(TOUCH.getParameters().getParamKey(0));
        Integer y = data.getInt(TOUCH.getParameters().getParamKey(1));
        for (ITouchPerceptionListener listener: listeners)
            listener.touch(x, y);
    }


    private void fling(Bundle data) {
        TouchGestureDirection dir = getDirection(FLING, data);
        Double angle = data.getDouble(FLING.getParameters().getParamKey(1));
        Long time = data.getLong(FLING.getParameters().getParamKey(2));
        Double distance = data.getDouble(FLING.getParameters().getParamKey(3));
        for (ITouchPerceptionListener listener: listeners)
            listener.fling(dir, angle, time, distance);
    }


    private void caress(Bundle data) {
        TouchGestureDirection dir = getDirection(CARESS, data);
        for (ITouchPerceptionListener listener: listeners)
            listener.caress(dir);
    }


    private TouchGestureDirection getDirection(TouchPerceptionAction action, Bundle data) {
        Integer code = data.getInt(action.getParameters().getParamKey(0));
        return (TouchGestureDirection) directionValues.get(code);
    }


}
