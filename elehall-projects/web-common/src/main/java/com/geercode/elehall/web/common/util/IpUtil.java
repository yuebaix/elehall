package com.geercode.elehall.web.common.util;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * <p>Description : ip工具类</p>
 * <p>Created on  : 2019-07-16 16:27</p>
 *
 * @author jerryniu
 */
public class IpUtil {
    private static final String IP_UNKNOWN = "unknown";
    public static final String LONG_LOOP_ADDR = "0:0:0:0:0:0:0:1";
    public static final String SHORT_LOOP_ADDR = "127.0.0.1";
    private static final String IP_SEPARATOR = ",";
    private static final String[] HEADERS = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR",
            "X-Real-IP"
    };

    private IpUtil() {}

    /**
     * 判断ip是否为空，空返回true
     * @param ip
     * @return
     */
    public static boolean isEmptyIp(final String ip){
        return (ip == null || ip.length() == 0 || ip.trim().equals("") || IP_UNKNOWN.equalsIgnoreCase(ip));
    }


    /**
     * 判断ip是否不为空，不为空返回true
     * @param ip
     * @return
     */
    public static boolean isNotEmptyIp(final String ip){
        return !isEmptyIp(ip);
    }

    /***
     * 获取客户端ip地址(可以穿透代理)
     * @param request HttpServletRequest
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = "";
        for (String header : HEADERS) {
            ip = request.getHeader(header);
            if(isNotEmptyIp(ip)) {
                break;
            }
        }
        if(isEmptyIp(ip)){
            ip = request.getRemoteAddr();
        }
        if(isNotEmptyIp(ip) && ip.contains(IP_SEPARATOR)){
            ip = ip.split(IP_SEPARATOR)[0];
        }
        if(LONG_LOOP_ADDR.equals(ip)){
            ip = SHORT_LOOP_ADDR;
        }
        return ip;
    }


    /**
     * 获取本机的局域网ip地址，兼容Linux
     * @return String
     * @throws Exception
     */
    public static String getLocalHostIp() {
        String localHostAddress = "";
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = allNetInterfaces.nextElement();
                Enumeration<InetAddress> address = networkInterface.getInetAddresses();
                while (address.hasMoreElements()) {
                    InetAddress inetAddress = address.nextElement();
                    if (inetAddress != null && inetAddress instanceof Inet4Address) {
                        localHostAddress = inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            throw new RuntimeException(ex);
        }
        return localHostAddress;
    }

    /**
     * <p>description : 判断一个ip是否为内网ip</p>
     * <p>create   on : 2019-07-16 16:52:21</p>
     *
     * @author jerryniu
     * @since 1.0.0
     */
    public static boolean isInnerIp(String ipAddress){
        boolean isInnerIp = false;
        long ipNum = getIpNum(ipAddress);
        /**
         私有IP：A类  10.0.0.0-10.255.255.255
         B类  172.16.0.0-172.31.255.255
         C类  192.168.0.0-192.168.255.255
         当然，还有127这个网段是环回地址
         **/
        long aBegin = getIpNum("10.0.0.0");
        long aEnd = getIpNum("10.255.255.255");
        long bBegin = getIpNum("172.16.0.0");
        long bEnd = getIpNum("172.31.255.255");
        long cBegin = getIpNum("192.168.0.0");
        long cEnd = getIpNum("192.168.255.255");
        isInnerIp = isInner(ipNum,aBegin,aEnd) || isInner(ipNum,bBegin,bEnd) || isInner(ipNum,cBegin,cEnd) || SHORT_LOOP_ADDR.equals(ipAddress);
        return isInnerIp;
    }

    private static long getIpNum(String ipAddress) {
        String [] ip = ipAddress.split("\\.");
        long a = Integer.parseInt(ip[0]);
        long b = Integer.parseInt(ip[1]);
        long c = Integer.parseInt(ip[2]);
        long d = Integer.parseInt(ip[3]);

        long ipNum = a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
        return ipNum;
    }

    private static boolean isInner(long userIp,long begin,long end){
        return (userIp>=begin) && (userIp<=end);
    }
}
