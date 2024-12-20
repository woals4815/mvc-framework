package next.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class PreparedStatementSetter {
    public abstract void setValues(PreparedStatement ps) throws SQLException;
}
