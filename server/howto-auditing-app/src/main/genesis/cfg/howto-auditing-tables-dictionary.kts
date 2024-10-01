/**
  * This file defines the entities (or tables) for the application.  
  * Entities aggregation a selection of the universe of fields defined in 
  * {app-name}-fields-dictionary.kts file into a business entity.  
  *
  * Note: indices defined here control the APIs available to the developer.
  * For example, if an entity requires lookup APIs by one or more of its attributes, 
  * be sure to define either a unique or non-unique index.

  * Full documentation on tables may be found here >> https://docs.genesis.global/docs/develop/server-capabilities/data-model/

 */

tables {
  // Table with auditing specified
  table(name = "TRADE", id = 11_000, audit = details(id = 11_500, sequence = "TA")) {
    field("TRADE_ID", STRING).sequence("TD")
    field("COUNTRY_NAME", STRING).notNull()
    field("CUSTOMER_ID", STRING).notNull()
    field("CUSTOMER_NAME", STRING).notNull()
    field("NOTIONAL", DOUBLE).notNull()
    field("RATE", DOUBLE).notNull()
    field("SETTLEMENT_DATE", DATE).notNull()
    field("SIDE", ENUM("SELL","BUY")).default("BUY").notNull()
    field("SOURCE_CURRENCY", STRING).notNull()
    field("TARGET_CURRENCY", STRING).notNull()
    field("VERSION", INT).notNull()

    primaryKey("TRADE_ID")

  }
}
