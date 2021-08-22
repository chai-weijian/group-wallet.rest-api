package com.chaiweijian.groupwallet.restapi.controllers;

import com.chaiweijian.groupwallet.restapi.grpc.clients.GroupAggregateService;
import com.chaiweijian.groupwallet.groupservice.v1.CreateGroupRequest;
import com.chaiweijian.groupwallet.groupservice.v1.Group;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "v1")
public class GroupsController {

    private final GroupAggregateService groupAggregateService;

    public GroupsController(GroupAggregateService groupAggregateService) {
        this.groupAggregateService = groupAggregateService;
    }

    @PostMapping(value = "groups", produces = ContentType.APPLICATION_JSON, consumes = ContentType.APPLICATION_JSON)
    public ResponseEntity<Group> createGroup(@RequestBody CreateGroupRequest createGroupRequest) {
        return ResponseEntity.ok(groupAggregateService.createGroup(createGroupRequest));
    }
}
