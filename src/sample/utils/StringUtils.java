/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.utils;

import java.util.HashMap;

/**
 *
 * @author Administrator
 */
public class StringUtils {

//    public static String encodeAddress(String address) {
//  
//    }

    public static String toRawString(String content) {
        content = content.toLowerCase();
        HashMap<Character, Character> hm = new HashMap<Character, Character>();

        char sa[] = {'á', 'à', 'ạ', 'ả', 'ã', 'ă', 'ắ', 'ằ', 'ẳ', 'ẵ', 'ặ', 'â', 'ấ', 'ầ', 'ẩ', 'ẫ'};
        char su[] = {'ú', 'ù', 'ủ', 'ũ', 'ụ'};
        char suu[] = {'ư', 'ừ', 'ứ', 'ử', 'ữ', 'ự'};
        char soo[] = {'ơ', 'ờ', 'ớ', 'ở', 'ợ', 'ỡ'};
        char so[] = {'ô', 'ồ', 'ố', 'ộ', 'ổ', 'ỗ', 'ó', 'ò', 'ọ', 'ỏ', 'õ'};
        char si[] = {'ì', 'í', 'ỉ', 'ĩ', 'ị'};
        char sy[] = {'ỳ', 'ý', 'ỷ', 'ỹ', 'ỵ'};
        char se[] = {'è', 'é', 'ẻ', 'ẽ', 'ẹ'};
        char see[] = {'ê', 'ề', 'ế', 'ể', 'ễ', 'ệ'};
        char sd[] = {'đ'};

        for (char c : sa) {
            hm.put(c, 'a');
        }
        for (char c : su) {
            hm.put(c, 'u');
        }
        for (char c : suu) {
            hm.put(c, 'u');
        }
        for (char c : so) {
            hm.put(c, 'o');
        }
        for (char c : soo) {
            hm.put(c, 'o');
        }
        for (char c : si) {
            hm.put(c, 'i');
        }
        for (char c : sy) {
            hm.put(c, 'y');
        }
        for (char c : se) {
            hm.put(c, 'e');
        }
        for (char c : see) {
            hm.put(c, 'e');
        }
        for (char c : sd) {
            hm.put(c, 'd');
        }

        content = content.toLowerCase();
        String rs = "";

        for (int i = 0; i < content.length(); i++) {
            if (hm.containsKey(content.charAt(i))) {
                rs += hm.get(content.charAt(i));
            } else {
                rs += content.charAt(i);
            }
        }
        return rs;
    }
}
