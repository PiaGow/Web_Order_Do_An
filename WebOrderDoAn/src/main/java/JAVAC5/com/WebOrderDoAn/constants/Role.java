package JAVAC5.com.WebOrderDoAn.constants;

import lombok.AllArgsConstructor;
@AllArgsConstructor
public enum Role {
    ADMIN(1),
    MANAGER(2),
    USER(3);
    public final long value;
}
