package binary404.mystica.common.core.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import java.util.Arrays;
import java.util.List;

import static net.minecraft.util.text.TextFormatting.fromFormattingCode;

public class StringUtil {

    public static List<String> listFormattedStringToWidth(String str, int wrapWidth) {
        return Arrays.asList(wrapFormattedStringToWidth(str, wrapWidth).split("\n"));
    }

    public static String wrapFormattedStringToWidth(String str, int wrapWidth) {
        String s;
        String s1;
        for (s = ""; !str.isEmpty(); s = s + s1 + "\n") {
            int i = sizeStringToWidth(str, wrapWidth);
            if (str.length() <= i) {
                return s + str;
            }

            s1 = str.substring(0, i);
            char c0 = str.charAt(i);
            boolean flag = c0 == ' ' || c0 == '\n';
            str = getFormatString(s1) + str.substring(i + (flag ? 1 : 0));
        }

        return s;
    }

    public static String getFormatString(String stringIn) {
        StringBuilder stringbuilder = new StringBuilder();
        int i = -1;
        int j = stringIn.length();

        while ((i = stringIn.indexOf(167, i + 1)) != -1) {
            if (i < j - 1) {
                TextFormatting textformatting = fromFormattingCode(stringIn.charAt(i + 1));
                if (textformatting != null) {
                    if (!textformatting.isFancyStyling()) {
                        stringbuilder.setLength(0);
                    }

                    if (textformatting != TextFormatting.RESET) {
                        stringbuilder.append((Object) textformatting);
                    }
                }
            }
        }

        return stringbuilder.toString();
    }

    public static int sizeStringToWidth(String str, int wrapWidth) {
        int i = Math.max(1, wrapWidth);
        int j = str.length();
        float f = 0.0F;
        int k = 0;
        int l = -1;
        boolean flag = false;

        for (boolean flag1 = true; k < j; ++k) {
            char c0 = str.charAt(k);
            switch (c0) {
                case '\n':
                    --k;
                    break;
                case ' ':
                    l = k;
                default:
                    if (f != 0.0F) {
                        flag1 = false;
                    }

                    f += getCharWidth(c0);
                    if (flag) {
                        ++f;
                    }
                    break;
                case '\u00a7':
                    if (k < j - 1) {
                        ++k;
                        TextFormatting textformatting = TextFormatting.fromFormattingCode(str.charAt(k));
                        if (textformatting == TextFormatting.BOLD) {
                            flag = true;
                        } else if (textformatting != null && !textformatting.isFancyStyling()) {
                            flag = false;
                        }
                    }
            }

            if (c0 == '\n') {
                ++k;
                l = k;
                break;
            }

            if (f > (float) i) {
                if (flag1) {
                    ++k;
                }
                break;
            }
        }

        return k != j && l != -1 && l < k ? l : k;
    }

    public static float getCharWidth(char character) {
        return character == 167 ? 0.0F : Minecraft.getInstance().fontRenderer.font.apply(Style.DEFAULT_FONT).func_238557_a_(character).getAdvance(false);
    }

}
