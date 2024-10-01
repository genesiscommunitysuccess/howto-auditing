package global.genesis.trades;

import global.genesis.gen.dao.Trade;
import global.genesis.gen.dao.enums.howto_auditing.trade.Side;
import global.genesis.message.core.event.Event;
import global.genesis.message.core.event.EventReply;
import global.genesis.testsupport.AbstractGenesisTestSupport;
import global.genesis.testsupport.EventResponse;
import global.genesis.testsupport.GenesisTestConfig;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JavaTradesTest extends AbstractGenesisTestSupport<EventResponse> {
    public JavaTradesTest() {
        super(GenesisTestConfig.builder()
                .setPackageNames(List.of("global.genesis.eventhandler", "global.genesis.trades"))
                .setGenesisHome("/genesisHome/")
                .setParser(EventResponse.Companion)
                .build()
        );
    }

    @Test
    public void testTradeInsert() throws InterruptedException {
        Trade trade = Trade.builder()
                .setTradeId("1")
                .setSide(Side.BUY)
                .setNotional(1.123)
                .setCountryName("ESP")
                .setCustomerId("1")
                .setSourceCurrency("USD")
                .setTargetCurrency("EUR")
                .setCustomerName("JohnDoe")
                .setVersion(1)
                .setSettlementDate(DateTime.now())
                .setRate(1.15)
                .build();
        Event event = new Event(trade, "EVENT_TRADEJ_INSERT", "JohnDoe");
        EventReply reply = getMessageClient().request(event, EventReply.class).blockingGet();
        assertEquals(new EventReply.EventAck(List.of(Map.of("TRADE_ID", trade.getTradeId()))), reply);
        Trade result = getRxDb().entityDb().get(Trade.byId("1")).blockingGet();
        assertNotNull(result);
    }
}
