package club.tempvs.image.util;

public interface ObjectFactory {
    <T> T getInstance(Class<T> clazz, Object... args);
}
