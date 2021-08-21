package com.chaiweijian.groupwallet.restapi;

import com.chaiweijian.groupwallet.userservice.v1.User;
import com.google.protobuf.FieldMask;
import com.google.protobuf.util.FieldMaskUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class UpdateUserFieldMaskTests {
    @Test
    public void testMask() {
        User user = User.newBuilder().build();
        FieldMask fieldMask = FieldMask.newBuilder().addPaths("name").build();
        assert FieldMaskUtil.isValid(user.getDescriptorForType(), fieldMask);
    }
}
