package com.yxx.common.constant;

public class MySqlConstats {

    /**
     *
     * JSON_EXTRACT：从 JSON 格式的数据中提取特定的值
     * JSON_UNQUOTE：移除 JSON 字符串值外层的引号，并将其中可能包含的转义字符（如 \"、\n等）还原
     * <p>
     * -- 使用 -> 和 ->> 操作符的对比
     * SELECT profile->'$.city' FROM users;        -- 结果: "New York" (带引号)
     * SELECT profile->>'$.city' FROM users;       -- 结果: New York (无引号，更干净)
     */
    public final static String QUERY_JSON_SQL = "CASE WHEN " +
            "JSON_VALID(%s)" +
            "   THEN JSON_EXTRACT(%s, '$.%s') " +
            "   ELSE NULL " +
            "END %s";

    /**
     * 对结果进行排序
     */
    public final static String SORT_FIELD_SQL = " ORDER BY FIELD(%s, %s)";


    /**
     * 分组查询最新记录
     */
    public interface TopGroupQuery {

        String SELECT_COL = "selectCol";

        String TABLE_NAME = "tableName";

        String GROUP_COL = "groupCol";

        String ORDER_COL = "orderCol";

        String TOP_GROUP_FUNC = "SELECT ${selectCol} " +
                " FROM (" +
                "  SELECT ${selectCol}, ROW_NUMBER() OVER (PARTITION BY ${groupCol} ORDER BY ${orderCol} DESC) AS rn" +
                "  FROM ${tableName} ${ew.customSqlSegment}" +
                " ) t" +
                " WHERE rn = 1;";

        String TOP_GROUP_SQL = "SELECT u1.* " +
                " FROM tb_user u1" +
                " LEFT JOIN tb_user u2" +
                " ON u1.user_id = u2.user_id AND u1.id < u2.id" +
                " WHERE u2.id IS NULL;";
    }
}
