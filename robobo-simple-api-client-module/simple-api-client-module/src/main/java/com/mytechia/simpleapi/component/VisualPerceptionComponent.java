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
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;

import com.mytechia.robobo.framework.hri.vision.blobTracking.Blob;
import com.mytechia.robobo.framework.hri.vision.blobTracking.Blobcolor;
import com.mytechia.simpleapi.Parameters;
import com.mytechia.simpleapi.VisualPerceptionAction;
import com.mytechia.simpleapi.util.ComActUtil;

import org.opencv.core.Point;

import java.util.HashSet;

import static com.mytechia.robobo.framework.hri.vision.blobTracking.Blobcolor.BLUE;
import static com.mytechia.robobo.framework.hri.vision.blobTracking.Blobcolor.CUSTOM;
import static com.mytechia.robobo.framework.hri.vision.blobTracking.Blobcolor.GREEN;
import static com.mytechia.robobo.framework.hri.vision.blobTracking.Blobcolor.RED;
import static com.mytechia.simpleapi.ServiceCommand.VISUAL_PERCEPTION;
import static com.mytechia.simpleapi.VisualPerceptionAction.BLOB_DETECTED;
import static com.mytechia.simpleapi.VisualPerceptionAction.BLOB_DISAPPEAR;
import static com.mytechia.simpleapi.VisualPerceptionAction.BLOB_TRACKING_CONFIG;
import static com.mytechia.simpleapi.VisualPerceptionAction.BLOB_TRACKING_START;
import static com.mytechia.simpleapi.VisualPerceptionAction.BLOB_TRACKING_STOP;
import static com.mytechia.simpleapi.VisualPerceptionAction.BLOB_TRACKING_THRESHOLD;
import static com.mytechia.simpleapi.VisualPerceptionAction.FACE_APPEAR;
import static com.mytechia.simpleapi.VisualPerceptionAction.FACE_DETECTED;
import static com.mytechia.simpleapi.VisualPerceptionAction.FACE_DETECTION_START;
import static com.mytechia.simpleapi.VisualPerceptionAction.FACE_DETECTION_STOP;
import static com.mytechia.simpleapi.VisualPerceptionAction.ON_BRIGHTNESS;
import static com.mytechia.simpleapi.VisualPerceptionAction.SET_HAS_CHANGED_AMOUNT;
import static com.mytechia.simpleapi.VisualPerceptionAction.SET_REFRESH_RATE;



public class VisualPerceptionComponent extends ASimpleAPIComponent {

    private static final String LOG_TAG = "VisualComponent";
    private SparseArray<Enum> mActionValues;

    private HashSet<IVisualPerceptionListener> listeners;


    public VisualPerceptionComponent(Context context, IComponentListener listener) {
        super(context, VISUAL_PERCEPTION, VisualPerceptionComponent.class, LOG_TAG, listener);
        this.mActionValues = ComActUtil.enum2HashCode(VisualPerceptionAction.values());
        this.listeners = new HashSet<>();
    }


    @Override
    protected void handleHandlerMessage(Message msg) {
        VisualPerceptionAction action = (VisualPerceptionAction) mActionValues.get(msg.what);
        Log.d(LOG_TAG, "handleHandlerMessage(): " + action.name());
        switch (action) {
            case BLOB_DETECTED:
                onTrackingBlob(msg.getData());
                break;

            case BLOB_DISAPPEAR:
                onBlobDisappear(msg.getData());
                break;

            case FACE_DETECTED:
                onFaceDetected(msg.getData());
                break;

            case FACE_APPEAR:
                onFaceAppear(msg.getData());
                break;

            case FACE_DISAPPEAR:
                onFaceDissapear();
                break;

            case ON_BRIGHTNESS:
                onBrightness(msg.getData());
                break;

            case ON_BRIGHTNESS_CHANGED:
                onBrightnessChanged();
                break;

            default:
                Log.e(LOG_TAG, "Unrecognized action");
        }
    }


    @Override
    protected void onStartUp() {

    }


    public void startBlobTracking() throws RemoteException {
        sendHandler(BLOB_TRACKING_START);
    }


    public void stopBlobTracking() throws RemoteException {
        sendHandler(BLOB_TRACKING_STOP);
    }


    public void blobTrackingConfig (boolean red, boolean green, boolean blue, boolean custom) throws RemoteException {
        Bundle params = new Bundle();
        params.putBoolean(BLOB_TRACKING_CONFIG.getParameters().getParamKey(0), red);
        params.putBoolean(BLOB_TRACKING_CONFIG.getParameters().getParamKey(1), green);
        params.putBoolean(BLOB_TRACKING_CONFIG.getParameters().getParamKey(2), blue);
        params.putBoolean(BLOB_TRACKING_CONFIG.getParameters().getParamKey(3), custom);
        sendHandler(BLOB_TRACKING_CONFIG, params);
    }


    public void blobTrackingThreshold (int th) throws RemoteException {
        Bundle params = new Bundle();
        params.putInt(BLOB_TRACKING_THRESHOLD.getParameters().getParamKey(0), th);
        sendHandler(BLOB_TRACKING_THRESHOLD, params);
    }


    public void startFaceDetection() throws RemoteException {
        sendHandler(FACE_DETECTION_START);
    }


    public void stopFaceDetection() throws RemoteException {
        sendHandler(FACE_DETECTION_STOP);
    }


    public void setRefreshRate(int millis) throws RemoteException {
        Bundle params = new Bundle();
        params.putInt(SET_REFRESH_RATE.getParameters().getParamKey(0), millis);
        sendHandler(SET_REFRESH_RATE, params);
    }


