package milmit.advancegram.messenger.camera;

public class VideoUtils {
    enum Quality {
        UNKNOWN,
        SD,
        HD,
        FULL_HD,
        QUAD_HD,
        ULTRA_HD;

        static Quality fromInt(int i) {
            return values()[i];
        }
    }

    public static float getMaxSize(int w, int h, int selectedCompression) {
        float ratio = (float) w / (float) h;
        switch (Quality.fromInt(selectedCompression)) {
            case SD:
                return getNewSide(480, ratio);
            case HD:
                return getNewSide(720, ratio);
            case FULL_HD:
                return getNewSide(1080, ratio);
            case QUAD_HD:
                return getNewSide(1440, ratio);
            case ULTRA_HD:
                return getNewSide(2160, ratio);
            default:
                return getNewSide(270, ratio);
        }
    }

    public static int getCompressionsCount(int width, int height) {
        return getCompressionsCount(width * height).ordinal() + 1;
    }

    private static Quality getCompressionsCount(int area) {
        if (area >= 3840 * 2160) {
            return Quality.ULTRA_HD;
        } else if (area >= 2560 * 1440) {
            return Quality.QUAD_HD;
        } else if (area >= 1920 * 1080) {
            return Quality.FULL_HD;
        } else if (area >= 1280 * 720) {
            return Quality.HD;
        } else if (area >= 854 * 480) {
            return Quality.SD;
        } else {
            return Quality.UNKNOWN;
        }
    }

    private static float getNewSide(int side, float ratio) {
        if (ratio > 1) {
            return side * ratio;
        } else {
            return side / ratio;
        }
    }
}
