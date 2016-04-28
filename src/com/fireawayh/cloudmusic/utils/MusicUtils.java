package com.fireawayh.cloudmusic.utils;

import com.oracle.javafx.jmx.json.JSONDocument;

/**
 * By FireAwayH on 15/11/10.
 */
public class MusicUtils extends JsonUtils {


    private static JSONDocument hMusic;
    private static JSONDocument mMusic;
    private static JSONDocument lMusic;
    private static JSONDocument audition;
    private static JSONDocument bMusic;
    private static String artist, songName;
    private String totalSize;
    private Logger log = new Logger();

    public MusicUtils(String songId){
        JSONDocument jd = createJSONByURL("http://music.163.com/api/song/detail?ids=[" + songId + "]");
        JSONDocument songs = getChildNodeByName(jd, "songs");
        hMusic = getChildNodeByName(songs, "hMusic");
        mMusic = getChildNodeByName(songs, "mMusic");
        lMusic = getChildNodeByName(songs, "lMusic");
        bMusic = getChildNodeByName(songs, "bMusic");
        audition = getChildNodeByName(songs, "audition");
        artist = getChildNodeByName(songs, "artists").object().get("name").toString();
        songName = songs.object().get("name").toString();
    }

    public JSONDocument getBestMusic(){
        JSONDocument result = null;

        if(hMusic != null){
            log.print("Download hMusic");
            result = hMusic;
        }else if(mMusic != null){
            log.print("Download mMusic");
            result = mMusic;
        }else if(lMusic != null){
            log.print("Download lMusic");
            result = lMusic;
        }else if(bMusic != null) {
            log.print("Download bMusic");
            result = bMusic;
        }else if(audition != null) {
            log.print("Download audition");
            result = audition;
        }else{
            log.print("Error!");
        }

        if(result != null) {
            setTotalSize(result.object().get("size").toString());
        }
        return result;
    }

    public void setTotalSize(String totalSize){
        this.totalSize = totalSize;
    }

    public String getTotalSize(){
        return this.totalSize;
    }

    public String getArtist(){
        return artist;
    }

    public String getSongName(){
        return songName;
    }

}
