package starship.cfm.trevorOpener;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;

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
    public Component summary() {
        MutableComponent root = Component.literal("  ");
        for (Map.Entry<String, String> entry : ICON_MAP.entrySet()) {
            String name = entry.getKey();
            String icon = entry.getValue();
            int count = record.getOrDefault(name, 0);
            if (record.get(name) == 0) continue;
            root.append(Component.literal(icon).setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withFont(
                        new FontDescription.Resource(Identifier.fromNamespaceAndPath("cfm", "icon")))));
            root.append(Component.literal("x" + count + "  "));
        }
        return root;
    }

}
