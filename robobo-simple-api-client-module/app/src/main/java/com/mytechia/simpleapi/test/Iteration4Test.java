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
import android.os.RemoteException;
import android.util.Log;

import com.mytechia.simpleapi.component.BalancePerceptionComponent;
import com.mytechia.simpleapi.component.ComponentNotFoundException;
import com.mytechia.simpleapi.component.IBalancePerceptionListener;
import com.mytechia.simpleapi.component.ISimpleRoboboManagerListener;
import com.mytechia.simpleapi.component.SimpleRoboboManager;



public class Iteration4Test extends Iteration3Test implements ISimpleRoboboManagerListener,
        IBalancePerceptionListener {

    private static final String LOG_TAG = "It4Test";
    private Context context;

    private SimpleRoboboManager simpleRoboboManager;
    private BalancePerceptionComponent balancePerception;


    public Iteration4Test(Context context, SimpleRoboboManager simpleRoboboManager) {
        super(context, simpleRoboboManager);
        this.context = context;
        this.simpleRoboboManager = simpleRoboboManager;
    }

    @Override
    public void start(String robName) throws ComponentNotFoundException {
        Log.d(LOG_TAG, "Starting It_4");
        simpleRoboboManager.addListener(this);
        simpleRoboboManager.addComponent(context, BalancePerceptionComponent.class);
        super.start(robName);
        balancePerception = (BalancePerceptionComponent) simpleRoboboManager
                .getComponentInstance(BalancePerceptionComponent.class);
        balancePerception.addListener(this);
    }


    @Override
    public void stop() {
        Log.d(LOG_TAG, "Stopping It_1");
        balancePerception.removeListener(this);
        balancePerception = null;
        super.stop();
    }


    @Override
    public void onComponentsStarted() {
        try {
            balancePerception.setDetectionThreshold(1);
            super.onComponentsStarted();
        } catch (RemoteException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }


    @Override
    public void onAccelerationChange() {
        complain();
    }


    @Override
    public void onAcceleration(int xaccel, int yaccel, int zaccel) {

    }


    @Override
    public void onOrientationChanged(float yaw, float pitch, float roll) {
        complain();
    }


    private void complain() {
        try {
            sayText("Eh! Para!");
        } catch (RemoteException e) {
            Log.d(LOG_TAG, "Error complaining: " + e.getMessage() + "\n\nStopping test");
            stop();
        }
    }


}
