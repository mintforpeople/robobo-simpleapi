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

package com.mytechia.simpleapi.test;

import android.graphics.PointF;
import android.os.RemoteException;
import android.util.Log;

import com.mytechia.robobo.framework.hri.vision.blobTracking.Blob;
import com.mytechia.robobo.framework.hri.vision.blobTracking.Blobcolor;
import com.mytechia.simpleapi.component.IVisualPerceptionListener;
import com.mytechia.simpleapi.component.VisualPerceptionComponent;



public class VisualTest implements IVisualPerceptionListener {

    private static final String LOG_TAG = "VisualTest";
    private VisualPerceptionComponent visualComp;


    public VisualTest(VisualPerceptionComponent visualComp) {
        this.visualComp = visualComp;
    }


    public void faceDetectionStart() {
        visualComp.addListener(this);
        try {
            visualComp.startFaceDetection();
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Error starting face detection: " + e.getMessage());
        }
    }


    public void faceDetectionStop() {
        visualComp.removeListener(this);
        try {
            visualComp.stopFaceDetection();
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Error stopping face detection: " + e.getMessage());
        }
    }


    public void blobTrackingStart() {
        visualComp.addListener(this);
        try {
            visualComp.startBlobTracking();
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Error starting blob tracking: " + e.getMessage());
        }
    }


    public void blobTrackingStop() {
        visualComp.removeListener(this);
        try {
            visualComp.stopBlobTracking();
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Error stopping blob tracking: " + e.getMessage());
        }
    }


    public void setRefreshRate(int rate) {
        try {
            visualComp.setRefreshRate(rate);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Error setRefreshRate: " + e.getMessage());
        }
    }


    public void setHasChangedAmount(int amount) {
        try {
            visualComp.setHasChangedAmount(amount);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Error setHasChangedAmount: " + e.getMessage());
        }
    }


    @Override
    public void onTrackingBlob(Blob blob) {
        Log.d(LOG_TAG, "trackingBlob()");
    }


    @Override
    public void onBlobDisappear(Blobcolor color) {
        Log.d(LOG_TAG, "blobDisappear()");
    }


    @Override
    public void onFaceDetected(PointF faceCoords, float eyeDistance) {
        Log.d(LOG_TAG, "faceDetected: (" + Float.toString(faceCoords.x) + ", " +
                        Float.toString(faceCoords.y) + ") -> " + Float.toString(eyeDistance));
    }


    @Override
    public void onFaceAppear(PointF faceCoords, float eyeDistance) {
        Log.d(LOG_TAG, "faceAppear: (" + Float.toString(faceCoords.x) + ", " +
                Float.toString(faceCoords.y) + ") -> " + Float.toString(eyeDistance));
    }


    @Override
    public void onFaceDisappear() {
        Log.d(LOG_TAG, "faceDissapear");
    }


    @Override
    public void onBrightness(float value) {
        Log.d(LOG_TAG, "onBrightness(): " + Float.toString(value));
    }


    @Override
    public void onBrightnessChanged() {
        Log.d(LOG_TAG, "onBrightnessChanged():");
    }


}
