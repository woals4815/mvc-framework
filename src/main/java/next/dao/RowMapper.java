package next.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class RowMapper {
    abstract Object mapRow(ResultSet rs) throws SQLException;
}
