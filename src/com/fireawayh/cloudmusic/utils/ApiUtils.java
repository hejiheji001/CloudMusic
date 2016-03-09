package com.fireawayh.cloudmusic.utils;

import sun.misc.BASE64Encoder;

import java.security.MessageDigest;

/**
 * Created by FireAwayH on 15/10/6. XD
 */
public class ApiUtils {
    public ApiUtils(){
        super();
    }
    

    public  String getDownloadUrl(String bestMusicId){
        return "http://m2.music.126.net/" + getEncryptedId(bestMusicId) + "/"+ bestMusicId + ".mp3";
    }

    public  String getEncryptedId(String bestMusicId){
        String result = "";
        try {
            byte[] magic = "3go8&$8*3*3h0k(2)2".getBytes("UTF-8");
            byte[] song_id = bestMusicId.getBytes();

            int magic_len = magic.length;
            int song_id_len = song_id.length;

            for (int i=0; i<song_id_len; i++) {
                song_id[i] = (byte) (song_id[i]^magic[i%magic_len]);
            }

            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(song_id);

//            for(byte x : test){
//                System.out.println(x);
//            }

            byte [] str_encoded = md5.digest();
//            StringBuilder stringbuilder = new StringBuilder();
//            for (int i = 0; i < str_encoded.length; i++) {
//                if ((str_encoded[i] & 0xff) < 0x10) {
//                    stringbuilder.append("0");
//                }
//                stringbuilder.append(Long.toString(str_encoded[i] & 0xff, 16));
//            }
//            System.out.println(stringbuilder.toString());

//            for(byte x : str_encoded){
//                System.out.println(x);
//            }


            result = new BASE64Encoder().encode(str_encoded);
            result = result.replace('/', '_');
            result = result.replace('+', '-');
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

//    private String[] getSongIds(String playListId){
//        String[] songIdArr = null;
//        return songIdArr;
//    }

}
