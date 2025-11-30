package starship.cfm.trevorOpener;

import net.minecraft.text.Text;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public abstract class Recorder {
    protected final Map<String, Integer> record = new HashMap<>();
    protected final Map<String, String> ICON_MAP = new HashMap<>();
    public Pattern namePattern = null;

    public Recorder() {
        for (String name : getNames()) {
            record.put(name, 0);
        }

        namePattern = compiledNamePattern();
    }

    public Pattern compiledNamePattern() {
        StringBuilder regex = new StringBuilder();
        for (String name : getNames()) {
            if (!regex.isEmpty()) regex.append("|");
            regex.append(Pattern.quote(name));
        }
        return Pattern.compile(regex.toString());
    }

    public void record(String name, int count) {
        if (record.containsKey(name)) {
            record.put(name, record.get(name) + count);
        }
    }

    public void reset() {
        record.replaceAll((k, v) -> 0);
    }

    public Map<String, Integer> getStats() {
        return Collections.unmodifiableMap(record);
    }

    protected abstract Set<String> getNames();

    protected abstract Text summary();

}
