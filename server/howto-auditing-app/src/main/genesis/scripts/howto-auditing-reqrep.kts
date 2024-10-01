/**
  * This file defines request-replies of the application. 
  * Request-Replies provide snapshot data from a table or view in response to a request from the front end.
  * Once the response is received, the transaction is over (unlike a Data Server, which stays connected to the client and pushes updates)

  * Full documentation on request server may be found here >> https://docs.genesis.global/docs/develop/server-capabilities/snapshot-queries-request-server/

 */

requestReplies {
  requestReply(TRADE)
  //Request reply onto the Audit table. You could also create a view onto the audit table and join to referenced tables
  //and use this in your request reply if desired.
  requestReply(TRADE_AUDIT)
}
