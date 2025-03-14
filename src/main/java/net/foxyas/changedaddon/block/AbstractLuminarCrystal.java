package net.foxyas.changedaddon.block;

import net.foxyas.changedaddon.entity.AbstractLuminarcticLeopard;
import net.foxyas.changedaddon.init.ChangedAddonModBlocks;
import net.foxyas.changedaddon.init.ChangedAddonModItems;
import net.ltxprogrammer.changed.block.AbstractLatexIceBlock;
import net.ltxprogrammer.changed.block.TransfurCrystalBlock;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedMaterials;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Random;



public class AbstractLuminarCrystal {

    public static abstract class Block extends AbstractLatexIceBlock {
        public static final int MAX_AGE = 3;
        public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
        public static final BooleanProperty DEFROST = BooleanProperty.create("defrost");
        private static final int NEIGHBORS_TO_AGE = 4;
        private static final int NEIGHBORS_TO_MELT = 2;

        public Block() {
            super(Properties.of(Material.ICE_SOLID, MaterialColor.SNOW)
                    .friction(0.98F)
                    .sound(SoundType.AMETHYST)
                    .strength(2.0F, 8.0F).hasPostProcess((blockState, blockGetter, blockPos) -> true)
                    .emissiveRendering((blockState, blockGetter, blockPos) -> true).noOcclusion().randomTicks());
            this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(DEFROST, false));
        }

        @Override
        public boolean skipRendering(BlockState blockState, BlockState blockState1, Direction direction) {
            return blockState1.is(this) ? true : super.skipRendering(blockState, blockState1, direction);
        }

