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

import android.content.Context;
import android.graphics.PointF;
import android.os.RemoteException;
import android.util.Log;

import com.mytechia.robobo.framework.hri.emotion.Emotion;
import com.mytechia.robobo.framework.hri.touch.TouchGestureDirection;
import com.mytechia.robobo.framework.hri.vision.blobTracking.Blob;
import com.mytechia.robobo.framework.hri.vision.blobTracking.Blobcolor;
import com.mytechia.simpleapi.component.ComponentNotFoundException;
import com.mytechia.simpleapi.component.FacialExpressionComponent;
import com.mytechia.simpleapi.component.IFacialExpressionListener;
import com.mytechia.simpleapi.component.ISimpleRoboboManagerListener;
import com.mytechia.simpleapi.component.ITouchPerceptionListener;
import com.mytechia.simpleapi.component.IVisualPerceptionListener;
import com.mytechia.simpleapi.component.SimpleRoboboManager;
import com.mytechia.simpleapi.component.TouchPerceptionComponent;
import com.mytechia.simpleapi.component.VisualPerceptionComponent;

import static com.mytechia.robobo.framework.hri.emotion.Emotion.ANGRY;
import static com.mytechia.robobo.framework.hri.emotion.Emotion.IN_LOVE;
import static com.mytechia.robobo.framework.hri.emotion.Emotion.NORMAL;
import static com.mytechia.robobo.framework.hri.vision.blobTracking.Blobcolor.RED;



public class Iteration2Test extends Iteration1Test implements ISimpleRoboboManagerListener,
        IVisualPerceptionListener, IFacialExpressionListener, ITouchPerceptionListener {


    private static final String LOG_TAG = "It2Test";
    private Context context;

    private SimpleRoboboManager simpleRoboboManager;
    private VisualPerceptionComponent visual;
    private FacialExpressionComponent facial;
    private TouchPerceptionComponent touch;


    public Iteration2Test(Context context, SimpleRoboboManager simpleRoboboManager) {
        super(context, simpleRoboboManager);
        this.context = context;
        this.simpleRoboboManager = simpleRoboboManager;
    }


    @Override
    public void start(String robName) throws ComponentNotFoundException {
        Log.d(LOG_TAG, "Starting It_2");
        simpleRoboboManager.addListener(this);
        simpleRoboboManager.addComponent(context, VisualPerceptionComponent.class);
        simpleRoboboManager.addComponent(context, FacialExpressionComponent.class);
        simpleRoboboManager.addComponent(context, TouchPerceptionComponent.class);
        super.start(robName);
        visual = (VisualPerceptionComponent) simpleRoboboManager
                .getComponentInstance(VisualPerceptionComponent.class);
        visual.addListener(this);
        facial = (FacialExpressionComponent) simpleRoboboManager
                .getComponentInstance(FacialExpressionComponent.class);
        facial.addListener(this);
        touch = (TouchPerceptionComponent) simpleRoboboManager
                .getComponentInstance(TouchPerceptionComponent.class);
        touch.addListener(this);
    }


    @Override
    public void stop() {
        Log.d(LOG_TAG, "Stopping It_2");
        if (touch != null)
            touch.removeListener(this);
        touch = null;
        if (facial != null)
            facial.removeListener(this);
        facial = null;
        if (visual != null)
            visual.removeListener(this);
        visual = null;
        simpleRoboboManager.removeComponent(TouchPerceptionComponent.class);
        simpleRoboboManager.removeComponent(FacialExpressionComponent.class);
        simpleRoboboManager.removeComponent(VisualPerceptionComponent.class);
        simpleRoboboManager.removeListener(this);
        super.stop();
    }


    @Override
    public void onComponentsStarted() {
        try {
            visual.startBlobTracking();
        } catch (RemoteException e) {
            Log.d(LOG_TAG, "Error starting blob tracking: " + e.getMessage() + "\n\nStopping test");
            stop();
        }
    }


    @Override
    public void tap(int x, int y) {
        if (started()) {
            stopMovement();
        } else {
            try {
                startMovement();
            } catch (RemoteException e) {
                Log.d(LOG_TAG, "Error starting movement: " + e.getMessage() + "\n\nStopping test");
                stop();
            }
        }
    }


    @Override
    public void touch(int x, int y) {

    }


    @Override
    public void fling(TouchGestureDirection dir, double angle, long time, double distance) {

    }


    @Override
    public void caress(TouchGestureDirection dir) {

    }


    @Override
    public void emotionChange(Emotion emotion) {

    }


    @Override
    public void onTrackingBlob(Blob blob) {
        Log.d(LOG_TAG, "onTrackingBlob()");
        if (blob.getColor() == RED)
            try {
                facial.setCurrentEmotion(IN_LOVE);
            } catch (RemoteException e) {
                Log.e(LOG_TAG, e.getMessage());
            }
    }


    @Override
    public void onBlobDisappear(Blobcolor c) {
        if (c == RED)
            try {
                facial.setTemporalEmotion(ANGRY, (long) 3000, NORMAL);
            } catch (RemoteException e) {
                Log.e(LOG_TAG, e.getMessage());
            }
    }


    @Override
    public void onFaceDetected(PointF faceCoords, float eyeDistance) {

    }


    @Override
    public void onFaceAppear(PointF faceCoords, float eyeDistance) {

    }


    @Override
    public void onFaceDisappear() {

    }


    @Override
    public void onBrightness(float value) {

    }


    @Override
    public void onBrightnessChanged() {

    }


}
