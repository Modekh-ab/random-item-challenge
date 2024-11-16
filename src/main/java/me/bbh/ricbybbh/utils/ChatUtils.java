package me.bbh.ricbybbh.utils;


import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import org.bukkit.ChatColor;
import java.nio.charset.StandardCharsets;

public class ChatUtils {
    public ChatUtils() {
    }

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String hideText(String text) {
        StringBuilder output = new StringBuilder();
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        String hex = HexBin.encode(bytes);
        char[] var7;
        int var6 = (var7 = hex.toCharArray()).length;

        for(int var5 = 0; var5 < var6; ++var5) {
            char c = var7[var5];
            output.append('§').append(c);
        }

        return output.toString();
    }
}
