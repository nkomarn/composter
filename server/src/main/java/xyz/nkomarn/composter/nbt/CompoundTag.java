package xyz.nkomarn.composter.nbt;

import java.util.Collections;
import java.util.Map;

public class CompoundTag extends Tag {

    private final Map<String, Tag> value;

    public CompoundTag(final String name, Map<String, Tag> value) {
        super(name);
        this.value = Collections.unmodifiableMap(value);
    }

    @Override
    public Map<String, Tag> getValue() {
        return value;
    }

    @Override
    public String toString() {
        String name = getName();
        String append = "";
        if (name != null && !name.equals("")) {
            append = "(\"" + this.getName() + "\")";
        }

        StringBuilder bldr = new StringBuilder();
        bldr.append("TAG_Compound" + append + ": " + value.size() + " entries\r\n{\r\n");
        for (Map.Entry<String, Tag> entry : value.entrySet()) {
            bldr.append("   " + entry.getValue().toString().replaceAll("\r\n", "\r\n   ") + "\r\n");
        }
        bldr.append("}");
        return bldr.toString();
    }
}
