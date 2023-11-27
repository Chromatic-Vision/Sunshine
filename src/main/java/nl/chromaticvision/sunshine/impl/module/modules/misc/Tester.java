package nl.chromaticvision.sunshine.impl.module.modules.misc;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;
import nl.chromaticvision.sunshine.impl.module.Category;
import nl.chromaticvision.sunshine.impl.module.Module;
import nl.chromaticvision.sunshine.impl.util.minecraft.MessageUtils;

import java.util.Random;

public class Tester extends Module {

    public Tester() {
        super("Tester",
                "test",
                Category.MISC,
                new ItemStack(Blocks.ENCHANTING_TABLE)
        );
    }

    @Override
    public void onEnable() {
        super.onEnable();

        float val = new Random().nextFloat() * 180f;

        System.out.println(val);

        mc.player.connection.sendPacket(new CPacketPlayer.Rotation(val, mc.player.rotationPitch, mc.player.onGround));
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
