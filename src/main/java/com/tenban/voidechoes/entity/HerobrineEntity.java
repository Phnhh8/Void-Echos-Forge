package com.tenban.voidechoes.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class HerobrineEntity extends Monster {
    private int actionCooldown = 0;

    public HerobrineEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.4D, false));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 64.0F));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.9D));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && this.getTarget() instanceof Player player) {
            if (actionCooldown > 0) {
                actionCooldown--;
                return;
            }

            double distance = this.distanceTo(player);
            // Nếu đứng cách xa người chơi từ 10 đến 40 block -> Dịch chuyển ngay sau lưng để hù
            if (distance > 10.0D && distance < 40.0D) {
                Vec3 lookVec = player.getLookAngle();
                double targetX = player.getX() - lookVec.x * 2.0D;
                double targetZ = player.getZ() - lookVec.z * 2.0D;
                double targetY = player.getY();

                this.teleportTo(targetX, targetY, targetZ);

                // Phát tiếng hét Enderman + tiếng sét nổ lớn tạo cảm giác jumpscare
                this.level().playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_SCREAM, SoundSource.HOSTILE, 2.0F, 0.5F);
                this.level().playSound(null, player.blockPosition(), SoundEvents.AMBIENT_CAVE.get(), SoundSource.HOSTILE, 2.0F, 0.5F);

                // Gây hiệu ứng tối sầm màn hình (Darkness) và làm chậm trong 4 giây
                player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 80, 1));
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1));

                // Giáng sấm sét cảnh cáo gần đó
                if (this.level() instanceof ServerLevel serverLevel && this.random.nextFloat() < 0.4F) {
                    LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(serverLevel);
                    if (lightning != null) {
                        lightning.moveTo(BlockPos.containing(targetX, targetY, targetZ).getCenter());
                        lightning.setVisualOnly(true);
                        serverLevel.addFreshEntity(lightning);
                    }
                }

                actionCooldown = 140; // Hồi chiêu 7 giây (20 ticks = 1 giây)
            }
        }
    }
}
