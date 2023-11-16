package nl.chromaticvision.sunshine.impl.module.modules.shulkerpvp;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import nl.chromaticvision.sunshine.impl.module.Category;
import nl.chromaticvision.sunshine.impl.module.Module;
import nl.chromaticvision.sunshine.impl.module.settings.Setting;
import nl.chromaticvision.sunshine.impl.util.minecraft.InventoryUtils;

import java.util.ArrayList;
import java.util.List;

public class Aura32k extends Module {

    public static ItemStack get32kSword() {

        NBTTagCompound enchantmentTag = new NBTTagCompound();

        short enchantmentId = 16;
        int enchantmentLevel = 32767;

        enchantmentTag.setShort("id", enchantmentId);
        enchantmentTag.setInteger("lvl", enchantmentLevel);

        NBTTagCompound itemTag = new NBTTagCompound();
        NBTTagList enchantmentsList = new NBTTagList();

        enchantmentsList.appendTag(enchantmentTag);
        itemTag.setTag("ench", enchantmentsList);

        ItemStack itemStack = new ItemStack(Items.DIAMOND_SWORD);
        itemStack.setTagCompound(itemTag);

        return itemStack;
    }

    public Aura32k() {
        super("32kAura",
                "Attacks super fast players around you when you have 32k in your hand",
                Category.SHULKERPVP,
                get32kSword());
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

    public final Setting<AttackMode> attackMode = register(new Setting<>("AttackMode", AttackMode.VANILLA));
    public final Setting<Boolean> silent = register(new Setting<>("Silent", true));
    public final Setting<Float> reachSetting = register(new Setting<>("Reach", 3.0f, 0.0f, 6.0f));
    public final Setting<Integer> delaySetting = register(new Setting<>("Delay", 5, 0, 20));
    public final Setting<Boolean> rotateSilent = register(new Setting<>("RotateSilent", false));
    public final Setting<Boolean> rotateLook = register(new Setting<>("RotateLook", false));
    public final Setting<TargetMode> targetMode = register(new Setting<>("TargetMode", TargetMode.SINGLE));
    private int superWeaponIndex = -1;
    private long lastAttackTime = 0;
    private List<EntityPlayer> targets = new ArrayList<>();
    private int currentTargetIndex = 0;



    @Override
    public void onEnable() {
        super.onEnable();
        superWeaponIndex = -1;
        targets.clear();
        currentTargetIndex = 0;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        superWeaponIndex = -1;
        targets.clear();
        currentTargetIndex = 0;
    }

    @Override
    public void onFastTick() {

        if (mc.world == null || mc.player == null) return;

        for (int i = 0; i < 9; i++) {
            if (InventoryUtils.isOverEnchantedItem(mc.player.inventory.getStackInSlot(i))) {
                superWeaponIndex = i;
                break;
            }

            superWeaponIndex = -1;
        }

        if (superWeaponIndex != -1) {

            targets.clear();

            for (EntityPlayer target : mc.world.playerEntities) {
                if (!target.getName().equals(mc.getSession().getUsername()) && mc.player.getDistance(target) <= reachSetting.getValue() && target.getHealth() > 0 && !target.isDead) {
                    targets.add(target);
                }
            }

            if (!targets.isEmpty()) {

                EntityPlayer currentTarget = targets.get(currentTargetIndex);

                long currentTime = System.currentTimeMillis();

                if (currentTime - lastAttackTime >= delaySetting.getValue()) {
                    lastAttackTime = currentTime;

                    if (rotateSilent.getValue()) {
                        float[] rotations = getRotations(currentTarget);
                        mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0], rotations[1], mc.player.onGround));
                    }

                    if (silent.getValue()) {
                        if (mc.player.inventory.getStackInSlot(superWeaponIndex).getItem().equals(Items.DIAMOND_SWORD) && InventoryUtils.isOverEnchantedItem(mc.player.inventory.getStackInSlot(superWeaponIndex))) {
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

                    if (targetMode.getValue() == TargetMode.SWITCH) {
                        currentTargetIndex = (currentTargetIndex + 1) % targets.size();
                    }
                }
            }
        }
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