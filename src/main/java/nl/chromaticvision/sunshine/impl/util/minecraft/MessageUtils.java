package nl.chromaticvision.sunshine.impl.util.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import nl.chromaticvision.sunshine.Reference;
import nl.chromaticvision.sunshine.api.toast.CustomToast;

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

    public static void addNotificationToast(String title, String subTitle) {
        mc.getToastGui().add(new SystemToast(SystemToast.Type.TUTORIAL_HINT,
                new TextComponentString(title),
                new TextComponentString(subTitle)));
    }

    public static void addCustomAdvancementTypeToast(String title, String subTitle, ItemStack displayStack, long delta) {
        mc.getToastGui().add(new CustomToast(title, subTitle, displayStack, delta));
    }
}
