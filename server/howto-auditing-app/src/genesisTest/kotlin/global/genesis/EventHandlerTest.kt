/**
  * This file tests the event handler APIs. 
  
  * The events: INSERT, MODIFY and DELETE are tested
  * Full documentation on event handler tests may be found here >> https://docs.genesis.global/docs/develop/server-capabilities/core-business-logic-event-handler/#integration-testing

 */

import global.genesis.db.rx.entity.multi.AsyncEntityDb
import global.genesis.gen.dao.Trade
import global.genesis.gen.dao.enums.howto_auditing.trade.Side
import global.genesis.message.core.event.EventReply
import global.genesis.testsupport.client.eventhandler.EventClientSync
import global.genesis.testsupport.jupiter.GenesisJunit
import global.genesis.testsupport.jupiter.TestScriptFile
import global.genesis.testsupport.jupiter.assertedCast
import jakarta.inject.Inject
import kotlin.String
import kotlin.Unit
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.joda.time.DateTime.now
import org.joda.time.DateTime.parse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(GenesisJunit::class)
@TestScriptFile("howto-auditing-eventhandler.kts")
class EventHandlerTest {
  @Inject
  lateinit var client: EventClientSync

  @Inject
  lateinit var entityDb: AsyncEntityDb

  private val adminUser: String = "admin"

  @Test
  fun `test insert TRADE`(): Unit = runBlocking {
    val result = client.sendEvent(
      details = Trade {
        countryName = "1"
        notional = 0.1
        customerId = "1"
        rate = 0.1
        version = 1
        side = Side.BUY
        sourceCurrency = "1"
        customerName = "1"
        settlementDate = now()
        targetCurrency = "1"
      },
      messageType = "EVENT_TRADE_INSERT",
      userName = adminUser
    )
    result.assertedCast<EventReply.EventAck>()
    val trade = entityDb.getBulk<Trade>().toList()
    assert(trade.isNotEmpty())
  }

  @Test
  fun `test modify TRADE`(): Unit = runBlocking {
    val result = entityDb.insert(
      Trade {
        countryName = "1"
        notional = 0.1
        customerId = "1"
        rate = 0.1
        version = 1
        side = Side.BUY
        sourceCurrency = "1"
        customerName = "1"
        settlementDate = now()
        targetCurrency = "1"
      }
    )
    val tradeIdValue = result.record.tradeId
    val modifyResult = client.sendEvent(
      details = Trade {
        tradeId = tradeIdValue
        countryName = "2"
        notional = 0.2
        customerId = "2"
        rate = 0.2
        version = 2
        side = Side.SELL
        sourceCurrency = "2"
        customerName = "2"
        settlementDate = parse("2024-01-01T00:00:00.000Z")
        targetCurrency = "2"
      },
      messageType = "EVENT_TRADE_MODIFY",
      userName = adminUser
    )
    modifyResult.assertedCast<EventReply.EventAck>()
    val modifiedRecord = entityDb.get(Trade.ById(tradeIdValue))
    assert(modifiedRecord?.countryName == "2")
    assert(modifiedRecord?.notional == 0.2)
    assert(modifiedRecord?.customerId == "2")
    assert(modifiedRecord?.rate == 0.2)
    assert(modifiedRecord?.version == 2)
    assert(modifiedRecord?.side == Side.SELL)
    assert(modifiedRecord?.sourceCurrency == "2")
    assert(modifiedRecord?.customerName == "2")
    assert(parse("2024-01-01T00:00:00.000Z").compareTo(modifiedRecord?.settlementDate) == 0)
    assert(modifiedRecord?.targetCurrency == "2")
  }

  @Test
  fun `test delete TRADE`(): Unit = runBlocking {
    val result = entityDb.insert(
      Trade {
        countryName = "1"
        notional = 0.1
        customerId = "1"
        rate = 0.1
        version = 1
        side = Side.BUY
        sourceCurrency = "1"
        customerName = "1"
        settlementDate = now()
        targetCurrency = "1"
      }
    )
    val numRecordsBefore = entityDb.getBulk<Trade>().toList().size
    val tradeIdValue = result.record.tradeId
    val deleteResult = client.sendEvent(
      details = Trade.ById(tradeIdValue),
      messageType = "EVENT_TRADE_DELETE",
      userName = adminUser
    )
    deleteResult.assertedCast<EventReply.EventAck>()
    val numRecordsAfter = entityDb.getBulk<Trade>().toList().size
    assert(numRecordsAfter == numRecordsBefore - 1)
  }
}
