package net.foxyas.changedaddon.client.gui;

import net.foxyas.changedaddon.configuration.ChangedAddonClientConfigsConfiguration;
import net.foxyas.changedaddon.init.ChangedAddonModKeyMappings;
import net.foxyas.changedaddon.procedures.PlayerUtilProcedure;
import net.ltxprogrammer.changed.ability.GrabEntityAbility;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class PatOverlay {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGameOverlayEvent.Pre event) {

        if (!ChangedAddonClientConfigsConfiguration.PAT_OVERLAY.get()){
            return;
        }

        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {

            int w = event.getWindow().getGuiScaledWidth();
            int h = event.getWindow().getGuiScaledHeight();

            double posX = ChangedAddonClientConfigsConfiguration.PAT_OVERLAY_X.get();
            double posY = ChangedAddonClientConfigsConfiguration.PAT_OVERLAY_Y.get();

            float floatPosX = (float) posX;
            float floatPosY = (float) posY;

            boolean DynamicChanges = ChangedAddonClientConfigsConfiguration.DYNAMIC_PAT_OVERLAY.get();


            Player entity = Minecraft.getInstance().player;

            if (entity != null) {
                if (entity.getMainHandItem().isEmpty() || entity.getOffhandItem().isEmpty()){
                    Entity lookedEntity = PlayerUtilProcedure.getEntityPlayerLookingAt(entity, 4);
                    if (lookedEntity != null && isPatableEntity(entity,lookedEntity) && isKeySet()) {
                        if (!PatInfo(entity).getString().isEmpty()){
                        	if (!lookedEntity.isInvisible() && isPossibletoPat(entity)){
                                float EntityNameLength = PatInfo2(lookedEntity).getString().length();
                        		float MoveOverlayAmount = EntityNameLength * 1.5f;
                        		//EntityNameLength > 16 ? EntityNameLength * 4 : EntityNameLength * 2;
                        		Minecraft.getInstance().font.draw(event.getMatrixStack(),
                                    PatInfo(lookedEntity), DynamicChanges ? floatPosX - MoveOverlayAmount : floatPosX, floatPosY, -1);
                                /*Minecraft.getInstance().font.draw(event.getMatrixStack(),
                                *   PatInfo2(lookedEntity), 257 - PatInfo2(lookedEntity).getString().length() , 260, -1);*/

                        	}
                        }
                    }
                }
            }
        }
    }

    private static boolean isPatableEntity(Player player,Entity PatEntity) {
        // Verifica se a entidade está dentro das tags definidas como 'patable entities'
        boolean isPatableByTag = PatEntity.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY,
                new ResourceLocation("changed_addon:patable_entitys")));
        if (PatEntity instanceof Player Patplayer){
            boolean isPatPlayerTransfur = ProcessTransfur.getPlayerTransfurVariant(Patplayer) != null;
            boolean isPlayerTransfur = ProcessTransfur.getPlayerTransfurVariant(player) != null;
            if (isPatPlayerTransfur){
                return true;
            } else if (isPlayerTransfur) {
                return true;
            }
        } else if (PatEntity instanceof ChangedEntity){
            return true;
        }
        return isPatableByTag;
    }

    private static boolean isPossibletoPat(Player player) {
        var variant = ProcessTransfur.getPlayerTransfurVariant(player);
        if (variant != null) {
            var ability = variant.getAbilityInstance(ChangedAbilities.GRAB_ENTITY_ABILITY.get());
            if (ability != null
                    && ability.suited
                    && ability.grabbedHasControl) {
                return false;
            }
        }


        return GrabEntityAbility.getGrabber(player) == null;
    }

    public static boolean isKeySet(){
        String key = ChangedAddonModKeyMappings.PAT_KEY.getTranslatedKeyMessage().getString();
        return !key.isEmpty();
    }


    private static TranslatableComponent PatInfo(Entity lookedEntity) {
        String key = ChangedAddonModKeyMappings.PAT_KEY.getTranslatedKeyMessage().getString();
        if (lookedEntity instanceof LivingEntity) {
            TranslatableComponent patMessage = new TranslatableComponent("changed_addon.info.is_patable", key.isEmpty() ? "Not Key Set" : key, lookedEntity.getDisplayName().getString());
            patMessage.withStyle(style ->
                    style.withColor(Color3.getColor("#FFFFFF").toInt())
                            .withBold(true)
                            .withItalic(true));
            return patMessage;
        } else {
            return new TranslatableComponent("");
        }
    }

    private static TextComponent PatInfo2(Entity lookedEntity) {
        if (lookedEntity instanceof LivingEntity) {
            TextComponent patMessage;

            if(lookedEntity.hasCustomName()){
                patMessage = new TextComponent(lookedEntity.getCustomName().getString());
            } else {
                patMessage = new TextComponent(lookedEntity.getDisplayName().getString());
            }
            patMessage.withStyle(style ->
                    style.withColor(Color3.getColor("#FFFFFF").toInt())
                            .withBold(true)
                            .withItalic(true));
            return patMessage;
        } else {
            return new TextComponent("");
        }
    }
}
