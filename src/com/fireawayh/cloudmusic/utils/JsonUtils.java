package com.fireawayh.cloudmusic.utils;

import com.oracle.javafx.jmx.json.JSONDocument;
import com.oracle.javafx.jmx.json.JSONFactory;
import com.oracle.javafx.jmx.json.JSONReader;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.*;

/**
 * Created by FireAwayH on 15/10/6.
 */
public class JsonUtils {


    private URIUtils uu = new URIUtils();
    private Logger log = new Logger();

    private static JSONDocument hMusic;
    private static JSONDocument mMusic;
    private static JSONDocument lMusic;
    private static JSONDocument audition;
    private static JSONDocument bMusic;
    private SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(SSLContexts.createDefault(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

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

    private JSONDocument getPlayListJson(String playListId){
        InputStream playListInStream = uu.getFromURI("http://music.163.com/api/playlist/detail?id=" + playListId);
        JSONDocument playListJson = JSONFactory.instance().makeReader(new InputStreamReader(playListInStream)).build();
        return playListJson;
    }

    public ArrayList<String> getPlayListSongs(String playListId){
        ArrayList<String> tracks = new ArrayList<String>();
        List<Object> jsonList = new ArrayList<Object>();
        JSONDocument playListJson = getPlayListJson(playListId);
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

    public List<Map<String, Object>> getPlayListBestMusic(String playListId){

        List<Object> jsonList = new ArrayList<Object>();
        JSONDocument playListJson = getPlayListJson(playListId);
        JSONDocument resultJson = getChildNodeByName(playListJson, "result");
        JSONDocument tracksJson = getChildNodesByName(resultJson, "tracks");

        if(tracksJson.isArray()){
            jsonList = tracksJson.array();
        }else if(tracksJson.isObject()){
//            jsonList = tracksJson
        }

        String title = resultJson.getString("name");
        List<Map<String, Object>> tracks = batchHandler(jsonList, title);
        return tracks;
    }

    private List<Map<String, Object>> batchHandler(List<Object> jsonList, String title) {
        List<Map<String, Object>> tracks = new ArrayList<>();
        Map<String, Object> temp = new HashMap<>();
        for(int i = 0; i < jsonList.size(); i++){
            Map<String, Object> x = ((JSONDocument) jsonList.get(i)).object();
            String id = x.get("id").toString();
            String name = x.get("name").toString();

            hMusic = (JSONDocument) x.get("hMusic");
            mMusic = (JSONDocument) x.get("mMusic");
            lMusic = (JSONDocument) x.get("lMusic");
            audition = (JSONDocument) x.get("audition");
            bMusic = (JSONDocument) x.get("bMusic");

            if(hMusic != null){
                temp = hMusic.object();
                log.print("Add hMusic");
            }else if(mMusic != null){
                temp = mMusic.object();
                log.print("Add mMusic");
            }else if(lMusic != null){
                temp = lMusic.object();
                log.print("Add lMusic");
            }else if(bMusic != null) {
                temp = bMusic.object();
                log.print("Add bMusic");
            }else if(audition != null) {
                temp = audition.object();
                log.print("Add audition");
            }else{
                log.print("Error, No Data");
            }
            temp.put("id", id);
            temp.put("name", name);
            tracks.add(temp);
        }
        temp.put("listname", title);
        tracks.add(temp);
        return tracks;
    }


    public List<Map<String, Object>> getAlbumBestMusic(String albumID){
        List<Object> jsonList = new ArrayList<Object>();
        JSONDocument albumJson = getAlbumJson(albumID);
        JSONDocument resultJson = getChildNodeByName(albumJson, "album");
        JSONDocument tracksJson = getChildNodesByName(resultJson, "songs");

        if(tracksJson.isArray()){
            jsonList = tracksJson.array();
        }else if(tracksJson.isObject()){
//            jsonList = tracksJson
        }
        String title = resultJson.getString("name");
        List<Map<String, Object>> tracks = batchHandler(jsonList, title);
        return tracks;
    }

    private JSONDocument getAlbumJson(String albumID) {
        JSONDocument albumJson = null;
        CloseableHttpClient c = getClient();
        HttpGet get = new HttpGet("http://music.163.com/api/album/" + albumID);
        HttpResponse response = null;
        try {
            response = c.execute(get);
            String content = EntityUtils.toString(response.getEntity());
            albumJson = JSONFactory.instance().makeReader(new StringReader(content)).build();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                EntityUtils.consume(response != null ? response.getEntity() : null);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return albumJson;
    }
    public CloseableHttpClient getClient() {
        List<Header> list = new LinkedList<>();
//                list.add(new BasicHeader("Host", HEADER_HOST));
//                list.add(new BasicHeader("Connection", HEADER_CONNECTION));
//                list.add(new BasicHeader("Accept", HEADER_ACCEPT));
//                list.add(new BasicHeader("Origin", HEADER_ORIGIN));
        list.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.75 Safari/537.36"));
        list.add(new BasicHeader("Referer", "http://music.163.com/"));
//                list.add(new BasicHeader("Accept-Encoding", HEADER_ACCEPT_ENCODING));
//                list.add(new BasicHeader("Accept-Language", HEADER_ACCEPT_LANGUAGE));
//                list.add(new BasicHeader("Cache-Control", "max-age=0"));
//                list.add(new BasicHeader("Connection", HEADER_CONNECTION));
        return HttpClientBuilder.create().setDefaultCookieStore(new BasicCookieStore()).setDefaultHeaders(list).setSSLSocketFactory(sslFactory).build();
    }

}
