package com.akfc.training.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HelloRmi extends Remote {
    String sayHello() throws RemoteException;
}
