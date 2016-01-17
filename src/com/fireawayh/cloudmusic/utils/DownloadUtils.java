package com.fireawayh.cloudmusic.utils;

import com.oracle.javafx.jmx.json.JSONDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by FireAwayH on 15/10/6.
 */
public class DownloadUtils {

    private ApiUtils au = new ApiUtils();
    private URIUtils uu = new URIUtils();
    private JsonUtils ju = new JsonUtils();
    private Logger log = new Logger();
    private List args = null;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    private String filePath = "";

    public DownloadUtils(){
        super();
    }
//    public DownloadUtils(String[] args){
//        super();
//        this.args = args;
//    }

    public void setArgs(List args){
        this.args = args;
    }

    public List getArgs(){
        return this.args;
    }

    public String downloadSongByList(){
        String result = "";
        String playListId = null;
        String filePath = null;
        if(args.contains("-playlist")) {
            int i = args.indexOf("-playlist");
            playListId = args.get(i + 1).toString().toUpperCase();
        }else{
            result = "No playlist id found";
        }
        if(args.contains("-path")) {
            int j = args.indexOf("-path");
            filePath = args.get(j + 1).toString().toUpperCase();
        }else{
            result = "No path id found";
        }

        if(!playListId.isEmpty() && !filePath.isEmpty()){
            ArrayList<String> songIds = ju.getPlayListSongs(playListId);
            filePath += songIds.get(songIds.size() - 1);
            log.print("Files will be saved to " + stringConvert(filePath));
            for (int i = 0; i < songIds.size() - 1; i++){
                downloadSongById(songIds.get(i), filePath);
            }
            log.print("All mission done!");
        }
        return result;
    }

    public String stringConvert(String str) {
        try {
            String fileEncode = System.getProperty("file.encoding");
            String result = new String(str.getBytes("UTF-8"), fileEncode);
            return result;
        }catch (Exception e){
            return "?";
        }
    }

    public String downloadSongById(String songId, String filePath){
        int bytesum = 0;
        int byteread = 0;
        this.filePath = filePath;

        try {
            MusicUtils mu = new MusicUtils(songId);
            Map music = mu.getBestMusic().object();
            String artistName = mu.getArtist();
            String songName = mu.getSongName();
            String bestMusicId = music.get("dfsId").toString();

            String fileName = artistName + " - " + songName  + "." + music.get("extension");
            String newFileName = stringConvert(fileName);

            String durl = au.getDownloadUrl(bestMusicId);
            String ipAddr = checkIP();

            log.print("Current task is ——> " + newFileName);
            log.print(durl + " AT " + ipAddr);

            if(!ipAddr.equals("CN")){
                String[] proxyInfo = uu.setProxy(args);
                ipAddr = checkIP();
                log.print(durl + " AT " + ipAddr + " VIA " + proxyInfo[0] + " PROXY " + proxyInfo[1] + ":" + proxyInfo[2]);
            }

            InputStream inStream = uu.getFromURI(durl);
            File folder = new File(this.filePath, artistName);
            if (!folder.exists() && !folder.isDirectory()) {
                folder.mkdirs();
            }

            File f = new File(folder, fileName);
            FileOutputStream fs = new FileOutputStream(f);

            byte[] buffer = new byte[2048];
            String totalSize = mu.getTotalSize();
            log.print("Total:" + totalSize);
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                fs.write(buffer, 0, byteread);
//                log.print("Finished:" + bytesum + "/" + totalSize + " " + getProgress(bytesum, totalSize));
            }
            fs.close();
            return "Download Complete";
        } catch (Exception e) {
            e.printStackTrace();
            return "Download Failed";
        }
    }

    // TODO: consider to use thread
    public void doloadSongByIds(String[] songIdArr, String filePath){
        for (String songId : songIdArr){
            downloadSongById(songId, filePath);
        }
    }

    public String checkIP(){
        String url = "http://ipinfo.io/json";
        ju =  new JsonUtils();
        JSONDocument ip = ju.createJSONByURL(url);
        return ip.getString("country");
    }

    public String getProgress(int sum, String total){
        double dSum = Double.valueOf(sum);
        double dTotal = Double.valueOf(total);
        double dProgress = dSum / dTotal * 100.0;
        return String.format("%.2f%s", dProgress, "%");
    }
}
