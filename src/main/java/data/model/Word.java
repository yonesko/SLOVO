package data.model;

/**
 * Represents word that can be published to VK
 * Created by gleb on 03.05.16.
 */
public interface Word {
    boolean isPublishable();
    String toPublish();

    @Override
    String toString();
}
