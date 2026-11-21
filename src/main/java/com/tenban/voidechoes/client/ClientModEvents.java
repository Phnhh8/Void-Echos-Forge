package com.tenban.voidechoes.client;

import com.tenban.voidechoes.VoidEchoesMod;
import com.tenban.voidechoes.entity.HerobrineEntity;
import com.tenban.voidechoes.entity.NullEntity;
import com.tenban.voidechoes.init.ModEntities;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = VoidEchoesMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    private static final ResourceLocation HEROBRINE_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(VoidEchoesMod.MODID, "textures/entity/herobrine.png");

    private static final ResourceLocation NULL_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(VoidEchoesMod.MODID, "textures/entity/null.png");

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.HEROBRINE.get(),
                ctx -> new HumanoidMobRenderer<HerobrineEntity, HumanoidModel<HerobrineEntity>>(
                        ctx, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER)), 0.5f) {
                    @Override
                    public ResourceLocation getTextureLocation(HerobrineEntity entity) {
                        return HEROBRINE_TEXTURE;
                    }
                });

        event.registerEntityRenderer(ModEntities.NULL_ENTITY.get(),
                ctx -> new HumanoidMobRenderer<NullEntity, HumanoidModel<NullEntity>>(
                        ctx, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER)), 0.5f) {
                    @Override
                    public ResourceLocation getTextureLocation(NullEntity entity) {
                        return NULL_TEXTURE;
                    }
                });
    }
}
