package gui;

import java.sql.SQLException;

public interface ICallbackReceiver
{
    void callback(Object caller) throws SQLException;
}
