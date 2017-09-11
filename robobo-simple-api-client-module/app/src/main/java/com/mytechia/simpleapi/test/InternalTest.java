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

import android.os.RemoteException;
import android.util.Log;

import com.mytechia.simpleapi.component.IInternalPerceptionListener;
import com.mytechia.simpleapi.component.InternalPerceptionComponent;



public class InternalTest implements IInternalPerceptionListener {

    private static final String LOG_TAG = "InternalTest";
    private InternalPerceptionComponent internalPerceptionComponent;

    public InternalTest(InternalPerceptionComponent internalPerceptionComponent) {
        this.internalPerceptionComponent = internalPerceptionComponent;
        this.internalPerceptionComponent.addListener(this);
    }


    public void setRefreshInterval(int millis) throws RemoteException {
        internalPerceptionComponent.setRefreshInterval(millis);
    }


    @Override
    public void onNewOboBatteryStatus(int battlevel) {
        Log.d(LOG_TAG, "onNewOboBatteryStatus(): " + Integer.toString(battlevel));
    }


    @Override
    public void onNewRobBatteryStatus(int battery, boolean charging) {
        Log.d(LOG_TAG, "onNewRobBatteryStatus(): " + Integer.toString(battery) + ", is " +
                ((charging)? "": "not") + " charging");
    }


}
