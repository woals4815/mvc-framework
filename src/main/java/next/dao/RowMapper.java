package next.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class RowMapper {
    abstract <T> T mapRow(ResultSet rs) throws SQLException;
}
