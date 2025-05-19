package com.programming.advanced.wordle.util;

import java.util.HashMap;

public class Latin2Hira {
    HashMap<String, String> m = new HashMap<String, String>();
    public Latin2Hira() {
        m.put("a", "あ");
        m.put("i", "い");
        m.put("u", "う");
        m.put("e", "え");
        m.put("o", "お");
        m.put("ka", "か");
        m.put("ki", "き");
        m.put("ku", "く");
        m.put("ke", "け");
        m.put("ko", "こ");
        m.put("sa", "さ");
        m.put("si", "し");
        m.put("su", "す");
        m.put("se", "せ");
        m.put("so", "そ");
        m.put("ta", "た");
        m.put("ti", "ち");
        m.put("tu", "つ");
        m.put("te", "て");
        m.put("to", "と");
        m.put("na", "な");
        m.put("ni", "に");
        m.put("nu", "ぬ");
        m.put("ne", "ね");
        m.put("no", "の");
        m.put("ha", "は");
        m.put("hi", "ひ");
        m.put("hu", "ふ");
        m.put("he", "へ");
        m.put("ho", "ほ");
        m.put("ma", "ま");
        m.put("mi", "み");
        m.put("mu", "む");
        m.put("me", "め");
        m.put("mo", "も");
        m.put("ya", "や");
        m.put("yu", "ゆ");
        m.put("yo", "よ");
        m.put("ra", "ら");
        m.put("ri", "り");
        m.put("ru", "る");
        m.put("re", "れ");
        m.put("ro", "ろ");
        m.put("wa", "わ");
        m.put("wo", "を");
        m.put("n", "ん");

        m.put("ga", "か");
        m.put("gi", "き");
        m.put("gu", "く");
        m.put("ge", "け");
        m.put("go", "こ");
        m.put("za", "さ");
        m.put("zi", "し");
        m.put("zu", "す");
        m.put("ze", "せ");
        m.put("zo", "そ");
        m.put("da", "た");
        m.put("di", "ち");
        m.put("du", "つ");
        m.put("de", "て");
        m.put("do", "と");
        m.put("ba", "は");
        m.put("bi", "ひ");
        m.put("bu", "ふ");
        m.put("be", "へ");
        m.put("bo", "ほ");
        m.put("pa", "は");
        m.put("pi", "ひ");
        m.put("pu", "ふ");
        m.put("pe", "へ");
        m.put("po", "ほ");

        m.put("fa", "ふあ");
        m.put("fi", "ふい");
        m.put("fu", "ふ");
        m.put("fe", "ふえ");
        m.put("fo", "ふお");

        m.put("lya", "や");
        m.put("lyu", "ゆ");
        m.put("lyo", "よ");
        m.put("xya", "や");
        m.put("xyu", "ゆ");
        m.put("xyo", "よ");

        m.put("kya", "きや");
        m.put("kyu", "きゆ");
        m.put("kyo", "きよ");
        m.put("sha", "しや");
        m.put("shu", "しゆ");
        m.put("sho", "しよ");
        m.put("cha", "ちや");
        m.put("chu", "ちゆ");
        m.put("cho", "ちよ");
        m.put("nya", "にや");
        m.put("nyu", "にゆ");
        m.put("nyo", "によ");
        m.put("hya", "ひや");
        m.put("hyu", "ひゆ");
        m.put("hyo", "ひよ");
        m.put("mya", "みや");
        m.put("myu", "みゆ");
        m.put("myo", "みよ");
        m.put("rya", "りや");
        m.put("ryu", "りゆ");
        m.put("ryo", "りよ");

        m.put("gya", "きや");
        m.put("gyu", "きゆ");
        m.put("gyo", "きよ");
        m.put("ja", "しや");
        m.put("ju", "しゆ");
        m.put("jo", "しよ");
        m.put("jya", "しや");
        m.put("jyu", "しゅ");
        m.put("jyo", "しよ");
        m.put("bya", "ひや");
        m.put("byu", "ひゆ");
        m.put("byo", "ひよ");
        m.put("pya", "ひや");
        m.put("pyu", "ひゆ");
        m.put("pyo", "ひよ");

        m.put("-", "ー");
    }

    public String latin2Hira(String s) {
        StringBuilder result = new StringBuilder();
        
        // 入力を小文字にへんかん
        s = s.toLowerCase();

        for(int i = 0; i < s.length(); i++) {
            // 小さい「つ」のチェック
            if (i + 2 < s.length() && isConsonant(s.charAt(i)) && s.charAt(i) == s.charAt(i + 1)) {
                result.append("つ");
                i++; // 次の文字（同じ子音）をスキップ
                String remaining = s.substring(i+1, i+2);
                if (m.containsKey(s.charAt(i) + remaining)) {
                    result.append(m.get(s.charAt(i) + remaining));
                    i++;
                }
                continue;
            }

            // 3文字の場合
            if (i + 2 < s.length()) {
                String word = s.substring(i, i+3);
                if (m.containsKey(word)) {
                    result.append(m.get(word));
                    i+=2;
                    continue;
                }
            }

            // 2文字の場合
            if (i + 1 < s.length()) {
                String word = s.substring(i, i+2);
                if (m.containsKey(word)) {
                    result.append(m.get(word));
                    i += 1;
                    continue;
                }
            }

            // 1文字の場合
            String word = s.substring(i, i + 1);
            if (m.containsKey(word)) {
                result.append(m.get(word));
            }
        }
        return result.toString();
    }

    // 子音かどうかをチェックするよーん
    private boolean isConsonant(char c) {
        return "bcdfghjklmnpqrstvwxz".indexOf(c) != -1;
    }
}
