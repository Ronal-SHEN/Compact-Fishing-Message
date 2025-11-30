package starship.cfm.trevorOpener;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Lure extends Recorder {
    public static final Set<String> NAMES = Set.of(
            "A.N.G.L.R. Elusive Ultralure", "A.N.G.L.R. Wayfinder Ultralure", "A.N.G.L.R. Pearl Ultralure",
            "A.N.G.L.R. Treasure Ultralure", "A.N.G.L.R. Spirit Ultralure",
            "A.N.G.L.R. Elusive Lure", "A.N.G.L.R. Wayfinder Lure", "A.N.G.L.R. Pearl Lure",
            "A.N.G.L.R. Treasure Lure", "A.N.G.L.R. Spirit Lure");
    private static final Map<String, String> ICON_MAP = new LinkedHashMap<>() {{
        put("A.N.G.L.R. Elusive Ultralure", "\uE060");
        put("A.N.G.L.R. Wayfinder Ultralure", "\uE061");
        put("A.N.G.L.R. Pearl Ultralure", "\uE062");
        put("A.N.G.L.R. Treasure Ultralure", "\uE063");
        put("A.N.G.L.R. Spirit Ultralure", "\uE064");
        put("A.N.G.L.R. Elusive Lure", "\uE065");
        put("A.N.G.L.R. Wayfinder Lure", "\uE066");
        put("A.N.G.L.R. Pearl Lure", "\uE067");
        put("A.N.G.L.R. Treasure Lure", "\uE068");
        put("A.N.G.L.R. Spirit Lure", "\uE069");
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
