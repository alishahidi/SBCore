package com.alishahidi.sbcore.util;

import com.alishahidi.sbcore.security.user.User;
import lombok.experimental.UtilityClass;
import org.quartz.JobDataMap;
import org.springframework.security.core.context.SecurityContextHolder;

import java.net.*;
import java.util.Enumeration;

@UtilityClass
public class QuartzUtils {

    public void setDefaultServerFields(JobDataMap jobDataMap) {
        jobDataMap.put("ip", getClientIp());
        jobDataMap.put("serverIp", getServerIp());
        jobDataMap.put("username", getUsername());
    }

    public static String getServerIp() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();

                if (iface.isLoopback() || iface.isVirtual() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof Inet4Address) {
                        String ip = addr.getHostAddress();
                        if (!ip.startsWith("127.") && !ip.startsWith("169.254.")) {
                            return ip;
                        }
                    }
                }
            }
        } catch (SocketException ignored) {
        }
        return "127.0.0.1";
    }

    public String getClientIp() {
        try {
            return IpUtils.getClientIp();
        } catch (Exception e) {
            return "0.0.0.0";
        }
    }

    public String getUsername() {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return user.getUsername();
        } catch (Exception e) {
            return "unknown";
        }
    }
}
