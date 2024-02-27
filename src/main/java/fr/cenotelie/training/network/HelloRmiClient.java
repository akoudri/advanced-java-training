package fr.cenotelie.training.network;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public class HelloRmiClient {

    public static void main(String[] args) {
        try {
            Remote r = Naming.lookup("rmi://" + InetAddress.getLocalHost().getHostAddress() + "/HelloRMI");
            if (r instanceof HelloRmi) {
                String s = ((HelloRmi)r).sayHello();
                System.out.println(s);
            }
        } catch (RemoteException | NotBoundException | MalformedURLException | UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
