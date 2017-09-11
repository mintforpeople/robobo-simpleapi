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

import com.mytechia.simpleapi.component.CorporalExpressionComponent;

import java.util.Timer;
import java.util.TimerTask;

import static com.mytechia.robobo.rob.MoveMTMode.FORWARD_FORWARD;
import static com.mytechia.robobo.rob.MoveMTMode.REVERSE_REVERSE;



public class CorporalTest {

    private static final String LOG_TAG = "CorporalTest";
    private CorporalExpressionComponent corporalComp;

    TimerTask setLED3WhiteTT = new TimerTask() {
        @Override
        public void run() {
            Log.d(LOG_TAG, "setLEDColor");
            try {
                corporalComp.setLEDColor(3, 0xFFFFFFFF);
            } catch (RemoteException e) {
                Log.e(LOG_TAG, "Error sending setLEDColor: " + e.getMessage());
            }
        }
    };
    TimerTask setLED1RedTT = new TimerTask() {
        @Override
        public void run() {
            Log.d(LOG_TAG, "setLEDColor");
            try {
                corporalComp.setLEDColor(1, 0xFFFF0000);
            } catch (RemoteException e) {
                Log.e(LOG_TAG, "Error sending setLEDColor: " + e.getMessage());
            }
        }
    };
    TimerTask setLED2GreenTT = new TimerTask() {
        @Override
        public void run() {
            Log.d(LOG_TAG, "setLEDColor");
            try {
                corporalComp.setLEDColor(2, 0xFF00FF00);
            } catch (RemoteException e) {
                Log.e(LOG_TAG, "Error sending setLEDColor: " + e.getMessage());
            }
        }
    };
    TimerTask setLED5ChocolateTT = new TimerTask() {
        @Override
        public void run() {
            Log.d(LOG_TAG, "setLEDColor");
            try {
                corporalComp.setLEDColor(5, -2987746);
            } catch (RemoteException e) {
                Log.e(LOG_TAG, "Error sending setLEDColor: " + e.getMessage());
            }
        }
    };
    TimerTask moveMTDegreesTT = new TimerTask() {
        @Override
        public void run() {
            Log.d(LOG_TAG, "moveMTDegrees");
            try {
                corporalComp.moveMTDegrees(REVERSE_REVERSE, 50, 90, 50, 90);
            } catch (RemoteException e) {
                Log.e(LOG_TAG, "Error sending moveMTDegrees: " + e.getMessage());
            }
        }
    };
    TimerTask moveMTDegreesRTT = new TimerTask() {
        @Override
        public void run() {
            Log.d(LOG_TAG, "moveMTRightDegrees");
            try {
                corporalComp.moveMTDegrees(50, 90, 0, 0);
            } catch (RemoteException e) {
                Log.e(LOG_TAG, "Error sending moveMTDegrees: " + e.getMessage());
            }
        }
    };
    TimerTask movePanTT = new TimerTask() {
        @Override
        public void run() {
            Log.d(LOG_TAG, "movePan");
            try {
                corporalComp.movePan(10, 0);
            } catch (RemoteException e) {
                Log.e(LOG_TAG, "Error sending movePan: " + e.getMessage());
            }
        }
    };
    TimerTask moveTiltTT = new TimerTask() {
        @Override
        public void run() {
            Log.d(LOG_TAG, "moveTilt");
            try {
                corporalComp.moveTilt(10, 90);
            } catch (RemoteException e) {
                Log.e(LOG_TAG, "Error sending moveTilt: " + e.getMessage());
            }
        }
    };
    TimerTask moveMTTimeTT = new TimerTask() {
        @Override
        public void run() {
            Log.d(LOG_TAG, "moveMTTime");
            try {
                corporalComp.moveMTTime(FORWARD_FORWARD, 50, 50, 1000);
            } catch (RemoteException e) {
                Log.e(LOG_TAG, "Error sending moveMTTime: " + e.getMessage());
            }
        }
    };
    TimerTask moveMTTimeLTT = new TimerTask() {
        @Override
        public void run() {
            Log.d(LOG_TAG, "moveMTLeftTime");
            try {
                corporalComp.moveMTTime(0, 50, 1000);
            } catch (RemoteException e) {
                Log.e(LOG_TAG, "Error sending moveMTTime: " + e.getMessage());
            }
        }
    };


    public CorporalTest(CorporalExpressionComponent corporalComp) {
        this.corporalComp = corporalComp;
    }


    public void test() {
        long delay = 0;
        Timer timer = new Timer();
        //timer.schedule(LEDsMode1TT, delay);
        //delay += 200;
        timer.schedule(moveMTDegreesTT, delay);
        delay += 800;
        timer.schedule(moveMTTimeTT, delay);
        delay += 1200;
        timer.schedule(moveMTDegreesRTT, delay);
        delay += 800;
        timer.schedule(moveMTTimeLTT, delay);
        delay += 1000;
        timer.schedule(movePanTT, delay);
        delay += 3800;
        timer.schedule(moveTiltTT, delay);
        //delay += 400;
        //timer.schedule(LEDsMode2TT, delay);
        delay += 800;
        timer.schedule(setLED3WhiteTT, delay);
        delay += 200;
        timer.schedule(setLED1RedTT, delay);
        delay += 200;
        timer.schedule(setLED2GreenTT, delay);
        delay += 200;
        timer.schedule(setLED5ChocolateTT, delay);
    }


}
