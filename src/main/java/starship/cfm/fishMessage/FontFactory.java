package starship.cfm.fishMessage;

import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;


public class FontFactory {
    private static final Map<String, Text> TRIGGER_TEXTS = new HashMap<>();
    private static final Map<String, Text> CATEGORY_TEXTS = new HashMap<>();

    static {
        registerTrigger("Speedy Rod");
        registerTrigger("Boosted Rod");
        registerTrigger("Graceful Rod");
        registerTrigger("Glitched Rod");
        registerTrigger("Stable Rod");
        registerTrigger("XP Magnet");
        registerTrigger("Fish Magnet");
        registerTrigger("Pearl Magnet");
        registerTrigger("Treasure Magnet");
        registerTrigger("Spirit Magnet");
        registerTrigger("Supply Preserve");
        registerTrigger("Elusive Catch");

        registerCategory(CategoryType.JUNK);
        registerCategory(CategoryType.NORMAL_FISH);
        registerCategory(CategoryType.ELUSIVE_FISH);
        registerCategory(CategoryType.PEARL);
        registerCategory(CategoryType.TREASURE);
        registerCategory(CategoryType.SPIRIT);

    }

    private static void registerTrigger(String name) {
        Text finalText = switch (name) {
            case "Speedy Rod" -> Text.literal("").append(Text.literal("").append(
                    Text.literal("\uE113").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(
                            Identifier.of("cfm", "icon"))))).setStyle(Style.EMPTY.withHoverEvent(
                    new HoverEvent.ShowText(Text.literal("Speedy Rod Perk\n\n").setStyle(
                            Style.EMPTY.withColor(0x219BF3)).append(Text.literal(
                            "When triggered after a catch, your next catch will appear ").setStyle(
                            Style.EMPTY.withColor(0x65FFFF)).append(Text.literal("Instantly").setStyle(
                            Style.EMPTY.withColor(Formatting.WHITE))).append(" upon casting.")))));
            case "Boosted Rod" -> Text.literal("").append(Text.literal("").append(
                    Text.literal("\uE10F").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(
                            Identifier.of("cfm", "icon"))
                    ))).setStyle(Style.EMPTY.withHoverEvent(
                    new HoverEvent.ShowText(
                            Text.literal("Boosted Rod Perk\n\n").setStyle(Style.EMPTY.withColor(Formatting.RED))
                                    .append(Text.literal("When triggered, ").setStyle(Style.EMPTY.withColor(0x65FFFF))
                                            .append(Text.literal("doubles").setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                                            .append(" your ")
                                            .append(Text.literal("Elusive Fish Chance").setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                                            .append(", ")
                                            .append(Text.literal("Pearl Chance").setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                                            .append(", ")
                                            .append(Text.literal("Treasure Chance").setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                                            .append(", ")
                                            .append(Text.literal("Spirit Chance").setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                                            .append(", and ")
                                            .append(Text.literal("Wayfinder Data").setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                                            .append(" for that catch.")
                                    )
                    )));
            case "Graceful Rod" -> Text.literal("").append(Text.literal("").append(
                    Text.literal("\uE117").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(
                            Identifier.of("cfm", "icon"))
                    ))).setStyle(Style.EMPTY.withHoverEvent(
                    new HoverEvent.ShowText(
                            Text.literal("Graceful Rod Perk\n\n").setStyle(Style.EMPTY.withColor(0x8833FF))
                                    .append(Text.literal("When triggered, cancels the decrease in ").setStyle(Style.EMPTY.withColor(0x65FFFF))
                                            .append(Text.literal("Stock").setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                                            .append(" of your fishing spot for that catch."))
                    )
            ));
            case "Glitched Rod" -> Text.literal("").append(Text.literal("").append(
                    Text.literal("\uE11B").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(
                            Identifier.of("cfm", "icon"))
                    ))).setStyle(Style.EMPTY.withHoverEvent(
                    new HoverEvent.ShowText(
                            Text.literal("Glitched Rod Perk\n\n").setStyle(Style.EMPTY.withColor(0xFF7E40))
                                    .append(Text.literal("When triggered, your catch counts twice towards your ").setStyle(Style.EMPTY.withColor(0x65FFFF))
                                            .append(Text.literal("Research").setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                                            .append("."))
                    )
            ));
            case "Stable Rod" -> Text.literal("").append(Text.literal("").append(
                    Text.literal("\uE11F").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(
                            Identifier.of("cfm", "icon"))
                    ))).setStyle(Style.EMPTY.withHoverEvent(
                    new HoverEvent.ShowText(
                            Text.literal("Stable Rod Perk\n\n").setStyle(Style.EMPTY.withColor(0x23C725))
                                    .append(Text.literal("When triggered, cancels the loss of ").setStyle(Style.EMPTY.withColor(0x65FFFF))
                                            .append(Text.literal("Stability").setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                                            .append(" of the Grotto for that catch."))
                    )
            ));
            case "XP Magnet" -> Text.literal("").append(Text.literal("").append(
                    Text.literal("\uE10E").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(
                            Identifier.of("cfm", "icon"))
                    ))).setStyle(Style.EMPTY.withHoverEvent(
                    new HoverEvent.ShowText(
                            Text.literal("XP Magnet Perk\n\n").setStyle(Style.EMPTY.withColor(Formatting.RED))
                                    .append(Text.literal("Increases your chances of ").setStyle(Style.EMPTY.withColor(0x65FFFF))
                                            .append(Text.literal("Multiplying").setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                                            .append(" the ")
                                            .append(Text.literal("Island XP").setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                                            .append(" you earn from fishing catches."))
                    )
            ));
            case "Fish Magnet" -> Text.literal("").append(Text.literal("").append(
                    Text.literal("\uE112").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(
                            Identifier.of("cfm", "icon"))
                    ))).setStyle(Style.EMPTY.withHoverEvent(
                    new HoverEvent.ShowText(
                            Text.literal("Fish Magnet Perk\n\n").setStyle(Style.EMPTY.withColor(0x219BF3))
                                    .append(Text.literal("When you catch a ").setStyle(Style.EMPTY.withColor(0x65FFFF))
                                            .append(Text.literal("Fish").setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                                            .append(", increases the chance to ")
                                            .append(Text.literal("Multiply").setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                                            .append(" the amount."))
                    )
            ));
            case "Pearl Magnet" -> Text.literal("").append(Text.literal("").append(
                    Text.literal("\uE116").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(
                            Identifier.of("cfm", "icon"))
                    ))).setStyle(Style.EMPTY.withHoverEvent(
                    new HoverEvent.ShowText(
                            Text.literal("Pearl Magnet Perk\n\n").setStyle(Style.EMPTY.withColor(0x8833FF))
                                    .append(Text.literal("When you catch a ").setStyle(Style.EMPTY.withColor(0x65FFFF))
                                            .append(Text.literal("Pearl").setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                                            .append(", increases the chance to ")
                                            .append(Text.literal("Multiply").setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                                            .append(" the amount."))
                    )
            ));
            case "Treasure Magnet" -> Text.literal("").append(Text.literal("").append(
                    Text.literal("\uE11A").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(
                            Identifier.of("cfm", "icon"))
                    ))).setStyle(Style.EMPTY.withHoverEvent(
                    new HoverEvent.ShowText(
                            Text.literal("Treasure Magnet Perk\n\n").setStyle(Style.EMPTY.withColor(0xFF7E40))
                                    .append(Text.literal("When you catch a ").setStyle(Style.EMPTY.withColor(0x65FFFF))
                                            .append(Text.literal("Trevor").setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                                            .append(", increases the chance to ")
                                            .append(Text.literal("Multiply").setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                                            .append(" the amount."))
                    )
            ));
            case "Spirit Magnet" -> Text.literal("").append(Text.literal("").append(
                    Text.literal("\uE11E").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(
                            Identifier.of("cfm", "icon"))
                    ))).setStyle(Style.EMPTY.withHoverEvent(
                    new HoverEvent.ShowText(
                            Text.literal("Spirit Magnet Perk\n\n").setStyle(Style.EMPTY.withColor(0x23C725))
                                    .append(Text.literal("When you catch a ").setStyle(Style.EMPTY.withColor(0x65FFFF))
                                            .append(Text.literal("Spirit").setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                                            .append(", increases the chance to ")
                                            .append(Text.literal("Multiply").setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                                            .append(" the amount."))
                    )
            ));
            case "Supply Preserve" -> Text.literal("").append(Text.literal("").append(
                    Text.literal("\uE121").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(
                            Identifier.of("cfm", "icon"))
                    ))).setStyle(Style.EMPTY.withHoverEvent(
                    new HoverEvent.ShowText(
                            Text.literal("Supply Preserve Perk\n\n").setStyle(Style.EMPTY.withColor(Formatting.RED))
                                    .append(Text.literal("When triggered, prevents all equipped consumables from losing a use.").setStyle(
                                            Style.EMPTY.withColor(0x65FFFF)))
                    )
            ));
            case "Elusive Catch" -> Text.literal("").append(Text.literal("").append(
                    Text.literal("\uE065").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(
                            Identifier.of("cfm", "icon"))
                    ))).setStyle(Style.EMPTY.withHoverEvent(
                    new HoverEvent.ShowText(
                            Text.literal("You caught an Elusive Fish!\n\n").setStyle(Style.EMPTY.withColor(0x55FF56))
                                    .append(Text.literal("Elusive").setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                                    .append(" ")
                                    .append(Text.literal("fish are extra rare, and are worth ").setStyle(Style.EMPTY.withColor(0x65FFFF))
                                            .append(Text.literal("3x").setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
                                            .append(" more than normal fish."))
                    )
            ));

            default -> Text.literal(name);
        };

        TRIGGER_TEXTS.put(name, finalText);
    }

    private static void registerCategory(CategoryType category) {
        Text text = switch (category) {
            case JUNK -> Text.empty().append(Text.literal("(").setStyle(Style.EMPTY.withColor(0x23D106)))
                    .append(Text.literal("").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(
                            Identifier.of("cfm", "icon"))));
            case NORMAL_FISH -> Text.empty().append(Text.literal("(").setStyle(Style.EMPTY.withColor(0x23D106)))
                    .append(Text.literal("").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(
                            Identifier.of("cfm", "icon"))));
            case ELUSIVE_FISH -> Text.empty().append(Text.literal("(").setStyle(Style.EMPTY.withColor(0x23D106)))
                    .append(Text.literal("").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(
                            Identifier.of("cfm", "icon"))));
            case PEARL -> Text.empty().append(Text.literal("(").setStyle(Style.EMPTY.withColor(0x23D106)))
                    .append(Text.literal("").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(
                            Identifier.of("cfm", "icon"))));
            case TREASURE -> Text.empty().append(Text.literal("(").setStyle(Style.EMPTY.withColor(0x23D106)))
                    .append(Text.literal("").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(
                            Identifier.of("cfm", "icon"))));
            case SPIRIT -> Text.empty().append(Text.literal("(").setStyle(Style.EMPTY.withColor(0x23D106)))
                    .append(Text.literal("").setStyle(Style.EMPTY.withColor(Formatting.WHITE).withFont(
                            Identifier.of("cfm", "icon"))));
        };

        CATEGORY_TEXTS.put(category.name(), text);
    }

    public static Text get(String name) {
        return TRIGGER_TEXTS.get(name);
    }

    public static Text getCategory(CategoryType category) {
        return CATEGORY_TEXTS.get(category.name());
    }

    public enum CategoryType {
        JUNK,
        NORMAL_FISH,
        ELUSIVE_FISH,
        PEARL,
        TREASURE,
        SPIRIT
    }

}
