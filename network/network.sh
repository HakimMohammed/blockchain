# Create Organization crypto material using cryptogen or CAs
function createOrgs() {
  if [ -d "organizations/peerOrganizations" ]; then
    rm -Rf organizations/peerOrganizations && rm -Rf organizations/ordererOrganizations
  fi

  # Create crypto material using cryptogen
  if [ "$CRYPTO" == "cryptogen" ]; then
    which cryptogen
    if [ "$?" -ne 0 ]; then
      fatalln "cryptogen tool not found. exiting"
    fi
    infoln "Generating certificates using cryptogen tool"

    #Company
    infoln "Creating Company Identities"

    set -x
    cryptogen generate --config=./organizations/cryptogen/crypto-config-company.yaml --output="organizations"
    res=$?
    { set +x; } 2>/dev/null
    if [ $res -ne 0 ]; then
      fatalln "Failed to generate certificates..."
    fi

    #Customer
    infoln "Creating Customer Identities"

    set -x
    cryptogen generate --config=./organizations/cryptogen/crypto-config-customer.yaml --output="organizations"
    res=$?
    { set +x; } 2>/dev/null
    if [ $res -ne 0 ]; then
      fatalln "Failed to generate certificates..."
    fi

    #Supplier A
    infoln "Creating Supplier A Identities"

    set -x
    cryptogen generate --config=./organizations/cryptogen/crypto-config-supplier_A.yaml --output="organizations"
    res=$?
    { set +x; } 2>/dev/null
    if [ $res -ne 0 ]; then
      fatalln "Failed to generate certificates..."
    fi

    #Supplier B
    infoln "Creating Supplier B Identities"

    set -x
    cryptogen generate --config=./organizations/cryptogen/crypto-config-supplier_B.yaml --output="organizations"
    res=$?
    { set +x; } 2>/dev/null
    if [ $res -ne 0 ]; then
      fatalln "Failed to generate certificates..."
    fi

    #Orderer
    infoln "Creating Orderer Identities"

    set -x
    cryptogen generate --config=./organizations/cryptogen/crypto-config-orderer.yaml --output="organizations"
    res=$?
    { set +x; } 2>/dev/null
    if [ $res -ne 0 ]; then
      fatalln "Failed to generate certificates..."
    fi

  fi

  # Create crypto material using cfssl
  if [ "$CRYPTO" == "cfssl" ]; then

    . organizations/cfssl/registerEnroll.sh
    # example of how to use the registerEnroll.sh script
    #function_name cert-type   CN   org
    
    #peer_cert peer peer0.org1.example.com org1
    #peer_cert admin Admin@org1.example.com org1

    #orderer_cert orderer orderer.example.com
    #orderer_cert admin Admin@example.com

    infoln "Generating certificates using cfssl"

    #Company
    infoln "Creating Company Identities"
    peer_cert peer company_peer.company.enset.com company
    peer_cert admin Admin@company.enset.com company

    #Customer
    infoln "Creating Customer Identities"
    peer_cert peer customer_peer.customer.enset.com customer
    peer_cert admin Admin@customer.enset.com customer

    #Supplier A
    infoln "Creating Supplier A Identities"
    peer_cert peer supplierA_peer.supplier_A.enset.com supplier_A
    peer_cert admin Admin@suppliera.enset.com supplier_A

    #Supplier B
    infoln "Creating Supplier B Identities"
    peer_cert peer supplierB_peer.supplier_B.enset.com supplier_B
    peer_cert admin Admin@supplierb.enset.com supplier_B

    #Orderer
    infoln "Creating Orderer Identities"
    orderer_cert orderer orderer.enset.com
    orderer_cert admin Admin@enset.com

  fi 

  # Create crypto material using Fabric CA
  if [ "$CRYPTO" == "Certificate Authorities" ]; then
    infoln "Generating certificates using Fabric CA"
    ${CONTAINER_CLI_COMPOSE} -f compose/$COMPOSE_FILE_CA -f compose/$CONTAINER_CLI/${CONTAINER_CLI}-$COMPOSE_FILE_CA up -d 2>&1

    . organizations/fabric-ca/registerEnroll.sh

    while :
    do
      if [ ! -f "organizations/fabric-ca/org1/tls-cert.pem" ]; then
        sleep 1
      else
        break
      fi
    done

    infoln "Creating Org1 Identities"

    createOrg1

    infoln "Creating Org2 Identities"

    createOrg2

    infoln "Creating Orderer Org Identities"

    createOrderer

  fi

  infoln "Generating CCP files for Org1 and Org2"
  ./organizations/ccp-generate.sh
}