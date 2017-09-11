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

import android.content.Context;
import android.util.Log;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.mytechia.simpleapi.ServiceCommand;
import com.mytechia.simpleapi.component.*;


public abstract class RoboboComponent extends AndroidNonvisibleComponent
        implements ISimpleRoboboManagerListener {

    private final String LOG_TAG;
    private final Class component;
    private Context context;
    private SimpleRoboboManager simpleRoboboManager;


    public RoboboComponent(ComponentContainer container,
                           Class component, String logTag) {
        super(container.$form());
        this.LOG_TAG = logTag;
        this.component = component;
        this.context = container.$context();
        this.simpleRoboboManager = SimpleRoboboManager.getInstance();
        this.addComponent();
        this.simpleRoboboManager.addListener(this);
    }


    public void startup(String robName) {
        String functionName = "startup";
        simpleRoboboManager.addListener(this);
        simpleRoboboManager.bind(robName);
    }


    public void shutdown() {
        simpleRoboboManager.removeListener(this);
        simpleRoboboManager.removeComponent(component);
    }


    protected ASimpleAPIComponent getComponentInstance() {
        return simpleRoboboManager.getComponentInstance(component);
    }


    @Override
    public void onComponentsStarted() {
        Log.d(LOG_TAG, "Robobo started");
        EventDispatcher.dispatchEvent(this, "ComponentsStarted");
    }


    public abstract void ComponentsStarted();


    @Override
    public void onComponentsError(String errorMsg) {
        Log.e(LOG_TAG, "Robobo error: " + errorMsg);
        EventDispatcher.dispatchEvent(this, "ComponentsError", errorMsg);
    }


    public abstract void ComponentsError(String errorMsg);


    private void addComponent() {
        String functionName = "addComponent";
        try {
            simpleRoboboManager.addComponent(context, component);
        } catch (ComponentNotFoundException e) {
            form.dispatchErrorOccurredEvent(this, functionName,
                    ErrorMessages.ERROR_ROBOBO_COMPONENT_NOT_FOUND,
                    e.getMessage());
        }
    }


}
