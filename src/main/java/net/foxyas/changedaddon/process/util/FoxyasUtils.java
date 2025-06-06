package net.foxyas.changedaddon.process.util;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public class FoxyasUtils {

    public static BlockHitResult manualRaycastIgnoringBlocks(Level level, Entity entity, double maxDistance, Set<Block> ignoredBlocks) {
        Vec3 start = entity.getEyePosition(1.0F);
        Vec3 lookVec = entity.getViewVector(1.0F);
        Vec3 end = start.add(lookVec.scale(maxDistance));

        double stepSize = 0.1;
        Vec3 currentPos = start;
        int steps = (int) (maxDistance / stepSize);

        for (int i = 0; i < steps; i++) {
            BlockPos blockPos = new BlockPos(currentPos);
            BlockState state = level.getBlockState(blockPos);

            if (!ignoredBlocks.contains(state.getBlock()) && state.isSolidRender(level, blockPos)) {
                Direction direction = Direction.getNearest(lookVec.x, lookVec.y, lookVec.z);
                //Vec3 hitPos = applyOffset(currentPos, direction, -0.05D);
                currentPos = applyOffset(currentPos, direction, 0.05D);
                return new BlockHitResult(currentPos, direction, blockPos, true);
            }

            currentPos = currentPos.add(lookVec.scale(stepSize));
        }

        Direction missDirection = Direction.getNearest(lookVec.x, lookVec.y, lookVec.z);
        Vec3 missPos = applyOffset(end, missDirection, -0.05D);
        return BlockHitResult.miss(missPos, missDirection, new BlockPos(end));
    }

    // Utilitário para aplicar deslocamento da face atingida
    public static Vec3 applyOffset(Vec3 hitPos, Direction face, double offset) {
        return hitPos.subtract(
                face.getStepX() * offset,
                face.getStepY() * offset,
                face.getStepZ() * offset
        );
    }

    public static Vec3 getRelativePositionEyes(Entity entity, float deltaX, float deltaY, float deltaZ) {
        // Obtém os vetores locais da entidade
        Vec3 forward = entity.getViewVector(1.0f); // Direção que a entidade está olhando (Surge)
        Vec3 up = entity.getUpVector(1.0F); // Vetor "para cima" da entidade (Heave)
        Vec3 right = forward.cross(up).normalize(); // Calcula o vetor para a direita (Sway)

        // Combina os deslocamentos locais
        Vec3 offset = right.scale(-deltaX) // Sway (esquerda/direita)
                .add(up.scale(deltaY)) // Heave (cima/baixo)
                .add(forward.scale(deltaZ)); // Surge (frente/trás)

        // Retorna a nova posição baseada no deslocamento local
        return entity.getEyePosition().add(offset);
    }

    public static Vec3 getRelativePositionEyes(Entity entity, double deltaX, double deltaY, double deltaZ) {
        // Obtém os vetores locais da entidade
        Vec3 forward = entity.getViewVector(1.0f); // Direção que a entidade está olhando (Surge)
        Vec3 up = entity.getUpVector(1.0F); // Vetor "para cima" da entidade (Heave)
        Vec3 right = forward.cross(up).normalize(); // Calcula o vetor para a direita (Sway)

        // Combina os deslocamentos locais
        Vec3 offset = right.scale(-deltaX) // Sway (esquerda/direita)
                .add(up.scale(deltaY)) // Heave (cima/baixo)
                .add(forward.scale(deltaZ)); // Surge (frente/trás)

        // Retorna a nova posição baseada no deslocamento local
        return entity.getEyePosition().add(offset);
    }

    public static Vec3 getRelativePositionEyes(Entity entity, Vec3 vec3) {
        double deltaX, deltaY, deltaZ;
        deltaX = vec3.x;
        deltaY = vec3.y;
        deltaZ = vec3.z;
        // Obtém os vetores locais da entidade
        Vec3 forward = entity.getViewVector(1.0f); // Direção que a entidade está olhando (Surge)
        Vec3 up = entity.getUpVector(1.0F); // Vetor "para cima" da entidade (Heave)
        Vec3 right = forward.cross(up).normalize(); // Calcula o vetor para a direita (Sway)

        // Combina os deslocamentos locais
        Vec3 offset = right.scale(-deltaX) // Sway (esquerda/direita)
                .add(up.scale(deltaY)) // Heave (cima/baixo)
                .add(forward.scale(deltaZ)); // Surge (frente/trás)

        // Retorna a nova posição baseada no deslocamento local
        return entity.getEyePosition().add(offset);
    }

    public static Vec3 getRelativePositionCommandStyle(Entity entity, double deltaX, double deltaY, double deltaZ) {
        Vec2 rotation = entity.getRotationVector(); // Obtém a rotação (Yaw e Pitch)
        Vec3 position = entity.position(); // Posição atual da entidade

        // Cálculo dos vetores locais
        float yawRad = (rotation.y + 90.0F) * ((float) Math.PI / 180F);
        float pitchRad = -rotation.x * ((float) Math.PI / 180F);
        float pitchRad90 = (-rotation.x + 90.0F) * ((float) Math.PI / 180F);

        float cosYaw = Mth.cos(yawRad);
        float sinYaw = Mth.sin(yawRad);
        float cosPitch = Mth.cos(pitchRad);
        float sinPitch = Mth.sin(pitchRad);
        float cosPitch90 = Mth.cos(pitchRad90);
        float sinPitch90 = Mth.sin(pitchRad90);

        Vec3 forward = new Vec3(cosYaw * cosPitch, sinPitch, sinYaw * cosPitch); // Vetor para frente (Surge)
        Vec3 up = new Vec3(cosYaw * cosPitch90, sinPitch90, sinYaw * cosPitch90); // Vetor para cima (Heave)
        Vec3 right = forward.cross(up).scale(-1.0D); // Vetor para direita (Sway)

        // Calcula nova posição baseada nos deslocamentos locais
        double newX = forward.x * deltaZ + up.x * deltaY + right.x * deltaX;
        double newY = forward.y * deltaZ + up.y * deltaY + right.y * deltaX;
        double newZ = forward.z * deltaZ + up.z * deltaY + right.z * deltaX;

        return new Vec3(position.x + newX, position.y + newY, position.z + newZ);
    }

    public static Vec3 getRelativePosition(Entity entity, double deltaX, double deltaY, double deltaZ, boolean onlyOffset) {
        if (entity == null) {
            return Vec3.ZERO;
        }
        // Obtém os vetores locais da entidade
        Vec3 forward = entity.getViewVector(1.0F); // Direção que a entidade está olhando (Surge)
        Vec3 up = entity.getUpVector(1.0F); // Vetor "para cima" da entidade (Heave)
        Vec3 right = forward.cross(up).normalize(); // Calcula o vetor para a direita (Sway)

        // Combina os deslocamentos locais
        Vec3 offset = right.scale(deltaX) // Sway (esquerda/direita)
                .add(up.scale(deltaY)) // Heave (cima/baixo)
                .add(forward.scale(deltaZ)); // Surge (frente/trás)

        if (onlyOffset) {
            return offset;
        }
        // Retorna a nova posição baseada no deslocamento local
        return entity.position().add(offset);
    }

    public static double getTorsoYOffset(ChangedEntity self) {
        float ageAdjusted = (float)self.tickCount * 0.33333334F * 0.25F * 0.15F;
        float ageSin = Mth.sin(ageAdjusted * 3.1415927F * 0.5F);
        float ageCos = Mth.cos(ageAdjusted * 3.1415927F * 0.5F);
        float bpiSize = (self.getBasicPlayerInfo().getSize() - 1.0F) * 2.0F;
        return (double)(Mth.lerp(Mth.lerp(1.0F - Mth.abs(Mth.positiveModulo(ageAdjusted, 2.0F) - 1.0F), ageSin * ageSin * ageSin * ageSin, 1.0F - ageCos * ageCos * ageCos * ageCos), 0.95F, 0.87F) + bpiSize);
    }

    public static double getTorsoYOffset(ChangedEntity self, float scale) {
	    float ageAdjusted = (float) self.tickCount * 0.33333334F * 0.25F * 0.15F;
	    float ageSin = Mth.sin(ageAdjusted * (float) Math.PI * 0.5F);
	    float ageCos = Mth.cos(ageAdjusted * (float) Math.PI * 0.5F);
	    float bpiSize = (self.getBasicPlayerInfo().getSize() - 1.0F) * 2.0F;
	
	    float baseOscillation = Mth.lerp(
	        Mth.lerp(
            	1.0F - Mth.abs(Mth.positiveModulo(ageAdjusted, 2.0F) - 1.0F),
            	ageSin * ageSin * ageSin * ageSin,
	            1.0F - ageCos * ageCos * ageCos * ageCos
        	),
	        0.95F, 0.87F
    	);

    	return baseOscillation * scale + bpiSize;
	}


}
