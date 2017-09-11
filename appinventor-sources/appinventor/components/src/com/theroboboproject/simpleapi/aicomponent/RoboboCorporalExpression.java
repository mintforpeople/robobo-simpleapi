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
import com.mytechia.robobo.rob.IRSensorStatus;
import com.mytechia.robobo.rob.MoveMTMode;
import com.mytechia.simpleapi.component.*;

import java.util.Collection;


@DesignerComponent(version = 0,
        description = "Robobo Corporal Expression",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = "images/extension.png")
@SimpleObject(external = true)
@UsesLibraries(libraries = "robobo-rob-interface.jar," +
        "robobo-simple-api-client-module.jar," +
        "robobo-simple-api-module.jar")
public class RoboboCorporalExpression extends RoboboComponent
        implements ICorporalExpressionListener {

    private static final String LOG_TAG = "AICorporalComponent";

    private CorporalExpressionComponent corporalComponent;
    private float[] irDistance;


    public RoboboCorporalExpression(ComponentContainer container) {
        super(container.$form(), CorporalExpressionComponent.class, LOG_TAG);
        this.irDistance = new float[8];
    }


    @SimpleFunction
    @Override
    public void startup(String robName) {
        super.startup(robName);
    }


    @SimpleFunction
    @Override
    public void shutdown() {
        if (corporalComponent != null)
            corporalComponent.unbind();
        super.shutdown();
    }


    @Override
    public void onComponentsStarted() {
        corporalComponent = (CorporalExpressionComponent)
                            getComponentInstance();
        corporalComponent.addListener(this);
        super.onComponentsStarted();
    }


    @SimpleEvent
    @Override
    public void ComponentsStarted() {

    }


    @Override
    public void onComponentsError(String errorMsg) {
        if (corporalComponent != null) {
            corporalComponent.removeListener(this);
            corporalComponent = null;
        }
        super.onComponentsError(errorMsg);
    }


    @SimpleEvent
    @Override
    public void ComponentsError(String errorMsg) {

    }


    @SimpleFunction
    public void setLEDColor(int led, int color) {
        String functionName = "setLEDColor";
        try {
            if (corporalComponent != null)
                corporalComponent.setLEDColor(led, color);
        } catch (RemoteException e) {
            form.dispatchErrorOccurredEvent(this, functionName,
                    ErrorMessages.ERROR_SIMPLE_API_SERVICE_NOT_AVALIABLE);
        }
    }


    @SimpleFunction
    public void moveMTDegrees(int angVelR, int angleR, int angVelL, int
            angleL) {
        String functionName = "moveMTDegrees";
        try {
            if (corporalComponent != null) {
                corporalComponent.moveMTDegrees(angVelR, angleR, angVelL,
                        angleL);
            }
        } catch (RemoteException e) {
            form.dispatchErrorOccurredEvent(this, functionName,
                    ErrorMessages.ERROR_SIMPLE_API_SERVICE_NOT_AVALIABLE);
        }
    }


    @SimpleFunction
    public void moveMTTime(int angVelR, int angVelL, long time) {
        String functionName = "moveMTTime";
        try {
            if (corporalComponent != null) {
                corporalComponent.moveMTTime(angVelR, angVelL, time);
            }
        } catch (RemoteException e) {
            form.dispatchErrorOccurredEvent(this, functionName,
                    ErrorMessages.ERROR_SIMPLE_API_SERVICE_NOT_AVALIABLE);
        }
    }


    @SimpleFunction
    public void movePan(int angVel, int angle) {
        String functionName = "movePan";
        try {
            if (corporalComponent != null)
                corporalComponent.movePan(angVel, angle);
        } catch (RemoteException e) {
            form.dispatchErrorOccurredEvent(this, functionName,
                    ErrorMessages.ERROR_SIMPLE_API_SERVICE_NOT_AVALIABLE);
        }
    }


    @SimpleFunction
    public void moveTilt(int angVel, int angle) {
        String functionName = "moveTilt";
        try {
            if (corporalComponent != null)
                corporalComponent.moveTilt(angVel, angle);
        } catch (RemoteException e) {
            form.dispatchErrorOccurredEvent(this, functionName,
                    ErrorMessages.ERROR_SIMPLE_API_SERVICE_NOT_AVALIABLE);
        }
    }


    @SimpleFunction
    public void resetPanTilt() {
        String functionName = "resetPanTilt";
        try {
            if (corporalComponent != null)
                corporalComponent.resetPanTilt();
        } catch (RemoteException e) {
            form.dispatchErrorOccurredEvent(this, functionName,
                    ErrorMessages.ERROR_SIMPLE_API_SERVICE_NOT_AVALIABLE);
        }
    }


    @SimpleFunction
    public void setRobStatusPeriod(int period) {
        String functionName = "setRobStatusPeriod";
        try {
            if (corporalComponent != null)
                corporalComponent.setRobStatusPeriod(period);
        } catch (RemoteException e) {
            form.dispatchErrorOccurredEvent(this, functionName,
                    ErrorMessages.ERROR_SIMPLE_API_SERVICE_NOT_AVALIABLE);
        }
    }


    @SimpleFunction
    public float getDistance(int sensor) {
        return irDistance[sensor - 1];
    }


    @SimpleEvent
    @Override
    public void internalError(String errorMsg) {
        String functionName = "internalError";
        EventDispatcher.dispatchEvent(this, "internalError", errorMsg);
    }


    @SimpleEvent
    public void statusUpdated() {
        Log.d(LOG_TAG, "statusUpdated()");
        EventDispatcher.dispatchEvent(this, "statusUpdated");
    }


    @Override
    public void statusIRs(Collection<IRSensorStatus> coll) {
        IRSensorStatus[] irStatus;
        irStatus = coll.toArray(new IRSensorStatus[coll.size()]);
        irDistance = new float[coll.size()];
        for (int i = 0; i < irDistance.length; i++)
            irDistance[i] = irStatus[i].getDistance();
        statusUpdated();
    }


}
