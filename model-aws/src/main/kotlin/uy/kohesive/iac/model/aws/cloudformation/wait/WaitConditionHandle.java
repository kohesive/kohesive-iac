package uy.kohesive.iac.model.aws.cloudformation.wait;

import java.io.Serializable;

public class WaitConditionHandle implements Serializable, Cloneable {

    private String ref;

    public WaitConditionHandle() {
    }

    public WaitConditionHandle(String ref) {
        this.ref = ref;
    }

    public WaitConditionHandle withRef(String ref) {
        setRef(ref);
        return this;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WaitConditionHandle that = (WaitConditionHandle) o;

        return ref != null ? ref.equals(that.ref) : that.ref == null;
    }

    @Override
    public int hashCode() {
        return ref != null ? ref.hashCode() : 0;
    }

    @Override
    public WaitCondition clone() {
        try {
            return (WaitCondition) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() " + "even though we're Cloneable!", e);
        }
    }

}
