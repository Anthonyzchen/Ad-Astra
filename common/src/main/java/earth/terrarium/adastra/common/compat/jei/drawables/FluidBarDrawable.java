package earth.terrarium.adastra.common.compat.jei.drawables;

import earth.terrarium.adastra.client.utils.GuiUtils;
import earth.terrarium.adastra.common.utils.TooltipUtils;
// TODO: CSL migration - FluidHolder needs CSL FluidResource equivalent
// import earth.terrarium.common_storage_lib.resources.fluid.FluidResource;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FluidBarDrawable implements IDrawable {

    private final int mouseX;
    private final int mouseY;
    private final boolean gain;
    private final long perTick;
    private final int cookTime;
    private final long capacity;
    private final Fluid fluid;

    // TODO: CSL migration - FluidHolder parameter needs CSL FluidResource equivalent
    public FluidBarDrawable(double mouseX, double mouseY, boolean generate, long capacity, int cookTime, Object fluid) {
        this.mouseX = (int) mouseX;
        this.mouseY = (int) mouseY;
        this.gain = generate;
        this.perTick = 0; // TODO: CSL migration - was fluid.getFluidAmount()
        this.cookTime = cookTime;
        this.capacity = capacity;
        this.fluid = null; // TODO: CSL migration - was fluid.getFluid()
    }

    @Override
    public int getWidth() {
        return GuiUtils.FLUID_BAR_WIDTH;
    }

    @Override
    public int getHeight() {
        return GuiUtils.FLUID_BAR_HEIGHT;
    }

    @Override
    public void draw(@NotNull GuiGraphics graphics, int xOffset, int yOffset) {
        // TODO: CSL migration - rework fluid drawing for CSL API
    }
}
