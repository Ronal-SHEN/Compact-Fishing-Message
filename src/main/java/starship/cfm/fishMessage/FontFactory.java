package starship.cfm.fishMessage;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;


public class FontFactory {
    private static final Map<String, Component> TRIGGER_TEXTS = new HashMap<>();
    private static final Map<String, Component> CATEGORY_TEXTS = new HashMap<>();

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
        Component finalText = switch (name) {
            case "Speedy Rod" -> Component.literal("").append(Component.literal("").append(
                    Component.literal("\uE113").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withFont(
                        new FontDescription.Resource(Identifier.fromNamespaceAndPath("cfm", "icon")))))).setStyle(Style.EMPTY.withHoverEvent(
                    new HoverEvent.ShowText( Component.literal("Speedy Rod Perk\n\n").setStyle(
                            Style.EMPTY.withColor(0x219BF3)).append(Component.literal(
                            "When triggered after a catch, your next catch will appear ").setStyle(
                            Style.EMPTY.withColor(0x65FFFF)).append(Component.literal("Instantly").setStyle(
                            Style.EMPTY.withColor(ChatFormatting.WHITE))).append(" upon casting.")))));
            case "Boosted Rod" -> Component.literal("").append(Component.literal("").append(
                    Component.literal("\uE10F").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withFont(
                        new FontDescription.Resource(Identifier.fromNamespaceAndPath("cfm", "icon")))))).setStyle(Style.EMPTY.withHoverEvent(
                    new HoverEvent.ShowText(
                            Component.literal("Boosted Rod Perk\n\n").setStyle(Style.EMPTY.withColor(ChatFormatting.RED))
                                    .append(Component.literal("When triggered, ").setStyle(Style.EMPTY.withColor(0x65FFFF))
                                            .append(Component.literal("doubles").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)))
                                            .append(" your ")
                                            .append(Component.literal("Elusive Fish Chance").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)))
                                            .append(", ")
                                            .append(Component.literal("Pearl Chance").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)))
                                            .append(", ")
                                            .append(Component.literal("Treasure Chance").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)))
                                            .append(", ")
                                            .append(Component.literal("Spirit Chance").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)))
                                            .append(", and ")
                                            .append(Component.literal("Wayfinder Data").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)))
                                            .append(" for that catch.")
                                    )
                    )));
            case "Graceful Rod" -> Component.literal("").append(Component.literal("").append(
                    Component.literal("\uE117").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withFont(
                        new FontDescription.Resource(Identifier.fromNamespaceAndPath("cfm", "icon")))))).setStyle(Style.EMPTY.withHoverEvent(
                    new HoverEvent.ShowText(
                            Component.literal("Graceful Rod Perk\n\n").setStyle(Style.EMPTY.withColor(0x8833FF))
                                    .append(Component.literal("When triggered, cancels the decrease in ").setStyle(Style.EMPTY.withColor(0x65FFFF))
                                            .append(Component.literal("Stock").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)))
                                            .append(" of your fishing spot for that catch."))
                    )
            ));
            case "Glitched Rod" -> Component.literal("").append(Component.literal("").append(
                    Component.literal("\uE11B").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withFont(
                        new FontDescription.Resource(Identifier.fromNamespaceAndPath("cfm", "icon")))))).setStyle(Style.EMPTY.withHoverEvent(
                    new HoverEvent.ShowText(
                            Component.literal("Glitched Rod Perk\n\n").setStyle(Style.EMPTY.withColor(0xFF7E40))
                                    .append(Component.literal("When triggered, your catch counts twice towards your ").setStyle(Style.EMPTY.withColor(0x65FFFF))
                                            .append(Component.literal("Research").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)))
                                            .append("."))
                    )
            ));
            case "Stable Rod" -> Component.literal("").append(Component.literal("").append(
                    Component.literal("\uE11F").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withFont(
                        new FontDescription.Resource(Identifier.fromNamespaceAndPath("cfm", "icon")))))).setStyle(Style.EMPTY.withHoverEvent(
                    new HoverEvent.ShowText(
                            Component.literal("Stable Rod Perk\n\n").setStyle(Style.EMPTY.withColor(0x23C725))
                                    .append(Component.literal("When triggered, cancels the loss of ").setStyle(Style.EMPTY.withColor(0x65FFFF))
                                            .append(Component.literal("Stability").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)))
                                            .append(" of the Grotto for that catch."))
                    )
            ));
            case "XP Magnet" -> Component.literal("").append(Component.literal("").append(
                    Component.literal("\uE10E").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withFont(
                        new FontDescription.Resource(Identifier.fromNamespaceAndPath("cfm", "icon")))))).setStyle(Style.EMPTY.withHoverEvent(
                    new HoverEvent.ShowText(
                            Component.literal("XP Magnet Perk\n\n").setStyle(Style.EMPTY.withColor(ChatFormatting.RED))
                                    .append(Component.literal("Increases your chances of ").setStyle(Style.EMPTY.withColor(0x65FFFF))
                                            .append(Component.literal("Multiplying").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)))
                                            .append(" the ")
                                            .append(Component.literal("Island XP").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)))
                                            .append(" you earn from fishing catches."))
                    )
            ));
            case "Fish Magnet" -> Component.literal("").append(Component.literal("").append(
                    Component.literal("\uE112").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withFont(
                        new FontDescription.Resource(Identifier.fromNamespaceAndPath("cfm", "icon")))))).setStyle(Style.EMPTY.withHoverEvent(
                    new HoverEvent.ShowText(
                            Component.literal("Fish Magnet Perk\n\n").setStyle(Style.EMPTY.withColor(0x219BF3))
                                    .append(Component.literal("When you catch a ").setStyle(Style.EMPTY.withColor(0x65FFFF))
                                            .append(Component.literal("Fish").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)))
                                            .append(", increases the chance to ")
                                            .append(Component.literal("Multiply").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)))
                                            .append(" the amount."))
                    )
            ));
            case "Pearl Magnet" -> Component.literal("").append(Component.literal("").append(
                    Component.literal("\uE116").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withFont(
                        new FontDescription.Resource(Identifier.fromNamespaceAndPath("cfm", "icon")))))).setStyle(Style.EMPTY.withHoverEvent(
                    new HoverEvent.ShowText(
                            Component.literal("Pearl Magnet Perk\n\n").setStyle(Style.EMPTY.withColor(0x8833FF))
                                    .append(Component.literal("When you catch a ").setStyle(Style.EMPTY.withColor(0x65FFFF))
                                            .append(Component.literal("Pearl").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)))
                                            .append(", increases the chance to ")
                                            .append(Component.literal("Multiply").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)))
                                            .append(" the amount."))
                    )
            ));
            case "Treasure Magnet" -> Component.literal("").append(Component.literal("").append(
                    Component.literal("\uE11A").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withFont(
                        new FontDescription.Resource(Identifier.fromNamespaceAndPath("cfm", "icon")))))).setStyle(Style.EMPTY.withHoverEvent(
                    new HoverEvent.ShowText(
                            Component.literal("Treasure Magnet Perk\n\n").setStyle(Style.EMPTY.withColor(0xFF7E40))
                                    .append(Component.literal("When you catch a ").setStyle(Style.EMPTY.withColor(0x65FFFF))
                                            .append(Component.literal("Trevor").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)))
                                            .append(", increases the chance to ")
                                            .append(Component.literal("Multiply").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)))
                                            .append(" the amount."))
                    )
            ));
            case "Spirit Magnet" -> Component.literal("").append(Component.literal("").append(
                    Component.literal("\uE11E").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withFont(
                        new FontDescription.Resource(Identifier.fromNamespaceAndPath("cfm", "icon")))))).setStyle(Style.EMPTY.withHoverEvent(
                    new HoverEvent.ShowText(
                            Component.literal("Spirit Magnet Perk\n\n").setStyle(Style.EMPTY.withColor(0x23C725))
                                    .append(Component.literal("When you catch a ").setStyle(Style.EMPTY.withColor(0x65FFFF))
                                            .append(Component.literal("Spirit").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)))
                                            .append(", increases the chance to ")
                                            .append(Component.literal("Multiply").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)))
                                            .append(" the amount."))
                    )
            ));
            case "Supply Preserve" -> Component.literal("").append(Component.literal("").append(
                    Component.literal("\uE121").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withFont(
                        new FontDescription.Resource(Identifier.fromNamespaceAndPath("cfm", "icon")))))).setStyle(Style.EMPTY.withHoverEvent(
                    new HoverEvent.ShowText(
                            Component.literal("Supply Preserve Perk\n\n").setStyle(Style.EMPTY.withColor(ChatFormatting.RED))
                                    .append(Component.literal("When triggered, prevents all equipped consumables from losing a use.").setStyle(
                                            Style.EMPTY.withColor(0x65FFFF)))
                    )
            ));
            case "Elusive Catch" -> Component.literal("").append(Component.literal("").append(
                    Component.literal("\uE065").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withFont(
                        new FontDescription.Resource(Identifier.fromNamespaceAndPath("cfm", "icon")))))).setStyle(Style.EMPTY.withHoverEvent(
                    new HoverEvent.ShowText(
                            Component.literal("You caught an Elusive Fish!\n\n").setStyle(Style.EMPTY.withColor(0x55FF56))
                                    .append(Component.literal("Elusive").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)))
                                    .append(" ")
                                    .append(Component.literal("fish are extra rare, and are worth ").setStyle(Style.EMPTY.withColor(0x65FFFF))
                                            .append(Component.literal("3x").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)))
                                            .append(" more than normal fish."))
                    )
            ));

            default -> Component.literal(name);
        };

        TRIGGER_TEXTS.put(name, finalText);
    }

    private static void registerCategory(CategoryType category) {
        Component text = switch (category) {
            case JUNK -> Component.empty().append(Component.literal("(").setStyle(Style.EMPTY.withColor(0x23D106)))
                    .append(Component.literal("\uE185").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withFont(
                        new FontDescription.Resource(Identifier.fromNamespaceAndPath("cfm", "icon")))));
            case NORMAL_FISH -> Component.empty().append(Component.literal("(").setStyle(Style.EMPTY.withColor(0x23D106)))
                    .append(Component.literal("\uE0DE").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withFont(
                        new FontDescription.Resource(Identifier.fromNamespaceAndPath("cfm", "icon")))));
            case ELUSIVE_FISH -> Component.empty().append(Component.literal("(").setStyle(Style.EMPTY.withColor(0x23D106)))
                    .append(Component.literal("\uE000").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withFont(
                        new FontDescription.Resource(Identifier.fromNamespaceAndPath("cfm", "icon")))));
            case PEARL -> Component.empty().append(Component.literal("(").setStyle(Style.EMPTY.withColor(0x23D106)))
                    .append(Component.literal("\uE180").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withFont(
                        new FontDescription.Resource(Identifier.fromNamespaceAndPath("cfm", "icon")))));
            case TREASURE -> Component.empty().append(Component.literal("(").setStyle(Style.EMPTY.withColor(0x23D106)))
                    .append(Component.literal("\uE201").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withFont(
                        new FontDescription.Resource(Identifier.fromNamespaceAndPath("cfm", "icon")))));
            case SPIRIT -> Component.empty().append(Component.literal("(").setStyle(Style.EMPTY.withColor(0x23D106)))
                    .append(Component.literal("\uE103").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withFont(
                            new FontDescription.Resource(Identifier.fromNamespaceAndPath("cfm", "icon")))));
        };

        CATEGORY_TEXTS.put(category.name(), text);
    }

    public static Component get(String name) {
        return TRIGGER_TEXTS.get(name);
    }

    public static Component getCategory(CategoryType category) {
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
