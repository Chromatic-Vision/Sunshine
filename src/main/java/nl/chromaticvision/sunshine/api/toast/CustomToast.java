package nl.chromaticvision.sunshine.api.toast;

import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import java.util.Iterator;
import java.util.List;

public class CustomToast implements IToast {

    public String title;
    public String subtitle;
    public ItemStack displayStack;
    public long length;

    public CustomToast(String title, String subtitle, ItemStack displayStack, long length) {
        this.title = title;
        this.subtitle = subtitle;
        this.displayStack = displayStack;
        this.length = length;
    }

    @Override
    public Visibility draw(GuiToast guiToast, long delta) {
        guiToast.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);

        GlStateManager.color(1.0F, 1.0F, 1.0F);
        guiToast.drawTexturedModalRect(0, 0, 0, 0, 160, 32);

        if (title != null && subtitle != null && displayStack != null) {

            List<String> list = guiToast.getMinecraft().fontRenderer.listFormattedStringToWidth(subtitle, 125);

            int i = 16776960;

            if (list.size() == 1) {
                guiToast.getMinecraft().fontRenderer.drawString(title, 30, 7, i | -16777216);
                guiToast.getMinecraft().fontRenderer.drawString(subtitle, 30, 18, -1);
            } else {
                int i1;
                if (delta < length / 2) {
                    i1 = MathHelper.floor(MathHelper.clamp((float) (length / 2 - delta) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
                    guiToast.getMinecraft().fontRenderer.drawString(title, 30, 11, i | i1);
                } else {
                    i1 = MathHelper.floor(MathHelper.clamp((float) (delta - length / 2) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
                    int l = 16 - list.size() * guiToast.getMinecraft().fontRenderer.FONT_HEIGHT / 2;

                    for (Iterator<String> it = list.iterator(); it.hasNext(); l += guiToast.getMinecraft().fontRenderer.FONT_HEIGHT) {
                        String s = it.next();
                        guiToast.getMinecraft().fontRenderer.drawString(s, 30, l, 16777215 | i1);
                    }
                }
            }

            RenderHelper.enableGUIStandardItemLighting();
            if (displayStack != null) guiToast.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI((EntityLivingBase) null, displayStack, 8, 8);

            return delta >= length ? Visibility.HIDE : Visibility.SHOW;
        } else {
            return Visibility.HIDE;
        }
    }
}
