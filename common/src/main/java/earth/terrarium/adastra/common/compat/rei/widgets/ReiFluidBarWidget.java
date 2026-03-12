package earth.terrarium.adastra.common.compat.rei.widgets;

import earth.terrarium.adastra.client.utils.GuiUtils;
import earth.terrarium.adastra.common.utils.TooltipUtils;
// TODO: CSL migration - FluidHolder needs CSL FluidResource equivalent
// import earth.terrarium.common_storage_lib.resources.fluid.FluidResource;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class ReiFluidBarWidget extends Widget {

    private final Rectangle bounds;
    private final boolean gain;
    private final long perTick;
    private final int cookTime;
    private final long capacity;
    private final Fluid fluid;

    // TODO: CSL migration - FluidHolder parameter needs CSL FluidResource equivalent
    public ReiFluidBarWidget(Point point, boolean generate, long capacity, int cookTime, Object fluid) {
        this.bounds = new Rectangle(new Rectangle(point.x, point.y, GuiUtils.FLUID_BAR_WIDTH, GuiUtils.FLUID_BAR_HEIGHT));
        this.gain = generate;
        this.perTick = 0; // TODO: CSL migration - was fluid.getFluidAmount()
        this.cookTime = cookTime;
        this.capacity = capacity;
        this.fluid = null; // TODO: CSL migration - was fluid.getFluid()
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // TODO: CSL migration - rework fluid rendering for CSL API
    }

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return List.of();
    }
}
