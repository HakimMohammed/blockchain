# HyperLedger Fabric: Technical Terms

## LEDGER: Stored Data
we will be storing two things in the blockchain
  -  World State ( Current State: what do we "have" now )
  -  Transaaction Log ( History: everything that happend before )
      -  this data is synced between peers ( whenever a peer updated his ledger, the other peers need to add that transaction to their ledger )
      -  for now wa9ila a big wa9ila this done by CFT ( Crash Fault Tolerance )
  
## PEER: a user in the network
a user needs to be "certified" ot get identified as a peer ( something to prove that u are who u saying u are , b7al carte nationale / passport )
  -  every transaction a peer does need to be "signed" by it's certificate to be valid
  -  every peer has its own copy of the LEGDER

## P2P : Peer to Peer network

## CERTIFICATE AUTHORITY
it will be the one to "certify" the peers ( make them valid/trusted users )

## SMART CONTRACT == CHAINCODE
with it we can initiate a transaction
  -  They are the ones who update the peer's ledger

## MEMBERSHIP SERVICE PROVIDER
specifies which peers are allowed to be a part of the network and which aren't ( so getting a certificate from a CA [Certificate Authority] doesnt necessary mean u are a part of the network)
it also specefied the rules of a network
