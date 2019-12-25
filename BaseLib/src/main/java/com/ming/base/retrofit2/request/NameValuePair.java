package com.ming.base.retrofit2.request;

import com.ming.base.util.LangUtils;

import java.io.Serializable;

/**
 * Created by ming on 2016/11/17.
 */

public class NameValuePair implements Cloneable, Serializable {
    private static final long serialVersionUID = -6437800749411518984L;
    private final String name;
    private final String value;

    public NameValuePair(String name, String value) {
        if (name == null) {
            throw new IllegalArgumentException("Name may not be null");
        } else {
            this.name = name;
            this.value = value;
        }
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public String toString() {
        if (this.value == null) {
            return this.name;
        } else {
            int len = this.name.length() + 1 + this.value.length();
            StringBuffer buffer = new StringBuffer(len);
            buffer.append(this.name);
            buffer.append("=");
            buffer.append(this.value);
            return buffer.toString();
        }
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        } else if (this == object) {
            return true;
        } else if (!(object instanceof NameValuePair)) {
            return false;
        } else {
            NameValuePair that = (NameValuePair) object;
            return this.name.equals(that.name) && LangUtils.equals(this.value, that.value);
        }
    }

    public int hashCode() {
        byte hash = 17;
        int hash1 = LangUtils.hashCode(hash, this.name);
        hash1 = LangUtils.hashCode(hash1, this.value);
        return hash1;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
