package com.holley.mvc.model.def;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

public class CurrentUser implements Serializable {

    private static final long serialVersionUID = -3712199821364173263L;
    private String            userName;
    private Set<String>       permissions      = new TreeSet<String>();

    public CurrentUser(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return userName;
    }

}
