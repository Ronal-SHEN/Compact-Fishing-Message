package starship.cfm.trevorOpener;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class BaitLine extends Recorder {
    private static final Set<String> NAMES = Set.of(
            "Mythic Bait", "Legendary Bait", "Epic Bait", "Rare Bait", "Uncommon Bait", "Common Bait",
            "Mythic Line", "Legendary Line", "Epic Line", "Rare Line", "Uncommon Line", "Common Line"
    );
    private static final Map<String, String> ICON_MAP = new LinkedHashMap<>() {{
        put("Mythic Bait", "\uE030");
        put("Legendary Bait", "\uE031");
        put("Epic Bait", "\uE032");
        put("Rare Bait", "\uE033");
        put("Uncommon Bait", "\uE034");
        put("Common Bait", "\uE035");
        put("Mythic Line", "\uE036");
        put("Legendary Line", "\uE037");
        put("Epic Line", "\uE038");
        put("Rare Line", "\uE039");
        put("Uncommon Line", "\uE03A");
        put("Common Line", "\uE03B");
    }};


    @Override
    protected Set<String> getNames() {
        return NAMES;
    }

    @Override
    public Text summary() {
        MutableText root = Text.literal("  ");
        for (Map.Entry<String, String> entry : ICON_MAP.entrySet()) {
            String name = entry.getKey();
            String icon = entry.getValue();
            int count = record.getOrDefault(name, 0);
            if (record.get(name) == 0) continue;
            root.append(Text.literal(icon).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(
                    Identifier.of("cfm", "icon"))));
            root.append(Text.literal(" x" + count + "  ").formatted(Formatting.GRAY));
            if (name.contains("Common Bait") && record.values().stream().allMatch(v -> v != 0))
                root.append(Text.literal("\n  "));
        }
        return root;
    }
}
