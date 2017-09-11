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

import com.mytechia.robobo.framework.hri.emotion.Emotion;
import com.mytechia.simpleapi.FacialExpressionAction;
import com.mytechia.simpleapi.util.ComActUtil;

import java.util.HashSet;

import static com.mytechia.robobo.framework.hri.emotion.Emotion.HAPPY;
import static com.mytechia.simpleapi.FacialExpressionAction.EMOTION_CHANGE;
import static com.mytechia.simpleapi.FacialExpressionAction.SET_CURRENT_EMOTION;
import static com.mytechia.simpleapi.FacialExpressionAction.SET_TEMPORAL_EMOTION;
import static com.mytechia.simpleapi.ServiceCommand.FACIAL_EXPRESSION;



public class FacialExpressionComponent extends ASimpleAPIComponent {

    private static final String LOG_TAG = "FacialComponent";
    private SparseArray<Enum> mActionValues;
    private HashSet<IFacialExpressionListener> listeners;

    private Emotion currentEmotion;


    public FacialExpressionComponent(Context context, IComponentListener listener) {
        super(context, FACIAL_EXPRESSION, FacialExpressionComponent.class, LOG_TAG, listener);
        this.mActionValues = ComActUtil.enum2HashCode(FacialExpressionAction.values());
        this.listeners = new HashSet<>();
        this.currentEmotion = HAPPY;
    }


    @Override
    protected void handleHandlerMessage(Message msg) {
        FacialExpressionAction action = (FacialExpressionAction) mActionValues.get(msg.what);
        Log.d(LOG_TAG, "handleHandlerMessage(): " + action.name());
        switch (action) {
            case GET_CURRENT_EMOTION:
                break;

            case EMOTION_CHANGE:
                emotionChange(msg.getData());
                break;

            default:
                Log.e(LOG_TAG, "Unrecognized action");
        }
    }


    @Override
    protected void onStartUp() {

    }


    public void setCurrentEmotion(Emotion emotion) throws RemoteException {
        Bundle params = new Bundle();
        params.putInt(SET_CURRENT_EMOTION.getParameters().getParamKey(0), emotion.hashCode());
        sendHandler(SET_CURRENT_EMOTION, params);
        currentEmotion = emotion;
    }


    public void setTemporalEmotion(Emotion emotion, Long duration, final Emotion nextEmotion)
            throws RemoteException {
        Bundle params = new Bundle();
        params.putInt(SET_TEMPORAL_EMOTION.getParameters().getParamKey(0), emotion.hashCode());
        params.putLong(SET_TEMPORAL_EMOTION.getParameters().getParamKey(1), duration);
        params.putInt(SET_TEMPORAL_EMOTION.getParameters().getParamKey(2), nextEmotion.hashCode());
        sendHandler(SET_TEMPORAL_EMOTION, params);
    }


    public Emotion getCurrentEmotion() {
        return currentEmotion;
    }


    private void emotionChange(Bundle data) {
        SparseArray<Enum> emotionValues = ComActUtil.enum2HashCode(Emotion.values());
        Emotion emotion = (Emotion) emotionValues.get(data.getInt(EMOTION_CHANGE.getParameters()
                                                                                .getReturnKey()));
        currentEmotion = emotion;
        for (IFacialExpressionListener listener: listeners)
            listener.emotionChange(emotion);
    }


    public void addListener(IFacialExpressionListener listener) {
        listeners.add(listener);
    }


    public void removeListener(IFacialExpressionListener listener) {
        listeners.remove(listener);
    }


}
