package me.cizezsy.yourdrawiguess;

import org.junit.Test;

import me.cizezsy.yourdrawiguess.model.message.SendToServerMessage;
import me.cizezsy.yourdrawiguess.model.Step;
import me.cizezsy.yourdrawiguess.util.JsonUtils;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        Step step = new Step();
        SendToServerMessage message = new SendToServerMessage<>(SendToServerMessage.Type.DRAW, step);
        String json = JsonUtils.toJson(message);
        System.out.println(json);
    }
}