package nl.chromaticvision.sunshine.impl.module.modules.shulkerpvp;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import nl.chromaticvision.sunshine.impl.module.Category;
import nl.chromaticvision.sunshine.impl.module.Module;
import nl.chromaticvision.sunshine.impl.module.settings.Setting;
import nl.chromaticvision.sunshine.impl.util.minecraft.InventoryUtils;

import java.util.ArrayList;
import java.util.List;

public class Aura32k extends Module {

    public Aura32k() {
        super("32kAura", "Attacks super fast players around you when you have 32k in your hand", Category.COMBAT);
    }

    enum AttackMode {
        VANILLA,
        PACKET,
        BOTH
    }

    enum TargetMode {
        SINGLE,
        SWITCH
    }

    enum Delay {
        NONE,
        ONE,
        FIVE,
        TEN,
        FIVETEEN
    }

    public final Setting<AttackMode> attackMode = register(new Setting<>("AttackMode", AttackMode.VANILLA));
    public final Setting<Boolean> silent = register(new Setting<>("Silent", true));
    public final Setting<Float> reachSetting = register(new Setting<>("Reach", 3.0f, 0.0f, 6.0f));
    public final Setting<Delay> delaySetting = register(new Setting<>("Delay", Delay.NONE));
    public final Setting<Boolean> rotateSilent = register(new Setting<>("RotateSilent", false));
    public final Setting<Boolean> rotateLook = register(new Setting<>("RotateLook", false));
    public final Setting<Boolean> drawCircle = register(new Setting<>("DrawCircle", true));
    public final Setting<TargetMode> targetMode = register(new Setting<>("TargetMode", TargetMode.SINGLE));
    private int superWeaponIndex = -1;
    private long lastAttackTime = 0;
    private int attackDelay = 0;
    private List<EntityPlayer> targets = new ArrayList<>();
    private int currentTargetIndex = 0;
    private EntityPlayer targetToDraw;



    @Override
    public void onEnable() {
        super.onEnable();
        superWeaponIndex = -1;
        targets.clear();
        currentTargetIndex = 0;
        targetToDraw = null;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        superWeaponIndex = -1;
        targets.clear();
        currentTargetIndex = 0;
        targetToDraw = null;
    }

    @Override
    public void onFastTick() {

        if (mc.world == null || mc.player == null) return;

        if (this.isEnabled()) {
            if (mc.world == null || mc.player == null) return;

            switch (delaySetting.getValue()) {
                case ONE:
                    attackDelay = 1;
                    break;
                case FIVE:
                    attackDelay = 5;
                    break;
                case TEN:
                    attackDelay = 10;
                    break;
                case FIVETEEN:
                    attackDelay = 15;
                    break;
                default:
                    attackDelay = 0;
                    break;
            }

            reachSetting.setValue(Math.max(0.0f, reachSetting.getValue()));

            for (int i = 0; i < 9; i++) {
                if (InventoryUtils.isOverEnchantedItem(mc.player.inventory.getStackInSlot(i))) {
                    superWeaponIndex = i;
                    break;
                }

                System.out.println("not found");
                superWeaponIndex = -1;
            }

            if (superWeaponIndex != -1) {
                targets.clear();
                for (EntityPlayer target : mc.world.playerEntities) {
                    if (!target.getName().equals(mc.getSession().getUsername()) && mc.player.getDistance(target) <= reachSetting.getValue() && target.getHealth() > 0 && !target.isDead) {
                        targets.add(target);
                        System.out.println("target found");
                    }
                }

                if (!targets.isEmpty()) {
                    EntityPlayer currentTarget = targets.get(currentTargetIndex);

                    long currentTime = System.currentTimeMillis();

                    if (currentTime - lastAttackTime >= attackDelay) {
                        lastAttackTime = currentTime;

                        if (rotateSilent.getValue()) {
                            float[] rotations = getRotations(currentTarget);
                            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0], rotations[1], mc.player.onGround));
                        }

                        if (silent.getValue()) {
                            if (mc.player.inventory.getStackInSlot(superWeaponIndex).getItem().equals(Items.DIAMOND_SWORD) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, mc.player.inventory.getStackInSlot(superWeaponIndex)) >= Short.MAX_VALUE) {
                                mc.player.connection.sendPacket(new CPacketHeldItemChange(superWeaponIndex));
                                mc.playerController.updateController();
                                mc.player.connection.sendPacket(new CPacketUseEntity(currentTarget));
                                mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
                                mc.player.swingArm(EnumHand.MAIN_HAND);
                            }
                        } else {
                            ItemStack crItemStack = mc.player.inventory.getCurrentItem();

                            if (crItemStack.getItem().equals(Items.DIAMOND_SWORD) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, crItemStack) >= Short.MAX_VALUE) {
                                mc.player.connection.sendPacket(new CPacketUseEntity(currentTarget));
                                mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
                                mc.player.swingArm(EnumHand.MAIN_HAND);
                            }
                        }

                        if (rotateLook.getValue()) {
                            lookAtTarget(currentTarget);
                        }

                        if (drawCircle.getValue()) {
                            targetToDraw = currentTarget;
                        } else {
                            targetToDraw = null;
                        }

                        if (targetMode.getValue() == TargetMode.SWITCH) {
                            currentTargetIndex = (currentTargetIndex + 1) % targets.size();
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (this.isEnabled()) {
            if (targetToDraw != null && event.getType() == RenderGameOverlayEvent.ElementType.HELMET) {
                drawCircleOnTargetHead(targetToDraw);
            }
        }
    }

    private void drawCircleOnTargetHead(EntityPlayer target) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        double x = scaledResolution.getScaledWidth() / 2.0;
        double y = scaledResolution.getScaledHeight() / 2.0;
        double radius = 15.0;

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();

        GlStateManager.color(1.0f, 0.0f, 0.0f, 0.5f);
        GlStateManager.glLineWidth(2.0f);

        for (int i = 0; i < 360; i++) {
            double angle = Math.toRadians(i);
            double x1 = x + radius * Math.cos(angle);
            double y1 = y + radius * Math.sin(angle);
            double x2 = x + radius * Math.cos(Math.toRadians(i + 1));
            double y2 = y + radius * Math.sin(Math.toRadians(i + 1));
            drawLine(x1, y1, x2, y2);
        }

        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    private void drawLine(double x1, double y1, double x2, double y2) {
        GlStateManager.glBegin(1);
//        GlStateManager.glVertex2d(x1, y1);
//        GlStateManager.glVertex2d(x2, y2);
        GlStateManager.glEnd();
    }

    private float[] getRotations(EntityPlayer target) {
        double x = target.posX - mc.player.posX;
        double y = (target.posY + target.getEyeHeight()) - (mc.player.posY + mc.player.getEyeHeight());
        double z = target.posZ - mc.player.posZ;
        double d1 = Math.sqrt(x * x + z * z);
        float yaw = (float) Math.toDegrees(Math.atan2(z, x)) - 90.0f;
        float pitch = (float) -Math.toDegrees(Math.atan2(y, d1));
        return new float[]{yaw, pitch};
    }

    private void lookAtTarget(EntityPlayer target) {
        double x = target.posX - mc.player.posX;
        double y = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0 - mc.player.posY + mc.player.getEyeHeight();
        double z = target.posZ - mc.player.posZ;
        double d1 = Math.sqrt(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0f;
        float pitch = (float) (Math.atan2(y, d1) * 180.0D / Math.PI);
        mc.player.rotationYaw = yaw;
        mc.player.rotationPitch = pitch;
    }
}