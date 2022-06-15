package com.hunglm.address.mapping.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public final class StringUtils {
    private StringUtils(){}

    private static final char[] marked = { 'à', 'á', 'ả', 'ã', 'ạ', 'ă', 'ằ', 'ắ', 'ẳ', 'ẵ', 'ặ', 'â', 'ầ', 'ấ', 'ẩ',
            'ẫ', 'ậ', 'À', 'Á', 'Ả', 'Ã', 'Ạ', 'Ă', 'Ằ', 'Ắ', 'Ẳ', 'Ẵ', 'Ặ', 'Â', 'Ầ', 'Ấ', 'Ẩ', 'Ẫ', 'Ậ', 'è', 'é',
            'ẻ', 'ẽ', 'ẹ', 'ê', 'ề', 'ế', 'ể', 'ễ', 'ệ', 'È', 'É', 'Ẻ', 'Ẽ', 'Ẹ', 'Ê', 'Ề', 'Ế', 'Ể', 'Ễ', 'Ệ', 'ì',
            'í', 'ỉ', 'ĩ', 'ị', 'Ì', 'Í', 'Ỉ', 'Ĩ', 'Ị', 'ò', 'ó', 'ỏ', 'õ', 'ọ', 'ô', 'ồ', 'ố', 'ổ', 'ỗ', 'ộ', 'ơ',
            'ờ', 'ớ', 'ở', 'ỡ', 'ợ', 'Ò', 'Ó', 'Ỏ', 'Õ', 'Ọ', 'Ô', 'Ồ', 'Ố', 'Ổ', 'Ỗ', 'Ộ', 'Ơ', 'Ờ', 'Ớ', 'Ở', 'Ỡ',
            'Ợ', 'ù', 'ú', 'ủ', 'ũ', 'ụ', 'ư', 'ừ', 'ứ', 'ử', 'ữ', 'ự', 'Ù', 'Ú', 'Ủ', 'Ũ', 'Ụ', 'ỳ', 'ý', 'ỷ', 'ỹ',
            'ỵ', 'Ỳ', 'Ý', 'Ỷ', 'Ỹ', 'Ỵ', 'đ', 'Đ', 'Ư', 'Ừ', 'Ử', 'Ữ', 'Ứ', 'Ự' };

    private static final char[] notmarked = { 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a',
            'a', 'a', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'e', 'e',
            'e', 'e', 'e', 'e', 'e', 'e', 'e', 'e', 'e', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'i',
            'i', 'i', 'i', 'i', 'I', 'I', 'I', 'I', 'I', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o',
            'o', 'o', 'o', 'o', 'o', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O',
            'O', 'u', 'u', 'u', 'u', 'u', 'u', 'u', 'u', 'u', 'u', 'u', 'U', 'U', 'U', 'U', 'U', 'y', 'y', 'y', 'y',
            'y', 'Y', 'Y', 'Y', 'Y', 'Y', 'd', 'D', 'U', 'U', 'U', 'U', 'U', 'U' };

    private static final String hashTagRegex = "[0-9a-zA-Z_ÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸĐàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđ]+";

    public static boolean hasLength(String str) {
        return hasLength((CharSequence) str);
    }
    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
    }

    public static String trimWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str);
        while (sb.length() > 0 && Character.isWhitespace(sb.charAt(0))) {
            sb.deleteCharAt(0);
        }
        while (sb.length() > 0 && Character.isWhitespace(sb.charAt(sb.length() - 1))) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static boolean isEmptyString(String value) {
        if (value == null || value.trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static String getMD5Hash(String input, String salt) {
        MessageDigest digest;
        try {
            if (salt != null) {
                input = salt.trim() + input;
            }
            digest = MessageDigest.getInstance("MD5");
            digest.update(input.getBytes());
            final byte[] hash = digest.digest();
            final StringBuilder result = new StringBuilder(hash.length);
            for (int i = 0; i < hash.length; i++) {
                result.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
            }
            return result.toString();
        } catch (final NoSuchAlgorithmException e) {
            return "error";
        }
    }

    public static String convertUnicodeToEngString(String unicodeString) {
        if (!hasLength(unicodeString)) {
            return "";
        }
        unicodeString = trimWhitespace(unicodeString);
        for (int i = 0; i < marked.length; i++) {
            unicodeString = unicodeString.replace(marked[i], notmarked[i]);
        }
        return unicodeString;
    }

    public static String nomalizeUnicodeAndSpecialChars(String input){
        String regex = "[^a-zA-Z\\d ]";
        return replaceNumberToString(convertUnicodeToEngString(input)
                .replaceAll(regex, "")
                .trim()
                .replaceAll(" +", " "));
    }

    private static String replaceNumberToString(String input){
        return input
                .replace("0", "NKhong")
                .replace("1", "NMot")
                .replace("2", "NHai")
                .replace("3", "NBa")
                .replace("4", "NBon")
                .replace("5", "NNam")
                .replace("6", "NSau")
                .replace("7", "NBay")
                .replace("8", "NTam")
                .replace("9", "NChin")
                .trim();
    }

    public static int countLines(String input) throws IOException {
        LineNumberReader lineNumberReader = new LineNumberReader(new StringReader(input));
        lineNumberReader.skip(Long.MAX_VALUE);
        return lineNumberReader.getLineNumber();
    }

    public static boolean checkIsNumber(String text) {
        String regexStr = "^\\d*$";
        return text.trim().matches(regexStr);
    }

    public static String removeAccent(String s) {
        if (isNullOrEmpty(s)) {
            return "";
        }
        s = s.toLowerCase();
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d");
    }

    public static String formatParam(String s) {
        if (isNullOrEmpty(s)) {
            return "";
        }
        s = s.toLowerCase();
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        s = pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d");
        s = s.replaceAll("[^a-zA-Z0-9-_]", "_");
        s = s.replaceAll("_+_", "_");
        return s;
    }

    public static String formatFileName(String s) {
        if (isNullOrEmpty(s)) {
            return "";
        }
        s = s.toLowerCase();
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        s = pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d");
        s = s.replaceAll("[^a-zA-Z0-9-.]", "-");
        s = s.replaceAll("-+-", "-");
        return s;
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }

    public static boolean isNotInList(String s, List<String> lst) {
        if (isNullOrEmpty(s)) {
            return false;
        }
        for (String str : lst) {
            if (str.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }

    public static String formatSearch(String s) {
        if (isNullOrEmpty(s)) {
            return "";
        }
        s = s.trim();
        s = s.toLowerCase();
        return s;
    }

    public static String formatSearchElasticLike(String s) {
        if (isNullOrEmpty(s)) {
            return "";
        }
        s = s.trim();
        s = s.toLowerCase();
        return "*" + s + "*";
    }

    public static String encodeJSON(String s) {
        if (isNullOrEmpty(s)) {
            return "";
        }
        return s.replaceAll("\"", "\\\\\"");
    }

    public static String removeEnter(String s) {
        if (isNullOrEmpty(s)) {
            return "";
        }
        return s.replaceAll("\r\n", " ").replaceAll("\n", " ").replaceAll("\\s+", " ");
    }

    public static Boolean hashTagIsValid(String s) {
        if (isNullOrEmpty(s)) {
            return false;
        }
        s = s.trim();
        return s.matches(hashTagRegex);
    }

    public static String randomHexColor() {
        // create random object - reuse this as often as possible
        Random random = new Random();

        // create a big random number - maximum is ffffff (hex) = 16777215 (dez)
        int nextInt = random.nextInt(0xffffff + 1);

        // format it as hexadecimal string (with hashtag and leading zeros)
        String colorCode = String.format("#%06x", nextInt);

        // print it
        System.out.println(colorCode);
        return colorCode;
    }

    public static String getStrFromList(List<Object> objects) {
        if (objects == null || objects.isEmpty())
            return "";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < objects.size(); i++) {
            if (i < objects.size() - 1) {
                if (objects.get(i) != null)
                    stringBuilder.append(objects.get(i).toString() + ", ");
            } else {
                if (objects.get(i) != null)
                    stringBuilder.append(objects.get(i).toString());
            }
        }
        return stringBuilder.toString();
    }

    public static boolean checkExistString(String[] values, String data) {
        boolean contains = Arrays.stream(values).anyMatch(data::equals);
        if (contains) {
            return true;
        }
        return false;
    }

    public static boolean checkExistArray(String[] values, String[] datas) {
        for (String string : datas) {
            if (checkExistString(values, string))
                return true;
        }
        return false;
    }

    public static String join(List<?> list, String glue) {
        StringBuilder line = new StringBuilder();
        for (Object s : list) {
            line.append(s).append(glue);
        }
        return list.size() == 0 ? "" : line.substring(0, line.length() - glue.length());
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static String decodeBase64(String s) {
        return org.apache.commons.codec.binary.StringUtils.newStringUtf8(Base64.decodeBase64(s));
    }

    public static String encodeBase64(String s) {
        return Base64.encodeBase64String(org.apache.commons.codec.binary.StringUtils.getBytesUtf8(s));
    }

    public static String getRandomNumberInts(int min, int max) {
        Random random = new Random();
        return random.ints(min, (max + 1)).findFirst().getAsInt() + "";
    }

    public static String getOTP() {
        return StringUtils.getRandomNumberInts(1000, 9999);
    }

    public static String randomNumberPhone() {
        Random rand = new Random();
        int num2 = rand.nextInt(743);
        int num3 = rand.nextInt(10000);

        DecimalFormat df3 = new DecimalFormat("000"); // 3 zeros
        DecimalFormat df4 = new DecimalFormat("0000"); // 4 zeros

        return "097" + df3.format(num2) + df4.format(num3);
    }

    public static String randomHoTen() {
        String[] dsHo = new String[] { "Trần", "Quan", "Hà", "Giản", "Kim", "Lâm", "Vương", "Ngô", "Hứa", "Trương",
                "Triệu", "Hoàng", "Huỳnh" };
        String[] dsLot = new String[] { "Thị", "Văn", "Gia", "Quỳnh", "Thế", "Tiến", "Ngọc", "Bích" };
        String[] dsTen = new String[] { "Quyên", "Thiện", "Trân", "Phương", "Bích", "Vương", "Toàn", "Trâm", "Châu",
                "Thế", "Phượng", "Vỹ", "Tâm", "Đào", "Mai", "Loan" };
        String ho = dsHo[new Random().nextInt(dsHo.length)];
        String lot = dsLot[new Random().nextInt(dsLot.length)];
        String ten = dsTen[new Random().nextInt(dsTen.length)];
        return ho + " " + lot + " " + ten;
    }


    public static String randomString(int length) {
        boolean useLetters = true;
        boolean useNumbers = true;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }


    public static String encyptPhone(String msisdn) {
        if(msisdn==null) {
            return null;
        }
        if(msisdn.startsWith("84")) {
            msisdn=msisdn.substring(2);
        }
        return Long.toHexString(Long.parseLong(msisdn) - 78712200L);
    }

    public static String decyptPhone(String encrypt) {
        return "84" + (Long.parseLong(encrypt, 16) + 78712200L) + "";
    }

}
