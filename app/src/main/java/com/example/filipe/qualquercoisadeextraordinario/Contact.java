package com.example.filipe.qualquercoisadeextraordinario;

/**
 * Created by Filipe on 12/05/2017.
 */

public class Contact {

    String name;
    String Password;
    String PoolName;


    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setPassword(String Password)
    {
        this.Password = Password;
    }

    public String getPassword()
    {
        return this.Password;
    }

    public void setPoolName(String PoolName)
    {
        this.PoolName = PoolName;
    }

    public String getPoolName()
    {
        return this.PoolName;
    }

}
