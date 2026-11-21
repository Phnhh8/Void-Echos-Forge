package com.tenban.voidechoes.init;

import com.tenban.voidechoes.VoidEchoesMod;
import com.tenban.voidechoes.entity.Entity404;
import com.tenban.voidechoes.entity.HerobrineEntity;
import com.tenban.voidechoes.entity.NullEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, VoidEchoesMod.MODID);

    public static final RegistryObject<EntityType<HerobrineEntity>> HEROBRINE = ENTITIES.register("herobrine",
            () -> EntityType.Builder.of(HerobrineEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.8F)
                    .build("herobrine"));

    public static final RegistryObject<EntityType<NullEntity>> NULL_ENTITY = ENTITIES.register("null_entity",
            () -> EntityType.Builder.of(NullEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.8F)
                    .build("null_entity"));

    public static final RegistryObject<EntityType<Entity404>> ENTITY_404 = ENTITIES.register("entity_404",
            () -> EntityType.Builder.of(Entity404::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.8F)
                    .build("entity_404"));

    public static AttributeSupplier.Builder createHorrorAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 120.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.36D)
                .add(Attributes.ATTACK_DAMAGE, 14.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D);
    }
}
