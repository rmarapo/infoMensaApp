package mensa.info.application.org.infomensaapp.sql.model;

import java.io.Serializable;

/**
 * Creato da Giuseppe Grosso in data 18/11/15.
 */
public class Login implements Serializable
{
    private int id;
    private String data;
    private String cf;
    private String ci;
    private String createdat;

    public Login()
    {
        this(0);
    }

    public Login(int id)
    {
        this(id, null);
    }

    public Login(int id, String data)
    {
        this(id, data, null);
    }

    public Login(int id, String data, String cf)
    {
        this(id, data, cf, null);
    }

    public Login(int id, String data, String cf, String ci)
    {
        this.id = id;
        this.data = data;
        this.cf = cf;
        this.ci = ci;
    }

    public Login(String cf, String ci)
    {
        this(0, null, cf, ci);
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    public String getCf()
    {
        return cf;
    }

    public void setCf(String cf)
    {
        this.cf = cf;
    }

    public String getCi()
    {
        return this.ci;
    }

    public void setCi(String ci)
    {
        this.ci = ci;
    }

    public String getCreatedat()
    {
        return createdat;
    }

    public void setCreatedat(String createdat)
    {
        this.createdat = createdat;
    }

}
