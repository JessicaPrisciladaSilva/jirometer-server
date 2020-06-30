package ut.br.com.nubank;

import org.junit.Test;
import br.com.nubank.api.MyPluginComponent;
import br.com.nubank.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}