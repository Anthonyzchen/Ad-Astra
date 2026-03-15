package earth.terrarium.adastra.client.neoforge;

import earth.terrarium.adastra.client.AdAstraClient;
import earth.terrarium.adastra.common.entities.vehicles.Vehicle;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TickEvent;

import java.util.HashMap;
import java.util.Map;

// TODO: 1.21.11 - NeoForge annotation may have changed to @EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AdAstraClientNeoForge {

    public static final Map<Item, AdAstraClient.CustomItemRenderer> ITEM_RENDERERS = new HashMap<>();

    public static void init(IEventBus bus) {
        bus.addListener(AdAstraClientNeoForge::onSetupItemColors);
        NeoForge.EVENT_BUS.addListener(AdAstraClientNeoForge::onRegisterClientHud);
        NeoForge.EVENT_BUS.addListener(AdAstraClientNeoForge::onClientTick);
        NeoForge.EVENT_BUS.addListener(AdAstraClientNeoForge::onRenderLevelStage);
        NeoForge.EVENT_BUS.addListener(AdAstraClientNeoForge::onCalculateCameraDistance);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(AdAstraClient::init);
        AdAstraClient.onRegisterItemRenderers(ITEM_RENDERERS::put);
    }

    @SubscribeEvent
    public static void onRegisterKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(AdAstraClient.KEY_TOGGLE_SUIT_FLIGHT);
        event.register(AdAstraClient.KEY_OPEN_RADIO);
    }

    @SubscribeEvent
    public static void onRegisterParticles(RegisterParticleProvidersEvent event) {
        AdAstraClient.onRegisterParticles((type, provider) -> event.registerSpriteSet(type, provider::create));
    }

    @SubscribeEvent
    public static void modelLoading(ModelEvent.RegisterAdditional event) {
        AdAstraClient.onRegisterModels(event::register);
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        AdAstraClient.onRegisterEntityLayers(event::registerLayerDefinition);
    }

    @SubscribeEvent
    public static void onClientReloadListeners(RegisterClientReloadListenersEvent event) {
        AdAstraClient.onAddReloadListener((id, listener) -> event.registerReloadListener(listener));
    }

    // TODO: 1.21.11 - NeoForge may have changed TickEvent.ClientTickEvent. In newer NeoForge,
    // it may be split into ClientTickEvent.Pre and ClientTickEvent.Post instead of using Phase.
    private static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.START)) {
            AdAstraClient.clientTick(Minecraft.getInstance());
        }
    }

    // TODO: 1.21.11 - RenderLevelStageEvent may have changed. Verify Stage.AFTER_PARTICLES still exists.
    private static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            AdAstraClient.renderOverlays(event.getPoseStack(), event.getCamera());
        }
    }

    private static void onRegisterClientHud(RenderGuiEvent.Post event) {
        AdAstraClient.onRegisterHud(hud -> hud.renderHud(event.getGuiGraphics(), event.getPartialTick()));
    }

    private static void onSetupItemColors(RegisterColorHandlersEvent.Item event) {
        // TODO: 1.21.11 - Item color registration has changed. The old ItemColor interface is gone.
        // NeoForge RegisterColorHandlersEvent.Item may use a different API now.
        // AdAstraClient.onAddItemColors(event::register);
    }

    private static void onCalculateCameraDistance(CalculateDetachedCameraDistanceEvent event) {
        if (event.getDistance() < 12.0 && event.getCamera().getEntity().getVehicle() instanceof Vehicle vehicle && vehicle.zoomOutCameraInThirdPerson()) {
            event.setDistance(12.0);
        }
    }
}
