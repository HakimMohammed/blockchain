#!/bin/bash
#
# Copyright IBM Corp All Rights Reserved
#
# SPDX-License-Identifier: Apache-2.0
#

# This is a collection of bash functions used by different scripts

# imports
# test network home var targets to test-network folder
# the reason we use a var here is to accommodate scenarios
# where execution occurs from folders outside of default as $PWD, such as the test-network/addOrg3 folder.
# For setting environment variables, simple relative paths like ".." could lead to unintended references
# due to how they interact with FABRIC_CFG_PATH. It's advised to specify paths more explicitly,
# such as using "../${PWD}", to ensure that Fabric's environment variables are pointing to the correct paths.
TEST_NETWORK_HOME=${TEST_NETWORK_HOME:-${PWD}}
. ${TEST_NETWORK_HOME}/scripts/utils.sh

export CORE_PEER_TLS_ENABLED=true
export ORDERER_CA=${TEST_NETWORK_HOME}/organizations/ordererOrganizations/enset.com/tlsca/tlsca.enset.com-cert.pem
export COMPANY_PEER_COMPANY_CA=${TEST_NETWORK_HOME}/organizations/peerOrganizations/company.enset.com/tlsca/tlsca.company.enset.com-cert.pem
export CUSTOMER_PEER_CUSTOMER_CA=${TEST_NETWORK_HOME}/organizations/peerOrganizations/customer.enset.com/tlsca/tlsca.customer.enset.com-cert.pem
export SUPPLIERA_PEER_SUPPLIERA_CA=${TEST_NETWORK_HOME}/organizations/peerOrganizations/suppliera.enset.com/tlsca/tlsca.suppliera.enset.com-cert.pem
export SUPPLIERB_PEER_SUPPLIERB_CA=${TEST_NETWORK_HOME}/organizations/peerOrganizations/supplierb.enset.com/tlsca/tlsca.supplierb.enset.com-cert.pem

# Set environment variables for the peer org
setGlobals() {
  # it was taking a number as an argument
  # now it takes a string as an argument e.g. "company", "customer", "suppliera", "supplierb"

  local ORGS=("$@")
  for USING_ORG in "${ORGS[@]}"; do
    infoln "Using organization ${USING_ORG}"
    if [ "$USING_ORG" == "company" ]; then
      export CORE_PEER_LOCALMSPID=CompanySMP
      export CORE_PEER_TLS_ROOTCERT_FILE=$COMPANY_PEER_COMPANY_CA
      export CORE_PEER_MSPCONFIGPATH=${TEST_NETWORK_HOME}/organizations/peerOrganizations/company.enset.com/users/Admin@company.enset.com/msp
      export CORE_PEER_ADDRESS=localhost:7051
    elif [ "$USING_ORG" == "customer" ]; then
      export CORE_PEER_LOCALMSPID=CustomerMSP
      export CORE_PEER_TLS_ROOTCERT_FILE=$CUSTOMER_PEER_CUSTOMER_CA
      export CORE_PEER_MSPCONFIGPATH=${TEST_NETWORK_HOME}/organizations/peerOrganizations/customer.enset.com/users/Admin@customer.enset.com/msp
      export CORE_PEER_ADDRESS=localhost:8051
    elif [ "$USING_ORG" == "suppliera" ]; then
      export CORE_PEER_LOCALMSPID=SupplierAMSP
      export CORE_PEER_TLS_ROOTCERT_FILE=$SUPPLIERA_PEER_SUPPLIERA_CA
      export CORE_PEER_MSPCONFIGPATH=${TEST_NETWORK_HOME}/organizations/peerOrganizations/suppliera.enset.com/users/Admin@suppliera.enset.com/msp
      export CORE_PEER_ADDRESS=localhost:9051
    elif [ "$USING_ORG" == "supplierb" ]; then
      export CORE_PEER_LOCALMSPID=SupplierBMSP
      export CORE_PEER_TLS_ROOTCERT_FILE=$SUPPLIERB_PEER_SUPPLIERB_CA
      export CORE_PEER_MSPCONFIGPATH=${TEST_NETWORK_HOME}/organizations/peerOrganizations/supplierb.enset.com/users/Admin@supplierb.enset.com/msp
      export CORE_PEER_ADDRESS=localhost:10051
    else
      errorln "Organization Unknown"
    fi

    if [ "$VERBOSE" = "true" ]; then
      env | grep CORE
    fi
  done
}

# parsePeerConnectionParameters $@
# Helper function that sets the peer connection parameters for a chaincode
# operation
parsePeerConnectionParameters() {
  PEER_CONN_PARMS=()
  PEERS=""
  while [ "$#" -gt 0 ]; do
    setGlobals $1
    #PEER="peer0.org$1"
    PEER="$1_peer.$1"
    ## Set peer addresses
    if [ -z "$PEERS" ]
    then
	PEERS="$PEER"
    else
	PEERS="$PEERS $PEER"
    fi
    PEER_CONN_PARMS=("${PEER_CONN_PARMS[@]}" --peerAddresses $CORE_PEER_ADDRESS)
    ## Set path to TLS certificate
    #CA=PEER0_ORG$1_CA
    uppercaseOrg=$(echo $1 | tr '[:lower:]' '[:upper:]')
    CA=$uppercaseOrg"_PEER_"$uppercaseOrg"_CA"
    
    TLSINFO=(--tlsRootCertFiles "${!CA}")
    PEER_CONN_PARMS=("${PEER_CONN_PARMS[@]}" "${TLSINFO[@]}")
    # shift by one to get to the next organization
    shift
  done
}

verifyResult() {
  if [ $1 -ne 0 ]; then
    fatalln "$2"
  fi
}
