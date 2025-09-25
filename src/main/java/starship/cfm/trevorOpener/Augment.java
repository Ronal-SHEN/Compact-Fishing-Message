package starship.cfm.trevorOpener;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Augment extends Recorder {
    public static final Set<String> NAMES = Set.of(
            "Strong Hook Augment", "XP Magnet Augment", "Boosted Rod Augment", "Strong Pot Augment",
            "Wise Hook Augment", "Fish Magnet Augment", "Speedy Rod Augment", "Wise Pot Augment",
            "Glimmering Hook Augment", "Pearl Magnet Augment", "Graceful Rod Augment", "Glimmering Pot Augment",
            "Greedy Hook Augment", "Treasure Magnet Augment", "Glitched Rod Augment", "Greedy Pot Augment",
            "Lucky Hook Augment", "Spirit Magnet Augment", "Stable Rod Augment", "Lucky Pot Augment"

    );
    private static final Map<String, String> ICON_MAP = new LinkedHashMap<>() {{
        put("Strong Hook Augment", "\uE10D"); // from mcci pack
        put("XP Magnet Augment", "\uE114");
        put("Boosted Rod Augment", "\uE0F9");
        put("Strong Pot Augment", "\uE10E");
        put("Wise Hook Augment", "\uE112");
        put("Fish Magnet Augment", "\uE0FC");
        put("Speedy Rod Augment", "\uE109");
        put("Wise Pot Augment", "\uE113");
        put("Glimmering Hook Augment", "\uE0FE");
        put("Pearl Magnet Augment", "\uE108");
        put("Graceful Rod Augment", "\uE101");
        put("Glimmering Pot Augment", "\uE0FF");
        put("Greedy Hook Augment", "\uE103");
        put("Treasure Magnet Augment", "\uE110");
        put("Glitched Rod Augment", "\uE100");
        put("Greedy Pot Augment", "\uE104");
        put("Lucky Hook Augment", "\uE106");
        put("Spirit Magnet Augment", "\uE10A");
        put("Stable Rod Augment", "\uE10B");
        put("Lucky Pot Augment", "\uE107");
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
                    Identifier.of("mcc", "icon"))));
            root.append(Text.literal(" x" + count + "  ").formatted(Formatting.GRAY));
            if ((name.contains("Strong Pot Augment") || name.contains("Wise Pot Augment")
                    || name.contains("Glimmering Pot Augment") || name.contains("Greedy Pot Augment"))
                    && record.values().stream().allMatch(v -> v != 0))
                root.append(Text.literal("\n  "));
        }
        return root;
    }
}
