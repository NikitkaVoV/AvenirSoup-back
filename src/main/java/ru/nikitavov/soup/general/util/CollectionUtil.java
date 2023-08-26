package ru.nikitavov.soup.general.util;

import java.util.*;

public class CollectionUtil {

    public static <E> List<E> asList(E... elements) {
        var list = new ArrayList<E>(elements.length);
        list.addAll(Arrays.asList(elements));
        return list;
    }

    public static <E> void insertArrayToList(List<E> list, E[] elements) {
        list.addAll(Arrays.asList(elements));
    }

    public static <E> void insertListToList(List<E> list, List<E> elements) {
        list.addAll(elements);
    }

    public static void clearOfNull(List<?> list) {
        list.removeIf(Objects::isNull);
    }

    public static <E> Set<E> mergeSets(Set<E>... sets) {
        HashSet<E> result = new HashSet<>();

        for (Set<E> set : sets) {
            result.addAll(set);
        }

        return result;
    }

    public static <K, E> Optional<E> findInMap(Map<K, E> map, K key) {
        return Optional.ofNullable(map.get(key));
    }
}
