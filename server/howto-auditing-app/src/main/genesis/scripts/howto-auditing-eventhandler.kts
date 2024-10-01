/**
  * This file defines the event handler APIs. These APIs (modeled after CQRS
  * commands) allow callers to manipulate the data in the database. By default,
  * insert, update and delete event handlers (or commands) have been created.
  * These result in the data being written to the database and messages to be
  * published for the rest of the platform to by notified.
  * 
  * Custom event handlers may be added to extend the functionality of the
  * application as well as custom logic added to existing event handlers.
  *
  * The following objects are visible in each eventhandler
  * `event.details` which holds the entity that this event handler is acting upon
  * `entityDb` which is database object used to perform INSERT, MODIFY and UPDATE the records
  * Full documentation on event handler may be found here >> https://docs.genesis.global/docs/develop/server-capabilities/core-business-logic-event-handler/

 */

eventHandler {
  // These GPAL event handlers provide auto-auditing because the entity is auditable.
  // These event handlers are also marked as transactional which ensures that the audit record is inserted along
  // with the entity record. See here for more details >> https://docs.genesis.global/docs/develop/server-capabilities/core-business-logic-event-handler/#eventhandler
  eventHandler<Trade>("TRADE_INSERT", transactional = true) {
    onCommit { event ->
      val details = event.details
      val insertedRow = entityDb.insert(details)
      // return an ack response which contains a list of record IDs
      ack(listOf(mapOf(
        "TRADE_ID" to insertedRow.record.tradeId,
      )))
    }
  }
  eventHandler<Trade>("TRADE_MODIFY", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.modify(details)
      ack()
    }
  }
  eventHandler<Trade.ById>("TRADE_DELETE", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.delete(details)
      ack()
    }
  }
}
