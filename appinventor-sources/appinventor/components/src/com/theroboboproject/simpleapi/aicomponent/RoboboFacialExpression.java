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

import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;
import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.mytechia.robobo.framework.hri.emotion.Emotion;
import com.mytechia.simpleapi.component.FacialExpressionComponent;
import com.mytechia.simpleapi.component.IFacialExpressionListener;
import com.mytechia.simpleapi.util.ComActUtil;


@DesignerComponent(version = 0,
        description = "Robobo Facial Expression",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = "images/extension.png")
@SimpleObject(external = true)
@UsesLibraries(libraries = "robobo-hri-emotion-module.jar," +
        "robobo-simple-api-client-module.jar," +
        "robobo-simple-api-module.jar")
public class RoboboFacialExpression extends RoboboComponent
        implements IFacialExpressionListener{

    private static final String LOG_TAG = "AIFacialExpression";
    private FacialExpressionComponent facialComponent;
    private final SparseArray<Enum> emotionValues;


    public RoboboFacialExpression(ComponentContainer container) {
        super(container, FacialExpressionComponent.class, LOG_TAG);
        this.emotionValues = ComActUtil.enum2HashCode(Emotion.values());
    }


    @SimpleFunction
    public void startup(String robName) {
        super.startup(robName);
    }


    @SimpleFunction
    public void shutdown() {
        if (facialComponent != null)
            facialComponent.unbind();
        super.shutdown();
    }



    @Override
    public void onComponentsStarted() {
        facialComponent = (FacialExpressionComponent) getComponentInstance();
        facialComponent.addListener(this);
        super.onComponentsStarted();
    }


    @SimpleEvent
    @Override
    public void ComponentsStarted() {

    }



    @Override
    public void onComponentsError(String errorMsg) {
        if (facialComponent != null) {
            facialComponent.removeListener(this);
            facialComponent = null;
        }
        super.onComponentsError(errorMsg);
    }


    @SimpleEvent
    @Override
    public void ComponentsError(String errorMsg) {

    }


    @SimpleFunction
    public void setCurrentEmotion(int emotion) {
        String functionName = "setCurrentEmotion";

        Emotion em = (Emotion) emotionValues.get(emotion);
        if (em == null)
            Log.e(LOG_TAG, "emotion = null");

        try {
            facialComponent.setCurrentEmotion((Emotion) emotionValues
                                                        .get(emotion));
        } catch (RemoteException e) {
            form.dispatchErrorOccurredEvent(this, functionName,
                    ErrorMessages.ERROR_SIMPLE_API_SERVICE_NOT_AVALIABLE);
        }
    }


    @SimpleFunction
    public void setTemporalEmotion(int emotion, long duration, int nextEmotion){
        String functionName = "setTemporalEmotion";
        Emotion e = (Emotion) emotionValues.get(emotion);
        Emotion next = (Emotion) emotionValues.get(nextEmotion);
        try {
            facialComponent.setTemporalEmotion(e, duration, next);
        } catch (RemoteException e1) {
            form.dispatchErrorOccurredEvent(this, functionName,
                    ErrorMessages.ERROR_SIMPLE_API_SERVICE_NOT_AVALIABLE);
        }
    }


    @SimpleProperty
    public int getCurrentEmotion() {
        return facialComponent.getCurrentEmotion().hashCode();
    }


    @SimpleEvent
    public void EmotionChange(int emotion) {
        EventDispatcher.dispatchEvent(this, "EmotionChange", emotion);
    }


    @Override
    public void emotionChange(Emotion emotion) {
        Log.d(LOG_TAG, "emotionChange()");
        EmotionChange(emotion.hashCode());
    }


}