        @Override
        public VoxelShape getVisualShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
            return Shapes.empty();
        }

        @Override
        public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
            return 4;
        }

        @Override
        public void stepOn(Level level, BlockPos blockPos, BlockState blockState, Entity entity) {
            super.stepOn(level, blockPos, blockState, entity);
            triggerCrystal(blockState, level, blockPos, entity);
            
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
            builder.add(AGE, DEFROST);
        }

        @Override
        public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, net.minecraftforge.common.IPlantable plantable) {
		BlockState plant = plantable.getPlant(world, pos.relative(facing));
		if (plant.getBlock() instanceof AbstractLuminarCrystal.CrystalSmall)
			return true;
		else
            return super.canSustainPlant(state, world, pos, facing, plantable);
        }

        @Override
        public void onPlace(BlockState blockstate, Level world, BlockPos pos, BlockState oldState, boolean moving) {
            super.onPlace(blockstate, world, pos, oldState, moving);
        }

        @Override
        public void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull Random random) {
            super.tick(state, level, pos, random);

            if (state.getValue(DEFROST)) {
                if (state.getValue(AGE) < MAX_AGE) {
                    level.setBlockAndUpdate(pos, state.setValue(AGE, state.getValue(AGE) + 1));
                } else {
                    level.destroyBlock(pos, false, null);
                }
                level.scheduleTick(pos, this, 70); //delay de 20 ticks antes de agir
            } else {
                BlockPos above = pos.above();
                if (level.getBlockState(above).is(Blocks.AIR)) {
                    level.setBlock(above, ChangedAddonModBlocks.LUMINAR_CRYSTAL_SMALL.get().defaultBlockState(), 3);
                    level.playSound(null, pos, ChangedSounds.ICE2, SoundSource.BLOCKS, 1.0f, 1.0f);
                    
                }
                //level.scheduleTick(pos, this, 20); //delay de 20 ticks antes de agir
            }
		/*BlockPos above = pos.above();
		if (level.getBlockState(above).is(Blocks.AIR)) {
			level.setBlock(above, ChangedAddonModBlocks.WHITE_WOLF_CRYSTAL_SMALL.get().defaultBlockState(), 3);
			level.playSound(null, pos, ChangedSounds.ICE2, SoundSource.BLOCKS, 1.0f, 1.0f);
		}*/
        }

        @Override
        public void playerDestroy(Level p_49827_, Player p_49828_, BlockPos p_49829_, BlockState p_49830_, @Nullable BlockEntity p_49831_, ItemStack p_49832_) {
            super.playerDestroy(p_49827_, p_49828_, p_49829_, p_49830_, p_49831_, p_49832_);
        }

        @Override
        public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
            List<AbstractLuminarcticLeopard> lumiList = level.getEntitiesOfClass(AbstractLuminarcticLeopard.class, new AABB(pos).inflate(10));

            for (AbstractLuminarcticLeopard boss : lumiList) {
                if (boss.canAttack(player) && boss.hasLineOfSight(player)) { // Verifica se pode atacar e ver o jogador
                    if (player.getLevel() instanceof ServerLevel){
                        player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS,60, 0, false, false, false));
                    }
                    boss.setTarget(player); // Define o jogador como alvo
                }
            }

            return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
        }

         @Override
        public List<ItemStack> getDrops(BlockState blockState, LootContext.Builder lootBuilder) {
            ResourceLocation resourcelocation = this.getLootTable();
            if (resourcelocation == BuiltInLootTables.EMPTY) {
                return Collections.emptyList();
            } else {
                LootContext lootcontext = lootBuilder.withParameter(LootContextParams.BLOCK_STATE, blockState).create(LootContextParamSets.BLOCK);
                ServerLevel serverlevel = lootcontext.getLevel();
                LootTable loottable = serverlevel.getServer().getLootTables().get(resourcelocation);
                return loottable.getRandomItems(lootcontext);
            }

        }

        @Override
        public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
            //super.randomTick(state, level, pos, p_60554_);
            if (state.getValue(DEFROST)){
                level.scheduleTick(pos, this, 70);
            } else {
                for (Direction direction: Direction.values()) {
                    if (random.nextFloat() <= 0.25f){
                        BlockPos relative = pos.relative(direction);
                        if (level.getBlockState(relative).is(Blocks.AIR)) {
                        	BlockState smallCrystalStage = ChangedAddonModBlocks.LUMINAR_CRYSTAL_SMALL.get().defaultBlockState();
                            smallCrystalStage.setValue(AbstractLuminarCrystal.CrystalSmall.FACING, direction);
                            smallCrystalStage.setValue(AbstractLuminarCrystal.CrystalSmall.WATERLOGGED , level.getBlockState(relative).getFluidState().isSourceOfType(Fluids.WATER));
                            
                            level.setBlock(relative, smallCrystalStage, 3);
                            level.playSound(null, pos, ChangedSounds.ICE2, SoundSource.BLOCKS, 1.0f, 0.8f);
                        }
                    }
                }
            }
        }

         private void triggerCrystal(BlockState blockState, Level level, BlockPos position, Entity entity) {

            if (entity instanceof LivingEntity le && !(entity instanceof ChangedEntity) && !le.isDeadOrDying()) {
                if (entity instanceof Player player && (ProcessTransfur.isPlayerTransfurred(player) || player.isCreative()))
                    return;
                level.scheduleTick(position, this, 20);
            }
        }

    }

    public static abstract class CrystalSmall extends TransfurCrystalBlock {
    
	    public static final BooleanProperty HEARTED = BooleanProperty.create("hearted");

        public static final DirectionProperty FACING = BlockStateProperties.FACING;
        public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

        protected static final VoxelShape NORTH_AABB = Block.box(2, 2, 0, 14, 14, 15.0);
        protected static final VoxelShape SOUTH_AABB = Block.box(2, 2, 0, 14, 14, 15.0);
        protected static final VoxelShape EAST_AABB = Block.box(0, 2, 2, 15.0, 14, 14);
        protected static final VoxelShape WEST_AABB = Block.box(0, 2, 2, 15.0, 14, 14);
        protected static final VoxelShape UP_AABB = Block.box(2, 0, 2, 14, 15.0, 14);
        protected static final VoxelShape DOWN_AABB = Block.box(2, 0, 2, 14, 15.0, 14);


        public CrystalSmall() {
            super(ChangedAddonModItems.LUMINAR_CRYSTAL_SHARD,
                    BlockBehaviour.Properties.of(ChangedMaterials.LATEX_CRYSTAL)
                            .sound(SoundType.AMETHYST_CLUSTER)
                            .noOcclusion()
                            .dynamicShape()
                            .strength(1.7F, 2.5F)
                            .hasPostProcess((blockState, blockGetter, blockPos) -> true)
                            .emissiveRendering((blockState, blockGetter, blockPos) -> true)
                            .noOcclusion());
            this.registerDefaultState(this.stateDefinition.any().setValue(HEARTED, false).setValue(FACING, Direction.UP).setValue(WATERLOGGED, false));
        }

        @Override
        public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
            Direction direction = state.getValue(FACING);
            return switch (direction) {
                case NORTH -> NORTH_AABB;
                case SOUTH -> SOUTH_AABB;
                case EAST -> EAST_AABB;
                case WEST -> WEST_AABB;
                case UP -> UP_AABB;
                case DOWN -> DOWN_AABB;
            };
        }

        @Override
        public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
            return Shapes.empty();
        }
        
        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
            builder.add(HEARTED);
            builder.add(FACING);
            builder.add(WATERLOGGED);
        }

        @Override
        public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
            super.entityInside(state, level, pos, entity);
            if (entity instanceof LivingEntity livingEntity && !livingEntity.hasEffect(MobEffects.WITHER)){
                livingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, 20 * 20, 1, false, true, true));
                livingEntity.setTicksFrozen(livingEntity.getTicksFrozen() + (int) (livingEntity.getTicksRequiredToFreeze() * 0.25f));
            }
        }

        @Override
        public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
            return true;
        }

        @Override
        public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
            return 4;
        }

        @Override
        public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
            return Shapes.empty();
        }

        @Override
        public List<ItemStack> getDrops(BlockState blockState, LootContext.Builder lootBuilder) {
            ResourceLocation resourcelocation = this.getLootTable();
            if (resourcelocation == BuiltInLootTables.EMPTY) {
                return Collections.emptyList();
            } else {
                LootContext lootcontext = lootBuilder.withParameter(LootContextParams.BLOCK_STATE, blockState).create(LootContextParamSets.BLOCK);
                ServerLevel serverlevel = lootcontext.getLevel();
                LootTable loottable = serverlevel.getServer().getLootTables().get(resourcelocation);
                return loottable.getRandomItems(lootcontext);
            }

        }

        @Override
        protected boolean mayPlaceOn(BlockState blockState, BlockGetter level, BlockPos blockPos) {
            return blockState.getBlock() == ChangedAddonModBlocks.LUMINAR_CRYSTAL_BLOCK.get();
        }

        @Nullable
        public BlockState getStateForPlacement(BlockPlaceContext p_152019_) {
            LevelAccessor levelaccessor = p_152019_.getLevel();
            BlockPos blockpos = p_152019_.getClickedPos();
            return this.defaultBlockState().setValue(WATERLOGGED, levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER).setValue(FACING, p_152019_.getClickedFace());
        }

        @Override
        public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos blockPos) {
            Direction oppositeDirection = blockState.getValue(FACING).getOpposite();
            BlockState blockStateOn = level.getBlockState(blockPos.relative(oppositeDirection));
            if (!canSupportRigidBlock(level, blockPos.relative(oppositeDirection)))
                return false;
            return blockStateOn.getBlock() == ChangedAddonModBlocks.LUMINAR_CRYSTAL_BLOCK.get();
        }

        @Override
        public @NotNull BlockState rotate(BlockState state, Rotation rot) {
            return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
        }

        @Override
        public @NotNull BlockState mirror(BlockState state, Mirror mirrorIn) {
            return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
        }

        @Override
        public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
            List<AbstractLuminarcticLeopard> lumiList = level.getEntitiesOfClass(AbstractLuminarcticLeopard.class, new AABB(pos).inflate(10));

            for (AbstractLuminarcticLeopard boss : lumiList) {
                if (boss.canAttack(player) && boss.hasLineOfSight(player)) { // Verifica se pode atacar e ver o jogador
                    if (player.getLevel() instanceof ServerLevel){
                        player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS,60, 0, false, false, false));
                    }
                    boss.setTarget(player); // Define o jogador como alvo
                }
            }

            return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
        }
    }
}
