package earth.terrarium.adastra.client.neoforge;

import earth.terrarium.adastra.client.AdAstraClient;
import earth.terrarium.adastra.common.entities.vehicles.Vehicle;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.common.NeoForge;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(value = Dist.CLIENT)
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
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        AdAstraClient.onRegisterEntityLayers(event::registerLayerDefinition);
    }

    @SubscribeEvent
    public static void onClientReloadListeners(AddClientReloadListenersEvent event) {
        AdAstraClient.onAddReloadListener((id, listener) -> event.addListener(id, listener));
    }

    private static void onClientTick(ClientTickEvent.Pre event) {
        AdAstraClient.clientTick(Minecraft.getInstance());
    }

    private static void onRenderLevelStage(RenderLevelStageEvent.AfterParticles event) {
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        AdAstraClient.renderOverlays(event.getPoseStack(), camera);
    }

    private static void onRegisterClientHud(RenderGuiEvent.Post event) {
        AdAstraClient.onRegisterHud(hud -> hud.renderHud(event.getGuiGraphics(), event.getPartialTick().getGameTimeDeltaPartialTick(true)));
    }

    private static void onSetupItemColors(RegisterColorHandlersEvent.ItemTintSources event) {
        // TODO: 1.21.11 - Item color registration has changed. The old ItemColor interface is gone.
        // NeoForge RegisterColorHandlersEvent.Item may use a different API now.
        // AdAstraClient.onAddItemColors(event::register);
    }

    private static void onCalculateCameraDistance(CalculateDetachedCameraDistanceEvent event) {
        if (event.getDistance() < 12.0f && event.getCamera().entity().getVehicle() instanceof Vehicle vehicle && vehicle.zoomOutCameraInThirdPerson()) {
            event.setDistance(12.0f);
        }
    }
}
