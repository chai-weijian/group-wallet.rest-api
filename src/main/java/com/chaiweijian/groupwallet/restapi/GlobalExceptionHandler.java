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

package com.chaiweijian.groupwallet.restapi;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.google.rpc.*;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.StatusProto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    // handle the gRPC StatusRuntimeException
    // return the Status to web request in JSON format
    @ExceptionHandler(StatusRuntimeException.class)
    public ResponseEntity<String> handler(StatusRuntimeException statusRuntimeException) throws InvalidProtocolBufferException {
        var status = StatusProto.fromThrowable(statusRuntimeException);
        // unable to handle if status is null
        // this should throw 500 error by Spring boot
        assert status != null;

        return new ResponseEntity<>(getErrorPrinter().print(status), mapStatus(status.getCode()));
    }

    // The details field of status is Any type, but it should be one of the message defined in error_details.proto
    // https://github.com/googleapis/api-common-protos/blob/master/google/rpc/error_details.proto
    // This printer register all the error detail type so the status can be printed to JSON.
    private JsonFormat.Printer getErrorPrinter() {
        var typeRegistry = JsonFormat.TypeRegistry.newBuilder()
                .add(RetryInfo.getDescriptor())
                .add(DebugInfo.getDescriptor())
                .add(QuotaFailure.getDescriptor())
                .add(ErrorInfo.getDescriptor())
                .add(PreconditionFailure.getDescriptor())
                .add(BadRequest.getDescriptor())
                .add(RequestInfo.getDescriptor())
                .add(ResourceInfo.getDescriptor())
                .add(Help.getDescriptor())
                .add(LocalizedMessage.getDescriptor())
                .build();

        return JsonFormat.printer().usingTypeRegistry(typeRegistry);
    }

    // map gRPC status code to HttpStatus following code.proto
    // https://github.com/googleapis/api-common-protos/blob/master/google/rpc/code.proto
    private HttpStatus mapStatus(int status) {
        switch (status) {
            case 1: return HttpStatus.valueOf(499); // CANCELLED: 499 Client Closed Request
            case 2: // UNKNOWN
            case 13: // INTERNAL
            case 15: // DATA_LOSS
                return HttpStatus.valueOf(500); // 500 Internal Server Error
            case 3: // INVALID_ARGUMENT
            case 9: // FAILED_PRECONDITION
            case 11: // OUT_OF_RANGE
                return HttpStatus.valueOf(400); // 400 Bad Request
            case 4: return HttpStatus.valueOf(504); // DEADLINE_EXCEEDED: 504 Gateway Timeout
            case 5: return HttpStatus.valueOf(404); // NOT_FOUND: 404 Not Found
            case 6: // ALREADY_EXISTS
            case 10: // ABORTED
                return HttpStatus.valueOf(409); // 409 Conflict
            case 7: return HttpStatus.valueOf(403); // PERMISSION_DENIED: 403 Forbidden
            case 8: return HttpStatus.valueOf(429); // RESOURCE_EXHAUSTED: 429 Too Many Requests
            case 12: return HttpStatus.valueOf(501); // UNIMPLEMENTED: 501 Not Implemented
            case 14: return HttpStatus.valueOf(503); // UNAVAILABLE: 503 Service Unavailable
            case 16: return HttpStatus.valueOf(401); // UNAUTHENTICATED: 401 Unauthorized
            default: throw new RuntimeException("unknown status");
        }
    }
}
