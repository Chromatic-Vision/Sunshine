package nl.chromaticvision.sunshine.mixin.mixins;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public class MixinGuiContainer {

    @Inject(method = "handleMouseClick",
            at = @At("RETURN")
    )
    public void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type, CallbackInfo ci) {
        System.out.println("slotid: " + slotId);
        System.out.println("mousebutton: " + mouseButton);
        System.out.println("clicktype: " + type.name());
    }
}
