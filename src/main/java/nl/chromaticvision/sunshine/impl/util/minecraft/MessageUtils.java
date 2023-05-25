package nl.chromaticvision.sunshine.impl.util.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import nl.chromaticvision.sunshine.Reference;

public class MessageUtils {

    public static final Minecraft mc = Minecraft.getMinecraft();
    public static final String prefix = TextFormatting.YELLOW
            + "["
            + Reference.NAME
            + "] "
            + TextFormatting.RESET;

    public static void sendClientChatMessage(String message) {
        mc.player.sendMessage(new TextComponentString(prefix + message));
    }
}
