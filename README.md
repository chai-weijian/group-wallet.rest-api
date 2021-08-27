# Group Wallet

This website is part my _Group Wallet_ project.

You can find other part of this project in following links:
- [website](https://github.com/chai-weijian/group-wallet.website)
- [user-service](https://github.com/chai-weijian/group-wallet.user-service)
- [group-service](https://github.com/chai-weijian/group-wallet.group-service)
- [protobuf](https://github.com/chai-weijian/group-wallet.protobuf)

# REST API layer

This [Spring boot](https://spring.io/) project serve as an REST API layer of _Group Wallet_ project.  

Over the past few years I have been trying to learn and design some principles that are flexible to be used to different projects.

One day I stumbled upon [API Improvement Plans](https://google.aip.dev/) and found it fits really well with what I learnt by trial and error, and offer a lot of suggestions that I haven't thought of. So I start adopting it to my project.

This layer simply forward HTTP requests to other microservices using gRPC. 

## Project Status - work in progress

I will progressively implement my ideas to this project. As I learn more and more technologies, the list of ideas are also expanding.

This project will be mark as completed only when I stop learning any new technologies, which probably wouldn't happen.

## Technologies

The technologies used in this REST API project:
- [Spring boot](https://spring.io/)
- [gRPC](https://grpc.io/)
- [Protocol Buffers](https://developers.google.com/protocol-buffers)