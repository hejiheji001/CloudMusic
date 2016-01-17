package com.fireawayh.cloudmusic.utils;

import com.oracle.javafx.jmx.json.JSONDocument;
import com.oracle.javafx.jmx.json.JSONFactory;
import com.oracle.javafx.jmx.json.JSONReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by FireAwayH on 15/10/6.
 */
public class JsonUtils {


    private URIUtils uu = new URIUtils();
    private Logger log = new Logger();

    public JsonUtils(){

    }

    public  JSONDocument createJSONByURL(String URLStr){
        try {
            InputStream apiInStream = uu.getFromURI(URLStr);
            JSONReader jsonReader = JSONFactory.instance().makeReader(new InputStreamReader(apiInStream));
            return jsonReader.build();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public  JSONDocument getChildNodeByName(JSONDocument father, String childName){
        JSONDocument child = null;
        if(father.isObject()){
            child  = (JSONDocument)father.object().get(childName);
        }else if(father.isArray()){
            log.print("Child is array:");
            child = (JSONDocument)father.getMap(0).get(childName);
        }

        if (child != null && child.isArray()){
            child = (JSONDocument)child.array().get(0);
        }

        return child;
    }

    public  JSONDocument getChildNodesByName(JSONDocument father, String childName){
        JSONDocument child = null;
        if(father.isObject()){
            child = (JSONDocument)father.object().get(childName);
        }else if(father.isArray()){
            log.print("Child is array:");
            child = (JSONDocument)father.getMap(0).get(childName);
        }
        return child;
    }

    public ArrayList<String> getPlayListSongs(String playListId){
        ArrayList<String> tracks = new ArrayList<String>();
        List<Object> jsonList = new ArrayList<Object>();
        InputStream playListInStream = uu.getFromURI("http://music.163.com/api/playlist/detail?id=" + playListId);
        JSONDocument playListJson = JSONFactory.instance().makeReader(new InputStreamReader(playListInStream)).build();
        JSONDocument resultJson = getChildNodeByName(playListJson, "result");
        JSONDocument tracksJson = getChildNodesByName(resultJson, "tracks");
        if(tracksJson.isArray()){
            jsonList = tracksJson.array();
        }else if(tracksJson.isObject()){
//            jsonList = tracksJson
        }
        for(int i = 0; i < jsonList.size(); i++){
            tracks.add(((JSONDocument)jsonList.get(i)).object().get("id").toString());
        }
        tracks.add(resultJson.getString("name"));
        return tracks;
    }

}
