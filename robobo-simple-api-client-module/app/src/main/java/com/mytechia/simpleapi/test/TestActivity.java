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

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.mytechia.robobo.framework.hri.emotion.Emotion;
import com.mytechia.simpleapi.component.BalancePerceptionComponent;
import com.mytechia.simpleapi.component.ComponentNotFoundException;
import com.mytechia.simpleapi.component.CorporalExpressionComponent;
import com.mytechia.simpleapi.component.FacialExpressionComponent;
import com.mytechia.simpleapi.component.ISimpleRoboboManagerListener;
import com.mytechia.simpleapi.component.InternalPerceptionComponent;
import com.mytechia.simpleapi.component.SimpleRoboboManager;
import com.mytechia.simpleapi.component.SoundExpressionComponent;
import com.mytechia.simpleapi.component.SoundPerceptionComponent;
import com.mytechia.simpleapi.component.TouchPerceptionComponent;
import com.mytechia.simpleapi.component.VisualPerceptionComponent;
import com.mytechia.simpleapi.component.app.R;

import java.util.Timer;
import java.util.TimerTask;



public class TestActivity extends AppCompatActivity implements ISimpleRoboboManagerListener {

    private static final String LOG_TAG = "TestActivity";
    private static final String ROB_NAME = "ROB_29";
    private SimpleRoboboManager simpleRoboboManager = null;
    private Thread workerThread;
    private IIterationTest iterationTest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        simpleRoboboManager = SimpleRoboboManager.getInstance();
        simpleRoboboManager.addListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        workerThread = null;
        if (iterationTest != null)
            iterationTest.stop();
        if (simpleRoboboManager != null) {
            simpleRoboboManager.unbind();
            simpleRoboboManager.removeComponent();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (simpleRoboboManager != null) {
            simpleRoboboManager.unbind();
            simpleRoboboManager.removeComponent();
            simpleRoboboManager.removeListener(this);
            simpleRoboboManager = null;
        }
    }


