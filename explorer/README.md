# Hyperledger Fabric Explorer UI

## Overview

Hyperledger Fabric Explorer UI is a user-friendly web application that provides a comprehensive visualization and operational interface for your Hyperledger Fabric blockchain networks. It allows users to view, invoke, deploy, and query blockchain components through an intuitive graphical interface.

## Key Features

- **Real-time Network Monitoring**: View live statistics of your blockchain network including blocks, transactions, and nodes
- **Block Information**: Detailed view of blocks, transactions, and chaincode information
- **Network Stats**: 
    - Transaction statistics
    - Block information
    - Node status
    - Chaincode deployment status
- **Smart Contract Management**: Deploy and interact with chaincodes through the UI
- **Access Control**: Role-based access control for different user types
- **Transaction Decoder**: Decode and view transaction details in a human-readable format

## Requirements

- Docker v20.10.x or higher
- Node.js v14.x or higher
- PostgreSQL v10.x or higher
- A running Hyperledger Fabric network
- Modern web browser (Chrome, Firefox, Safari)

## Usage

1. Start the Explorer:
```bash
cd explorer
docker-compose up
```

2. Access the UI:
- Open your browser and navigate to `http://localhost:3000`
- Default credentials: 
    - Username: exploreradmin
    - Password: exploreradminpw

3. Stop the Explorer
```bash
docker-compose down
```

## Configuration

The Explorer can be configured through various configuration files:

- `config.json`: Main configuration file for network details
- `connection-profile/`: Directory containing network connection profiles
- `docker-compose.yaml`: Explorer-specific configurations

## Troubleshooting

If there is any error or problem in the process of starting explorer run these commands:
```bash
docker-compose down
docker system prune
docker volume prune
```

then try to start Explorer again

*Ensure all ports required by the Explorer are available*

## Support

- [Documentation](https://github.com/hyperledger/blockchain-explorer/wiki)
- [Issue Tracker](https://github.com/hyperledger/blockchain-explorer/issues)
- [Hyperledger Chat](https://chat.hyperledger.org)

