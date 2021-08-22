package com.chaiweijian.groupwallet.restapi.controllers;

import com.chaiweijian.groupwallet.groupservice.v1.CreateGroupRequest;
import com.chaiweijian.groupwallet.groupservice.v1.GetGroupRequest;
import com.chaiweijian.groupwallet.groupservice.v1.Group;
import com.chaiweijian.groupwallet.groupservice.v1.UpdateGroupRequest;
import com.chaiweijian.groupwallet.restapi.grpc.clients.GroupAggregateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "v1")
public class GroupsController {

    private final GroupAggregateService groupAggregateService;

    public GroupsController(GroupAggregateService groupAggregateService) {
        this.groupAggregateService = groupAggregateService;
    }

    @GetMapping(value = "groups/{name}", produces = ContentType.APPLICATION_JSON, consumes = ContentType.APPLICATION_JSON)
    public ResponseEntity<Group> getUser(@PathVariable String name) {
        return ResponseEntity.ok(
                groupAggregateService.getGroup(GetGroupRequest.newBuilder().setName(nameFromPathVariable(name)).build()));
    }

    @PostMapping(value = "groups", produces = ContentType.APPLICATION_JSON, consumes = ContentType.APPLICATION_JSON)
    public ResponseEntity<Group> createGroup(@RequestBody CreateGroupRequest createGroupRequest) {
        return ResponseEntity.ok(groupAggregateService.createGroup(createGroupRequest));
    }

    @PatchMapping(value = "groups/{name}", produces = ContentType.APPLICATION_JSON, consumes = ContentType.APPLICATION_JSON)
    public ResponseEntity<Group> updateUser(@RequestBody UpdateGroupRequest updateGroupRequest, @PathVariable String name) {
        var group = updateGroupRequest.getGroup().toBuilder().setName(nameFromPathVariable(name));
        return ResponseEntity.ok(groupAggregateService.updateGroup(updateGroupRequest.toBuilder().setGroup(group).build()));
    }

    private String nameFromPathVariable(String name) {
        return String.format("groups/%s", name);
    }
}
