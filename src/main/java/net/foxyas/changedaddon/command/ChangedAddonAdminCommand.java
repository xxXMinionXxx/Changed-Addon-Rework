
package net.foxyas.changedaddon.command;

import org.checkerframework.checker.units.qual.s;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.common.util.FakePlayerFactory;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Direction;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.Commands;

import net.foxyas.changedaddon.procedures.SetmaxTransfurToleranceProcedure;
import net.foxyas.changedaddon.procedures.SetTransfurProgressCommandProcedure;
import net.foxyas.changedaddon.procedures.SetPlayerTransfurProgressCommandProcedure;
import net.foxyas.changedaddon.procedures.SetExp10BossPermProcedure;
import net.foxyas.changedaddon.procedures.SetDefaultValueProcedure;
import net.foxyas.changedaddon.procedures.SetBossPermProcedure;
import net.foxyas.changedaddon.procedures.GetmaxTransfurToleranceProcedure;
import net.foxyas.changedaddon.procedures.GetExp10BossPermProcedure;
import net.foxyas.changedaddon.procedures.GetBossPermProcedure;
import net.foxyas.changedaddon.procedures.AddTransfurProgressCommandProcedure;
import net.foxyas.changedaddon.procedures.AddPlayerTransfurProgressProcedure;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;

@Mod.EventBusSubscriber
public class ChangedAddonAdminCommand {
	@SubscribeEvent
	public static void registerCommand(RegisterCommandsEvent event) {
		event.getDispatcher().register(Commands.literal("changed-addon-admin").requires(s -> s.hasPermission(2))
				.then(Commands.literal("allow_boss_transfur").then(Commands.literal("Ket_Exp_009").then(Commands.literal("get").then(Commands.argument("player", EntityArgument.player()).executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);
					Direction direction = entity.getDirection();

					GetBossPermProcedure.execute(arguments, entity);
					return 0;
				}))).then(Commands.literal("set").then(Commands.argument("target", EntityArgument.player()).then(Commands.argument("set", BoolArgumentType.bool()).executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);
					Direction direction = entity.getDirection();

					SetBossPermProcedure.execute(arguments, entity);
					return 0;
				}))))).then(Commands.literal("Exp_10").then(Commands.literal("get").then(Commands.argument("player", EntityArgument.player()).executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);
					Direction direction = entity.getDirection();

					GetExp10BossPermProcedure.execute(arguments, entity);
					return 0;
				}))).then(Commands.literal("set").then(Commands.argument("target", EntityArgument.player()).then(Commands.argument("set", BoolArgumentType.bool()).executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);
					Direction direction = entity.getDirection();

					SetExp10BossPermProcedure.execute(arguments, entity);
					return 0;
				})))))).then(Commands.literal("SetTransfurProgress").then(Commands.argument("Number", DoubleArgumentType.doubleArg()).then(Commands.literal("add").executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);
					Direction direction = entity.getDirection();

					AddTransfurProgressCommandProcedure.execute(arguments, entity);
					return 0;
				})).then(Commands.literal("set").executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);
					Direction direction = entity.getDirection();

					SetTransfurProgressCommandProcedure.execute(arguments, entity);
					return 0;
				})))).then(Commands.literal("SetPlayerTransfurProgress").then(Commands.argument("Target", EntityArgument.player()).then(Commands.argument("Number", DoubleArgumentType.doubleArg()).then(Commands.literal("add").executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);
					Direction direction = entity.getDirection();

					AddPlayerTransfurProgressProcedure.execute(arguments, entity);
					return 0;
				})).then(Commands.literal("set").executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);
					Direction direction = entity.getDirection();

					SetPlayerTransfurProgressCommandProcedure.execute(arguments, entity);
					return 0;
				}))))).then(Commands.literal("SetMaxTransfurTolerance").then(Commands.argument("target", EntityArgument.player()).then(Commands.argument("MaxNumber", DoubleArgumentType.doubleArg(0.1)).executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);
					Direction direction = entity.getDirection();

					SetmaxTransfurToleranceProcedure.execute(arguments, entity);
					return 0;
				})).then(Commands.literal("Default").executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);
					Direction direction = entity.getDirection();

					SetDefaultValueProcedure.execute(arguments, entity);
					return 0;
				})))).then(Commands.literal("GetMaxTransfurTolerance").executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);
					Direction direction = entity.getDirection();

					GetmaxTransfurToleranceProcedure.execute(entity);
					return 0;
				})));
	}
}
