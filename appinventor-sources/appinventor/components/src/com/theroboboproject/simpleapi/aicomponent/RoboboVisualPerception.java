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

package com.theroboboproject.simpleapi.aicomponent;

import android.graphics.PointF;
import android.os.RemoteException;
import android.util.Log;
import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.mytechia.robobo.framework.hri.vision.blobTracking.Blob;
import com.mytechia.robobo.framework.hri.vision.blobTracking.Blobcolor;
import com.mytechia.simpleapi.component.IVisualPerceptionListener;
import com.mytechia.simpleapi.component.VisualPerceptionComponent;


@DesignerComponent(version = 0,
        description = "Robobo Visual Perception",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = "images/extension.png")
@SimpleObject
@UsesLibraries(libraries = "robobo-simple-api-client-module.jar," +
        "robobo-simple-api-module.jar," + "robobo-vision-SAMSUNG.jar," +
        "opencv.jar")
public class RoboboVisualPerception extends RoboboComponent
        implements IVisualPerceptionListener {

    private static final String LOG_TAG = "AIVisualPerception";
    private VisualPerceptionComponent visualComponent;


    public RoboboVisualPerception(ComponentContainer container) {
        super(container, VisualPerceptionComponent.class, LOG_TAG);
    }


    @SimpleFunction
    public void startup(String robName) {
        super.startup(robName);
    }


    @SimpleFunction
    public void shutdown() {
        if (visualComponent != null)
            visualComponent.unbind();
        super.shutdown();
    }


    @Override
    public void onComponentsStarted() {
        visualComponent = (VisualPerceptionComponent) getComponentInstance();
        visualComponent.addListener(this);
        super.onComponentsStarted();
        //EventDispatcher.dispatchEvent(this, "onComponentsStarted");
    }


    @SimpleEvent
    @Override
    public void ComponentsStarted() {

    }


    @Override
    public void onComponentsError(String errorMsg) {
        if (visualComponent != null) {
            visualComponent.removeListener(this);
            visualComponent = null;
        }
        super.onComponentsError(errorMsg);
        //EventDispatcher.dispatchEvent(this, "onComponentsError", errorMsg);
    }


    @SimpleEvent
    @Override
    public void ComponentsError(String errorMsg) {

    }


    @SimpleFunction
    public void startBlobTracking() {
        String functionName = "startBlobTracking";
        try {
            visualComponent.startBlobTracking();
        } catch (RemoteException e) {
            form.dispatchErrorOccurredEvent(this, functionName,
                    ErrorMessages.ERROR_SIMPLE_API_SERVICE_NOT_AVALIABLE);
        }
    }


    @SimpleFunction
    public void stopBlobTracking() {
        String functionName = "stopBlobTracking";
        try {
            visualComponent.stopBlobTracking();
        } catch (RemoteException e) {
            form.dispatchErrorOccurredEvent(this, functionName,
                    ErrorMessages.ERROR_SIMPLE_API_SERVICE_NOT_AVALIABLE);
        }
    }


    @SimpleFunction
    public void blobTrackingConfig(boolean detectRed, boolean detectGreen,
                                   boolean detectBlue, boolean detectCustom) {
        String functionName = "blobTrackingConfig";
        try {
            visualComponent.blobTrackingConfig(detectRed, detectGreen,
                                                detectBlue, detectCustom);
        } catch (RemoteException e) {
            form.dispatchErrorOccurredEvent(this, functionName,
                    ErrorMessages.ERROR_SIMPLE_API_SERVICE_NOT_AVALIABLE);
        }
    }


    @SimpleFunction
    public void blobTrackingThreshold(int threshold) {
        String functionName = "blobTrackingThreshold";
        try {
            visualComponent.blobTrackingThreshold(threshold);
        } catch (RemoteException e) {
            form.dispatchErrorOccurredEvent(this, functionName,
                    ErrorMessages.ERROR_SIMPLE_API_SERVICE_NOT_AVALIABLE);
        }
    }


    @SimpleFunction
    public void startFaceDetection() {
        String functionName = "startFaceDetection";
        try {
            visualComponent.startFaceDetection();
        } catch (RemoteException e) {
            form.dispatchErrorOccurredEvent(this, functionName,
                    ErrorMessages.ERROR_SIMPLE_API_SERVICE_NOT_AVALIABLE);
        }
    }


    @SimpleFunction
    public void stopFaceDetection() {
        String functionName = "stopFaceDetection";
        try {
            visualComponent.stopFaceDetection();
        } catch (RemoteException e) {
            form.dispatchErrorOccurredEvent(this, functionName,
                    ErrorMessages.ERROR_SIMPLE_API_SERVICE_NOT_AVALIABLE);
        }
    }


    @SimpleProperty
    public void setHasChangedAmount(int amount) {
        String functionName = "setHasChangedAmount";
        try {
            visualComponent.setHasChangedAmount(amount);
        } catch (RemoteException e) {
            form.dispatchErrorOccurredEvent(this, functionName,
                    ErrorMessages.ERROR_SIMPLE_API_SERVICE_NOT_AVALIABLE);
        }
    }


    @SimpleEvent
    public void trackingBlob(int color, boolean isBall, boolean isSquare,
                             int size, int x, int y) {
        EventDispatcher.dispatchEvent(this, "trackingBlob",
                                        color, isBall, isSquare, size, x ,y);
    }


    @Override
    public void onTrackingBlob(Blob blob) {
        Log.d(LOG_TAG, "trackingBlob()");
        Blob lastTrackedBlob = blob;
        trackingBlob(blob.getColor().hashCode(), blob.isBall(), blob.isSquare(),
                    blob.getSize(), blob.getX(), blob.getY());
    }


    @SimpleEvent
    public void blobDisappear(int color) {
        EventDispatcher.dispatchEvent(this, "blobDisappear", color);
    }


    @Override
    public void onBlobDisappear(Blobcolor color) {
        Log.d(LOG_TAG, "blobDisappear()");
        blobDisappear(color.hashCode());
    }


    @SimpleEvent
    public void faceDetected(float x, float y, float eyeDistance) {
        EventDispatcher.dispatchEvent(this, "faceDetected",
                                    x, y, eyeDistance);
    }


    @Override
    public void onFaceDetected(PointF pointF, float eyeDistance) {
        Log.d(LOG_TAG, "faceDetected()");
        faceDetected(pointF.x, pointF.y, eyeDistance);
    }


    @SimpleEvent
    public void faceAppear(float x, float y, float eyeDistance) {
        EventDispatcher.dispatchEvent(this, "faceAppear",
                                    x, y, eyeDistance);
    }


    @Override
    public void onFaceAppear(PointF pointF, float eyeDistance) {
        Log.d(LOG_TAG, "faceAppear()");
        faceAppear(pointF.x, pointF.y, eyeDistance);
    }


    @SimpleEvent
    @Override
    public void onFaceDisappear() {
        Log.d(LOG_TAG, "faceDisappear()");
        EventDispatcher.dispatchEvent(this, "onFaceDisappear");
    }


    @SimpleEvent
    @Override
    public void onBrightness(float value) {
        Log.d(LOG_TAG, "onBrightness(): " + Float.toString(value));
        EventDispatcher.dispatchEvent(this, "onBrightness", value);
    }


    @SimpleEvent
    @Override
    public void onBrightnessChanged() {
        Log.d(LOG_TAG, "onBrightnessChanged()");
        EventDispatcher.dispatchEvent(this, "onBrightnessChanged");
    }


}
