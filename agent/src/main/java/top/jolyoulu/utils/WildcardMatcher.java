package top.jolyoulu.utils;

import java.util.regex.Pattern;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/7 13:02
 * @Version 1.0
 * 通配符工具类
 */
public class WildcardMatcher {

    private final Pattern pattern;

    public WildcardMatcher(final String expression) {
        final String[] parts = expression.split("&");
        final StringBuffer regex = new StringBuffer(expression.length() * 2);
        boolean next =false;
        for (final String part : parts) {
            if (next){
                regex.append('|');
            }
            regex.append('(').append(toRegex(part)).append(')');
            next = true;
        }
        pattern = Pattern.compile(regex.toString());
    }

    public static CharSequence toRegex(final String expression){
        final StringBuffer regex = new StringBuffer(expression.length() * 2);
        for (final char c : expression.toCharArray()) {
            switch (c){
                case '?':
                    regex.append(".?");
                    break;
                case '*':
                    regex.append(".*");
                    break;
                default:
                    regex.append(Pattern.quote(String.valueOf(c)));
                    break;
            }
        }
        return regex;
    }

    public boolean matches(final String s){
        return pattern.matcher(s).matches();
    }
}
