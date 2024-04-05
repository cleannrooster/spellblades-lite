package com.spellbladenext.mixin;

import com.google.common.collect.Maps;
import com.spellbladenext.Spellblades;
import com.spellbladenext.config.ServerConfig;
import com.spellbladenext.items.Orb;
import com.spellbladenext.items.interfaces.PlayerDamageInterface;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Vec3d;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.internals.SpellContainerHelper;
import net.spell_engine.internals.SpellHelper;
import net.spell_engine.internals.SpellRegistry;
import net.spell_engine.internals.WorldScheduler;
import net.spell_engine.particle.ParticleHelper;
import net.spell_engine.utils.TargetHelper;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.SpellDamageSource;
import net.spell_power.api.SpellPower;
import net.spell_power.api.attributes.SpellAttributes;
import net.spell_power.mixin.DamageSourcesAccessor;
import net.spell_engine.internals.casting.SpellCasterEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static com.extraspellattributes.ReabsorptionInit.WARDING;
import static com.spellbladenext.Spellblades.*;
import static net.spell_engine.internals.SpellHelper.ammoForSpell;
import static net.spell_engine.internals.SpellHelper.impactTargetingMode;

@Mixin(value = LivingEntity.class)
public class LivingEntityMixin {

    @Shadow
    private  DefaultedList<ItemStack> syncedHandStacks;
    @Shadow
    private  DefaultedList<ItemStack> syncedArmorStacks;

    private ItemStack getSyncedHandStack(EquipmentSlot slot) {
        return (ItemStack)this.syncedHandStacks.get(slot.getEntitySlotId());
    }

    private ItemStack getSyncedArmorStack(EquipmentSlot slot) {
        return (ItemStack)this.syncedArmorStacks.get(slot.getEntitySlotId());
    }

    @Inject(at = @At("HEAD"), method = "onAttacking", cancellable = true)
    public void onAttackingSpellbladesMixin(Entity target, CallbackInfo info) {
        LivingEntity living = (LivingEntity) (Object) this;

        if (!living.getWorld().isClient() && living instanceof PlayerEntity player && living instanceof SpellCasterEntity caster && living instanceof PlayerDamageInterface damageInterface &&
            SpellContainerHelper.getEquipped(living.getMainHandStack(), player) != null && SpellContainerHelper.getEquipped(player.getMainHandStack(), player).spell_ids != null && SpellContainerHelper.getEquipped(player.getMainHandStack(), player).spell_ids.contains("spellbladenext:deathchill")) {
            if(!FabricLoader.getInstance().isModLoaded("frostiful")) {

                target.setFrozenTicks(target.getFrozenTicks() + 28);
            }
            else{
                target.setFrozenTicks(target.getFrozenTicks() + 28*3*20);

            }
        }
        if (!living.getWorld().isClient() && living instanceof PlayerEntity player && living instanceof SpellCasterEntity caster && living instanceof PlayerDamageInterface damageInterface &&
                SpellContainerHelper.getEquipped(living.getMainHandStack(), player) != null && SpellContainerHelper.getEquipped(player.getMainHandStack(), player).spell_ids != null && SpellContainerHelper.getEquipped(player.getMainHandStack(), player).spell_ids.contains("spellbladenext:combustion")) {
            target.setOnFireFor(2);
        }
    }


    @ModifyVariable(at = @At("HEAD"), method = "applyMovementInput", index = 1)
    public Vec3d applyInputMIX(Vec3d vec3d) {
        LivingEntity living = ((LivingEntity) (Object) this);

        if(living instanceof PlayerEntity player && player instanceof SpellCasterEntity entity && entity.getCurrentSpell() != null && player.getMainHandStack().getItem() instanceof Orb) {
            return vec3d.multiply(6);
        }
        else{
            return vec3d;
        }
    }
    @Inject(at = @At("TAIL"), method = "onEquipStack", cancellable = true)
    public void onEquipStackSpellblades(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack, CallbackInfo info) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if(newStack.getAttributeModifiers(slot).containsKey(WARDING)) {
            if(entity instanceof PlayerDamageInterface playerDamageInterface) {
                playerDamageInterface.resetDamageAbsorbed();
            }
        }
    }
}
