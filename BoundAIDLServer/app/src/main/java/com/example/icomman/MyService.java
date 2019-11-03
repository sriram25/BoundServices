package com.example.icomman;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;

public class MyService extends Service {
    private MyImpl impl = new MyImpl();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new IComman.Stub() {
            @Override
            public int calculate(int num1, int num2) throws RemoteException {
                return num1 * num2;
            }

            @Override
            public List<Person> getPersonsList() throws RemoteException {
                return MainActivity.getPersonList();
            }
        };
    }

    public class MyImpl extends IComman.Stub {
        @Override
        public int calculate(int num1, int num2) throws RemoteException {
            return num1 * num2;
        }

        @Override
        public List<Person> getPersonsList() throws RemoteException {
            return MainActivity.getPersonList();
        }
    }

}
