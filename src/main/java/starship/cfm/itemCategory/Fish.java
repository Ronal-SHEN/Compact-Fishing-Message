package starship.cfm.itemCategory;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class Fish {
    public static final Set<String> ELUSIVE_ITEMS = new HashSet<>();

    public static final Set<String> ancient_sands_elusive = Set.of(
            "Ancient Trevally", "Clown Triggerfish", "Desert Mackerel", "Royal Gramma", "Sandy Salmon", "Wreckfish");
    public static final Set<String> ancient_sands_normal = Set.of(
            "Ancient fangtooth", "Baked sunfish", "Bone swordfish", "Cacti pufferfish", "Desert clownfish", "Ghostfin",
            "Gobi goby", "Moonfin flounder", "Sand snook", "Speckled grouper", "Summer sprat", "Sunny sardine"
    );


    public static final Set<String> ashen_wastes_elusive = Set.of(
            "Ashen Gourami", "Boiling Boxfish", "Driftfish Raider", "Emberpike", "Volcanic Butterflyfish", "Volcanic Goldfish");
    public static final Set<String> ashen_wastes_normal = Set.of(
            "Ashen grouper", "Ashen sturgeon", "Ashen tuna", "Black velvetfish", "Blackmouth salmon", "Charred char",
            "Hot cod", "Volcanic arowana", "Volcanic piranha", "Volcanic remora", "Volcanic spikefish", "Wailing walleye");


    public static final Set<String> blazing_canyon_elusive = Set.of(
            "Barrockuda", "Burnt Basa", "Canyon Perch", "Congo Tetra", "Golden Trout", "Phoenix Angelfish");
    public static final Set<String> blazing_canyon_normal = Set.of(
            "Blazing betta", "Blazing gourami", "Flame angelfish", "Golden cichlid", "Golden snapper", "Goldfish",
            "Mesa flounder", "Painted discus", "Scorpionfish", "Solar mackerel", "Sunburnt goby", "Sunburnt surgeonfish");


    public static final Set<String> coral_shores_elusive = Set.of(
            "Coral Angelfish", "Coral Beauty", "Gorgonian Grouper", "Mahi Mahi", "Pearlescent Gourami", "Staghorn Flounder");
    public static final Set<String> coral_shores_normal = Set.of(
            "Banggai cardinalfish", "Braincoral betta", "Disco discus", "Hammerhead haddock", "Midnight mbunas", "Neon tetra",
            "Ocean sunfish", "Rainbow kribensis", "Reef anchovy", "Serpae tetra", "Sugarload pufferfish", "Tropical baracuda");


//    public static final Set<String> crab_collection = Set.of("anemone crab", "ashen spidercrab", "blazing crab", "boulder crab", "boxer crab", "bubble crab", "celestial crab", "cinder crab", "claw crab", "cluster crab", "coal crab", "coconut crab", "coral crab", "cremini crab", "cursed crab", "fairy crab", "floaty crab", "floral crab", "ghostly crab", "grave crab", "horseshoe crab", "mosaic crab", "mossy crab", "mud crab", "oilspill crab", "orange crab", "peekytoe crab", "pineapple crab", "prismarine crab", "queen crab", "runic crab", "sandking crab", "sandy crab", "scarab crab", "scorpion crab", "shimmer crab", "spiney crab", "spore crab", "spotted crab", "thorny crab", "twisted spidercrab", "vampire crab");

    public static final Set<String> dark_grove_elusive = Set.of(
            "Emperor Angelfish", "Fungal Salmon", "Queenfish", "Snaggletooth Gar", "Troutarium", "Agaric Tarpon");
    public static final Set<String> dark_grove_normal = Set.of(
            "Frilled mackerel", "Fungal flounder", "Fungal tarpon", "Grove baracuda", "Grove cod",
            "Hodhedge fish", "Midnight gourami", "Midnight tang", "Nightmare marlin", "Portobello grouper", "Sprout trout",
            "Walking catfish");


//    public static final Set<String> desert_ruins = Set.of(
//            "ancient fangtooth", "ancient trevally", "baked sunfish", "bone swordfish", "cacti pufferfish",
//            "desert goldfish", "desert mackerel", "gobi goby", "moonfish flounder", "royal gramma", "sand salmon",
//            "sand snook", "speckled grouper", "sunburnt goby", "sunny sardine");


    public static final Set<String> floral_forest_elusive = Set.of(
            "Clover Koi", "Lavender Leatherneck", "Napoleonfish", "Poppy Clownfish", "Rainbow Trout", "Tulapia");
    public static final Set<String> floral_forest_normal = Set.of(
            "Blossom betta", "Dahliafish", "Daisy pike", "Floral anchovy", "Flowerhorn cichlid", "Giant treevalley",
            "Lime sole", "Lotus tuna", "Maple fangtooth", "Olive flounder", "Peacock cichlid", "Temperate remora");


    public static final Set<String> mirrored_oasis_elusive = Set.of(
            "Coral Cod", "Cosmic Cod", "Glass Pike", "Pearlescent Betta", "Rose Goldfish", "Treasured Tilapia");
    public static final Set<String> mirrored_oasis_normal = Set.of(
            "Crystalline cod", "Diamond oarfish", "Faint pike", "Golden tuna", "Midnight koi", "Midnight sprat",
            "Mirrored mahi", "Mosaic guppy", "Obsidian guppy", "Ruby remora", "Sapphire salmon", "Silver snook");


    public static final Set<String> sunken_swamp_elusive = Set.of(
            "Gloopy Gar", "Glowberry Guppy", "Lemon Sole", "Mutated Marlin", "Overgrown Grouper", "Shardine");
    public static final Set<String> sunken_swamp_normal = Set.of(
            "Ancient snapper", "Boggy bass", "Everglade gourami", "Fern flounder", "Floodfish", "Greengill",
            "Pale minnow", "Quagmire catfish", "Rusted pike", "Sunken knifejaw", "Sunken koi", "Swampy betta");


    public static final Set<String> tropical_undergrowth_elusive = Set.of(
            "Binding Jellyfish", "Butterfly Fish", "Emerald Jewelfish", "Jungle Clownfish", "Jungle Fangtooth", "Zebra Turkeyfish");
    public static final Set<String> tropical_undergrowth_normal = Set.of(
            "Bluegill", "Daintree guppy", "Jagged zebrafish", "Parrotfish", "Pineapple piranha", "Red snapper",
            "Sinharaja salmon", "Surgeonfish", "Tiger mackerel", "Viney perch", "Viperfish", "Zebrafish");


    public static final Set<String> twisted_swamp_elusive = Set.of(
            "Alligator Gar", "Goliath Tigerfish", "Grave Gudgeon", "Green Terror Cichlid", "Mud Pike", "Mudfin Catfish");
    public static final Set<String> twisted_swamp_normal = Set.of(
            "Autumn sprat", "Barred knifejaw", "Gloom mullet", "Gloopy goldfish", "Mimifry", "Mud minnow",
            "Ocean moonfish", "Snake mackerel", "Tiny mudskipper", "Twisted fangtooth", "Twisted koi", "Twisted sturgeon");


    public static final Set<String> verdant_woods_elusive = Set.of(
            "Dorado", "Foxfish", "Mossy Mullet", "Stone Carp", "Stone Salmon", "Tarnished Goldfish");
    public static final Set<String> verdant_woods_normal = Set.of(
            "Arowana", "Black batbass", "Branch snapper", "Fairy goldfish", "Forest cod", "Mossy mackerel",
            "Plain goby", "Spring sprat", "Stringfish", "Sunset fish", "Tilapia", "Tree perch"
    );


    public static final Set<String> volcanic_springs_elusive = Set.of(
            "Ashen Goliath", "Basalt Discus", "Flying Flashfish", "Heated Haddock", "Molten Goldfish", "Torch Tarpon");
    public static final Set<String> volcanic_springs_normal = Set.of(
            "Ashen koi", "Ashen tilapia", "Coal cod", "Dark ocean salmonidae", "Geyser gudgeon", "Lava goby",
            "Molten anchovy", "Molten triggerfish", "Pompeii pompano", "Scalding mullet", "Volcanic surgeonfish", "Wobbegong fish"
    );

    public static final Pattern ELUSIVE_PATTERN;

    static {
        ELUSIVE_ITEMS.addAll(verdant_woods_elusive);
        ELUSIVE_ITEMS.addAll(floral_forest_elusive);
        ELUSIVE_ITEMS.addAll(dark_grove_elusive);
        ELUSIVE_ITEMS.addAll(sunken_swamp_elusive);

        ELUSIVE_ITEMS.addAll(tropical_undergrowth_elusive);
        ELUSIVE_ITEMS.addAll(coral_shores_elusive);
        ELUSIVE_ITEMS.addAll(twisted_swamp_elusive);
        ELUSIVE_ITEMS.addAll(mirrored_oasis_elusive);

        ELUSIVE_ITEMS.addAll(ancient_sands_elusive);
        ELUSIVE_ITEMS.addAll(blazing_canyon_elusive);
        ELUSIVE_ITEMS.addAll(ashen_wastes_elusive);
        ELUSIVE_ITEMS.addAll(volcanic_springs_elusive);

        StringBuilder regex = new StringBuilder();
        for (String item : ELUSIVE_ITEMS) {
            if (!regex.isEmpty()) regex.append("|");
            regex.append(Pattern.quote(item));
        }
        ELUSIVE_PATTERN = Pattern.compile(regex.toString());
    }

}
