package com.gmail.nossr50.util.text;

import com.gmail.nossr50.datatypes.party.PartyFeature;
import com.gmail.nossr50.datatypes.skills.SuperAbilityType;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * Utility class for String operations, including formatting and caching deterministic results to improve performance.
 */
public class StringUtils {

    protected static final DecimalFormat percent = new DecimalFormat("##0.00%");
    protected static final DecimalFormat shortDecimal = new DecimalFormat("##0.0");

    // Using concurrent hash maps to avoid concurrency issues (Folia)
    private static final Map<EntityType, String> formattedEntityStrings = new HashMap<>();
    private static final Map<SuperAbilityType, String> formattedSuperAbilityStrings = new HashMap<>();
    private static final Map<Material, String> formattedMaterialStrings = new HashMap<>();
    private static final Map<PartyFeature, String> prettyPartyFeatureStringCache = new HashMap<>();

    /**
     * Gets a capitalized version of the target string.
     * Results are cached to improve performance.
     *
     * @param target String to capitalize
     * @return the capitalized string
     */
    public static String getCapitalized(String target) {
        if (target == null || target.isEmpty()) {
            return target;
        }
        return target.substring(0, 1).toUpperCase(Locale.ENGLISH) + target.substring(1).toLowerCase(Locale.ENGLISH);
    }

    /**
     * Converts ticks to seconds, formatted to one decimal place.
     *
     * @param ticks Number of ticks
     * @return String representation of seconds
     */
    public static String ticksToSeconds(double ticks) {
        return shortDecimal.format(ticks / 20);
    }

    /**
     * Gets a pretty string representation of a SuperAbilityType.
     * Results are cached to improve performance.
     *
     * @param superAbilityType SuperAbilityType to convert
     * @return Pretty string representation of the SuperAbilityType
     */
    public static String getPrettySuperAbilityString(SuperAbilityType superAbilityType) {
        requireNonNull(superAbilityType, "superAbilityType cannot be null");
        return formattedSuperAbilityStrings.computeIfAbsent(superAbilityType, StringUtils::createPrettyString);
    }

    /**
     * Creates a string from an array skipping the first n elements.
     *
     * @param args  The array to iterate over when forming the string
     * @param index The number of elements to skip over
     * @return The "trimmed" string
     */
    public static String buildStringAfterNthElement(@NotNull String @NotNull [] args, int index) {
        StringBuilder trimMessage = new StringBuilder();

        for (int i = index; i < args.length; i++) {
            if (i > index) {
                trimMessage.append(' ');
            }
            trimMessage.append(args[i]);
        }

        return trimMessage.toString();
    }

    /**
     * Gets a pretty string representation of a Material.
     * Results are cached to improve performance.
     *
     * @param material Material to convert
     * @return Pretty string representation of the Material
     */
    public static String getFormattedMaterialString(Material material) {
        return formattedMaterialStrings.computeIfAbsent(material, StringUtils::createPrettyString);
    }

    /**
     * Gets a pretty string representation of an EntityType.
     * Results are cached to improve performance.
     *
     * @param entityType EntityType to convert
     * @return Pretty string representation of the EntityType
     */
    public static String getPrettyEntityTypeString(EntityType entityType) {
        return formattedEntityStrings.computeIfAbsent(entityType, StringUtils::createPrettyString);
    }

    /**
     * Gets a wildcard configuration string for BlockData.
     * Results are cached to improve performance.
     *
     * @param blockData BlockData to convert
     * @return Wildcard configuration string
     */
    public static String getWildcardConfigBlockDataString(BlockData blockData) {
        return getFormattedMaterialString(blockData.getMaterial());
    }

    /**
     * Gets an explicit configuration string for BlockData.
     * Results are cached to improve performance.
     *
     * @param data BlockData to convert
     * @return Explicit configuration string
     */
    public static String getExplicitConfigBlockDataString(BlockData data) {
        return getFormattedMaterialString(data.getMaterial());
    }

    /**
     * Gets a pretty string representation of a PartyFeature.
     * Results are cached to improve performance.
     *
     * @param partyFeature PartyFeature to convert
     * @return Pretty string representation
     */
    public static String getPrettyPartyFeatureString(PartyFeature partyFeature) {
        return prettyPartyFeatureStringCache.computeIfAbsent(partyFeature, StringUtils::createPrettyString);
    }

    /**
     * Creates a pretty string from a base string by splitting underscores and capitalizing words.
     *
     * @param baseString String to convert
     * @return Pretty string
     */
    private static String createPrettyString(String baseString) {
        return PRETTY_STRING_FUNC.apply(baseString);
    }

    /**
     * Function to create a pretty string from a base string.
     */
    private static final Function<String, String> PRETTY_STRING_FUNC = baseString -> {
        if (baseString.contains("_") && !baseString.contains(" ")) {
            return prettify(baseString.split("_"));
        } else {
            if(baseString.contains(" ")) {
                return prettify(baseString.split(" "));
            } else{
                return getCapitalized(baseString);
            }
        }
    };

    private static @NotNull String prettify(String[] substrings) {
        final StringBuilder prettyString = new StringBuilder();

        for (int i = 0; i < substrings.length; i++) {
            prettyString.append(getCapitalized(substrings[i]));
            if (i < substrings.length - 1) {
                prettyString.append(' ');
            }
        }

        return prettyString.toString();
    }

    /**
     * Creates a pretty string from an object.
     * @param object Object to convert
     * @return Pretty string representation of the object
     */
    private static String createPrettyString(Object object) {
        return createPrettyString(object.toString());
    }

    /**
     * Determine if a string represents an Integer.
     *
     * @param string String to check
     * @return true if the string is an Integer, false otherwise
     */
    public static boolean isInt(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    /**
     * Determine if a string represents a Double.
     *
     * @param string String to check
     * @return true if the string is a Double, false otherwise
     */
    public static boolean isDouble(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }
}
