package earth.terrarium.adastra.client.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;
import com.teamresourceful.resourcefullib.client.utils.RenderUtils;
import com.teamresourceful.resourcefullib.client.utils.ScreenUtils;
import earth.terrarium.adastra.AdAstra;
import earth.terrarium.adastra.common.registry.ModFluids;
import earth.terrarium.adastra.common.utils.TooltipUtils;
import earth.terrarium.common_storage_lib.resources.fluid.FluidResource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class GuiUtils {

    public static final ResourceLocation ENERGY_BAR = ResourceLocation.fromNamespaceAndPath(AdAstra.MOD_ID, "energy_bar");
    public static final int ENERGY_BAR_WIDTH = 13;
    public static final int ENERGY_BAR_HEIGHT = 46;

    public static final ResourceLocation FLUID_BAR = ResourceLocation.fromNamespaceAndPath(AdAstra.MOD_ID, "fluid_bar");
    public static final int FLUID_BAR_WIDTH = 12;
    public static final int FLUID_BAR_HEIGHT = 46;

    public static final ResourceLocation HAMMER = ResourceLocation.fromNamespaceAndPath(AdAstra.MOD_ID, "textures/gui/sprites/hammer.png");
    public static final ResourceLocation SNOWFLAKE = ResourceLocation.fromNamespaceAndPath(AdAstra.MOD_ID, "textures/gui/sprites/snowflake.png");
    public static final ResourceLocation FIRE = ResourceLocation.fromNamespaceAndPath(AdAstra.MOD_ID, "textures/gui/sprites/fire.png");
    public static final ResourceLocation ARROW = ResourceLocation.fromNamespaceAndPath(AdAstra.MOD_ID, "textures/gui/sprites/arrow.png");
    public static final ResourceLocation SUN = ResourceLocation.fromNamespaceAndPath(AdAstra.MOD_ID, "sun");
    public static final ResourceLocation SLIDER = ResourceLocation.fromNamespaceAndPath(AdAstra.MOD_ID, "slider");

    public static final WidgetSprites SETTINGS_BUTTON_SPRITES = createPressableButtonSprites("settings_button");
    public static final WidgetSprites RESET_BUTTON_SPRITES = createPressableButtonSprites("reset_button");
    public static final WidgetSprites SHOW_BUTTON_SPRITES = createPressableButtonSprites("show_button");
    public static final WidgetSprites HIDE_BUTTON_SPRITES = createPressableButtonSprites("hide_button");

    public static final WidgetSprites CRAFTING_BUTTON_SPRITES = createPressableButtonSprites("crafting_button");
    public static final WidgetSprites FURNACE_BUTTON_SPRITES = createPressableButtonSprites("furnace_button");

    public static final WidgetSprites NONE_BUTTON_SPRITES = createPressableButtonSprites("side_config/none");
    public static final WidgetSprites PUSH_BUTTON_SPRITES = createPressableButtonSprites("side_config/push");
    public static final WidgetSprites PULL_BUTTON_SPRITES = createPressableButtonSprites("side_config/pull");
    public static final WidgetSprites PUSH_PULL_BUTTON_SPRITES = createPressableButtonSprites("side_config/push_pull");

    public static final WidgetSprites REDSTONE_ALWAYS_ON_SPRITES = createPressableButtonSprites("redstone/always_on_button");
    public static final WidgetSprites REDSTONE_ON_WHEN_POWERED_SPRITES = createPressableButtonSprites("redstone/on_when_powered_button");
    public static final WidgetSprites REDSTONE_ON_WHEN_NOT_POWERED_SPRITES = createPressableButtonSprites("redstone/on_when_not_powered_button");
    public static final WidgetSprites REDSTONE_NEVER_ON_SPRITES = createPressableButtonSprites("redstone/never_on_button");

    public static WidgetSprites createPressableButtonSprites(String name) {
        return new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(AdAstra.MOD_ID, "buttons/" + name),
            ResourceLocation.fromNamespaceAndPath(AdAstra.MOD_ID, "buttons/" + name + "_pressed"),
            ResourceLocation.fromNamespaceAndPath(AdAstra.MOD_ID, "buttons/" + name + "_highlighted")
        );
    }

    public static void drawEnergyBar(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, long energy, long capacity, Component... tooltips) {
        float ratio = energy / (float) capacity;
        try (var ignored = RenderUtils.createScissorBox(Minecraft.getInstance(), graphics.pose(), x + 6, y - 31 + ENERGY_BAR_HEIGHT - (int) (ENERGY_BAR_HEIGHT * ratio), ENERGY_BAR_WIDTH, ENERGY_BAR_HEIGHT)) {
            graphics.blitSprite(ENERGY_BAR, x + 6, y - 31, ENERGY_BAR_WIDTH, ENERGY_BAR_HEIGHT);
        }

        drawTooltips(mouseX, mouseY, x + 6, x + 19, y - 31, y + 15, list -> {
            list.add(TooltipUtils.getEnergyComponent(energy, capacity));
            Collections.addAll(list, tooltips);
            return list;
        });
    }

    public static void drawFluidBar(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, FluidResource fluid, long amount, long capacity, Component... tooltips) {
        int barX = x + 6;
        int barY = y - 31;
        float ratio = capacity > 0 ? amount / (float) capacity : 0;

        if (ratio > 0 && !fluid.isBlank()) {
            int fillHeight = (int) (FLUID_BAR_HEIGHT * ratio);
            int fillY = barY + FLUID_BAR_HEIGHT - fillHeight;
            renderFluidFill(graphics, fluid.getType(), barX, fillY, FLUID_BAR_WIDTH, fillHeight);
        }

        // Draw the bar frame overlay on top
        graphics.blitSprite(FLUID_BAR, barX, barY, FLUID_BAR_WIDTH, FLUID_BAR_HEIGHT);

        drawTooltips(mouseX, mouseY, x + 6, x + 18, y - 31, y + 15, list -> {
            list.add(TooltipUtils.getFluidComponent(fluid, amount, capacity));
            Collections.addAll(list, tooltips);
            return list;
        });
    }

    public static void drawHorizontalProgressBar(GuiGraphics graphics, ResourceLocation texture, int mouseX, int mouseY, int x, int y, int width, int height, int progress, int maxProgress, boolean reverse, Component... tooltips) {
        int widthProgress = (int) (width * (progress / (float) maxProgress));
        if (reverse) widthProgress = width - widthProgress;
        graphics.blit(texture, x, y, 0, 0, widthProgress, height, width, height);

        drawTooltips(mouseX, mouseY, x, x + width, y, y + height, list -> {
            Collections.addAll(list, tooltips);
            return list;
        });
    }

    public static void drawVerticalProgressBar(GuiGraphics graphics, ResourceLocation texture, int mouseX, int mouseY, int x, int y, int width, int height, int progress, int maxProgress, Component... tooltips) {
        int heightProgress = (int) (height * (progress / (float) maxProgress));
        heightProgress = height - heightProgress;
        graphics.blit(texture, x, y + heightProgress, 0, heightProgress, width, height - heightProgress, width, height);

        drawTooltips(mouseX, mouseY, x, x + width, y, y + height, list -> {
            Collections.addAll(list, tooltips);
            return list;
        });
    }

    /**
     * Renders the fluid's still texture tiled and tinted within the given area.
     */
    public static void renderFluidFill(GuiGraphics graphics, Fluid fluid, int x, int y, int width, int height) {
        if (height <= 0) return;

        ResourceLocation stillTexture = getFluidStillTexture(fluid);
        int color = getFluidColor(fluid);

        @SuppressWarnings("deprecation")
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(stillTexture);

        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        float a = ((color >> 24) & 0xFF) / 255f;

        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(r, g, b, a);
        RenderSystem.enableBlend();

        Matrix4f matrix = graphics.pose().last().pose();
        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        // Tile the 16x16 fluid sprite to fill the area from top to bottom
        int drawY = y;
        int remaining = height;
        while (remaining > 0) {
            int drawHeight = Math.min(remaining, 16);
            float minU = sprite.getU0();
            float maxU = sprite.getU(width / 16f);
            float minV = sprite.getV0();
            float maxV = sprite.getV(drawHeight / 16f);

            builder.addVertex(matrix, x, drawY + drawHeight, 0).setUv(minU, maxV);
            builder.addVertex(matrix, x + width, drawY + drawHeight, 0).setUv(maxU, maxV);
            builder.addVertex(matrix, x + width, drawY, 0).setUv(maxU, minV);
            builder.addVertex(matrix, x, drawY, 0).setUv(minU, minV);

            drawY += drawHeight;
            remaining -= drawHeight;
        }

        BufferUploader.drawWithShader(builder.buildOrThrow());
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
    }

    /**
     * Returns the still texture location for a given fluid.
     */
    public static ResourceLocation getFluidStillTexture(Fluid fluid) {
        if (fluid == Fluids.LAVA || fluid == Fluids.FLOWING_LAVA) {
            return ResourceLocation.withDefaultNamespace("block/lava_still");
        }
        // All Ad Astra fluids and water use the water_still texture
        return ResourceLocation.withDefaultNamespace("block/water_still");
    }

    /**
     * Returns the ARGB tint color for a given fluid.
     * Colors are sourced from ModFluidProperties for Ad Astra fluids.
     */
    public static int getFluidColor(Fluid fluid) {
        if (fluid == Fluids.WATER || fluid == Fluids.FLOWING_WATER) {
            return 0xFF3F76E4; // Vanilla water blue
        }
        if (fluid == Fluids.LAVA || fluid == Fluids.FLOWING_LAVA) {
            return 0xFFFFFFFF; // Lava texture is already colored
        }
        if (fluid == ModFluids.OXYGEN.get()) {
            return 0xFFDAE6F0; // Light blue-white
        }
        if (fluid == ModFluids.HYDROGEN.get()) {
            return 0xFF89CFF0; // Light blue
        }
        if (fluid == ModFluids.OIL.get()) {
            return 0xFF373A36; // Dark gray-brown
        }
        if (fluid == ModFluids.FUEL.get()) {
            return 0xFFE5292B; // Red
        }
        if (fluid == ModFluids.CRYO_FUEL.get()) {
            return 0xFF6CFFFA; // Cyan
        }
        return 0xFFFFFFFF; // Default: no tint
    }

    public static void drawTooltips(int mouseX, int mouseY, int minX, int maxX, int minY, int maxY, Function<List<Component>, List<Component>> tooltips) {
        if (mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY <= maxY) {
            List<Component> lines = tooltips.apply(new ArrayList<>());
            lines.removeIf(c -> c.getString().isEmpty());
            ScreenUtils.setTooltip(lines);
        }
    }

    public static void drawColoredShadowCenteredString(GuiGraphics graphics, Font font, Component text, int x, int y, int color, int shadowColor) {
        FormattedCharSequence formattedCharSequence = text.getVisualOrderText();
        graphics.drawString(font, formattedCharSequence, x - font.width(formattedCharSequence) / 2 - 1, y + 1, shadowColor, false);
        graphics.drawString(font, formattedCharSequence, x - font.width(formattedCharSequence) / 2, y, color, false);
    }

    public static void drawColoredShadowString(GuiGraphics graphics, Font font, Component text, int x, int y, int color, int shadowColor) {
        graphics.drawString(font, text, x - 1, y + 1, shadowColor, false);
        graphics.drawString(font, text, x, y, color, false);
    }
}
