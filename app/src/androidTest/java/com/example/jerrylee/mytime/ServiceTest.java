package com.example.jerrylee.mytime;

import android.app.Instrumentation;
import android.content.Intent;
import android.content.pm.InstrumentationInfo;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ServiceTestCase;

import org.junit.*;
import service.TimerService;


public class ServiceTest extends ServiceTestCase<TimerService>{
    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();

    public ServiceTest() {
        super(TimerService.class);
    }


    @SmallTest
    public void TestStartable(){
        Intent intent = new Intent();
        intent.setClass(getContext(),TimerService.class);
        startService(intent);
        assertNotNull(getService());

    }

    @MediumTest
    public void TestBinding() {
        Intent intent = new Intent();
        intent.setClass(getContext(), TimerService.class);
        IBinder service = bindService(intent);
        assertNotNull(service);
    }

}
