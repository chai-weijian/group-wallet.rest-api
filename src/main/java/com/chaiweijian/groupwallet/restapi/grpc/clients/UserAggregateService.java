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

import com.chaiweijian.groupwallet.userservice.v1.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class UserAggregateService {
    @GrpcClient("user-aggregate")
    private UserAggregateServiceGrpc.UserAggregateServiceBlockingStub userAggregateServiceBlockingStub;

    public User getUser(GetUserRequest getUserRequest) {
        return userAggregateServiceBlockingStub.getUser(getUserRequest);
    }

    public User findUser(FindUserRequest findUserRequest) {
        return userAggregateServiceBlockingStub.findUser(findUserRequest);
    }

    public User createUser(CreateUserRequest createUserRequest) {
        return userAggregateServiceBlockingStub.createUser(createUserRequest);
    }

    public User updateUser(UpdateUserRequest updateUserRequest) {
        return userAggregateServiceBlockingStub.updateUser(updateUserRequest);
    }
}
