package global.genesis.trades;

import com.google.inject.Inject;
import global.genesis.commons.annotation.Module;
import global.genesis.db.rx.entity.multi.RxEntityDb;
import global.genesis.eventhandler.typed.rx3.Rx3EventHandler;
import global.genesis.gen.dao.Trade;
import global.genesis.message.core.event.Event;
import global.genesis.message.core.event.EventReply;
import io.reactivex.rxjava3.core.Single;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

@Module
public class TradeJavaEventHandler implements Rx3EventHandler<Trade, EventReply> {

    private final RxEntityDb entityDb;

    //Inject the RxEntityDb object
    @Inject
    public TradeJavaEventHandler(RxEntityDb entityDb) {
        this.entityDb = entityDb;
    }

    @Nullable
    @Override
    public String messageType() {
        return "TRADEJ_INSERT";
    }

    @Override
    public Single<EventReply> process(Event<Trade> tradeEvent) {
        Trade trade = tradeEvent.getDetails();
        //Create the auditEntityDb object to later use to write the object
        var auditEntityDb = entityDb.audited(
                tradeEvent.getUserName(),
                tradeEvent.getMessageType(),
                "New Trade inserted by " + tradeEvent.getUserName()
        );
        //Use the auditEntityDb to insert as opposed to entityDb, this will ensure the audit records are written.
        return auditEntityDb.insert(trade).flatMap(result -> ack(this, List.of(Map.of("TRADE_ID", result.getRecord().getTradeId()))));
    }
}
