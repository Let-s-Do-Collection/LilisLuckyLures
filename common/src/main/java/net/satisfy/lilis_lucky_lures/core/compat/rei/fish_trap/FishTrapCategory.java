package net.satisfy.lilis_lucky_lures.core.compat.rei.fish_trap;

import com.google.common.collect.Lists;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.satisfy.lilis_lucky_lures.LilisLuckyLures;
import net.satisfy.lilis_lucky_lures.core.registry.ObjectRegistry;

import java.util.List;

public class FishTrapCategory implements DisplayCategory<FishTrapDisplay> {
    public static final CategoryIdentifier<FishTrapDisplay> FISH_TRAP_DISPLAY = CategoryIdentifier.of(LilisLuckyLures.MOD_ID, "fish_trap_display");


    @Override
    public CategoryIdentifier<FishTrapDisplay> getCategoryIdentifier() {
        return FISH_TRAP_DISPLAY;
    }


    @Override
    public Component getTitle() {
        return ObjectRegistry.FISH_TRAP.get().getName();
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ObjectRegistry.FISH_TRAP.get());
    }

    @Override
    public int getDisplayWidth(FishTrapDisplay display) {
        return 64;
    }

    @Override
    public int getDisplayHeight() {
        return 96;
    }

    @Override
    public List<Widget> setupDisplay(FishTrapDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX(), bounds.getCenterY());
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(startPoint.x - 12, startPoint.y - 12))
                .animationDurationTicks(50));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x - 8, startPoint.y + 12)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x - 8, startPoint.y + 12)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());

        if (display.getInputEntries().isEmpty())
            widgets.add(Widgets.createSlotBackground(new Point(startPoint.x - 8, startPoint.y - 32)));
        else
            widgets.add(Widgets.createSlot(new Point(startPoint.x - 8, startPoint.y - 32)).entries(display.getInputEntries().get(0)).markInput());

        return widgets;
    }
}