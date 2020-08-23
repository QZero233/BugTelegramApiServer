package com.qzero.bt.common.view;

import org.springframework.stereotype.Component;

@Component
public class CommonPackedObjectFactory implements IPackedObjectFactory {

    @Override
    public PackedObject getPackedObject() {
        return new CommonPackedObject();
    }

    @Override
    public PackedObject getReturnValue(boolean success, String message) {
        return getReturnValue(new ActionResult(success,message));
    }

    @Override
    public PackedObject getReturnValue(ActionResult actionResult) {
        PackedObject packedObject=getPackedObject();
        packedObject.addObject(actionResult);
        return packedObject;
    }
}
