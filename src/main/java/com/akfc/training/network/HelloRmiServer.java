package com.akfc.training.network;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.nio.channels.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class HelloRmiServer extends UnicastRemoteObject implements HelloRmi {

    protected HelloRmiServer() throws RemoteException {
        super();
    }

    @Override
    public String sayHello() throws RemoteException {
        return "Hello !";
    }

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            HelloRmiServer server = new HelloRmiServer();
            String url = "rmi://" + InetAddress.getLocalHost().getHostAddress() + "/HelloRMI";
            Naming.rebind(url, server);
        } catch (RemoteException | AlreadyBoundException | UnknownHostException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