    public void corporalTest(View v) {
        Log.d(LOG_TAG, "Corporal Test");
        try {
            simpleRoboboManager.addComponent(this, CorporalExpressionComponent.class);
            simpleRoboboManager.bind(ROB_NAME);

            final CorporalTest ct = new CorporalTest((CorporalExpressionComponent)
                    simpleRoboboManager.getComponentInstance(CorporalExpressionComponent.class));

            workerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(LOG_TAG, "Starting CorporalTest");
                    ct.test();
                }
            });

        } catch (ComponentNotFoundException e) {
            Log.e(LOG_TAG, "Component not found: " + e.getMessage());
        }
    }


    public void it1Test(View v) {
        try {
            iterationTest = new Iteration1Test(this, SimpleRoboboManager.getInstance());
            iterationTest.start(ROB_NAME);
        } catch (ComponentNotFoundException e) {
            Log.e(LOG_TAG, "Component not found: " + e.getMessage());
        }
    }


    public void visualTest(View v) {
        Log.d(LOG_TAG, "Visual Test");
        try {
            simpleRoboboManager.addComponent(this, VisualPerceptionComponent.class);
            simpleRoboboManager.bind(ROB_NAME);

            final VisualTest vt = new VisualTest((VisualPerceptionComponent) simpleRoboboManager
                    .getComponentInstance(VisualPerceptionComponent.class));


            workerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(LOG_TAG, "Starting VisualTest");
                    //vt.setRefreshRate(100);
                    vt.setHasChangedAmount(0);
                    vt.faceDetectionStart();
                    vt.blobTrackingStart();

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            vt.faceDetectionStop();
                            vt.blobTrackingStop();
                        }
                    }, 20000);
                }
            });

        } catch (ComponentNotFoundException e) {
            Log.e(LOG_TAG, "Component not found: " + e.getMessage());
        }
    }


    public void facialTest(View v) {
        Log.d(LOG_TAG, "Facial Test");
        try {
            simpleRoboboManager.addComponent(this, FacialExpressionComponent.class);
            simpleRoboboManager.bind(ROB_NAME);

            final FacialTest ft = new FacialTest((FacialExpressionComponent) simpleRoboboManager
                            .getComponentInstance(FacialExpressionComponent.class));

            workerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(LOG_TAG, "Starting FacialTest");
                    Integer delay = 0;
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            ft.setCurrentEmotion(Emotion.HAPPY);
                            ft.getCurrentEmotion();
                        }
                    }, delay);
                    delay += 2000;
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            ft.setCurrentEmotion(Emotion.SURPRISED);
                            ft.getCurrentEmotion();
                        }
                    }, delay);
                    delay += 2000;
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            ft.setCurrentEmotion(Emotion.HAPPY);
                        }
                    }, delay);
                    delay += 1000;
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            ft.setTemporalEmotion(Emotion.ANGRY, (long) 2000, Emotion.HAPPY);
                        }
                    }, delay);
                }
            });

        } catch (ComponentNotFoundException e) {
            Log.e(LOG_TAG, "Component not found: " + e.getMessage());
        }
    }


    public void touchTest(View v) {
        Log.d(LOG_TAG, "Touch Test");
        try {
            simpleRoboboManager.addComponent(this, TouchPerceptionComponent.class);
            simpleRoboboManager.bind(ROB_NAME);

            final TouchTest tt = new TouchTest((TouchPerceptionComponent) simpleRoboboManager
                    .getComponentInstance(TouchPerceptionComponent.class));
        } catch (ComponentNotFoundException e) {
            Log.e(LOG_TAG, "Component not found: " + e.getMessage());
        }
    }


    public void it2Test(View v) {
        try {
            iterationTest = new Iteration2Test(this, SimpleRoboboManager.getInstance());
            iterationTest.start(ROB_NAME);
        } catch (ComponentNotFoundException e) {
            Log.e(LOG_TAG, "Component not found: " + e.getMessage());
        }
    }


    public void soundTest(View v) {
        Log.d(LOG_TAG, "Sound Test");
        try {
            simpleRoboboManager.addComponent(this, SoundPerceptionComponent.class);
            simpleRoboboManager.addComponent(this, SoundExpressionComponent.class);
            simpleRoboboManager.bind(ROB_NAME);

            final SoundTest st = new SoundTest((SoundPerceptionComponent) simpleRoboboManager
                            .getComponentInstance(SoundPerceptionComponent.class),
                    (SoundExpressionComponent) simpleRoboboManager
                            .getComponentInstance(SoundExpressionComponent.class));

            workerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(LOG_TAG, "Starting SoundTest");
                    st.test();
                }
            });

        } catch (ComponentNotFoundException e) {
            Log.e(LOG_TAG, "Component not found: " + e.getMessage());
        }
    }


    public void it3Test(View v) {
        try {
            iterationTest = new Iteration3Test(this, SimpleRoboboManager.getInstance());
            iterationTest.start(ROB_NAME);
        } catch (ComponentNotFoundException e) {
            Log.e(LOG_TAG, "Component not found: " + e.getMessage());
        }
    }


    public void balanceTest(View v) {
        Log.d(LOG_TAG, "Balance Test");
        try {
            simpleRoboboManager.addComponent(this, BalancePerceptionComponent.class);
            simpleRoboboManager.bind(ROB_NAME);

            final BalanceTest bt = new BalanceTest((BalancePerceptionComponent) simpleRoboboManager
                    .getComponentInstance(BalancePerceptionComponent.class));

            workerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d(LOG_TAG, "Starting BalanceTest");
                        bt.setDetectionThreshold(1);
                    } catch (RemoteException e) {
                        Log.e(LOG_TAG, "setDetectionThreshold(): RemoteException");
                    }
                }
            });
        } catch (ComponentNotFoundException e) {
            Log.e(LOG_TAG, "Component not found: " + e.getMessage());
        }
    }


    public void internalTest(View v) {
        Log.d(LOG_TAG, "Internal Test");
        try {
            simpleRoboboManager.addComponent(this, InternalPerceptionComponent.class);
            simpleRoboboManager.bind(ROB_NAME);

            final InternalTest it = new InternalTest(simpleRoboboManager
                    .getComponent(InternalPerceptionComponent.class));
            workerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(LOG_TAG, "Starting InternalTest");
                    try {
                        it.setRefreshInterval(1000);
                    } catch (RemoteException e) {
                        Log.e(LOG_TAG, "Component not found: " + e.getMessage());
                    }
                }
            });

        } catch (ComponentNotFoundException e) {
            Log.e(LOG_TAG, "Component not found: " + e.getMessage());
        }
    }


    public void it4Test(View v) {
        try {
            iterationTest = new Iteration4Test(this, SimpleRoboboManager.getInstance());
            iterationTest.start(ROB_NAME);
        } catch (ComponentNotFoundException e) {
            Log.e(LOG_TAG, "Component not found: " + e.getMessage());
        }
    }


    @Override
    public void onComponentsStarted() {
        Log.d(LOG_TAG, "Robobo Started");
        if (workerThread != null)
            workerThread.start();
    }


    @Override
    public void onComponentsError(String errorMsg) {
        Log.e(LOG_TAG, "Robobo error: " + errorMsg);
    }


}
