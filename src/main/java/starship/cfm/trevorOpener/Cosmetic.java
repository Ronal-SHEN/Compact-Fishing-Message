package starship.cfm.trevorOpener;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Cosmetic extends Recorder {
    private static final Set<String> UNCOMMON_NAMES = Set.of(
            "Bear Head Token", "Leaf Crown Token", "Potted Tree Token", "Fungal Staff Token", "Seed Pouch Token", "Twig Token",
            "Crocodile Head Token", "Jungle Sprout Token", "Shell Crown Token", "Beach Bucket Token", "Flower Staff Token", "Tropical Shell Token",
            "Archeologist Cap Token", "Desert Turban Token", "Mummy Mask Token", "Ancient Staff Token", "Archeologist Brush Token", "Golden Statue Token"
    );
    private static final Set<String> Rare_NAMES = Set.of(
            "Hunter Cap Token", "Treant Mask Token", "Adventurers Staff (Forest) Token", "Treant Fist Token", "Fungal Rod Token",
            "Frog Friend Token", "Lotus Crown Token", "Coral Trident Token", "Lotus Lantern Token", "Coral Rod Token",
            "Cowboy Hat (Gilded) Token", "Munchflower Sprout (Ashen) Token", "Lava Fist Token", "Munchflower Arm (Ashen) Token", "Archeologist Rod Token"
    );
    private static final Set<String> EPIC_NAMES = Set.of(
            "Witch Hat (Forest) Token", "Witch Staff (Forest) Token", "Witch Cauldron Token", "Witch Rod Token",
            "Explorer's Hat (Overgrown) Token", "Jungle Machete Token", "Explorer Backpack Token", "Jungle Rod Token",
            "Prospector Cap Token", "Prospector Pick Token", "Prospector Pack Token", "Gilded Rod Token"
    );
    private static final Set<String> LEGENDARY_NAMES = Set.of(
            "Flower Crown (Glimmering) Token", "Fairy Wand Token", "Fairy Wings Token", "Fairy Rod Token",
            "Butterfly Antennae Token", "Nectar Fist Token", "Butterfly Wings Token", "Twisted Rod Token",
            "Pirate Hat (Ashen) Token", "Cutlass (Ashen) Token", "Ship Wheel (Ashen) Token", "Pirate Rod (Ashen) Token"
    );
    private static final Set<String> NAMES = Set.of(
            "Uncommon", "Rare", "Epic", "Legendary"
    );

    @Override
    public Pattern compiledNamePattern() {
        Set<String> MERGED_NAMES = Stream.of(UNCOMMON_NAMES, Rare_NAMES, EPIC_NAMES, LEGENDARY_NAMES)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
        StringBuilder regex = new StringBuilder();
        for (String name : MERGED_NAMES) {
            if (!regex.isEmpty()) regex.append("|");
            regex.append(Pattern.quote(name));
        }
        return Pattern.compile(regex.toString());
    }

    @Override
    protected Set<String> getNames() {
        return NAMES;
    }

    @Override
    public void record(String name, int count) {
        if (UNCOMMON_NAMES.contains(name)) {
            record.put("Uncommon", record.get("Uncommon") + count);
            return;
        }
        if (Rare_NAMES.contains(name)) {
            record.put("Rare", record.get("Rare") + count);
            return;
        }
        if (EPIC_NAMES.contains(name)) {
            record.put("Epic", record.get("Epic") + count);
            return;
        }
        if (LEGENDARY_NAMES.contains(name)) {
            record.put("Legendary", record.get("Legendary") + count);
        }
    }

    @Override
    public Component summary() {
        MutableComponent root = Component.literal("  ");
        if (record.get("Legendary") != 0)
            root.append(Component.literal("Legendary").withStyle(ChatFormatting.GOLD))
                    .append(Component.literal(" x" + record.get("Legendary") + "     ").withStyle(ChatFormatting.GRAY));
        if (record.get("Epic") != 0)
            root.append(Component.literal("Epic").withStyle(ChatFormatting.DARK_PURPLE))
                    .append(Component.literal(" x" + record.get("Epic") + "     ").withStyle(ChatFormatting.GRAY));
        if (record.get("Rare") != 0)
            root.append(Component.literal("Rare").withStyle(ChatFormatting.BLUE))
                    .append(Component.literal(" x" + record.get("Rare") + "     ").withStyle(ChatFormatting.GRAY));
        if (record.get("Uncommon") != 0)
            root.append(Component.literal("Uncommon").withStyle(ChatFormatting.GREEN))
                    .append(Component.literal(" x" + record.get("Uncommon")).withStyle(ChatFormatting.GRAY));
        return root;
    }
}
