
package com.keepmegrowing.databaseapi.database.sql;

import com.keepmegrowing.databaseapi.database.DatabaseSettings;
import com.keepmegrowing.databaseapi.database.objects.Table;
import org.bukkit.plugin.java.JavaPlugin;

public class SQLConfiguration {
    private String loadObjectSQL;
    private String saveObjectSQL;
    private String deleteObjectSQL;
    private String objectExistsSQL;
    private String schemaSQL;
    private String loadObjectsSQL;
    private String renameTableSQL;
    private final String tableName;
    private boolean renameRequired;
    private final String oldTableName;
    private final String TABLE_NAME = "\\[tableName]";
    private boolean useQuotes = true;

    public <T> SQLConfiguration(JavaPlugin javaPlugin, Class<T> clazz, DatabaseSettings databaseSettings) {
        this.oldTableName = databaseSettings.getDatabasePrefix() + clazz.getCanonicalName();
        this.tableName = databaseSettings.getDatabasePrefix() + (clazz.getAnnotation(Table.class) == null ? clazz.getCanonicalName() : clazz.getAnnotation(Table.class).name());
        this.renameRequired = !this.tableName.equals(this.oldTableName);
        this.schema("CREATE TABLE IF NOT EXISTS `[tableName]` (json JSON, uniqueId VARCHAR(255) GENERATED ALWAYS AS (json->\"$.uniqueId\"), UNIQUE INDEX i (uniqueId) )");
        this.loadObjects("SELECT `json` FROM `[tableName]`");
        this.loadObject("SELECT `json` FROM `[tableName]` WHERE uniqueId = ? LIMIT 1");
        this.saveObject("INSERT INTO `[tableName]` (json) VALUES (?) ON DUPLICATE KEY UPDATE json = ?");
        this.deleteObject("DELETE FROM `[tableName]` WHERE uniqueId = ?");
        this.objectExists("SELECT IF ( EXISTS( SELECT * FROM `[tableName]` WHERE `uniqueId` = ?), 1, 0)");
        this.renameTable("SELECT Count(*) INTO @exists FROM information_schema.tables WHERE table_schema = '" + databaseSettings.getDatabaseName() + "' AND table_type = 'BASE TABLE' AND table_name = '[oldTableName]'; SET @query = If(@exists=1,'RENAME TABLE `[oldTableName]` TO `[tableName]`','SELECT \\'nothing to rename\\' status'); PREPARE stmt FROM @query;EXECUTE stmt;");
    }

    public SQLConfiguration loadObject(String string) {
        this.loadObjectSQL = string.replaceFirst("\\[tableName]", this.tableName);
        return this;
    }

    public SQLConfiguration saveObject(String string) {
        this.saveObjectSQL = string.replaceFirst("\\[tableName]", this.tableName);
        return this;
    }

    public SQLConfiguration deleteObject(String string) {
        this.deleteObjectSQL = string.replaceFirst("\\[tableName]", this.tableName);
        return this;
    }

    public SQLConfiguration objectExists(String string) {
        this.objectExistsSQL = string.replaceFirst("\\[tableName]", this.tableName);
        return this;
    }

    public SQLConfiguration schema(String string) {
        this.schemaSQL = string.replaceFirst("\\[tableName]", this.tableName);
        return this;
    }

    public SQLConfiguration loadObjects(String string) {
        this.loadObjectsSQL = string.replaceFirst("\\[tableName]", this.tableName);
        return this;
    }

    public SQLConfiguration renameTable(String string) {
        this.renameTableSQL = string.replace("\\[tableName]", this.tableName).replace("\\[oldTableName\\]", this.oldTableName);
        return this;
    }

    public SQLConfiguration setUseQuotes(boolean bl) {
        this.useQuotes = bl;
        return this;
    }

    public String getLoadObjectSQL() {
        return this.loadObjectSQL;
    }

    public String getSaveObjectSQL() {
        return this.saveObjectSQL;
    }

    public String getDeleteObjectSQL() {
        return this.deleteObjectSQL;
    }

    public String getObjectExistsSQL() {
        return this.objectExistsSQL;
    }

    public String getSchemaSQL() {
        return this.schemaSQL;
    }

    public String getLoadObjectsSQL() {
        return this.loadObjectsSQL;
    }

    public String getRenameTableSQL() {
        return this.renameTableSQL;
    }

    public String getTableName() {
        return this.tableName;
    }

    public String getOldTableName() {
        return this.oldTableName;
    }

    public boolean renameRequired() {
        return this.renameRequired;
    }

    public boolean isUseQuotes() {
        return this.useQuotes;
    }
}
