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

import com.mytechia.robobo.framework.hri.emotion.Emotion;
import com.mytechia.simpleapi.component.FacialExpressionComponent;
import com.mytechia.simpleapi.component.IFacialExpressionListener;



public class FacialTest implements IFacialExpressionListener {

    private static final String LOG_TAG = "FacialTest";
    private FacialExpressionComponent facialComp;


    public FacialTest(FacialExpressionComponent facialComp) {
        this.facialComp = facialComp;
        this.facialComp.addListener(this);
    }


    public void setCurrentEmotion(Emotion emotion) {
        try {
            facialComp.setCurrentEmotion(emotion);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Error setting current emotion: " + e.getMessage());
        }
    }


    public void setTemporalEmotion(Emotion emotion, Long duration, Emotion nextEmotion) {
        try {
            facialComp.setTemporalEmotion(emotion, duration, nextEmotion);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Error setting temporal emotion: " + e.getMessage());
        }
    }


    public void getCurrentEmotion() {
        Log.d(LOG_TAG, "getCurrentEmotion(): " + facialComp.getCurrentEmotion().name());
    }


    @Override
    public void emotionChange(Emotion emotion) {
        Log.d(LOG_TAG, "emotionChange()");
    }


}
