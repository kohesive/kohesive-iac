package uy.kohesive.iac.model.aws.cloudformation.wait;

import com.amazonaws.AmazonWebServiceRequest;

import java.io.Serializable;

public class CreateWaitConditionRequest extends AmazonWebServiceRequest implements Serializable, Cloneable {

    private String name;
    private String handle;
    private Integer count;
    private Long timeout;

    public CreateWaitConditionRequest() {
    }

    public CreateWaitConditionRequest(String name, String handle, Long timeout) {
        this.name = name;
        this.handle = handle;
        this.timeout = timeout;
    }

    public CreateWaitConditionRequest withName(String name) {
        setName(name);
        return this;
    }

    public CreateWaitConditionRequest withHandle(String handle) {
        setHandle(handle);
        return this;
    }

    public CreateWaitConditionRequest withCount(Integer count) {
        setCount(count);
        return this;
    }

    public CreateWaitConditionRequest withTimeout(Long timeout) {
        setTimeout(timeout);
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreateWaitConditionRequest that = (CreateWaitConditionRequest) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (handle != null ? !handle.equals(that.handle) : that.handle != null) return false;
        if (count != null ? !count.equals(that.count) : that.count != null) return false;
        return timeout != null ? timeout.equals(that.timeout) : that.timeout == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (handle != null ? handle.hashCode() : 0);
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + (timeout != null ? timeout.hashCode() : 0);
        return result;
    }
}
