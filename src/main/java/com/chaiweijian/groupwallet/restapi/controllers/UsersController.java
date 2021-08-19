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

import com.chaiweijian.groupwallet.restapi.grpc.clients.UserAggregateService;
import com.chaiweijian.groupwallet.userservice.v1.CreateUserRequest;
import com.chaiweijian.groupwallet.userservice.v1.GetUserRequest;
import com.chaiweijian.groupwallet.userservice.v1.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "v1")
public class UsersController {

    private final UserAggregateService userAggregateService;

    public UsersController(UserAggregateService userAggregateService) {
        this.userAggregateService = userAggregateService;
    }

    @GetMapping(value = "users/{name}", produces = ContentType.APPLICATION_JSON, consumes = ContentType.APPLICATION_JSON)
    public ResponseEntity<User> getUser(@PathVariable String name) {
        return ResponseEntity.ok(
                userAggregateService.getUser(GetUserRequest.newBuilder().setName(String.format("users/%s", name)).build()));
    }

    @PostMapping(value = "users", produces = ContentType.APPLICATION_JSON, consumes = ContentType.APPLICATION_JSON)
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        return ResponseEntity.ok(userAggregateService.createUser(createUserRequest));
    }
}
