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

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;

import static com.mytechia.robobo.framework.hri.vision.blobTracking.Blobcolor.*;


@DesignerComponent(version = 0,
        description = "Robobo Visual Perception",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = "images/extension.png")
@SimpleObject(external = true)
@UsesLibraries(libraries = "robobo-vision-SAMSUNG.jar")
public class RoboboBlobcolor extends AndroidNonvisibleComponent {


    public RoboboBlobcolor(ComponentContainer container) {
        super(container.$form());
    }


    @SimpleProperty
    public int Red() {
        return RED.hashCode();
    }


    @SimpleProperty
    public int Green() {
        return GREEN.hashCode();
    }


    @SimpleProperty
    public int Blue() {
        return BLUE.hashCode();
    }


    @SimpleProperty
    public int Custom() {
        return CUSTOM.hashCode();
    }


}
