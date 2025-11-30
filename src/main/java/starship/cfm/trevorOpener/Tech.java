package starship.cfm.trevorOpener;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


public class Tech extends Recorder {
    public static final Set<String> NAMES = Set.of("A.N.G.L.R. Elusive Soda", "A.N.G.L.R. Rarity Rod",
            "A.N.G.L.R. Stock Replenisher", "A.N.G.L.R. Lure Battery", "A.N.G.L.R. Pure Beacon", "A.N.G.L.R. Auto Rod");

    private static final Map<String, String> ICON_MAP = new LinkedHashMap<>() {{
        put("A.N.G.L.R. Elusive Soda", "\uE06A");
        put("A.N.G.L.R. Rarity Rod", "\uE06B");
        put("A.N.G.L.R. Stock Replenisher", "\uE06C");
        put("A.N.G.L.R. Lure Battery", "\uE06D");
        put("A.N.G.L.R. Pure Beacon", "\uE06E");
        put("A.N.G.L.R. Auto Rod", "\uE06F");
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
        }
        return root;
    }
}
