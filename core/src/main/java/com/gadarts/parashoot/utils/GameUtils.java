package com.gadarts.parashoot.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.gadarts.parashoot.model.GameObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by gadw1_000 on 27-Apr-15.
 */
public final class GameUtils {

    public static float fixedDegrees(float direction) {
        while (direction < 0) {
            direction += 360;
        }
        while (direction >= 360) {
            direction -= 360;
        }
        return direction;
    }

    public static String lowerCaseFirst(String string) {
        char c[] = string.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c);
    }

    public static float getDistanceBetweenObjectToPoint(GameObject object, float x2, float y2) {
        return getDistanceBetweenTwoPoints(object.getX(), object.getY(), x2, y2);
    }

    public static float getDistanceBetweenTwoPoints(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public static float getDistanceBetweenTwoObjects(GameObject object1, GameObject object2) {
        return (float) Math.sqrt((object1.getX() - object2.getX()) * (object1.getX() - object2.getX()) + (object1.getY() - object2.getY()) * (object1.getY() - object2.getY()));
    }

    static public float getDirectionToPoint(float x1, float y1, float x2, float y2) {
        return (float) (180 + MathUtils.radiansToDegrees * Math.atan2(y1 - y2, x1 - x2));
    }

    static public float getDirectionToPoint(float x1, float y1, GameObject gameObject) {
        return getDirectionToPoint(x1, y1, gameObject.getX(), gameObject.getY());
    }

    static public float getDirectionToPoint(GameObject gameObject, float x1, float y1) {
        return getDirectionToPoint(gameObject.getX(), gameObject.getY(), x1, y1);
    }

    static public float getDirectionToPoint(GameObject gameObject1, GameObject gameObject2) {
        return getDirectionToPoint(gameObject1.getX(), gameObject1.getY(), gameObject2.getX(), gameObject2.getY());
    }

    public static long getLocalUnixEpochSeconds() {
        return TimeUtils.millis() / 1000;
    }

    public static void getUnixTimeFromServerSeconds() {
        Net.HttpRequest httpRequest = new Net.HttpRequest(Net.HttpMethods.GET);
        httpRequest.setUrl(Rules.System.TIME_EPOCH_URL);
        Net.HttpResponseListener request = new Net.HttpResponseListener() {

            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                final InputStream resultAsStream = httpResponse.getResultAsStream();
                byte[] temp = new byte[100];
                Long startTimeStampSeconds;
                try {
                    int numberOfBytes = resultAsStream.read(temp);
                    byte[] timeArray = new byte[numberOfBytes];
                    for (int i = 0; i < numberOfBytes; i++) {
                        timeArray[i] = temp[i];
                    }
                    startTimeStampSeconds = new Long(new String(timeArray));
                } catch (IOException e) {
                    startTimeStampSeconds = GameUtils.getLocalUnixEpochSeconds();
                }
                long millis = GameUtils.getLocalUnixEpochSeconds();
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("LOCAL CLOCK:", GameUtils.getLocalUnixEpochSeconds() + "");
            }

            @Override
            public void cancelled() {
                Gdx.app.log("LOCAL CLOCK:", GameUtils.getLocalUnixEpochSeconds() + "");
            }
        };
        Gdx.net.sendHttpRequest(httpRequest, request);
    }

    public static boolean isFullyOutsideScreen(float x, float y, float width, float height) {
        return x + width <= 0 || y + height <= 0 || x >= Rules.System.Resolution.WIDTH_TARGET_RESOLUTION || y >= Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION;
    }

    public static int getIndexOfValueInArray(Object[] array, Object object) {
        for (int i = 0; i < array.length; i++) {
            Object obj = array[i];
            if (object == obj || object.equals(obj)) {
                return i;
            }
        }
        return -1;
    }

    public static CharSequence milliSecondsToTime(long timeStamp) {
        float timeStampInSeconds = ((float) timeStamp) / 1000;
        int minutes = (int) (timeStampInSeconds / 60);
        double v = (((double) timeStampInSeconds) / 60);
        int seconds = (int) ((v - minutes) * 60);
        return minutes + ":" + seconds;
    }

    public static float realSpeed(float speed, float delta) {
        return (speed * Rules.System.OBJECT_SPEED_MULTIPLICATION) * delta;
    }
}
