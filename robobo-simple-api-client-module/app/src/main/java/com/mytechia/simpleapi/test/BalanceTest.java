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

import com.mytechia.simpleapi.component.BalancePerceptionComponent;
import com.mytechia.simpleapi.component.IBalancePerceptionListener;



public class BalanceTest implements IBalancePerceptionListener {

    private static final String LOG_TAG = "BalanceTest";
    private BalancePerceptionComponent balancePerceptionComponent;


    public BalanceTest(BalancePerceptionComponent balancePerceptionComponent) {
        this.balancePerceptionComponent = balancePerceptionComponent;
        this.balancePerceptionComponent.addListener(this);
    }


    public void setDetectionThreshold(int threshold) throws RemoteException {
        balancePerceptionComponent.setDetectionThreshold(threshold);
    }


    @Override
    public void onAccelerationChange() {
        Log.d(LOG_TAG, "onAccelerationChange()");
    }


    @Override
    public void onAcceleration(int xaccel, int yaccel, int zaccel) {
        Log.d(LOG_TAG, "onAcceleration(): (" + Integer.toString(xaccel) + ", " +
                Integer.toString(yaccel) + ", " + Integer.toString(zaccel) + ")");
    }


    @Override
    public void onOrientationChanged(float yaw, float pitch, float roll) {
        Log.d(LOG_TAG, "onOrientationChanged(): (" + Float.toString(yaw) + ", " +
                Float.toString(pitch) + ", " + Float.toString(roll) + ")");
    }


}
