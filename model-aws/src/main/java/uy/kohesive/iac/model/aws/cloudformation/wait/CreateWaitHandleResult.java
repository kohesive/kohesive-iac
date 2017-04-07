package uy.kohesive.iac.model.aws.cloudformation.wait;

import com.amazonaws.AmazonWebServiceResult;
import com.amazonaws.ResponseMetadata;

import java.io.Serializable;

public class CreateWaitHandleResult extends AmazonWebServiceResult<ResponseMetadata> implements Serializable, Cloneable {

    private WaitConditionHandle handle;

    public CreateWaitHandleResult() {
    }

    public CreateWaitHandleResult(WaitConditionHandle handle) {
        this.handle = handle;
    }

    public CreateWaitHandleResult withHandle(WaitConditionHandle handle) {
        setHandle(handle);
        return this;
    }

    public WaitConditionHandle getHandle() {
        return handle;
    }

    public void setHandle(WaitConditionHandle handle) {
        this.handle = handle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreateWaitHandleResult that = (CreateWaitHandleResult) o;

        return handle != null ? handle.equals(that.handle) : that.handle == null;
    }

    @Override
    public int hashCode() {
        return handle != null ? handle.hashCode() : 0;
    }

    @Override
    public CreateWaitHandleResult clone() {
        try {
            return (CreateWaitHandleResult) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() " + "even though we're Cloneable!", e);
        }
    }

}
