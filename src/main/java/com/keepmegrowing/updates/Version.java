package com.keepmegrowing.updates;

public final class Version implements Comparable<Version> {

    public static final Version ZERO = new Version("0.0.0");

    private static final int MAX_PARTS = 4;

    private final int[] parts;

    public Version(String version) {
        if (version == null) {
            version = "0.0.0";
        }
        version = version.trim();
        if (version.startsWith("v") || version.startsWith("V")) {
            version = version.substring(1).trim();
        }
        version = version.replaceAll("[^0-9.].*$", "").replaceAll("^[^0-9]*", "");
        if (version.isEmpty()) {
            version = "0.0.0";
        }
        String[] split = version.split("\\.");
        parts = new int[MAX_PARTS];
        for (int i = 0; i < MAX_PARTS; i++) {
            if (i < split.length) {
                try {
                    parts[i] = Integer.parseInt(split[i]);
                } catch (NumberFormatException e) {
                    parts[i] = 0;
                }
            }
        }
    }

    @Override
    public int compareTo(Version other) {
        for (int i = 0; i < MAX_PARTS; i++) {
            if (parts[i] != other.parts[i]) {
                return Integer.compare(parts[i], other.parts[i]);
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            if (i > 0) {
                sb.append('.');
            }
            sb.append(parts[i]);
        }
        if (parts[3] > 0) {
            sb.append('.').append(parts[3]);
        }
        return sb.toString();
    }
}
