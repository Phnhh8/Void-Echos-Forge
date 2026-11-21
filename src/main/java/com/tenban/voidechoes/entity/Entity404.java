package com.tenban.voidechoes.entity;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class Entity404 extends Monster {
    private int glitchCooldown = 0;

    public Entity404(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.5D, false));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 64.0F));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && this.getTarget() instanceof Player player) {
            if (glitchCooldown > 0) {
                glitchCooldown--;
                return;
            }

            // Kỹ năng "Lỗi 404": Dịch chuyển ngẫu nhiên quanh người chơi + phát tiếng nhiễu sóng + làm chao đảo tầm nhìn
            if (this.distanceTo(player) < 25.0D) {
                double offsetX = (this.random.nextDouble() - 0.5D) * 6.0D;
                double offsetZ = (this.random.nextDouble() - 0.5D) * 6.0D;
                this.teleportTo(player.getX() + offsetX, player.getY(), player.getZ() + offsetZ);

                this.level().playSound(null, player.blockPosition(), SoundEvents.PORTAL_TRIGGER, SoundSource.HOSTILE, 2.0F, 0.2F);
                this.level().playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.HOSTILE, 2.0F, 0.3F);

                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 1));
                player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60, 0));

                glitchCooldown = 100; // Hồi chiêu 5 giây
            }
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hit = super.doHurtTarget(target);
        if (hit && target instanceof LivingEntity living) {
            // Đánh trúng gây làm chậm cực mạnh và mù tạm thời
            living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 3));
            living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 40, 0));
        }
        return hit;
    }
}