    public void setHasChangedAmount(int amount) throws RemoteException {
        Bundle params = new Bundle();
        params.putInt(SET_HAS_CHANGED_AMOUNT.getParameters().getParamKey(0), amount);
        sendHandler(SET_HAS_CHANGED_AMOUNT, params);
    }


    public void addListener(IVisualPerceptionListener listener) {
        listeners.add(listener);
    }


    public void removeListener(IVisualPerceptionListener listener) {
        listeners.remove(listener);
    }


    private void onTrackingBlob(Bundle data) {
        Parameters blobTrackedParams = BLOB_DETECTED.getParameters();
        Blobcolor c = getBlobColorParams(BLOB_DETECTED, data);
        Point blobCoords = new Point();
        blobCoords.x = data.getInt(blobTrackedParams.getParamKey(6));
        blobCoords.y = data.getInt(blobTrackedParams.getParamKey(7));
        Integer blobSize = data.getInt(blobTrackedParams.getParamKey(8));
        Boolean isBall = data.getBoolean(blobTrackedParams.getParamKey(9));
        Boolean isSquare = data.getBoolean(blobTrackedParams.getParamKey(10));
        Blob blob = new Blob(c, blobCoords, blobSize, isBall, isSquare);
        for (IVisualPerceptionListener listener : listeners)
            listener.onTrackingBlob(blob);
    }


    private void onBlobDisappear(Bundle data) {
        for (IVisualPerceptionListener listener : listeners)
            listener.onBlobDisappear(getBlobColorParams(BLOB_DISAPPEAR, data));
    }


    private Blobcolor getBlobColorParams(VisualPerceptionAction action, Bundle data) {
        if (RED.hmin == data.getInt(action.getParameters().getParamKey(0)) &&
                RED.smin == data.getInt(action.getParameters().getParamKey(1)) &&
                RED.vmin == data.getInt(action.getParameters().getParamKey(2)) &&
                RED.hmax == data.getInt(action.getParameters().getParamKey(3)) &&
                RED.smax == data.getInt(action.getParameters().getParamKey(4)) &&
                RED.vmax == data.getInt(action.getParameters().getParamKey(5))) {
            return RED;
        } else if (GREEN.hmin == data.getInt(action.getParameters().getParamKey(0)) &&
                GREEN.smin == data.getInt(action.getParameters().getParamKey(1)) &&
                GREEN.vmin == data.getInt(action.getParameters().getParamKey(2)) &&
                GREEN.hmax == data.getInt(action.getParameters().getParamKey(3)) &&
                GREEN.smax == data.getInt(action.getParameters().getParamKey(4)) &&
                GREEN.vmax == data.getInt(action.getParameters().getParamKey(5))) {
            return GREEN;
        } else if (BLUE.hmin == data.getInt(action.getParameters().getParamKey(0)) &&
                BLUE.smin == data.getInt(action.getParameters().getParamKey(1)) &&
                BLUE.vmin == data.getInt(action.getParameters().getParamKey(2)) &&
                BLUE.hmax == data.getInt(action.getParameters().getParamKey(3)) &&
                BLUE.smax == data.getInt(action.getParameters().getParamKey(4)) &&
                BLUE.vmax == data.getInt(action.getParameters().getParamKey(5))) {
            return BLUE;
        } else if (CUSTOM.hmin == data.getInt(action.getParameters().getParamKey(0)) &&
                CUSTOM.smin == data.getInt(action.getParameters().getParamKey(1)) &&
                CUSTOM.vmin == data.getInt(action.getParameters().getParamKey(2)) &&
                CUSTOM.hmax == data.getInt(action.getParameters().getParamKey(3)) &&
                CUSTOM.smax == data.getInt(action.getParameters().getParamKey(4)) &&
                CUSTOM.vmax == data.getInt(action.getParameters().getParamKey(5))) {
            return CUSTOM;
        }
        return null;
    }


    private void onFaceDetected(Bundle data) {
        float x = data.getFloat(FACE_DETECTED.getParameters().getParamKey(0));
        float y = data.getFloat(FACE_DETECTED.getParameters().getParamKey(1));
        float eyeDistance = data.getFloat(FACE_DETECTED.getParameters().getParamKey(2));
        PointF faceCoord = new PointF(x, y);
        for (IVisualPerceptionListener listener: listeners)
            listener.onFaceDetected(faceCoord, eyeDistance);
    }


    private void onFaceAppear(Bundle data) {
        float x = data.getFloat(FACE_APPEAR.getParameters().getParamKey(0));
        float y = data.getFloat(FACE_APPEAR.getParameters().getParamKey(1));
        float eyeDistance = data.getFloat(FACE_APPEAR.getParameters().getParamKey(2));
        PointF faceCoord = new PointF(x, y);
        for (IVisualPerceptionListener listener: listeners)
            listener.onFaceAppear(faceCoord, eyeDistance);
    }


    private void onFaceDissapear() {
        for (IVisualPerceptionListener listener: listeners)
            listener.onFaceDisappear();
    }


    private void onBrightness(Bundle data) {
        float value = data.getFloat(ON_BRIGHTNESS.getParameters().getParamKey(0));
        for (IVisualPerceptionListener listener: listeners)
            listener.onBrightness(value);
    }


    private void onBrightnessChanged() {
        for (IVisualPerceptionListener listener: listeners)
            listener.onBrightnessChanged();
    }


}
