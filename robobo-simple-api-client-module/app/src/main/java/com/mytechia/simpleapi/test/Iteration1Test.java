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
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;

import com.mytechia.robobo.rob.IRSensorStatus;
import com.mytechia.robobo.rob.MoveMTMode;
import com.mytechia.simpleapi.component.ComponentNotFoundException;
import com.mytechia.simpleapi.component.CorporalExpressionComponent;
import com.mytechia.simpleapi.component.ICorporalExpressionListener;
import com.mytechia.simpleapi.component.ISimpleRoboboManagerListener;
import com.mytechia.simpleapi.component.SimpleRoboboManager;

import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

import static com.mytechia.robobo.rob.MoveMTMode.FORWARD_FORWARD;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.round;



public class Iteration1Test implements IIterationTest,
        ISimpleRoboboManagerListener, ICorporalExpressionListener {

    private static final String LOG_TAG = "It1Test";
    private final Context context;
    private SimpleRoboboManager simpleRoboboManager;

    private CorporalExpressionComponent corporal;
    private int statusPeriod = 100;
    private static final int S1_THRESHOLD = 94; // 50
    private static final int S2_THRESHOLD = 34; // 25
    private static final int S3_THRESHOLD = 16 - 5; // Front Center
    private static final int S4_THRESHOLD = 22 + 14; // 15
    private static final int S5_THRESHOLD = 112 - 17; // 100
    private float alpha = (float) 1.0;

    private IRSensorStatus[] irStatus;
    private float[] irDistance;

    private boolean movementStarted;
    private Runnable movementRunnable;
    private final Handler movementHandler = new Handler();
    private Timer timer;
    private static final int MAX_ANG_VEL = 150;
    private int angVelR;
    private int angVelL;
    private MoveMTMode moveMTMode;


    public Iteration1Test(Context context, SimpleRoboboManager simpleRoboboManager) {
        this.context = context;
        this.simpleRoboboManager = simpleRoboboManager;

        this.angVelR = MAX_ANG_VEL;
        this.angVelL = MAX_ANG_VEL;
        this.moveMTMode = FORWARD_FORWARD;
        this.timer = new Timer();
    }


    public void start(String robName) throws ComponentNotFoundException {
        Log.d(LOG_TAG, "Starting It_1");
        simpleRoboboManager.addListener(this);
        simpleRoboboManager.addComponent(context, CorporalExpressionComponent.class);
        simpleRoboboManager.bind(robName);
        corporal = simpleRoboboManager.getComponent(CorporalExpressionComponent.class);
        corporal.addListener(this);
    }


    public void stop() {
        Log.d(LOG_TAG, "Stopping It_1");
        stopMovement();
        if (corporal != null) {
            corporal.removeListener(this);
        }
        corporal = null;
        simpleRoboboManager.removeComponent(CorporalExpressionComponent.class);
        simpleRoboboManager.removeListener(this);
        simpleRoboboManager.unbind();
    }


    public boolean started() {
        return movementStarted;
    }


    protected void startMovement() throws RemoteException {
        final long actionPeriod = 2 * statusPeriod;
        corporal.setRobStatusPeriod(statusPeriod);
        movementStarted = true;
        /*movementRunnable = new Runnable() {
            @Override
            public void run() {
                if (movementStarted) {
                    try {
                        corporal.moveMTTime(moveMTMode, angVelR, angVelL, actionPeriod);
                        movementHandler.postDelayed(movementRunnable, actionPeriod);
                    } catch (RemoteException e) {
                        Log.e(LOG_TAG, "Error moving ROB: " + e.getMessage());
                        movementStarted = false;
                    }
                }
            }
        };
        movementHandler.post(movementRunnable);*/


        TimerTask movementTask = new TimerTask() {
            @Override
            public void run() {
                if (movementStarted) {
                    try {
                        corporal.moveMTTime(moveMTMode, angVelR, angVelL, actionPeriod);
                    } catch (RemoteException e) {
                        Log.e(LOG_TAG, "Error moving ROB: " + e.getMessage());
                        timer.cancel();
                        stop();
                    }
                }
            }
        };
        timer.schedule(movementTask, 0, statusPeriod);
    }


    protected void stopMovement() {
        movementStarted = false;
    }


    private void adjustSpeed() {
        float s1Adj, s2Adj, s3Adj, s4Adj, s5Adj, rcAdj, lcAdj, s1C, s5C;
        s1C = alpha * S1_THRESHOLD - irDistance[0];
        s5C = alpha * S5_THRESHOLD - irDistance[4];
        s1Adj = max(0, min(1, s1C));
        s2Adj = max(0, min(1, (alpha * S2_THRESHOLD - irDistance[1])));
        s3Adj = max(0, min(1, (alpha * S3_THRESHOLD - irDistance[2])));
        s4Adj = max(0, min(1, (alpha * S4_THRESHOLD - irDistance[3])));
        s5Adj = max(0, min(1, s5C));

        if (s3Adj <= 0) {
            Log.d(LOG_TAG, "Front Obstacle");
            if (s1C < s5C) {
                rcAdj = 0;
                lcAdj = 1;
            } else {
                rcAdj = 1;
                lcAdj = 0;
            }
        } else {
            rcAdj = (s1Adj + s2Adj + s3Adj) / 3;
            lcAdj = (s4Adj + s5Adj + s3Adj) / 3;
        }

        angVelR = round(MAX_ANG_VEL * rcAdj);
        angVelL = round(MAX_ANG_VEL * lcAdj);


        Log.d(LOG_TAG, "(" + (S1_THRESHOLD - irDistance[0]) + ", " + (S2_THRESHOLD - irDistance[1])
                + ", " + (S3_THRESHOLD - irDistance[2]) + ", "
                + (S4_THRESHOLD - irDistance[3]) + ", " + (S5_THRESHOLD - irDistance[4]) + ")");
        Log.d(LOG_TAG, "(" + irDistance[0] + ", " + irDistance[1] + ", " + irDistance[2] + ", "
                + irDistance[3] + ", " + irDistance[4] +  ")");
        Log.d(LOG_TAG, "(" + s1Adj + ", " + s2Adj + ", " + s3Adj + ", "
                + s4Adj + ", " + s5Adj  + ")");
        Log.d(LOG_TAG, "AdjustSpeed: (" + lcAdj + ", " + rcAdj + ")");
        Log.d(LOG_TAG, "Speed: (" + angVelL + ", " + angVelR + ")");
    }


    @Override
    public void internalError(String errorMsg) {
        stop();
        Log.e(LOG_TAG, "Robobo had an internal error. Stopping movement thread.");
    }


    @Override
    public void statusIRs(Collection<IRSensorStatus> irStatus) {
        this.irStatus = irStatus.toArray(new IRSensorStatus[irStatus.size()]);
        irDistance = new float[this.irStatus.length];
        for (int i = 0; i < irDistance.length; i++)
            irDistance[i] = this.irStatus[i].getDistance();
        adjustSpeed();
    }


    @Override
    public void onComponentsStarted() {
        Log.d(LOG_TAG, "Robobo movementStarted");
        try {
            startMovement();
        } catch (RemoteException e) {
            Log.d(LOG_TAG, "Error starting movement: " + e.getMessage() + "\n\nStopping test");
            stop();
        }
    }


    @Override
    public void onComponentsError(String errorMsg) {
        Log.d(LOG_TAG, "Robobo error: " + errorMsg);
    }


}
