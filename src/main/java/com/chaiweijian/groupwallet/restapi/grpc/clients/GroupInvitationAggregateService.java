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

import com.chaiweijian.groupwallet.userservice.v1.AcceptGroupInvitationRequest;
import com.chaiweijian.groupwallet.userservice.v1.CreateGroupInvitationRequest;
import com.chaiweijian.groupwallet.userservice.v1.GroupInvitation;
import com.chaiweijian.groupwallet.userservice.v1.GroupInvitationAggregateServiceGrpc;
import com.chaiweijian.groupwallet.userservice.v1.ListGroupInvitationsRequest;
import com.chaiweijian.groupwallet.userservice.v1.ListGroupInvitationsResponse;
import com.chaiweijian.groupwallet.userservice.v1.RejectGroupInvitationRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class GroupInvitationAggregateService {
    @GrpcClient("group-invitation-aggregate")
    private GroupInvitationAggregateServiceGrpc.GroupInvitationAggregateServiceBlockingStub groupInvitationServiceBlockingStub;

    public ListGroupInvitationsResponse listGroupInvitation(ListGroupInvitationsRequest listGroupInvitationsRequest) {
        return groupInvitationServiceBlockingStub.listGroupInvitations(listGroupInvitationsRequest);
    }

    public GroupInvitation createGroupInvitation(CreateGroupInvitationRequest createGroupInvitationRequest) {
        return groupInvitationServiceBlockingStub.createGroupInvitation(createGroupInvitationRequest);
    }

    public GroupInvitation acceptGroupInvitation(AcceptGroupInvitationRequest acceptGroupInvitationRequest) {
        return groupInvitationServiceBlockingStub.acceptGroupInvitation(acceptGroupInvitationRequest);
    }

    public GroupInvitation rejectGroupInvitation(RejectGroupInvitationRequest rejectGroupInvitationRequest) {
        return groupInvitationServiceBlockingStub.rejectGroupInvitation(rejectGroupInvitationRequest);
    }
}
