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
- Docker Compose v2.0.x or higher
- Node.js v14.x or higher
- PostgreSQL v10.x or higher
- A running Hyperledger Fabric network
- Modern web browser (Chrome, Firefox, Safari)

## Installation

1. Clone the repository:
```bash
git clone https://github.com/hyperledger/blockchain-explorer.git
cd blockchain-explorer
```

2. Install dependencies:
```bash
npm install
```

3. Configure the database:
```bash
cd app/persistence/fabric/postgreSQL
./createdb.sh
```

4. Update the config.json with your network details:
```json
{
"network-configs": {
    "test-network": {
    "name": "Test Network",
    "profile": "./connection-profile/test-network.json"
    }
}
}
```

## Usage

1. Start the Explorer:
```bash
./start.sh
```

2. Access the UI:
- Open your browser and navigate to `http://localhost:8080`
- Default credentials: 
    - Username: admin
    - Password: adminpw

## Configuration

The Explorer can be configured through various configuration files:

- `config.json`: Main configuration file for network details
- `connection-profile/`: Directory containing network connection profiles
- `explorerconfig.json`: Explorer-specific configurations

## Troubleshooting

- Ensure all ports required by the Explorer are available
- Check logs in the `logs/` directory for detailed error messages
- Verify network connectivity to your Fabric network
- Ensure proper configuration in connection profiles

## Contributing

Contributions are welcome! Please read our [Contributing Guidelines](CONTRIBUTING.md) before submitting pull requests.

## License

This project is licensed under the Apache-2.0 License - see the [LICENSE](LICENSE) file for details.

## Support

- [Documentation](https://github.com/hyperledger/blockchain-explorer/wiki)
- [Issue Tracker](https://github.com/hyperledger/blockchain-explorer/issues)
- [Hyperledger Chat](https://chat.hyperledger.org)

