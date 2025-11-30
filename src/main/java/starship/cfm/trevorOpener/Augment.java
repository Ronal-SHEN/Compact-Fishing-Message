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
        put("Strong Hook Augment", "\uE10D");
        put("XP Magnet Augment", "\uE10E");
        put("Boosted Rod Augment", "\uE10F");
        put("Strong Pot Augment", "\uE110");
        put("Wise Hook Augment", "\uE111");
        put("Fish Magnet Augment", "\uE112");
        put("Speedy Rod Augment", "\uE113");
        put("Wise Pot Augment", "\uE114");
        put("Glimmering Hook Augment", "\uE115");
        put("Pearl Magnet Augment", "\uE116");
        put("Graceful Rod Augment", "\uE117");
        put("Glimmering Pot Augment", "\uE118");
        put("Greedy Hook Augment", "\uE119");
        put("Treasure Magnet Augment", "\uE11A");
        put("Glitched Rod Augment", "\uE11B");
        put("Greedy Pot Augment", "\uE11C");
        put("Lucky Hook Augment", "\uE11D");
        put("Spirit Magnet Augment", "\uE11E");
        put("Stable Rod Augment", "\uE11F");
        put("Lucky Pot Augment", "\uE120");
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
            if ((name.contains("Strong Pot Augment") || name.contains("Wise Pot Augment")
                    || name.contains("Glimmering Pot Augment") || name.contains("Greedy Pot Augment"))
                    && record.values().stream().allMatch(v -> v != 0))
                root.append(Text.literal("\n  "));
        }
        return root;
    }
}
