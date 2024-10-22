package app.security.entities;

public interface ISecurityUser {
    boolean verifyPassword(String pw);
    void addRole(Role role);
}