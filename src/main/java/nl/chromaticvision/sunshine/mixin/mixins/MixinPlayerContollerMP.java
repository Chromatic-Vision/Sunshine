package nl.chromaticvision.sunshine.mixin.mixins;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerContollerMP {


    @Inject(method = "processRightClickBlock",
            at = @At("HEAD")
    )
    public void onRightClickBlock(EntityPlayerSP player, WorldClient worldIn, BlockPos pos, EnumFacing direction, Vec3d vec, EnumHand hand, CallbackInfoReturnable<EnumActionResult> cir) {
        System.out.println("Direction: " + direction.getName());
        System.out.println("BlockPos: " + pos.getX() + "/" + pos.getY() + "/" + pos.getZ());
        System.out.println("Vec: " + vec.toString());
    }

    @Inject(method = "processRightClick",
            at = @At("HEAD")
    )
    public void onRightClick(EntityPlayer player, World worldIn, EnumHand hand, CallbackInfoReturnable<EnumActionResult> cir) {
        System.out.println("Callback: " + cir.toString());
    }
}
