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
import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.mytechia.simpleapi.component.BalancePerceptionComponent;
import com.mytechia.simpleapi.component.IBalancePerceptionListener;


@DesignerComponent(version = 0,
        description = "Robobo Balance Perception",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = "images/extension.png")
@SimpleObject(external = true)
@UsesLibraries(libraries = "robobo-simple-api-client-module.jar," +
        "robobo-simple-api-module.jar," + "robobo-sensing.jar")
public class RoboboBalancePerception extends RoboboComponent
        implements IBalancePerceptionListener{

    private static final String LOG_TAG = "AIBalanceComponent";
    private BalancePerceptionComponent balanceComponent;


    public RoboboBalancePerception(ComponentContainer container) {
        super(container.$form(), BalancePerceptionComponent.class, LOG_TAG);
    }


    @SimpleFunction
    @Override
    public void startup(String robName) {
        super.startup(robName);
    }


    @SimpleFunction
    @Override
    public void shutdown() {
        if (balanceComponent != null)
            balanceComponent.unbind();
        super.shutdown();
    }


    @Override
    public void onComponentsStarted() {
        balanceComponent = (BalancePerceptionComponent) getComponentInstance();
        balanceComponent.addListener(this);
        super.onComponentsStarted();
    }


    @SimpleEvent
    @Override
    public void ComponentsStarted() {

    }


    @Override
    public void onComponentsError(String errorMsg) {
        if (balanceComponent != null) {
            balanceComponent.removeListener(this);
            balanceComponent = null;
        }
        super.onComponentsError(errorMsg);
    }


    @SimpleEvent
    @Override
    public void ComponentsError(String errorMsg) {

    }


    @SimpleProperty
    public void setDetectionThreshold(int threshold) {
        String functionName = "setDetectionThreshold";
        try {
            balanceComponent.setDetectionThreshold(threshold);
        } catch (RemoteException e) {
            form.dispatchErrorOccurredEvent(this, functionName,
                    ErrorMessages.ERROR_SIMPLE_API_SERVICE_NOT_AVALIABLE);
        }
    }


    @SimpleEvent
    @Override
    public void onAccelerationChange() {
        Log.d(LOG_TAG, "onAccelerationChange()");
        EventDispatcher.dispatchEvent(this, "onAccelerationChange");
    }


    @SimpleEvent
    @Override
    public void onAcceleration(int x, int y, int z) {
        Log.d(LOG_TAG, "onAcceleration(): " + Integer.toString(x) + ", " +
                Integer.toString(y) + ", " + Integer.toString(z));
        EventDispatcher.dispatchEvent(this, "onAcceleration", x ,y, z);
    }


    @SimpleEvent
    @Override
    public void onOrientationChanged(float yaw, float pitch, float roll) {
        Log.d(LOG_TAG, "onOrientationChanged(): ");
        EventDispatcher.dispatchEvent(this, "onOrientationChanged", yaw, pitch, roll);
    }


}
