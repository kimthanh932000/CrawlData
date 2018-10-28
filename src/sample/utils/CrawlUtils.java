/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class CrawlUtils {
//    LCS

    public static int computeMatchingDensity(String a, String b) {
        int n = a.length();
        int m = b.length();
        int dp[][] = new int[n + 1][m + 1];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (a.charAt(i) == b.charAt(j)) {
                    dp[i + 1][j + 1] = dp[i][j] + 1;
                } else {
                    dp[i + 1][j + 1] = Math.max(dp[i + 1][j], dp[i][j + 1]);
                }
            }
        }
        return dp[n][m];
    }
//    STACK

    public static boolean isAlphaChar(char x) {
        return (x >= 'a' && x <= 'z');
    }

    public static String getTagName(String content) {
        //if content is <input/><br/>
        if (content.charAt(content.length() - 2) == '/') {
            return null;
        }

        String res = "";
        int i = 1;

        //if content is end tag
        if (content.charAt(1) == '/') {
            res += "/";
            i++;
        }

        //content is open tag / end tag
        while (isAlphaChar(content.charAt(i))) {
            res += content.charAt(i);
            i++;
        }
        if (res.length() == 0 || (res.length() == 1 && res.charAt(0) == '/')) {
            return null;
        }

        return res;
    }

    public static String fixString(String content) {
        List<String> stack = new ArrayList<String>();
        List<Integer> li = new ArrayList<Integer>();
        List<String> addTag = new ArrayList<String>();

        int sz = content.length();
        int mark[] = new int[sz];
        Arrays.fill(mark, -1);

        int i = 0;
        while (i < content.length()) {
            if (content.charAt(i) == '<') {
                int j = i + 1;
                String tagTmp = "" + content.charAt(i);

                while (j < content.length() && content.charAt(j) != '>') {
                    tagTmp += content.charAt(j);
                    j++;
                }
                int curEnd = j;
                tagTmp += '>';
                i = j + 1;
                String tag = getTagName(tagTmp);

                if (tag != null) {
                    if (tag.charAt(0) != '/') {
                        stack.add(tag);
                        li.add(curEnd);
                    } else {
                        while (stack.size() > 0) {
                            if (stack.get(stack.size() - 1).equals(tag.substring(1))) {
                                stack.remove(stack.size() - 1);
                                li.remove(li.size() - 1);
                                break;
                            } else {
                                //need to
                                addTag.add(stack.get(stack.size() - 1));
                                mark[li.get(li.size() - 1)] = addTag.size() - 1;
                                //remove
                                stack.remove(stack.size() - 1);
                                li.remove(li.size() - 1);
                            }
                        }
                    }
                }
            }//end if first char == '<'
            else {
                i++;
            }
        }
        while (stack.size() > 0) {
            addTag.add(stack.get(stack.size() - 1));
            mark[li.get(li.size() - 1)] = addTag.size() - 1;
            stack.remove(stack.size() - 1);
            li.remove(li.size() - 1);
        }
        String newContent = "";
        for (int j = 0; j < content.length(); j++) {
            newContent = newContent + content.charAt(j);
            if (mark[j] != -1) {
                newContent += "</" + addTag.get(mark[j]) + ">";
            }
        }
        return "<root>" + "\n" + newContent + "</root>";
    }
//    HASHING

    public static int hashingString(String content) {
        int mod = 1000000007;
        int base = 30757; //random prime number

        int hashValue = 0;
        for (int i = 0; i < content.length(); i++) {
            hashValue = (int) (((long) hashValue * base + (long) content.charAt(i)) % mod);
        }
        return hashValue;
    }

    public static String cleanHTMLContent(String content) {
        content = content.replace("<br />", "")
                .replace("itemscope", "")
                .replace("null", "")
                .replace("&#38;&nbsp;", "")
                .replace("&nbsp;&nbsp;", "")
                .replace("\t", "")
                .replace("&", "&#38;")
                .replace("/>", ">")
                .replace("LANBELLE LAN'STAMANU CREAM-kem dưỡng da chiết xuất từ tự nhiên", "")
                .replace("< ", "")
                .replaceAll("(?m)^\\s", "");
        content = "<root>" + "\n" + content + "</root>";
        return content;
    }

}
