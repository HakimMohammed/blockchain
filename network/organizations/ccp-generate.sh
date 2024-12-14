#!/bin/bash

function one_line_pem {
    echo "`awk 'NF {sub(/\\n/, ""); printf "%s\\\\\\\n",$0;}' $1`"
}

function json_ccp {
    local PP=$(one_line_pem $4)
    local CP=$(one_line_pem $5)
    sed -e "s/\${ORG}/$1/" \
        -e "s/\${P0PORT}/$2/" \
        -e "s/\${CAPORT}/$3/" \
        -e "s#\${PEERPEM}#$PP#" \
        -e "s#\${CAPEM}#$CP#" \
        organizations/ccp-template.json
}

function yaml_ccp {
    local PP=$(one_line_pem $4)
    local CP=$(one_line_pem $5)
    sed -e "s/\${ORG}/$1/" \
        -e "s/\${P0PORT}/$2/" \
        -e "s/\${CAPORT}/$3/" \
        -e "s#\${PEERPEM}#$PP#" \
        -e "s#\${CAPEM}#$CP#" \
        organizations/ccp-template.yaml | sed -e $'s/\\\\n/\\\n          /g'
}

ORG=company
P0PORT=7051
CAPORT=7054
PEERPEM=organizations/peerOrganizations/company.enset.com/tlsca/tlsca.company.enset.com-cert.pem
CAPEM=organizations/peerOrganizations/company.enset.com/ca/ca.company.enset.com-cert.pem

echo "$(json_ccp $ORG $P0PORT $CAPORT $PEERPEM $CAPEM)" > organizations/peerOrganizations/company.enset.com/connection-company.json
echo "$(yaml_ccp $ORG $P0PORT $CAPORT $PEERPEM $CAPEM)" > organizations/peerOrganizations/company.enset.com/connection-company.yaml

ORG=customer
P0PORT=8051
CAPORT=8054
PEERPEM=organizations/peerOrganizations/customer.enset.com/tlsca/tlsca.customer.enset.com-cert.pem
CAPEM=organizations/peerOrganizations/customer.enset.com/ca/ca.customer.enset.com-cert.pem

echo "$(json_ccp $ORG $P0PORT $CAPORT $PEERPEM $CAPEM)" > organizations/peerOrganizations/customer.enset.com/connection-customer.json
echo "$(yaml_ccp $ORG $P0PORT $CAPORT $PEERPEM $CAPEM)" > organizations/peerOrganizations/customer.enset.com/connection-customer.yaml

ORG=suppliera
P0PORT=9051
CAPORT=9054
PEERPEM=organizations/peerOrganizations/suppliera.enset.com/tlsca/tlsca.suppliera.enset.com-cert.pem
CAPEM=organizations/peerOrganizations/suppliera.enset.com/ca/ca.suppliera.enset.com-cert.pem

echo "$(json_ccp $ORG $P0PORT $CAPORT $PEERPEM $CAPEM)" > organizations/peerOrganizations/suppliera.enset.com/connection-suppliera.json
echo "$(yaml_ccp $ORG $P0PORT $CAPORT $PEERPEM $CAPEM)" > organizations/peerOrganizations/suppliera.enset.com/connection-suppliera.yaml

ORG=supplierb
P0PORT=10051
CAPORT=10054
PEERPEM=organizations/peerOrganizations/supplierb.enset.com/tlsca/tlsca.supplierb.enset.com-cert.pem
CAPEM=organizations/peerOrganizations/supplierb.enset.com/ca/ca.supplierb.enset.com-cert.pem

echo "$(json_ccp $ORG $P0PORT $CAPORT $PEERPEM $CAPEM)" > organizations/peerOrganizations/supplierb.enset.com/connection-supplierb.json
echo "$(yaml_ccp $ORG $P0PORT $CAPORT $PEERPEM $CAPEM)" > organizations/peerOrganizations/supplierb.enset.com/connection-supplierb.yaml