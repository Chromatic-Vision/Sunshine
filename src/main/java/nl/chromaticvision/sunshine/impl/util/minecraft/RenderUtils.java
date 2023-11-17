package nl.chromaticvision.sunshine.impl.util.minecraft;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class RenderUtils {

    public static void drawTexturedRect(int x, int y, int textureX, int textureY, int width, int height, int zLevel) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder BufferBuilder = tessellator.getBuffer();

        BufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        BufferBuilder.pos(x, y + height, zLevel)
                .tex((float) (textureX) * (1f / 256),
                        (float) (textureY + height) * (1f / 256)).endVertex();

        BufferBuilder.pos(x + width, y + height, zLevel)
                .tex((float) (textureX + width) * (1f / 256),
                        (float) (textureY + height) * (1f / 256)).endVertex();

        BufferBuilder.pos(x + width, y, zLevel)
                .tex((float) (textureX + width) * (1f / 256),
                (float) (textureY) * (1f / 256)).endVertex();

        BufferBuilder.pos(x, y, zLevel).tex((float) (textureX) * (1f / 256),
                (float) (textureY) * (1f / 256)).endVertex();

        tessellator.draw();
    }
}
