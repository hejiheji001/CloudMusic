package com.fireawayh.cloudmusic.utils;

import com.oracle.javafx.jmx.json.JSONDocument;

/**
 * By FireAwayH on 15/11/10.
 */
public class MusicUtils extends JsonUtils {


    private static JSONDocument hMusic;
    private static JSONDocument mMusic;
    private static JSONDocument lMusic;
    private static String artist, songName;
    private String totalSize;
    private Logger log = new Logger();

    public MusicUtils(String songId){
        JSONDocument jd = createJSONByURL("http://music.163.com/api/song/detail?ids=[" + songId + "]");
        JSONDocument songs = getChildNodeByName(jd, "songs");
        hMusic = getChildNodeByName(songs, "hMusic");
        mMusic = getChildNodeByName(songs, "mMusic");
        lMusic = getChildNodeByName(songs, "lMusic");
        artist = getChildNodeByName(songs, "artists").object().get("name").toString();
        songName = songs.object().get("name").toString();
    }

    public JSONDocument getBestMusic(){
        JSONDocument result = hMusic;

        if(hMusic == null){
            if(mMusic == null){
                log.print("Download lMusic");
                result = lMusic;
            }else {
                log.print("Download mMusic");
                result = mMusic;
            }
        }else{
            log.print("Download hMusic");
        }
        setTotalSize(result.object().get("size").toString());
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
