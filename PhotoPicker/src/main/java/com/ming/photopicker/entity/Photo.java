package com.ming.photopicker.entity;

/**
 * Created by donglua on 15/6/30.
 */
public class Photo {

    private int id;
    private String path;

    private String thumbnail;

    public Photo(int id, String path) {
        this(id, path, null);
    }

    public Photo(String path, String thumbnail) {
        this(0, path, thumbnail);
    }

    public Photo(int id, String path, String thumbnail) {
        this.id = id;
        this.path = path;
        this.thumbnail = thumbnail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Photo)) return false;

        Photo photo = (Photo) o;

        if (id >= 0 && photo.id >= 0) {
            return id == photo.id;
        }
        if (path != null && path.equals(photo.getPath())) {
            return true;
        }
        return this == photo;
    }

    @Override
    public int hashCode() {
        if (id > 0) {
            return id;
        }
        return super.hashCode();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
