package ProjectLib;

import java.util.Map;

public class DriverManager 
{
    private static ThreadLocal<Map<String, String>> executionParameters = new ThreadLocal<>();

    public static void setParameters(Map<String, String> params) 
    {
        executionParameters.set(params);
    }

    public static Map<String, String> getParameters() 
    {
        if (executionParameters.get() == null) 
        {
            throw new IllegalStateException("Execution parameters have not been set for the current thread.");
        }
        return executionParameters.get();
    }
}
