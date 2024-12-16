# Hyperledger Fabric Network Setup with Minifabric

## Install Minifabric
```bash
mkdir -p ~/CascadeProjects/inventory-blockchain
cd ~/CascadeProjects/inventory-blockchain
curl -o minifab -sL https://tinyurl.com/yxa2q6yr
chmod +x minifab
```

## Create spec.yaml file

Enter the following command to create spec.yaml file
```bash
nano spec.yaml
```
Add the following content to the spec.yaml file
```yaml
fabric:
  cas:
    - "ca1.company.com"
    - "ca1.supplier1.com"
    - "ca1.supplier2.com"
    - "ca1.client.com"
  peers:
    - "peer1.company.com"
    - "peer1.supplier1.com"
    - "peer1.supplier2.com"
    - "peer1.client.com"
  orderers:
    - "orderer1.example.com"
  settings:
    ca:
      FABRIC_LOGGING_SPEC: DEBUG
    peer:
      FABRIC_LOGGING_SPEC: DEBUG
      CORE_CHAINCODE_MODE: dev
    orderer:
      FABRIC_LOGGING_SPEC: DEBUG

  channels:
    - name: supplier1channel
      definition:
        consortium: SampleConsortium
        members:
          - org: company.com
            type: peer
          - org: supplier1.com
            type: peer
    - name: supplier2channel
      definition:
        consortium: SampleConsortium
        members:
          - org: company.com
            type: peer
          - org: supplier2.com
            type: peer
    - name: clientchannel
      definition:
        consortium: SampleConsortium
        members:
          - org: company.com
            type: peer
          - org: client.com
            type: peer
```

## Set up Minifabric network
```bash
sudo ./minifab up -o company.com -e 7050
```

## Check created network
```bash
sudo docker ps -a
```

## Access and Test the Network
If you want to interact with the blockchain network, you can use the minifab invoke and minifab query commands to test transactions. For example:
```bash
sudo ./minifab invoke -n simple -p '"invoke","a","b","10"'

```

To query the state:
```bash
sudo ./minifab query -n simple -p '"query","a"'
```

## Clean up
```bash
sudo ./minifab down
```

**If you encounter any cleaning issues do these commands**
```bash
sudo ./minifab cleanup -o company.com
sudo docker stop $(sudo docker ps -aq)
sudo docker rm $(sudo docker ps -aq)
sudo docker system prune -f
```

# The End