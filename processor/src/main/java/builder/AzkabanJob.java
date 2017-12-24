package builder;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Inherited
public @interface AzkabanJob {
    String descryption();
    String memory() default "1g";
}
