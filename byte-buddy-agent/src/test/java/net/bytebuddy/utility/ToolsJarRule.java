package net.bytebuddy.utility;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.logging.Logger;

public class ToolsJarRule implements MethodRule {

    public static final String JAVA_HOME_PROPERTY = "java.home";

    public static final String TOOLS_JAR_LOCATION = "/../lib/tools.jar";

    public static final String VIRTUAL_MACHINE_TYPE = "com/sun/tools/attach/VirtualMachine.class";

    private final boolean openJDK;

    public ToolsJarRule() {
        openJDK = new File(System.getProperty(JAVA_HOME_PROPERTY).replace('\\', '/') + TOOLS_JAR_LOCATION).isFile();
    }

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        return openJDK || method.getAnnotation(Enforce.class) == null
                ? base
                : new NoOpStatement();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface Enforce {
        /* empty */
    }

    private static class NoOpStatement extends Statement {

        @Override
        public void evaluate() throws Throwable {
            Logger.getAnonymousLogger().warning("Ignored test case that requires an OpenJDK installation with tools.jar");
        }
    }
}
