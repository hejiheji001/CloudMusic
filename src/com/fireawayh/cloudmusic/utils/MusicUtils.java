package com.fireawayh.cloudmusic.utils;

import com.oracle.javafx.jmx.json.JSONDocument;

/**
 * Created by FireAwayH on 15/11/10.
 */
public class MusicUtils extends JsonUtils {


    private static JSONDocument jd, songs, hMusic, mMusic, lMusic;
    private static String artist, songName;
    private String totalSize;
    private Logger log = new Logger();

    public MusicUtils(String songId){
        this.jd = createJSONByURL("http://music.163.com/api/song/detail?ids=["+ songId +"]");
        this.songs = getChildNodeByName(jd, "songs");
        this.hMusic = getChildNodeByName(songs, "hMusic");
        this.mMusic = getChildNodeByName(songs, "mMusic");
        this.lMusic = getChildNodeByName(songs, "lMusic");
        this.artist = getChildNodeByName(songs, "artists").object().get("name").toString();
        this.songName = songs.object().get("name").toString();
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
