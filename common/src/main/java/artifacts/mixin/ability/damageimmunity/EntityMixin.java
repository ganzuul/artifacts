package artifacts.mixin.ability.damageimmunity;

import artifacts.equipment.EquipmentHelper;
import artifacts.registry.ModDataComponents;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {

    @SuppressWarnings("ConstantConditions")
    @ModifyReturnValue(method = "isInvulnerableTo(Lnet/minecraft/world/damagesource/DamageSource;)Z", at = @At("RETURN"), require = 0)
    public boolean artifacts$isInvulnerableToLegacy(boolean original, DamageSource damageSource) {
        return artifacts$handleDamageImmunity(original, damageSource);
    }

    @SuppressWarnings("ConstantConditions")
    @ModifyReturnValue(method = "isInvulnerableTo(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;)Z", at = @At("RETURN"), require = 0)
    public boolean artifacts$isInvulnerableTo(boolean original, ServerLevel level, DamageSource damageSource) {
        return artifacts$handleDamageImmunity(original, damageSource);
    }

    private boolean artifacts$handleDamageImmunity(boolean original, DamageSource damageSource) {
        if (!original && ((Object) this) instanceof LivingEntity entity && EquipmentHelper.hasAbilityActive(
                ModDataComponents.DAMAGE_IMMUNITY.get(), entity, true,
                ability -> ability.condition().test(entity) && damageSource.is(ability.tag())
        )) {
            return true;
        }
        return original;
    }
}
