// Copyright 2021 Chai Wei Jian
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.chaiweijian.groupwallet.restapi.grpc.clients;

import com.chaiweijian.groupwallet.groupservice.v1.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class GroupAggregateService {
    @GrpcClient("group-aggregate")
    private GroupAggregateServiceGrpc.GroupAggregateServiceBlockingStub groupAggregateServiceBlockingStub;

    public Group createGroup(CreateGroupRequest createGroupRequest) {
        return groupAggregateServiceBlockingStub.createGroup(createGroupRequest);
    }

    public Group updateGroup(UpdateGroupRequest updateGroupRequest) {
        return groupAggregateServiceBlockingStub.updateGroup(updateGroupRequest);
    }

    public Group deleteGroup(DeleteGroupRequest deleteGroupRequest) {
        return groupAggregateServiceBlockingStub.deleteGroup(deleteGroupRequest);
    }

    public Group undeleteGroup(UndeleteGroupRequest deleteGroupRequest) {
        return groupAggregateServiceBlockingStub.undeleteGroup(deleteGroupRequest);
    }

    public Group removeMember(RemoveMemberRequest removeMemberRequest) {
        return groupAggregateServiceBlockingStub.removeMember(removeMemberRequest);
    }

    public Group getGroup(GetGroupRequest getGroupRequest) {
        return groupAggregateServiceBlockingStub.getGroup(getGroupRequest);
    }
}
