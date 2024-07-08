package JAVAC5.com.WebOrderDoAn.RegexFileName;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtils {
    public static String normalizeFileName(String fileName) {
        // Chuyển đổi các ký tự tiếng Việt có dấu thành không dấu
        String normalized = Normalizer.normalize(fileName, Normalizer.Form.NFD);
        // Loại bỏ các dấu
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        normalized = pattern.matcher(normalized).replaceAll("");
        // Thay thế các ký tự đặc biệt bằng dấu gạch dưới hoặc bỏ đi
        normalized = normalized.replaceAll("[^a-zA-Z0-9.-]", "_");
        return normalized;
    }
}
