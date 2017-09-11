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

import android.util.Log;

import com.mytechia.robobo.framework.hri.touch.TouchGestureDirection;
import com.mytechia.simpleapi.component.ITouchPerceptionListener;
import com.mytechia.simpleapi.component.TouchPerceptionComponent;



public class TouchTest implements ITouchPerceptionListener{

    private static final String LOG_TAG = "TouchTest";
    private TouchPerceptionComponent touchComp;


    public TouchTest(TouchPerceptionComponent touchComp) {
        this.touchComp = touchComp;
        this.touchComp.addListener(this);
    }


    @Override
    public void tap(int x, int y) {
        Log.d(LOG_TAG, "tap()");
    }


    @Override
    public void touch(int x, int y) {
        Log.d(LOG_TAG, "touch()");
    }


    @Override
    public void fling(TouchGestureDirection dir, double angle, long time, double distance) {
        Log.d(LOG_TAG, "fling()");
    }


    @Override
    public void caress(TouchGestureDirection dir) {
        Log.d(LOG_TAG, "caress()");
    }


}
