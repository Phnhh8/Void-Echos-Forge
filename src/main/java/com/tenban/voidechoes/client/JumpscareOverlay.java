package com.tenban.voidechoes.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.tenban.voidechoes.VoidEchoesMod;
import com.tenban.voidechoes.entity.HerobrineEntity;
import com.tenban.voidechoes.entity.NullEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = VoidEchoesMod.MODID, value = Dist.CLIENT)
public class JumpscareOverlay {
    private static final ResourceLocation JUMPSCARE_TEX =
            new ResourceLocation(VoidEchoesMod.MODID, "textures/gui/jumpscare.png");

    private static int jumpscareTimer = 0; // Thời gian hiện ảnh (ticks)
    private static int cooldown = 0;       // Thời gian hồi chiêu giữa 2 lần hù

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.level == null) return;

        if (jumpscareTimer > 0) jumpscareTimer--;
        if (cooldown > 0) {
            cooldown--;
            return;
        }

        // Quét xem có Herobrine hoặc Null ở sát bên trong bán kính 4.5 block không
        List<Entity> nearby = mc.level.getEntities(player, player.getBoundingBox().inflate(4.5D),
                e -> e instanceof HerobrineEntity || e instanceof NullEntity);

        if (!nearby.isEmpty()) {
            jumpscareTimer = 18; // Hiện hình ảnh jumpscare kín màn hình trong gần 1 giây
            cooldown = 160;      // Hồi chiêu 8 giây để không bị spam liên tục
            player.playSound(SoundEvents.ENDERMAN_SCREAM, 3.0F, 0.4F);
            player.playSound(SoundEvents.GHAST_SCREAM, 3.0F, 0.5F);
        }
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiOverlayEvent.Post event) {
        if (jumpscareTimer <= 0) return;

        Minecraft mc = Minecraft.getInstance();
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        // Hiệu ứng rung lắc màn hình khi jumpscare
        int shakeX = (int) ((Math.random() - 0.5) * 20);
        int shakeY = (int) ((Math.random() - 0.5) * 20);

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        // Vẽ ảnh phủ kín 100% màn hình
        event.getGuiGraphics().blit(JUMPSCARE_TEX, shakeX - 10, shakeY - 10, 0, 0, width + 20, height + 20, width + 20, height + 20);

        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }
}
