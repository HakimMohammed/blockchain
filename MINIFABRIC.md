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
  
  # Add organization definitions
  organizations:
    company.com:
      mspid: CompanyMSP
      domain: company.com
      enableNodeOUs: true
      ca:
        hostname: ca1
      peers:
        - peer1
      users:
        - admin
        - user1

    supplier1.com:
      mspid: Supplier1MSP
      domain: supplier1.com
      enableNodeOUs: true
      ca:
        hostname: ca1
      peers:
        - peer1
      users:
        - admin
        - user1

    supplier2.com:
      mspid: Supplier2MSP
      domain: supplier2.com
      enableNodeOUs: true
      ca:
        hostname: ca1
      peers:
        - peer1
      users:
        - admin
        - user1

    client.com:
      mspid: ClientMSP
      domain: client.com
      enableNodeOUs: true
      ca:
        hostname: ca1
      peers:
        - peer1
      users:
        - admin
        - user1

    example.com:
      mspid: OrdererMSP
      domain: example.com
      enableNodeOUs: true
      orderers:
        - orderer1

  # Network configuration
  network:
    template:
      type: fabricv2
      version: 2.5.4  # Using a stable Fabric version
```

## Set up Minifabric network
```bash
sudo ./minifab up -o company.com -e 7050
```

## Check created network
```bash
sudo docker ps -a
```

## Add Channels
Create a file named channels.json at the vars folder
```bash
sudo nano vars/channels.json 
```
Paste this in the file
```json
{
    "companysupplier1channel": {
        "organizations": ["CompanyMSP", "Supplier1MSP"]
    },
    "companysupplier2channel": {
        "organizations": ["CompanyMSP", "Supplier2MSP"]
    },
    "companyclientchannel": {
        "organizations": ["CompanyMSP", "ClientMSP"]
    }
}
```

Create the first channel and join organizations
```bash
# Create and join Company-Supplier1 channel
sudo ./minifab create -c companysupplier1channel
sudo ./minifab join -c companysupplier1channel -n peer1.company.com,peer1.supplier1.com
```
Create and join the second channel
```bash
# Create and join Company-Supplier2 channel
sudo ./minifab create -c companysupplier2channel
sudo ./minifab join -c companysupplier2channel -n peer1.company.com,peer1.supplier2.com
```
Create and join the third channel
```bash
# Create and join Company-Client channel
sudo ./minifab create -c companyclientchannel
sudo ./minifab join -c companyclientchannel -n peer1.company.com,peer1.client.com
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