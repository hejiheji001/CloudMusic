package com.fireawayh.cloudmusic.utils;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * By FireAwayH on 15/11/10.
 */
public class URIUtils {
    public InputStream getFromURI(String URI){
        try {
            URL u = new URL(URI);
            URLConnection uri = u.openConnection();
            return uri.getInputStream();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String[] setProxy(List args){
        String proxy = "210.26.85.241";
        String port = "80";
        String proxytype = "HTTP";
        System.setProperty("proxySet", "true");

        if(args.contains("-proxy")){
            int i = args.indexOf("-proxy");
            proxy =  args.get(i + 1).toString();
        }

        if(args.contains("-port")){
            int i = args.indexOf("-port");
            port = args.get(i + 1).toString();
        }

        if(args.contains("-proxytype")) {
            int i = args.indexOf("-proxytype");
            proxytype =  args.get(i + 1).toString().toUpperCase();
        }

        switch (proxytype){
            case "HTTP":
                System.setProperty("http.proxyHost", proxy);
                System.setProperty("http.proxyPort", port);
                break;
            case "HTTPS":
                System.setProperty("https.proxyHost", proxy);
                System.setProperty("https.proxyPort", port);
                break;
            case "FTP":
                System.setProperty("ftp.proxHost", proxy);
                System.setProperty("ftp.proxyPort", port);
                break;
            case "SOCKS4":
                System.setProperty("socksProxyHost", proxy);
                System.setProperty("socksProxyPort", port);
                break;
            case "SOCKS5":
                System.setProperty("socksProxyHost", proxy);
                System.setProperty("socksProxyPort", port);
                break;
        }
        return new String[]{proxytype, proxy, port};
    }
}
