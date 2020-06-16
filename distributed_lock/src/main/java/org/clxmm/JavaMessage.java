package org.clxmm;

/**
 * @author clx
 * @date 2020-06-16 20:58
 */
public class JavaMessage {


    private String id;

    private Object data;


    @Override
    public String toString() {
        return "JavaMessage{" +
                "id='" + id + '\'' +
                ", data=" + data +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
