package com.qzero.bt.common.view;

public class ExecuteResult {

    private ActionResult actionResult;
    private PackedParameter packedParameter;

    public ExecuteResult() {
    }

    public ExecuteResult(boolean succeeded,String message){
        actionResult=new ActionResult(succeeded,message);
        packedParameter=new PackedParameter();
    }

    public ExecuteResult(boolean succeeded,String message,Object resultObject){
        actionResult=new ActionResult(succeeded,message);
        packedParameter=new PackedParameter(resultObject);
    }

    public ExecuteResult(ActionResult actionResult, PackedParameter packedParameter) {
        this.actionResult = actionResult;
        this.packedParameter = packedParameter;
    }

    public ActionResult getActionResult() {
        return actionResult;
    }

    public void setActionResult(ActionResult actionResult) {
        this.actionResult = actionResult;
    }

    public PackedParameter getPackedParameter() {
        return packedParameter;
    }

    public void setPackedParameter(PackedParameter packedParameter) {
        this.packedParameter = packedParameter;
    }

    @Override
    public String toString() {
        return "ExecuteResult{" +
                "actionResult=" + actionResult +
                ", packedParameter=" + packedParameter +
                '}';
    }
}
