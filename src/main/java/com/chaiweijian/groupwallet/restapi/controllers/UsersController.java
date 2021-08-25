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

package com.chaiweijian.groupwallet.restapi.controllers;

import com.chaiweijian.groupwallet.restapi.grpc.clients.GroupInvitationAggregateService;
import com.chaiweijian.groupwallet.restapi.grpc.clients.UserAggregateService;
import com.chaiweijian.groupwallet.userservice.v1.AcceptGroupInvitationRequest;
import com.chaiweijian.groupwallet.userservice.v1.CreateGroupInvitationRequest;
import com.chaiweijian.groupwallet.userservice.v1.CreateUserRequest;
import com.chaiweijian.groupwallet.userservice.v1.FindUserRequest;
import com.chaiweijian.groupwallet.userservice.v1.GetUserRequest;
import com.chaiweijian.groupwallet.userservice.v1.GroupInvitation;
import com.chaiweijian.groupwallet.userservice.v1.ListGroupInvitationsRequest;
import com.chaiweijian.groupwallet.userservice.v1.ListGroupInvitationsResponse;
import com.chaiweijian.groupwallet.userservice.v1.RejectGroupInvitationRequest;
import com.chaiweijian.groupwallet.userservice.v1.UpdateUserRequest;
import com.chaiweijian.groupwallet.userservice.v1.User;
import com.google.rpc.Code;
import com.google.rpc.Status;
import io.grpc.protobuf.StatusProto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "v1")
public class UsersController {

    private final UserAggregateService userAggregateService;
    private final GroupInvitationAggregateService groupInvitationAggregateService;

    public UsersController(UserAggregateService userAggregateService, GroupInvitationAggregateService groupInvitationAggregateService) {
        this.userAggregateService = userAggregateService;
        this.groupInvitationAggregateService = groupInvitationAggregateService;
    }

    @GetMapping(value = "users:findUser", produces = ContentType.APPLICATION_JSON, consumes = ContentType.APPLICATION_JSON)
    public ResponseEntity<User> findUser(@RequestParam String uid) {
        return ResponseEntity.ok(userAggregateService.findUser(FindUserRequest.newBuilder().setUid(uid).build()));
    }

    @GetMapping(value = "users/{name}", produces = ContentType.APPLICATION_JSON, consumes = ContentType.APPLICATION_JSON)
    public ResponseEntity<User> getUser(@PathVariable String name) {
        return ResponseEntity.ok(
                userAggregateService.getUser(GetUserRequest.newBuilder().setName(userNameFromPathVariable(name)).build()));
    }

    @PostMapping(value = "users", produces = ContentType.APPLICATION_JSON, consumes = ContentType.APPLICATION_JSON)
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        return ResponseEntity.ok(userAggregateService.createUser(createUserRequest));
    }

    @PatchMapping(value = "users/{name}", produces = ContentType.APPLICATION_JSON, consumes = ContentType.APPLICATION_JSON)
    public ResponseEntity<User> updateUser(@RequestBody UpdateUserRequest updateUserRequest, @PathVariable String name) {
        var user = updateUserRequest.getUser().toBuilder().setName(userNameFromPathVariable(name));
        return ResponseEntity.ok(userAggregateService.updateUser(updateUserRequest.toBuilder().setUser(user).build()));
    }

    @GetMapping(value = "users/{name}/groupInvitations", produces = ContentType.APPLICATION_JSON, consumes = ContentType.APPLICATION_JSON)
    public ResponseEntity<ListGroupInvitationsResponse> listGroupInvitation(@PathVariable String name,
                                                                            @RequestParam(required = false) String pageToken,
                                                                            @RequestParam(required = false, defaultValue = "50") Integer pageSize) {

        var builder = ListGroupInvitationsRequest.newBuilder();

        builder.setParent(userNameFromPathVariable(name))
                .setPageSize(Math.min(pageSize, 1000));

        if (pageToken != null) {
            builder.setPageToken(pageToken);
        }

        return ResponseEntity.ok(groupInvitationAggregateService.listGroupInvitation(builder.build()));
    }

    @PostMapping(value = "users/{user}/groupInvitations", produces = ContentType.APPLICATION_JSON, consumes = ContentType.APPLICATION_JSON)
    public ResponseEntity<GroupInvitation> createGroupInvitation(@RequestBody CreateGroupInvitationRequest createGroupInvitationRequest,
                                                                 @PathVariable String user) {
        return ResponseEntity.ok(groupInvitationAggregateService.createGroupInvitation(createGroupInvitationRequest.toBuilder()
                .setParent(userNameFromPathVariable(user))
                .build()));
    }

    @PostMapping(value = "users/{user}/groupInvitations/{groupInvitation}:accept", produces = ContentType.APPLICATION_JSON, consumes = ContentType.APPLICATION_JSON)
    public ResponseEntity<GroupInvitation> acceptGroupInvitation(@RequestBody AcceptGroupInvitationRequest acceptGroupInvitationRequest,
                                                                 @PathVariable String user,
                                                                 @PathVariable String groupInvitation) {
        if (!groupInvitationNameFromPathVariable(user, groupInvitation).equals(acceptGroupInvitationRequest.getGroupInvitation().getName())) {
            throw StatusProto.toStatusRuntimeException(Status.newBuilder().setCode(Code.INVALID_ARGUMENT_VALUE).setMessage("URL and resource name does not match").build());
        }
        return ResponseEntity.ok(groupInvitationAggregateService.acceptGroupInvitation(acceptGroupInvitationRequest));
    }

    @PostMapping(value = "users/{user}/groupInvitations/{groupInvitation}:reject", produces = ContentType.APPLICATION_JSON, consumes = ContentType.APPLICATION_JSON)
    public ResponseEntity<GroupInvitation> rejectGroupInvitation(@RequestBody RejectGroupInvitationRequest rejectGroupInvitationRequest,
                                                                 @PathVariable String user,
                                                                 @PathVariable String groupInvitation) {
        if (!groupInvitationNameFromPathVariable(user, groupInvitation).equals(rejectGroupInvitationRequest.getGroupInvitation().getName())) {
            throw StatusProto.toStatusRuntimeException(Status.newBuilder().setCode(Code.INVALID_ARGUMENT_VALUE).setMessage("URL and resource name does not match").build());
        }
        return ResponseEntity.ok(groupInvitationAggregateService.rejectGroupInvitation(rejectGroupInvitationRequest));
    }

    private String userNameFromPathVariable(String name) {
        return String.format("users/%s", name);
    }

    private String groupInvitationNameFromPathVariable(String user, String name) {
        return String.format("users/%s/groupInvitations/%s", user, name);
    }
}
