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
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.mytechia.simpleapi.ServiceCommand;
import com.mytechia.simpleapi.component.ComponentNotFoundException;
import com.mytechia.simpleapi.component.DummyComponent;
import com.mytechia.simpleapi.component.ISimpleRoboboManagerListener;
import com.mytechia.simpleapi.component.SimpleRoboboManager;

import static com.mytechia.simpleapi.ServiceCommand.DUMMY_EXPRESSION;


@DesignerComponent(version = 0,
        description = "Robobo Dummy",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = "images/extension.png")
@SimpleObject(external = true)
@UsesLibraries(libraries = "robobo-simple-api-module.jar," +
        "robobo-simple-api-client-module.jar")
public class RoboboDummy extends AndroidNonvisibleComponent
        implements ISimpleRoboboManagerListener {

    private final String LOG_TAG = "AIDummyComponent";
    private final Class component = DummyComponent.class;

    private Context context;
    private SimpleRoboboManager simpleRoboboManager;
    private DummyComponent dummyComponent;

    public RoboboDummy(ComponentContainer container) {
        super(container.$form());
        this.context = container.$context();
        this.simpleRoboboManager = SimpleRoboboManager.getInstance();
        this.simpleRoboboManager.addListener(this);
    }


    @SimpleFunction
    public void startup(String robName) {
        String functionName = "startup";
        try {
            simpleRoboboManager.addComponent(context, component);
        } catch (ComponentNotFoundException e) {
            form.dispatchErrorOccurredEvent(this, functionName,
                    ErrorMessages.ERROR_ROBOBO_COMPONENT_NOT_FOUND,
                    e.getMessage());
        }
        simpleRoboboManager.bind(robName);
    }


    @SimpleFunction
    public void shutdown() {
        simpleRoboboManager.unbind();
    }


    @Override
    public void onComponentsStarted() {
        Log.i(LOG_TAG, "onComponentsStarted()");
        dummyComponent = (DummyComponent) simpleRoboboManager
                                        .getComponentInstance(component);
    }


    @Override
    public void onComponentsError(String errorMsg) {
        Log.e(LOG_TAG, "onComponentsStarted()");
        dummyComponent = null;
    }


}
