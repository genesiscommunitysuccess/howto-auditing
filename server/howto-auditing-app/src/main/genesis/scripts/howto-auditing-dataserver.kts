/**
  * This file defines the data server queries for the application. Data server
  * will load the data defined here and expose APIs for the clients including
  * Genesis UI Components to present this data as well as keep it up to date as
  * the data set changes underneath.
  *
  * Data server queries also allow for the definition of dynamic columns as well
  * as rich access controls definitions.

  * Full documentation on dataserver may be found here >> https://docs.genesis.global/docs/develop/server-capabilities/real-time-queries-data-server/

 */

dataServer {
  query("ALL_TRADES", TRADE) {
    fields {
      TRADE_ID
      CUSTOMER_ID
      CUSTOMER_NAME
      COUNTRY_NAME
      NOTIONAL
      RATE
      SETTLEMENT_DATE
      SIDE
      SOURCE_CURRENCY
      TARGET_CURRENCY
      VERSION
    }
  }
}
