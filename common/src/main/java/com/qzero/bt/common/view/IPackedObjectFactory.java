package com.qzero.bt.common.view;

public interface IPackedObjectFactory {

    PackedObject getPackedObject();

    PackedObject getReturnValue(boolean success,String message);

    PackedObject getReturnValue(ActionResult actionResult);

}
