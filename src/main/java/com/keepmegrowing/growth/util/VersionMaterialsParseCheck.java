package com.keepmegrowing.growth.util;

/** Fails if year-based version parsing breaks (26.3, pre-release suffixes). */
public final class VersionMaterialsParseCheck {
    public static void main(String[] args) {
        assert "26.3".equals(VersionMaterials.normalizeGameVersion("26.3"));
        assert "26.3".equals(VersionMaterials.normalizeGameVersion("26.3-rc2"));
        assert "26.3".equals(VersionMaterials.normalizeGameVersion("26.3.build.12-alpha"));
        assert "1.21".equals(VersionMaterials.normalizeGameVersion("1.21.4"));
    }

    private VersionMaterialsParseCheck() {}
}
