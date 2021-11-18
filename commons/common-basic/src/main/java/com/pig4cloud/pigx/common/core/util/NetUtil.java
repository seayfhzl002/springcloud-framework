package com.pig4cloud.pigx.common.core.util;

import java.net.*;
import java.util.Enumeration;

public class NetUtil {
    public NetUtil() {
    }

//    public static void main(String[] a) throws UnknownHostException, SocketException {
//        InetAddress inet = getFirstNonLoopbackAddress(true, false);
//        System.out.println("local realIP: " + getIp());
//        System.out.println(getLocalIpAddr());
//    }

    public static String getIp() throws UnknownHostException {
        try {
            return getFirstNonLoopbackAddress(true, false).getHostAddress();
        } catch (SocketException var1) {
            return InetAddress.getLocalHost().getHostAddress();
        }
    }

    public static InetAddress getFirstNonLoopbackAddress(boolean preferIpv4, boolean preferIPv6) throws SocketException {
        Enumeration en = NetworkInterface.getNetworkInterfaces();

        while (en.hasMoreElements()) {
            NetworkInterface i = (NetworkInterface) en.nextElement();
            Enumeration en2 = i.getInetAddresses();

            while (en2.hasMoreElements()) {
                InetAddress addr = (InetAddress) en2.nextElement();
                if (!addr.isLoopbackAddress()) {
                    if (addr instanceof Inet4Address) {
                        if (!preferIPv6) {
                            return addr;
                        }
                    } else if (addr instanceof Inet6Address && !preferIpv4) {
                        return addr;
                    }
                }
            }
        }

        return null;
    }

    public static String getLocalIpAddr() {
        String clientIP = null;
        Enumeration networks = null;

        try {
            networks = NetworkInterface.getNetworkInterfaces();
            if (networks == null) {
                return null;
            }
        } catch (SocketException var7) {
            System.out.println(var7.getMessage());
        }

        while (networks.hasMoreElements()) {
            NetworkInterface ni = (NetworkInterface) networks.nextElement();

            try {
                if (!ni.isUp() || ni.isLoopback() || ni.isVirtual()) {
                    continue;
                }
            } catch (SocketException var8) {
            }

            Enumeration<InetAddress> addrs = ni.getInetAddresses();
            if (addrs != null) {
                while (addrs.hasMoreElements()) {
                    InetAddress ip = addrs.nextElement();
                    if (!ip.isLoopbackAddress() && ip.isSiteLocalAddress() && ip.getHostAddress().indexOf(":") == -1) {
                        try {
                            clientIP = ip.toString().split("/")[1];
                        } catch (ArrayIndexOutOfBoundsException var6) {
                            clientIP = null;
                        }
                    }
                }
            }
        }

        return clientIP;
    }
}
