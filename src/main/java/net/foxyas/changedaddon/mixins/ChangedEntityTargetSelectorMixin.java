package net.foxyas.changedaddon.mixins;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.item.DarkLatexCoatItem;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ChangedEntity.class,remap = false)
public class ChangedEntityTargetSelectorMixin {

    @Inject(method = "targetSelectorTest", at = @At("HEAD"), cancellable = true)
    private void CancelTarget(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir){
        ItemStack Head = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack Chest = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
        if (isDarkLatexCoat(Head) && isDarkLatexCoat(Chest)){
            //ChangedAddonMod.LOGGER.info("Evento cancelado: capacete e peitoral detectados");
            cir.setReturnValue(false);
        } else if (isDarkLatexCoat(Head) ^ isDarkLatexCoat(Chest)) {
            if (livingEntity.distanceTo((ChangedEntity) (Object) this) >= 4){
                //ChangedAddonMod.LOGGER.info("Evento cancelado: item parcial detectado, distância > 4");
                cir.setReturnValue(false);
            }
        }
    }
    
    private static boolean isDarkLatexCoat(ItemStack itemStack) {
        return itemStack != null 
                && !itemStack.isEmpty() 
                && itemStack.getItem() instanceof DarkLatexCoatItem;
    }
}
