package uy.kohesive.iac.model.aws.cloudformation.wait;

import java.io.Serializable;

public class CreateWaitConditionResult extends com.amazonaws.AmazonWebServiceResult<com.amazonaws.ResponseMetadata> implements Serializable, Cloneable {

    private WaitCondition waitCondition;

    public CreateWaitConditionResult() {
    }

    public CreateWaitConditionResult(WaitCondition waitCondition) {
        this.waitCondition = waitCondition;
    }

    public WaitCondition getWaitCondition() {
        return waitCondition;
    }

    public void setWaitCondition(WaitCondition waitCondition) {
        this.waitCondition = waitCondition;
    }

    public CreateWaitConditionResult withWaitCondition(WaitCondition waitCondition) {
        setWaitCondition(waitCondition);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreateWaitConditionResult that = (CreateWaitConditionResult) o;

        return waitCondition != null ? waitCondition.equals(that.waitCondition) : that.waitCondition == null;
    }

    @Override
    public int hashCode() {
        return waitCondition != null ? waitCondition.hashCode() : 0;
    }

    @Override
    public CreateWaitConditionResult clone() {
        try {
            return (CreateWaitConditionResult) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Got a CloneNotSupportedException from Object.clone() " + "even though we're Cloneable!", e);
        }
    }

}
