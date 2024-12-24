# Network with customized chaincode

This is a network with a customized chaincode. The chaincode is written in Java and is a simple chaincode for a stock management application

## Instructions

### Install config and bin
```shell
curl -sSL https://bit.ly/2ysbOFE | bash -s -- -d -s
```

### Remove existing network
```shell
./netwrok.sh down
```
### Check if the network is down
```shell
docker ps -a
```

### Start the network
```shell
./network.sh up createChannel
```

### Deploy the chaincode
```shell
./network.sh deployCC -l java
```


## Chaincode

in case u want to deploy your own chaincode, you can do so by following the steps below:

1. Create a new chaincode in the `chaincode/java` directory
2. go to `scripts/deployCC.sh` and change the value of variable `CC_SRC_PATH` to the path of your chaincode
3. also change any occurrence of the previous chaincode name ( in our case `stock`)
