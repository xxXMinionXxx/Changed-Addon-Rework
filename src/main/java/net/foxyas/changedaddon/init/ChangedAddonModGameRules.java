
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.foxyas.changedaddon.init;

import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.level.GameRules;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonModGameRules {
	public static final GameRules.Key<GameRules.BooleanValue> FIGHTTOKEEPCONSCIOUSNESS = GameRules.register("fighttokeepconsciousness", GameRules.Category.PLAYER, GameRules.BooleanValue.create(false));
	public static final GameRules.Key<GameRules.BooleanValue> CHANGED_ADDON_DUCT_MECANIC = GameRules.register("changedAddonDuctMecanic", GameRules.Category.PLAYER, GameRules.BooleanValue.create(false));
}