package starship.cfm.fishMessage;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import starship.cfm.itemCategory.*;

import java.util.ArrayList;
import java.util.List;

public class FishSession {
    public final List<Text> triggerIcons = new ArrayList<>();
    public final List<String> triggers = new ArrayList<>();
    public String lootName = "";
    public int lootCount = 1;
    public MutableText caughtMessage = null;
    public int xpGained = 0;
    public boolean isActive = false;
    public boolean isLast = false;
    public long catchTime = 0;

    public Text msgSummary = null;
    public FontFactory.CategoryType catType = null;

    public void reset() {
        lootName = "";
        lootCount = 1;
        triggerIcons.clear();
        xpGained = 0;
        isActive = false;
        caughtMessage = null;
        msgSummary = null;
        isLast = false;
        catchTime = 0;
        catType = null;
        triggers.clear();
    }

    public FontFactory.CategoryType extraCategoryFromName(String name) {
        if (name == null) return FontFactory.CategoryType.NORMAL_FISH;

        if (Junk.ITEMS.contains(name)) return FontFactory.CategoryType.JUNK;
        if (Pearl.ITEMS.contains(name)) return FontFactory.CategoryType.PEARL;
        if (Treasure.ITEMS.contains(name)) return FontFactory.CategoryType.TREASURE;
        if (Spirit.ITEMS.contains(name)) return FontFactory.CategoryType.SPIRIT;
        if (Fish.ELUSIVE_PATTERN.matcher(name).find()) return FontFactory.CategoryType.ELUSIVE_FISH;

        return FontFactory.CategoryType.NORMAL_FISH;
    }


}
