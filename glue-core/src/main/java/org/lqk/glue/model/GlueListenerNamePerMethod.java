package org.lqk.glue.model;

public class GlueListenerNamePerMethod {
    private String name;
    private String methodName;

    public GlueListenerNamePerMethod(String name, String methodName) {
        this.name = name;
        this.methodName = methodName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public int hashCode() {
        return (methodName + name).hashCode();
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GlueListenerNamePerMethod)) {
            return false;
        }
        GlueListenerNamePerMethod _obj = (GlueListenerNamePerMethod) obj;
        String _name = methodName + name;
        return _name.equals(_obj.getMethodName() + _obj.getName());
    }
}
