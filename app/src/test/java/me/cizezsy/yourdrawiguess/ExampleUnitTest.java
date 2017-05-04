package me.cizezsy.yourdrawiguess;

import org.junit.Assert;
import org.junit.Test;

import me.cizezsy.yourdrawiguess.model.PlayerMessage;
import me.cizezsy.yourdrawiguess.util.JsonUtils;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String json = JsonUtils.toJson(new PlayerMessage<>(PlayerMessage.Type.DRAW, "sadasd"));
        System.out.println(json);
    }
}