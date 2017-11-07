/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flightsystem;

/**
 *
 * @author dg71532
 */
public class ComboItem
{
    private String key;
    private String value;

    public ComboItem(String code, String airport)
    {
        this.key = code;
        this.value = airport;
    }

    ComboItem() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString()
    {
        return key+"-"+value;
    }

    public String getKey()
    {
        return key;
    }

    public String getValue()
    {
        return value;
    }
}