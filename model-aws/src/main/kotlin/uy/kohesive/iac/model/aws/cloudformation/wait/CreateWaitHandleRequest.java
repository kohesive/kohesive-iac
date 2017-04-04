package uy.kohesive.iac.model.aws.cloudformation.wait;

import com.amazonaws.AmazonWebServiceRequest;

import java.io.Serializable;

public class CreateWaitHandleRequest extends AmazonWebServiceRequest implements Serializable, Cloneable {

    private String name;

    public CreateWaitHandleRequest() {
    }

    public CreateWaitHandleRequest(String name) {
        this.name = name;
    }

    public CreateWaitHandleRequest withName(String name) {
        setName(name);
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreateWaitHandleRequest that = (CreateWaitHandleRequest) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public CreateWaitHandleRequest clone() {
        return (CreateWaitHandleRequest) super.clone();
    }

}
