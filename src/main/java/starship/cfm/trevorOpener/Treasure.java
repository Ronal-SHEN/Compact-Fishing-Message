package starship.cfm.trevorOpener;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.StyleSpriteSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Treasure extends Recorder {

    private static final Set<String> NAMES = Set.of(
            "Mythic Treasure",
            "Legendary Treasure",
            "Epic Treasure",
            "Rare Treasure",
            "Uncommon Treasure",
            "Common Treasure"
    );
    private static final Map<String, String> ICON_MAP = new LinkedHashMap<>() {{
        put("Mythic Treasure", "\uE006");
        put("Legendary Treasure", "\uE001");
        put("Epic Treasure", "\uE002");
        put("Rare Treasure", "\uE003");
        put("Uncommon Treasure", "\uE004");
        put("Common Treasure", "\uE005");
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
                    new StyleSpriteSource.Font(Identifier.of("cfm", "icon")))));
            root.append(Text.literal("x" + count + "  "));
        }
        return root;
    }

}
