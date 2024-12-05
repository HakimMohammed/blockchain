# HyperLedger Fabric: Technical Terms

## LEDGER: Stored Data
we will be storing two things in the blockchain
  -  World State ( Current State: what do we "have" now )
  -  Transaaction Log ( History: everything that happend before )
      -  this data is synced between peers ( whenever a peer updated his ledger, the other peers need to add that transaction to their ledger )
   
## CTF: Crash Fault Tolerance
guaranties to withstand system failures, such as crashes, network partitioning. 
  -  **HyberLedger Fabric** uses **Kafka** for that
  
## PEER: a user in the network
a user needs to be "certified" ot get identified as a peer ( something to prove that u are who u saying u are , b7al carte nationale / passport )
  -  every transaction a peer does need to be "signed" by it's certificate to be valid
  -  every peer has its own copy of the LEGDER

## P2P : Peer to Peer network

## CERTIFICATE AUTHORITY
it will be the one to "certify" the peers ( make them valid/trusted users )

## SMART CONTRACT == CHAINCODE
with it we can initiate a transaction

## MEMBERSHIP SERVICE PROVIDER
specifies which peers are allowed to be a part of the network and which aren't ( so getting a certificate from a CA [Certificate Authority] doesnt necessary mean u are a part of the network)
it also specefied the rules of a network

## Block
a block is a transaction, so every transaction that's being done == a block needs to be add to the ledger

## Orderer Peer: ( Writer )
It's the one that writes/add blocks to the peer's ledgers

## Channel
it's like a group, only the peers in a channel can see it's ledger . 
  -  Multiple channels can exist in the same blockchain network

## Application
  -  a user is outside the network he connect to it through a **Smart Contract** which communicates directly with the **Peer** who can acces a **channel** directly

## Endorsement
when a Peer allows an **Application** to do a transaction in a **Channel** ( Note: allowign a transaction doesnt mean it's done, it just means it could happen )

## Transaction
  -  **Application** send a transaction request to a **Peer**
  -  **Peer** send a confirmation message ( endorssement / license ) to the **Application** allowing it to do the transaction
  -  **Application** sends the transaction with the **endorssement** to the **Orderer Peer**
  -  **Orderer Peer** verifies the **endorssemet**, then allows the tranasaction to happen. which means a **block** needs to be added to the **ledger**
  -  **Orderer Peer** notifies the **Peer** in the network that a new block is present and u should add it to his **ledger**
