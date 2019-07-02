package edu.kit.checkstyle.checks;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class CollectionUtils {

    @SafeVarargs
    public static Set mkSet(Object... as) {
        return Sets.newHashSet(as);
    }

    @SafeVarargs
    public static List mkList(Object... as) {
        return Lists.newArrayList(as);
    }

    public static Map mkMap() {
        return Maps.newHashMap();
    }
}
