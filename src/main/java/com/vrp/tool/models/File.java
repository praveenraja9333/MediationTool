package com.vrp.tool.models;

import java.io.Serializable;
import java.util.Objects;

public class File implements Serializable,Comparable {
    long id;
    String name;

    public File(String name,long id){
        this.name=name;
        this.id=id;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if(o instanceof String)return Objects.equals(name,o);
        File file = (File) o;
        return Objects.equals(name, file.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int compareTo(Object o) {
        if(o==null)return 1;
        if(this==o)return 0;
        if(o instanceof String){
            return this.name.compareTo((String) o);
        }
        if(o instanceof File)
            return this.name.compareTo(((File) o).name);
        return 1;
    }
}
