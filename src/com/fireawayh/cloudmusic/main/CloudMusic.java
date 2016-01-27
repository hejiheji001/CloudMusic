package com.fireawayh.cloudmusic.main;

import com.fireawayh.cloudmusic.utils.DownloadUtils;
import com.fireawayh.cloudmusic.utils.GUIUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CloudMusic {

    private static DownloadUtils du = new DownloadUtils();
    private static GUIUtils gu = new GUIUtils();


//    static String loadStream(InputStream in) throws IOException {
//        int ptr = 0;
//        in = new BufferedInputStream(in);
//        StringBuffer buffer = new StringBuffer();
//        while ((ptr = in.read()) != -1) {
//            buffer.append((char) ptr);
//        }
//        return buffer.toString();
//    }

    public static void main(String[] args) {
        // 1 getJson http://music.163.com/song?id=29561031
        // 2 getHmusicId
        // 3 getFileName
//        showHelp();



//        String cmd = "ls";
//        try {
//            Process ps = Runtime.getRuntime().exec(cmd);
//            System.out.print(loadStream(ps.getInputStream()));
//            System.err.print(loadStream(ps.getErrorStream()));
//        } catch(IOException ioe) {
//            ioe.printStackTrace();
//        }
//
//        System.exit(0);
        // 4 download
        ArrayList<String> argsList = new ArrayList<>(Arrays.asList(args));

        if(argsList.isEmpty()){
            test();
        }

////        argsList.add("-port");
////        argsList.add("1080");
////        argsList.add("-proxy");
////        argsList.add("127.0.0.1");
////        argsList.add("-proxytype");
////        argsList.add("socks5");

//        argsList.add("-playlist");
//        argsList.add("90649914");
//        argsList.add("-path");
//        argsList.add("/Users/FireAwayH/Desktop/");

//        try {
//
//            String xxx = "bds.comm.queryId=\"fa0c8f9300020a01\";\n" +
//                    "        bds.comm.pdc=0;\n" +
//                    "        bds.comm.user=\"剑仙乘仙剑\";\n" +
//                    "    bds.comm.username=bds.comm.user;";
//
//            Pattern pattern = Pattern.compile("bds.comm.user=\"(.*?)\"");
//            Matcher matcher = pattern.matcher(xxx);
//            if(matcher.find()){
//                System.out.println(matcher.group(1));
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }

//        System.out.println(xxx.contains("bdstoken"));


//        CookieUtils x = new CookieUtils();
//        try {
//
//            x.sendGet();
//            System.out.println("1111");
//        }catch(Exception e){
//            e.printStackTrace();
//        }


        System.exit(0);


        du.setArgs(argsList);
        gu.setArgs(argsList);
        switch (args[0]){
            case "-gui":
               gu.showGui();
                break;
            case "-h":
                showHelp();
                break;
            case "-playlist":
                du.downloadSongByList();
                break;
            default:
               du.downloadSongById(args[0], args[1]);
               break;
        }

//        du.downloadSongByList();
//        du.downloadSongById("253994", "/Users/FireAwayH/Desktop/");
    }

    private static void showHelp(){
        System.out.println("Download Music From Cloud Music!\r\n\r\nSimply put two arguments and everything will on the GO!\r\n\r\nExample:\r\nif\r\nyou want the music of http://music.163.com//song?id=31273246\r\nthen \r\njava -jar CloudMusic.jar 31273246 /Users/$whoami/Desktop/\r\n\r\nAlso, you can use proxy and gui(do not support proxy yet) :)\r\n\r\nUseage:\r\n\r\n<id> <path>: Complusory args\r\n\r\n-h Show help\r\n\r\n-gui Open GUI\r\n\r\n-proxy <host>: Use <host> as proxy, default = 210.26.85.241\r\n\r\n-port <port>: Use <port> for proxy, default = 80\r\n\r\n-proxytype <type>: Support http, https, ftp, socks4 and socks5, default = http\r\n");
        System.out.println("-playlist: Download all songs in a list!\r\n\r\n-path <path>: Set save path, if you use -playlist then you must set path");
        System.out.println("=============\r\nMy Google+: +FireAwayH");
        gu.printAscii("/ASCII_IMG/Rin.txt");
    }

    private static void test(){
//        du.downloadSongByList();
        Pattern p = Pattern.compile("/([0-9]*.mp3)");
        Matcher matcher = p.matcher("http://m2.music.126.net/8G7S9OVVCvhzNGJeZDIm-A==/5938462302083815.mp3");
        if (matcher.find()) {
            String oldFileName = matcher.group(1);
            System.out.print(oldFileName);
        }
    }

}
