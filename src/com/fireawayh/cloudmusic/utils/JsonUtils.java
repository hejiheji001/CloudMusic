package com.fireawayh.cloudmusic.utils;

import com.oracle.javafx.jmx.json.JSONDocument;
import com.oracle.javafx.jmx.json.JSONFactory;
import com.oracle.javafx.jmx.json.JSONReader;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
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

    public JSONDocument getMusicById(String songId){
        JSONDocument musicJson = null;
        CloseableHttpClient c = getClient();
        HttpGet get = new HttpGet("http://music.163.com/api/song/detail?ids=[" + songId + "]");
        HttpResponse response = null;
        try {
            response = c.execute(get);
            String content = EntityUtils.toString(response.getEntity());
            musicJson = JSONFactory.instance().makeReader(new StringReader(content)).build();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                EntityUtils.consume(response != null ? response.getEntity() : null);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return musicJson;
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
            JSONDocument artistJson = JSONFactory.instance().makeReader(new StringReader(x.get("artists").toString())).build();

            String artist = (((JSONDocument)artistJson.array().get(0)).object().get("name").toString());

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
            temp.put("artist", artist);
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
//        BasicCookieStore c = new BasicCookieStore();
//        c.addCookie(new BasicClientCookie("vjuids", "3b804c5ee.14937066b3f.0.532f524a"));
//        c.addCookie(new BasicClientCookie("_ntes_nuid", "0ccb5db5a4619b10640e6ffef4c69e8e"));
//        c.addCookie(new BasicClientCookie("__oc_uuid", "6da408e0-4c32-11e4-a7d2-25282df4e62f"));
//        c.addCookie(new BasicClientCookie("__utma", "187553192.19570170.1415529773.1426167151.1434093871.5"));
//        c.addCookie(new BasicClientCookie("usertrack", "c+5+hVYieSVoQQPAmWzMAg=="));
//        c.addCookie(new BasicClientCookie("_ntes_nnid", "0ccb5db5a4619b10640e6ffef4c69e8e,1448710701136"));
//        c.addCookie(new BasicClientCookie("mop_uniq_ckid", "147.188.254.235_1449242941_252763542"));
//        c.addCookie(new BasicClientCookie("NTES_CMT_USER_INFO", "8169123%7CFireAwayH%7C%7Cfalse%7CaGVqaWhlamkwMDFAMTYzLmNvbQ%3D%3D"));
//        c.addCookie(new BasicClientCookie("NTES_REPLY_NICKNAME", "hejiheji001%40163.com%7CFireAwayH%7C5953924576045841280%7C482801921%7C%7C%7C1%7C2"));
//        c.addCookie(new BasicClientCookie("P_INFO", "hejiheji001@163.com|1461360563|0|mobilemail|11&15|UK&1459896046&cloudmusic#UK&null#10#0#0|150879&0||hejiheji001@163.com"));
//        c.addCookie(new BasicClientCookie("__gads", "ID=9ec743bf0b3b8286:T=1462144114:S=ALNI_MZa6cZOCYNRzuvYVFQpli4CMMJWBw"));
//        c.addCookie(new BasicClientCookie("vinfo_n_f_l_n3", "3589991f323e8f11.1.16.1413967407959.1458602147375.1462144138358"));
//        c.addCookie(new BasicClientCookie("JSESSIONID-WYYY", "132a0af230b499b1160250dbfa389d579eb7a0e6e66753d7f96b1e370eaaa878420d9f094d8e785ab0d9ba37ce59916c4b3f09e6b7b4890b867c6d4c343491db960d41bd9f5dfabeef08b6f224b4493e990537389c8762b23b528fad6b7f715d95905c470c1ca160ab2e50266404b428e1a69ab4de20a1363cfefdd3089dbd112f3ae56e%3A1462286163816"));
//        c.addCookie(new BasicClientCookie("NTESmusic_yyrSI", "9B200338BCFC6C427090D6018B203F11.classa-music-yyr2.server.163.org-8010"));
//        c.addCookie(new BasicClientCookie("MUSIC_U", "1d6625c7be820bc0f431e9d514b9f9bb9014cdb96ea48ad47852d44d61ff9212e34c24c5364c14f550e0339ac1b6f46641049cea1c6bb9b6"));
//        c.addCookie(new BasicClientCookie("__utmz", "94650624.1445868593.64.40.utmcsr=euwebmail.mail.163.com|utmccn=(referral)|utmcmd=referral|utmcct=/js6/main.jsp"));
//        c.addCookie(new BasicClientCookie("_ga","GA1.2.19570170.1415529773"));
//        c.addCookie(new BasicClientCookie("vjlast","1413967408.1462144117.11"));
//        c.addCookie(new BasicClientCookie("ne_analysis_trace_id","1462144117435"));
//        c.addCookie(new BasicClientCookie("Province","0"));
//        c.addCookie(new BasicClientCookie("City","0"));
//        c.addCookie(new BasicClientCookie("s_n_f_l_n3","3589991f323e8f111462144117443"));
//        c.addCookie(new BasicClientCookie("_iuqxldmzr_","25"));
//        c.addCookie(new BasicClientCookie("visited","true"));
//        c.addCookie(new BasicClientCookie("playerid","14738835"));
//        c.addCookie(new BasicClientCookie("__remember_me","true"));
//        c.addCookie(new BasicClientCookie("__csrf","fbf24741069cfbc3d36da520bd06a5f7"));
//        c.addCookie(new BasicClientCookie("NETEASE_WDA_UID","29697234#|#1404555711519"));
//        c.addCookie(new BasicClientCookie("__utma","94650624.401899771.1412947942.1462135436.1462282624.109"));
//        c.addCookie(new BasicClientCookie("__utmb","94650624.25.10.1462282624"));
//        c.addCookie(new BasicClientCookie("__utmc","94650624"));
        List<Header> list = new LinkedList<>();
        list.add(new BasicHeader("Host", "music.163.com"));
        list.add(new BasicHeader("Cookie", "vjuids=3b804c5ee.14937066b3f.0.532f524a; _ntes_nuid=0ccb5db5a4619b10640e6ffef4c69e8e; __oc_uuid=6da408e0-4c32-11e4-a7d2-25282df4e62f; __utma=187553192.19570170.1415529773.1426167151.1434093871.5; usertrack=c+5+hVYieSVoQQPAmWzMAg==; NTES_REPLY_NICKNAME=hejiheji001%40163.com%7CFireAwayH%7C5953924576045841280%7C482801921%7C%7C%7C1%7C2; _ntes_nnid=0ccb5db5a4619b10640e6ffef4c69e8e,1448710701136; mop_uniq_ckid=147.188.254.235_1449242941_252763542; NTES_CMT_USER_INFO=8169123%7CFireAwayH%7C%7Cfalse%7CaGVqaWhlamkwMDFAMTYzLmNvbQ%3D%3D; P_INFO=hejiheji001@163.com|1461360563|0|mobilemail|11&15|UK&1459896046&cloudmusic#UK&null#10#0#0|150879&0||hejiheji001@163.com; _ga=GA1.2.19570170.1415529773; __gads=ID=9ec743bf0b3b8286:T=1462144114:S=ALNI_MZa6cZOCYNRzuvYVFQpli4CMMJWBw; vjlast=1413967408.1462144117.11; ne_analysis_trace_id=1462144117435; Province=0; City=0; vinfo_n_f_l_n3=3589991f323e8f11.1.16.1413967407959.1458602147375.1462144138358; s_n_f_l_n3=3589991f323e8f111462144117443; visited=true; playerid=14738835; NTESmusic_yyrSI=9A035027E1A5D875C3B7FC56EE9E764B.classa-music-yyr1.server.163.org-8010; JSESSIONID-WYYY=fd358739d58fd1d823aeee43ca4382188e288cf53f3e7994f2abb3d79c2d61e2aaa041131d6f9f0415dffa13829a538b1af36df5e38f808cd9b471ab619853b21dc8217ad0f51a0cab03f9293ffb5bd06d149716307c92735f1e953fe4485534ff4f0fe03c86bc53c9653d916fa42ef07c83ba7885d0a67c51180ee126d745c2a9861f34%3A1462295781973; _iuqxldmzr_=25; __remember_me=true; MUSIC_U=1d6625c7be820bc0f431e9d514b9f9bbb90ced4c68342cb8c41368cc663c166bb44e9d99d9fb7ad8854f33fc5c8c11d241049cea1c6bb9b6; __csrf=fbf24741069cfbc3d36da520bd06a5f7; NETEASE_WDA_UID=29697234#|#1404555711519; __utma=94650624.401899771.1412947942.1462282624.1462293984.110; __utmb=94650624.4.10.1462293984; __utmc=94650624; __utmz=94650624.1445868593.64.40.utmcsr=euwebmail.mail.163.com|utmccn=(referral)|utmcmd=referral|utmcct=/js6/main.jsp"));
//                list.add(new BasicHeader("Accept", HEADER_ACCEPT));
        list.add(new BasicHeader("Connection", "keep-alive"));
//                list.add(new BasicHeader("Origin", HEADER_ORIGIN));
        list.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.76 Mobile Safari/537.36"));
        list.add(new BasicHeader("Referer", "http://music.163.com/"));
//                list.add(new BasicHeader("Accept-Encoding", HEADER_ACCEPT_ENCODING));
//                list.add(new BasicHeader("Accept-Language", HEADER_ACCEPT_LANGUAGE));
//                list.add(new BasicHeader("Cache-Control", "max-age=0"));
//                list.add(new BasicHeader("Connection", HEADER_CONNECTION));
        return HttpClientBuilder.create().setDefaultHeaders(list).setSSLSocketFactory(sslFactory).build();
    }

}
