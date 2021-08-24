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

import com.chaiweijian.groupwallet.restapi.grpc.clients.GroupInvitationService;
import com.chaiweijian.groupwallet.restapi.grpc.clients.UserAggregateService;
import com.chaiweijian.groupwallet.userservice.v1.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "v1")
public class UsersController {

    private final UserAggregateService userAggregateService;
    private final GroupInvitationService groupInvitationService;

    public UsersController(UserAggregateService userAggregateService, GroupInvitationService groupInvitationService) {
        this.userAggregateService = userAggregateService;
        this.groupInvitationService = groupInvitationService;
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

    @PostMapping(value = "users/{user}/groupInvitations", produces = ContentType.APPLICATION_JSON, consumes = ContentType.APPLICATION_JSON)
    public ResponseEntity<GroupInvitation> createGroupInvitation(@RequestBody CreateGroupInvitationRequest createGroupInvitationRequest, @PathVariable String user) {
        return ResponseEntity.ok(groupInvitationService.createGroup(createGroupInvitationRequest.toBuilder().setParent(userNameFromPathVariable(user)).build()));
    }

    private String userNameFromPathVariable(String name) {
        return String.format("users/%s", name);
    }
}
