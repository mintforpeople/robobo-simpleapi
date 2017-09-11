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
import com.mytechia.simpleapi.component.IInternalPerceptionListener;
import com.mytechia.simpleapi.component.InternalPerceptionComponent;


@DesignerComponent(version = 0,
        description = "Robobo Internal Perception",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = "images/extension.png")
@SimpleObject(external = true)
@UsesLibraries(libraries = "robobo-simple-api-client-module.jar," +
        "robobo-simple-api-module.jar," + "robobo-sensing.jar")
public class RoboboInternalPerception extends RoboboComponent
        implements IInternalPerceptionListener {

    private static final String LOG_TAG = "AIInternalComponent";
    private InternalPerceptionComponent internalComponent;


    public RoboboInternalPerception(ComponentContainer container) {
        super(container.$form(), InternalPerceptionComponent.class, LOG_TAG);
    }


    @SimpleFunction
    @Override
    public void startup(String robName) {
        super.startup(robName);
    }


    @SimpleFunction
    @Override
    public void shutdown() {
        if (internalComponent != null)
            internalComponent.unbind();
        super.shutdown();
    }


    @Override
    public void onComponentsStarted() {
        internalComponent = (InternalPerceptionComponent)getComponentInstance();
        internalComponent.addListener(this);
        super.onComponentsStarted();
    }


    @SimpleEvent
    @Override
    public void ComponentsStarted() {

    }


    @Override
    public void onComponentsError(String errorMsg) {
        if (internalComponent != null) {
            internalComponent.removeListener(this);
            internalComponent = null;
        }
        super.onComponentsError(errorMsg);
    }


    @SimpleEvent
    @Override
    public void ComponentsError(String errorMsg) {

    }


    @SimpleProperty
    public void setRefreshInterval(int millis) {
        String functionName = "setRefreshInterval";
        try {
            internalComponent.setRefreshInterval(millis);
        } catch (RemoteException e) {
            form.dispatchErrorOccurredEvent(this, functionName,
                    ErrorMessages.ERROR_SIMPLE_API_SERVICE_NOT_AVALIABLE);
        }
    }


    @SimpleEvent
    @Override
    public void onNewOboBatteryStatus(int battery) {
        Log.d(LOG_TAG, "onNewOboBatteryStatus(): " +
                Integer.toString(battery));
        EventDispatcher.dispatchEvent(this, "onNewOboBatteryStatus", battery);
    }


    @SimpleEvent
    @Override
    public void onNewRobBatteryStatus(int battlevel, boolean charging) {
        Log.d(LOG_TAG, "onNewRobBatteryStatus(): " +
                Integer.toString(battlevel) + ", is " + ((charging)? "": "not")
                + "charging");
        EventDispatcher.dispatchEvent(this, "onNewRobBatteryStatus", battlevel,
                charging);
    }


}
